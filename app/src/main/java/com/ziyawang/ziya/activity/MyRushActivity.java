package com.ziyawang.ziya.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.MyRushAdapter;
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.tools.Json_FindInfo;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyRushActivity extends BaseActivity {


    private List<FindInfoEntity> list ;
    private MyRushAdapter adapter ;
    private List<FindInfoEntity> data  = new ArrayList<FindInfoEntity>();
    private static String login;
    private int page  ;
    private int count = 1 ;
    private Boolean isOK = true ;
    private MyProgressDialog dialog ;
    private MyScrollView scrollView ;
    private BenListView listView ;
    private RelativeLayout pre ;
    private TextView niuniuniuniu ;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 208) {
                Log.e("HomeINfo", count + "====================================" + page) ;
                if (isOK){
                    if (count <= page ){
                        isOK = false ;
                        dialog = new MyProgressDialog( MyRushActivity.this , "加载数据中请稍后。。。") ;
                        dialog.show();
                        loadData();
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    isOK = true ;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }else {
                        ToastUtils.shortToast(MyRushActivity.this, "没有更多数据");
                    }
                }
            }
            super.handleMessage(msg);
        }
    };


    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("我的抢单页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("我的抢单页面");
        //统计时长
        MobclickAgent.onPause(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rush);

        final SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);

        initView() ;
        
        loadData() ;

        //添加回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //添加分页加载
        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {

                View childView = scrollView.getChildAt(0);
                if (childView.getMeasuredHeight() <= scrollY + scrollView.getHeight()) {
                    mHandler.sendEmptyMessage(208);
                }
            }
        });
    }

    private void initView() {

        scrollView = (MyScrollView)findViewById(R.id.scrollView ) ;
        listView = (BenListView)findViewById(R.id.listView ) ;
        listView.setDivider(new ColorDrawable(Color.argb(0, 244, 244, 244)));
        listView.setDividerHeight(1);
        pre = (RelativeLayout)findViewById(R.id.pre ) ;

        niuniuniuniu = (TextView)findViewById(R.id.niuniuniuniu) ;

    }

    private void loadData() {

        String urls = String.format(Url.MyRush, login);

        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        httpUtils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (dialog != null) {
                    dialog.dismiss();
                }

                Log.e("benben" , responseInfo.result ) ;

                //Log.e("benben_myTeamWork", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    String pages = jsonObject.getString("pages");

                    String counts = jsonObject.getString("counts");
                    if (!TextUtils.isEmpty(counts) && counts.equals("0")){
                        scrollView.setVisibility(View.GONE);
                        niuniuniuniu.setVisibility(View.VISIBLE);
                    }else {
                        page = Integer.parseInt(pages);
                        count++;
                        Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);

                        try {
                            List<FindInfoEntity> list = Json_FindInfo.getParse(responseInfo.result);
                            data.addAll(list);
                            Log.e("benben", "数据的个数" + data.size());

                            for (int i = 0; i < list.size(); i++) {
                                String member = list.get(i).getMember();
                                Log.e("benben" , member ) ;
                            }
                            adapter = new MyRushAdapter(MyRushActivity.this, data , login );

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
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }) ;

    }
}
