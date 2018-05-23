package com.ziyawang.ziya.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.V3AskAdapter;
import com.ziyawang.ziya.entity.AskEntity;
import com.ziyawang.ziya.entity.DictionaryEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.XEditText;

import java.util.ArrayList;
import java.util.List;

public class SearchAskActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    private XEditText edit_search ;
    private ImageView search_button ;
    private ListView listView ;

    private V3AskAdapter adapter ;
    private MyProgressDialog dialog ;
    private List<AskEntity> datas  = new ArrayList<AskEntity>();
    private int startpage = 1 ;
    private LinearLayout footLinearLayout ;
    private TextView ben ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_search_ask);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        edit_search = (XEditText)findViewById(R.id.edit_search) ;
        search_button = (ImageView) findViewById(R.id.search_button) ;
        listView = (ListView) findViewById(R.id.listView) ;
        listView.setDivider(null);
        footLinearLayout = (LinearLayout) LayoutInflater.from(SearchAskActivity.this).inflate(R.layout.home_foot_item ,null ) ;
        ben = (TextView)footLinearLayout.findViewById(R.id.ben ) ;
        adapter = new V3AskAdapter(SearchAskActivity.this, datas);
        listView.addFooterView(footLinearLayout);
        listView.setAdapter(adapter);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        search_button.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.search_button:
                if (!TextUtils.isEmpty(edit_search.getText().toString().trim())){
                    loadData() ;
                }else {
                    ToastUtils.shortToast(SearchAskActivity.this , "请键入关键字在尝试搜索") ;
                }
                break;
            default:
                break;
        }
    }

    private void loadData() {
        //显示ProgressDialog
        dialog = new MyProgressDialog(SearchAskActivity.this , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();
        //数据请求
        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("search" , edit_search.getText().toString().trim());
        params.addQueryStringParameter("pageCount", "10");
        params.addQueryStringParameter("startPage", "" + startpage);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.POST, Url.queSearchList, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                //关闭dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("问答搜索", responseInfo.result);
                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                startpage += 1 ;
                JSONArray data = jsonObj.getJSONArray("data");
                if (data.size() != 0 ){
                    List<AskEntity> list = JSON.parseArray(data.toJSONString(), AskEntity.class);
                    datas.addAll(list);
                    adapter.notifyDataSetChanged();

                    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                            switch (scrollState) {
                                case SCROLL_STATE_IDLE:
                                    if (isListViewReachBottomEdge(absListView)){
                                        dialog = new MyProgressDialog(SearchAskActivity.this , "加载数据中请稍后。。。") ;
                                        dialog.show();
                                        Log.e("测试" , "测试" ) ;
                                        loadData();
                                    }

                                    break;
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        }
                    });
                }else {
                    ben.setText("没有更多数据");
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("视频", msg);
                error.printStackTrace();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }) ;
    }

    public boolean isListViewReachBottomEdge(final AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        return result;
    }
}
