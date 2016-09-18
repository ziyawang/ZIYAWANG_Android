package com.ziyawang.ziya.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ziyawang.ziya.tools.MyTimeFormat;
import com.ziyawang.ziya.tools.Player;
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
 * 信息的详情页面
 */
public class DetailsFindInfoActivity extends BenBenActivity implements View.OnClickListener {
    //滑动布局
    private ScrollView info_scroll;
    //定义播放器
    private Player player;
    //回退按钮
    private RelativeLayout pre ;
    //信息的title
    private TextView info_title;
    //详情的头像
    private ImageView info_icon;
    //信息的类型
    private TextView info_type;
    //信息的审核时间和观看次数
    private TextView info_time ;
    //信息的编号
    private TextView info_no;
    //信息的收藏
    private TextView info_collect;
    //信息的分享
    private TextView info_share;
    //类型的linear
    private LinearLayout type_linear ;
    //详细信息页面
    //是否存在语音
    private TextView voice_status ;
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
    private TextView info_text_des;
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
    //是否是vip资源的显示
    private ImageView info_vip ;
    //数据加载的dialog
    private MyProgressDialog dialog  ;
    //数据解析后用到的数据
    private static String proArea ;
    private static String Member ;
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
    private float DownX = 0;
    //作为发布方进入的页面，并非投资和融资模块
    private LinearLayout show_info_register ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //未登陆状态下的显示样式,如果用户未登陆，
        showView() ;
        //加载数据
        loadData(id);

