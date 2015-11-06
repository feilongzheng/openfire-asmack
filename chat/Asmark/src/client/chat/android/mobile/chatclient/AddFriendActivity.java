package client.chat.android.mobile.chatclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddFriendActivity extends ParentActivity
{

    private EditText mEditTextAccount;
    private EditText mEditTextAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mEditTextAccount = (EditText) findViewById(R.id.edittext_account);
        mEditTextAlias = (EditText) findViewById(R.id.edittext_alias);

    }

    public void onClick_Add_Friend(View view)
    {
        String account = mEditTextAccount.getText().toString().trim();
        String alias = mEditTextAlias.getText().toString().trim();

        if("".equals(account))
        {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if("".equals(alias))
        {
            alias = account;
        }
        try
        {
            mXMPPConnection.getRoster().createEntry(account, alias, null);
            Intent intent = new Intent();
            intent.putExtra("user", account);
            intent.putExtra("name", alias);
            setResult(1, intent);
            Toast.makeText(this, "成功添加好友.", Toast.LENGTH_LONG).show();
            finish();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "添加好友失败（" + e.getMessage() + "）", Toast.LENGTH_LONG).show();
        }
    }

}
