package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class FindPwdActivity extends BenBenActivity implements View.OnClickListener {
    //手机号输入框
    private XEditText register_editText_userName ;
    //验证码输入框
    private EditText register_editText_smsCode ;
    //首次输入密码
    private EditText register_editText_pwd_one ;
    //再一次输入密码
    private EditText register_editText_pwd_two ;
    //发送验证码按钮
    private Button register_button_smsCode ;
    //确定找回密码按钮
    private Button register_button_register ;
    //进度显示条
    private MyProgressDialog dialog ;
    //获取验证码进度条
    private MyProgressDialog myProgressDialog ;
    //sp
    private SharedPreferences loginCode ;
    private SharedPreferences isLogin ;
    private SharedPreferences myNumber ;
    private SharedPreferences role ;
    private SharedPreferences userId ;
    //返回按钮
    private RelativeLayout pre ;

    private SharedPreferences r_token ;

    //发送验证阿60s后，才可以再次发送。
    private int recLen =60;
    Timer timer = new Timer() ;

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
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_find_pwd);
    }

    @Override
    public void initViews() {
        register_editText_userName = (XEditText)findViewById(R.id.register_username) ;
        register_editText_userName.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        register_editText_smsCode = (EditText)findViewById(R.id.register_editText_smsCode) ;
        //register_editText_smsCode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        register_editText_pwd_one = (EditText)findViewById(R.id.register_editText_pwd_one) ;
        register_editText_pwd_two = (EditText)findViewById(R.id.register_editText_pwd_two) ;
        register_button_smsCode = (Button)findViewById(R.id.register_button_get_smsCode) ;
        register_button_register = (Button)findViewById(R.id.register_button_register) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
    }

    @Override
    public void initListeners() {
        register_button_smsCode.setOnClickListener(this);
        register_button_register.setOnClickListener(this);
        pre.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //为账号的格式添加监听事件,每三个加空格。
        register_editText_userName.setSeparator(" ");
        register_editText_userName.setPattern(new int[]{3, 4, 4});
    }

    //注册操作
    private void register() {
        final String edit_phoneNumber = register_editText_userName.getText().toString().replace(" ","");
        if (judgePhoneNumber(edit_phoneNumber)){
            //在开始进行网络连接时显示进度条对话框
            showBenDialog() ;
            //进行网络请求
            loadData(edit_phoneNumber) ;
        }
    }

    private void loadData(String muserName) {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        //将用户名和密码封装到Post体里面
        params.addBodyParameter("access_token" , "token");
        params.addBodyParameter("phonenumber" , muserName);
        params.addBodyParameter("password" , register_editText_pwd_one.getText().toString().trim() );
        params.addBodyParameter("smscode" , register_editText_smsCode.getText().toString().trim() );
        //发送请求
        utils.send(HttpRequest.HttpMethod.POST, Url.FindPwd, params, new RequestCallBack<String>() {
            //失败回调
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result) ;
                //关闭dialog
                hiddenBenDialog() ;
                //解析数据
                dealResult(responseInfo.result);
            }
            //失败回掉
            @Override
            public void onFailure(HttpException error, String msg) {
                //关闭dialog
                hiddenBenDialog() ;
                //打印失败回调log
                error.printStackTrace();
                //提示用户
                ToastUtils.shortToast(FindPwdActivity.this , "网络连接异常");
            }
        });
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
                        register_button_smsCode.setOnClickListener(FindPwdActivity.this);
                        register_button_smsCode.setText(R.string.register_button_getsmsCode);
                    }
                }
            });
        }
    };

    private void dealResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            final String status_code = jsonObject.getString("status_code");
            switch(status_code){
                case "200" :
                    final String ticket = jsonObject.getString("token");
                    //将找回密码得到的数据存储到sp里面。
                    initSp(jsonObject) ;

                    connect(ticket);
                    //直接跳转到主页面,并关闭当前页面
                    //goLoginActivity() ;
                    //ToastUtils.shortToast(FindPwdActivity.this, "登录成功");
                    break;
                case "404":
                    ToastUtils.shortToast(FindPwdActivity.this, "找回密码失败");
                    break;
                case "405":
                    ToastUtils.shortToast(FindPwdActivity.this , "账号不存在");
                    break;
                case "401":
                    ToastUtils.shortToast(FindPwdActivity.this , "参数错误");
                    break;
                case "402":
                    ToastUtils.shortToast(FindPwdActivity.this , "手机验证码错误");
                    break;
                case "504":
                    ToastUtils.shortToast(FindPwdActivity.this , "服务器异常，请稍后重试");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initSp(JSONObject jsonObject) throws JSONException {
        final String ticket = jsonObject.getString("token");
        String role_a = jsonObject.getString("role");
        String userID = jsonObject.getString("UserID");
        //Umeng的账号统计
        MobclickAgent.onProfileSignIn(userID);
        //将找回密码得到的数据存储到sp里面。
        loginCode = getSharedPreferences("loginCode" , MODE_PRIVATE );
        isLogin = getSharedPreferences("isLogin" , MODE_PRIVATE ) ;
        myNumber = getSharedPreferences("myNumber" , MODE_PRIVATE ) ;
        role = getSharedPreferences("role" , MODE_PRIVATE ) ;
        userId = getSharedPreferences("userId" , MODE_PRIVATE ) ;
        loginCode.edit().putString("loginCode", ticket).commit();
        isLogin.edit().putBoolean("isLogin", true).commit();
        myNumber.edit().putString("myNumber", register_editText_userName.getText().toString().replace(" ","")).commit();
        role.edit().putString("role", role_a).commit();
        userId.edit().putString("userId", userID).commit();
    }

    private void goLoginActivity() {
        Intent intent = new Intent(FindPwdActivity.this , MainActivity.class ) ;
        startActivity(intent);
        finish();
    }

    private void hiddenBenDialog() {
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    private void showBenDialog() {
        dialog = new MyProgressDialog(FindPwdActivity.this , "加载中请稍后...");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();
    }

    private boolean judgePhoneNumber(String edit_phoneNumber) {
        if (TextUtils.isEmpty(edit_phoneNumber)){
            ToastUtils.longToast(FindPwdActivity.this, "请输入手机号");
            return false ;
        }
        if (!edit_phoneNumber.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){
            ToastUtils.longToast(FindPwdActivity.this , "请输入正确的手机号码");
            return false ;
        }
        if (TextUtils.isEmpty(register_editText_smsCode.getText().toString().replace(" ", ""))){
            ToastUtils.longToast(FindPwdActivity.this, "请输入验证码");
            return false ;
        }
        if (TextUtils.isEmpty(register_editText_pwd_one.getText().toString())){
            ToastUtils.longToast(FindPwdActivity.this, "请输入密码");
            return false ;
        }
        if (register_editText_pwd_one.getText().toString().length() < 6){
            ToastUtils.longToast(FindPwdActivity.this, "请至少输入6位密码");
            return false ;
        }
        if (register_editText_pwd_one.getText().toString().length() > 16){
            ToastUtils.longToast(FindPwdActivity.this , "至多输入16位密码");
            return false ;
        }
        if (TextUtils.isEmpty(register_editText_pwd_two.getText().toString())){
            ToastUtils.longToast(FindPwdActivity.this, "请再次输入密码");
            return false ;
        }
        if (!register_editText_pwd_one.getText().toString().equals(register_editText_pwd_two.getText().toString())){
            ToastUtils.longToast(FindPwdActivity.this , "两次输入密码不一致");
            return false ;
        }
        return true ;
    }

    //获取验证码
    private void registerSmsCode() {
        final String edit_phoneNumber = register_editText_userName.getText().toString().replace(" ","");
        if (!TextUtils.isEmpty(edit_phoneNumber)){
            String muserName = edit_phoneNumber ;
            //判断输入的账号是否是一个真实有效的手机号
            if (muserName.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){
                //在开始进行网络连接时显示进度条对话框
                showSMSDialog() ;
                //请求smsCode
                loadSMSData(muserName) ;

            }else {
                ToastUtils.longToast(FindPwdActivity.this , "请输入正确的手机号码");
            }
        }else {
            ToastUtils.longToast(FindPwdActivity.this, "请输入手机号");
        }
    }

    private void loadSMSData(String muserName) {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("access_token" , "token");
        params.addBodyParameter("phonenumber", muserName);
        params.addBodyParameter("action", "login");
        httpUtils.send(HttpRequest.HttpMethod.POST, Url.GetSMS, params , new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //关闭dialog
                hiddenSMSDialog() ;
                Log.e("benben", responseInfo.result);
                dealSMSResult(responseInfo.result) ;
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                //关闭dialog
                hiddenSMSDialog() ;
                //打印失败信息的log
                error.printStackTrace();
                //Toast提示用户
                ToastUtils.longToast(FindPwdActivity.this, "网络连接异常");
            }
        }) ;
    }

    private void dealSMSResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            final String status_code = jsonObject.getString("status_code");
            switch (status_code) {
                case "200":
                    ToastUtils.longToast(FindPwdActivity.this, "验证码发送成功");
                    timer.schedule(task, 1000, 1000);
                    register_button_smsCode.setOnClickListener(null);
                    break;
                case "401":
                    ToastUtils.longToast(FindPwdActivity.this, "参数不正确");
                    break;
                case "403":
                    ToastUtils.longToast(FindPwdActivity.this, "验证码发送失败");
                    break;
                case "406":
                    ToastUtils.longToast(FindPwdActivity.this, "账号不存在");
                    break;
                case "503":
                    ToastUtils.longToast(FindPwdActivity.this, "服务器错误，验证码发送失败");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void hiddenSMSDialog() {
        if (myProgressDialog != null){
            myProgressDialog.dismiss();
        }
    }

    private void showSMSDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        myProgressDialog = new MyProgressDialog(FindPwdActivity.this , "正在获取验证码请稍后。。。");
        myProgressDialog.setCancelable(false);// 不可以用“返回键”取消
        myProgressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.pre :
                finish();
                break;
            //获取验证码方法的回掉
            case R.id.register_button_get_smsCode :
                //获取验证码
                registerSmsCode();
                break;
            //找回密码确认方法的回掉
            case R.id.register_button_register :
                register();
                break;
            default:
                break;
        }
    }

    private void connect(final String ticket) {

        r_token = getSharedPreferences("r_token", MODE_PRIVATE);
        final String cloud_token = r_token.getString("r_token", "");
        RongIM.connect(cloud_token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
                String urls = String.format(Url.RCToken, ticket);
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
                                    connect(ticket);
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
                        //return null;
                        //return new UserInfo( userId , "牛牛" ,  uir ) ;
                        return null;
                    }
                }, true);

                Log.e("benben", "—--------------------------------—onSuccess—--------------------------------" + userId);

                finish();
                MyApplication.finishSingleActivityByClass(LoginActivity.class);
                ToastUtils.shortToast(FindPwdActivity.this, "登录成功");

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

                Log.e("benben", "——----------------------onError—-------------------------------" + errorCode);
            }
        });

        /**********************************************************************************************/

    }

}
