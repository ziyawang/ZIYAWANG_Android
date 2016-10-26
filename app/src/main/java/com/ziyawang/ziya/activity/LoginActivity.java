package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.paysdk.login.Login;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class LoginActivity extends BenBenActivity implements View.OnClickListener {

    //用户名输入框
    //private EditText userName  ;
    private XEditText userName  ;
    //密码输入框
    private EditText pwd ;
    //注册按钮
    private Button register ;
    //登陆按钮
    private Button login ;
    //sp
    private SharedPreferences loginCode ;
    private SharedPreferences isLogin ;
    private SharedPreferences myNumber ;
    private SharedPreferences role ;
    private SharedPreferences userId ;
    private SharedPreferences isLoad ;
    //数据加载
    private MyProgressDialog dialog  ;
    //找回密码按钮
    private TextView register_textView_find_pwd ;
    //返回按钮
    private RelativeLayout pre ;

    private SharedPreferences r_token ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initViews() {
        userName = (XEditText)findViewById(R.id.userName) ;
        userName.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        pwd = (EditText)findViewById(R.id.pwd) ;
        register = (Button)findViewById(R.id.register) ;
        login = (Button)findViewById(R.id.login) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        register_textView_find_pwd = (TextView)findViewById(R.id.register_textView_find_pwd ) ;
    }

    @Override
    public void initListeners() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        register_textView_find_pwd.setOnClickListener(this);
        pre.setOnClickListener(this);
    }

    @Override
    public void initData() {

        userName.setSeparator(" ");
        userName.setPattern(new int[]{3, 4, 4});
    }

    private void goFindPwdActivity() {
        Intent intent = new Intent(LoginActivity.this , FindPwdActivity.class ) ;
        startActivity(intent);
    }

    private void login() {
        final String edit_phoneNumber = userName.getText().toString().replace(" ","");
        if (judgePwd(edit_phoneNumber)){
            //显示Dialog
            shoeDialog() ;
            //进行网络请求
            loadData() ;
        }
    }

    private void loadData() {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        //将用户名和密码封装到Post体里面
        params.addBodyParameter("access_token" , "token");
        params.addBodyParameter("phonenumber" , userName.getText().toString().replace(" ","").trim() );
        params.addBodyParameter("password" , pwd.getText().toString().trim() );
        //发送请求
        utils.send(HttpRequest.HttpMethod.POST, Url.Login, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result) ;
                //隐藏dialog
                hiddenDialog() ;
                //处理result
                dealResult(responseInfo.result) ;
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //隐藏dialog
                hiddenDialog() ;
                //打印失败毁掉的log
                error.printStackTrace();
                //提示用户
                ToastUtils.shortToast(LoginActivity.this , "网络连接异常");
            }
        });
    }

    private void dealResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            final String status_code = jsonObject.getString("status_code");
            switch(status_code){
                case "200" :
                    final String ticket = jsonObject.getString("token");
                    //将sp存储
                    initSp(jsonObject) ;

                    connect(ticket);
                    //登陆成功，跳转到主页面,并关闭此页面
                    //goLoginActivity() ;
                    //ToastUtils.shortToast(LoginActivity.this , "登录成功");
                    break;
                case "404":
                    ToastUtils.shortToast(LoginActivity.this, "用户名或密码错误");
                    break;
                case "406":
                    ToastUtils.shortToast(LoginActivity.this , "账号不存在");
                    break;
                case "502":
                    ToastUtils.shortToast(LoginActivity.this , "服务器异常，请稍后重试。");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(LoginActivity.this , MainActivity.class ) ;
        startActivity(intent);
        finish();
    }

    private void initSp(JSONObject jsonObject) throws JSONException {
        final String ticket = jsonObject.getString("token");
        String role_a = jsonObject.getString("role");
        String userID = jsonObject.getString("UserID");
        //用于Umeng的新增账号统计
        MobclickAgent.onProfileSignIn(userID);

        loginCode = getSharedPreferences("loginCode" , MODE_PRIVATE );
        isLogin = getSharedPreferences("isLogin" , MODE_PRIVATE ) ;
        myNumber = getSharedPreferences("myNumber" , MODE_PRIVATE ) ;
        role = getSharedPreferences("role" , MODE_PRIVATE ) ;
        userId = getSharedPreferences("userId" , MODE_PRIVATE ) ;

        loginCode.edit().putString("loginCode", ticket).commit();
        isLogin.edit().putBoolean("isLogin", true).commit();
        myNumber.edit().putString("myNumber", userName.getText().toString().replace(" ", "").trim()).commit();
        role.edit().putString("role", role_a).commit();
        userId.edit().putString("userId", userID).commit();
    }

    private void hiddenDialog() {
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    private void shoeDialog() {
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(LoginActivity.this , "正在登陆，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();
    }

    private boolean judgePwd(String edit_phoneNumber) {
        if (TextUtils.isEmpty(edit_phoneNumber)){
            ToastUtils.longToast(LoginActivity.this, "请输入手机号");
            return false ;
        }
        if (!edit_phoneNumber.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){
            ToastUtils.longToast(LoginActivity.this , "请输入正确的手机号码");
            return false ;
        }
        if (TextUtils.isEmpty(pwd.getText().toString())){
            ToastUtils.longToast(LoginActivity.this , "请输入密码");
            return false ;
        }
        if (pwd.getText().toString().length() < 6){
            ToastUtils.longToast(LoginActivity.this, "请至少输入6位密码");
            return false ;
        }
        if (pwd.getText().toString().length() > 16){
            ToastUtils.longToast(LoginActivity.this, "至多输入16位密码");
            return false ;
        }
        return true ;
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //回退页面的监听事件
            case R.id.pre :
                finish();
                break;
            //登陆按钮的回掉方法
            case R.id.login :
                login() ;
                break;
            //注册按钮的回掉方法
            case R.id.register :
                goRegisterActivity() ;
                break;
            //忘记密码的回掉方法
            case R.id.register_textView_find_pwd :
                goFindPwdActivity() ;
                break;
            default:
                break;
        }
    }

    private void goRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this , RegisterActivity.class ) ;
        startActivity(intent);
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
                ToastUtils.shortToast(LoginActivity.this  , "登录成功");

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

                Log.e("benben", "——----------------------onError—-------------------------------" + errorCode);
            }
        });

        /**********************************************************************************************/

    }
}
