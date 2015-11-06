package client.chat.android.mobile.chatclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import client.chat.android.mobile.chatclient.adapter.ChatListAdapter;
import client.chat.android.mobile.chatclient.common.Const;
import client.chat.android.mobile.chatclient.common.Util;
import client.chat.android.mobile.chatclient.data.ChatData;
import client.chat.android.mobile.chatclient.data.DataWarehouse;
import client.chat.android.mobile.chatclient.data.LoginData;


public class MultiUserChatRoomActivity extends ParentActivity implements Const, PacketListener
{


    private ChatListAdapter mChatListAdapter;

    private ChatManager mChatManager;
    private LoginData mLoginData;

    private EditText mEditTextChatText;
    private ListView mListViewChatList;
    private String mChatServiceJID;
    private String mChatRoomName;
    private MultiUserChat mMultiUserChat;
    private PacketFilter mFilter = new MessageTypeFilter(Message.Type.groupchat);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_user_chat_room);

        mLoginData = DataWarehouse.getGlobalData(this).loginData;

        mEditTextChatText = (EditText) findViewById(R.id.edittext_chat_text);
        mListViewChatList = (ListView) findViewById(R.id.listview_ChatList);

        mXMPPConnection.addPacketListener(this, mFilter);
        mChatServiceJID = getIntent().getStringExtra(JID);
        mChatRoomName = getIntent().getStringExtra(CHAT_ROOM_NAME);

        mChatListAdapter = new ChatListAdapter(this);

        mListViewChatList.setAdapter(mChatListAdapter);
        mListViewChatList.setDivider(null);
        try
        {
            mMultiUserChat = new MultiUserChat(mXMPPConnection, mChatRoomName + "@" + mChatServiceJID);
            mMultiUserChat.join(DataWarehouse.getGlobalData(this).loginData.username);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "聊天室加入失败", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case 1:
                int faceId = data.getIntExtra(KEY_FACE_ID, -1);
                if (faceId != -1)
                {
                    String faceResName = FACE_PREFIX + faceId;

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                            Util.getResourceIDFromName(R.drawable.class, faceResName));

                    Matrix matrix = new Matrix();
                    matrix.postScale(0.6f, 0.6f);
                    Bitmap smallBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                            matrix, true);
                    ImageSpan imageSpan = new ImageSpan(this, smallBitmap);
                    String faceText = FACE_TEXT_PREFIX + faceId + FACE_TEXT_SUFFIX;
                    SpannableString spannableString = new SpannableString(faceText);

                    spannableString.setSpan(imageSpan, 0, faceText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                    mEditTextChatText.getText().insert(mEditTextChatText.getSelectionStart(), spannableString);


                }
                break;
        }
    }
    public void onClick_Face(View view)
    {
        Intent intent = new Intent(this, FaceActivity.class);
        startActivityForResult(intent, 1);
    }
    private Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(android.os.Message msg)
        {
            Message message = (Message) msg.obj;
            String body = message.getBody();
            if (body != null)
            {
                String from = Util.extractUserFromChatGroup(message.getFrom());
                if(mLoginData.username.equals(from))
                {
                    return true;
                }

                ChatData item = new ChatData();

                item.text = body;
                item.name = MainActivity.mFriendListAdapter.findName(from);

                mChatListAdapter.addItem(item);
                mListViewChatList.setSelection(mListViewChatList.getAdapter().getCount() - 1);

            }
            return true;
        }
    });

    @Override
    public void processPacket(Packet packet) throws SmackException.NotConnectedException
    {
        Message message = (Message) packet;
        android.os.Message msg = new android.os.Message();
        msg.obj = message;
        mHandler.sendMessage(msg);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mXMPPConnection.removePacketListener(this);
        try
        {
            mMultiUserChat.leave();
        }
        catch (Exception e)
        {

        }
    }

    public void onClick_Send(View view)
    {
        try
        {
            String text = mEditTextChatText.getText().toString().trim();
            if (!"".equals(text))
            {


                mMultiUserChat.sendMessage(text);

                mEditTextChatText.setText("");

                ChatData item = new ChatData();
                item.text = text;
                item.name = mLoginData.username;
                item.user = mLoginData.username;
                item.isOwner = true;
                mChatListAdapter.addItem(item);
                mListViewChatList.setSelection(mListViewChatList.getAdapter().getCount() - 1);
            }
            else
            {
                Toast.makeText(this, "请输入要发送的文本.", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
