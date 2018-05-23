package com.ziyawang.ziya.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.application.MyApplication;
import com.ziyawang.ziya.fragment.HomePageFragment;
import com.ziyawang.ziya.fragment.InformationFragment;
import com.ziyawang.ziya.fragment.MyFragment;
import com.ziyawang.ziya.fragment.NewsFragment;
import com.ziyawang.ziya.fragment.ReleaseFragment;
import com.ziyawang.ziya.fragment.SearchFragment;
import com.ziyawang.ziya.fragment.V3FindInfoFragment;
import com.ziyawang.ziya.fragment.V3FindServiceFragment;
import com.ziyawang.ziya.fragment.V3HomePageFragment;
import com.ziyawang.ziya.tools.DownLoadManager;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.NetUtils;
import com.ziyawang.ziya.tools.PermissionsUtil;
import com.ziyawang.ziya.tools.SystemBarTintManager;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.CustomDialogVersion;
import com.ziyawang.ziya.view.NotificationButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private SharedPreferences isThree ;
    private FrameLayout button_release;
    private LinearLayout linear;
    //private NotificationButton button_count;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private static Boolean isQuit = false;
    private Timer timer = new Timer();
    private MyApplication app;
    //public HomePageFragment homePageFragment;
    public V3HomePageFragment v3HomePageFragment ;
    //private InformationFragment informationFragment;
    //public NewsFragment newsFragment ;
    public V3FindServiceFragment v3FindServiceFragment ;
    public MyFragment myFragment;
    public ReleaseFragment releaseFragment;
    //public SearchFragment searchFragment;

    public V3FindInfoFragment v3FindInfoFragment ;

    private SharedPreferences r_token;
    private String login;
    private final static String lancherActivityClassName = StartActivity.class.getName();
    // fragment list
    List<Fragment> listFragment = new ArrayList<>() ;

    private ImageView img_red_point ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        //存储 activity 路径
        app = (MyApplication) getApplication();
        this.getApplication();
        app.addActivity(this);
        //设置通知栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        //更改通知栏所需颜色
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_login_head);
        //检查app所需权限
        PermissionsUtil.checkAndRequestPermissions(this);
        //获取用户当前是否登录
        SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        //获取用户token
        final SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);
        //获取当前最新版本信息，提示用户更新
        checkVersion() ;
        //实例化组件
        initView();
        //第一次加载的Fragment
        selectTab(0);
        //选择的Fragment
        selectedFragment();
        //已经登录连接融云聊天服务器
        if (isLogin) {
            connect();
        }
    }
    //获取当前最新版本信息，提示用户更新
    private void checkVersion() {
        HttpUtils utils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        utils.send(HttpRequest.HttpMethod.GET, Url.CheckUpdata, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result);
                try {
                    JSONArray array = new JSONArray(responseInfo.result);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String versionCode = jsonObject.getString("VersionCode");
                    final String UpdateUrl = jsonObject.getString("UpdateUrl");
                    String UpdateTitle = jsonObject.getString("UpdateTitle");
                    String UpdateDes = jsonObject.getString("UpdateDes");
                    int num_web = Integer.parseInt(versionCode);
                    // 获取packagemanager的实例
                    PackageManager packageManager = getPackageManager();
                    // getPackageName()是你当前类的包名，0代表是获取版本信息
                    PackageInfo packInfo = null;
                    try {
                        packInfo = packageManager.getPackageInfo(getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    final int num_local = packInfo.versionCode;
                    if (num_web > num_local) {
                        final CustomDialogVersion.Builder builder = new CustomDialogVersion.Builder(MainActivity.this);
                        builder.setTitle(UpdateTitle);
                        builder.setMessage(UpdateDes);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                int networkType = NetUtils.getNetworkType(MainActivity.this);
                                switch (networkType) {
                                    case NetUtils.NETTYPE_CMNET:
                                    case NetUtils.NETTYPE_CMWAP:
                                        CustomDialog.Builder customDialog = new CustomDialog.Builder(MainActivity.this) ;
                                        customDialog.setTitle("亲爱的用户");
                                        customDialog.setMessage("当前未连接wifi，是否继续下载");
                                        customDialog.setPositiveButton("继续下载", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                //下载apk
                                                final ProgressDialog pd;    //进度条对话框
                                                pd = new ProgressDialog(MainActivity.this);
                                                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                pd.setMessage("正在下载更新");
                                                pd.setCancelable(true);//设置进度条是否可以按退回键取消
                                                //设置点击进度对话框外的区域对话框不消失
                                                pd.setCanceledOnTouchOutside(false);
                                                pd.show();
                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            File file = DownLoadManager.getFileFromServer(Url.FileIP + UpdateUrl, pd);
                                                            sleep(3000);
                                                            installApk(file);
                                                            pd.dismiss(); //结束掉进度条对话框
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }.start();
                                            }
                                        }) ;
                                        customDialog.setNegativeButton("等待wifi", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }) ;
                                        customDialog.create().show();
                                        break;
                                    case NetUtils.NETTYPE_WIFI:
                                        final ProgressDialog pd;    //进度条对话框
                                        pd = new ProgressDialog(MainActivity.this);
                                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                        pd.setMessage("正在下载更新");
                                        pd.setCancelable(true);//设置进度条是否可以按退回键取消
                                        //设置点击进度对话框外的区域对话框不消失
                                        pd.setCanceledOnTouchOutside(false);
                                        pd.show();
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                try {
                                                    File file = DownLoadManager.getFileFromServer(Url.FileIP + UpdateUrl, pd);
                                                    sleep(3000);
                                                    installApk(file);
                                                    pd.dismiss(); //结束掉进度条对话框
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                        break;
                                }
                            }
                        });
                        builder.create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        }) ;
    }
    // 安装app
    private void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
    //连接融云聊天服务器
    private void connect() {
        //获取融云token
        r_token = getSharedPreferences("r_token", MODE_PRIVATE);
        final String cloud_token = r_token.getString("r_token", "");
        RongIM.connect(cloud_token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
                Log.d("benben", "——————————————————————————————Token过期————————————————————————————");
                String urls = String.format(Url.RCToken, login);
                HttpUtils utils = new HttpUtils();
                RequestParams params = new RequestParams();
                utils.configCurrentHttpCacheExpiry(1000);
                utils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("benben", responseInfo.result);
                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                            String status_code = jsonObject.getString("status_code");
                            switch (status_code) {
                                case "200":
                                    String rcToken = jsonObject.getString("rcToken");
                                    r_token.edit().putString("r_token", rcToken).commit();
                                    connect();
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        error.printStackTrace();
                    }
                });
            }

            @Override
            public void onSuccess(String userId) {
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(final String userId) {
                        HttpUtils httpUtils = new HttpUtils();
                        RequestParams params = new RequestParams();
                        httpUtils.send(HttpRequest.HttpMethod.POST, String.format(Url.RongIcon, userId), params, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Log.e("benben", responseInfo.result);
                                try {
                                    String a = "";
                                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    String userPicture = data.getString("UserPicture");
                                    String serviceName = data.getString("ServiceName");
                                    String role = data.getString("role");
                                    switch (role) {
                                        case "0":
                                        case "2":
                                            String phonenumber = data.getString("phonenumber");
                                            String substring = phonenumber.substring(0, 3);
                                            String substring1 = phonenumber.substring(7, 11);
                                            a = substring + "****" + substring1;
                                            break;
                                        case "1":
                                            a = serviceName;
                                            break;
                                        default:
                                            break;
                                    }
                                    Uri uir = Uri.parse(Url.FileIP + userPicture);
                                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, a, uir));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {

                                error.printStackTrace();
                            }
                        });
                        Log.e("MainActivity", "UserId is ：" + userId);
                        return null;
                    }
                }, true);
                Log.e("benben", "—--------------------------------—onSuccess—--------------------------------" + userId);
                if (RongIM.getInstance() != null) {

                    Log.e("benben", "--------------------------");
                    /**
                     * 接收未读消息的监听器。
                     *
                     * @param listener          接收所有未读消息消息的监听器。
                     */
                    RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new MyReceiveUnreadCountChangedListener(), Conversation.ConversationType.PRIVATE);

                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("benben", "——----------------------onError—-------------------------------" + errorCode);
            }
        });
    }

    /**
     * 接收未读消息的监听器。相关展示。
     */
    private class MyReceiveUnreadCountChangedListener implements RongIM.OnReceiveUnreadCountChangedListener {
        /**
         * @param count 未读消息数。
         */
        @Override
        public void onMessageIncreased(int count) {
            //button_count.setNotificationNumber(count);
            if (count==0){
                img_red_point.setVisibility(View.GONE);
            }else {
                img_red_point.setVisibility(View.VISIBLE);
            }
            sendBadgeNumber(count);
        }
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

    //退出时执行的方法
    @Override
    public void onBackPressed() {
        if (!isQuit) {
            isQuit = true;
            Toast.makeText(MainActivity.this, "再按一次退出资芽", Toast.LENGTH_SHORT).show();
            TimerTask task = null;
            task = new TimerTask() {
                @Override
                public void run() {
                    isQuit = false;
                }
            };
            timer.schedule(task, 2000);
        } else {
            app.onTerminate();
            finish();
            MobclickAgent.onKillProcess(this);
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void selectTab(int position) {
        for (int i = 0; i < linear.getChildCount(); i++) {
            linear.getChildAt(i).setSelected(false);
            linear.getChildAt(i).setClickable(true);
        }
        linear.getChildAt(position).setSelected(true);
        linear.getChildAt(position).setClickable(true);
    }

    private void initView() {
        //button_count = (NotificationButton) findViewById(R.id.button_count);
        img_red_point = (ImageView) findViewById(R.id.img_red_point);
        button_release = (FrameLayout) findViewById(R.id.button_release);
        linear = (LinearLayout) findViewById(R.id.linear);
        for (int i = 0; i < linear.getChildCount(); i++) {
            linear.getChildAt(i).setOnClickListener(this);
        }
    }

    private void selectedFragment() {
        v3HomePageFragment = new V3HomePageFragment();
        //newsFragment = new NewsFragment() ;
        v3FindServiceFragment = new V3FindServiceFragment() ;
        myFragment = new MyFragment();
        releaseFragment = new ReleaseFragment();
        //searchFragment = new SearchFragment();
        v3FindInfoFragment = new V3FindInfoFragment() ;
        SetDefaultFragment(v3HomePageFragment) ;

        listFragment.add(0 , v3HomePageFragment );
        listFragment.add(1 , v3FindInfoFragment );
        listFragment.add(2 , releaseFragment );
        listFragment.add(3 , v3FindServiceFragment );
        listFragment.add(4 , myFragment );
    }

    private void SetDefaultFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_frameyout, fragment);
        transaction.commit() ;
    }

    private void ActiveFragment(List<Fragment> listFragment, FragmentTransaction transaction, Fragment fragment) {
        for (int i = 0; i < listFragment.size(); i++) {
            if (listFragment.get(i).isVisible()){
                if (fragment.isAdded()){
                    transaction.hide(listFragment.get(i)).show(fragment) ;
                }else {
                    transaction.hide(listFragment.get(i)).add(R.id.main_frameyout , fragment) ;
                }
                break;
            }
        }
        transaction.commit() ;
    }

    @Override
    public void onClick(View v) {
        transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            //homepage页面
            case R.id.button_homePage:
                selectTab(0);
                ActiveFragment(listFragment , transaction , v3HomePageFragment) ;
                break;
            //找信息页面
            case R.id.button_search:
                selectTab(1);
                ActiveFragment(listFragment , transaction , v3FindInfoFragment) ;
                break;
            //release页面
            case R.id.button_release:
                selectTab(2);
                ActiveFragment(listFragment , transaction , releaseFragment) ;

                break;
            //找服务 页面
            case R.id.button_information:
                selectTab(3);
                ActiveFragment(listFragment , transaction , v3FindServiceFragment) ;
                break;
            //我的页面
            case R.id.button_me:
                selectTab(4);
                ActiveFragment(listFragment , transaction , myFragment) ;
                break;
        }
    }

    private void sendBadgeNumber(int count) {
        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            if (count > 0) {
                sendToXiaoMi(count);
            }
        } else if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            sendToSony("" + count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("sony")) {
            sendToSamsumg("" + count);
        }
    }

    private void sendToXiaoMi(int number) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        boolean isMiUIV6 = true;
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("您有" + number + "未读消息");
            builder.setTicker("您有" + number + "未读消息");
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setDefaults(Notification.DEFAULT_LIGHTS);
            notification = builder.build();
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, number);// 设置信息数
            field = notification.getClass().getField("extraNotification");
            field.setAccessible(true);
            field.set(notification, miuiNotification);
        } catch (Exception e) {
            e.printStackTrace();
            //miui 6之前的版本
            isMiUIV6 = false;
            Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra("android.intent.extra.update_application_component_name", getPackageName() + "/" + lancherActivityClassName);
            localIntent.putExtra("android.intent.extra.update_application_message_text", number);
            sendBroadcast(localIntent);
        }
        //finally {
