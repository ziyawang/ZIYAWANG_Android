package com.ziyawang.ziya.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.ziyawang.ziya.tools.SystemBarTintManager;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class LoginActivity extends BaseActivity {

    private EditText userName  ;
    private EditText pwd ;
    private Button register ;
    private Button login ;

    private SharedPreferences loginCode ;
    private SharedPreferences isLogin ;
    private SharedPreferences myNumber ;
    private SharedPreferences role ;

    private MyProgressDialog dialog  ;
    private ProgressBar bar  ;

    private TextView register_textView_find_pwd ;

    private MyApplication app ;

    private RelativeLayout pre ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_login_head);//通知栏所需颜色

        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);


        //实例化组件
        initView() ;

        //登陆按钮的回掉方法
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login() ;

            }
        });

        //注册按钮的回掉方法
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , RegisterActivity.class ) ;
                startActivity(intent);
            }
        });

        //忘记密码的回掉方法
        register_textView_find_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPwd() ;
            }
        });

        //回退页面的监听事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    private void findPwd() {

        Intent intent = new Intent(LoginActivity.this , FindPwdActivity.class ) ;
        startActivity(intent);

    }

    private void login() {


        final String edit_phoneNumber = userName.getText().toString().replace(" ","");

        if (!TextUtils.isEmpty(edit_phoneNumber)){
            final String muserName = edit_phoneNumber ;
            //判断输入的账号是否是一个真实有效的手机号
            if (muserName.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){
                //判断密码是否为空
                if (!TextUtils.isEmpty(pwd.getText().toString())){
                    String mpwd = pwd.getText().toString();
                    if (pwd.length() < 6 ){
                        ToastUtils.longToast(LoginActivity.this, "您输入的密码过短");
                    }else if (pwd.length() > 16){
                        ToastUtils.longToast(LoginActivity.this , "您输入的密码过长");
                    }else {

                       /* 显示ProgressDialog */
                        //在开始进行网络连接时显示进度条对话框
                        dialog = new MyProgressDialog(LoginActivity.this , "正在登陆，请稍后。。。");
                        dialog.setCancelable(false);// 不可以用“返回键”取消
                        dialog.show();
                        //进行网络请求
                        HttpUtils utils = new HttpUtils();
                        RequestParams params = new RequestParams();
                        //将用户名和密码封装到Post体里面
                        params.addBodyParameter("access_token" , "token");
                        params.addBodyParameter("phonenumber" , muserName);
                        params.addBodyParameter("password" , mpwd );
                        //发送请求
                        utils.send(HttpRequest.HttpMethod.POST, Url.Login, params, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Log.e("benben", responseInfo.result) ;

                                if (dialog!=null){
                                    dialog.dismiss();
                                }
                                try {
                                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                                    final String status_code = jsonObject.getString("status_code");
                                    switch(status_code){
                                        case "200" :
                                            final String ticket = jsonObject.getString("token");
                                            String role_a = jsonObject.getString("role");

                                            String userID = jsonObject.getString("UserID");
                                            MobclickAgent.onProfileSignIn(userID);

                                            loginCode = getSharedPreferences("loginCode" , MODE_PRIVATE );
                                            isLogin = getSharedPreferences("isLogin" , MODE_PRIVATE ) ;
                                            myNumber = getSharedPreferences("myNumber" , MODE_PRIVATE ) ;
                                            role = getSharedPreferences("role" , MODE_PRIVATE ) ;

                                            loginCode.edit().putString("loginCode", ticket).commit();
                                            isLogin.edit().putBoolean("isLogin", true).commit();
                                            myNumber.edit().putString("myNumber", muserName).commit();
                                            role.edit().putString("role", role_a).commit();


                                            Intent intent = new Intent(LoginActivity.this , MainActivity.class ) ;
                                            startActivity(intent);
                                            finish();

                                            break;
                                        case "404":
                                            ToastUtils.shortToast(LoginActivity.this, "用户名或密码错误");
                                            break;
                                        case "406":
                                            ToastUtils.shortToast(LoginActivity.this , "用户不存在");
                                            break;
                                        case "502":
                                            ToastUtils.shortToast(LoginActivity.this , "服务器异常，请稍后重试。");
                                            break;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                if (dialog!=null){
                                    dialog.dismiss();
                                }
                                error.printStackTrace();
                                ToastUtils.shortToast(LoginActivity.this , "网络加载异常");
                            }
                        });
                    }

                }else {
                    ToastUtils.longToast(LoginActivity.this, "请输入您的密码");
                }
            }else {
                ToastUtils.longToast(LoginActivity.this , "请输入正确的手机号码");
            }

        }else {
            ToastUtils.longToast(LoginActivity.this, "请输入您的用户名");
        }

    }

