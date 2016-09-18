package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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
import com.ziyawang.ziya.activity.SearchVideoActivity;
import com.ziyawang.ziya.adapter.MovieBigItemAdapter;
import com.ziyawang.ziya.adapter.MovieItemAdapter;
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.MyScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/3.
 */
public class MovieThreeFragment extends Fragment {

    private BenListView gridView ;
    private MovieBigItemAdapter adapter ;

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
            if (msg.what == 8876) {
                // Bundle b = msg.getData();
                // tv.setText(b.getString("num"));
                Log.e("HomeINfo", count + "====================================" +page ) ;
                if (isOK){
                    if (count <= page ){
                        isOK = false ;
                        dialog = new MyProgressDialog( getActivity() , "加载数据中请稍后。。。") ;
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
                        ToastUtils.shortToast( getActivity(),  "没有更多数据");
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

    //private MyProgressDialog dialog  ;
    private RelativeLayout movie_search ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_three, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        count = 1 ;
        //数据加载
        data.clear();
        initview(view);
        loadData() ;


        //搜索按钮
        movie_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchVideoActivity.class);
                startActivity(intent);
            }
        });

        //添加分页加载
        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {

                View childView = scrollView.getChildAt(0);
                if (childView.getMeasuredHeight() <= scrollY + scrollView.getHeight()) {

                    mHandler.sendEmptyMessage(8876);
                }
            }
        });


    }

    private void loadData() {

        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(getActivity() , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();

        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pagecount", "5");
        params.addQueryStringParameter("VideoLabel", "tj");
        params.addQueryStringParameter("startpage", "" + count);
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.GetMovie, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (dialog != null) {
                    dialog.dismiss();
                }

                Log.e("视频", responseInfo.result);

                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");

                String pages = jsonObj.getString("pages");
                page = Integer.parseInt(pages);
                count++;

                Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);
                List<FindVideoEntity> list = JSON.parseArray(result.toJSONString(), FindVideoEntity.class);


                data.addAll(list);

                adapter = new MovieBigItemAdapter(getActivity(), data);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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

    private void initview(View view ) {

        gridView = (BenListView)view.findViewById(R.id.gridView01);
        movie_search = (RelativeLayout)view.findViewById(R.id.movie_search);
        scrollView = (MyScrollView)view.findViewById(R.id.scrollView);

    }
}
