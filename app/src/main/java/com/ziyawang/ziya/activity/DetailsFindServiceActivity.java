package com.ziyawang.ziya.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.MyImageView;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.rong.imkit.RongIM;

/**
 * 服务的详情页面
 */
public class DetailsFindServiceActivity extends BenBenActivity implements View.OnClickListener {
    //缓存到本地的用户的电话号
    private static String spphoneNumber;
    private static String ConnectPhone;
    private static String ConnectPerson;
    //缓存到本地的用户的ticket
    private static String login;
    //缓存到本地的用户的userID
    private static String spUserId ;
    //缓存到本地的用户的否登陆
    private static boolean isLogin;
    //缓存到本地的用户的级别
    private static String root;
    //整个View
    private ScrollView service_scroll;
    //返回按钮
    private RelativeLayout pre;
    private ImageView service_icon;
    //服务的类型
    private TextView service_type;
    //服务的编号
    private TextView service_no;
    //服务的收藏
    private TextView service_collect;
    //服务的分享Hom
    private TextView service_share;
    //服务方的名称
    private TextView service_details_two;
    //服务方的所在地
    private TextView service_details_four;
    //服务放的简介
    private TextView service_text_des;
    //服务地区
    private TextView service_for_part;
    //服务类型
    private TextView service_des_type;
    //相关凭证1
    private MyImageView service_img_one;
    //相关凭证2
    private MyImageView service_img_two;
    //相关凭证3
    private MyImageView service_img_three;
    //拨打电话按钮
    private RelativeLayout service_call;
    //私聊按钮
    private RelativeLayout service_sendMessage;
    //我的抢单按钮
    private LinearLayout search_person;
    //悬停布局
    private LinearLayout linearLayout;
    //服务的id
    private static String id;
    //服务方的名称
    private String ServiceName;
    //数据加载的dialog
    private MyProgressDialog dialog;
    //数据解析后用到的数据
    //详情真实可用的图片个数
    private static String pic_num;
    private static String ServiceIntroduction;
    private static String ServiceLocation;
    private static String ServiceType;
    private static String ServiceArea;
    private static String UserPicture;
    private static String ServiceNumber;
    private static String CollectFlag;
    private static String ConfirmationP1;
    private static String ConfirmationP2;
    private static String ConfirmationP3;
    private static String UserID;
    private static String ViewCount;
    private static String time;
    private static String insider;
    private static String Founds;
    private static String Size;
    private TextView service_size;
    private TextView service_money;
    private TextView service_person;
    private float DownX = 0;
    private ImageView image_01 ;
    private ImageView image_02 ;
    private ImageView image_03 ;
    private ImageView image_04 ;
    private ImageView image_05 ;
    private ImageView image_06 ;
    private ImageView image_07 ;
    private ImageView image_08 ;
    private ImageView image_09 ;
    private ImageView image_10 ;
    private TextView ben ;
    //查看详情
    private TextView details_search ;
    //头像的popupwindow
    private PopupWindow popupWindow ;
    //举报按钮
    private TextView report ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载数据
        loadData(id);