//    private void connectRongYun(String a ) {
//
//        String Token = "l1mGz2SFxOi6bz2biGzGbmtgz2fIXi50V82Gr9MQdd2ldsTSjeB2DywVTJyDjA0YjX0HWJ0L9c3Ujl3MCj9Wy/9JSQ9r0dkF" ;
//
//        RongIM.connect(Token, new RongIMClient.ConnectCallback() {
//            @Override
//            public void onTokenIncorrect() {
//
//                //Connect Token 失效的状态处理，需要重新获取 Token
//                Log.d("benben", "——————————————————————————————--Token过期————————————————————————————————");
//            }
//
//            @Override
//            public void onSuccess(String userId) {
//
//                Log.e("benben", "—--------------------------------—onSuccess—--------------------------------" + userId);
//
//                if (RongIM.getInstance() != null) {
//
//                    Log.e("benben", "--------------------------");
//                    /**
//                     * 接收未读消息的监听器。
//                     *
//                     * @param listener          接收所有未读消息消息的监听器。
//                     */
//                    RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new MyReceiveUnreadCountChangedListener(), Conversation.ConversationType.PRIVATE);
//
//                }
//
//
//                //启动会话界面
////                if (RongIM.getInstance() != null)
////                    RongIM.getInstance().startPrivateChat(MainActivity.this, "18610301342", "犇犇");
//
//                //启动会话列表界面
////                if (RongIM.getInstance() != null)
////                    RongIM.getInstance().startConversationList(MainActivity.this);
//
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//
//                Log.e("benben", "——----------------------onError—-------------------------------" + errorCode);
//            }
//        }) ;
//
//
//
//
//
//
//    }
//
//
//    /**
//     * 接收未读消息的监听器。
//     */
//    private class MyReceiveUnreadCountChangedListener implements RongIM.OnReceiveUnreadCountChangedListener {
//
//        /**
//         * @param count           未读消息数。
//         */
//        @Override
//        public void onMessageIncreased(int count) {
//            Log.e("benben" , "----------------------------------" + count ) ;
//            sendBadgeNumber(count) ;
//        }
//    }
//

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("登陆页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("登陆页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    private void initView() {
        userName = (EditText)findViewById(R.id.userName) ;
        userName.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        pwd = (EditText)findViewById(R.id.pwd) ;
        register = (Button)findViewById(R.id.register) ;
        login = (Button)findViewById(R.id.login) ;

        pre = (RelativeLayout)findViewById(R.id.pre ) ;

        register_textView_find_pwd = (TextView)findViewById(R.id.register_textView_find_pwd ) ;

        //为账号的格式添加监听事件
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    userName.setText(sb.toString());
                    userName.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
//
//    private void sendBadgeNumber(int count) {
//
//        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
//            if (count > 0 ){
//                sendToXiaoMi(count);
//            }
//
//        } else if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
//            sendToSony("" + count);
//        } else if (Build.MANUFACTURER.toLowerCase().contains("sony")) {
//            sendToSamsumg("" + count);
//        } else {
//            ToastUtils.shortToast(this ,"桌面图标显示，暂不支持您的机型" );
//        }
//    }
//
//    private void sendToXiaoMi(int number) {
//        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = null;
//        boolean isMiUIV6 = true;
//        try {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//            builder.setContentTitle("您有"+number+"未读消息");
//            builder.setTicker("您有"+number+"未读消息");
//            builder.setAutoCancel(true);
//            builder.setSmallIcon(R.drawable.ic_launcher);
//            builder.setDefaults(Notification.DEFAULT_LIGHTS);
//            notification = builder.build();
//            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
//            Object miuiNotification = miuiNotificationClass.newInstance();
//            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
//            field.setAccessible(true);
//            field.set(miuiNotification, number);// 设置信息数
//            field = notification.getClass().getField("extraNotification");
//            field.setAccessible(true);
//            field.set(notification, miuiNotification);
//            //Toast.makeText(this, "Xiaomi=>isSendOk=>1", Toast.LENGTH_LONG).show();
//        }catch (Exception e) {
//            e.printStackTrace();
//            //miui 6之前的版本
//            isMiUIV6 = false;
//            Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
//            localIntent.putExtra("android.intent.extra.update_application_component_name",getPackageName() + "/"+ lancherActivityClassName );
//            localIntent.putExtra("android.intent.extra.update_application_message_text",number);
//            sendBroadcast(localIntent);
//        }
//        finally {
//            if(notification!=null && isMiUIV6 ) {
//                //miui6以上版本需要使用通知发送
//                nm.notify(101010, notification);
//            }
//        }
//    }
//
//    private void sendToSony(String number) {
//        boolean isShow = true;
//        if ("0".equals(number)) {
//            isShow = false;
//        }
//        Intent localIntent = new Intent();
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE",isShow);//是否显示
//        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",lancherActivityClassName );//启动页
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", number);//数字
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME",getPackageName());//包名
//        sendBroadcast(localIntent);
//
//        Toast.makeText(this, "Sony," + "isSendOk", Toast.LENGTH_LONG).show();
//    }
//
//    private void sendToSamsumg(String number) {
//        Intent localIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
//        localIntent.putExtra("badge_count", number);//数字
//        localIntent.putExtra("badge_count_package_name", getPackageName());//包名
//        localIntent.putExtra("badge_count_class_name",lancherActivityClassName ); //启动页
//        sendBroadcast(localIntent);
//        Toast.makeText(this, "Samsumg," + "isSendOk", Toast.LENGTH_LONG).show();
//    }

}
