package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.FeedBackActivity;
import com.ziyawang.ziya.activity.LoginActivity;
import com.ziyawang.ziya.activity.MainActivity;
import com.ziyawang.ziya.activity.MyCollectActivity;
import com.ziyawang.ziya.activity.MyGoldActivity;
import com.ziyawang.ziya.activity.MyReleaseActivity;
import com.ziyawang.ziya.activity.MyRushActivity;
import com.ziyawang.ziya.activity.MySetActivity;
import com.ziyawang.ziya.activity.MyTeamWorkActivity;
import com.ziyawang.ziya.activity.PersonalInformationActivity;
import com.ziyawang.ziya.activity.ServiceRegisterActivity;
import com.ziyawang.ziya.activity.StartActivity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.LoadImageAsyncTask;
import com.ziyawang.ziya.tools.SDUtil;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyIconImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by 牛海丰 on 2016/7/19.
 */
public class MyFragment extends Fragment implements View.OnClickListener {

    //我的抢单按钮
    private TextView my_rush ;
    //服务方进入的头布局
    private RelativeLayout me_change_icon ;
    //发布方进入的头布局
    private RelativeLayout niu_relative ;
    //头像在内存中的缓存bitmap
    private Bitmap bitmap ;
    //服务方自定义显示头像组件
    private MyIconImageView default_icon ;
    //发布方自定义显示头像组件
    private MyIconImageView niu_icon ;
    //服务方认证
    private TextView service_register ;
    //用户缓存的root
    private static String root ;
    //服务方进入的电话号
    private TextView my_name ;
    //服务方进入的服务地区
    private TextView textView8 ;
    //服务方进入的名称
    private TextView textView9 ;
    //发布方或游客进入的电话号显示控件
    private TextView niu_phone ;
    //用户缓存的ticket
    private String login ;
    //用户缓存的电话号
    private String number_niu ;
    //用户缓存的是否登陆状态
    private boolean isLogin ;
    //提醒余额充值的图标
    private TextView add_pay ;
    //芽币剩余
    private String account ;
    //网络请求获取的数据，电话号，服务方名称，电话号，联系人，服务类型，公司所在地，服务介绍，三张图片介绍，服务地区
    private String username ;
    private String phoneNumber ;
    private String ServiceName ;
    private String ConnectPhone ;
    private String ConnectPerson ;
    private String ServiceType ;
    private String ServiceLocation ;
    private String ServiceIntroduction ;
    private String ConfirmationP1 ;
    private String ConfirmationP2 ;
    private String ConfirmationP3 ;
    private String ServiceArea ;
    //意见反馈按钮
    private TextView feedBack ;
    //设置按钮
    private TextView my_set ;
    //我的发布数量显示控件
    private TextView publish_count ;
    //我的合作数量显示控件
    private TextView cooperation_count ;
    //我的收藏数量显示控件
    private TextView collection_count ;
    //我的发布relative
    private RelativeLayout publish_relative  ;
    //我的合作relative
    private RelativeLayout cooperation_relative  ;
    //我的收藏relative
    private RelativeLayout collection_relative  ;
    public static final String RELEASE = "0" ;
    public static final String SERVICE = "1" ;
    public static final String UNSERVICE = "2" ;
    //下啦刷新组件
    private SwipeRefreshLayout swipeRefreshLayout;
    //无参构造
    public MyFragment(){}

    @Override
    public void onResume() {
        super.onResume();
        //当Fragment执行OnResume方法时，自动从内存中，加载头像。
        initIcon() ;
    }

