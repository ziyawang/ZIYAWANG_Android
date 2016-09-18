package com.ziyawang.ziya.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.model.Conversation;


/**
 * Created by 牛海丰 on 2016/7/19.
 */
public class MyApplication extends Application {

    //activity Task
    private List<Activity> activities = new ArrayList<Activity>();

    //add activity
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化融云SDK
        initSDK() ;
        //融云扩展功能的自定义,去掉定位，只存在图片的发送
        initData() ;
        //设置Umeng的统计场景
        setUmengType() ;
    }

    private void setUmengType() {
        //设置Umeng的监控场景
        MobclickAgent.setScenarioType(MyApplication.this , MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    private void initData() {
        //扩展功能自定义
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),
        };
        RongIM.resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
    }

    private void initSDK() {
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) || "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIM.init(this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : activities) {
            activity.finish();
        }
        System.exit(0);
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
