package com.ziyawang.ziya.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ziyawang.ziya.tools.SystemBarTintManager;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePwdActivity extends BaseActivity {

    private TextView changePwd_ok ;
    private RelativeLayout pre ;

    private EditText changePwd_editText ;

    private String login ;

    private MyProgressDialog dialog ;

    public void onResume() {

        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("更改密码页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("更改密码页面");
        //统计时长
        MobclickAgent.onPause(this);



    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        //设置通知栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.aaa);//通知栏所需颜色
        SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);

        //实例化组件
        initView() ;
        //监听回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //保存事件
        changePwd_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //判断密码是否为空
                if (!TextUtils.isEmpty(changePwd_editText.getText().toString().trim())){
                    String s = changePwd_editText.getText().toString();
                    if (changePwd_editText.length() < 6 ){
                        ToastUtils.longToast(ChangePwdActivity.this, "您输入的密码过短");
                    }else if (changePwd_editText.length() > 16){
                        ToastUtils.longToast(ChangePwdActivity.this , "您输入的密码过长");
                    }else {

                        /* 显示ProgressDialog */
                        //在开始进行网络连接时显示进度条对话框
                        dialog = new MyProgressDialog(ChangePwdActivity.this , "修改密码中，请稍后。。。");
                        dialog.setCancelable(false);// 不可以用“返回键”取消
                        dialog.show();

                        String urls = String.format(Url.ChangePwd, login);
                        HttpUtils utils = new HttpUtils() ;
                        RequestParams params = new RequestParams() ;
                        params.addBodyParameter("password" , s );
                        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {

                                if (dialog!= null){
                                    dialog.dismiss();
                                }
                                Log.e("benben", responseInfo.result);
                                try {
                                    JSONObject jsonObject = new JSONObject(responseInfo.result) ;
                                    String status_code = jsonObject.getString("status_code");
                                    switch (status_code){
                                        case "200" :
                                            ToastUtils.shortToast(ChangePwdActivity.this , "密码修改成功");
                                            finish();
                                            break;
                                        case "410" :
                                            ToastUtils.shortToast(ChangePwdActivity.this , "密码修改失败");
                                            break;
                                        default:
                                            ToastUtils.shortToast(ChangePwdActivity.this , "密码修改失败");
                                            break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure(HttpException error, String msg) {
                                error.printStackTrace();
                                if (dialog!= null){
                                    dialog.dismiss();
                                }
                                ToastUtils.shortToast(ChangePwdActivity.this, "网络连接异常");
                            }
                        }) ;
                    }
                }else {
                    ToastUtils.shortToast(ChangePwdActivity.this , "请输入密码");
                }
            }
        });

    }


    private void initView() {

        changePwd_ok = (TextView)findViewById(R.id.changePwd_ok) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        changePwd_editText = (EditText)findViewById(R.id.changePwd_editText) ;
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
