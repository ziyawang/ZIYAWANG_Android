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

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.MyReleaseAdapter;
import com.ziyawang.ziya.adapter.V2FindInfoAdapter;
import com.ziyawang.ziya.adapter.V2MyReleaseAdapter;
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.entity.V2InfoEntity;
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

public class MyReleaseActivity extends BaseActivity {

    private List<V2InfoEntity> list ;
    private V2MyReleaseAdapter adapter ;
    private TextView niuniuniuniu ;
    private List<V2InfoEntity> data  = new ArrayList<V2InfoEntity>();
    private int page  ;
    private int count = 1 ;
    private Boolean isOK = true ;
    private MyProgressDialog dialog ;
    private MyScrollView scrollView ;
    private BenListView listView ;
    private RelativeLayout pre ;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 206) {
                Log.e("HomeINfo", count + "====================================" + page) ;
                if (isOK){
                    if (count <= page ){
                        isOK = false ;
                        dialog = new MyProgressDialog( MyReleaseActivity.this , "加载数据中请稍后。。。") ;
                        dialog.show();
                        myRelease();
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
                        ToastUtils.shortToast(MyReleaseActivity.this, "信息没有更多数据");
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

    private static String login;
    private static boolean isLogin;
    private static String root;
    private static String spphoneNumber;


    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("我的发布列表页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("我的发布列表页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release2 ) ;

        final SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);

        final SharedPreferences myNumber = getSharedPreferences("myNumber", MODE_PRIVATE);
        spphoneNumber = myNumber.getString("myNumber", null);

        final SharedPreferences role = getSharedPreferences("role", MODE_PRIVATE);
        root = role.getString("role", null);

        SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
        isLogin = sp.getBoolean("isLogin", false);

        //实例化组件
        initView() ;

        //加载数据
        myRelease() ;

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
                    mHandler.sendEmptyMessage(206);
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

        niuniuniuniu = (TextView)findViewById(R.id.niuniuniuniu ) ;

    }

    private void myRelease( ) {

        String urls = String.format(Url.MyRelease, login ) ;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("startpage" , "" + count );
        params.addQueryStringParameter("pagecount", "10" );
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("benben_myRelease", responseInfo.result);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(responseInfo.result);
                String pages = object.getString("pages");
                page = Integer.parseInt(pages);
                count++;
                String counts = object.getString("counts");
                if (!TextUtils.isEmpty(counts) && counts.equals("0")) {
                    scrollView.setVisibility(View.GONE);
                    niuniuniuniu.setVisibility(View.VISIBLE);
                } else {
                    scrollView.setVisibility(View.VISIBLE);
                    niuniuniuniu.setVisibility(View.GONE);
                    com.alibaba.fastjson.JSONArray data01 = object.getJSONArray("data");
                    List<V2InfoEntity> v2InfoEntities = JSON.parseArray(data01.toJSONString(), V2InfoEntity.class);
                    data.addAll(v2InfoEntities);
                    adapter = new V2MyReleaseAdapter(MyReleaseActivity.this, data);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetInvalidated();
                }

            }
            @Override
            public void onFailure(HttpException error, String msg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                error.printStackTrace();
            }
        });
    }
}
