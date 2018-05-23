package com.ziyawang.ziya.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.MyTimeFormat;
import com.ziyawang.ziya.tools.Player;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.FlowLayout;
import com.ziyawang.ziya.view.JustifyTextView;
import com.ziyawang.ziya.view.MyImageView;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Set;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.rong.imkit.RongIM;

public class V2DetailsFindInfoActivity extends BenBenActivity implements View.OnClickListener {
    //滑动布局
    private ScrollView info_scroll;
    //定义播放器
    private Player player;
    //回退按钮
    private RelativeLayout pre ;
    //信息的head
    private TextView info_head;
    //信息的收藏的区域
    private RelativeLayout relative_collect ;
    //信息的收藏
    private ImageView info_collect;
    //信息的分享的区域
    private RelativeLayout relative_share ;
    //信息的分享
    private ImageView info_share;
    //信息的title
    private JustifyTextView info_title ;
    //详情的头像
    private ImageView info_icon;
    //信息的昵称
    private TextView info_nickName ;
    //信息的编号
    private TextView info_no;
    //信息的观看次数
    private TextView info_time ;
    //是否是vip资源的显示
    private ImageView info_vip ;
    //举报按钮
    private RelativeLayout report ;
    //信息的审核时间
    private TextView info_time_new ;
    //次级信息
    private LinearLayout linear_one ;
    private LinearLayout linear_two ;
    private LinearLayout linear_three ;
    private LinearLayout linear_four ;
    private LinearLayout linear_five ;
    private LinearLayout linear_six ;
    private LinearLayout linear_seven ;
    private LinearLayout linear_eight ;
    private LinearLayout linear_nine ;
    private LinearLayout linear_nine_add ;
    private LinearLayout linear_ten ;
    private LinearLayout linear_ten_add ;

    private TextView one_left ;
    private TextView two_left ;
    private TextView three_left ;
    private TextView four_left ;
    private TextView five_left ;
    private ImageView five_img ;
    private ImageView six_img ;
    private ImageView seven_img ;
    private ImageView eight_img ;
    private TextView six_left ;
    private TextView seven_left ;
    private TextView eight_left ;
    private TextView nine_left ;
    private TextView nine_left_add ;
    private TextView ten_left ;
    private TextView ten_left_add ;

    private TextView one_right ;
    private TextView two_right ;
    private TextView three_right ;
    private TextView four_right ;
    private TextView five_right ;
    private TextView six_right ;
    private TextView seven_right ;
    private TextView eight_right ;
    private TextView nine_right ;
    private TextView nine_right_add ;
    private TextView ten_right ;
    private TextView ten_right_add ;
    

    private LinearLayout linear_01 ;
    private LinearLayout linear_02 ;
    private LinearLayout linear_03 ;

    private TextView text_01_left ;
    private TextView text_02_left ;
    private TextView text_03_left ;

    private TextView text_01_right ;
    private TextView text_02_right ;
    private TextView text_03_right ;

    private FlowLayout flowlayout ;

    //其他信息头布局
    private TextView second_head ;
    //项目亮点头布局
    private TextView proLabel_head ;
    //项目亮点
    private LinearLayout linear_proLabel ;

    //是否约谈过
    private TextView textView6 ;
    //是否存在语音
    private TextView voice_status ;
    //是否是委托发布的标识
    private ImageView info_image_publisher ;
    //title
    private String title ;
    private TextView info_details_one;
    private TextView info_details_two;
    private TextView info_details_three;
    private TextView info_details_four;
    private TextView info_details_five;
    private TextView info_details_six;
    private TextView info_details_seven;
    private TextView info_details_eight;
    private TextView info_details_nine;
    private TextView info_details_ten;
    //信息的语言描述
    private JustifyTextView info_text_des;
    //信息的声音描述
    private Button info_audio_des;
    //信息的声音描述的时间
    private TextView info_audio_des_duration;
    //信息的图片描述1
    private MyImageView info_img_one;
    //信息的图片描述2
    private MyImageView info_img_two;
    //信息的图片描述3
    private MyImageView info_img_three;
    //信息的清单下载
    private RelativeLayout info_upload;
    //拨打电话
    private RelativeLayout info_call;
    //申请抢单
    private RelativeLayout go;
    //私聊
    private RelativeLayout info_sendMessage;
    //上层信息展示栏位
    private LinearLayout niu;
    //下层信息展示栏位
    private LinearLayout niu_145;
    //信息展示栏位分割线
    private LinearLayout niu_niu;
    //查看抢单人按钮
    private LinearLayout search_person;
    //联系方式布局
    private LinearLayout linearLayout;
    //sp中存储的电话号码
    private static String spphoneNumber;
    //网络请求获取的电话号码
    private static String phoneNumber;
    //用户的ticket
    private static String login;
    //用户是否登录的判断状态
    private static boolean isLogin;
    //用户的权限等级
    private static String root;
    //当前登入用户的id
    private static String spUserId ;
    //详情的id
    private static String id;
    //详情真实可用的图片个数
    private static String pic_num;
    //详情的文字描述
    private String wordDes;
    //抢单状态的显示1、申请抢单2、已抢单
    private TextView textView7 ;
    //抢单的判断状态
    private String publishState ;

