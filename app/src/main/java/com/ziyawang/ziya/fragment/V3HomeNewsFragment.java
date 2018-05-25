package com.ziyawang.ziya.fragment;

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
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.NewsAdapter;
import com.ziyawang.ziya.entity.NewsEntity;
import com.ziyawang.ziya.entity.V3BannerEntity;
import com.ziyawang.ziya.tools.GlideImageLoader;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2018/5/18.
 */

public class V3HomeNewsFragment extends NewsBenBenFragment {

    private ListView listView ;
    private NewsAdapter adapter ;
    private MyProgressDialog dialog ;
    private SwipeRefreshLayout swipeRefreshLayout ;

    private List<NewsEntity> datas  = new ArrayList<NewsEntity>();
    private int startpage = 1 ;

    private LinearLayout footLinearLayout ;
    private LinearLayout headLinearLayout ;
    private TextView ben ;
    private Banner banner ;
    private List<String> listBanner = new ArrayList() ;

    private boolean isLoad = true ;

    @Override
    protected void lazyLoad() {
        //加载新闻页轮播图
        loadBannerData() ;
        if (isLoad){
            startpage = 1 ;
            //数据加载
            datas.clear();
            //加载数据
            loadData() ;
        }
    }

    private void loadBannerData() {
        listBanner.clear();
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.send(HttpRequest.HttpMethod.POST, Url.getBanner, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("getBanner" , responseInfo.result) ;
                JSONObject object = JSON.parseObject(responseInfo.result);
                String status_code = object.getString("status_code");
                if (status_code.equals("200")){
                    JSONArray data = object.getJSONArray("data");
                    List<V3BannerEntity> list = JSON.parseArray(data.toJSONString(), V3BannerEntity.class);
                    for (int i = 0; i < list.size(); i++) {
                        String picUrl = Url.FileIP + list.get(i).getBannerLink() ;
                        listBanner.add(picUrl) ;
                    }
                    banner.setImages(listBanner) ;
                    banner.start() ;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        }) ;
    }

    private void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //显示ProgressDialog
                dialog = new MyProgressDialog(getContext() , "数据加载中，请稍后。。。");
                dialog.setCancelable(false);// 不可以用“返回键”取消
                dialog.show();
                startpage = 1 ;
                datas.clear();
                Log.e("swipe" , "swipe") ;
                loadData();
                loadBannerData() ;
            }
        });
    }

    private void initView(View view ) {
        footLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_foot_item , null ) ;
        headLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_news_head , null ) ;
        banner = (Banner)headLinearLayout.findViewById(R.id.banner ) ;
        ben = (TextView)footLinearLayout.findViewById(R.id.ben ) ;
        listView = (ListView) view.findViewById(R.id.listView ) ;
        listView.setDivider(null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout ) ;
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe);
        adapter = new NewsAdapter(getActivity(), datas);
        listView.addFooterView(footLinearLayout);
        listView.addHeaderView(headLinearLayout);
        listView.setAdapter(adapter);


        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.isAutoPlay(true) ;
        banner.setDelayTime(3000);
        banner.setImageLoader(new GlideImageLoader());

    }

    private void loadData() {
        //数据请求
        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pageCount", "10");
        params.addQueryStringParameter("startPage", "" + startpage);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.POST, Url.newsList, params, new RequestCallBack<String>() {
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
                Log.e("视频", responseInfo.result);
                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                startpage += 1 ;
                JSONArray data = jsonObj.getJSONArray("data");
                if (data.size() != 0 ){
                    List<NewsEntity> list = JSON.parseArray(data.toJSONString(), NewsEntity.class);
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
