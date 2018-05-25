package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.entity.AskEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.JustifyTextView;

import java.util.List;

public class DetailsAskActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    private JustifyTextView text_question ;
    private TextView text_answer ;

    private ScrollView scrollView ;

    private String id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData() ;
    }

    private void loadData() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("id" , id );
        httpUtils.send(HttpRequest.HttpMethod.POST, Url.questionDetail, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("questionDetail" , responseInfo.result ) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                switch (status_code){
                    case "200" :
                        JSONArray data = object.getJSONArray("data");
                        if (data.size() != 0 ){
                            String question = data.getJSONObject(0).getString("question");
                            String answer = data.getJSONObject(0).getString("Answer");
                            text_question.setText(question);
                            text_answer.setText(answer);
                            scrollView.setVisibility(View.VISIBLE);
                        }else {
                            ToastUtils.shortToast(DetailsAskActivity.this , "数据异常");
                            finish();
                        }

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.shortToast(DetailsAskActivity.this , "网络连接异常");
                error.printStackTrace();
            }
        }) ;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_details_ask);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        text_question = (JustifyTextView) findViewById(R.id.text_question ) ;
        text_answer = (TextView)findViewById(R.id.text_answer ) ;
        scrollView  =(ScrollView)findViewById(R.id.scrollView ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        id = intent.getStringExtra("id");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            default:
                break;
        }
    }
}
