package com.ziyawang.ziya.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.application.MyApplication;
import com.ziyawang.ziya.tools.SystemBarTintManager;

/**
 * 基础的Activity，其他Activity继承。
 */
public class BaseActivity extends Activity {

    //声明MyApplication变量
    private MyApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //获得activity的Task路径
        getActivityTask() ;
        //改变通知栏的颜色
        changeNotifyColor() ;
    }

    private void changeNotifyColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断系统是否为KitKat以上
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        //通知栏所需颜色
        tintManager.setStatusBarTintResource(R.color.aaa);
    }

    private void getActivityTask() {
        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
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
