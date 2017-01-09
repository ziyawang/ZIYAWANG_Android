package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.EvaluateActivity_start;
import com.ziyawang.ziya.activity.FindVideoActivity;
import com.ziyawang.ziya.activity.LoginActivity;
import com.ziyawang.ziya.activity.MainActivity;
import com.ziyawang.ziya.activity.MyRuleActivity;
import com.ziyawang.ziya.activity.SearchActivity;
import com.ziyawang.ziya.activity.ServiceRegisterActivity;
import com.ziyawang.ziya.activity.VipCenterActivity;
import com.ziyawang.ziya.adapter.HeadpagerAdapter;
import com.ziyawang.ziya.entity.BannerEntity;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.tools.FixedSpeedScroller;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyScrollView;
import com.ziyawang.ziya.view.NotificationButton;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.rong.imageloader.core.ImageLoader;

/**
 * Created by 牛海丰 on 2016/7/19.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener{

    public static final String RELEASE = "0" ;
    public static final String SERVICE = "1" ;
    public static final String UNSERVICE = "2" ;

    private SharedPreferences spVideoID ;

    private TextView homepage_change;
    private TextView homepage_home;
    private TextView homepage_change02;

    private RelativeLayout niu_head;

    private MyScrollView mys;
    private HomeInfoFragment homeInfoFragment;
    private HomeServiceFragment homeServiceFragment;

    private FragmentManager manager01;
    private FragmentTransaction transaction01;

    //跳转到搜索页面的按钮
    private RelativeLayout homepage_search;
    //跳转到搜索页面的悬停按钮
    private RelativeLayout homepage_search02;
    //跳转到搜索视频的页面的按钮
    private FrameLayout homepage_video ;
    //跳转到搜索视频的页面的悬停按钮
    private FrameLayout homepage_video_02 ;
    //跳转到测评系统的页面的按钮
    private FrameLayout homepage_sys ;
    //跳转到测评系统的页面的悬停按钮
    private FrameLayout homepage_sys02 ;
    private RelativeLayout search01;

    private ViewPager home_viewPager;

    public HomePageFragment() {}

    private FrameLayout home_frame;

    private List<ImageView> list;

    private HeadpagerAdapter adapter;

    private ImageView one, two, three , four ;
    //系统显示的小红点
    private ImageView red_point ;
    private ImageView red_point02 ;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 0x01){//收到消息1
                handler.removeMessages(0x02);//remove调消息2
                handler.sendEmptyMessageDelayed(0x02, 4000);//重新发送消息2
            }else if(msg.what == 0x02){//收到消息2，滚动到下一张轮播图
                final int position = home_viewPager.getCurrentItem();
                home_viewPager.setCurrentItem(position + 1);
            }
            if (msg.what == 888) {
                loadData();
            }
            if (msg.what == 8080){
                swipeRefreshLayout.setEnabled(false);
            }
            if (msg.what == 8081){
                swipeRefreshLayout.setEnabled(true);
            }
        }
    };

    public Handler getHandler(){
        return handler;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mys.smoothScrollTo(0, 0);
            mys.scrollTo(0, 0);
            homepage_search.setVisibility(View.VISIBLE);
            search01.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepager, container, false);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (!GetBenSharedPreferences.getIsLoad(getActivity())){
//            switch (homepage_change.getText().toString()) {
//                case "找信息":
//                    sendMessageToFrame(12345);
//                    break;
//                case "找服务":
//                    sendMessageToServ(12345);
//                    break;
//                default:
//                    break;
//            }
//            isLoad = getActivity().getSharedPreferences("isLoad" , getActivity().MODE_PRIVATE ) ;
//            isLoad.edit().putBoolean("isLoad", true).commit();
//        }
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实例化组件
        initView(view);
        //default Fragment
        selectedFragment();
        //注册监听
        initListeners() ;
        //根据时间是否显示小红点
        isShowRedPoint() ;
        //添加下拉刷新。
        swipeRefreshLayout.setColorSchemeResources(R.color.one, R.color.two, R.color.three, R.color.four);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (homepage_change.getText().toString()) {
                    case "找信息":
                        sendMessageToFrame(12345);
                        break;
                    case "找服务":
                        sendMessageToServ(12345);
                        break;
                    default:
                        break;
                }
                //重新加载首页Banner的数据
                loadData();
                //是否有新的视频
                isShowRedPoint() ;

            }
        });
        //定义一个子线程，发送handler888
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                handler.sendEmptyMessage(888);
            }
        };
        thread1.start();
        //对ScrollView的滑动的监听
        mys.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                View childView = mys.getChildAt(0);
                if (childView.getMeasuredHeight() <= scrollY + mys.getHeight()) {
                    switch (homepage_change.getText().toString()) {
                        case "找信息":
                            sendMessageToFrame(200);
                            break;
                        case "找服务":
                            sendMessageToServ(200);
                            break;
                        default:
                            break;
                    }
                }

                if (scrollY >= niu_head.getBottom()) {
                    if (search01.getVisibility() == View.INVISIBLE) {
                        //根据homepage_change的值，来进行数据的显示
                        judgeShowView();
                        homepage_search.setVisibility(View.INVISIBLE);
                        search01.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (search01.getVisibility() == View.VISIBLE) {
                        //根据homepage_change的值，来进行数据的显示
                        judgeShowView();
                        homepage_search.setVisibility(View.VISIBLE);
                        search01.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        //点击跳转到搜索页面
        homepage_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeGoView(homepage_change);
            }
        });
        homepage_search02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeGoView(homepage_change02);
            }
        });
        //更改信息和服务
        homepage_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager01 = getActivity().getSupportFragmentManager();
                transaction01 = manager01.beginTransaction();
                mys.smoothScrollTo(0, 0);
                mys.scrollTo(0, 0);
                if (homepage_change.getText().toString().equals("找服务")) {
                    homepage_change.setText("找信息");
                    //展示信息的Fragment
                    if (!homeInfoFragment.isAdded()) {
                        transaction01.hide(homeServiceFragment).add(R.id.home_frame, homeInfoFragment).commit();
                    } else {
                        transaction01.hide(homeServiceFragment).show(homeInfoFragment).commit();
                    }
                } else {
                    homepage_change.setText("找服务");
                    //展示服务的Fragment
                    if (!homeServiceFragment.isAdded()) {
                        transaction01.hide(homeInfoFragment).add(R.id.home_frame, homeServiceFragment).commit();
                    } else {
                        transaction01.hide(homeInfoFragment).show(homeServiceFragment).commit();
                    }
                }
            }
        });
    }

    private void isShowRedPoint() {
        //拿到现在最新的存在的VideoID
        LoadVideoID() ;
    }

    private void LoadVideoID() {
        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;

        params.addQueryStringParameter("pagecount", "1");
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.GetMovie, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");
                List<FindVideoEntity> list = JSON.parseArray(result.toJSONString(), FindVideoEntity.class);
                if (list.size() == 1) {
                    String videoID = list.get(0).getVideoID();
                    Log.e("benben", "VideoID======" + videoID);
                    //拿到用户缓存的spVideoID的值,用户看过的最新的VideoID
                    spVideoID = getActivity().getSharedPreferences("VideoID", getActivity().MODE_PRIVATE);
                    String spVideoIDString = spVideoID.getString("VideoID", "");
                    //boolean isLook1 = isLook.getBoolean("isLook", false);
                    Log.e("benben", "spVideoID======" + spVideoIDString);
                    if (!spVideoIDString.equals(videoID)) {
                        red_point.setVisibility(View.VISIBLE);
                        red_point02.setVisibility(View.VISIBLE);
                    } else {
                        red_point.setVisibility(View.GONE);
                        red_point02.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();

            }
        }) ;
    }

    private void initListeners() {
        homepage_video.setOnClickListener(this);
        homepage_video_02.setOnClickListener(this);
        homepage_sys.setOnClickListener(this);
        homepage_sys02.setOnClickListener(this);
    }

    private void judgeGoView(TextView tv ) {
        switch (tv.getText().toString()) {
            case "找信息":
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("type", "信息");
                intent.putExtra("title", "搜索信息");
                startActivity(intent);
                break;
            case "找服务":
                Intent intent01 = new Intent(getActivity(), SearchActivity.class);
                intent01.putExtra("type", "服务");
                intent01.putExtra("title", "搜索服务");
                startActivity(intent01);
                break;
            default:
                break;
        }
    }

    private void judgeShowView() {
        switch (homepage_change.getText().toString()) {
            case "找信息":
                homepage_change02.setText("找信息");
                break;
            case "找服务":
                homepage_change02.setText("找服务");
                break;
            default:
                break;
        }
    }

    private void loadData() {

        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        utils.configCurrentHttpCacheExpiry(1000);
        utils.send(HttpRequest.HttpMethod.GET, Url.BannerTWO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result);

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                //JSONArray objects = JSON.parseArray(responseInfo.result);
                final List<BannerEntity> list01 = JSON.parseArray(responseInfo.result, BannerEntity.class);

                list = new ArrayList<ImageView>();
                ImageView img1 = new ImageView(getActivity());
                BitmapUtils bitmapUitl = new BitmapUtils(getActivity());
                bitmapUitl.display(img1, Url.FileIP + list01.get(0).getBannerLink());
                img1.setScaleType(ImageView.ScaleType.FIT_XY);
                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("5".equals(list01.get(0).getTypeID())){
                            Intent intent = new Intent( getActivity() , MyRuleActivity.class ) ;
                            intent.putExtra("type" , "other" ) ;
                            intent.putExtra("url" , list01.get(0).getUrl() ) ;
                            intent.putExtra("title" , list01.get(0).getTitle() ) ;
                            startActivity(intent);
                        }else if ("6".equals(list01.get(0).getTypeID())) {
                            if (GetBenSharedPreferences.getIsLogin(getActivity())){
                                switch (GetBenSharedPreferences.getRole(getActivity())){
                                    case "1" :
                                        goVipCenterActivity() ;
                                        break;
                                    default:
                                        goServiceRegisterActivity() ;
                                        break;

                                }
                            }else {
                                Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                                startActivity(intent);
                            }
                        }else {
                            Intent intent = new Intent(getActivity(), FindVideoActivity.class);
                            startActivity(intent);
                        }

                    }
                });
                list.add(img1);

                ImageView img2 = new ImageView(getActivity());
                bitmapUitl.display(img2, Url.FileIP + list01.get(1).getBannerLink());
                img2.setScaleType(ImageView.ScaleType.FIT_XY);
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("5".equals(list01.get(1).getTypeID())){
                            Intent intent = new Intent( getActivity() , MyRuleActivity.class ) ;
                            intent.putExtra("type" , "other" ) ;
                            intent.putExtra("url" , list01.get(1).getUrl() ) ;
                            intent.putExtra("title" , list01.get(1).getTitle() ) ;
                            startActivity(intent);
                        }else if ("6".equals(list01.get(1).getTypeID())) {
                            if (GetBenSharedPreferences.getIsLogin(getActivity())){
                                switch (GetBenSharedPreferences.getRole(getActivity())){
                                    case "1" :
                                        goVipCenterActivity() ;
                                        break;
                                    default:
                                        goServiceRegisterActivity() ;
                                        break;

                                }
                            }else {
                                Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                                startActivity(intent);
                            }

                        }else {
                            Intent intent = new Intent(getActivity(), FindVideoActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                list.add(img2);

                ImageView img3 = new ImageView(getActivity());
                bitmapUitl.display(img3, Url.FileIP + list01.get(2).getBannerLink());
                img3.setScaleType(ImageView.ScaleType.FIT_XY);
                img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("5".equals(list01.get(2).getTypeID())){
                            Intent intent = new Intent( getActivity() , MyRuleActivity.class ) ;
                            intent.putExtra("type" , "other" ) ;
                            intent.putExtra("url" , list01.get(2).getUrl() ) ;
                            intent.putExtra("title" , list01.get(2).getTitle() ) ;
                            startActivity(intent);
                        }else if ("6".equals(list01.get(2).getTypeID())) {
                            if (GetBenSharedPreferences.getIsLogin(getActivity())){
                                switch (GetBenSharedPreferences.getRole(getActivity())){
                                    case "1" :
                                        goVipCenterActivity() ;
                                        break;
                                    default:
                                        goServiceRegisterActivity() ;
                                        break;

                                }
                            }else {
                                Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                                startActivity(intent);
                            }

                        }else {
                            Intent intent = new Intent(getActivity(), FindVideoActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                list.add(img3);

                ImageView img4 = new ImageView(getActivity());
                bitmapUitl.display(img4, Url.FileIP + list01.get(3).getBannerLink());
                img4.setScaleType(ImageView.ScaleType.FIT_XY);
                img4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("5".equals(list01.get(3).getTypeID())){
                            Intent intent = new Intent( getActivity() , MyRuleActivity.class ) ;
                            intent.putExtra("type" , "other" ) ;
                            intent.putExtra("url" , list01.get(3).getUrl() ) ;
                            intent.putExtra("title" , list01.get(3).getTitle() ) ;
                            startActivity(intent);
                        }else if ("6".equals(list01.get(3).getTypeID())) {
//                            NotificationButton button_me = (NotificationButton) getActivity().findViewById(R.id.button_me);
//                            Button button_homePage = (Button) getActivity().findViewById(R.id.button_homePage);
//                            button_homePage.setSelected(false);
//                            button_homePage.setClickable(true);
//                            button_me.setSelected(true);
//                            button_me.setClickable(true);
//
//                            MainActivity activity=(MainActivity)getActivity()  ;
//                            FragmentManager fm = activity.getSupportFragmentManager();
//                            FragmentTransaction transaction = fm.beginTransaction();
//                            if (!activity.myFragment.isAdded()) {
//                                transaction.hide(activity.homePageFragment).add(R.id.main_frameyout, activity.myFragment).commit();
//                            }else {
//                                transaction.hide(activity.homePageFragment).show(activity.myFragment).commit();
//                            }
                            if (GetBenSharedPreferences.getIsLogin(getActivity())){
                                switch (GetBenSharedPreferences.getRole(getActivity())){
                                    case "1" :
                                        goVipCenterActivity() ;
                                        break;
                                    default:
                                        goServiceRegisterActivity() ;
                                        break;

                                }
                            }else {
                                Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                                startActivity(intent);
                            }

                        }else {
                            Intent intent = new Intent(getActivity(), FindVideoActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                list.add(img4);

                //实例化适配器
                adapter = new HeadpagerAdapter(list, getActivity());
                //为ViewPager添加适配器
                home_viewPager.setAdapter(adapter);

                //将ViewPager下的四个小点点加入到ViewPager之中。
                final List<ImageView> list_diadian = new ArrayList<>();
                list_diadian.add(one);
                list_diadian.add(two);
                list_diadian.add(three);
                list_diadian.add(four);


                //ViewPager实现点击事件的监听
                home_viewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_MOVE:
                                swipeRefreshLayout.setEnabled(false);
                                break;
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                swipeRefreshLayout.setEnabled(true);
                                break;
                        }
                        return false;
                    }
                });
                //对ViewPager实现监听。
                home_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        //设置圆点的初始状态
                        initialState();
                        list_diadian.get(position % list_diadian.size()).setEnabled(true);

                        //发送消息1
                        handler.sendEmptyMessage(0x01);
                    }

                    @Override
                    public void onPageSelected(int position) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                //刷新适配器
                adapter.notifyDataSetChanged();

                mys.smoothScrollTo(0, 0);
                mys.scrollTo(0, 0);

            }

            @Override
            public void onFailure(HttpException error, String msg) {

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                error.printStackTrace();
            }
        });

    }

    private void goVipCenterActivity() {
        String urls = String.format(Url.Myicon, GetBenSharedPreferences.getTicket(getActivity())) ;
        HttpUtils utils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //处理请求成功后的数据
                try {
                    dealResult02(responseInfo.result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //打印用户的失败回调
                error.printStackTrace();
                ToastUtils.shortToast(getActivity(), "网络连接异常");
            }
        }) ;
    }

    private void dealResult02(String result) throws JSONException {
        org.json.JSONObject jsonObject = new org.json.JSONObject(result);
        org.json.JSONObject user1 = jsonObject.getJSONObject("user");
        org.json.JSONObject showright = user1.getJSONObject("showright");
        String type_01 = showright.optString("资产包");
        String type_02 = showright.optString("企业商账");
        String type_03 = showright.optString("固定资产");
        String type_04 = showright.optString("融资信息");
        String type_05 = showright.optString("个人债权");
        Intent intent = new Intent(getActivity() , VipCenterActivity.class ) ;
        intent.putExtra("type_01" , type_01 ) ;
        intent.putExtra("type_02" , type_02 ) ;
        intent.putExtra("type_03" , type_03 ) ;
        intent.putExtra("type_04" , type_04 ) ;
        intent.putExtra("type_05", type_05) ;
        startActivity(intent);
    }

    private void goServiceRegisterActivity() {
        String urls = String.format(Url.Myicon, GetBenSharedPreferences.getTicket(getActivity())) ;
        HttpUtils utils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //处理请求成功后的数据
                try {
                    dealResult(responseInfo.result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //打印用户的失败回调
                error.printStackTrace();
                ToastUtils.shortToast(getActivity(), "网络连接异常");
            }
        }) ;
    }

    private void dealResult(String result) throws JSONException {
        org.json.JSONObject object = new org.json.JSONObject(result);
        org.json.JSONObject service = object.getJSONObject("service");
        //企业名称
        String ServiceName = service.getString("ServiceName");
        //企业简介
        String ServiceIntroduction = service.getString("ServiceIntroduction");
        //企业所在地
        String ServiceLocation = service.getString("ServiceLocation");
        //服务类型
        String ServiceType = service.getString("ServiceType");
        //联系人姓名
        String ConnectPerson = service.getString("ConnectPerson");
        //联系方式
        String ConnectPhone = service.getString("ConnectPhone");
        //图1
        String ConfirmationP1 = service.getString("ConfirmationP1");
        //图2
        String ConfirmationP2 = service.getString("ConfirmationP2");
        //图3
        String ConfirmationP3 = service.getString("ConfirmationP3");
        //服务地区
        String ServiceArea = service.getString("ServiceArea");

        String Regtime = service.getString("RegTime");
        String Founds = service.getString("Founds");
        String Size = service.getString("Size");

        Intent intent = new Intent(getActivity()  , ServiceRegisterActivity.class ) ;
        String root = GetBenSharedPreferences.getRole(getActivity()) ;
        intent.putExtra("root", root) ;
        switch (root){
            case SERVICE :
            case UNSERVICE :
                //企业名称
                intent.putExtra("ServiceName" , ServiceName) ;
                //企业简介
                intent.putExtra("ServiceIntroduction" , ServiceIntroduction) ;
                //企业所在地
                intent.putExtra("ServiceLocation" , ServiceLocation) ;
                //服务类型
                intent.putExtra("ServiceType" , ServiceType) ;
                //联系人姓名
                intent.putExtra("ConnectPerson" , ConnectPerson) ;
                //联系方式
                intent.putExtra("ConnectPhone" , ConnectPhone) ;
                //图1
                intent.putExtra("ConfirmationP1" , ConfirmationP1) ;
                //图2
                intent.putExtra("ConfirmationP2" , ConfirmationP2) ;
                //图3
                intent.putExtra("ConfirmationP3" , ConfirmationP3) ;
                //服务地区
                intent.putExtra("ServiceArea", ServiceArea) ;

                intent.putExtra("Size", Size ) ;
                intent.putExtra("Founds", Founds ) ;
                intent.putExtra("Regtime", Regtime ) ;
                break;
        }
        startActivity(intent);

    }

    private void initialState() {
        one.setEnabled(false);
        two.setEnabled(false);
        three.setEnabled(false);
        four.setEnabled(false);
    }

    FixedSpeedScroller mScroller = null;

    private void controlViewPagerSpeed() {
        try {
            Field mField;
            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(getActivity(), new AccelerateInterpolator());
            mScroller.setmDuration(300); // 2000ms
            mField.set(home_viewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        homepage_change = (TextView) view.findViewById(R.id.homepage_change);
        homepage_change02 = (TextView) view.findViewById(R.id.homepage_change02);
        homepage_home = (TextView) view.findViewById(R.id.homepage_home);
        home_frame = (FrameLayout) view.findViewById(R.id.home_frame);
        homepage_search = (RelativeLayout) view.findViewById(R.id.homepage_search);
        homepage_search02 = (RelativeLayout) view.findViewById(R.id.homepage_search02);
        homepage_video = (FrameLayout) view.findViewById(R.id.homepage_video);
        homepage_video_02 = (FrameLayout) view.findViewById(R.id.homepage_video_02);
        homepage_sys = (FrameLayout) view.findViewById(R.id.homepage_sys);
        homepage_sys02 = (FrameLayout) view.findViewById(R.id.homepage_sys02);
        search01 = (RelativeLayout) view.findViewById(R.id.search01);
        niu_head = (RelativeLayout) view.findViewById(R.id.niu_head);
        mys = (MyScrollView) view.findViewById(R.id.mys);
        home_viewPager = (ViewPager) view.findViewById(R.id.home_viewPager);
        controlViewPagerSpeed();
        one = (ImageView) view.findViewById(R.id.one);
        two = (ImageView) view.findViewById(R.id.two);
        three = (ImageView) view.findViewById(R.id.three);
        four = (ImageView) view.findViewById(R.id.four);
        red_point = (ImageView) view.findViewById(R.id.red_point);
        red_point02 = (ImageView) view.findViewById(R.id.red_point02);
    }

    private void selectedFragment() {
        manager01 = getActivity().getSupportFragmentManager();
        transaction01 = manager01.beginTransaction();
        homeInfoFragment = new HomeInfoFragment();
        transaction01.add(R.id.home_frame, homeInfoFragment);
        transaction01.commit();
        homeServiceFragment = new HomeServiceFragment();
    }

    /**
     * 传递消息给子找信息fragment
     *
     * @param what
     */
    private void sendMessageToFrame(int what) {
        HomeInfoFragment homeInfoFragment = null;
        List<Fragment> list = getActivity().getSupportFragmentManager().getFragments();
        for (Fragment fment : list) {
            if (fment instanceof HomeInfoFragment) {
                homeInfoFragment = (HomeInfoFragment) fment;
            }
        }
        Handler handler = homeInfoFragment.getHandler();
        handler.sendEmptyMessage(what);
    }

    /**
     * 传递消息给子找服务fragment
     *
     * @param what
     */
    private void sendMessageToServ(int what) {
        HomeServiceFragment homeServiceFragment = null;
        List<Fragment> list = getActivity().getSupportFragmentManager().getFragments();
        for (Fragment fment : list) {
            if (fment instanceof HomeServiceFragment) {
                homeServiceFragment = (HomeServiceFragment) fment;
            }
        }
        Handler handler = homeServiceFragment.getHandler();
        handler.sendEmptyMessage(what);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homepage_video :
                //跳转到播放视频页面
                goFindVideoActivity() ;
                break;
            case R.id.homepage_video_02 :
                //跳转到播放视频页面
                goFindVideoActivity() ;
                break;
            case R.id.homepage_sys:
            case R.id.homepage_sys02:
                goEvaluateActivity_start() ;
                break;
            default:
                break;
        }
    }

    private void goEvaluateActivity_start() {
        Intent intent = new Intent(getActivity() , EvaluateActivity_start.class ) ;
        startActivity(intent);
    }

    private void goFindVideoActivity() {
        Intent intent = new Intent(getActivity(), FindVideoActivity.class);
        startActivity(intent);
    }


}