//            if (notification != null && isMiUIV6) {
//                //miui6以上版本需要使用通知发送
//                nm.notify(101010, notification);
//            }
//        }
    }

    private void sendToSony(String number) {
        boolean isShow = true;
        if ("0".equals(number)) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);//是否显示
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", lancherActivityClassName);//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", number);//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", getPackageName());//包名
        sendBroadcast(localIntent);
    }

    private void sendToSamsumg(String number) {
        Intent localIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        localIntent.putExtra("badge_count", number);//数字
        localIntent.putExtra("badge_count_package_name", getPackageName());//包名
        localIntent.putExtra("badge_count_class_name", lancherActivityClassName); //启动页
        sendBroadcast(localIntent);
        //Toast.makeText(this, "Samsumg," + "isSendOk", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //不知道干啥的 忘了
        if (GetBenSharedPreferences.getisThree(this)){
            selectTab(2);
            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();
            if (!releaseFragment.isAdded()) {
                transaction.hide(v3HomePageFragment).add(R.id.main_frameyout, releaseFragment).commit();
            }else {
                transaction.hide(v3HomePageFragment).show(releaseFragment).commit();
            }
            isThree = getSharedPreferences("isThree" , MODE_PRIVATE ) ;
            isThree.edit().putBoolean("isThree", false).commit();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
