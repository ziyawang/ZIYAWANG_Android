package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.ziyawang.ziya.activity.MyReleaseActivity;
import com.ziyawang.ziya.activity.MyRushActivity;
import com.ziyawang.ziya.activity.MySetActivity;
import com.ziyawang.ziya.activity.MyTeamWorkActivity;
import com.ziyawang.ziya.activity.PersonalInformationActivity;
import com.ziyawang.ziya.activity.ServiceRegisterActivity;
import com.ziyawang.ziya.activity.StartActivity;
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
public class MyFragment extends Fragment {


    private TextView release_head ;
    private TextView my_rush ;
    private RelativeLayout me_change_icon ;
    private RelativeLayout niu_relative ;

    public MyFragment(){}

    private Bitmap bitmap ;
    private MyIconImageView default_icon ;
    private MyIconImageView niu_icon ;

    private TextView my_release ;
    private TextView my_teamwork ;
    private TextView my_collect ;
    private TextView service_register ;

    private static String root ;

    private TextView my_name ;
    private TextView textView8 ;
    private TextView textView9 ;

    private TextView niu_phone ;

    private String login ;
    private String number ;

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

    private TextView feedBack ;
    private TextView my_set ;


    private SharedPreferences loginCode ;

    private SharedPreferences sharedPreferences ;
    private SharedPreferences myNumber ;
    private SharedPreferences role ;


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getActivity().getSharedPreferences("isLogin", getActivity().MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        if (isLogin){
            //Log.e("benbn" , "================") ;
            //先拿到用户的缓存的头像,存在进行加载，不能重新操作。
            String path = SDUtil.getSDPath() + File.separator + "ziya"+ File.separator + "icon.png" ;
            File files = new File(path);
            if (files.exists()) {
                final byte[] icons = SDUtil.getDataFromSDCard("icon.png");
                bitmap = BitmapFactory.decodeByteArray(icons, 0, icons.length);
                default_icon.setImageBitmap(bitmap);
                niu_icon.setImageBitmap(bitmap);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sp = getActivity().getSharedPreferences("isLogin", getActivity().MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);

        //实例化组件
        initView(view) ;


        if (isLogin){


            SharedPreferences loginCode = getActivity().getSharedPreferences("loginCode", getActivity().MODE_PRIVATE);
            login = loginCode.getString("loginCode", null);

            final SharedPreferences role = getActivity().getSharedPreferences("role", getActivity().MODE_PRIVATE);
            root = role.getString("role", null);

            final SharedPreferences myNumber = getActivity().getSharedPreferences("myNumber", getActivity().MODE_PRIVATE);
            String number_niu = myNumber.getString("myNumber", null);
            niu_phone.setText(number_niu);


            if (!TextUtils.isEmpty(root)){
                switch (root){
                    case "0" :
                        my_rush.setVisibility(View.GONE);
                        me_change_icon.setVisibility(View.GONE);
                        niu_relative.setVisibility(View.VISIBLE);
                        break;
                    case "1" :
                        my_rush.setVisibility(View.VISIBLE);
                        niu_relative.setVisibility(View.GONE);
                        me_change_icon.setVisibility(View.VISIBLE);
                        break;
                    case "2" :
                        my_rush.setVisibility(View.GONE);
                        niu_relative.setVisibility(View.GONE);
                        me_change_icon.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            //先拿到用户的缓存的头像,存在进行加载，不能重新操作。
            String path = SDUtil.getSDPath() + File.separator + "ziya"+ File.separator + "icon.png" ;
            File files = new File(path);
            if (files.exists()) {
                final byte[] icons = SDUtil.getDataFromSDCard("icon.png");
                bitmap = BitmapFactory.decodeByteArray(icons, 0, icons.length);
                default_icon.setImageBitmap(bitmap);
                niu_icon.setImageBitmap(bitmap);
            }

            //加载数据
            loadData() ;

            //更改头像的事件监听
            me_change_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity() , PersonalInformationActivity.class ) ;
                    intent.putExtra("phoneNumber" , phoneNumber ) ;
                    startActivity(intent);
                }
            });
            niu_relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , PersonalInformationActivity.class ) ;
                    intent.putExtra("phoneNumber" , phoneNumber ) ;
                    startActivity(intent);
                }
            });
            //我的发布的页面
            my_release.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , MyReleaseActivity.class ) ;
                    startActivity(intent);
                }
            });
            //我的合作页面
            my_teamwork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , MyTeamWorkActivity.class ) ;
                    startActivity(intent);
                }
            });
            //我的收藏的页面
            my_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , MyCollectActivity.class ) ;
                    startActivity(intent);
                }
            });
            //我的抢单的页面
            my_rush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity()  , MyRushActivity.class ) ;
                    startActivity(intent);
                }
            });
            //服务方认证
            service_register.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity()  , ServiceRegisterActivity.class ) ;
                    intent.putExtra("root" , root ) ;
                    switch (root){
                        case "1" :
                        case "2" :
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
            });
            //帮助反馈页面
            feedBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , FeedBackActivity.class ) ;
                    startActivity(intent);
                }
            });


        }else {
            //ToastUtils.shortToast(getActivity() , "还未登录");

            //我的页面
            niu_relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                    startActivity(intent);
                }
            });
            me_change_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                    startActivity(intent);
                }
            });

            my_release.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                    startActivity(intent);
                }
            });
            my_teamwork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                    startActivity(intent);
                }
            });
            my_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                    startActivity(intent);
                }
            });
            service_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                    startActivity(intent);
                }
            });
            my_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                    startActivity(intent);
                }
            });
            feedBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , LoginActivity.class ) ;
                    startActivity(intent);
                }
            });

        }

        //设置页面
        my_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , MySetActivity.class ) ;
                startActivity(intent);
            }
        });
    }

    private void loadData() {


        String urls = String.format(Url.Myicon, login) ;
        Log.e("benbne", login);
        HttpUtils utils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("niuniu", responseInfo.result);

                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);


                    String role1 = jsonObject.getString("role");

                    Log.e("benben", role1);

                    final SharedPreferences role = getActivity().getSharedPreferences("role", getActivity().MODE_PRIVATE);
                    role.edit().putString("role", role1).commit();

                    /********************************************重新加载头像****************************************************/

                    JSONObject user007 = jsonObject.getJSONObject("user");
                    String userid007 = user007.getString("userid");

                    loadRongIcon(userid007);
                    /********************************************重新加载头像****************************************************/

                    switch (role1) {
                        case "0":
                            //作为一个发布方进来的view
                            JSONObject user = jsonObject.getJSONObject("user");
                            String userid = user.getString("userid");
                            String username = user.getString("username");
                            phoneNumber = user.getString("phonenumber");
                            final String UserPicture = user.getString("UserPicture");

                            me_change_icon.setVisibility(View.GONE);
                            niu_relative.setVisibility(View.VISIBLE);

                            niu_phone.setText(phoneNumber);
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
                            break;

                        case "1":
                            my_rush.setVisibility(View.VISIBLE);
                            me_change_icon.setVisibility(View.VISIBLE);
                            niu_relative.setVisibility(View.GONE);

                            JSONObject object = new JSONObject(responseInfo.result);
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
                            String UserID = service.getString("UserID");

                            JSONObject user1 = jsonObject.getJSONObject("user");
                            final String userPicture = user1.getString("UserPicture");
                            phoneNumber = user1.getString("phonenumber");

                            niu_relative.setVisibility(View.GONE);
                            me_change_icon.setVisibility(View.VISIBLE);

                            //更改头像
                            new LoadImageAsyncTask(new LoadImageAsyncTask.CallBack() {
                                @Override
                                public void setData(final Bitmap bitmap) {
//                                    //为拼图游戏的原图预览设置监听事件
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
                            break;
                        case "2":
                            my_rush.setVisibility(View.GONE);
                            me_change_icon.setVisibility(View.VISIBLE);
                            niu_relative.setVisibility(View.GONE);

                            JSONObject object01 = new JSONObject(responseInfo.result);
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
                            String UserID01 = service01.getString("UserID");

                            JSONObject user101 = jsonObject.getJSONObject("user");
                            final String userPicture01 = user101.getString("UserPicture");
                            phoneNumber = user101.getString("phonenumber");

                            niu_relative.setVisibility(View.GONE);
                            me_change_icon.setVisibility(View.VISIBLE);

                            //更改头像
                            new LoadImageAsyncTask(new LoadImageAsyncTask.CallBack() {
                                @Override
                                public void setData(final Bitmap bitmap) {
//                                    //为拼图游戏的原图预览设置监听事件
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
                            break;
                        default:
                            break;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(getActivity(), "网络连接异常");
            }
        }) ;

    }

    private void loadRongIcon(final String userid007 ) {

        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.send(HttpRequest.HttpMethod.POST, String.format(Url.RongIcon, userid007),params ,  new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben" , responseInfo.result ) ;
                try {

                    String a = "" ;
                    JSONObject jsonObject = new JSONObject(responseInfo.result) ;
                    JSONObject data = jsonObject.getJSONObject("data");
                    String userPicture = data.getString("UserPicture");
                    String serviceName = data.getString("ServiceName");
                    String role = data.getString("role");
                    switch (role){
                        case "0" :
                        case "2" :
                            String phonenumber = data.getString("phonenumber");
                            String substring = phonenumber.substring(0, 3);
                            String substring1 = phonenumber.substring(7, 11);
                            a = substring + "****" + substring1  ;
                            break;
                        case "1" :
                            a = serviceName ;
                            break;
                        default:
                            break;
                    }
                    Uri uir =Uri.parse ( Url.FileIP + userPicture);
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo( userid007 ,  a  , uir ));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        }) ;

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
        release_head = (TextView) v.findViewById(R.id.release_head ) ;
        service_register = (TextView) v.findViewById(R.id.service_register ) ;
        my_rush = (TextView) v.findViewById(R.id.my_rush ) ;
        my_release = (TextView) v.findViewById(R.id.my_release ) ;
        my_teamwork = (TextView) v.findViewById(R.id.my_teamwork ) ;
        my_collect = (TextView) v.findViewById(R.id.my_collect ) ;
        default_icon = (MyIconImageView)v.findViewById(R.id.default_icon ) ;

        niu_icon = (MyIconImageView)v.findViewById(R.id.niu_icon ) ;

        my_name = (TextView)v.findViewById(R.id.my_name) ;
        textView8 = (TextView)v.findViewById(R.id.textView8) ;
        textView9 = (TextView)v.findViewById(R.id.textView9) ;

        niu_phone = (TextView)v.findViewById(R.id.niu_phone) ;
        feedBack = (TextView)v.findViewById(R.id.feedBack) ;

        niu_relative = (RelativeLayout)v.findViewById(R.id.niu_relative ) ;

        my_set = (TextView)v.findViewById(R.id.my_set ) ;

    }

}