        info_scroll.setOnTouchListener(new View.OnTouchListener() {
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
        setContentView(R.layout.activity_details_find_info);
    }

    @Override
    public void initViews() {
        niu = (LinearLayout) findViewById(R.id.niu);
        type_linear = (LinearLayout) findViewById(R.id.type_linear);
        show_info_register = (LinearLayout) findViewById(R.id.show_info_register);
        niu_145 = (LinearLayout) findViewById(R.id.niu_145);
        niu_niu = (LinearLayout) findViewById(R.id.niu_niu);
        search_person = (LinearLayout) findViewById(R.id.search_person);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        info_scroll = (ScrollView) findViewById(R.id.info_scroll);
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        voice_status = (TextView) findViewById(R.id.voice_status);
        info_title = (TextView) findViewById(R.id.info_title);
        info_time = (TextView) findViewById(R.id.info_time);
        info_icon = (ImageView) findViewById(R.id.info_icon);
        info_vip = (ImageView) findViewById(R.id.info_vip);
        info_type = (TextView) findViewById(R.id.info_type);
        info_no = (TextView) findViewById(R.id.info_no);
        info_collect = (TextView) findViewById(R.id.info_collect);
        textView7 = (TextView) findViewById(R.id.textView7);
        info_share = (TextView) findViewById(R.id.info_share);
        info_details_one = (TextView) findViewById(R.id.info_details_one);
        info_details_two = (TextView) findViewById(R.id.info_details_two);
        info_details_three = (TextView) findViewById(R.id.info_details_three);
        info_details_four = (TextView) findViewById(R.id.info_details_four);
        info_details_five = (TextView) findViewById(R.id.info_details_five);
        info_details_six = (TextView) findViewById(R.id.info_details_six);
        info_details_seven = (TextView) findViewById(R.id.info_details_seven);
        info_details_eight = (TextView) findViewById(R.id.info_details_eight);
        info_details_nine = (TextView) findViewById(R.id.info_details_nine);
        info_details_ten = (TextView) findViewById(R.id.info_details_ten);
        info_text_des = (TextView) findViewById(R.id.info_text_des);
        info_audio_des = (Button) findViewById(R.id.info_audio_des);
        info_audio_des_duration = (TextView) findViewById(R.id.info_audio_des_duration);
        info_img_one = (MyImageView) findViewById(R.id.info_img_one);
        info_img_two = (MyImageView) findViewById(R.id.info_img_two);
        info_img_three = (MyImageView) findViewById(R.id.info_img_three);
        info_upload = (RelativeLayout) findViewById(R.id.info_upload);
        go = (RelativeLayout) findViewById(R.id.go);
        info_call = (RelativeLayout) findViewById(R.id.info_call);
        info_sendMessage = (RelativeLayout) findViewById(R.id.info_sendMessage);
        player = new Player();
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        go.setOnClickListener(this);
        info_call.setOnClickListener(this);
        info_upload.setOnClickListener(this);
        info_share.setOnClickListener(this);
        info_collect.setOnClickListener(this);
        info_sendMessage.setOnClickListener(this);
        info_audio_des.setOnClickListener(this);
        show_info_register.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //获得用户的ticket
        login = GetBenSharedPreferences.getTicket(this);
        //获得用户的spphoneNumber
        spphoneNumber = GetBenSharedPreferences.getSpphoneNumber(this) ;
        //获得用户的root
        root = GetBenSharedPreferences.getRole(this) ;
        //获得用户的isLogin
        isLogin = GetBenSharedPreferences.getIsLogin(this) ;
        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        //设置title
        info_title.setText(title);
    }

    private void showView() {
        if (!isLogin) {
            linearLayout.setVisibility(View.VISIBLE);
            info_call.setVisibility(View.VISIBLE);
            info_sendMessage.setVisibility(View.VISIBLE);
            go.setVisibility(View.VISIBLE);
            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(DetailsFindInfoActivity.this , LoginActivity.class ) ;
                    startActivity(intent1);
                }
            });
        }
    }

    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(DetailsFindInfoActivity.this , "数据加载中，请稍后。。。");
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

    private void loadData(final String id) {
        //显示数据加载框
        showBenDialog();
        //开启网络请求
        String urls = String.format(Url.Details_info, id ,login );
        HttpUtils httpUtils = new HttpUtils();
        final RequestParams params = new RequestParams();
        params.addQueryStringParameter("access_token", "token");
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benbne", responseInfo.result);
                //隐藏dialog
                hiddenBenDialog();
                //处理json
                dealResult(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(DetailsFindInfoActivity.this, "网络连接失败");
                hiddenBenDialog();
            }
        });
    }

    private void dealResult(String result) {
        //数据加载成功后显示整个view
        info_scroll.setVisibility(View.VISIBLE);
        try {
            //数据的解析
            JSONObject jsonObject = new JSONObject(result);
            publishState = jsonObject.getString("PublishState");
            wordDes = jsonObject.getString("WordDes");
            phoneNumber = jsonObject.getString("PhoneNumber");
            proArea = jsonObject.getString("ProArea");
            Member = jsonObject.getString("Member");
            typeName = jsonObject.getString("TypeName");
            totalMoney = jsonObject.getString("TotalMoney");
            assetType = jsonObject.getString("AssetType");
            transferMoney = jsonObject.getString("TransferMoney");
            userPicture = jsonObject.getString("UserPicture");
            projectNumber = jsonObject.getString("ProjectNumber");
            collectFlag = jsonObject.getString("CollectFlag");
            rushFlag = jsonObject.getString("RushFlag");
            userID = jsonObject.getString("UserID");
            corpore = jsonObject.getString("Corpore");
            voiceDes = jsonObject.getString("VoiceDes");
            pictureDes1 = jsonObject.getString("PictureDes1");
            pictureDes2 = jsonObject.getString("PictureDes2");
            pictureDes3 = jsonObject.getString("PictureDes3");
            time = jsonObject.getString("PublishTime");
            view_count = jsonObject.getString("ViewCount");
            //根据请求的数据，进行页面的显示
            showData(jsonObject) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showData(JSONObject jsonObject) throws JSONException {
        //根据数据请求过来的Member来正确显示是否是VIP资源
        showVipStatus(Member) ;
        //根据数据请求过来的collectFlag来正确显示是否是收藏
        showCollectStatus(collectFlag) ;
        //根据数据请求过来的userPicture来正确显示头像
        showUserPicture(userPicture) ;
        //根据数据请求过来的typeName来正确显示title
        showTitle(typeName) ;
        //根据数据请求过来的projectNumber来正确显示编号
        showNo(projectNumber) ;
        //根据数据请求过来的wordDes来正确显示信息描述
        showWordDes(wordDes) ;
        //得到图片的个数
        getPicNum(pictureDes1, pictureDes2, pictureDes3);
        //根据数据请求过来的pictureDes1来正确显示第一张图片
        showPictureDes1(pictureDes1);
        //根据数据请求过来的pictureDes2来正确显示第二张图片
        showPictureDes2(pictureDes2);
        //根据数据请求过来的pictureDes3来正确显示第三张图片
        showPictureDes3(pictureDes3) ;
        info_time.setText(time + "  浏览：" + view_count);
        //音频文件不存在，将文字置为空。
        if (TextUtils.isEmpty(voiceDes) && voiceDes.equals("")){
            //info_audio_des.setText("");
            info_audio_des.setVisibility(View.GONE);
            voice_status.setVisibility(View.VISIBLE);
        }
        //根据数据判断，已经登陆状态下sp不空
        //Log.e("benbensp" ,spphoneNumber ) ;
        //Log.e("benben", phoneNumber) ;
        if (judgeNotNull()){
            //是自己发布显示抢单人页面,否则不显示
            if (judgeMyself()){
                Log.e("judgeMyself" , "自己发布的信息") ;
                showMyselfView() ;
            }else {
                showNotMyselfVIew(rushFlag) ;
            }
        }
        //设置不同的类型显示不同的信息
        showTypeName(typeName, jsonObject) ;
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

    private void showTypeName(String typeName, JSONObject jsonObject) throws JSONException {
        switch (typeName) {
            case "资产包转让":
                showTypeName01(jsonObject) ;
                break;
            case "委外催收":
                showTypeName02(jsonObject) ;
                break;
            case "法律服务":
                showTypeName03(jsonObject) ;
                break;
            case "商业保理":
                showTypeName04(jsonObject) ;
                break;
            case "融资需求":
                showTypeName05(jsonObject) ;
                break;
            case "典当担保":
                showTypeName06() ;
                break;
            case "悬赏信息":
                showTypeName07() ;
                break;
            case "尽职调查":
                showTypeName08(jsonObject) ;
                break;
            case "固产转让":
                showTypeName09() ;
                break;
            case "资产求购":
                showTypeName10(jsonObject) ;
                break;
            case "债权转让":
                showTypeName11() ;
                break;
            case "投资需求":
                showTypeName12(jsonObject) ;
                break;
            default:
                break;
        }
    }

    private void showTypeName12(JSONObject jsonObject) throws JSONException {
        String investType = jsonObject.getString("InvestType");
        String year = jsonObject.getString("Year");
        String rate = jsonObject.getString("Rate");
        info_details_one.setText("回报率：");
        info_details_two.setText(rate + "%");
        info_details_three.setText("投资期限：");
        info_details_four.setText(year + "年");
        info_details_five.setText("投资方式：");
        info_details_six.setText(assetType);
        info_details_seven.setText("地区：");
        info_details_eight.setText(proArea);
        info_details_nine.setText("投资类型：");
        info_details_ten.setText(investType);

        if (isLogin){
            if (judgeMyself()){
                Log.e("judgeMyself" , "自己发布的信息") ;
                showMyselfView() ;
                show_info_register.setVisibility(View.GONE);
            } else {
                showNotMyselfVIew(rushFlag) ;
                show_info_register.setVisibility(View.GONE);
                //可以查看拨打电话
                linearLayout.setVisibility(View.VISIBLE);
                info_sendMessage.setVisibility(View.VISIBLE);
                info_call.setVisibility(View.VISIBLE);
                show_info_register.setVisibility(View.GONE);
            }

        }else {
            //可以查看拨打电话
            linearLayout.setVisibility(View.VISIBLE);
            info_sendMessage.setVisibility(View.VISIBLE);
            info_call.setVisibility(View.VISIBLE);
            show_info_register.setVisibility(View.GONE);
        }
    }

    private void showTypeName11() {
        info_details_one.setText("金额：");
        info_details_two.setText(totalMoney + "万");
        info_details_four.setText(transferMoney + "万");
        info_details_five.setText("类型：");
        info_details_six.setText(assetType);
        info_details_seven.setText("地区：");
        info_details_eight.setText(proArea);
        info_details_nine.setVisibility(View.GONE);
        info_details_ten.setVisibility(View.GONE);
        type_linear.setVisibility(View.GONE);
    }

    private void showTypeName10(JSONObject jsonObject) throws JSONException {
        String buyer = jsonObject.getString("Buyer");
        info_details_one.setText("求购方：");
        info_details_two.setText(buyer);
        info_details_three.setVisibility(View.GONE);
        info_details_four.setVisibility(View.GONE);
        info_details_five.setText("类型：");
        info_details_six.setText(assetType);
        info_details_seven.setText("地区：");
        info_details_eight.setText(proArea);
        info_details_nine.setVisibility(View.GONE);
        info_details_ten.setVisibility(View.GONE);
        type_linear.setVisibility(View.GONE);
        if (isLogin){

            if (judgeMyself()){
                Log.e("judgeMyself" , "自己发布的信息") ;
                showMyselfView() ;
                show_info_register.setVisibility(View.GONE);
            }else {
                showNotMyselfVIew(rushFlag) ;
                show_info_register.setVisibility(View.GONE);
                //可以查看拨打电话
                linearLayout.setVisibility(View.VISIBLE);
                info_sendMessage.setVisibility(View.VISIBLE);
                info_call.setVisibility(View.VISIBLE);
                show_info_register.setVisibility(View.GONE);
            }
        }else {
            linearLayout.setVisibility(View.VISIBLE);
            info_sendMessage.setVisibility(View.VISIBLE);
            info_call.setVisibility(View.VISIBLE);
            show_info_register.setVisibility(View.GONE);
        }

    }

    private void showTypeName09() {
        niu.setVisibility(View.VISIBLE);
        niu_145.setVisibility(View.VISIBLE);
        niu_niu.setVisibility(View.VISIBLE);
        info_details_one.setText("转让价：");
        info_details_one.setTextColor(Color.rgb(192, 20, 68));
        info_details_two.setText(transferMoney + "万");
        info_details_three.setText("地区：");
        info_details_four.setText(proArea);
        info_details_five.setText("标的物：");
        info_details_six.setText(corpore);
        info_details_seven.setText("");
        info_details_eight.setText("");
        info_details_ten.setText(assetType);
    }

    private void showTypeName08(JSONObject jsonObject) throws JSONException {
        String informant = jsonObject.getString("Informant");
        niu.setVisibility(View.GONE);
        niu_niu.setVisibility(View.GONE);
        info_details_five.setText("被调查方：");
        info_details_five.setTextColor(Color.rgb(192, 20, 68));
        info_details_six.setText(informant);
        info_details_seven.setText("地区：");
        info_details_eight.setText(proArea);
        info_details_ten.setText(assetType);
    }

    private void showTypeName07() {
        niu.setVisibility(View.GONE);
        niu_niu.setVisibility(View.GONE);
        info_details_five.setText("金额：");
        info_details_five.setTextColor(Color.rgb(192, 20, 68));
        info_details_six.setText(totalMoney + "元");
        info_details_seven.setText("地区：");
        info_details_eight.setText(proArea);
        info_details_ten.setText(assetType);
    }

    private void showTypeName06() {
        niu.setVisibility(View.GONE);
        niu_niu.setVisibility(View.GONE);
        info_details_five.setText("金额：");
        info_details_five.setTextColor(Color.rgb(192, 20, 68));
        info_details_six.setText(totalMoney + "万");
        info_details_seven.setText("地区：");
        info_details_eight.setText(proArea);
        info_details_ten.setText(assetType);
    }

    private void showTypeName05(JSONObject jsonObject) throws JSONException {
        String rate1 = jsonObject.getString("Rate");
        info_details_one.setText("金额：");
        info_details_two.setText(totalMoney + "万");
        info_details_three.setText("回报率：");
        info_details_four.setText(rate1 + "%");
        info_details_five.setText("方式：");
        info_details_six.setText(assetType);
        info_details_seven.setText("地区:");
        info_details_eight.setText(proArea);
        info_details_nine.setVisibility(View.GONE);
        info_details_ten.setVisibility(View.GONE);
        type_linear.setVisibility(View.GONE);
    }

    private void showTypeName04(JSONObject jsonObject) throws JSONException {
        String buyerNature = jsonObject.getString("BuyerNature");
        niu.setVisibility(View.GONE);
        niu_niu.setVisibility(View.GONE);
        info_details_five.setTextColor(Color.rgb(192, 20, 68));
        info_details_five.setText("合同金额：");
        info_details_six.setText(totalMoney + "万");
        info_details_seven.setText("地区：");
        info_details_eight.setText(proArea);
        info_details_nine.setText("买方性质：");
        info_details_ten.setText(buyerNature);
    }

    private void showTypeName03(JSONObject jsonObject) throws JSONException {
        String requirement = jsonObject.getString("Requirement");
        niu.setVisibility(View.GONE);
        niu_niu.setVisibility(View.GONE);
        info_details_five.setTextColor(Color.rgb(192, 20, 68));
        info_details_five.setText("需求：");
        info_details_six.setText(requirement);
        info_details_seven.setText("地区：");
        info_details_eight.setText(proArea);
        info_details_ten.setText(assetType);
    }

    private void showTypeName02(JSONObject jsonObject) throws JSONException {
        String rate = jsonObject.getString("Rate");
        String status = jsonObject.getString("Status");
        info_details_one.setText("金额：");
        info_details_two.setText(totalMoney + "万");
        info_details_three.setText("佣金比例：");
        info_details_four.setText(rate);
        info_details_five.setText("状态：");
        info_details_six.setText(status);
        info_details_nine.setText("债务人所在地：");
        info_details_seven.setText("类型：");
        info_details_eight.setText(assetType);
        info_details_ten.setText(proArea);
    }

    private void showTypeName01(JSONObject jsonObject) throws JSONException {
        String fromWhere = jsonObject.getString("FromWhere");
        info_details_two.setText(totalMoney + "万");
        info_details_four.setText(transferMoney + "万");
        info_details_six.setText(proArea);
        info_details_eight.setText(fromWhere);
        info_details_ten.setText(assetType);
        info_upload.setVisibility(View.VISIBLE);
    }

    private void showNotMyselfVIew(String rushFlag) {
        switch (root) {
            case "0":
            case "2":
                //不是自己发出的信息，身份为不是服务方
                showRootIsZeroTwo() ;
                break;
            case "1":
                //不是自己发出的信息，身份是服务方
                showRootIsZeroOne(rushFlag) ;
                break;
            default:
                break;
        }
    }

    private void showRootIsZeroOne(String rushFlag) {
        //电话私聊抢单人收藏存在
        linearLayout.setVisibility(View.VISIBLE);
        go.setVisibility(View.VISIBLE);
        info_call.setVisibility(View.VISIBLE);
        info_sendMessage.setVisibility(View.VISIBLE);
        //不是自己发出的信息，身为服务方，根据信息rushFlag，正确展示
        showRushFlagStatus(rushFlag) ;

    }

    private void showRushFlagStatus(String rushFlag) {
        if (!TextUtils.isEmpty(rushFlag)) {
            switch (rushFlag) {
                case "0":
                    //还未被抢单，点击抢单，可以抢单
                    showUnRush() ;
                    break;
                case "1":
                    //已经被抢单，展示被抢单信息。
                    showRush() ;
                    break;
                default:
                    break;
            }
        }
    }

    private void showRush() {
        textView7.setText("已抢单");
    }

    private void showUnRush() {
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载动画
                showBenDialog();
                //开启网络请求，申请抢单
                goRush();
            }
        });
    }

    private void goRush() {
        String urls = String.format(Url.Rush, login);
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("ProjectID", id);
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result);
                //成功回调
                dealRushResult(responseInfo.result);
                //关闭dialog
                hiddenBenDialog();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //失败回调
                ToastUtils.shortToast(DetailsFindInfoActivity.this, "申请抢单失败");
                //打印错误信息
                error.printStackTrace();
                //关闭dialog
                hiddenBenDialog();
            }
        });
    }

    private void dealRushResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String msg = jsonObject.getString("msg");
            switch (msg) {
                case "您已抢单，请不要重复抢单！":
                    ToastUtils.shortToast(DetailsFindInfoActivity.this, "您已抢单，请不要重复抢单！");
                    break;
                case "抢单成功":
                    ToastUtils.shortToast(DetailsFindInfoActivity.this, "抢单成功");
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showRootIsZeroTwo() {
        //电话私聊抢单人收藏消失
        linearLayout.setVisibility(View.GONE);
        info_call.setVisibility(View.GONE);
        info_sendMessage.setVisibility(View.GONE);
        info_collect.setVisibility(View.GONE);

        show_info_register.setVisibility(View.VISIBLE);
    }

    private void showMyselfView() {
        linearLayout.setVisibility(View.GONE);
        search_person.setVisibility(View.VISIBLE);
        search_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailsFindInfoActivity.this, RushPersonActivity.class);
                intent1.putExtra("id", id);
                intent1.putExtra("status", publishState);
                startActivity(intent1);
            }
        });
    }

    private boolean judgeMyself() {
       if (spphoneNumber.equals(phoneNumber)){
           Log.e("benbensp" ,spphoneNumber ) ;
           Log.e("benben" ,phoneNumber ) ;
           return true ;
       }else {
           return false ;
       }
    }

    private boolean judgeNotNull() {
        if (!isLogin){
            return false ;
        }
        if (TextUtils.isEmpty(spphoneNumber)){
            return false ;
        }
        return true;
    }

    private void showPictureDes3(final String pictureDes3) {
        BitmapUtils bitmapUtils3 = new BitmapUtils(DetailsFindInfoActivity.this);
        bitmapUtils3.display(info_img_three, Url.FileIP + pictureDes3);
        info_img_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(pictureDes3)) {
                    subData("3") ;
                }
            }
        });
    }

    private void showPictureDes2(final String pictureDes2) {
        BitmapUtils bitmapUtils2 = new BitmapUtils(DetailsFindInfoActivity.this);
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

    private void showPictureDes1(final String pictureDes1) {
        BitmapUtils bitmapUtils1 = new BitmapUtils(DetailsFindInfoActivity.this);
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

    private void subData(String index) {
        Intent intent = new Intent(DetailsFindInfoActivity.this, ShowImageViewActivity.class);
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

    private void showWordDes(String wordDes) {
        info_text_des.setText(wordDes);
    }

    private void showNo(String projectNumber) {
        info_no.setText("---" + projectNumber);
    }

    private void showTitle(String typeName) {
        info_type.setText(typeName);
    }

    private void showUserPicture(String userPicture) {
        BitmapUtils bitmapUtils = new BitmapUtils(DetailsFindInfoActivity.this);
        bitmapUtils.display(info_icon, Url.FileIP + userPicture);
    }

    private void showCollectStatus(String collectFlag) {
        switch (collectFlag) {
            //未收藏状态的显示
            case "0":
                unCollect() ;
                break;
            //收藏状态的显示
            case "1":
                collect() ;
                break;
            default:
                break;
        }
    }

    /**
     * 未收藏状态的显示
     */
    private void collect() {
        Drawable drawable02 = getResources().getDrawable(R.mipmap.collect);
        drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
        info_collect.setCompoundDrawables(null, drawable02, null, null);
    }

    /**
     * 收藏状态的显示
     */
    private void unCollect() {
        Drawable drawable01 = getResources().getDrawable(R.mipmap.collect_un);
        drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
        info_collect.setCompoundDrawables(null, drawable01, null, null);
    }

    /**
     * 根据数据请求过来的Member来正确显示是否是VIP资源
     * @param Member
     */
    private void showVipStatus(String Member) {
        if (!TextUtils.isEmpty(Member) && Member.equals("1")) {
            info_vip.setVisibility(View.VISIBLE);
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle("资芽");
        oks.setTitleUrl("http://www.ziyawang.com");
        oks.setImageUrl("http://images.ziyawang.com/Applogo/logo.png");
        oks.setText(wordDes);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url.ShareInfo + id);
        // 启动分享GUI
        oks.show(this);
    }

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("找信息详情页面");
        //统计时长
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("找信息详情页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按键
            case R.id.pre:
                finish();
                break;
            //拨打电话按钮
            case R.id.info_call :
                goCall() ;
                break;
            //清单文件下载
            case R.id.info_upload :
                goUpload() ;
                break;
            //分享按钮的监听事件
            case R.id.info_share :
                showShare();
                break;
            //收藏按钮的监听事件
            case R.id.info_collect :
                goCollect();
                break;
            //私聊按钮的监听事件
            case R.id.info_sendMessage :
                goSendMessage() ;
                break;
            //播放语音
            case R.id.info_audio_des :
                goAudioDes() ;
                break;
            //作为发布方进入的页面，并非投资和融资模块
            case R.id.show_info_register :
                goServiceRegister() ;
                break;
            default:
                break;
        }
    }

    private void goServiceRegister() {
        ToastUtils.shortToast(DetailsFindInfoActivity.this, "通过认证的服务方才能查看联系方式、申请抢单、私聊。");
    }

    private void goAudioDes() {
        if (!TextUtils.isEmpty(voiceDes) && !voiceDes.equals("")) {
            //加载语音文件
            goLoadAudioDes() ;
        } else {
            ToastUtils.shortToast(DetailsFindInfoActivity.this, "发布方尚未对此信息添加语音描述");
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

    private void goSendMessage() {
        if (isLogin) {
            //启动会话界面
            if (RongIM.getInstance() != null) RongIM.getInstance().startPrivateChat(DetailsFindInfoActivity.this, userID, "聊天详情");
        } else {
            //未登录，跳转到登陆页面
            goLoginActivity();
        }
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
                ToastUtils.shortToast(DetailsFindInfoActivity.this, "收藏失败");
            }
        });
    }

    private void dealCollectResult(String result) {
        try {
            JSONObject object = new JSONObject(result);
            String msg = object.getString("msg");
            switch (msg) {
                case "取消收藏成功！":
                    Toast.makeText(DetailsFindInfoActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                    unCollect();
                    break;
                case "收藏成功！":
                    Toast.makeText(DetailsFindInfoActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
                    collect();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void goUpload() {
        ToastUtils.shortToast(DetailsFindInfoActivity.this, "请登录到PC端进行下载");
    }

    private void goCall() {
        if (isLogin) {
            //显示是否拨打电话的登陆按钮
            showCustomDialog() ;
        } else {
            //跳转到登录页面
            goLoginActivity() ;

        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(DetailsFindInfoActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void showCustomDialog() {
        final CustomDialog.Builder builder01 = new CustomDialog.Builder(DetailsFindInfoActivity.this);
        builder01.setTitle("亲爱的用户");
        builder01.setMessage("您确定要联系" + phoneNumber + "?");
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
    }

    private void goCallNumber() {
        String str = "tel:" + phoneNumber;
        //直接拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
        if (ActivityCompat.checkSelfPermission(DetailsFindInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast(DetailsFindInfoActivity.this, "请在管理中心，给予直接拨打电话权限。");
            return;
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }
}
