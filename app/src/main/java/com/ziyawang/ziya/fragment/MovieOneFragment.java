package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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
import com.ziyawang.ziya.activity.MovieListActivity;
import com.ziyawang.ziya.adapter.MovieItemAdapter;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.tools.Url;

import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/3.
 */
public class MovieOneFragment extends Fragment {

    private TextView movieMore01 , movieMore02,movieMore03 ;
    private GridView gridView01 , gridView02 , gridView03 ;

    private MovieItemAdapter adapter01 ;
    private MovieItemAdapter adapter02 ;
    private MovieItemAdapter adapter03 ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_one, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //实例化组件
        initView(view) ;

        //加载数据01
        loadData01() ;
        //加载数据02
        loadData02() ;
       // 加载数据03
        loadData03() ;

        movieMore01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity() , MovieListActivity.class ) ;
                intent.putExtra("title" , "精品推荐") ;
                startActivity(intent);

            }
        });
        movieMore02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity() , MovieListActivity.class ) ;
                intent.putExtra("title" , "热播排行") ;
                startActivity(intent);
            }
        });
        movieMore03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity() , MovieListActivity.class ) ;
                intent.putExtra("title" , "最新发布") ;
                startActivity(intent);
            }
        });
    }

    private void loadData03() {
        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pagecount", "2" );
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET,  Url.GetMovie,params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("视频" , responseInfo.result ) ;

                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");
                List<FindVideoEntity> list = JSON.parseArray(result.toJSONString(), FindVideoEntity.class);

                adapter03 = new MovieItemAdapter(getActivity() , list ) ;
                gridView03.setAdapter(adapter03);
                adapter03.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                Log.e("视频" , msg) ;
                error.printStackTrace();
            }
        }) ;
    }

    private void loadData02() {

        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("order", "order" );
        params.addQueryStringParameter("pagecount", "2" );
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET,  Url.GetMovie,params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("视频" , responseInfo.result ) ;

                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");
                List<FindVideoEntity> list = JSON.parseArray(result.toJSONString(), FindVideoEntity.class);

                adapter02 = new MovieItemAdapter(getActivity() , list ) ;
                gridView02.setAdapter(adapter02);
                adapter02.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                Log.e("视频" , msg) ;
                error.printStackTrace();
            }
        }) ;

    }

    private void loadData01() {

        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("weight", "weight" );
        params.addQueryStringParameter("pagecount", "2" );
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET,  Url.GetMovie,params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("视频" , responseInfo.result ) ;

                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");
                List<FindVideoEntity> list = JSON.parseArray(result.toJSONString(), FindVideoEntity.class);

                adapter01 = new MovieItemAdapter(getActivity() , list ) ;
                gridView01.setAdapter(adapter01);
                adapter01.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                Log.e("视频" , msg) ;
                error.printStackTrace();
            }
        }) ;
    }

    private void initView(View view ) {

        movieMore01  = (TextView)view.findViewById(R.id.movieMore01 ) ;
        movieMore02  = (TextView)view.findViewById(R.id.movieMore02 ) ;
        movieMore03  = (TextView)view.findViewById(R.id.movieMore03 ) ;

        gridView01 = (GridView)view.findViewById(R.id.gridView01) ;
        gridView02 = (GridView)view.findViewById(R.id.gridView02) ;
        gridView03 = (GridView)view.findViewById(R.id.gridView03) ;

    }
}
