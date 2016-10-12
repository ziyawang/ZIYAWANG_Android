package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.EmojiEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeNickNameActivity extends BenBenActivity implements View.OnClickListener {
    //返回按钮
    private RelativeLayout pre ;
    //保存按钮
    private TextView changeUserName_ok ;
    //输入框
    private EmojiEditText changeUserName_editText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_change_nick_name);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        changeUserName_ok = (TextView)findViewById(R.id.changeUserName_ok ) ;
        changeUserName_editText = (EmojiEditText)findViewById(R.id.changeUserName_editText ) ;

    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        changeUserName_ok.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        String username = intent.getStringExtra("username");
        if (!username.equals("您还未设置您的昵称")){
            changeUserName_editText.setText(username);
            changeUserName_editText.setSelection(username.length());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.changeUserName_ok :
                submitUserName() ;
                break;
            default:
                break;
        }
    }

    private void submitUserName() {

        if (judge()){
            String urls = String.format(Url.ChangeNickName, GetBenSharedPreferences.getTicket(this)) ;
            HttpUtils httpUtils = new HttpUtils() ;
            RequestParams params = new RequestParams() ;
            params.addBodyParameter("username", changeUserName_editText.getText().toString().trim() );
            httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("benben" , responseInfo.result ) ;
                    try {
                        dealResult(responseInfo.result ) ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    error.printStackTrace();
                    ToastUtils.shortToast(ChangeNickNameActivity.this , "网络连接异常，请稍后再试");
                }
            }) ;
        }
    }

    private boolean judge() {
        if (TextUtils.isEmpty(changeUserName_editText.getText().toString().trim())){
            ToastUtils.shortToast(ChangeNickNameActivity.this , "请输入您要修改的昵称。");
            return false ;
        }
        if (changeUserName_editText.getText().toString().trim().length() > 16 ){
            ToastUtils.shortToast(ChangeNickNameActivity.this , "您输入的昵称过长。");
            return false ;
        }
        return true ;
    }

    private void dealResult(String result) throws JSONException {
        JSONObject object = new JSONObject(result) ;
        String status_code = object.getString("status_code");
        switch (status_code){
            case "200" :
                ToastUtils.shortToast(ChangeNickNameActivity.this , "昵称修改成功！");

                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("username", changeUserName_editText.getText().toString().trim() );
                //设置返回数据
                ChangeNickNameActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ChangeNickNameActivity.this.finish();
                finish();
                break;
            case "419" :
                ToastUtils.shortToast(ChangeNickNameActivity.this , "昵称已存在！");
                break;
            case "420" :
                ToastUtils.shortToast(ChangeNickNameActivity.this , "昵称修改失败！");
                break;
            default:
                break;
        }
    }
}
