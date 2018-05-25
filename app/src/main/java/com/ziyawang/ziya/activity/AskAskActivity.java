package com.ziyawang.ziya.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.ziyawang.ziya.view.XEditText;

public class AskAskActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;

    private XEditText edit_word ;
    private TextView word_notice ;
    private TextView text_submit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_ask_ask);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        edit_word = (XEditText)findViewById(R.id.edit_word ) ;
        word_notice = (TextView)findViewById(R.id.word_notice ) ;
        text_submit = (TextView)findViewById(R.id.text_submit ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        text_submit.setOnClickListener(this);
        edit_word.setOnXTextChangeListener(new XEditText.OnXTextChangeListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = edit_word.getText().toString().trim().length();
                if (length <= 50){
                    int benben = 50-length ;
                    word_notice.setText(benben + "");
                }else {
                    word_notice.setText("0");
                }

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.text_submit :
                if (isSubmit()){
                    toSubmit() ;
                }
                break;
            default:
                break;
        }
    }

    private void toSubmit() {
        String urls = String.format(Url.myQuestion, GetBenSharedPreferences.getTicket(AskAskActivity.this)) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("question" , edit_word.getText().toString().trim());
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("question" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code){
                    case "200" :
                        ToastUtils.shortToast(AskAskActivity.this , "您的提问将在10天之内得到解答，请在“问吧”或“我的提问”查看问答动态。");
                        finish();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(AskAskActivity.this , "网络连接异常");
            }
        }) ;
    }

    public boolean isSubmit() {
        if (TextUtils.isEmpty(edit_word.getText().toString())){
            ToastUtils.shortToast(AskAskActivity.this , "最输入您的提问");
            return false ;
        }
        if (edit_word.getText().toString().trim().length() > 50){
            ToastUtils.shortToast(AskAskActivity.this , "最多可以输入50字");
            return false ;
        }
        return true;
    }
}
