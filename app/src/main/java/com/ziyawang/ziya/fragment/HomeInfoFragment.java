package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.FindInfoActivity;
import com.ziyawang.ziya.activity.V2FindInfoActivity;
import com.ziyawang.ziya.adapter.FindInfoAdapter;
import com.ziyawang.ziya.adapter.HomeViewPagerAdapter;
import com.ziyawang.ziya.adapter.V2FindInfoAdapter;
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.entity.V2InfoEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.Json_FindInfo;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imageloader.core.ImageLoader;

/**
 * Created by 牛海丰 on 2016/7/28.
 */
public class HomeInfoFragment extends Fragment {

    private ViewPager home_info_viewPager;
    private List<View> viewList = new ArrayList<View>();
    private V2FindInfoAdapter adapter;
    private BenListView home_listView;

    private ImageView five , six ;

    private LayoutInflater inflater;

    private List<V2InfoEntity> data  = new ArrayList<V2InfoEntity>();

    private int page  ;
    private int count = 1 ;

    private Boolean isOK = true ;

    private MyProgressDialog dialog ;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 200) {
                // Bundle b = msg.getData();
                // tv.setText(b.getString("num"));
                Log.e("HomeINfo", count + "====================================" +page ) ;

                if (isOK){
                    if (count <= page ){
                        isOK = false ;
                        dialog = new MyProgressDialog(getActivity() , "加载数据中请稍后。。。") ;
                        dialog.show();
                        findinfo();
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
                        ToastUtils.shortToast(getActivity() , "信息没有更多数据");
                    }
                }

            }
            else if (msg.what == 12345) {
                count =1 ;
                data.clear();
                findinfo();
            }
            super.handleMessage(msg);
        }
    };

    private HomeViewPagerAdapter homeViewPagerAdapter;


    public HomeInfoFragment() {}

    public Handler getHandler(){
        return mHandler;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        inflater = LayoutInflater.from(getActivity());
        View view1 = inflater.inflate(R.layout.view_info_one, null);
        View view2 = inflater.inflate(R.layout.view_info_two, null);

        viewList.add(view1);
        viewList.add(view2);

        homeViewPagerAdapter = new HomeViewPagerAdapter(viewList);
        home_info_viewPager.setAdapter(homeViewPagerAdapter);

        //加载数据
        findinfo() ;

        viewList.get(0).findViewById(R.id.info_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben", "_____________资产包________________");
                Intent intent = new Intent(getActivity(), V2FindInfoActivity.class);
                intent.putExtra("type", "资产包");
                startActivity(intent);
            }
        });
        viewList.get(0).findViewById(R.id.info_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben", "_____________融资信息________________");
                Intent intent = new Intent(getActivity() , V2FindInfoActivity.class  )  ;
                intent.putExtra("type" , "融资信息" ) ;
                startActivity(intent);
            }
        });
        viewList.get(0).findViewById(R.id.info_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "_____________固定资产________________") ;
                Intent intent = new Intent(getActivity() , V2FindInfoActivity.class  )  ;
                intent.putExtra("type" , "固定资产" ) ;
                startActivity(intent);
            }
        });
        viewList.get(0).findViewById(R.id.info_four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "_____________个人债权________________") ;
                Intent intent = new Intent(getActivity() , V2FindInfoActivity.class  )  ;
                intent.putExtra("type" , "个人债权" ) ;
                startActivity(intent);
            }
        });
        viewList.get(1).findViewById(R.id.info_five).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "_____________企业商账________________") ;
                Intent intent = new Intent(getActivity() , V2FindInfoActivity.class  )  ;
                intent.putExtra("type" , "企业商账" ) ;
                startActivity(intent);
            }
        });
        viewList.get(1).findViewById(R.id.info_six).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "_____________法拍资产________________") ;
                Intent intent = new Intent(getActivity() , V2FindInfoActivity.class  )  ;
                intent.putExtra("type" , "法拍资产" ) ;
                startActivity(intent);
            }
        });
        viewList.get(1).findViewById(R.id.info_seven).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "_____________处置公告________________") ;
                Intent intent = new Intent(getActivity() , V2FindInfoActivity.class  )  ;
                intent.putExtra("type" , "处置公告" ) ;
                startActivity(intent);
            }
        });

    }

    private void initView(View view) {

        home_info_viewPager = (ViewPager) view.findViewById(R.id.home_info_viewPager);
        home_listView = (BenListView) view.findViewById(R.id.home_listView);
        home_listView.setDivider(new ColorDrawable(Color.argb(0, 244, 244, 244)));
        home_listView.setDividerHeight(1);
        five = (ImageView)view.findViewById(R.id.five ) ;
        six = (ImageView)view.findViewById(R.id.six ) ;


        //ViewPager点击时消费到touch方法
        home_info_viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:

                        HomePageFragment homePageFragment = null;
                        List<Fragment> list = getActivity().getSupportFragmentManager().getFragments();
                        for (Fragment fment : list) {
                            if (fment instanceof HomePageFragment) {
                                homePageFragment = (HomePageFragment) fment;
                            }
                        }
                        Handler handler = homePageFragment.getHandler();
                        handler.sendEmptyMessage(8080);


                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        HomePageFragment homePageFragment01 = null;
                        List<Fragment> list01 = getActivity().getSupportFragmentManager().getFragments();
                        for (Fragment fment : list01) {
                            if (fment instanceof HomePageFragment) {
                                homePageFragment01 = (HomePageFragment) fment;
                            }
                        }
                        Handler handler01 = homePageFragment01.getHandler();
                        handler01.sendEmptyMessage(8081);
                        break;
                }
                return false;
            }
        });

        //对ViewPager实现监听。
        home_info_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //设置圆点的初始状态
                five.setEnabled(false);
                six.setEnabled(false);
                switch (position){
                    case 0 :
                        five.setEnabled(true);
                        break;
                    case 1 :
                        six.setEnabled(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        data.clear();
//        count = 1 ;
//        findinfo();
//    }

    private void findinfo() {

        String urls ;
        if (GetBenSharedPreferences.getIsLogin(getActivity())){
              urls = String.format(Url.GetInfo, GetBenSharedPreferences.getTicket(getActivity()) ) ;
        }else {
              urls = String.format(Url.GetInfo, "" ) ;
        }

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        //params.addBodyParameter("access_token", "token");
        params.addQueryStringParameter("startpage" , "" + count );
        params.addQueryStringParameter("pagecount", "10" );
        httpUtils.configCurrentHttpCacheExpiry(1000);

        httpUtils.send(HttpRequest.HttpMethod.GET, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {


                Log.e("benben_home_info", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result) ;
                    String pages = jsonObject.getString("pages");

                    page = Integer.parseInt(pages);
                    count ++ ;

                    Log.e("benbne" , "当前页：" + count +"-------------总页数："+ pages ) ;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    com.alibaba.fastjson.JSONObject object = JSON.parseObject(responseInfo.result);
                    com.alibaba.fastjson.JSONArray data01 = object.getJSONArray("data");
                    List<V2InfoEntity> v2InfoEntities = JSON.parseArray(data01.toJSONString(), V2InfoEntity.class);
                    data.addAll(v2InfoEntities);

                    Log.e("benben", "数据的个数" + data.size()) ;

                    //home_listView.setDivider(new ColorDrawable(Color.GRAY));


                    adapter = new V2FindInfoAdapter(getActivity(), data);
                    home_listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    if (dialog != null ){
                        dialog.dismiss();
                    }

                    //setListViewHeightBasedOnChildren(home_listView) ;

            }

            @Override
            public void onFailure(HttpException error, String msg) {

                if (dialog != null ){
                    dialog.dismiss();
                }
                error.printStackTrace();
            }
        });

    }

    /**
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
