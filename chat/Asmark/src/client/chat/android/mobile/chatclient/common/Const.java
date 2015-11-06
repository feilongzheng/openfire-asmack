package client.chat.android.mobile.chatclient.common;

/**
 * Created by 潇洒哥 on 15/5/13.
 */
public interface Const {
    String KEY_USERNAME = "username";
    String KEY_PASSWORD = "password";
    String KEY_LOGIN_SERVER = "login_server";
    String KEY_SAVE_PASSWORD = "save_password";
    String KEY_AUTO_LOGIN = "auto_login";

    String FACE_PREFIX = "face";
    String KEY_FACE_ID = "face_id";
    String CHAT_ROOM_NAME = "chat_room_name";   // 聊天室名称
    String FACE_TEXT_PREFIX = "<:";  //  不要使用正则表达式中使用的符号
    /***
     * 插在EditText或TextView中的文本的后缀，和前缀以及face id一起使用
     * e.g.  <:4:>
     */
    String FACE_TEXT_SUFFIX = ":>";

    String JID = "jid";
}
