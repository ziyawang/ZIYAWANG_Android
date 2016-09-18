package com.ziyawang.ziya.activity;

import android.app.Activity;
import android.os.Bundle;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.application.MyApplication;
import com.ziyawang.ziya.tools.ChangeNotifyColor;

/**
 * 基类
 */
public abstract class BenBenActivity extends Activity {
    //声明MyApplication变量
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 定制流程
        setContentView();
        initViews();
        initListeners();
        initData();
        //获得activity的Task路径
        getActivityTask() ;
        //改变通知栏的颜色
        ChangeNotifyColor.change(R.color.aaa, this);
    }

    private void getActivityTask() {
        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);
    }

    public abstract void setContentView();

    public abstract void initViews();

    public abstract void initListeners();

    public abstract void initData();
}
