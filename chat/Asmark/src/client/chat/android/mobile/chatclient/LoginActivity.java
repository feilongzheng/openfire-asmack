package client.chat.android.mobile.chatclient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Registration;


import client.chat.android.mobile.chatclient.common.Const;
import client.chat.android.mobile.chatclient.common.Storage;
import client.chat.android.mobile.chatclient.common.XMPPUtil;
import client.chat.android.mobile.chatclient.data.DataWarehouse;
import client.chat.android.mobile.chatclient.data.LoginData;

// 保存和恢复登录信息
public class LoginActivity extends Activity implements Const {
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private EditText mEditTextServer;
    private CheckBox mCheckBoxSavePassword;
    private CheckBox mCheckBoxAutoLogin;

    private LoginData mLoginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextUsername = (EditText) findViewById(R.id.edittext_user);
        mEditTextPassword = (EditText) findViewById(R.id.edittext_password);
        mEditTextServer = (EditText) findViewById(R.id.edittext_server);

        mCheckBoxAutoLogin = (CheckBox) findViewById(R.id.checkbox_auto_login);
        mCheckBoxSavePassword = (CheckBox) findViewById(R.id.checkbox_save_password);
        mLoginData = DataWarehouse.getGlobalData(this).loginData;

        mLoginData.username = Storage.getString(this, KEY_USERNAME);
        mLoginData.password = Storage.getString(this, KEY_PASSWORD);
        mLoginData.loginServer = Storage.getString(this, KEY_LOGIN_SERVER);

        mLoginData.isAutoLogin = Storage.getBoolean(this, KEY_AUTO_LOGIN);
        mLoginData.isSavePassword = Storage.getBoolean(this, KEY_SAVE_PASSWORD);

        mEditTextUsername.setText(mLoginData.username);
        mEditTextServer.setText(mLoginData.loginServer);
        mCheckBoxAutoLogin.setChecked(mLoginData.isAutoLogin);
        mCheckBoxSavePassword.setChecked(mLoginData.isSavePassword);

        if(mLoginData.isSavePassword)
        {
            mEditTextPassword.setText(mLoginData.password);
        }

        if(mLoginData.isAutoLogin)
        {
            onClick_Login(null);
        }

    }

    //  用于在线程中登录聊天服务器的方法
    private boolean login() {
        try {
            XMPPConnection connection = XMPPUtil.getXMPPConnection(mLoginData.loginServer);
            if (connection == null) {
                throw new Exception("连接聊天服务器失败.");
            }
            connection.login(mLoginData.username, mLoginData.password);
            DataWarehouse.setXMPPConnection(this, connection);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    private Handler mHandler = new Handler();

    //  登录按钮的单击事件方法
    public void onClick_Login(View view) {

        //  将登录信息保存到全局对象中
        mLoginData.username = mEditTextUsername.getText().toString();
        mLoginData.password = mEditTextPassword.getText().toString();
        mLoginData.loginServer = mEditTextServer.getText().toString();
        mLoginData.isSavePassword = mCheckBoxSavePassword.isChecked();
        mLoginData.isAutoLogin = mCheckBoxAutoLogin.isChecked();

        // 存储登录信息
        Storage.putString(this, KEY_USERNAME, mLoginData.username);

        Storage.putString(this, KEY_LOGIN_SERVER, mLoginData.loginServer);

        Storage.putBoolean(this, KEY_AUTO_LOGIN, mLoginData.isAutoLogin);
        Storage.putBoolean(this, KEY_SAVE_PASSWORD, mLoginData.isSavePassword);

        if(mLoginData.isSavePassword)
        {
            Storage.putString(this, KEY_PASSWORD, mLoginData.password);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //  登录成功
                if (login()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else //  登录失败
                {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登录失败，请检查用户名、密码和服务器IP的正确性.", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        }).start();

    }
    public void onClick_Register(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        String server = mEditTextServer.getText().toString();
        if(!"".equals(server))
        {
            intent.putExtra("server", server);
        }
        startActivity(intent);
    }

}
