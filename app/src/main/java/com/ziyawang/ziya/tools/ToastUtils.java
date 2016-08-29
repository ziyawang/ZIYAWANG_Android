package com.ziyawang.ziya.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 牛海丰 on 2016/07/19 0029.
 */
public class ToastUtils {

    private static Toast mToast;

    public static void showToast(Context context, String msg, int duration) {
        if(context!=null){
            context = context.getApplicationContext();
        }else{
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showToast(Context context, String msg) {// , int duration
        showToast(context, msg,Toast.LENGTH_SHORT);
    }

    public static void longToast(Context context,String msg) {// , int duration
        showToast(context, msg,Toast.LENGTH_LONG);
    }

    public static void shortToast(Context context,String msg) {// , int duration
        showToast(context, msg,Toast.LENGTH_SHORT);
    }
}
