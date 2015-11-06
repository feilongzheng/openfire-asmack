package client.chat.android.mobile.chatclient.common;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Created by 潇洒哥 on 15/5/12.
 */
public class XMPPUtil {
    //  获取聊天服务器连接
    public static XMPPConnection getXMPPConnection(String server, int port)
    {
        try
        {
            ConnectionConfiguration config = new ConnectionConfiguration(server, port);
            config.setReconnectionAllowed(true);
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            config.setSendPresence(true);

            SASLAuthentication.supportSASLMechanism("PLAIN", 0);

            XMPPConnection connection = new XMPPTCPConnection(config, null);
            connection.connect();
            return connection;

        }
        catch (Exception e)
        {
        }

        return null;
    }
    public static XMPPConnection getXMPPConnection(String server)
    {
        return getXMPPConnection(server, 5222);
    }
}
