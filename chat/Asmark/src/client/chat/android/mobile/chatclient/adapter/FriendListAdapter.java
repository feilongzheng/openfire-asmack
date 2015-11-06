package client.chat.android.mobile.chatclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import client.chat.android.mobile.chatclient.R;
import client.chat.android.mobile.chatclient.data.DataWarehouse;
import client.chat.android.mobile.chatclient.data.UserData;

/**
 * Created by 潇洒哥 on 15/5/15.
 */
public class FriendListAdapter extends BaseAdapter
{

    private List<UserData> mUsers;
    private Map<String, String> mUserMap;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public FriendListAdapter(Context context, Collection<RosterEntry> entries)
    {
        mContext = context;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mUsers = new ArrayList<UserData>();
        mUserMap = new HashMap<String, String>();

        if(entries != null)
        {
            Iterator<RosterEntry> iterator = entries.iterator();
            while(iterator.hasNext())
            {
                RosterEntry entry = iterator.next();
                if(entry.getUser().indexOf("@") == -1)
                {
                    UserData userData = new UserData(entry.getName(), entry.getUser());
                    mUserMap.put(entry.getUser(), entry.getName());
                    mUsers.add(userData);
                }
            }
        }
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return mUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    //  获取用户昵称
    public String getName(int position)
    {
        return mUsers.get(position).name;

    }
    //  获取登录名
    public String getUser(int position)
    {
        return mUsers.get(position).user;

    }

    // 根据登录名获取昵称
    public String findName(String user)
    {
        String name = mUserMap.get(user);
        if(name == null)
        {
            name = user;
        }
        return name;
    }

    public void addUserData(UserData userData)
    {
        mUsers.add(userData);
        notifyDataSetChanged();
    }
    public void removeUserData(int position)
    {
        XMPPConnection conn = DataWarehouse.getXMPPConnection(mContext);
        RosterEntry entry = conn.getRoster().getEntry(getUser(position));
        if(entry != null)
        {
            try
            {
                conn.getRoster().removeEntry(entry);

            }
            catch (Exception e)
            {

            }
        }
        mUsers.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.friend_list_item, null);
        }
        TextView user = (TextView)view.findViewById(R.id.textview_friend_list_item_user);
        if(getName(i) == null)
        {
            user.setText(getUser(i));
        }
        else
        {
            user.setText(getName(i));
        }
        return view;
    }
}
