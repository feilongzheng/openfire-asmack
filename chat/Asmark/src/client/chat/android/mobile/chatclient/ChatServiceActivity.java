package client.chat.android.mobile.chatclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import client.chat.android.mobile.chatclient.adapter.ChatServiceListAdapter;
import client.chat.android.mobile.chatclient.common.Const;


public class ChatServiceActivity extends ParentActivity implements AdapterView.OnItemClickListener,Const
{
    private ListView mListViewChatServices;
    private ChatServiceListAdapter mChatServiceListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_service);
        mListViewChatServices = (ListView)findViewById(R.id.listview_chat_services);

        mChatServiceListAdapter = new ChatServiceListAdapter(this, getHostedRooms());
        mListViewChatServices.setAdapter(mChatServiceListAdapter);
        mListViewChatServices.setOnItemClickListener(this);
    }
    private List<HostedRoom> getHostedRooms()
    {
        Collection<HostedRoom> hostedRooms;
        List<HostedRoom> chatServices = new ArrayList<HostedRoom>();
        try
        {
            hostedRooms = MultiUserChat.getHostedRooms(mXMPPConnection,mXMPPConnection.getServiceName());
            for(HostedRoom entry:hostedRooms)
            {
                chatServices.add(entry);
            }
        }
        catch (Exception e)
        {

        }
        return chatServices;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String jid = mChatServiceListAdapter.getJID(i);
        Intent intent = new Intent(this, MultiUserChatRoomsActivity.class);
        intent.putExtra(JID, jid);
        startActivity(intent);
    }
}
