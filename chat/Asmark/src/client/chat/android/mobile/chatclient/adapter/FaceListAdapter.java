package client.chat.android.mobile.chatclient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import client.chat.android.mobile.chatclient.R;
import client.chat.android.mobile.chatclient.common.Const;
import client.chat.android.mobile.chatclient.common.Util;

/**
 * Created by 潇洒哥 on 15/10/01.
 */
public class FaceListAdapter extends ParentAdapter implements Const {
    private int mFaceCount; // 表情图像总数
    public FaceListAdapter(Context context) {
        super(context);
        calculateFaceCount();
    }
    private void calculateFaceCount()
    {
        int i = 0;
        while(true)
        {
            i++;
            String faceName = FACE_PREFIX + i;
            try
            {
                R.drawable.class.getField(faceName);
            }
            catch (Exception e)
            {
                break;
            }
        }
        i--;
        mFaceCount = i;

    }
    public int getFace(int position)
    {
        position++;
        if(position > mFaceCount)
            return -1;
        try
        {
            return Util.getResourceIDFromName(R.drawable.class, FACE_PREFIX + position);
        }
        catch (Exception e)
        {
            return -1;
        }
    }
    @Override
    public int getCount() {
        return mFaceCount;
    }

    @Override
    public Object getItem(int i) {
        return super.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.face, null);
        }
        ImageView face = (ImageView)view;
        face.setImageResource(getFace(i));
        return view;
    }
}
