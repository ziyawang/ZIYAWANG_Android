package com.ziyawang.ziya.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ziyawang.ziya.adapter.FindInfoAdapter;
import com.ziyawang.ziya.adapter.FindServiceAdapter;
import com.ziyawang.ziya.adapter.V2FindInfoAdapter;
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.entity.FindServiceEntity;
import com.ziyawang.ziya.entity.V2InfoEntity;
import com.ziyawang.ziya.tools.Json_FindInfo;
import com.ziyawang.ziya.tools.Json_FindService;
import com.ziyawang.ziya.tools.NetUtils;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends BaseActivity {

    private List<V2InfoEntity> data = new ArrayList<V2InfoEntity>();
    private List<FindServiceEntity> data01 = new ArrayList<FindServiceEntity>();
    private V2FindInfoAdapter adapter;
    private FindServiceAdapter adapter01;
    private int page;
    private int count = 1;
    private RelativeLayout pre;
    private TextView info_title;
    private TextView search_text;
    private EditText search_edit;
    private ImageView search_button;
    private String type;
    private Boolean isOK = true;
    private MyProgressDialog dialog;
    private MyScrollView scrollView;
    private BenListView listView;
    private TextView niuniuniuniu ;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 300) {
                // Bundle b = msg.getData();
                // tv.setText(b.getString("num"));
                Log.e("HomeINfo", count + "====================================" + page);

                if (isOK) {
                    if (count <= page) {
                        isOK = false;

                        loadData();
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    isOK = true;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    } else {
                        ToastUtils.shortToast(SearchActivity.this, "信息没有更多数据");
                    }
                }

            }
            super.handleMessage(msg);
        }
    };

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("搜索页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("搜索页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //实例化组件
        initView();
        //添加回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        finish();
                    }

                }, 100);
            }
        });
        //获得title和type
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String type = intent.getStringExtra("type");
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(type)) {
            info_title.setText(title);
            switch (type) {
                case "信息":
                    search_text.setText("找信息");
                    break;
                case "服务":
                    search_text.setText("找服务");
                    break;
                default:
                    break;
            }
        }
        //对找信息和找服务的点击事件
        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_text.getText().toString().equals("找信息")) {
                    search_text.setText("找服务");
                    info_title.setText("搜索服务");
                    search_edit.setText("");
                    //listView.removeAllViews();
                } else {
                    search_text.setText("找信息");
                    info_title.setText("搜索信息");
                    search_edit.setText("");
                    //listView.removeAllViews();
                }
            }
        });
        //对搜索按钮的点击
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean networkConnected = NetUtils.isNetworkConnected(SearchActivity.this);
                if (networkConnected){
                    data.clear();
                    data01.clear();
                    count = 1;
                    //加载数据
                    loadData();
                }else {
                    ToastUtils.shortToast(SearchActivity.this , "未连接任何网络" );
                }

            }
        });
        //添加分页加载
        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                View childView = scrollView.getChildAt(0);
                if (childView.getMeasuredHeight() <= scrollY + scrollView.getHeight()) {

                    mHandler.sendEmptyMessage(300);
                }
            }
        });


    }

    private void loadData() {

        if (!TextUtils.isEmpty(search_edit.getText().toString())) {
            String content = search_edit.getText().toString().trim();
            dialog = new MyProgressDialog(SearchActivity.this, "加载数据中请稍后。。。");
            dialog.show();
            if (search_text.getText().toString().equals("找信息")) {
                type = "1";
                HttpUtils httpUtils = new HttpUtils();
                RequestParams params = new RequestParams();
                params.addBodyParameter("content", content);
                params.addQueryStringParameter("startpage", "" + count);
                params.addBodyParameter("type", type);
                httpUtils.send(HttpRequest.HttpMethod.POST, Url.Search, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("benben", responseInfo.result);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
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
                            adapter = new V2FindInfoAdapter(SearchActivity.this, data);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetInvalidated();
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        ToastUtils.shortToast(SearchActivity.this , "数据连接异常");

                        error.printStackTrace();
                    }
                });
            } else {
                type = "4";
                HttpUtils httpUtils = new HttpUtils();
                RequestParams params = new RequestParams();
                params.addBodyParameter("content", content);
                params.addQueryStringParameter("startpage" , "" + count );
                params.addBodyParameter("type", type);
                httpUtils.send(HttpRequest.HttpMethod.POST, Url.Search, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        Log.e("benben", responseInfo.result);

                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                            String pages = jsonObject.getString("pages");

                            page = Integer.parseInt(pages);
                            count++;

                            Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);

                            String counts = jsonObject.getString("counts");
                            if (!TextUtils.isEmpty(counts) && counts.equals("0")){
                                scrollView.setVisibility(View.GONE);
                                niuniuniuniu.setVisibility(View.VISIBLE);
                            }else {

                                scrollView.setVisibility(View.VISIBLE);
                                niuniuniuniu.setVisibility(View.GONE);

                                try {
                                    List<FindServiceEntity> list = Json_FindService.getParse(responseInfo.result);

                                    data01.addAll(list);

                                    adapter01 = new FindServiceAdapter(SearchActivity.this, data01);
                                    listView.setAdapter(adapter01);
                                    adapter01.notifyDataSetChanged();

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
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        ToastUtils.shortToast(SearchActivity.this , "数据连接异常");

                        error.printStackTrace();
                    }
                });

            }

        } else {
            ToastUtils.shortToast(SearchActivity.this, "请输入您想要索搜的关键字");
        }
    }

    private void initView() {

        info_title = (TextView) findViewById(R.id.info_title);
        pre = (RelativeLayout) findViewById(R.id.pre);

        niuniuniuniu = (TextView)findViewById(R.id.niuniuniuniu ) ;

        search_text = (TextView) findViewById(R.id.search_text);
        search_edit = (EditText) findViewById(R.id.search_edit);
        search_button = (ImageView) findViewById(R.id.search_button);

        search_edit.setFocusable(true);
        search_edit.setFocusableInTouchMode(true);
        search_edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) search_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(search_edit, 0);

        listView = (BenListView) findViewById(R.id.listView);

        listView.setDivider(new ColorDrawable(Color.argb(0, 244, 244, 244)));
        listView.setDividerHeight(1);

        scrollView = (MyScrollView) findViewById(R.id.scrollView);

        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //loadDataCompany();

                    loadData();

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }

                    return true;
                }
                return false;
            }
        });


    }
}
