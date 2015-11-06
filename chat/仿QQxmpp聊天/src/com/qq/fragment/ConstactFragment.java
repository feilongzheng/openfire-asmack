package com.qq.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;

import com.qq.QQApplication;
import com.qq.R;
import com.qq.activity.AddFriendActivity;
import com.qq.activity.ChatActivity;
import com.qq.adapter.ConstactAdapter;
import com.qq.bean.Child;
import com.qq.bean.Group;
import com.qq.bean.Session;
import com.qq.util.XmppUtil;
import com.qq.view.IphoneTreeView;
import com.qq.view.TitleBarView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class ConstactFragment extends Fragment {
	private Context mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	private IphoneTreeView mIphoneTreeView;
	private ConstactAdapter mExpAdapter;
	private List<Group> listGroup;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_constact_father, null);
		findView();
		return mBaseView;
	}

	private void findView() {
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
		mTitleBarView.setTitleText(R.string.constacts);//标题
		mTitleBarView.setTitleRight("添加");//右按钮-添加好友
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, AddFriendActivity.class);
				startActivity(intent);
			}
		});
		mIphoneTreeView = (IphoneTreeView) mBaseView.findViewById(R.id.iphone_tree_view);
		mIphoneTreeView.setHeaderView(LayoutInflater.from(mContext).inflate(R.layout.fragment_constact_head_view, mIphoneTreeView, false));
		mIphoneTreeView.setGroupIndicator(null);
		mIphoneTreeView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,int arg3, long arg4) {
				Intent intent =new Intent(mContext, ChatActivity.class);
				intent.putExtra("from", listGroup.get(arg2).getChildList().get(arg3).getUsername());
				startActivity(intent);
				return true;
			}
		});
		if(QQApplication.xmppConnection==null){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					initData();
				}
			}, 1000);
		}else{
			initData();
		}
	}
	
	/**
	 * 加载数据
	 */
	void initData(){
		findFriends();
		if(listGroup.size()<=0){
			mIphoneTreeView.setVisibility(View.GONE);
			return;
		}
		mExpAdapter = new ConstactAdapter(mContext, listGroup, mIphoneTreeView);
		mIphoneTreeView.setAdapter(mExpAdapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	public void findFriends() {
		try {
			listGroup=new ArrayList<Group>();
			XMPPConnection conn = QQApplication.xmppConnection;
			Roster roster = conn.getRoster();
			Collection<RosterGroup> groups = roster.getGroups();
			for (RosterGroup group : groups) {
				Group mygroup=new Group();
				mygroup.setGroupName(group.getName());
				Collection<RosterEntry> entries = group.getEntries();
				List<Child> childList=new ArrayList<Child>();
				for (RosterEntry entry : entries) {
					if(entry.getType().name().equals("both")){
						Child child=new Child();
						child.setUsername(entry.getUser().split("@")[0]);
						childList.add(child);
					}
				}
				mygroup.setChildList(childList);
				listGroup.add(mygroup);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
