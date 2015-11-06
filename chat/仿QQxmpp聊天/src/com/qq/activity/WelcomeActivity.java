package com.qq.activity;


import com.qq.MainActivity;
import com.qq.R;
import com.qq.service.MsfService;
import com.qq.util.PreferencesUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {
	protected static final String TAG = "WelcomeActivity";
	private Context mContext;
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		mContext = this;
		findView();
		init();
	}

	private void findView() {
		mImageView = (ImageView) findViewById(R.id.iv_welcome);
	}

	private void init() {
		mImageView.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent =null;
				String username=PreferencesUtils.getSharePreStr(WelcomeActivity.this, "username");
				String pwd=PreferencesUtils.getSharePreStr(WelcomeActivity.this, "pwd");
				if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(pwd)){
					intent = new Intent(mContext, MainActivity.class);
					intent.putExtra("isStartService", true);
				}else{
					intent = new Intent(mContext, LoginActivity.class);
				}
				startActivity(intent);
				finish();
			}
		},2000);
		
	}
}