        service_scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        DownX = event.getX();//float DownX
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        float moveX = DownX - event.getX();//x轴距离
                        Log.e("benben", "" + moveX);
                        if (moveX < -250) {
                            finish();
                        }
                        break;
                    }
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_details_find_service);
    }

    @Override
    public void initViews() {
        service_scroll = (ScrollView) findViewById(R.id.service_scroll);
        pre = (RelativeLayout) findViewById(R.id.pre);
        service_icon = (ImageView) findViewById(R.id.service_icon);
        service_type = (TextView) findViewById(R.id.service_type);
        service_no = (TextView) findViewById(R.id.service_no);
        service_collect = (TextView) findViewById(R.id.service_collect);
        service_share = (TextView) findViewById(R.id.service_share);
        service_details_two = (TextView) findViewById(R.id.service_details_two);
        service_details_four = (TextView) findViewById(R.id.service_details_four);
        service_text_des = (TextView) findViewById(R.id.service_text_des);
        service_for_part = (TextView) findViewById(R.id.service_for_part);
        service_des_type = (TextView) findViewById(R.id.service_des_type);
        service_img_one = (MyImageView) findViewById(R.id.service_img_one);
        service_img_two = (MyImageView) findViewById(R.id.service_img_two);
        service_img_three = (MyImageView) findViewById(R.id.service_img_three);
        service_call = (RelativeLayout) findViewById(R.id.service_call);
        service_sendMessage = (RelativeLayout) findViewById(R.id.service_sendMessage);
        search_person = (LinearLayout) findViewById(R.id.search_person);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        report = (TextView) findViewById(R.id.report);
        service_size = (TextView) findViewById(R.id.service_size);
        service_money = (TextView) findViewById(R.id.service_money);
        service_person = (TextView) findViewById(R.id.service_person);
        image_01 = (ImageView) findViewById(R.id.image_01);
        image_02 = (ImageView) findViewById(R.id.image_02);
        image_03 = (ImageView) findViewById(R.id.image_03);
        image_04 = (ImageView) findViewById(R.id.image_04);
        image_05 = (ImageView) findViewById(R.id.image_05);
        image_06 = (ImageView) findViewById(R.id.image_06);
        image_07 = (ImageView) findViewById(R.id.image_07);
        image_08 = (ImageView) findViewById(R.id.image_08);
        image_09 = (ImageView) findViewById(R.id.image_09);
        image_10 = (ImageView) findViewById(R.id.image_10);
        ben = (TextView)findViewById(R.id.ben ) ;
        details_search = (TextView)findViewById(R.id.details_search ) ;
        details_search.setText(Html.fromHtml("<u>" + "查看详情" + "</u>"));
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        service_call.setOnClickListener(this);
        service_share.setOnClickListener(this);
        service_collect.setOnClickListener(this);
        service_sendMessage.setOnClickListener(this);
        service_icon.setOnClickListener(this);
        report.setOnClickListener(this);
        details_search.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //获得用户的ticket
        login = GetBenSharedPreferences.getTicket(this);
        //获得用户的spphoneNumber
        spphoneNumber = GetBenSharedPreferences.getSpphoneNumber(this);
        //获得用户的root
        root = GetBenSharedPreferences.getRole(this);
        //获得用户的isLogin
        isLogin = GetBenSharedPreferences.getIsLogin(this);
        //获取用户的spUserId
        spUserId = GetBenSharedPreferences.getUserId(this ) ;
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
    }

    private void loadData(final String id) {
        //展示数据加载动画
        showBenDialog();
        //开启网络请求
        String urls = String.format(Url.Details_service, id, login);
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("access_token", "token");
        utils.configCurrentHttpCacheExpiry(1000);
        utils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result);
                //dialog消失
                hiddenBenDialog();
                //成功接口的回调
                dealResult(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //打印错误信息
                error.printStackTrace();
                //dialog消失
                hiddenBenDialog();
            }
        });

    }

    private void dealResult(String result) {
        //加载数据之后，设置为可见。
        service_scroll.setVisibility(View.VISIBLE);
        //数据解析
        try {
            JSONObject object = new JSONObject(result);
            ConnectPhone = object.getString("ConnectPhone");
            ConnectPerson = object.getString("ConnectPerson");
            ServiceName = object.getString("ServiceName");
            ServiceIntroduction = object.getString("ServiceIntroduction");
            ServiceLocation = object.getString("ServiceLocation");
            ServiceType = object.getString("ServiceType");
            ServiceArea = object.getString("ServiceArea");
            UserPicture = object.getString("UserPicture");
            ServiceNumber = object.getString("ServiceNumber");
            CollectFlag = object.getString("CollectFlag");
            ConfirmationP1 = object.getString("ConfirmationP1");
            ConfirmationP2 = object.getString("ConfirmationP2");
            ConfirmationP3 = object.getString("ConfirmationP3");
            UserID = object.getString("UserID");
            ViewCount = object.getString("ViewCount");
            time = object.getString("created_at");
            insider = object.getString("insider");
            Size = object.getString("Size");
            Founds = object.getString("Founds");
            String right = object.getString("right");
            String level = object.getString("Level");
            //加载会员类型
            loadRight(right ) ;
            //加载认证的类型
            loadLevel(level) ;
            //根据获得的数据进行页面的显示
            showData() ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadLevel(String level) {
        if (!TextUtils.isEmpty(level) ){
            String[] split = level.split(",");
            for (int i = 0; i < split.length; i++) {
                if ("1".equals(split[i].toString())){
                    image_01.setImageResource(R.mipmap.v203_0101);
                }
                if ("2".equals(split[i].toString())){
                    image_02.setImageResource(R.mipmap.v203_0102);
                }
                if ("3".equals(split[i].toString()) ){
                    image_03.setImageResource(R.mipmap.v203_0103);
                }
                if ("4".equals(split[i].toString()) ){
                    image_04.setImageResource(R.mipmap.v203_0104);
                }
                if ("5".equals(split[i].toString())){
                    image_05.setImageResource(R.mipmap.v203_0105);
                }
            }
        }
    }

    private void loadRight(String right) {
        if (!TextUtils.isEmpty(right) ){
            String[] split = right.split(",");
            for (int i = 0; i < split.length; i++) {
                if ("1".equals(split[i].toString())){
                    image_06.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("18".equals(split[i].toString())){
                    image_07.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("12".equals(split[i].toString()) ){
                    image_08.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("6".equals(split[i].toString()) ){
                    image_09.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("19".equals(split[i].toString())){
                    image_10.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
            }
        }
    }

    private void showData() {
        if ("0".equals(Size)){
            service_size.setText("未填写");
        }else {
            service_size.setText(Size);
        }
        if ("0".equals(Founds)){
            service_money.setText("未填写");
        }else {
            service_money.setText(Founds);
        }
//        if ("0000-00-00 00:00:00".equals(Regtime)){
//            text_time.setText("");
//        }else {
//            text_time.setText(Regtime);
//        }
        service_person.setText(ConnectPerson);
        //根据数据请求过来的collectFlag来正确显示是否是收藏
        showCollectStatus(CollectFlag) ;
        //根据数据请求过来的userPicture来正确显示头像
        showUserPicture(UserPicture);
        //根据数据请求过来的projectNumber来正确显示编号
        showNo(ServiceNumber);
        //得到图片的个数
        getPicNum(ConfirmationP1, ConfirmationP2, ConfirmationP3);
        //根据数据请求过来的wordDes来正确显示信息描述
        showWordDes(ServiceIntroduction);
        //根据数据请求过来的pictureDes1来正确显示第一张图片
        showPictureDes1(ConfirmationP1);
        //根据数据请求过来的pictureDes2来正确显示第二张图片
        showPictureDes2(ConfirmationP2);
        //根据数据请求过来的pictureDes3来正确显示第三张图片
        showPictureDes3(ConfirmationP3);
        //服务地区的描述
        service_for_part.setText(ServiceArea);
        //服务类型的描述
        service_des_type.setText(ServiceType);
        //公司名称
        service_details_two.setText(ServiceName
                //+ "___(" + ConnectPerson + ")"
        );
        //服务方所在地
        service_details_four.setText(ServiceLocation);
        //服务方的浏览次数
        service_no.setText(time +"  浏览：" + ViewCount   );
        //根据数据判断，已经登陆状态下sp不空
        if (judgeNotNull()){
            //是自己发布显示抢单人页面,否则不显示
            if (judgeMyself()){
                showMyselfView() ;
                report.setVisibility(View.GONE);
            }else {
                linearLayout.setVisibility(View.VISIBLE);
                search_person.setVisibility(View.GONE);
                report.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showMyselfView() {
        //点击进入我的抢单列表
        linearLayout.setVisibility(View.GONE);
        search_person.setVisibility(View.VISIBLE);
        search_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailsFindServiceActivity.this, MyRushActivity.class);
                startActivity(intent1);
            }
        });
    }

    private boolean judgeMyself() {
        if (spUserId.equals(UserID)){
            return true ;
        }else {
            return false ;
        }
    }

    private boolean judgeNotNull() {
        if (!isLogin){
            return false ;
        }
        if (TextUtils.isEmpty(spUserId)){
            return false ;
        }else {
            return true;
        }

    }

    private void showNo(String serviceNumber) {
        service_type.setText("编号：" + serviceNumber);
    }

    private void getPicNum(String pictureDes1, String pictureDes2, String pictureDes3) {
        if (!TextUtils.isEmpty(pictureDes3)){
            pic_num = "3" ;
        }else {
            if (!TextUtils.isEmpty(pictureDes2)){
                pic_num = "2" ;
            }else {
                pic_num = "1" ;
            }

        }

    }

    private void showPictureDes3(final String confirmationP3) {
        BitmapUtils bitmapUtils3 = new BitmapUtils(DetailsFindServiceActivity.this);
        bitmapUtils3.display(service_img_three, Url.FileIP + confirmationP3);
        service_img_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(confirmationP3)) {
                    subData("3") ;
                }
            }
        });
    }

    private void showPictureDes2(final String confirmationP2) {
        BitmapUtils bitmapUtils2 = new BitmapUtils(DetailsFindServiceActivity.this);
        bitmapUtils2.display(service_img_two, Url.FileIP + confirmationP2);
        service_img_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(confirmationP2)) {
                    subData("2") ;
                }
            }
        });
    }

    private void showPictureDes1(final String confirmationP1) {
        BitmapUtils bitmapUtils1 = new BitmapUtils(DetailsFindServiceActivity.this);
        bitmapUtils1.display(service_img_one, Url.FileIP + confirmationP1);
        service_img_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(confirmationP1)) {
                    subData("1");
                }
            }
        });
    }

    private void subData(String index) {
        Intent intent = new Intent(DetailsFindServiceActivity.this, ShowImageViewActivity.class);
        intent.putExtra("count" ,index) ;
        intent.putExtra("pic_number", pic_num) ;
        if (Integer.parseInt(pic_num) == 3){
            intent.putExtra("pic1" ,Url.FileIP + ConfirmationP1 ) ;
            intent.putExtra("pic2" ,Url.FileIP + ConfirmationP2 ) ;
            intent.putExtra("pic3" ,Url.FileIP + ConfirmationP3 ) ;
        }else if (Integer.parseInt(pic_num) == 2){
            intent.putExtra("pic1" ,Url.FileIP + ConfirmationP1 ) ;
            intent.putExtra("pic2" ,Url.FileIP + ConfirmationP2 ) ;
        }else if (Integer.parseInt(pic_num) == 1){
            intent.putExtra("pic1" ,Url.FileIP + ConfirmationP1 ) ;
        }
        startActivity(intent);
    }

    private void showWordDes(String serviceIntroduction) {
        service_text_des.setText(serviceIntroduction);
    }

    private void showUserPicture(String userPicture) {
        //设置头像
        BitmapUtils bitmapUtils = new BitmapUtils(DetailsFindServiceActivity.this);
        bitmapUtils.display(service_icon, Url.FileIP + userPicture);
    }

    private void showCollectStatus(String collectFlag) {
        switch (collectFlag) {
            case "0":
                unCollect() ;
                break;
            case "1":
                collect() ;
                break;
            default:
                break;
        }
    }

    private void collect() {
        Drawable drawable02 = getResources().getDrawable(R.mipmap.collect);
        drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
        service_collect.setCompoundDrawables(null, drawable02, null, null);
    }

    private void unCollect() {
        Drawable drawable01 = getResources().getDrawable(R.mipmap.collect_un);
        drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
        service_collect.setCompoundDrawables(null, drawable01, null, null);
    }

    public void onResume() {
        //获得用户的ticket
        login = GetBenSharedPreferences.getTicket(this);
        //获得用户的spphoneNumber
        spphoneNumber = GetBenSharedPreferences.getSpphoneNumber(this) ;
        //获得用户的root
        root = GetBenSharedPreferences.getRole(this) ;
        //获取用户的userId
        spUserId = GetBenSharedPreferences.getUserId(this) ;
        //获得用户的isLogin
        isLogin = GetBenSharedPreferences.getIsLogin(this) ;
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("找服务详情页面");
        //统计时长
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("找服务详情页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle(ServiceName);
        oks.setTitleUrl(Url.ShareService + id);
        oks.setImageUrl("http://images.ziyawang.com/Applogo/logo.png");
        oks.setText(ServiceIntroduction);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url.ShareService + id);
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre:
                finish();
                break;
            //拨打电话按钮
            case R.id.service_call :
                goCall() ;
                break;
            //分享按钮的监听事件
            case R.id.service_share :
                showShare();
                break;
            //收藏按钮的监听事件
            case R.id.service_collect :
                goCollect();
                break;
            //私聊按钮的监听事件
            case R.id.service_sendMessage :
                goSendMessage() ;
                break;
            //头像按钮的监听事件
            case R.id.service_icon :
                goShowIcon() ;
                break;
            //举报的监听事件
            case R.id.report :
                goReportActivity() ;
                break;
            case R.id.details_search :
                //Intent intent = new Intent()
                goDetailsStarRegisterActivity() ;
                break;
            default:
                break;
        }
    }

    private void goDetailsStarRegisterActivity() {
        Intent intent = new Intent(DetailsFindServiceActivity.this , DetailsStarRegisterActivity.class ) ;
        intent.putExtra("id" , id ) ;
        startActivity(intent);
    }

    private void goReportActivity() {
        if (GetBenSharedPreferences.getIsLogin(this)){
            Intent intent = new Intent(DetailsFindServiceActivity.this , ReportActivity.class ) ;
            intent.putExtra("type" , "service") ;
            intent.putExtra("id" , id ) ;
            startActivity(intent);
        }else {
            Intent intent = new Intent(DetailsFindServiceActivity.this , LoginActivity.class ) ;
            startActivity(intent ) ;
        }

    }


    private void goShowIcon() {

        //获取布局加载器对象
        View contentView = getLayoutInflater().inflate(R.layout.popupwindow_show_icon, null);
        //实例化组件
        ImageView imageView = (ImageView) contentView.findViewById(R.id.popupwindow_image);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        service_icon.setDrawingCacheEnabled(true);
        Bitmap bm = service_icon.getDrawingCache();
        imageView.setImageBitmap(bm);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        //创建一个PopupWindow放入到容器内
        popupWindow = new PopupWindow(contentView, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getWidth() );
        //对PopupWindow的弹出窗进行设置
        popupWindow.setAnimationStyle(R.style.animation_01);
        //取得焦点
        popupWindow.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //弹出位置
        popupWindow.showAsDropDown(service_icon, 0, 0);
        //popupWindow.showAtLocation( mGameView, Gravity.CENTER, 0, 20);

        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private void goSendMessage() {
        if (isLogin) {
            //启动会话界面
            if (RongIM.getInstance() != null) RongIM.getInstance().startPrivateChat(DetailsFindServiceActivity.this, UserID, "聊天详情");
        } else {
            //未登录，跳转到登陆页面
            goLoginActivity();
        }
    }

    private void goLoginActivity() {
        Intent intent1 = new Intent(DetailsFindServiceActivity.this, LoginActivity.class);
        startActivity(intent1);
    }

    private void goCollect() {
        if (isLogin) {
            //收藏功能接口的调用
            goLoadCollect() ;
        } else {
            goLoginActivity();
        }
    }

    private void goLoadCollect() {
        HttpUtils utils = new HttpUtils();
        RequestParams params1 = new RequestParams();
        params1.addBodyParameter("itemID", id);
        params1.addBodyParameter("type", "4");
        String a = String.format(Url.Collect, login);
        utils.send(HttpRequest.HttpMethod.POST, a, params1, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benbne", responseInfo.result);
                //成功回调
                dealCollectResult(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(DetailsFindServiceActivity.this, "网络连接异常");
            }
        });
    }

    private void dealCollectResult(String result) {
        try {
            JSONObject object = new JSONObject(result);
            String msg = object.getString("msg");
            switch (msg) {
                case "取消收藏成功！":
                    Toast.makeText(DetailsFindServiceActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    unCollect();
                    break;
                case "收藏成功！":
                    Toast.makeText(DetailsFindServiceActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    collect();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void goCall() {
        if (isLogin) {
            //显示是否拨打电话的登陆按钮
            showCustomDialog() ;
        } else {
            //未登录，跳转到登陆页面
            goLoginActivity();
        }
    }

    private void showCustomDialog() {
        if ("1".equals(insider)){
            //计数
            sendAccount() ;
            //开启拨打电话的dialog
            final CustomDialog.Builder builder01 = new CustomDialog.Builder(DetailsFindServiceActivity.this);
            builder01.setTitle("亲爱的用户");
            builder01.setMessage("您确定要联系  " + ConnectPerson + ":" + ConnectPhone + "?");
            builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {goCallNumber() ;
                }
            });
            builder01.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder01.create().show();
        }else {
            ToastUtils.shortToast(DetailsFindServiceActivity.this , "该服务方未办理会员，无法查看其联系方式");
        }

    }

    private void sendAccount() {
        String urls = String.format(Url.ServiceAccount, GetBenSharedPreferences.getTicket(this)) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("ServiceID" , id ) ;
        params.addBodyParameter("Channel" , "ANDROID" ) ;
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }
              @Override
            public void onFailure(HttpException error, String msg) {

            }

        }) ;
    }

    private void goCallNumber() {
        String str = "tel:" + ConnectPhone;
        //直接拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
        if (ActivityCompat.checkSelfPermission(DetailsFindServiceActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast(DetailsFindServiceActivity.this, "请在管理中心，给予直接拨打电话权限。");
            return;
        }
        startActivity(intent);
    }

    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(DetailsFindServiceActivity.this, "数据加载中，请稍后。。。");
        // 不可以用“返回键”取消
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 隐藏数据加载框
     */
    private void hiddenBenDialog() {
        //关闭dialog
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
