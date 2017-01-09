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
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.FindServiceActivity;
import com.ziyawang.ziya.adapter.FindServiceAdapter;
import com.ziyawang.ziya.adapter.HomeViewPagerAdapter;
import com.ziyawang.ziya.entity.FindServiceEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/7/28.
 */
public class HomeServiceFragment extends Fragment {

    private ViewPager home_service_viewPager;
    private List<View> viewList_service = new ArrayList<View>();
    private FindServiceAdapter adapter;
    private BenListView home_service_listView;
    private LayoutInflater inflater;

    private HomeViewPagerAdapter homeViewPagerAdapter;

    private List<FindServiceEntity> data = new ArrayList<FindServiceEntity>() ;

    private int page ;
    private int count = 1 ;

    private MyProgressDialog dialog ;

    private Boolean isOK = true ;

    private ImageView five , six ;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 200) {
                // Bundle b = msg.getData();
                // tv.setText(b.getString("num"));
                Log.e("HomeINfo", count + "====================================" + page) ;

                if (isOK){
                    if (count <= page ){
                        isOK = false ;
                        dialog = new MyProgressDialog(getActivity() , "加载数据中请稍后。。。") ;
                        dialog.show();
                        findService();
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
                        ToastUtils.shortToast(getActivity() , "服务没有更多数据");
                    }
                }

            }else if (msg.what == 12345) {
                count = 1 ;
                data.clear();
                findService();
            }
            super.handleMessage(msg);
        }
    };

    public HomeServiceFragment(){}

    public Handler getHandler(){
        return mHandler;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //实例化组件
        initView(view);

        inflater = LayoutInflater.from(getActivity());
        View view3 = inflater.inflate(R.layout.view_service_one , null ) ;
        View view4 = inflater.inflate(R.layout.view_service_two , null ) ;
        viewList_service.add(view3) ;
        viewList_service.add(view4) ;
        homeViewPagerAdapter = new HomeViewPagerAdapter(viewList_service);
        home_service_viewPager.setAdapter(homeViewPagerAdapter);

        findService() ;

        viewList_service.get(0).findViewById(R.id.service_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben", "--------------------------------服务01");
                Intent intent = new Intent(getActivity() , FindServiceActivity.class  )  ;
                intent.putExtra("typeName" , "01" ) ;
                startActivity(intent);
            }
        });
        viewList_service.get(0).findViewById(R.id.service_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "--------------------------------服务02") ;
                Intent intent = new Intent(getActivity() , FindServiceActivity.class  )  ;
                intent.putExtra("typeName" , "14" ) ;
                startActivity(intent);
            }
        });
        viewList_service.get(1).findViewById(R.id.service_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "--------------------------------服务03") ;
                Intent intent = new Intent(getActivity() , FindServiceActivity.class  )  ;
                intent.putExtra("typeName" , "03" ) ;
                startActivity(intent);
            }
        });
        viewList_service.get(0).findViewById(R.id.service_four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "--------------------------------服务04") ;
                Intent intent = new Intent(getActivity() , FindServiceActivity.class  )  ;
                intent.putExtra("typeName" , "04" ) ;
                startActivity(intent);
            }
        });
        viewList_service.get(0).findViewById(R.id.service_five).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "--------------------------------服务05") ;
                Intent intent = new Intent(getActivity() , FindServiceActivity.class  )  ;
                intent.putExtra("typeName" , "05" ) ;
                startActivity(intent);
            }
        });
        viewList_service.get(0).findViewById(R.id.service_six).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "--------------------------------服务06") ;
                Intent intent = new Intent(getActivity() , FindServiceActivity.class  )  ;
                intent.putExtra("typeName" , "06" ) ;
                startActivity(intent);
            }
        });
        viewList_service.get(0).findViewById(R.id.service_seven).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "--------------------------------服务07") ;
                Intent intent = new Intent(getActivity() , FindServiceActivity.class  )  ;
                intent.putExtra("typeName" , "10" ) ;
                startActivity(intent);
            }
        });
        viewList_service.get(0).findViewById(R.id.service_eight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "--------------------------------服务08") ;
                Intent intent = new Intent(getActivity() , FindServiceActivity.class  )  ;
                intent.putExtra("typeName" , "12" ) ;
                startActivity(intent);
            }
        });
        viewList_service.get(1).findViewById(R.id.service_nine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben", "--------------------------------服务09");
                Intent intent = new Intent(getActivity() , FindServiceActivity.class  )  ;
                intent.putExtra("typeName" , "ben" ) ;
                startActivity(intent);
            }
        });
        viewList_service.get(0).findViewById(R.id.service_ten).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("benben" , "--------------------------------服务10") ;
                Intent intent = new Intent(getActivity() , FindServiceActivity.class  )  ;
                intent.putExtra("typeName" , "02" ) ;
                startActivity(intent);
            }
        });

    }

    private void initView(View view) {

        home_service_viewPager = (ViewPager) view.findViewById(R.id.home_service_viewPager);
        home_service_listView = (BenListView) view.findViewById(R.id.home_service_listView);

        home_service_listView.setDivider(new ColorDrawable(Color.argb(0, 244, 244, 244)));
        home_service_listView.setDividerHeight(1);

        five = (ImageView)view.findViewById(R.id.five ) ;
        six = (ImageView)view.findViewById(R.id.six ) ;


        //ViewPager点击时消费到touch方法
        home_service_viewPager.setOnTouchListener(new View.OnTouchListener() {
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
                return false ;
            }
        });

        //对ViewPager实现监听。
        home_service_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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




    private void findService() {

        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        params.addQueryStringParameter("startpage" , "" + count );
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.GetService, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {



                Log.e("benben", responseInfo.result) ;

                Log.e("benben_home_service", responseInfo.result);
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
                List<FindServiceEntity> loadDataEntity = JSON.parseArray(data01.toJSONString(), FindServiceEntity.class);
                data.addAll(loadDataEntity) ;

                adapter = new FindServiceAdapter(getActivity() , data ) ;
                home_service_listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (dialog != null ){
                    dialog.dismiss();
                }



            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (dialog != null ){
                    dialog.dismiss();
                }
                error.printStackTrace();
            }
        }) ;

    }

}
