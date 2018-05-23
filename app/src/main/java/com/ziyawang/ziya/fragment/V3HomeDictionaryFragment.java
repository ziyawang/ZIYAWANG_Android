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
import com.ziyawang.ziya.activity.DicAskActivity;
import com.ziyawang.ziya.activity.LoginActivity;
import com.ziyawang.ziya.activity.SearchDicActivity;
import com.ziyawang.ziya.adapter.V3DicAdapter;
import com.ziyawang.ziya.entity.DictionaryEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2018/5/19.
 */

public class V3HomeDictionaryFragment extends NewsBenBenFragment implements View.OnClickListener{

    private ListView listView ;
    private V3DicAdapter adapter ;
    private MyProgressDialog dialog ;
    private SwipeRefreshLayout swipeRefreshLayout ;

    private List<DictionaryEntity> datas  = new ArrayList<DictionaryEntity>();
    private int startpage = 1 ;

    private LinearLayout footLinearLayout ;
    private LinearLayout headLinearLayout ;
    private TextView ben ;
    private RelativeLayout relative_search ;
    private LinearLayout linear_ask ;

    private boolean isLoad = true ;

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

    private void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startpage = 1 ;
                datas.clear();
                Log.e("swipe" , "swipe") ;
                loadData();
            }
        });
        relative_search.setOnClickListener(this);
        linear_ask.setOnClickListener(this);
    }

    private void initView(View view ) {
        footLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_foot_item , null ) ;
        headLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_dic_head , null ) ;
        relative_search = (RelativeLayout)headLinearLayout.findViewById(R.id.relative_search ) ;
        linear_ask = (LinearLayout) headLinearLayout.findViewById(R.id.linear_ask ) ;
        ben = (TextView)footLinearLayout.findViewById(R.id.ben ) ;
        listView = (ListView) view.findViewById(R.id.listView ) ;
        listView.setDivider(null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout ) ;
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe);
        adapter = new V3DicAdapter(getActivity(), datas);
        listView.addFooterView(footLinearLayout);
        listView.addHeaderView(headLinearLayout);
        listView.setAdapter(adapter);

    }

    private void loadData() {
        //显示ProgressDialog
        dialog = new MyProgressDialog(getActivity() , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();
        //数据请求
        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pageCount", "10");
        params.addQueryStringParameter("startPage", "" + startpage);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.POST, Url.dicList, params, new RequestCallBack<String>() {
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
                    List<DictionaryEntity> list = JSON.parseArray(data.toJSONString(), DictionaryEntity.class);
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
                goSearchDicActivity() ;
                break;
            case R.id.linear_ask :
                if (GetBenSharedPreferences.getIsLogin(getActivity())){
                    goDicAskActivity() ;
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

    private void goDicAskActivity() {
        Intent intent = new Intent(getActivity() , DicAskActivity.class ) ;
        startActivity(intent);
    }

    private void goSearchDicActivity() {
        Intent intent = new Intent(getActivity() , SearchDicActivity.class) ;
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_three, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        //注册监听事件
        initListeners() ;

    }

}
