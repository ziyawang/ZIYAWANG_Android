package com.ziyawang.ziya.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.CollectListAdapter;
import com.ziyawang.ziya.entity.MyCollectListEntity;
import com.ziyawang.ziya.tools.Json_MyCollectList;
import com.ziyawang.ziya.tools.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyCollectActivity extends BaseActivity {

    private static String login;
    private static boolean isLogin;
    private static String root;
    private static String spphoneNumber;
    private TextView niuniuniuniu ;
    private RelativeLayout pre  ;
    private ListView listView ;
    private CollectListAdapter adapter ;
    List<MyCollectListEntity> list ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);

        final SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);

        final SharedPreferences myNumber = getSharedPreferences("myNumber", MODE_PRIVATE);
        spphoneNumber = myNumber.getString("myNumber", null);

        final SharedPreferences role = getSharedPreferences("role", MODE_PRIVATE);
        root = role.getString("role", null);

        SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
        isLogin = sp.getBoolean("isLogin", false);

        initView();
        loadData() ;

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initView() {

        listView = (ListView)findViewById(R.id.listView ) ;
        listView.setDivider(new ColorDrawable(Color.argb(0, 244, 244, 244)));
        listView.setDividerHeight(1);
        pre = (RelativeLayout)findViewById(R.id.pre ) ;

        niuniuniuniu = (TextView)findViewById(R.id.niuniuniuniu ) ;
    }

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("我的收藏页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("我的收藏页面");
        //统计时长
        MobclickAgent.onPause(this);
    }


    private void loadData() {

        String urls = String.format(Url.MyCollectList, login ) ;

        Log.e("benben" , login ) ;

        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pagecount", "10000");
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        httpUtils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result) ;

                try {
                    JSONObject object = new JSONObject(responseInfo.result) ;
                    String counts = object.getString("counts");
                    if (!TextUtils.isEmpty(counts) && counts.equals("0")){
                        listView.setVisibility(View.GONE);
                        niuniuniuniu.setVisibility(View.VISIBLE);
                    }else {

                        listView.setVisibility(View.VISIBLE);
                        niuniuniuniu.setVisibility(View.GONE);

                        try {
                            list = Json_MyCollectList.getParse(responseInfo.result);

                            for (int i = 0; i < list.size(); i++) {
                                Log.e("benben" ,list.get(i).getWordDes() ) ;
                            }

                            adapter = new CollectListAdapter(MyCollectActivity.this , list ) ;
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                error.printStackTrace();
            }
        }) ;
    }
}
