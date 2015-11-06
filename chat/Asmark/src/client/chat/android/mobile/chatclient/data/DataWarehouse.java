package client.chat.android.mobile.chatclient.data;

import android.content.Context;

import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by 潇洒哥 on 15/5/12.
 */
public class DataWarehouse {
    public static GlobalData getGlobalData(Context ctx)
    {
        return (GlobalData)ctx.getApplicationContext();
    }

    public static XMPPConnection getXMPPConnection(Context ctx)
    {
        return  getGlobalData(ctx).xmppConnection;
    }

    public static void setXMPPConnection(Context ctx, XMPPConnection conn)
    {
        getGlobalData(ctx).xmppConnection = conn;
    }
}
