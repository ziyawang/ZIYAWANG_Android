package com.ziyawang.ziya.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.SystemAdapter;
import com.ziyawang.ziya.entity.SystemEntity;
import com.ziyawang.ziya.tools.Json_System;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SystemInformationActivity extends BaseActivity {

    private RelativeLayout pre ;
    private TextView niuniuniuniu ;
    private ListView listView ;
    private SystemAdapter adpter ;

    private SharedPreferences spTextID ;

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_information);
        //添加回退事件
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        niuniuniuniu = (TextView)findViewById(R.id.niuniuniuniu) ;
        listView = (ListView)findViewById(R.id.listView) ;

        loadData() ;




    }

    private void loadData() {

        SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        if (isLogin){
            SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
            String login = loginCode.getString("loginCode", null);

            String urls = String.format(Url.GetMessage, login ) ;
            HttpUtils httpUtils = new HttpUtils() ;
            RequestParams params = new RequestParams() ;
            params.addBodyParameter("pagecount" , "10000" );
            httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("benben", responseInfo.result) ;
                    try {
                        JSONObject jsonObject = new JSONObject(responseInfo.result) ;
                        String status_code = jsonObject.getString("status_code");
                        switch (status_code){
                            case "200" :
                                String counts = jsonObject.getString("counts");
                                if ("0".equals(counts)){
                                    niuniuniuniu.setVisibility(View.VISIBLE);
                                }else {
                                    List<SystemEntity> list = Json_System.getParse(responseInfo.result);
                                    adpter = new SystemAdapter(SystemInformationActivity.this , list ) ;
                                    listView.setAdapter(adpter);
                                    adpter.notifyDataSetChanged();
                                    String textID = list.get(0).getTextID();
                                    //拿到用户缓存的spVideoID的值,用户看过的最新的VideoID
                                    spTextID = getSharedPreferences("TextID", MODE_PRIVATE);
                                    spTextID.edit().putString("TextID", textID).commit();
                                }
                                break;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    error.printStackTrace();
                    ToastUtils.shortToast(SystemInformationActivity.this, "网络连接异常");
                }
            }) ;

        }else {
            ToastUtils.shortToast( SystemInformationActivity.this , "还未登录");
        }


    }
}
