package client.chat.android.mobile.chatclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;

import client.chat.android.mobile.chatclient.adapter.ChatListAdapter;
import client.chat.android.mobile.chatclient.common.Const;
import client.chat.android.mobile.chatclient.common.Util;
import client.chat.android.mobile.chatclient.data.ChatData;
import client.chat.android.mobile.chatclient.data.DataWarehouse;
import client.chat.android.mobile.chatclient.data.LoginData;


public class ChatActivity extends ParentActivity implements PacketListener,Const {

    protected String mUser;   //  对方的用户名
    protected String mName;   //  对方的别名
    private String mServiceName;   //  服务名

    private LoginData mLoginData;
    private EditText mEditTextChatText;
    protected ListView mListViewChatList;
    protected ChatListAdapter mChatListAdapter;
    private PacketFilter mFilter = new MessageTypeFilter(Message.Type.chat);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mLoginData = DataWarehouse.getGlobalData(this).loginData;
        mUser = getIntent().getStringExtra("user");
        mName = getIntent().getStringExtra("name");
        mServiceName = mXMPPConnection.getServiceName();

        mEditTextChatText = (EditText) findViewById(R.id.edittext_chat_text);
        mListViewChatList = (ListView) findViewById(R.id.listview_ChatList);

        mXMPPConnection.addPacketListener(this, mFilter);

        mChatListAdapter = new ChatListAdapter(this);
        mListViewChatList.setAdapter(mChatListAdapter);
        //  隐藏分隔条
        mListViewChatList.setDivider(null);

        String body = getIntent().getStringExtra("body");
        if(body != null)
        {
            ChatData item = new ChatData();
            item.text = body;
            item.user = mUser;
            item.name = mName;
            mChatListAdapter.addItem(item);
        }

        DataWarehouse.getGlobalData(this).chatUsers.add(mUser);
    }

    private static class MyHandler extends Handler {
        private Context mContext;
        private ChatActivity mChatActivity;

        public MyHandler(Context context) {
            mContext = context;
            mChatActivity = (ChatActivity) context;
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            Message message = (Message) msg.obj;
            if(Util.extractUserFromChat(message.getFrom()).equals(mChatActivity.mUser)) {
                String body = message.getBody();
                if (!"".equals(body) && body != null) {
                    //Toast.makeText(mContext, body, Toast.LENGTH_LONG).show();
                    ChatData item = new ChatData();
                    item.text = body;
                    item.user = mChatActivity.mUser;
                    item.name = mChatActivity.mName;
                    mChatActivity.mChatListAdapter.addItem(item);
                    mChatActivity.mListViewChatList.setSelection(mChatActivity.mListViewChatList.getAdapter().getCount() - 1);


                }
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    @Override
    public void processPacket(Packet packet) throws SmackException.NotConnectedException {
        android.os.Message msg = new android.os.Message();
        msg.obj = packet;
        mHandler.sendMessage(msg);
    }

    public void onClick_Send(View view) {
        try {
            String text = mEditTextChatText.getText().toString().trim();
            if (!"".equals(text)) {
                //  账号@服务名
                Message msg = new Message(mUser + "@" + mServiceName, Message.Type.chat);
                msg.setBody(text);

                DeliveryReceiptManager.addDeliveryReceiptRequest(msg);
                mXMPPConnection.sendPacket(msg);
                mEditTextChatText.setText("");

                //  显示发送聊天信息的记录
                ChatData item = new ChatData();
                item.text = text;
                item.name = mLoginData.username;
                item.user = mLoginData.username;
                item.isOwner = true;

                mChatListAdapter.addItem(item);
                mListViewChatList.setSelection(mListViewChatList.getAdapter().getCount() - 1);


            } else {
                Toast.makeText(this, "请输入要发送的文本.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mXMPPConnection.removePacketListener(this);
        DataWarehouse.getGlobalData(this).chatUsers.remove(mUser);
    }
    public void onClick_Face(View view)
    {
        Intent intent = new Intent(this, FaceActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case 1:
                int faceId = data.getIntExtra(KEY_FACE_ID, -1);
                if(faceId != -1)
                {
                    String faceResName = FACE_PREFIX + faceId;
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                            Util.getResourceIDFromName(R.drawable.class, faceResName));
                    Matrix matrix = new Matrix();
                    matrix.postScale(0.6f, 0.6f);
                    //  将表情图像缩小到原来的60%
                    Bitmap smallBitmap = Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(),
                             bitmap.getHeight(),matrix,true);
                    ImageSpan imageSpan = new ImageSpan(this, smallBitmap);
                    String faceText = FACE_TEXT_PREFIX + faceId + FACE_TEXT_SUFFIX;
                    SpannableString spannableString = new SpannableString(faceText);
                    spannableString.setSpan(imageSpan, 0, faceText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    mEditTextChatText.getText().insert(mEditTextChatText.getSelectionStart(), spannableString);

                }
                break;
        }
    }
}