    private void initIcon() {
        if (isLogin){
            //先拿到用户的缓存的头像,存在进行加载，不能重新操作。
            File files = new File(Url.IconPath);
            if (files.exists()) {
                final byte[] icons = SDUtil.getDataFromSDCard("icon.png");
                bitmap = BitmapFactory.decodeByteArray(icons, 0, icons.length);
                default_icon.setImageBitmap(bitmap);
                niu_icon.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实例化组件
        initView(view) ;
        //注册监听事件
        initListeners() ;
        //展示数据视图
        initData() ;
        //添加下拉刷新
        addSwipeRefreshLayout() ;
    }

    private void addSwipeRefreshLayout() {
        //添加下拉刷新。
        swipeRefreshLayout.setColorSchemeResources(R.color.one, R.color.two, R.color.three, R.color.four);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //加载数据
                loadData();
            }
        });
    }

    private void initData() {
        //得到缓存在本地的sp，拿到isLogin状态
        getSpData() ;
        if (isLogin){
            //展示登陆页面
            showLoginView() ;
        }else {
            //展示还未登陆页面
            showUnLoginView() ;
        }
    }

    private void showUnLoginView() {
        collection_count.setText("0");
        publish_count.setText("0");
        cooperation_count.setText("0");
    }

    private void showLoginView() {
        //加载本地的缓存phone到页面上
        loadSpPhoneData() ;
        //根据不同的root加载不同的布局
        showView() ;
        //加载内存中用户的头像
        initIcon();
        //开启网络请求，访问接口,加载数据.
        loadData() ;
    }

    private void loadSpPhoneData() {
        //将登陆的background擦除掉
        niu_phone.setBackgroundColor(Color.argb(0, 0, 0, 0));
        niu_phone.setText(number_niu);
    }

    private void showView() {
        if (!TextUtils.isEmpty(root)){
            switch (root){
                case RELEASE :
                    //发布方的展示组件
                    showReleaseViews() ;
                    break;
                case SERVICE :
                    //服务方的展示组件
                    showServiceViews() ;
                    break;
                case UNSERVICE :
                    //未审核通过的服务放的展示组件
                    showUnServiceViews() ;
                    break;
                default:
                    break;
            }
        }
    }

    private void showUnServiceViews() {
        my_rush.setVisibility(View.GONE);
        niu_relative.setVisibility(View.GONE);
        me_change_icon.setVisibility(View.VISIBLE);
    }

    private void showServiceViews() {
        my_rush.setVisibility(View.VISIBLE);
        niu_relative.setVisibility(View.GONE);
        me_change_icon.setVisibility(View.VISIBLE);
    }

    private void showReleaseViews() {
        my_rush.setVisibility(View.GONE);
        me_change_icon.setVisibility(View.GONE);
        niu_relative.setVisibility(View.VISIBLE);
    }

    private void getSpData() {
        login = GetBenSharedPreferences.getTicket(getActivity()) ;
        root = GetBenSharedPreferences.getRole(getActivity()) ;
        number_niu = GetBenSharedPreferences.getSpphoneNumber(getActivity()) ;
        isLogin = GetBenSharedPreferences.getIsLogin(getActivity()) ;
    }

    private void initListeners() {
        my_set.setOnClickListener(this);
        feedBack.setOnClickListener(this);
        my_rush.setOnClickListener(this);
        service_register.setOnClickListener(this);
        publish_relative.setOnClickListener(this);
        cooperation_relative.setOnClickListener(this);
        collection_relative.setOnClickListener(this);
        niu_relative.setOnClickListener(this);
        me_change_icon.setOnClickListener(this);
    }

    private void loadData() {
        if (GetBenSharedPreferences.getIsLogin(getActivity())){
            String urls = String.format(Url.Myicon, login) ;
            Log.e("benbne", login);
            HttpUtils utils = new HttpUtils() ;
            RequestParams params = new RequestParams() ;
            utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //关闭刷新
                    hiddenSwipe();
                    //处理请求成功后的数据
                    dealResult(responseInfo.result);
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    //关闭刷新
                    hiddenSwipe();
                    //打印用户的失败回调
                    error.printStackTrace();
                    ToastUtils.shortToast(getActivity(), "网络连接异常");
                }
            }) ;
        }else {
            //关闭刷新
            hiddenSwipe();
        }
    }

    private void hiddenSwipe() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void dealResult(String result) {
        Log.e("niuniu", result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            //得到发布，合作，收藏的数目
            getCount(jsonObject) ;
            //改变融云聊天的头像
            loadRongIcon(changeChatIcon(jsonObject));
            //存储当前用户的role
            String role = saveSpRole(jsonObject);
            switch (role) {
                case RELEASE:
                    //作为一个发布方加载的view
                    loadReleaseView(jsonObject) ;
                    break;
                case SERVICE:
                    //作为一个服务方加载的view
                    loadServiceView(result , jsonObject) ;
                    break;
                case UNSERVICE:
                    //作为一个未审核通过的服务方加载的view
                    unLoadServiceView(result, jsonObject) ;
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void unLoadServiceView(String result, JSONObject jsonObject) throws JSONException {
        my_rush.setVisibility(View.GONE);
        me_change_icon.setVisibility(View.VISIBLE);
        niu_relative.setVisibility(View.GONE);
        JSONObject object01 = new JSONObject(result);
        JSONObject service01 = object01.getJSONObject("service");
        //企业名称
        ServiceName = service01.getString("ServiceName");
        //企业简介
        ServiceIntroduction = service01.getString("ServiceIntroduction");
        //企业所在地
        ServiceLocation = service01.getString("ServiceLocation");
        //服务类型
        ServiceType = service01.getString("ServiceType");
        //联系人姓名
        ConnectPerson = service01.getString("ConnectPerson");
        //联系方式
        ConnectPhone = service01.getString("ConnectPhone");
        //图1
        ConfirmationP1 = service01.getString("ConfirmationP1");
        //图2
        ConfirmationP2 = service01.getString("ConfirmationP2");
        //图3
        ConfirmationP3 = service01.getString("ConfirmationP3");
        //服务地区
        ServiceArea = service01.getString("ServiceArea");
        JSONObject user101 = jsonObject.getJSONObject("user");
        final String userPicture01 = user101.getString("UserPicture");
        username = user101.getString("username");
        phoneNumber = user101.getString("phonenumber");
        niu_relative.setVisibility(View.GONE);
        me_change_icon.setVisibility(View.VISIBLE);
        //更改头像
        new LoadImageAsyncTask(new LoadImageAsyncTask.CallBack() {
            @Override
            public void setData(final Bitmap bitmap) {
                default_icon.setImageBitmap(bitmap);
                default_icon.setDrawingCacheEnabled(true);
                Bitmap bitmap_icon = Bitmap.createBitmap(default_icon.getDrawingCache());
                niu_icon.setDrawingCacheEnabled(false);
                Log.e("benbne", userPicture01);
                String subStr = getSubStr(userPicture01);
                final byte[] icon_data = getByte(bitmap_icon, subStr);
                SDUtil.saveDataInfoSDCard(icon_data, "ziya", "icon.png");
            }
        }).execute(Url.FileIP + userPicture01);
        my_name.setText(phoneNumber);
        textView8.setText(ServiceLocation);
        textView9.setText(ServiceName);
    }

    private void loadServiceView(String result, JSONObject jsonObject) throws JSONException {
        my_rush.setVisibility(View.VISIBLE);
        me_change_icon.setVisibility(View.VISIBLE);
        niu_relative.setVisibility(View.GONE);
        JSONObject object = new JSONObject(result);
        JSONObject service = object.getJSONObject("service");
        //企业名称
        ServiceName = service.getString("ServiceName");
        //企业简介
        ServiceIntroduction = service.getString("ServiceIntroduction");
        //企业所在地
        ServiceLocation = service.getString("ServiceLocation");
        //服务类型
        ServiceType = service.getString("ServiceType");
        //联系人姓名
        ConnectPerson = service.getString("ConnectPerson");
        //联系方式
        ConnectPhone = service.getString("ConnectPhone");
        //图1
        ConfirmationP1 = service.getString("ConfirmationP1");
        //图2
        ConfirmationP2 = service.getString("ConfirmationP2");
        //图3
        ConfirmationP3 = service.getString("ConfirmationP3");
        //服务地区
        ServiceArea = service.getString("ServiceArea");
        JSONObject user1 = jsonObject.getJSONObject("user");
        final String userPicture = user1.getString("UserPicture");
        username = user1.getString("username");
        phoneNumber = user1.getString("phonenumber");
        niu_relative.setVisibility(View.GONE);
        me_change_icon.setVisibility(View.VISIBLE);
        //更改头像
        new LoadImageAsyncTask(new LoadImageAsyncTask.CallBack() {
            @Override
            public void setData(final Bitmap bitmap) {
                default_icon.setImageBitmap(bitmap);
                default_icon.setDrawingCacheEnabled(true);
                Bitmap bitmap_icon = Bitmap.createBitmap(default_icon.getDrawingCache());
                niu_icon.setDrawingCacheEnabled(false);
                Log.e("benbne", userPicture);
                String subStr = getSubStr(userPicture);
                final byte[] icon_data = getByte(bitmap_icon, subStr);
                SDUtil.saveDataInfoSDCard(icon_data, "ziya", "icon.png");
            }
        }).execute(Url.FileIP + userPicture);
        my_name.setText(phoneNumber);
        textView8.setText(ServiceLocation);
        textView9.setText(ServiceName);
    }

    private void loadReleaseView(JSONObject jsonObject) throws JSONException {
        JSONObject user = jsonObject.getJSONObject("user");
        phoneNumber = user.getString("phonenumber");
        username = user.getString("username");
        final String UserPicture = user.getString("UserPicture");
        me_change_icon.setVisibility(View.GONE);
        niu_relative.setVisibility(View.VISIBLE);
        if ( username!=null && username.equals("")){
            niu_phone.setText(phoneNumber);
        }else {
            niu_phone.setText(username);
        }

        new LoadImageAsyncTask(new LoadImageAsyncTask.CallBack() {
            @Override
            public void setData(final Bitmap bitmap) {
                niu_icon.setImageBitmap(bitmap);
                niu_icon.setDrawingCacheEnabled(true);
                Bitmap bitmap_icon = Bitmap.createBitmap(niu_icon.getDrawingCache());
                niu_icon.setDrawingCacheEnabled(false);
                String subStr = getSubStr(UserPicture);
                final byte[] icon_data = getByte(bitmap_icon, subStr);
                SDUtil.saveDataInfoSDCard(icon_data, "ziya", "icon.png");
            }
        }).execute(Url.FileIP + UserPicture);
    }

    private String changeChatIcon(JSONObject jsonObject) throws JSONException {
        JSONObject ChatId = jsonObject.getJSONObject("user");
        return ChatId.getString("userid");
    }

    private String saveSpRole(JSONObject jsonObject) throws JSONException {
        String role1 = jsonObject.getString("role");
        Log.e("benben", role1);
        final SharedPreferences role = getActivity().getSharedPreferences("role", getActivity().MODE_PRIVATE);
        role.edit().putString("role", role1).commit();
        return role1 ;
    }

    private void getCount(JSONObject jsonObject) throws JSONException {
        //我的发布-->我的牙币余额
        //String myProCount = jsonObject.getString("MyProCount");
        JSONObject user = jsonObject.getJSONObject("user");
        account = user.getString("Account");
        int i = Integer.parseInt(account);
        if (i <= 10 ){
            add_pay.setVisibility(View.VISIBLE);
        }else {
            add_pay.setVisibility(View.GONE);
        }
        publish_count.setText(account);
        //我的收藏
        String myColCount = jsonObject.getString("MyColCount");
        collection_count.setText(myColCount);
        //我的合作--我的发布
        //String myCooCount = jsonObject.getString("MyCooCount");
        String myProCount = jsonObject.getString("MyProCount");
        cooperation_count.setText(myProCount);
    }

    private void loadRongIcon(final String chatId ) {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.send(HttpRequest.HttpMethod.POST, String.format(Url.RongIcon, chatId), params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dealChatIconResult(chatId, responseInfo.result);
                Log.e("RongIcon", responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }

        }) ;
    }

    private void dealChatIconResult(String chatId, String result) {
        try {
            String chatTitle = "";
            JSONObject jsonObject = new JSONObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            String userPicture = data.getString("UserPicture");
            String serviceName = data.getString("ServiceName");
            String role = data.getString("role");
            switch (role) {
                case RELEASE:
                case UNSERVICE:
                    String phonenumber = data.getString("phonenumber");
                    String username = data.getString("username");
                    if (!TextUtils.isEmpty(username) && !"".equals(username) ){
                        chatTitle = username ;
                    }else {
                        String substring = phonenumber.substring(0, 3);
                        String substring1 = phonenumber.substring(7, 11);
                        chatTitle = substring + "****" + substring1;
                    }

                    break;
                case SERVICE:
                    chatTitle = serviceName;
                    break;
                default:
                    break;
            }
            Uri uir = Uri.parse(Url.FileIP + userPicture);
            RongIM.getInstance().refreshUserInfoCache(new UserInfo(chatId, chatTitle, uir));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //得到图片的类型
    public String getSubStr(String str){
        int position = str.lastIndexOf(".");
        str = str.substring(position + 1);
        return str;
    }

    //将bitmap转化为byte[]
    public byte[] getByte(Bitmap bit , String str){
        Bitmap.CompressFormat temp = null;
        if ("jpg".equals(str)){
            temp = Bitmap.CompressFormat.JPEG;
        }else if ("png".equals(str)){
            temp = Bitmap.CompressFormat.PNG;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(temp, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    private void initView(View v ) {
        me_change_icon = (RelativeLayout)v.findViewById(R.id.me_change_icon ) ;
        service_register = (TextView) v.findViewById(R.id.service_register ) ;
        my_rush = (TextView) v.findViewById(R.id.my_rush ) ;
        default_icon = (MyIconImageView)v.findViewById(R.id.default_icon ) ;
        niu_icon = (MyIconImageView)v.findViewById(R.id.niu_icon ) ;
        my_name = (TextView)v.findViewById(R.id.my_name) ;
        textView8 = (TextView)v.findViewById(R.id.textView8) ;
        textView9 = (TextView)v.findViewById(R.id.textView9) ;
        niu_phone = (TextView)v.findViewById(R.id.niu_phone) ;
        feedBack = (TextView)v.findViewById(R.id.feedBack) ;
        niu_relative = (RelativeLayout)v.findViewById(R.id.niu_relative ) ;
        my_set = (TextView)v.findViewById(R.id.my_set ) ;
        collection_count = (TextView)v.findViewById(R.id.collection_count ) ;
        cooperation_count = (TextView)v.findViewById(R.id.cooperation_count ) ;
        publish_count = (TextView)v.findViewById(R.id.publish_count ) ;
        publish_relative = (RelativeLayout)v.findViewById(R.id.publish_relative ) ;
        cooperation_relative = (RelativeLayout)v.findViewById(R.id.cooperation_relative ) ;
        collection_relative = (RelativeLayout)v.findViewById(R.id.collection_relative ) ;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        add_pay = (TextView) v.findViewById(R.id.add_pay ) ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //服务方head按钮
            case R.id.me_change_icon :
                judgeServiceIconView() ;
                break;
            //发布方head按钮
            case R.id.niu_relative :
                judgeReleaseIconView() ;
                break;
            //我的发布按钮-->我的牙币
            case R.id.publish_relative :
                //judgePublishView() ;
                judgeMyGoldVIew() ;
                break;
            //我的合作按钮-->我的发布
            case R.id.cooperation_relative :
                //judgeCooperationView() ;
                judgePublishView() ;
                break;
            //我的收藏按钮
            case R.id.collection_relative :
                judgeCollectionView() ;
                break;
            //服务方认证按钮
            case R.id.service_register :
                judgeServiceRegisterView() ;
                break;
            //我的抢单按钮
            case R.id.my_rush :
                judgeMyRushView();
                break;
            //意见反馈按钮
            case R.id.feedBack :
                judgeFeedBackView();
                break;
            //设置按钮
            case R.id.my_set :
                goSetActivity() ;
                break;
            default:
                break;
        }
    }

    private void judgeMyGoldVIew() {
        if (isLogin){
            goMyGoldActivity() ;
        }else {
            goLoginActivity();
        }
    }

    private void judgeFeedBackView() {
        if (isLogin){
            goFeedBackActivity() ;
        }else {
            goLoginActivity() ;
        }
    }

    private void judgeMyRushView() {
        if (isLogin){
            goMyRushActivity() ;
        }else {
            goLoginActivity() ;
        }
    }

    private void judgeServiceRegisterView() {
        if (isLogin){
            goServiceRegisterActivity() ;
        }else {
            goLoginActivity() ;
        }
    }

    private void judgeCollectionView() {
        if (isLogin){
            goMyCollectActivity() ;
        }else {
            goLoginActivity() ;
        }
    }

    private void judgeCooperationView() {
        if (isLogin){
            goMyTeamWorkActivity() ;
        }else {
            goLoginActivity() ;
        }
    }

    private void judgePublishView() {
        if (isLogin){
            goMyReleaseActivity() ;
        }else {
            goLoginActivity() ;
        }
    }

    private void judgeReleaseIconView() {
        if (isLogin){
            goPersonalInformationActivity() ;
        }else {
            goLoginActivity() ;
        }
    }

    private void judgeServiceIconView() {
        if (isLogin){
            goPersonalInformationActivity() ;
        }else {
            goLoginActivity() ;
        }
    }

    private void goPersonalInformationActivity() {
        Intent intent = new Intent(getActivity() , PersonalInformationActivity.class ) ;
        intent.putExtra("phoneNumber" , phoneNumber ) ;
        intent.putExtra("username" , username ) ;
        startActivity(intent);
    }

    private void goMyCollectActivity() {
        Intent intent = new Intent(getActivity() , MyCollectActivity.class ) ;
        startActivity(intent);
    }

    private void goMyTeamWorkActivity() {
        Intent intent = new Intent(getActivity() , MyTeamWorkActivity.class ) ;
        startActivity(intent);
    }

    private void goMyReleaseActivity() {
        Intent intent = new Intent(getActivity() , MyReleaseActivity.class ) ;
        startActivity(intent);
    }

    private void goServiceRegisterActivity() {
        Intent intent = new Intent(getActivity()  , ServiceRegisterActivity.class ) ;
        root = GetBenSharedPreferences.getRole(getActivity()) ;
        intent.putExtra("root" , root ) ;
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
                break;
        }
        startActivity(intent);
    }

    private void goMyRushActivity() {
        Intent intent = new Intent(getActivity()  , MyRushActivity.class ) ;
        startActivity(intent);
    }

    private void goFeedBackActivity() {
        Intent intent = new Intent(getActivity() , FeedBackActivity.class ) ;
        startActivity(intent);
    }

    private void goLoginActivity() {
        Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
        startActivity(intent);
    }

    private void goSetActivity() {
        Intent intent = new Intent(getActivity() , MySetActivity.class ) ;
        startActivity(intent);
    }

    private void goMyGoldActivity() {
        Intent intent = new Intent(getActivity() , MyGoldActivity.class ) ;
        intent.putExtra("account" , account ) ;
        startActivity(intent);
    }

}
