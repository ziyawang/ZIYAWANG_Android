package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsNewsActivity;
import com.ziyawang.ziya.activity.FindServiceActivity;
import com.ziyawang.ziya.adapter.FindNewsAdapter;
import com.ziyawang.ziya.entity.FindNewsEntity;
import com.ziyawang.ziya.tools.Json_FindNews;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.BitmapHelp;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/11/11.
 */
public  class NewsItemsFragment extends NewsBenBenFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    /**
     * 获取bitmapUtils单例
     */
    public static BitmapUtils bitmapUtils;

    private String title ;

    private List<FindNewsEntity> list ;
    private List<FindNewsEntity> data = new ArrayList<FindNewsEntity>();
    private FindNewsAdapter adapter ;

    //private BenListView listView ;
    private ListView listView ;

    //private MyScrollView scrollView ;
    private int page  ;
    private int count = 1 ;
    private Boolean isOK = true ;
    private MyProgressDialog dialog ;

    private View rootView ;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 105401) {
                Log.e("HomeINfo", count + "====================================" + page) ;
                if (isOK){
                    if (count <= page ){
                        isOK = false ;
                        dialog = new MyProgressDialog( getActivity() , "加载数据中请稍后。。。") ;
                        dialog.show();
                        loadNewsData( listView );
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
                        ToastUtils.shortToast(getActivity(), "没有更多数据");
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

    public NewsItemsFragment() {}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if(rootView == null) {
            rootView= inflater.inflate(R.layout.fragment_news_01, container, false);
            listView = (ListView)rootView.findViewById(R.id.listView);
            // 获取bitmapUtils单例
            bitmapUtils = BitmapHelp.getBitmapUtils(getContext());
            /**
             * 设置默认的图片展现、加载失败的图片展现
             */
            bitmapUtils.configDefaultLoadingImage(R.mipmap.fast_error);
            bitmapUtils.configDefaultLoadFailedImage(R.mipmap.error_imgs_big);
            bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
            //scrollView = (MyScrollView)rootView.findViewById(R.id.scrollView) ;
            //获得索引值
            Bundle bundle = getArguments();
            if (bundle != null) {
                title = bundle != null ? bundle.getString("title") : "";
            }
            isPrepared = true;
            lazyLoad();
        }
        return rootView;
    }

    private void loadNewsData(final ListView listView ) {
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(getActivity() , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("startpage", "" + count);
        params.addQueryStringParameter("pagecount", "7" );
        params.addQueryStringParameter("NewsLabel" , title );
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.NewsLists, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                mHasLoadedOnce = true;
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("benben" , responseInfo.result ) ;
                try {
                    list = Json_FindNews.getParse(responseInfo.result);
                    //scrollView.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    String pages = jsonObject.getString("pages");
                    page = Integer.parseInt(pages);
                    count++;
                    Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);
                    data.addAll(list);
                    adapter = new FindNewsAdapter(getContext(), data ) ;
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), DetailsNewsActivity.class);
                            intent.putExtra("id", data.get(position).getNewsID());
                            startActivity(intent);
                        }
                    });
                    adapter.notifyDataSetChanged();

                    listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true, new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            switch (scrollState) {
                                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 当不迁移转变时
                                    // 断定迁移转变到底部
                                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                                        if (isOK) {
                                            if (count <= page) {
                                                isOK = false;
                                                addData();
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
                                                ToastUtils.shortToast(getActivity(), "服务没有更多数据");
                                            }
                                        }
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }
                    }));

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
                ToastUtils.shortToast(getActivity() , "网络连接异常");
            }
        }) ;
    }

    private void addData() {
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(getActivity() , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("startpage", "" + count);
        params.addQueryStringParameter("pagecount", "7" );
        params.addQueryStringParameter("NewsLabel" , title );
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.NewsLists, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                mHasLoadedOnce = true;
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("benben" , responseInfo.result ) ;
                try {
                    list = Json_FindNews.getParse(responseInfo.result);
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    String pages = jsonObject.getString("pages");
                    page = Integer.parseInt(pages);
                    count++;
                    Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);
                    data.addAll(list);
                    adapter.notifyDataSetChanged();
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
                ToastUtils.shortToast(getActivity() , "网络连接异常");
            }
        }) ;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        count = 1 ;
        //数据加载
        data.clear();
        loadNewsData(listView);
    }
}