package client.chat.android.mobile.chatclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.jivesoftware.smack.XMPPConnection;

import client.chat.android.mobile.chatclient.data.DataWarehouse;


public class ParentActivity extends Activity {

    protected XMPPConnection mXMPPConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mXMPPConnection = DataWarehouse.getXMPPConnection(this);
    }

}
