package client.chat.android.mobile.chatclient.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import client.chat.android.mobile.chatclient.R;
import client.chat.android.mobile.chatclient.common.Util;
import client.chat.android.mobile.chatclient.data.ChatData;

/**
 * Created by 潇洒哥 on 15/10/01.
 */
public class ChatListAdapter extends BaseAdapter{
    private Context mContext;
    private List<ChatData> mChatDataList;
    private LayoutInflater mLayoutInflater;

    public ChatListAdapter(Context context)
    {
        mContext = context;
        mChatDataList = new ArrayList<ChatData>();
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mChatDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return (i < mChatDataList.size())?mChatDataList.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addItem(ChatData item)
    {
        mChatDataList.add(item);
        notifyDataSetChanged();
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.chat_list_item, null);
        }

        View left = null;
        View right = null;
        if(view.getTag(R.id.linearlayout_chat_left) == null)
        {
            left = view.findViewById(R.id.linearlayout_chat_left);
            right = view.findViewById(R.id.linearlayout_chat_right);
            view.setTag(R.id.linearlayout_chat_left, left);
            view.setTag(R.id.linearlayout_chat_right, right);
        }
        else
        {
            left = (View)view.getTag(R.id.linearlayout_chat_left);
            right = (View)view.getTag(R.id.linearlayout_chat_right);
        }

        TextView leftChatText = (TextView)view.findViewById(R.id.textview_chat_text_left);
        TextView rightChatText = (TextView)view.findViewById(R.id.textview_chat_text_right);

        TextView leftUserName = (TextView)view.findViewById(R.id.textview_left_user_name);
        TextView rightUserName = (TextView)view.findViewById(R.id.textview_right_user_name);

        ImageView leftHeadPortrait = (ImageView)view.findViewById(R.id.imageview_chat_left_head_portrait);
        ImageView rightHeadPortait = (ImageView)view.findViewById(R.id.imageview_chat_right_head_portrait);

        left.setVisibility(View.GONE);
        right.setVisibility(View.GONE);

        // 获取要显示的数据
        ChatData chatData = mChatDataList.get(i);

        TextView currentChatText;
        TextView currentUserName;
        if(chatData.isOwner)
        {
            right.setVisibility(View.VISIBLE);
            currentChatText = rightChatText;
            currentUserName = rightUserName;
        }
        else
        {
            left.setVisibility(View.VISIBLE);
            currentChatText = leftChatText;
            currentUserName = leftUserName;
        }

        currentChatText.setText(chatData.text);

        currentUserName.setText(chatData.name);
        Util.updateFacesForTextView(mContext, currentChatText);
        return view;
    }
}
