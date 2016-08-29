package com.ziyawang.ziya.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

public class FindPwdActivity extends BaseActivity {

    private EditText register_editText_userName ;
    private EditText register_editText_smsCode ;
    private EditText register_editText_pwd_one ;
    private EditText register_editText_pwd_two ;

    private Button register_button_smsCode ;
    private Button register_button_register ;

    private MyProgressDialog dialog ;
    private MyProgressDialog myProgressDialog ;

    private SharedPreferences loginCode ;
    private SharedPreferences isLogin ;
    private SharedPreferences myNumber ;
    private SharedPreferences role ;

    private MyApplication app ;

    private RelativeLayout pre ;

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("找回密码页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("找回密码页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_login_head);//通知栏所需颜色

        //实例化组件
        initView() ;

        //获取验证码方法的回掉
        register_button_smsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取验证码
                registerSmsCode() ;
            }
        });

        //找回密码确认方法的回掉
        register_button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        //添加回退事件
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
                            ToastUtils.longToast(FindPwdActivity.this, "您输入的密码过短");
                        }else if (mpwd.length() > 16){
                            ToastUtils.longToast(FindPwdActivity.this , "您输入的密码过长");
                        }else {
                            //判断两次驶入的密码时候一致
                            if (register_editText_pwd_one.getText().toString().equals(register_editText_pwd_two.getText().toString())){

                                /* 显示ProgressDialog */
                                //在开始进行网络连接时显示进度条对话框
                                dialog = new MyProgressDialog(FindPwdActivity.this , "加载中请稍后...");
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
                                utils.send(HttpRequest.HttpMethod.POST, Url.FindPwd, params, new RequestCallBack<String>() {
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

                                                    Intent intent = new Intent(FindPwdActivity.this , MainActivity.class ) ;
                                                    startActivity(intent);
                                                    finish();

                                                    break;
                                                case "404":
                                                    ToastUtils.shortToast(FindPwdActivity.this, "登陆失败");
                                                    break;
                                                case "405":
                                                    ToastUtils.shortToast(FindPwdActivity.this , "您还未注册成为我们的会员。");
                                                    break;
                                                case "401":
                                                    ToastUtils.shortToast(FindPwdActivity.this , "参数错误");
                                                    break;
                                                case "402":
                                                    ToastUtils.shortToast(FindPwdActivity.this , "手机验证码错误");
                                                    break;
                                                case "504":
                                                    ToastUtils.shortToast(FindPwdActivity.this , "服务器异常，请稍后重试。");
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
                                        ToastUtils.shortToast(FindPwdActivity.this , "网络加载异常");
                                    }
                                });
                            }else{
                                ToastUtils.longToast(FindPwdActivity.this , "两次输入密码不一致");
                            }

                        }

                    }else {
                        ToastUtils.longToast(FindPwdActivity.this, "请输入您的密码");
                    }

                }else{
                    ToastUtils.longToast(FindPwdActivity.this, "请输入您的验证码");
                }

            }else {
                ToastUtils.longToast(FindPwdActivity.this , "请输入正确的手机号码");
            }
        }else {
            ToastUtils.longToast(FindPwdActivity.this, "请输入您的用户名");
        }
    }

    //获取验证码
    private void registerSmsCode() {

        final String edit_phoneNumber = register_editText_userName.getText().toString().replace(" ","");

        if (!TextUtils.isEmpty(edit_phoneNumber)){
            String muserName = edit_phoneNumber ;
            //判断输入的账号是否是一个真实有效的手机号
            if (muserName.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){

                /* 显示ProgressDialog */
                //在开始进行网络连接时显示进度条对话框
                myProgressDialog = new MyProgressDialog(FindPwdActivity.this , "正在获取验证码请稍后。。。");
                myProgressDialog.setCancelable(false);// 不可以用“返回键”取消
                myProgressDialog.show();

                //请求smsCode
                HttpUtils httpUtils = new HttpUtils() ;
                RequestParams params = new RequestParams() ;
                params.addBodyParameter("access_token" , "token");
                params.addBodyParameter("phonenumber", muserName);
                params.addBodyParameter("action", "login");

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
                                    ToastUtils.longToast(FindPwdActivity.this, "验证码发送成功");
                                    break;
                                case "401":
                                    ToastUtils.longToast(FindPwdActivity.this, "参数不正确");
                                    break;
                                case "403":
                                    ToastUtils.longToast(FindPwdActivity.this, "验证码发送失败");
                                    break;
                                case "406":
                                    ToastUtils.longToast(FindPwdActivity.this, "手机号码不存在，请核验。");
                                    break;
                                case "503":
                                    ToastUtils.longToast(FindPwdActivity.this, "服务器错误，验证码发送失败。");
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
                        ToastUtils.longToast(FindPwdActivity.this, "网络连接异常");
                    }
                }) ;

            }else {
                ToastUtils.longToast(FindPwdActivity.this , "请输入正确的手机号码");
            }
        }else {
            ToastUtils.longToast(FindPwdActivity.this, "请输入您的用户名");
        }
    }

    //实例化组件
    private void initView() {

        register_editText_userName = (EditText)findViewById(R.id.register_username) ;
        register_editText_userName.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        register_editText_smsCode = (EditText)findViewById(R.id.register_editText_smsCode) ;
        register_editText_smsCode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        register_editText_pwd_one = (EditText)findViewById(R.id.register_editText_pwd_one) ;
        register_editText_pwd_two = (EditText)findViewById(R.id.register_editText_pwd_two) ;

        register_button_smsCode = (Button)findViewById(R.id.register_button_get_smsCode) ;
        register_button_register = (Button)findViewById(R.id.register_button_register) ;

        pre = (RelativeLayout)findViewById(R.id.pre ) ;


        //为账号的格式添加监听事件
        register_editText_userName.addTextChangedListener(new TextWatcher() {
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
                    register_editText_userName.setText(sb.toString());
                    register_editText_userName.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
