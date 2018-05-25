package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.ziyawang.ziya.activity.AskAskActivity;
import com.ziyawang.ziya.activity.LoginActivity;
import com.ziyawang.ziya.activity.MyAskActivity;
import com.ziyawang.ziya.activity.SearchAskActivity;
import com.ziyawang.ziya.adapter.V3AskAdapter;
import com.ziyawang.ziya.entity.AskEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2018/5/19.
 */

public class V3HomeAskFragment extends NewsBenBenFragment implements View.OnClickListener {

    private ListView listView ;
    private V3AskAdapter adapter ;
    private MyProgressDialog dialog ;
    private SwipeRefreshLayout swipeRefreshLayout ;

    private List<AskEntity> datas  = new ArrayList<AskEntity>();
    private int startpage = 1 ;

    private LinearLayout footLinearLayout ;
    private LinearLayout headLinearLayout ;
    private TextView ben ;

    private RelativeLayout relative_search ;
    private LinearLayout linear_ask ;
    private LinearLayout linear_myAsk ;

    private boolean isLoad = true ;

    private void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //显示ProgressDialog
                dialog = new MyProgressDialog(getActivity() , "数据加载中，请稍后。。。");
                dialog.setCancelable(false);// 不可以用“返回键”取消
                dialog.show();
                startpage = 1 ;
                datas.clear();
                Log.e("swipe" , "swipe") ;
                loadData();
            }
        });
        relative_search.setOnClickListener(this);
        linear_ask.setOnClickListener(this);
        linear_myAsk.setOnClickListener(this);
    }

    private void loadData() {
        //数据请求
        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pageCount", "10");
        params.addQueryStringParameter("startPage", "" + startpage);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.POST, Url.questionList, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                isLoad = false ;
                //关闭dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.e("词典", responseInfo.result);
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
                                        dialog = new MyProgressDialog(getActivity() , "加载数据中请稍后。。。") ;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_search :
                goSearchAskActivity() ;
                break;
            case R.id.linear_ask :
                if (GetBenSharedPreferences.getIsLogin(getActivity())){
                    goAskAskActivity() ;
                }else {
                    goLoginActivity() ;
                }
                break;
            case R.id.linear_myAsk :
                if (GetBenSharedPreferences.getIsLogin(getActivity())){
                    goMyAskActivity() ;
                }else {
                    goLoginActivity() ;
                }
                break;
            default:
                break;
        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
        startActivity(intent);
    }

    private void goMyAskActivity() {
        Intent intent = new Intent(getActivity() , MyAskActivity.class ) ;
        startActivity(intent);
    }

    private void goAskAskActivity() {
        Intent intent = new Intent(getActivity() , AskAskActivity.class ) ;
        startActivity(intent);
    }

    private void goSearchAskActivity() {
        Intent intent = new Intent(getActivity() , SearchAskActivity.class ) ;
        startActivity(intent);
    }

    private void initView(View view ) {
        footLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_foot_item , null ) ;
        headLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_ask_head , null ) ;
        relative_search = (RelativeLayout)headLinearLayout.findViewById(R.id.relative_search ) ;
        linear_ask = (LinearLayout) headLinearLayout.findViewById(R.id.linear_ask ) ;
        linear_myAsk = (LinearLayout) headLinearLayout.findViewById(R.id.linear_myAsk ) ;
        ben = (TextView)footLinearLayout.findViewById(R.id.ben ) ;
        listView = (ListView) view.findViewById(R.id.listView ) ;
        listView.setDivider(null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout ) ;
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_ask);
        adapter = new V3AskAdapter(getActivity(), datas);
        listView.addFooterView(footLinearLayout);
        listView.addHeaderView(headLinearLayout);
        listView.setAdapter(adapter);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ask, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        //注册监听事件
        initListeners() ;

    }

    @Override
    protected void lazyLoad() {
        if (isLoad){
            startpage = 1 ;
            //数据加载
            datas.clear();
            //加载数据
            loadData() ;
        }
    }
}
