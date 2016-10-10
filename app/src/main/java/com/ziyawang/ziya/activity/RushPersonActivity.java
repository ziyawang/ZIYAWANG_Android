package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.MyPersonAdapter;
import com.ziyawang.ziya.entity.MyPersonEntity;
import com.ziyawang.ziya.tools.Url;

import java.util.List;

public class RushPersonActivity extends BaseActivity {

    private RelativeLayout pre ;
    private static String id ;
    private static String login  ;
    private String status ;
    private ListView myPerson_listView ;
    private MyPersonAdapter adapter ;
    private TextView niuniuniuniu ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_person);
        final SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);
        Intent intent = getIntent() ;
        id = intent.getStringExtra("id");
        status = intent.getStringExtra("status");

        //实例化组件
        initView() ;

        //加载数据
        loadData() ;

        //对pre设置监听回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("我的抢单人页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("我的抢单人页面");
        //统计时长
        MobclickAgent.onPause(this);
    }


    private void initView() {

        myPerson_listView = (ListView)findViewById(R.id.myPerson_listView ) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        niuniuniuniu = (TextView)findViewById(R.id.niuniuniuniu ) ;
    }

    private void loadData() {

        String urls = String.format(Url.RushList, id, login);
        HttpUtils utils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pagecount" , "100000");
        utils.configCurrentHttpCacheExpiry(1000) ;
        utils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben" , responseInfo.result ) ;

                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");

                String counts = jsonObj.getString("counts");
                if (!TextUtils.isEmpty(counts) && counts.equals("0")){
                    myPerson_listView.setVisibility(View.GONE);
                    niuniuniuniu.setVisibility(View.VISIBLE);
                }else {
                    List<MyPersonEntity> list = JSON.parseArray(result.toJSONString(), MyPersonEntity.class);
                    adapter = new MyPersonAdapter(RushPersonActivity.this , list , id  , status ) ;
                    myPerson_listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        });


    }
}
