package client.chat.android.mobile.chatclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.util.Set;

import client.chat.android.mobile.chatclient.adapter.FriendListAdapter;
import client.chat.android.mobile.chatclient.common.Const;
import client.chat.android.mobile.chatclient.common.Storage;
import client.chat.android.mobile.chatclient.common.Util;
import client.chat.android.mobile.chatclient.data.DataWarehouse;
import client.chat.android.mobile.chatclient.data.LoginData;
import client.chat.android.mobile.chatclient.data.UserData;


public class MainActivity extends ParentActivity implements PacketListener,  AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private ListView mFriendList;
    private LoginData mLoginData;
    protected Set<String> mChatUsers;
    private PacketFilter mFilter = new MessageTypeFilter(Message.Type.chat);
    private int mCurrentPosition = -1;
    public static FriendListAdapter mFriendListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginData = DataWarehouse.getGlobalData(this).loginData;
        mFriendList = (ListView) findViewById(R.id.listview_friends);

        mFriendListAdapter = new FriendListAdapter(this, mXMPPConnection.getRoster().getEntries());
        mFriendList.setAdapter(mFriendListAdapter);

        mChatUsers = DataWarehouse.getGlobalData(this).chatUsers;
        mFriendList.setOnItemClickListener(this);
        mFriendList.setOnItemLongClickListener(this);
        mXMPPConnection.addPacketListener(this, mFilter);

        registerForContextMenu(mFriendList);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Intent intent = null;
        switch (id)
        {
            case R.id.menu_item_remove_friend:
                if(mCurrentPosition > -1)
                {
                    mFriendListAdapter.removeUserData(mCurrentPosition);
                }
                break;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        getMenuInflater().inflate(R.menu.friend_list_context_menu, menu);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        mCurrentPosition = i;
        return false;
    }

    private static class MyHandler extends Handler {
        private MainActivity mMainActivity;

        public MyHandler(Context context) {
            mMainActivity = (MainActivity) context;
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            Message message = (Message) msg.obj;
            String user = Util.extractUserFromChat(message.getFrom());
            if (!mMainActivity.mChatUsers.contains(user)) {
                // 查找昵称
                String name = mFriendListAdapter.findName(user);
                String body = message.getBody();
                if (body == null)
                    return;
                mMainActivity.mChatUsers.add(user);
                Intent intent = new Intent(mMainActivity, ChatActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("name", name);
                intent.putExtra("body", body);
                mMainActivity.startActivity(intent);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ChatActivity.class);
        String user = mFriendListAdapter.getUser(i);
        String name = mFriendListAdapter.getName(i);
        intent.putExtra("user", user);
        intent.putExtra("name", name);

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    String name = data.getStringExtra("name");
                    String user = data.getStringExtra("user");

                    UserData userData = new UserData(name, user);
                    mFriendListAdapter.addUserData(userData);
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.add_friend:
                intent = new Intent(this, AddFriendActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.chat_services:
                intent = new Intent(this, ChatServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_logout:
                new AlertDialog.Builder(this).setTitle("注销").setMessage("确定要注销当前用户吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Storage.putBoolean(MainActivity.this, Const.KEY_AUTO_LOGIN, false);
                                        try {
                                            mXMPPConnection.disconnect();
                                        } catch (Exception e) {

                                        }
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                        ).setNegativeButton("取消", null).show();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mXMPPConnection.removePacketListener(this);
    }
}
