package com.ziyawang.ziya.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 牛海丰 on 2016/9/2.
 */
public class ChangeNotifyColor {
    /**
     *
     * @param Color 通知栏的颜色
     * @param activity 当前activity
     */
    public static void change(int Color , final Activity activity ){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断系统是否为KitKat以上
            setTranslucentStatus(true ,activity);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(Color);//通知栏所需颜色
    }

    @TargetApi(19)
    private static void setTranslucentStatus(boolean on , Activity activity) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
