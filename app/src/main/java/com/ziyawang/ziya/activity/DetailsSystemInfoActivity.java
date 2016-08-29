package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

public class DetailsSystemInfoActivity extends BaseActivity {

    private RelativeLayout pre ;
    private TextView details_title ;
    private TextView details_time ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_system_info);

        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        details_title = (TextView)findViewById(R.id.details_title ) ;
        details_time = (TextView)findViewById(R.id.details_time ) ;

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent() ;
        if (intent != null ){

            String title = intent.getStringExtra("title");
            String time = intent.getStringExtra("time");
            String textId = intent.getStringExtra("textId");
            String text = intent.getStringExtra("text");
            String status = intent.getStringExtra("status");
            details_title.setText(text);
            details_time.setText(time);

            //未读消息
            if (status.equals("0")){
                changeStatus(textId  ) ;
            }

        }


    }

    private void changeStatus(String textId ) {

        SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        String login = loginCode.getString("loginCode", null);

        String urls = String.format(Url.ReadMessage,  login ) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("TextID" , textId  );
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben" , responseInfo.result )  ;
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                error.printStackTrace();
                ToastUtils.shortToast(DetailsSystemInfoActivity.this , "网络连接异常");
            }
        });

    }
}
