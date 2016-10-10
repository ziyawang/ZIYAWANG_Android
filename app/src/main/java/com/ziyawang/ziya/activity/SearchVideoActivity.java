package com.ziyawang.ziya.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.alibaba.fastjson.JSONArray;
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
import com.ziyawang.ziya.adapter.MovieBigItemAdapter;
import com.ziyawang.ziya.adapter.MovieItemAdapter;
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.entity.FindServiceEntity;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.tools.Json_FindInfo;
import com.ziyawang.ziya.tools.Json_FindService;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchVideoActivity extends BaseActivity {

    private TextView niuniuniuniu ;
    private int page;
    private int count = 1;
    private RelativeLayout pre;
    private TextView search_text;
    private EditText search_edit;
    private ImageView search_button;
    private Boolean isOK = true;
    private MyProgressDialog dialog;
    private MyScrollView scrollView;
    private BenListView listView;
    private MovieBigItemAdapter adapter;
    private List<FindVideoEntity> data = new ArrayList<FindVideoEntity>();

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 305) {
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
                        ToastUtils.shortToast(SearchVideoActivity.this, "没有更多数据");
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("找视频页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("找视频页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video);
        //实例化组件
        initView();
        //添加回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //对搜索按钮的点击
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                count = 1;
                //加载数据
                loadData();
            }
        });
        //添加分页加载
        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                View childView = scrollView.getChildAt(0);
                if (childView.getMeasuredHeight() <= scrollY + scrollView.getHeight()) {

                    mHandler.sendEmptyMessage(305);
                }
            }
        });

    }

    private void loadData() {

        if (!TextUtils.isEmpty(search_edit.getText().toString())) {
            String content = search_edit.getText().toString().trim();
            dialog = new MyProgressDialog(SearchVideoActivity.this, "加载数据中请稍后。。。");
            dialog.show();

            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("content", content);
            params.addBodyParameter("type", "2");
            httpUtils.send(HttpRequest.HttpMethod.POST, Url.Search, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Log.e("benben", responseInfo.result);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseInfo.result);
                        String pages = jsonObject.getString("pages");
                        page = Integer.parseInt(pages);
                        count++;
                        Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);

                        String counts = jsonObject.getString("counts");
                        if (counts.equals("0")) {
                            scrollView.setVisibility(View.GONE);
                            niuniuniuniu.setVisibility(View.VISIBLE);
                        }else {

                            scrollView.setVisibility(View.VISIBLE);
                            niuniuniuniu.setVisibility(View.GONE);

                            com.alibaba.fastjson.JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                            JSONArray result = jsonObj.getJSONArray("data");
                            List<FindVideoEntity> list = JSON.parseArray(result.toJSONString(), FindVideoEntity.class);

                            data.addAll(list);

                            adapter = new MovieBigItemAdapter(SearchVideoActivity.this, data);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

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

                    error.printStackTrace();
                }
            });

        } else {
            ToastUtils.shortToast(SearchVideoActivity.this, "请输入您想要索搜的关键字");
        }
    }

    private void initView() {

        pre = (RelativeLayout) findViewById(R.id.pre);

        niuniuniuniu = (TextView) findViewById(R.id.niuniuniuniu);
        search_text = (TextView) findViewById(R.id.search_text);
        search_edit = (EditText) findViewById(R.id.search_edit);
        search_button = (ImageView) findViewById(R.id.search_button);

        search_edit.setFocusable(true);
        search_edit.setFocusableInTouchMode(true);
        search_edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) search_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(search_edit, 0);

        listView = (BenListView) findViewById(R.id.listView);
        scrollView = (MyScrollView) findViewById(R.id.scrollView);

        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

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
