package com.ziyawang.ziya.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
import com.ziyawang.ziya.adapter.MovieItemAdapter;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.tools.Url;

import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/3.
 */
public class MovieFourFragment extends Fragment {

    private GridView gridView ;
    private MovieItemAdapter adapter ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_four, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initview(view);
        loadData() ;

    }

    private void loadData() {

        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pagecount" , "10000");
        params.addQueryStringParameter("VideoLabel" , "大咖秀");
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET,  Url.GetMovie,params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("视频", responseInfo.result) ;

                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");
                List<FindVideoEntity> list = JSON.parseArray(result.toJSONString(), FindVideoEntity.class);

                adapter = new MovieItemAdapter(getActivity() , list ) ;
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                Log.e("视频" , msg) ;
                error.printStackTrace();
            }
        }) ;
    }

    private void initview(View view ) {

        gridView = (GridView)view.findViewById(R.id.gridView01);

    }
}