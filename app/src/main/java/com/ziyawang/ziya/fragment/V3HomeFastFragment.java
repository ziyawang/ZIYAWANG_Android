package com.ziyawang.ziya.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ziyawang.ziya.adapter.FastAdapter;
import com.ziyawang.ziya.entity.FastEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by 牛海丰 on 2018/5/19.
 */

public class V3HomeFastFragment extends NewsBenBenFragment{

    private SwipeRefreshLayout swipeRefreshLayout ;

    private boolean isLoad = true ;
    private FastAdapter adapter ;

    private RecyclerView recyclerView;
    private List<FastEntity> datas = new ArrayList<>();
    private MyProgressDialog dialog ;
    //private int startpage = 1 ;

    private TextView text01 ;
    private TextView text02 ;


    @Override
    protected void lazyLoad() {
        loadHeadData() ;
        if (isLoad){
            //加载数据
            loadData() ;
        }
    }

    private void loadHeadData() {
        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期

        int mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期

        String ben = "" ;
        switch (mWay){
            case Calendar.SUNDAY :
                ben = "星期日" ;
                break;
            case Calendar.MONDAY :
                ben = "星期一" ;
                break;
            case Calendar.TUESDAY :
                ben = "星期二" ;
                break;
            case Calendar.WEDNESDAY :
                ben = "星期三" ;
                break;
            case Calendar.THURSDAY :
                ben = "星期四" ;
                break;
            case Calendar.FRIDAY :
                ben = "星期五" ;
                break;
            case Calendar.SATURDAY :
                ben = "星期六" ;
                break;
            default:
                break;
        }
        text01.setText(mYear + "年" + mMonth + "月" + mDay + "日");
        text02.setText(ben);

    }

    private void loadData() {
        datas.clear();
        //显示ProgressDialog
        dialog = new MyProgressDialog(getActivity() , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();
        //数据请求
        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pageCount", "10000");
        //params.addQueryStringParameter("startPage", "" + startpage);
        params.addQueryStringParameter("startPage", "1" );
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.POST, Url.messageList, params, new RequestCallBack<String>() {
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
                Log.e("快讯", responseInfo.result);
                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                //startpage += 1 ;
                JSONArray data = jsonObj.getJSONArray("data");
                //if (data.size() != 0 ){
                    List<FastEntity> list = JSON.parseArray(data.toJSONString(), FastEntity.class);
                    datas.addAll(list);
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("视频", msg);
                error.printStackTrace();
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }) ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_fast, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        //注册监听事件
        //initListeners() ;

    }

    private void goShare(String id) {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitleUrl(Url.ShareFast + id );
        oks.setTitle("资芽快讯");
        oks.setImageUrl("http://images.ziyawang.com/Applogo/logo.png");
        oks.setText("资芽快讯");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url.ShareFast + id);
        // 启动分享GUI
        oks.show(getActivity());
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        //recyclerView.setLayoutManager((new LinearLayoutManager(getActivity()))) ;

        adapter = new FastAdapter(datas, getActivity());
        adapter.setOnItemClickListener(new FastAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //ToastUtils.shortToast(getActivity() , position + "") ;
                //分享的快讯的id     ;
                goShare(datas.get(position).getId()) ;
            }
        });


        //设置adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        text01 = (TextView)view.findViewById(R.id.text01 ) ;
        text02 = (TextView)view.findViewById(R.id.text02 ) ;
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout ) ;
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("swipe" , "swipe") ;
                loadData();
                loadHeadData() ;
            }
        });

    }

}
