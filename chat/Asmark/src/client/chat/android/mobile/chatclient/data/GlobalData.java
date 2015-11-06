package client.chat.android.mobile.chatclient.data;

import android.app.Application;

import org.jivesoftware.smack.XMPPConnection;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by 潇洒哥 on 15/5/12.
 */
public class GlobalData extends Application {
    public XMPPConnection xmppConnection;
    public LoginData loginData = new LoginData();
    public Set<String> chatUsers = new TreeSet<String>();  // 当前正在聊天的用户


}
