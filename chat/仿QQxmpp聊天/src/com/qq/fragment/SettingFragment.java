package com.qq.fragment;

import com.qq.R;
import com.qq.activity.LoginActivity;
import com.qq.service.MsfService;
import com.qq.util.PreferencesUtils;
import com.qq.view.TitleBarView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingFragment extends Fragment implements OnClickListener{

	private Context mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	private TextView name;
	private RelativeLayout login_out;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mContext=getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_mine, null);
		init();
		return mBaseView;
	}
	
	private void init(){
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.mime);
		
		name=(TextView) mBaseView.findViewById(R.id.name);
		name.setText(PreferencesUtils.getSharePreStr(mContext, "username"));
		
		login_out=(RelativeLayout) mBaseView.findViewById(R.id.login_out);
		login_out.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.login_out:
			Intent intent=new Intent(mContext, LoginActivity.class);
			mContext.startActivity(intent);
			try{
				MsfService.getInstance().stopSelf();
			}catch (Exception e) {
				
			}
			getActivity().finish();
			break;
		}
	}

}
