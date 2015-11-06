package client.chat.android.mobile.chatclient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jivesoftware.smackx.disco.packet.DiscoverItems;

import java.util.ArrayList;
import java.util.List;

import client.chat.android.mobile.chatclient.R;
import client.chat.android.mobile.chatclient.data.ChatRoomData;

/**
 * Created by 潇洒哥 on 15/10/1.
 */
public class MultiUserChatRoomListAdapter extends  ParentAdapter{
    private List<ChatRoomData> mChatRoomList;

    public MultiUserChatRoomListAdapter(Context context, List<DiscoverItems.Item> chatRoomList)
    {
        super(context);
        mChatRoomList = new ArrayList<ChatRoomData>();
        for(int i = 0; i < chatRoomList.size();i++)
        {
            ChatRoomData data = new ChatRoomData();
            data.item = chatRoomList.get(i);
            mChatRoomList.add(data);
        }

    }

    @Override
    public int getCount() {
        return mChatRoomList.size();
    }

    @Override
    public Object getItem(int i) {
        return super.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }
    public DiscoverItems.Item getChatRoom(int i)
    {
        return (i < mChatRoomList.size())?mChatRoomList.get(i).item:null;
    }
    //  刷新聊天室列表
    public void updateChatRoom(List<DiscoverItems.Item> chatRoomList)
    {
        mChatRoomList.clear();
        for(int i = 0; i < chatRoomList.size();i++)
        {
            ChatRoomData data = new ChatRoomData();
            data.item = chatRoomList.get(i);
            mChatRoomList.add(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.multi_user_chat_room_item, null);

        }
        TextView chatRoom = (TextView)view.findViewById(R.id.textview_chat_room_item);
        DiscoverItems.Item item = mChatRoomList.get(i).item;
        chatRoom.setText(item.getName());
        return view;
    }
}
