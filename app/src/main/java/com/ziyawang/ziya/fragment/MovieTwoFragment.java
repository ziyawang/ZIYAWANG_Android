package com.ziyawang.ziya.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.SearchActivity;
import com.ziyawang.ziya.adapter.MovieBigItemAdapter;
import com.ziyawang.ziya.adapter.MovieItemAdapter;
import com.ziyawang.ziya.adapter.V2FindInfoAdapter;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.entity.V2InfoEntity;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;

import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/3.
 */
public class MovieTwoFragment extends Fragment {

    private ListView gridView ;
    private MovieBigItemAdapter adapter ;
    private TextView niuniuniuniu  ;


    //private MyProgressDialog dialog  ;
    private String title ;
    //private TextView movie_title ;

    private String label ;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        title = bundle != null ? bundle.getString("title") : "";

        initview(view);
        loadData(title) ;

    }

    private void loadData(String title) {

        //movie_title.setText(title);

        switch (title){
            case "资芽一分钟" :
                label = "zyyfz" ;
                break;
            case "行业说" :
                label = "hys" ;
                break;
            case "资芽哈哈哈" :
                label = "zyhhh" ;
                break;
            case "付费课程" :
                label = "ffkc" ;
                break;
            default:
                break;

        }


//        /* 显示ProgressDialog */
//        //在开始进行网络连接时显示进度条对话框
//        dialog = new MyProgressDialog(getActivity() , "数据加载中，请稍后。。。");
//        dialog.setCancelable(false);// 不可以用“返回键”取消
//        dialog.show();
        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("pagecount" , "10000");
        params.addQueryStringParameter("VideoLabel" , label  );
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET,  Url.GetMovie,params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("视频", responseInfo.result) ;

                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                String counts = jsonObj.getString("counts");
                if (!TextUtils.isEmpty(counts) && counts.equals("0")) {
                    gridView.setVisibility(View.GONE);
               niuniuniuniu.setVisibility(View.VISIBLE);
           }else {
               gridView.setVisibility(View.VISIBLE);
               niuniuniuniu.setVisibility(View.GONE);
               JSONArray result = jsonObj.getJSONArray("data");
               List<FindVideoEntity> list = JSON.parseArray(result.toJSONString(), FindVideoEntity.class);
               adapter = new MovieBigItemAdapter(getActivity() , list ) ;
               gridView.setAdapter(adapter);
               adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {

                Log.e("视频" , msg) ;
                error.printStackTrace();
//                if (dialog!=null){
//                    dialog.dismiss();
//                }
            }
        }) ;
    }

    private void initview(View view ) {

        gridView = (ListView)view.findViewById(R.id.gridView01);
        niuniuniuniu = (TextView)view.findViewById(R.id.niuniuniuniu ) ;

        //movie_title =(TextView)view.findViewById(R.id.movie_title ) ;

    }
}
