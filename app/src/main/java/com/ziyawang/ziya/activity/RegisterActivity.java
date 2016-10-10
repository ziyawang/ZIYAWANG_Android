package com.ziyawang.ziya.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.ziyawang.ziya.view.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private XEditText register_editText_userName ;
    private EditText register_editText_smsCode ;
    private EditText register_editText_pwd_one ;
    private EditText register_editText_pwd_two ;
    private Button register_button_smsCode ;
    private Button register_button_register ;
    //private CheckBox register_checkBox ;
    private MyProgressDialog dialog , myProgressDialog ;
    private SharedPreferences loginCode ;
    private SharedPreferences isLogin ;
    private SharedPreferences myNumber ;
    private SharedPreferences role ;
    private SharedPreferences userId ;
    private MyApplication app ;
    private RelativeLayout pre ;
    //发送验证阿60s后，才可以再次发送。
    private int recLen =60;
    Timer timer = new Timer() ;
    private TextView register_rule ;


    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("注册页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("注册页面");
        //统计时长
        MobclickAgent.onPause(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        register_button_smsCode.setOnClickListener(this);
        register_rule.setOnClickListener(this);


        //注册方法的回掉
        register_button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (!register_checkBox.isChecked()) {
                //    ToastUtils.shortToast(RegisterActivity.this , "您还未同意我们的协议");
                //} else {
                    register();
                //}
            }
        });
        //返回页面的监听回调
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


    //注册操作
    private void register() {

        final String edit_phoneNumber = register_editText_userName.getText().toString().replace(" ","");

        if (!TextUtils.isEmpty(edit_phoneNumber)){
            final String muserName = edit_phoneNumber ;
            //判断输入的账号是否是一个真实有效的手机号
            if (muserName.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){

                //判断验证码是否为空
                if (!TextUtils.isEmpty(register_editText_smsCode.getText().toString().replace(" ", ""))){
                    String smsCode = register_editText_smsCode.getText().toString().replace(" ", "") ;

                    //判断密码是否为空
                    if (!TextUtils.isEmpty(register_editText_pwd_one.getText().toString())){
                        String mpwd = register_editText_pwd_one.getText().toString();
                        if (mpwd.length() < 6 ){
                            ToastUtils.longToast(RegisterActivity.this, "请至少输入6位密码");
                        }else if (mpwd.length() > 16){
                            ToastUtils.longToast(RegisterActivity.this , "至多输入16位密码");
                        }else {
                            if (!TextUtils.isEmpty(register_editText_pwd_two.getText().toString())){
                                //判断两次驶入的密码时候一致
                                if (register_editText_pwd_one.getText().toString().equals(register_editText_pwd_two.getText().toString())){

                                /* 显示ProgressDialog */
                                    //在开始进行网络连接时显示进度条对话框
                                    dialog = new MyProgressDialog(RegisterActivity.this , "加载中请稍后...");
                                    dialog.setCancelable(false);// 不可以用“返回键”取消
                                    dialog.show();

                                    //进行网络请求
                                    HttpUtils utils = new HttpUtils();
                                    RequestParams params = new RequestParams();
                                    //将用户名和密码封装到Post体里面
                                    params.addBodyParameter("access_token" , "token");
                                    params.addBodyParameter("phonenumber" , muserName);
                                    params.addBodyParameter("password" , mpwd );
                                    params.addBodyParameter("smscode" , smsCode );
                                    //发送请求
                                    utils.send( HttpRequest.HttpMethod.POST , Url.Register , params, new RequestCallBack<String>() {
                                        //失败回调
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
                                                        ToastUtils.shortToast(RegisterActivity.this , "注册成功");
                                                        final String ticket = jsonObject.getString("token");
                                                        String role_a = jsonObject.getString("role");

                                                        String userID = jsonObject.getString("UserID");
                                                        MobclickAgent.onProfileSignIn(userID);

                                                        loginCode = getSharedPreferences("loginCode" , MODE_PRIVATE );
                                                        isLogin = getSharedPreferences("isLogin" , MODE_PRIVATE ) ;
                                                        myNumber = getSharedPreferences("myNumber" , MODE_PRIVATE ) ;
                                                        role = getSharedPreferences("role" , MODE_PRIVATE ) ;
                                                        userId = getSharedPreferences("userId" , MODE_PRIVATE ) ;

                                                        loginCode.edit().putString("loginCode", ticket).commit();
                                                        isLogin.edit().putBoolean("isLogin", true).commit();
                                                        myNumber.edit().putString("myNumber", muserName).commit();
                                                        role.edit().putString("role", role_a).commit();
                                                        userId.edit().putString("userId", userID).commit();

                                                        Intent intent = new Intent(RegisterActivity.this , MainActivity.class ) ;
                                                        startActivity(intent);
                                                        finish();

                                                        break;
                                                    case "405":
                                                        ToastUtils.shortToast(RegisterActivity.this, "该账号已注册，请直接登录");
                                                        break;
                                                    case "402":
                                                        ToastUtils.shortToast(RegisterActivity.this , "验证码错误");
                                                        break;
                                                    case "401":
                                                        ToastUtils.shortToast(RegisterActivity.this , "参数错误");
                                                        break;
                                                    case "501":
                                                        ToastUtils.shortToast(RegisterActivity.this , "服务器异常，请稍后重试");
                                                        break;
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        //成功回掉
                                        @Override
                                        public void onFailure(HttpException error, String msg) {
                                            if (dialog!=null){
                                                dialog.dismiss();
                                            }
                                            error.printStackTrace();
                                            ToastUtils.shortToast(RegisterActivity.this , "网络连接异常");
                                        }
                                    });
                                }else{
                                    ToastUtils.longToast(RegisterActivity.this , "两次密码输入不一致");
                                }
                            }else {
                                ToastUtils.longToast(RegisterActivity.this, "请再次输入密码");
                            }


                        }

                    }else {
                        ToastUtils.longToast(RegisterActivity.this, "请输入密码");
                    }

                }else{
                    ToastUtils.longToast(RegisterActivity.this, "请输入验证码");
                }

            }else {
                ToastUtils.longToast(RegisterActivity.this , "请输入正确的手机号码");
            }
        }else {
            ToastUtils.longToast(RegisterActivity.this, "请输入手机号");
        }
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recLen--;
                    register_button_smsCode.setText(recLen + " s");
                    if (recLen == 0){
                        timer.cancel();
                        register_button_smsCode.setOnClickListener(RegisterActivity.this);
                        register_button_smsCode.setText(R.string.register_button_getsmsCode);
                    }
                }
            });
        }
    };

    //获取验证码
    private void registerSmsCode() {

        final String edit_phoneNumber = register_editText_userName.getText().toString().replace(" ","");

        if (!TextUtils.isEmpty(edit_phoneNumber)){
            String muserName = edit_phoneNumber ;
            //判断输入的账号是否是一个真实有效的手机号
            if (muserName.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){

                /* 显示ProgressDialog */
                //在开始进行网络连接时显示进度条对话框
                myProgressDialog = new MyProgressDialog(RegisterActivity.this , "加载中请稍后...");
                myProgressDialog.setCancelable(false);// 不可以用“返回键”取消
                myProgressDialog.show();

                //请求smsCode
                HttpUtils httpUtils = new HttpUtils() ;
                RequestParams params = new RequestParams() ;
                params.addBodyParameter("access_token" , "token");
                params.addBodyParameter("phonenumber", muserName);
                params.addBodyParameter("action", "register");

                httpUtils.send(HttpRequest.HttpMethod.POST, Url.GetSMS, params , new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        myProgressDialog.dismiss();

                        Log.e("benben", responseInfo.result);
                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                            final String status_code = jsonObject.getString("status_code");
                            switch (status_code) {
                                case "200":
                                    ToastUtils.longToast(RegisterActivity.this, "验证码发送成功");
                                    timer.schedule(task, 1000, 1000);
                                    register_button_smsCode.setOnClickListener(null);
                                    break;
                                case "401":
                                    ToastUtils.longToast(RegisterActivity.this, "参数不正确");
                                    break;
                                case "403":
                                    ToastUtils.longToast(RegisterActivity.this, "验证码发送失败");
                                    break;
                                case "405":
                                    ToastUtils.longToast(RegisterActivity.this, "该账户已注册，请直接登录");
                                    break;
                                case "503":
                                    ToastUtils.longToast(RegisterActivity.this, "服务器错误，验证码发送失败");
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        myProgressDialog.dismiss();
                        error.printStackTrace();
                        ToastUtils.longToast(RegisterActivity.this, "网络连接异常");
                    }
                }) ;

            }else {
                ToastUtils.longToast(RegisterActivity.this , "请输入正确的手机号码");
            }
        }else {
            ToastUtils.longToast(RegisterActivity.this, "请输入手机号");
        }
    }


    //实例化组件
    private void initView() {

        register_editText_userName = (XEditText)findViewById(R.id.register_username) ;
        register_editText_userName.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        register_editText_smsCode = (EditText)findViewById(R.id.register_editText_smsCode) ;
        //register_editText_smsCode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        register_editText_pwd_one = (EditText)findViewById(R.id.register_editText_pwd_one) ;
        register_editText_pwd_two = (EditText)findViewById(R.id.register_editText_pwd_two) ;

        register_button_smsCode = (Button)findViewById(R.id.register_button_get_smsCode) ;
        register_button_register = (Button)findViewById(R.id.register_button_register) ;

        //register_checkBox = (CheckBox)findViewById(R.id.register_checkBox) ;

        register_rule = (TextView)findViewById(R.id.register_rule ) ;

        pre = (RelativeLayout)findViewById(R.id.pre ) ;

        register_editText_userName.setSeparator(" ");
        register_editText_userName.setPattern(new int[]{3, 4, 4});

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button_get_smsCode :
                //获取验证码
                registerSmsCode() ;
                break;
            case R.id.register_rule :
                //跳转资芽公约页面。
                goMyRuleActivity() ;
                break;
            default:
                break;
        }
    }

    private void goMyRuleActivity() {
        Intent intent = new Intent(RegisterActivity.this , MyRuleActivity.class ) ;
        intent.putExtra("type" , "rule" ) ;
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
        }
    }
}
