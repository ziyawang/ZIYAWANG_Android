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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
import com.ziyawang.ziya.fragment.ReleaseFragment;
import com.ziyawang.ziya.fragment.SearchFragment;
import com.ziyawang.ziya.tools.DownLoadManager;
import com.ziyawang.ziya.tools.NetUtils;
import com.ziyawang.ziya.tools.PermissionsUtil;
import com.ziyawang.ziya.tools.SystemBarTintManager;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.NotificationButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private FrameLayout button_release;
    private LinearLayout linear;
    private NotificationButton button_information;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private static Boolean isQuit = false;
    private Timer timer = new Timer();
    private MyApplication app;
    private HomePageFragment homePageFragment;
    private InformationFragment informationFragment;
    private MyFragment myFragment;
    private ReleaseFragment releaseFragment;
    private SearchFragment searchFragment;
    private SharedPreferences r_token;
    private String login;
    private final static String lancherActivityClassName = StartActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (MyApplication) getApplication();
        this.getApplication();
        app.addActivity(this);

        //设置通知栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.aaa);//通知栏所需颜色

        PermissionsUtil.checkAndRequestPermissions(this);

        final SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);

        SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);


        //实例化组件
        initView();
        //第一次加载的Fragment
        selectTab(0);
        //选择的Fragment
        selectedFragment();

        checkVersion() ;

        if (isLogin) {
            //ToastUtils.shortToast(MainActivity.this, login) ;
            connect();
            //设置自己的userinfo
            //myUserInfo() ;

            //toastNet() ;

        } else {
            //ToastUtils.shortToast(MainActivity.this, "还未登陆");
        }
    }

    private void checkVersion() {

        HttpUtils utils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        utils.send(HttpRequest.HttpMethod.GET, Url.CheckUpdata, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result);
                try {
                    //JSONObject jsonObject = new JSONObject(responseInfo.result) ;

                    JSONArray array = new JSONArray(responseInfo.result);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String versionCode = jsonObject.getString("VersionCode");
                    String versionName = jsonObject.getString("VersionName");
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
                    //String version = packInfo.versionName;
                    final int num_local = packInfo.versionCode;

                    if (num_web > num_local) {
                        final CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
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
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    } else {
                        //ToastUtils.shortToast(MainActivity.this , "已经是最新版本");
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

    private void installApk(File file) {

        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);

    }

    private void myUserInfo() {

        final String urls = String.format(Url.Myicon, login ) ;
        Log.e("benbne" , login  );
        HttpUtils utils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("niuniu", responseInfo.result);

                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    String role1 = jsonObject.getString("role");

                    Log.e("benben", role1);

                    final SharedPreferences role = getSharedPreferences("role", MODE_PRIVATE);
                    role.edit().putString("role", role1).commit();

                    switch (role1) {
                        case "0":
                        case "2":
                            JSONObject user = jsonObject.getJSONObject("user");
                            String userid = user.getString("userid");
                            //String username = user.getString("username");
                            String phone = user.getString("phonenumber");
                            String substring = phone.substring(0, 3);
                            String substring1 = phone.substring(7, 11);

                            Log.e("benben", substring + substring1);
                            final String UserPicture = user.getString("UserPicture");
                            Uri uir = Uri.parse(Url.FileIP + UserPicture);

                            RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid, substring + "***" + substring1, uir));
                            break;

                        case "1":
                            JSONObject object = new JSONObject(responseInfo.result);
                            JSONObject service = object.getJSONObject("service");
                            //企业名称
                            String ServiceName = service.getString("ServiceName");
                            JSONObject user1 = jsonObject.getJSONObject("user");
                            String Userid = user1.getString("userid");
                            final String userPicture = user1.getString("UserPicture");
                            Uri uir01 = Uri.parse(Url.FileIP + userPicture);
                            RongIM.getInstance().setCurrentUserInfo(new UserInfo(Userid, ServiceName, uir01));

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
        }) ;
    }

    private void connect() {

        r_token = getSharedPreferences("r_token", MODE_PRIVATE);
        final String cloud_token = r_token.getString("r_token", "");

        Log.e("benbne" , cloud_token ) ;
        /*******************************************获取融云token***************************************************/
        //  18610301342  牛海丰
        //String Token = "ysxnr+lQxBdpEcLAiO+ixUFbtPqxQ3+uKBlZaj5Qs0XrF7dgr9/e3j0KojmodjXxp4Zd2NBK8fQ5NMr6k8ckNkf/qbRFMskM"  ;
        //   18600000000 Android
        //String Token = "l1mGz2SFxOi6bz2biGzGbmtgz2fIXi50V82Gr9MQdd2ldsTSjeB2DywVTJyDjA0YjX0HWJ0L9c3Ujl3MCj9Wy/9JSQ9r0dkF" ;

        RongIM.connect(cloud_token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
                Log.d("benben", "——————————————————————————————--Token过期————————————————————————————————");
                Log.e("benben", cloud_token);
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


                //RongIM.getInstance().setMessageAttachedUserInfo(true);

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
                        //return null;
                        //return new UserInfo( userId , "牛牛" ,  uir ) ;
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


                //启动会话界面
//                if (RongIM.getInstance() != null)
//                    RongIM.getInstance().startPrivateChat(MainActivity.this, "18610301342", "犇犇");

                //启动会话列表界面
//                if (RongIM.getInstance() != null)
//                    RongIM.getInstance().startConversationList(MainActivity.this);

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

                Log.e("benben", "——----------------------onError—-------------------------------" + errorCode);
            }
        });

        /**********************************************************************************************/

    }

    /**
     * 接收未读消息的监听器。
     */
    private class MyReceiveUnreadCountChangedListener implements RongIM.OnReceiveUnreadCountChangedListener {

        /**
         * @param count 未读消息数。
         */
        @Override
        public void onMessageIncreased(int count) {
            Log.e("benben", "----------------------------------" + count);
            button_information.setNotificationNumber(count);
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
        if (isQuit == false) {
            isQuit = true;
            //MyToast.getToast(MainActivity.this);
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
        button_information = (NotificationButton) findViewById(R.id.button_information);
        button_release = (FrameLayout) findViewById(R.id.button_release);
        linear = (LinearLayout) findViewById(R.id.linear);
        for (int i = 0; i < linear.getChildCount(); i++) {
            linear.getChildAt(i).setOnClickListener(this);
        }
    }

    private void selectedFragment() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        homePageFragment = new HomePageFragment();
        transaction.add(R.id.main_frameyout, homePageFragment);
        transaction.commit();
        informationFragment = new InformationFragment();
        myFragment = new MyFragment();
        releaseFragment = new ReleaseFragment();
        searchFragment = new SearchFragment();
    }

    @Override
    public void onClick(View v) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        switch (v.getId()) {
            //homepager页面
            case R.id.button_homePage:

                selectTab(0);
                //判断是否被add过
                if (!homePageFragment.isAdded()) {
                    if (releaseFragment.isVisible()) {
                        transaction.hide(releaseFragment).add(R.id.main_frameyout, homePageFragment).commit();
                    } else if (informationFragment.isVisible()) {
                        transaction.hide(informationFragment).add(R.id.main_frameyout, homePageFragment).commit();
                    } else if (myFragment.isVisible()) {
                        transaction.hide(myFragment).add(R.id.main_frameyout, homePageFragment).commit();
                    } else if (searchFragment.isVisible()) {
                        transaction.hide(searchFragment).add(R.id.main_frameyout, homePageFragment).commit();
                    }
                } else {
                    if (releaseFragment.isVisible()) {
                        transaction.hide(releaseFragment).show(homePageFragment).commit();
                    } else if (myFragment.isVisible()) {
                        transaction.hide(myFragment).show(homePageFragment).commit();
                    } else if (informationFragment.isVisible()) {
                        transaction.hide(informationFragment).show(homePageFragment).commit();
                    } else if (searchFragment.isVisible()) {
                        transaction.hide(searchFragment).show(homePageFragment).commit();
                    }
                }
                break;
            //search页面
            case R.id.button_search:
                selectTab(1);
                if (!searchFragment.isAdded()) {
                    if (homePageFragment.isVisible()) {
                        transaction.hide(homePageFragment).add(R.id.main_frameyout, searchFragment).commit();
                    } else if (informationFragment.isVisible()) {
                        transaction.hide(informationFragment).add(R.id.main_frameyout, searchFragment).commit();
                    } else if (myFragment.isVisible()) {
                        transaction.hide(myFragment).add(R.id.main_frameyout, searchFragment).commit();
                    } else if (releaseFragment.isVisible()) {
                        transaction.hide(releaseFragment).add(R.id.main_frameyout, searchFragment).commit();
                    }
                } else {
                    if (homePageFragment.isVisible()) {
                        transaction.hide(homePageFragment).show(searchFragment).commit();
                    } else if (informationFragment.isVisible()) {
                        transaction.hide(informationFragment).show(searchFragment).commit();
                    } else if (myFragment.isVisible()) {
                        transaction.hide(myFragment).show(searchFragment).commit();
                    } else if (releaseFragment.isVisible()) {
                        transaction.hide(releaseFragment).show(searchFragment).commit();
                    }
                }
                break;
            //release页面
            case R.id.button_release:
                selectTab(2);
                if (!releaseFragment.isAdded()) {
                    if (homePageFragment.isVisible()) {
                        transaction.hide(homePageFragment).add(R.id.main_frameyout, releaseFragment).commit();
                    } else if (informationFragment.isVisible()) {
                        transaction.hide(informationFragment).add(R.id.main_frameyout, releaseFragment).commit();
                    } else if (myFragment.isVisible()) {
                        transaction.hide(myFragment).add(R.id.main_frameyout, releaseFragment).commit();
                    } else if (searchFragment.isVisible()) {
                        transaction.hide(searchFragment).add(R.id.main_frameyout, releaseFragment).commit();
                    }
                } else {
                    if (homePageFragment.isVisible()) {
                        transaction.hide(homePageFragment).show(releaseFragment).commit();
                    } else if (informationFragment.isVisible()) {
                        transaction.hide(informationFragment).show(releaseFragment).commit();
                    } else if (myFragment.isVisible()) {
                        transaction.hide(myFragment).show(releaseFragment).commit();
                    } else if (searchFragment.isVisible()) {
                        transaction.hide(searchFragment).show(releaseFragment).commit();
                    }
                }
                break;
            //information 页面
            case R.id.button_information:

                SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
                boolean isLogin = sp.getBoolean("isLogin", false);
                if (isLogin){
                    selectTab(3);
                    if (!informationFragment.isAdded()) {
                        if (homePageFragment.isVisible()) {
                            transaction.hide(homePageFragment).add(R.id.main_frameyout, informationFragment).commit();
                        } else if (releaseFragment.isVisible()) {
                            transaction.hide(releaseFragment).add(R.id.main_frameyout, informationFragment).commit();
                        } else if (myFragment.isVisible()) {
                            transaction.hide(myFragment).add(R.id.main_frameyout, informationFragment).commit();
                        } else if (searchFragment.isVisible()) {
                            transaction.hide(searchFragment).add(R.id.main_frameyout, informationFragment).commit();
                        }
                    } else {
                        if (homePageFragment.isVisible()) {
                            transaction.hide(homePageFragment).show(informationFragment).commit();
                        } else if (releaseFragment.isVisible()) {
                            transaction.hide(releaseFragment).show(informationFragment).commit();
                        } else if (myFragment.isVisible()) {
                            transaction.hide(myFragment).show(informationFragment).commit();
                        } else if (searchFragment.isVisible()) {
                            transaction.hide(searchFragment).show(informationFragment).commit();
                        }
                    }
                }else {
                    Intent intent = new Intent( MainActivity.this , LoginActivity.class ) ;
                    startActivity(intent);
                }

                break;
            //我的页面
            case R.id.button_me:
                selectTab(4);
                if (!myFragment.isAdded()) {
                    if (homePageFragment.isVisible()) {
                        transaction.hide(homePageFragment).add(R.id.main_frameyout, myFragment).commit();
                    } else if (releaseFragment.isVisible()) {
                        transaction.hide(releaseFragment).add(R.id.main_frameyout, myFragment).commit();
                    } else if (informationFragment.isVisible()) {
                        transaction.hide(informationFragment).add(R.id.main_frameyout, myFragment).commit();
                    } else if (searchFragment.isVisible()) {
                        transaction.hide(searchFragment).add(R.id.main_frameyout, myFragment).commit();
                    }
                } else {
                    if (homePageFragment.isVisible()) {
                        transaction.hide(homePageFragment).show(myFragment).commit();
                    } else if (releaseFragment.isVisible()) {
                        transaction.hide(releaseFragment).show(myFragment).commit();
                    } else if (informationFragment.isVisible()) {
                        transaction.hide(informationFragment).show(myFragment).commit();
                    } else if (searchFragment.isVisible()) {
                        transaction.hide(searchFragment).show(myFragment).commit();
                    }
                }
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
        } else {
            //ToastUtils.shortToast(this, "桌面图标显示，暂不支持您的机型");
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
            //Toast.makeText(this, "Xiaomi=>isSendOk=>1", Toast.LENGTH_LONG).show();
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

        //Toast.makeText(this, "Sony," + "isSendOk", Toast.LENGTH_LONG).show();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