    //数据加载的dialog
    private MyProgressDialog dialog  ;
    //收费信息的整体组件
    private LinearLayout member_linear ;
    //收费信息的备注信息
    private TextView company_text_des ;
    //数据解析后用到的数据
    private static String proArea ;
    private static String ViewCount ;
    private static String publishTime ;
    private static String member ;
    private static String CooperateState ;
    private static String TypeID ;
    private static String nickName ;
    private static String phonenumber ;
    private static String Price ;
    private static String PayFlag ;
    private static String Account ;
    private static String typeName ;
    private static String totalMoney ;
    private static String assetType ;
    private static String transferMoney ;
    private static String userPicture ;
    private static String projectNumber ;
    private static String collectFlag ;
    private static String rushFlag ;
    private static String userID ;
    private static String corpore ;
    private static String voiceDes ;
    private static String pictureDes1 ;
    private static String pictureDes2 ;
    private static String pictureDes3 ;
    private static String time ;
    private static String view_count ;
    private static String Publisher ;
    private static String connectPerson ;
    private static String connectPhone ;
    private float DownX = 0;
    //作为发布方进入的页面
    private LinearLayout show_info_register ;
    //账户余额
    private TextView info_account ;
    //头像的popupwindow
    private PopupWindow popupWindow ;
    //用户是否承诺该条信息
    private LinearLayout linear_promise ;
    //项目详情头区域
    private TextView v216_PictureDet_head ;
    //项目详情
    private ImageView v216_PictureDet ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_v2_details_find_info);
    }

    @Override
    public void initViews() {
        linear_promise = (LinearLayout)findViewById(R.id.linear_promise ) ;
        info_head = (TextView)findViewById(R.id.info_head ) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        flowlayout = (FlowLayout)findViewById(R.id.flowlayout ) ;
        relative_share = (RelativeLayout)findViewById(R.id.relative_share ) ;
        relative_collect = (RelativeLayout)findViewById(R.id.relative_collect ) ;
        report = (RelativeLayout)findViewById(R.id.report ) ;
        info_share = (ImageView)findViewById(R.id.info_share ) ;
        info_collect = (ImageView)findViewById(R.id.info_collect ) ;
        info_title = (JustifyTextView)findViewById(R.id.info_title ) ;
        info_scroll = (ScrollView)findViewById(R.id.info_scroll ) ;
        info_icon = (ImageView)findViewById(R.id.info_icon ) ;
        info_nickName = (TextView)findViewById(R.id.info_nickName ) ;
        info_no = (TextView)findViewById(R.id.info_no ) ;
        info_time = (TextView)findViewById(R.id.info_view_count ) ;
        info_time_new = (TextView)findViewById(R.id.info_time_new ) ;
        info_vip = (ImageView)findViewById(R.id.info_vip ) ;

        one_left = (TextView)findViewById(R.id.one_left ) ;
        two_left = (TextView)findViewById(R.id.two_left ) ;
        three_left = (TextView)findViewById(R.id.three_left ) ;
        four_left = (TextView)findViewById(R.id.four_left ) ;
        five_left = (TextView)findViewById(R.id.five_left ) ;
        five_img = (ImageView)findViewById(R.id.five_img ) ;
        six_img = (ImageView)findViewById(R.id.six_img ) ;
        seven_img = (ImageView)findViewById(R.id.seven_img ) ;
        eight_img = (ImageView)findViewById(R.id.eight_img ) ;
        six_left = (TextView)findViewById(R.id.six_left ) ;
        seven_left = (TextView)findViewById(R.id.seven_left ) ;
        eight_left = (TextView)findViewById(R.id.eight_left ) ;
        nine_left = (TextView)findViewById(R.id.nine_left ) ;
        ten_left = (TextView)findViewById(R.id.ten_left ) ;

        one_right = (TextView)findViewById(R.id.one_right ) ;
        two_right = (TextView)findViewById(R.id.two_right ) ;
        three_right = (TextView)findViewById(R.id.three_right ) ;
        four_right = (TextView)findViewById(R.id.four_right ) ;
        five_right = (TextView)findViewById(R.id.five_right ) ;
        six_right = (TextView)findViewById(R.id.six_right ) ;
        seven_right = (TextView)findViewById(R.id.seven_right ) ;
        eight_right = (TextView)findViewById(R.id.eight_right ) ;
        nine_right = (TextView)findViewById(R.id.nine_right ) ;
        ten_right = (TextView)findViewById(R.id.ten_right ) ;
        nine_right_add = (TextView)findViewById(R.id.nine_right_add ) ;
        ten_right_add = (TextView)findViewById(R.id.ten_right_add ) ;
        linear_nine_add = (LinearLayout)findViewById(R.id.linear_nine_add )  ;
        linear_ten_add = (LinearLayout)findViewById(R.id.linear_ten_add )  ;

        linear_01 = (LinearLayout)findViewById(R.id.linear_01 ) ;
        linear_02 = (LinearLayout)findViewById(R.id.linear_02 ) ;
        linear_03 = (LinearLayout)findViewById(R.id.linear_03 ) ;

        text_01_left = (TextView)findViewById(R.id.text_01_left ) ;
        text_02_left = (TextView)findViewById(R.id.text_02_left ) ;
        text_03_left = (TextView)findViewById(R.id.text_03_left ) ;

        text_01_right = (TextView)findViewById(R.id.text_01_right ) ;
        text_02_right = (TextView)findViewById(R.id.text_02_right ) ;
        text_03_right = (TextView)findViewById(R.id.text_03_right ) ;
        second_head = (TextView)findViewById(R.id.second_head ) ;
        proLabel_head = (TextView)findViewById(R.id.proLabel_head ) ;
        linear_proLabel = (LinearLayout)findViewById(R.id.linear_proLabel ) ;
        info_text_des = (JustifyTextView)findViewById(R.id.info_text_des ) ;
        info_img_one = (MyImageView)findViewById(R.id.info_img_one ) ;
        info_img_two = (MyImageView)findViewById(R.id.info_img_two ) ;
        info_img_three = (MyImageView)findViewById(R.id.info_img_three ) ;

        info_audio_des_duration = (TextView)findViewById(R.id.info_audio_des_duration ) ;
        info_audio_des = (Button)findViewById(R.id.info_audio_des ) ;
        voice_status = (TextView)findViewById(R.id.voice_status ) ;
        player = new Player();
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout ) ;
        search_person = (LinearLayout)findViewById(R.id.search_person ) ;
        show_info_register = (LinearLayout)findViewById(R.id.show_info_register ) ;

        info_sendMessage = (RelativeLayout)findViewById(R.id.info_sendMessage ) ;
        info_call = (RelativeLayout)findViewById(R.id.info_call ) ;

        v216_PictureDet_head = (TextView)findViewById(R.id.v216_PictureDet_head ) ;
        v216_PictureDet = (ImageView) findViewById(R.id.v216_PictureDet ) ;

    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        relative_collect.setOnClickListener(this);
        relative_share.setOnClickListener(this);
        report.setOnClickListener(this);
        info_icon.setOnClickListener(this);
        info_audio_des.setOnClickListener(this);
        show_info_register.setOnClickListener(this);
        info_sendMessage.setOnClickListener(this);
        info_call.setOnClickListener(this);
    }

    @Override
    public void initData() {
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
        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        //loadData(id) ;
    }

    private void loadData(String id) {
        //显示数据加载框
        showBenDialog();
        //开启网络请求
        String urls = String.format(Url.Details_info, id, login);
        HttpUtils httpUtils = new HttpUtils();
        final RequestParams params = new RequestParams();
        params.addQueryStringParameter("access_token", "token");
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("V2Details_info", responseInfo.result);
                //隐藏dialog
                hiddenBenDialog();
                //处理json
                dealResult(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(V2DetailsFindInfoActivity.this, "网络连接失败");
                hiddenBenDialog();
            }
        });
    }

    private void dealResult(String result) {
        //数据加载成功后显示整个view
        info_scroll.setVisibility(View.VISIBLE);
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(result);
        title = object.getString("Title");
        member = object.getString("Member");
        CooperateState = object.getString("CooperateState");
        TypeID = object.getString("TypeID");
        userPicture = object.getString("UserPicture");
        nickName = object.getString("username");
        phonenumber = object.getString("phonenumber");
        projectNumber = object.getString("ProjectNumber");
        publishTime = object.getString("PublishTime");
        ViewCount = object.getString("ViewCount");
        wordDes = object.getString("WordDes");
        voiceDes = object.getString("VoiceDes");
        pictureDes1 = object.getString("PictureDes1");
        pictureDes2 = object.getString("PictureDes2");
        pictureDes3 = object.getString("PictureDes3");
        userID = object.getString("UserID");
        publishState = object.getString("PublishState");
        rushFlag = object.getString("RushFlag");
        connectPerson = object.getString("ConnectPerson");
        connectPhone = object.getString("ConnectPhone");
        String collectFlag = object.getString("CollectFlag");
        PayFlag = object.getString("PayFlag");
        String Promise = object.getString("Promise");
        if ("承诺".equals(Promise)){
            linear_promise.setVisibility(View.VISIBLE);
        }else {
            linear_promise.setVisibility(View.GONE);
        }
        switch (collectFlag){
            case "1" :
                info_collect.setImageResource(R.mipmap.v2shouc);
                break;
            default:
                info_collect.setImageResource(R.mipmap.v2shoucang);
                break;
        }
        showViews(object) ;
    }

    private void showViews(com.alibaba.fastjson.JSONObject object) {
        info_head.setText(object.getString("TypeName"));
        //根据数据请求过来的title来正确显示
        showTitle(title) ;
        //根据数据请求过来的Member来正确显示是否是VIP资源
        showVipStatus(member , CooperateState , TypeID ) ;
        //根据数据请求过来的userPicture来正确显示头像
        showUserPicture(userPicture) ;
        //根据数据请求过来的nickName和phonenumber来正确显示昵称
        showNickName(nickName, phonenumber) ;
        //根据数据请求过来的projectNumber来正确显示编号
        showProjectNumber(projectNumber) ;
        //根据数据请求过来的publishTime 和 ViewCount 来正确显示日期和次数
        showTimeCount(publishTime, ViewCount) ;
        //根据请求数据来正确显示次级信息
        showSecondViews(object) ;
        //根据请求数据来正确显示项目亮点
        showProLabel(object) ;
        //根据数据请求过来的wordDes来正确显示信息描述
        showWordDes(wordDes) ;
        //音频文件不存在，将文字置为空。
        if (TextUtils.isEmpty(voiceDes) && voiceDes.equals("")){
            info_audio_des.setVisibility(View.GONE);
            voice_status.setVisibility(View.VISIBLE);
        }
        //根据请求数据来正确显示项目详情
        showPictureDet(object) ;
        //得到图片的个数
        getPicNum(pictureDes2, pictureDes3);
        //根据数据请求过来的pictureDes1来正确显示第一张图片
        showPictureDes1(pictureDes1);
        //根据数据请求过来的pictureDes2来正确显示第二张图片
        showPictureDes2(pictureDes2);
        //根据数据请求过来的pictureDes3来正确显示第三张图片
        showPictureDes3(pictureDes3) ;
        //根据数据判断，已经登陆状态下sp不空
        if (judgeNotNull()){
            //是自己发布显示抢单人页面,否则不显示
            if (judgeMyself()){
                Log.e("judgeMyself" , "自己发布的信息") ;
                showMyselfView() ;
                report.setVisibility(View.GONE);
            }else {
                showNotMyselfVIew() ;
                report.setVisibility(View.VISIBLE);
            }
        }else {
            linearLayout.setVisibility(View.VISIBLE);
        }

    }

    private void showPictureDet(com.alibaba.fastjson.JSONObject object) {
        final String pictureDet = object.getString("PictureDet");
        if (!TextUtils.isEmpty(pictureDet)){
            BitmapUtils bitmapUtils1 = new BitmapUtils(V2DetailsFindInfoActivity.this);
            bitmapUtils1.configDefaultLoadFailedImage(R.mipmap.error_imgs);
            bitmapUtils1.display(v216_PictureDet, Url.FileIP + pictureDet );
            v216_PictureDet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(V2DetailsFindInfoActivity.this, Image216Activity.class);
                    intent.putExtra("count" ,"1") ;
                    intent.putExtra("pic_number", "1") ;
                    intent.putExtra("pic1" ,Url.FileIP + pictureDet ) ;
                    startActivity(intent);
                }
            });
        }
    }

    private void showNotMyselfVIew() {
        switch (root) {
            case "0":
            case "2":
                /**
                 * 201712041346 修改
                 */
//                //不是自己发出的信息，身份为不是服务方
//                showRootIsZeroTwo() ;
//                break;
            case "1":
                //不是自己发出的信息，身份是服务方
                showRootIsZeroOne() ;
                break;
            default:
                break;
        }
    }

    private void showRootIsZeroOne() {
        if ("1".equals(CooperateState) || "2".equals(CooperateState)){
            show_info_register.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }else {
            show_info_register.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            search_person.setVisibility(View.GONE);
            info_call.setVisibility(View.VISIBLE);
            info_sendMessage.setVisibility(View.VISIBLE);
        }
    }

    private void showRootIsZeroTwo() {
        if ("1".equals(CooperateState) || "2".equals(CooperateState)){
            show_info_register.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }else {
            //电话私聊抢单人收藏消失
            search_person.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            info_call.setVisibility(View.GONE);
            info_sendMessage.setVisibility(View.GONE);
            show_info_register.setVisibility(View.VISIBLE);
        }

    }

    private void showMyselfView() {
        linearLayout.setVisibility(View.GONE);
        search_person.setVisibility(View.VISIBLE);
        show_info_register.setVisibility(View.GONE);
        search_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(V2DetailsFindInfoActivity.this, RushPersonActivity.class);
                intent1.putExtra("id", id);
                intent1.putExtra("status", publishState);
                startActivity(intent1);
            }
        });
    }

    private boolean judgeMyself() {
        if (spUserId.equals(userID)){
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
        }
        return true;
    }

    private void showPictureDes3(final String pictureDes3) {
        if (!TextUtils.isEmpty(pictureDes3)){
            BitmapUtils bitmapUtils3 = new BitmapUtils(V2DetailsFindInfoActivity.this);
            bitmapUtils3.configDefaultLoadFailedImage(R.mipmap.error_imgs);
            bitmapUtils3.display(info_img_three, Url.FileIP + pictureDes3);
            info_img_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(pictureDes3)) {
                        subData("3");
                    }
                }
            });
        }
    }

    private void showPictureDes2(final String pictureDes2) {
        if (!TextUtils.isEmpty(pictureDes2)){
            BitmapUtils bitmapUtils2 = new BitmapUtils(V2DetailsFindInfoActivity.this);
            bitmapUtils2.configDefaultLoadFailedImage(R.mipmap.error_imgs);
            bitmapUtils2.display(info_img_two, Url.FileIP + pictureDes2);
            info_img_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(pictureDes2)) {
                        subData("2");
                    }

                }
            });
        }
    }

    private void showPictureDes1(final String pictureDes1) {
        if (!TextUtils.isEmpty(pictureDes1)){
            BitmapUtils bitmapUtils1 = new BitmapUtils(V2DetailsFindInfoActivity.this);
            bitmapUtils1.configDefaultLoadFailedImage(R.mipmap.error_imgs);
            bitmapUtils1.display(info_img_one, Url.FileIP + pictureDes1);
            info_img_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(pictureDes1)) {
                        subData("1");
                    }

                }
            });
        }
    }

    private void subData(String index) {
        Intent intent = new Intent(V2DetailsFindInfoActivity.this, Image216Activity.class);
        intent.putExtra("count" ,index) ;
        intent.putExtra("pic_number", pic_num) ;
        if (Integer.parseInt(pic_num) == 3){
            intent.putExtra("pic1" ,Url.FileIP + pictureDes1 ) ;
            intent.putExtra("pic2" ,Url.FileIP + pictureDes2 ) ;
            intent.putExtra("pic3" ,Url.FileIP + pictureDes3 ) ;
        }else if (Integer.parseInt(pic_num) == 2){
            intent.putExtra("pic1" ,Url.FileIP + pictureDes1 ) ;
            intent.putExtra("pic2" ,Url.FileIP + pictureDes2 ) ;
        }else if (Integer.parseInt(pic_num) == 1){
            intent.putExtra("pic1" ,Url.FileIP + pictureDes1 ) ;
        }
        startActivity(intent);
    }

    private void getPicNum(String pictureDes2, String pictureDes3) {
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

    private void showWordDes(String wordDes) {
        info_text_des.setText(wordDes);
    }

    private void showProLabel(com.alibaba.fastjson.JSONObject object) {
        String proLabel = object.getString("ProLabel");
        if (TextUtils.isEmpty(proLabel ) || "".equals(proLabel) ){
            proLabel_head.setVisibility(View.GONE);
            linear_proLabel.setVisibility(View.GONE);
        }else {
            String[] split = proLabel.split(",");
            initChildViews(split) ;
        }
    }

    private void showSecondViews(com.alibaba.fastjson.JSONObject object) {
        String typeID = object.getString("TypeID");
        switch (typeID){
            case "1" :
                showView1(object) ;
                break;
            case "6" :
                showView6(object) ;
                break;
            case "12" :
                showView12(object) ;
                break;
            case "16" :
                showView16(object) ;
                break;
            case "17" :
                showView17(object) ;
                break;
            case "18" :
                showView18(object) ;
                break;
            case "19" :
                showView19(object) ;
                break;
            case "20" :
                showView20(object) ;
                break;
            case "21" :
                showView21(object) ;
                break;
            case "22" :
                showView22(object) ;
                break;
            default:
                break;
        }
    }

    private void showView22(com.alibaba.fastjson.JSONObject object) {
        one_left.setText("资产类型：");
        one_right.setText(object.getString("AssetType"));
        two_left.setText("品牌型号：");
        two_right.setText(object.getString("Brand"));
        three_left.setText("起拍价：");
        three_right.setText(object.getString("Money"));
        four_left.setText("拍卖地点：");
        four_right.setText(object.getString("ProArea"));
        five_left.setText("拍卖时间：");
        five_right.setText(object.getString("Year"));
        six_left.setText("拍卖阶段：");
        six_right.setText(object.getString("State"));
        seven_left.setText("处置单位：");
        seven_right.setText(object.getString("Court"));
        eight_left.setVisibility(View.GONE);
        eight_right.setVisibility(View.GONE);
        nine_left.setVisibility(View.GONE);
        nine_right.setVisibility(View.GONE);
        ten_left.setVisibility(View.GONE);
        ten_right.setVisibility(View.GONE);

        three_right.setTextColor(Color.rgb(239, 130, 0));
        three_right.setTextSize(20);

        linear_01.setVisibility(View.GONE);
        linear_02.setVisibility(View.GONE);
        linear_03.setVisibility(View.GONE);
        second_head.setVisibility(View.GONE);
    }

    private void showView21(com.alibaba.fastjson.JSONObject object) {
        one_left.setText("资产类型：");
        one_right.setText(object.getString("AssetType"));
        two_left.setText("面积：");
        String area = object.getString("Area");
        two_right.setText(area + "平方米");
        three_left.setText("性质：");
        three_right.setText(object.getString("Nature"));
        four_left.setText("起拍价：");
        String money = object.getString("Money");
        four_right.setText(money + "万元");
        five_left.setText("拍卖地点：");
        five_right.setText(object.getString("ProArea"));
        six_left.setText("拍卖时间：");
        six_right.setText(object.getString("Year"));
        seven_left.setText("拍卖阶段：");
        seven_right.setText(object.getString("State"));
        eight_left.setText("处置单位：");
        eight_right.setText(object.getString("Court"));
        nine_left.setVisibility(View.GONE);
        nine_right.setVisibility(View.GONE);
        ten_left.setVisibility(View.GONE);
        ten_right.setVisibility(View.GONE);

        four_right.setTextColor(Color.rgb(239, 130, 0));
        four_right.setTextSize(20);

        linear_01.setVisibility(View.GONE);
        linear_02.setVisibility(View.GONE);
        linear_03.setVisibility(View.GONE);
        second_head.setVisibility(View.GONE);
    }

    private void showView20(com.alibaba.fastjson.JSONObject object) {
        one_left.setText("资产类型：");
        one_right.setText(object.getString("AssetType"));
        two_left.setText("面积：");
        String area = object.getString("Area");
        two_right.setText(area + "平方米");
        three_left.setText("性质：");
        three_right.setText(object.getString("Nature"));
        four_left.setText("起拍价：");
        String money = object.getString("Money");
        four_right.setText(money + "万元");
        five_left.setText("拍卖地点：");
        five_right.setText(object.getString("ProArea"));
        six_left.setText("拍卖时间：");
        six_right.setText(object.getString("Year"));
        seven_left.setText("拍卖阶段：");
        seven_right.setText(object.getString("State"));
        eight_left.setText("处置单位：");
        eight_right.setText(object.getString("Court"));
        nine_left.setVisibility(View.GONE);
        nine_right.setVisibility(View.GONE);
        ten_left.setVisibility(View.GONE);
        ten_right.setVisibility(View.GONE);

        four_right.setTextColor(Color.rgb(239, 130, 0));
        four_right.setTextSize(20);

        linear_01.setVisibility(View.GONE);
        linear_02.setVisibility(View.GONE);
        linear_03.setVisibility(View.GONE);
        second_head.setVisibility(View.GONE);
    }

    private void showView19(com.alibaba.fastjson.JSONObject object) {
        one_left.setText("发布方身份：");
        one_right.setText(object.getString("Identity"));
        two_left.setText("总金额：");
        String money = object.getString("TotalMoney");
        two_right.setText(money + "万元");
        three_left.setText("逾期时间：");
        String month = object.getString("Month");
        three_right.setText(month + "个月");
        four_left.setVisibility(View.GONE);
        four_right.setVisibility(View.GONE);
        five_left.setText("诉讼佣金比例：");
        five_right.setText(object.getString("Law"));
        five_right.setText(object.getString("Law"));
        six_left.setText("非诉讼佣金比例：");
        six_right.setText(object.getString("UnLaw"));
        seven_left.setText("债权人所在地：");
        seven_right.setText(object.getString("DebteeLocation"));
        eight_left.setText("债务人所在地：");
        eight_right.setText(object.getString("ProArea"));
        nine_left.setVisibility(View.GONE);
        nine_right.setVisibility(View.GONE);
        ten_left.setVisibility(View.GONE);
        ten_right.setVisibility(View.GONE);

        two_right.setTextColor(Color.rgb(239, 130, 0));
        two_right.setTextSize(20);

        linear_01.setVisibility(View.VISIBLE);
        linear_02.setVisibility(View.VISIBLE);
        linear_03.setVisibility(View.VISIBLE);

        String guaranty = object.getString("Guaranty");
        String property = object.getString("Property");
        String connect = object.getString("Connect");
        String pay = object.getString("Pay");
        String credentials = object.getString("Credentials");

        //是否有担保
        if (TextUtils.isEmpty(guaranty) || "请选择".equals(guaranty)){
            text_01_left.setText("是否有担保：未填写");
        }else {
            text_01_left.setText("是否有担保：" + guaranty);
        }
        //是否有抵押
        if (TextUtils.isEmpty(property) || "请选择".equals(property)){
            text_02_left.setText("是否有抵押：未填写");
        }else {
            text_02_left.setText("是否有抵押：" + property);
        }
        //债务人是否失联
        if (TextUtils.isEmpty(connect) || "请选择".equals(connect)){
            text_03_left.setText("债务人是否失联：未填写");
        }else {
            text_03_left.setText("债务人是否失联：" + connect);
        }
        //债务人是否有偿还能力
        if (TextUtils.isEmpty(pay) || "请选择".equals(pay)){
            text_01_right.setText("债务人是否有偿还能力：未填写");
        }else {
            text_01_right.setText("债务人是否有偿还能力：" + pay);
        }
        //相关凭证是否齐全
        if (TextUtils.isEmpty(credentials) || "请选择".equals(credentials)){
            text_02_right.setText("相关凭证是否齐全：未填写");
        }else {
            text_02_right.setText("相关凭证是否齐全：" + credentials);
        }
        text_03_right.setText("");
    }

    private void showView18(com.alibaba.fastjson.JSONObject object) {
        one_left.setText("发布方身份：");
        one_right.setText(object.getString("Identity"));
        two_left.setText("商账类型：");
        two_right.setText(object.getString("AssetType"));
        three_left.setText("债权金额：");
        String money = object.getString("Money");
        three_right.setText(money + "万元");
        four_left.setText("逾期时间：");
        String month = object.getString("Month");
        four_right.setText(month + "个月");
        five_left.setVisibility(View.GONE);
        five_right.setVisibility(View.GONE);
        six_left.setText("诉讼佣金比例：");
        six_right.setText(object.getString("Law"));
        seven_left.setText("非诉讼佣金比例：");
        seven_right.setText(object.getString("UnLaw"));
        eight_left.setText("债务方地区：");
        eight_right.setText(object.getString("ProArea"));
        nine_left.setText("债务方企业性质：");
        nine_right.setText(object.getString("Nature"));
        ten_left.setText("债务方经营情况：");
        ten_right.setText(object.getString("Status"));

        three_right.setTextColor(Color.rgb(239, 130, 0));
        three_right.setTextSize(20);

        linear_01.setVisibility(View.VISIBLE);
        linear_02.setVisibility(View.VISIBLE);
        linear_03.setVisibility(View.GONE);

        String guaranty = object.getString("Guaranty");
        String state = object.getString("State");
        String industry = object.getString("Industry");

        //有无债权相关凭证
        if (TextUtils.isEmpty(guaranty) || "请选择".equals(guaranty)){
            text_01_left.setText("有无债权相关凭证：未填写");
        }else {
            text_01_left.setText("有无债权相关凭证：" + guaranty);
        }
        //债务方行业
        if (TextUtils.isEmpty(industry) || "请选择".equals(industry)){
            text_01_right.setText("债务方行业：未填写");
        }else {
            text_01_right.setText("债务方行业：" + industry );
        }
        //债权涉诉情况
        if (TextUtils.isEmpty(state) || "请选择".equals(state)){
            text_02_left.setText("债权涉诉情况：未填写");
        }else {
            text_02_left.setText("债权涉诉情况：" + state );
        }
        text_02_right.setText("");
        text_03_right.setText("");
        text_03_left.setText("");
    }

    private void showView17(com.alibaba.fastjson.JSONObject object) {
        one_left.setText("发布方身份：");
        one_right.setText(object.getString("Identity"));
        two_left.setText("项目所在地：");
        two_right.setText(object.getString("ProArea"));
        three_left.setText("融资方式：");
        three_right.setText(object.getString("AssetType"));
        four_left.setText("担保方式：");
        four_right.setText(object.getString("Type"));
        five_left.setText("融资金额：");
        String money = object.getString("Money");
        five_right.setText(money + "万元");
        six_left.setText("使用期限：");
        String month = object.getString("Month");
        six_right.setText(month + "个月" );

        seven_left.setVisibility(View.GONE);
        seven_right.setVisibility(View.GONE);
        eight_left.setVisibility(View.GONE);
        eight_right.setVisibility(View.GONE);
        nine_left.setVisibility(View.GONE);
        nine_right.setVisibility(View.GONE);
        ten_left.setVisibility(View.GONE);
        ten_right.setVisibility(View.GONE);

        five_right.setTextColor(Color.rgb(239, 130, 0));
        five_right.setTextSize(20);

        linear_01.setVisibility(View.GONE);
        linear_02.setVisibility(View.GONE);
        linear_03.setVisibility(View.GONE);
        second_head.setVisibility(View.GONE);
    }

    //土地
    private void showView16(com.alibaba.fastjson.JSONObject object) {
        one_left.setText("发布方身份：");
        one_right.setText(object.getString("Identity"));
        two_left.setText("标的物类型：");
        two_right.setText(object.getString("AssetType"));
        three_left.setText("地区：");
        three_right.setText(object.getString("ProArea"));
        four_left.setText("规划用途：");
        four_right.setText(object.getString("Usefor"));
        five_left.setText("土地面积：");
        String area = object.getString("Area");
        five_right.setText(area + "平米");
        six_left.setText("建筑面积：");
        String buildArea = object.getString("BuildArea");
        if ("0.00".equals(buildArea)){
            six_right.setText("");
        }else {
            six_right.setText(buildArea + "平米");
        }
        seven_left.setText("容积率：");
        if ("0.00".equals(object.getString("FloorRatio"))){
            seven_right.setText("");
        }else {
            seven_right.setText(object.getString("FloorRatio"));
        }

        eight_left.setVisibility(View.GONE);
        eight_img.setVisibility(View.VISIBLE);
        eight_img.setImageResource(R.mipmap.zhuangrangjia);
        String money = object.getString("TransferMoney");
        eight_right.setText(money + "万元");
        nine_left.setVisibility(View.GONE);
        nine_right.setVisibility(View.GONE);
        ten_left.setVisibility(View.GONE);
        ten_right.setVisibility(View.GONE);

        eight_right.setTextColor(Color.rgb(239, 130, 0));
        eight_right.setTextSize(20);

        linear_01.setVisibility(View.GONE);
        linear_02.setVisibility(View.GONE);
        linear_03.setVisibility(View.GONE);
        second_head.setVisibility(View.GONE);
        String pictureDet = object.getString("PictureDet");
        if (!TextUtils.isEmpty(pictureDet) && !"".equals(pictureDet)){
            v216_PictureDet_head.setVisibility(View.VISIBLE);
            v216_PictureDet.setVisibility(View.VISIBLE);
        }


//        String credentials = object.getString("Credentials");
//        String dispute = object.getString("Dispute");
//        String debt = object.getString("Debt");
//        String guaranty = object.getString("Guaranty");
//        String property = object.getString("Property");
//
//        //有无相关证件
//        if (TextUtils.isEmpty(credentials) || "请选择".equals(credentials)){
//            text_01_left.setText("有无相关证件：未填写");
//        }else {
//            text_01_left.setText("有无相关证件：" + credentials);
//        }
//        //有无法律纠纷
//        if (TextUtils.isEmpty(dispute) || "请选择".equals(dispute)){
//            text_02_left.setText("有无法律纠纷：未填写");
//        }else {
//            text_02_left.setText("有无法律纠纷：" + dispute );
//        }
//        //有无负债
//        if (TextUtils.isEmpty(debt) || "请选择".equals(debt)){
//            text_03_left.setText("有无负债：未填写");
//        }else {
//            text_03_left.setText("有无负债：" + debt );
//        }
//
//        //有无抵押担保
//        if (TextUtils.isEmpty(guaranty) || "请选择".equals(guaranty)){
//            text_01_right.setText("有无抵押担保：未填写");
//        }else {
//            text_01_right.setText("有无抵押担保：" + guaranty);
//        }
//        //是否拥有全部产权
//        if (TextUtils.isEmpty(property) || "请选择".equals(property)){
//            text_02_right.setText("是否拥有全部产权：未填写");
//        }else {
//            text_02_right.setText("是否拥有全部产权：" + property );
//        }
//        text_03_right.setText("");

    }

    //房产
    private void showView12(com.alibaba.fastjson.JSONObject object) {
        one_left.setText("发布方身份：");
        one_right.setText(object.getString("Identity"));
        two_left.setText("标的物类型：");
        two_right.setText(object.getString("AssetType"));
        three_left.setText("地区：");
        three_right.setText(object.getString("ProArea"));
        four_left.setText("房产类型：");
        four_right.setText(object.getString("Type"));
        five_left.setText("面积：");
        String area = object.getString("Area");
        five_right.setText(area + "平米");
        six_left.setVisibility(View.GONE);
        six_img.setVisibility(View.VISIBLE);
        six_img.setImageResource(R.mipmap.shichangjia);
        String marketPrice = object.getString("MarketPrice");
        six_right.setText(marketPrice + "万元");
        seven_left.setVisibility(View.GONE);
        seven_img.setVisibility(View.VISIBLE);
        seven_img.setImageResource(R.mipmap.zhuangrangjia);
        String money = object.getString("TransferMoney");
        seven_right.setText(money + "万元");

        eight_left.setText("市场单价：");
        DecimalFormat df = new DecimalFormat("0.00");
        String s1 = "0" ;
        if (!TextUtils.isEmpty(area) &&!"0".equals(area)){
            float b = Float.parseFloat(area) ;
            if (!TextUtils.isEmpty(marketPrice)){
                float a = Float.parseFloat(marketPrice) ;
                float c =  a / b ;
                s1 = String.valueOf(df.format(c));
            }
            eight_right.setText(s1 + " 万元/平方米");
        }

        nine_left.setText("转让单价：");
        String s2 = "0" ;
        if (!TextUtils.isEmpty(area) &&!"0".equals(area)){
            float b = Float.parseFloat(area) ;
            if (!TextUtils.isEmpty(money)){
                float a = Float.parseFloat(money) ;
                float d =  a / b ;
                s2 = String.valueOf(df.format(d));
            }
            nine_right.setText(s2 + " 万元/平方米");
        }
        ten_left.setVisibility(View.GONE);
        ten_right.setVisibility(View.GONE);
        six_right.setTextColor(Color.rgb(239, 130, 0));
        six_right.setTextSize(20);
        seven_right.setTextColor(Color.rgb(239, 130, 0));
        seven_right.setTextSize(20);

        linear_01.setVisibility(View.GONE);
        linear_02.setVisibility(View.GONE);
        linear_03.setVisibility(View.GONE);
        second_head.setVisibility(View.GONE);
        String pictureDet = object.getString("PictureDet");
        if (!TextUtils.isEmpty(pictureDet) && !"".equals(pictureDet)){
            v216_PictureDet_head.setVisibility(View.VISIBLE);
            v216_PictureDet.setVisibility(View.VISIBLE);
        }


//        String credentials = object.getString("Credentials");
//        String dispute = object.getString("Dispute");
//        String debt = object.getString("Debt");
//        String guaranty = object.getString("Guaranty");
//        String property = object.getString("Property");
//
//        //有无相关证件
//        if (TextUtils.isEmpty(credentials) || "请选择".equals(credentials)){
//            text_01_left.setText("有无相关证件：未填写");
//        }else {
//            text_01_left.setText("有无相关证件：" + credentials);
//        }
//        //有无法律纠纷
//        if (TextUtils.isEmpty(dispute) || "请选择".equals(dispute)){
//            text_02_left.setText("有无法律纠纷：未填写");
//        }else {
//            text_02_left.setText("有无法律纠纷：" + dispute );
//        }
//        //有无负债
//        if (TextUtils.isEmpty(debt) || "请选择".equals(debt)){
//            text_03_left.setText("有无负债：未填写");
//        }else {
//            text_03_left.setText("有无负债：" + debt );
//        }
//
//        //有无抵押担保
//        if (TextUtils.isEmpty(guaranty) || "请选择".equals(guaranty)){
//            text_01_right.setText("有无抵押担保：未填写");
//        }else {
//            text_01_right.setText("有无抵押担保：" + guaranty);
//        }
//        //是否拥有全部产权
//        if (TextUtils.isEmpty(property) || "请选择".equals(property)){
//            text_02_right.setText("是否拥有全部产权：未填写");
//        }else {
//            text_02_right.setText("是否拥有全部产权：" + property );
//        }
//        text_03_right.setText("");

    }

    private void showView6(com.alibaba.fastjson.JSONObject object) {
        one_left.setText("发布方身份：");
        one_right.setText(object.getString("Identity"));
        two_left.setText("项目所在地：");
        two_right.setText(object.getString("ProArea"));
        three_left.setText("融资方式：");
        three_right.setText(object.getString("AssetType"));
        four_left.setText("融资金额：");
        String money2 = object.getString("TotalMoney");
        four_right.setText(money2 + "万元");
        five_left.setText("出让股权比例：");
        String rate = object.getString("Rate");
        five_right.setText(rate + "%");
        six_left.setText("企业现状：");
        six_right.setText(object.getString("Status"));
        seven_left.setText("所属行业：");
        seven_right.setText(object.getString("Belong"));
        eight_left.setText("资金用途：");
        eight_right.setText(object.getString("Usefor"));
        nine_left.setVisibility(View.GONE);
        nine_right.setVisibility(View.GONE);
        ten_left.setVisibility(View.GONE);
        ten_right.setVisibility(View.GONE);

        four_right.setTextColor(Color.rgb(239, 130, 0));
        four_right.setTextSize(20);

        linear_01.setVisibility(View.GONE);
        linear_02.setVisibility(View.GONE);
        linear_03.setVisibility(View.GONE);
        second_head.setVisibility(View.GONE);
    }

    //资产包
    private void showView1(com.alibaba.fastjson.JSONObject object) {
        one_left.setText("发布方身份：");
        one_right.setText(object.getString("Identity"));
        two_left.setText("卖家类型：");
        two_right.setText(object.getString("FromWhere"));
        three_left.setText("资产包类型：");
        three_right.setText(object.getString("AssetType"));
        four_left.setText("地区：");
        four_right.setText(object.getString("ProArea"));
        five_left.setVisibility(View.GONE);
        five_img.setVisibility(View.VISIBLE);
        five_img.setImageResource(R.mipmap.zongjine);
        String money = object.getString("TotalMoney");
        five_right.setText(money + "万元");
        six_left.setVisibility(View.GONE);
        six_img.setVisibility(View.VISIBLE);
        six_img.setImageResource(R.mipmap.zhuangrangjia);
        String money3 = object.getString("TransferMoney");
        six_right.setText(money3 + "万元");


        seven_left.setText("本金：");
        String money1 = object.getString("Money");
        if ("0.00".equals(money1)){
            seven_right.setText("");
        }else {
            seven_right.setText(money1 + "万元");
        }
        eight_left.setText("利息：");
        String money2 = object.getString("Rate");
        if("0.00".equals(money2)){
            eight_right.setText("");
        }else {
            eight_right.setText(money2 + "万元");
        }


        nine_left.setVisibility(View.GONE);
        nine_right.setVisibility(View.GONE);
        ten_left.setVisibility(View.GONE);
        ten_right.setVisibility(View.GONE);

        five_right.setTextColor(Color.rgb(239, 130, 0));
        six_right.setTextColor(Color.rgb(239, 130, 0));
        five_right.setTextSize(20);
        six_right.setTextSize(20);

        linear_01.setVisibility(View.GONE);
        linear_02.setVisibility(View.GONE);
        linear_03.setVisibility(View.GONE);
        second_head.setVisibility(View.GONE);
        String pictureDet = object.getString("PictureDet");
        if (!TextUtils.isEmpty(pictureDet) && !"".equals(pictureDet)){
            v216_PictureDet_head.setVisibility(View.VISIBLE);
            v216_PictureDet.setVisibility(View.VISIBLE);
        }

//        String money4 = object.getString("Money");
//        String rate = object.getString("Rate");
//        String counts = object.getString("Counts");
//        String report = object.getString("Report");
//        String time = object.getString("Time");
//        String pawn = object.getString("Pawn");
//        //本金数据的处理
//        if (TextUtils.isEmpty(money4) || "0".equals(money4) ){
//            text_01_left.setText("本金：未填写");
//        }else {
//            text_01_left.setText("本金：" + money4 + "万元");
//        }
//        //利息数据的处理
//        if (TextUtils.isEmpty(rate) || "0".equals(rate) ){
//            text_02_left.setText("利息：未填写");
//        }else {
//            text_02_left.setText("利息：" + rate + "万元" );
//        }
//        //户数数据的处理
//        if (TextUtils.isEmpty(counts) || "0".equals(counts) ){
//            text_03_left.setText("户数：未填写");
//        }else {
//            text_03_left.setText("户数：" + counts + "户");
//        }
//        //有无尽调报告
//        if (TextUtils.isEmpty(report) || "请选择".equals(report)){
//            text_01_right.setText("有无尽调报告：未填写");
//        }else {
//            text_01_right.setText("有无尽调报告：" + report);
//        }
//        //出表时间
//        if (TextUtils.isEmpty(time) || "请选择".equals(time)){
//            text_02_right.setText("出表时间：未填写");
//        }else {
//            text_02_right.setText("出表时间：" + time );
//        }
//        //抵押物类型
//        if (TextUtils.isEmpty(pawn) || "请选择".equals(pawn)){
//            text_03_right.setText("抵押物类型：未填写");
//        }else {
//            text_03_right.setText("抵押物类型：" + pawn );
//        }
    }

    private void showTimeCount(String publishTime, String viewCount) {
        if (publishTime.length() >= 11){
            String substring = publishTime.substring(0, 11);
            info_time.setText(viewCount);
            info_time_new.setText(substring);
        }
    }

    private void showProjectNumber(String projectNumber) {
        info_no.setText(projectNumber);
    }

    private void showNickName(String nickName, String phonenumber) {
        if (!TextUtils.isEmpty(nickName)){
            info_nickName.setText(nickName);
        }else {
            String substring = phonenumber.substring(0, 3);
            String substring1 = phonenumber.substring(7, 11);
            info_nickName.setText(substring + "****" + substring1 );
        }
    }

    private void showUserPicture(String userPicture) {
        BitmapUtils bitmapUtils = new BitmapUtils(V2DetailsFindInfoActivity.this);
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.error_imgs);
        bitmapUtils.display(info_icon, Url.FileIP + userPicture);
    }

    private void showVipStatus(String member , String CooperateState  , String TypeID ) {
        if (!TextUtils.isEmpty(CooperateState) && "1".equals(CooperateState)){
            info_vip.setVisibility(View.VISIBLE);
            info_vip.setImageResource(R.mipmap.v2140101);
        }else if (!TextUtils.isEmpty(CooperateState) && "2".equals(CooperateState)){
            if ("6".equals(TypeID) || "17".equals(TypeID)  || "20".equals(TypeID)  || "21".equals(TypeID)  || "22".equals(TypeID) ){
                info_vip.setVisibility(View.VISIBLE);
                info_vip.setImageResource(R.mipmap.v2140102);
            }else {
                info_vip.setVisibility(View.VISIBLE);
                info_vip.setImageResource(R.mipmap.v2140103);
            }
        }else if (!TextUtils.isEmpty(member) && member.equals("1")) {
            info_vip.setVisibility(View.VISIBLE);
            info_vip.setImageResource(R.mipmap.v2_vip_icon);
        }else if (!TextUtils.isEmpty(member) && member.equals("2")){
            info_vip.setVisibility(View.VISIBLE);
            info_vip.setImageResource(R.mipmap.v2_money_icon);
        }else {
            info_vip.setVisibility(View.VISIBLE);
            info_vip.setImageResource(R.mipmap.mianfei);
        }
    }

    private void showTitle( String title ) {
        info_title.setText(title);
    }

    private void goShowIcon() {
        //获取布局加载器对象
        View contentView = getLayoutInflater().inflate(R.layout.popupwindow_show_icon, null);
        //实例化组件
        ImageView imageView = (ImageView) contentView.findViewById(R.id.popupwindow_image);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        info_icon.setDrawingCacheEnabled(true);
        Bitmap bm = info_icon.getDrawingCache();
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
        popupWindow.showAsDropDown(info_icon, 0, 0);
        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void goReportActivity() {
        if (GetBenSharedPreferences.getIsLogin(this)){
            Intent intent = new Intent(V2DetailsFindInfoActivity.this , ReportActivity.class ) ;
            intent.putExtra("type" , "info") ;
            intent.putExtra("id" , id ) ;
            startActivity(intent);
        }else {
            Intent intent = new Intent(V2DetailsFindInfoActivity.this , LoginActivity.class ) ;
            startActivity(intent ) ;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();



        String shareTitle ;
        if (wordDes.length() >= 60){
            shareTitle = wordDes.substring( 0 , 60 ) ;
        }else{
            shareTitle = wordDes ;
        }


        oks.setTitle(title);
        oks.setTitleUrl(Url.ShareInfo + id);
        oks.setImageUrl("http://images.ziyawang.com/Applogo/logo.png");
        oks.setText(shareTitle);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url.ShareInfo + id);
        // 启动分享GUI
        oks.show(this);
    }

    private void goCollect() {
        if (isLogin) {
            //收藏功能接口的调用
            goLoadCollect() ;
        } else {
            //未登录，跳转到登陆页面
            goLoginActivity();
        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(V2DetailsFindInfoActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goLoadCollect() {
        HttpUtils utils = new HttpUtils();
        RequestParams params1 = new RequestParams();
        params1.addBodyParameter("itemID", id);
        params1.addBodyParameter("type", "1");
        String a = String.format(Url.Collect, login);
        Log.e("benben_id", id);
        Log.e("benben_login", login);
        Log.e("benben_a", a);
        utils.send(HttpRequest.HttpMethod.POST, a, params1, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benbne", responseInfo.result);
                dealCollectResult(responseInfo.result);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(V2DetailsFindInfoActivity.this, "网络连接异常");
            }
        });
    }

    private void dealCollectResult(String result) {
        try {
            JSONObject object = new JSONObject(result);
            String msg = object.getString("msg");
            switch (msg) {
                case "取消收藏成功！":
                    Toast.makeText(V2DetailsFindInfoActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    unCollect();
                    break;
                case "收藏成功！":
                    Toast.makeText(V2DetailsFindInfoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    collect();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 未收藏状态的显示
     */
    private void collect() {
        info_collect.setImageResource(R.mipmap.v2shouc);
    }

    /**
     * 收藏状态的显示
     */
    private void unCollect() {
        info_collect.setImageResource(R.mipmap.v2shoucang);
    }

    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(V2DetailsFindInfoActivity.this , "数据加载中，请稍后。。。");
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

    private void initChildViews(String[] split) {
        // TODO Auto-generated method stub
        flowlayout = (FlowLayout) findViewById(R.id.flowlayout);
        flowlayout.removeAllViews();
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10 ;
        lp.rightMargin = 10 ;
        lp.topMargin = 10 ;
        lp.bottomMargin = 10 ;
        for(int i = 0; i < split.length; i ++){
            TextView view = new TextView(this);
            view.setText(split[i]);
            view.setTextColor(Color.rgb(239,130,0));
            view.setTextSize(12);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.flowstyle));
            flowlayout.addView(view,lp);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按键
            case R.id.pre :
                finish();
                break;
            //收藏按钮的监听事件
            case R.id.relative_collect :
                goCollect();
                break;
            //分享按钮的监听事件
            case R.id.relative_share :
                showShare();
                break;
            //据保按钮的监听
            case R.id.report :
                goReportActivity() ;
                break;
            //头像的监听事件
            case R.id.info_icon:
                goShowIcon() ;
                break;
            //播放语音
            case R.id.info_audio_des :
                goAudioDes() ;
                break;
            //拨打电话按钮
            case R.id.info_call :
                goCall() ;
                break;
            //私聊按钮的监听事件
            case R.id.info_sendMessage :
                goSendMessage() ;
                break;
            //作为发布方进入的页面
            case R.id.show_info_register :
                goServiceRegister() ;
                break;
            default:
                break;

        }
    }

    public void onResume() {
        super.onResume();
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
        //统计页面
        MobclickAgent.onPageStart("找信息详情页面");
        //统计时长
        MobclickAgent.onResume(this);
        //加载数据
        loadData(id);
    }

    private void goServiceRegister() {
        ToastUtils.shortToast(V2DetailsFindInfoActivity.this, "建议您通过认证后再进行约谈、私聊");
    }

    private void goSendMessage() {
        if (isLogin) {
            //启动会话界面
            if (RongIM.getInstance() != null) RongIM.getInstance().startPrivateChat(V2DetailsFindInfoActivity.this, userID, "聊天详情");
        } else {
            //未登录，跳转到登陆页面
            goLoginActivity();
        }
    }

    private void goCall() {
        if (isLogin) {

            //显示是否拨打电话的登陆按钮
            //showCustomDialog() ;

            //已经支付过
            final CustomDialog.Builder builder01 = new CustomDialog.Builder(V2DetailsFindInfoActivity.this);
            builder01.setTitle("亲爱的用户");
            builder01.setMessage("您确定要联系" + connectPhone + "?");
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



        } else {
            //跳转到登录页面
            goLoginActivity() ;
        }
    }

    private void showCustomDialog() {

        if ("1".equals(PayFlag)){
            //已经支付过
            final CustomDialog.Builder builder01 = new CustomDialog.Builder(V2DetailsFindInfoActivity.this);
            builder01.setTitle("亲爱的用户");
            builder01.setMessage("您确定要联系" + connectPhone + "?");
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
            //还未支付过,是否是收费信息，若是收费信息，直接拨打电话，不是收费信息，则调用接口拨打电话。
            //showPopUpWindow() ;
            //1.0.5版本。收费信息到此页面，一定是支付过，未支付的信息为不收费信息。
            String url = String.format(Url.Pay, login ) ;
            HttpUtils httpUtils = new HttpUtils() ;
            RequestParams params = new RequestParams() ;
            params.addBodyParameter("ProjectID" , id );
            httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("benben" , responseInfo.result ) ;
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseInfo.result);
                        String status_code = jsonObject.getString("status_code");
                        switch (status_code){
                            case "200" :
                                PayFlag = "1" ;
                                //textView6.setText("已约谈");
                                final CustomDialog.Builder builder01 = new CustomDialog.Builder(V2DetailsFindInfoActivity.this);
                                builder01.setTitle("亲爱的用户");
                                builder01.setMessage("您确定要联系" + connectPhone + "?");
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
                                break;
                            case "416" :
                                ToastUtils.shortToast(V2DetailsFindInfoActivity.this , "非收费信息");
                                break;
                            case "417" :
                                ToastUtils.shortToast(V2DetailsFindInfoActivity.this , "您已经支付过该条信息");
                                break;
                            case "418" :
                                ToastUtils.shortToast(V2DetailsFindInfoActivity.this , "余额不足，请充值。");
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
                    ToastUtils.shortToast( V2DetailsFindInfoActivity.this , "网络连接异常");
                }
            }) ;
        }

    }

    private void goCallNumber() {
        String str = "tel:" + connectPhone;
        //直接拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
        if (ActivityCompat.checkSelfPermission(V2DetailsFindInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast(V2DetailsFindInfoActivity.this, "请在管理中心，给予直接拨打电话权限。");
            return;
        }
        startActivity(intent);
    }

    private void goAudioDes() {
        if (!TextUtils.isEmpty(voiceDes) && !voiceDes.equals("")) {
            //加载语音文件
            goLoadAudioDes() ;
        } else {
            ToastUtils.shortToast(V2DetailsFindInfoActivity.this, "发布方尚未对此信息添加语音描述");
        }
    }

    private void goLoadAudioDes() {
        if (info_audio_des.getText().equals("播放")) {
            info_audio_des.setText("停止");
            String url = Url.FileIPAudio + voiceDes;
            player.playUrl(url);
            //设置声音的时长
            int durationTime = player.getDurationTime();
            String s = MyTimeFormat.changeTimeMS(durationTime);
            info_audio_des_duration.setText(s);
        } else {
            player.stop();
            info_audio_des.getText().equals("播放") ;
        }
    }

}
