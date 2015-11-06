package client.chat.android.mobile.chatclient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jivesoftware.smackx.muc.HostedRoom;

import java.util.ArrayList;
import java.util.List;

import client.chat.android.mobile.chatclient.R;
import client.chat.android.mobile.chatclient.data.ChatServiceData;

/**
 * Created by 潇洒哥 on 15/10/1.
 */
public class ChatServiceListAdapter extends ParentAdapter{
    private List<ChatServiceData> mChatServiceList;
    public ChatServiceListAdapter(Context context, List<HostedRoom> chatServiceList) {
        super(context);
        mChatServiceList = new ArrayList<ChatServiceData>();
        for(int i = 0; i < chatServiceList.size();i++)
        {
            String jid = chatServiceList.get(i).getJid();
            if(!jid.startsWith("conference"))
            {
                continue;
            }
            ChatServiceData data = new ChatServiceData();
            data.hostedRoom = chatServiceList.get(i);
            mChatServiceList.add(data);
        }

    }

    @Override
    public int getCount() {
        return mChatServiceList.size();
    }

    @Override
    public Object getItem(int i) {
        return (i < mChatServiceList.size()) ? mChatServiceList.get(i):null;
    }
    public String getJID(int i)
    {
        return (i < mChatServiceList.size())?mChatServiceList.get(i).hostedRoom.getJid():null;
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.chat_service_item, null);
        }
        TextView chatService = (TextView)view.findViewById(R.id.textview_chat_service_item);
        HostedRoom hostedRoom = mChatServiceList.get(i).hostedRoom;
        chatService.setText(hostedRoom.getJid());
        return view;
    }
}
