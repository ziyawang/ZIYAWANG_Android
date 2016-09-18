package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
import com.ziyawang.ziya.adapter.MovieItemAdapter;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyGridView;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.MyScrollView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends BaseActivity {

    private RelativeLayout pre ;
    private TextView title ;
    private MyGridView gridView ;
    private MovieItemAdapter adapter ;
    private String title_a ;
    private List<FindVideoEntity> data  = new ArrayList<FindVideoEntity>();
    private int page  ;
    private int count = 1 ;
    private Boolean isOK = true ;
    private MyProgressDialog dialog ;
    private MyScrollView scrollView ;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 203) {
                // Bundle b = msg.getData();
                // tv.setText(b.getString("num"));
                Log.e("HomeINfo", count + "====================================" +page ) ;

                if (isOK){
                    if (count <= page ){
                        isOK = false ;
                        dialog = new MyProgressDialog( MovieListActivity.this , "加载数据中请稍后。。。") ;
                        dialog.show();
                        loadData01(title_a) ;
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
                        ToastUtils.shortToast(MovieListActivity.this, "没有更多数据");
                    }
                }

            }
            super.handleMessage(msg);
        }
    };

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("视频更多页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("视频更多页面");
        //统计时长
        MobclickAgent.onPause(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //实例化组件
        initView() ;
        //添加回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /***********************************分页加载****************************************/
        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                //Log.e("benben________", scrollY + "");

                View childView = scrollView.getChildAt(0);
                if (childView.getMeasuredHeight() <= scrollY + scrollView.getHeight()) {

                    mHandler.sendEmptyMessage(203);
                }
            }
        });
        /***********************************分页加载****************************************/

        Intent intent =getIntent() ;
        title_a = intent.getStringExtra("title");
        this.title.setText(title_a);
        loadData01(title_a) ;

    }

    private void initView() {

        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        title = (TextView)findViewById(R.id.title) ;
        gridView = (MyGridView)findViewById(R.id.gridView01);
        scrollView = (MyScrollView)findViewById(R.id.scrollView) ;
    }


    private void loadData01(String title) {

        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;

        switch (title){
            case "精品推荐" :
                params.addQueryStringParameter("weight", "weight" );
                break;
            case "热播排行" :
                params.addQueryStringParameter("weight", "weight" );
                break;
            case "最新发布" :
                break;
        }

        params.addQueryStringParameter("pagecount" , "10");
        params.addQueryStringParameter("startpage", "" + count);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET,  Url.GetMovie,params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("视频", responseInfo.result) ;

                if (dialog != null ){
                    dialog.dismiss();
                }


                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(responseInfo.result) ;
                    String pages = jsonObject.getString("pages");

                    page = Integer.parseInt(pages);
                    count ++ ;

                    Log.e("benbne" , "当前页：" + count +"-------------总页数："+ pages ) ;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");
                List<FindVideoEntity> list = JSON.parseArray(result.toJSONString(), FindVideoEntity.class);

                data.addAll(list) ;

                adapter = new MovieItemAdapter(MovieListActivity.this , data ) ;
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                Log.e("视频" , msg) ;
                error.printStackTrace();

                if (dialog != null ){
                    dialog.dismiss();
                }
            }
        }) ;
    }


}
