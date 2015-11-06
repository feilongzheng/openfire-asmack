package client.chat.android.mobile.chatclient.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.chat.android.mobile.chatclient.R;

/**
 * Created by 潇洒哥 on 15/10/01.
 */
public class Util {
    //  从包含服务名的user中（user2@thor）中提取@之前的内容
    public static String extractUserFromChat(String user)
    {
        int index = user.indexOf("@");
        if(index == -1)
        {
            return user;
        }
        else
        {
            return user.substring(0, index);
        }
    }

    //  根据资源名获取资源ID
    public static int getResourceIDFromName(Class c, String name)
    {
        int resId = -1;
        try
        {
            Field field = c.getField(name);
            resId = field.getInt(null);
        }
        catch (Exception e)
        {

        }
        return resId;
    }

    @SuppressLint("NewApi") public static void updateFacesForTextView(Context context, TextView textView)
    {
        //  ab<:12:>xyz<:1:>abc
        String regExp = Const.FACE_TEXT_PREFIX + "\\d+" + Const.FACE_TEXT_SUFFIX;

        Pattern pattern = Pattern.compile(regExp);

        String oldText = textView.getText().toString();

        textView.setText("");
        String oldTextArray[] = oldText.split(regExp);
        Matcher matcher = pattern.matcher(oldText);
        int start = 0;
        int i = 0;
        int count = 0;

        while(matcher.find(start))
        {
            if(i < oldTextArray.length)
            {
                textView.append(oldTextArray[i++]);
            }
            count++;
            String faceText = matcher.group();
            int faceTextSuffixStartIndex = faceText.indexOf(Const.FACE_TEXT_SUFFIX);
            int faceId = Integer.parseInt(faceText.substring(Const.FACE_TEXT_PREFIX.length(),
                    faceTextSuffixStartIndex));
            start = matcher.end();
            String faceResName = Const.FACE_PREFIX + faceId;
            int resId = Util.getResourceIDFromName(R.drawable.class, faceResName);

            if(resId == -1)
            {
                textView.append(faceText);
                continue;
            }

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    resId);
            Matrix matrix = new Matrix();
            matrix.postScale(0.6f, 0.6f);
            Bitmap smallBitmap = Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(),
                    bitmap.getHeight(),matrix,true);

            ImageSpan imageSpan = new ImageSpan(context, smallBitmap);
            SpannableString spannableString = new SpannableString(faceText);
            spannableString.setSpan(imageSpan, 0, faceText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.append(spannableString);

        }

        if(count == oldTextArray.length - 1)
        {
            textView.append(oldTextArray[count]);
        }
    }


    public static String extractUserFromChatGroup(String user)
    {
        int index = user.indexOf("@");
        if (index == -1)
        {
            return user;
        }
        else
        {
            int index1 = user.indexOf("/");
            if (index1 == -1)
                return user;
            else
                return user.substring(index1 + 1);
        }
    }
}
