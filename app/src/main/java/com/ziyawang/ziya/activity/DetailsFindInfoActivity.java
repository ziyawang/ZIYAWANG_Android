package com.ziyawang.ziya.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.MyImageView;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.rong.imkit.RongIM;

public class DetailsFindInfoActivity extends BaseActivity implements View.OnClickListener {

    private ScrollView info_scroll;

    private Player player;

    /**
     * 回退按钮
     */
    private RelativeLayout pre ;
    /**
     * 信息的title
     */
    private TextView info_title;
    /**
     * 详情的头像
     */
    private ImageView info_icon;
    /**
     * 信息的类型
     */
    private TextView info_type;
    /**
     * 信息的编号
     */
    private TextView info_no;
    /**
     * 信息的信息完整度
     */
    //private RatingBar info_rating;
    /**
     * 信息的收藏
     */
    private TextView info_collect;
    /**
     * 信息的分享
     */
    private TextView info_share;

    /**
     * 详细信息页面
     */
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

    /**
     * 信息的语言描述
     */
    private TextView info_text_des;
    /**
     * 信息的声音描述
     */
    private Button info_audio_des;
    /**
     * 信息的声音描述的时间
     */
    private TextView info_audio_des_duration;

    /**
     * 信息的图片描述1
     */
    private MyImageView info_img_one;
    /**
     * 信息的图片描述2
     */
    private MyImageView info_img_two;
    /**
     * 信息的图片描述3
     */
    private MyImageView info_img_three;

    /**
     * 信息的清单下载
     */
    private RelativeLayout info_upload;

    /**
     * 拨打电话
     */
    private RelativeLayout info_call;
    private RelativeLayout go;
    /**
     * 拨打电话
     */
    private RelativeLayout info_sendMessage;

    private LinearLayout niu;
    private LinearLayout niu_niu;
    private LinearLayout search_person;
    private LinearLayout linearLayout;


    private static String spphoneNumber;
    private static String phoneNumber;
    private static String login;
    private static boolean isLogin;
    private static String root;
    private static String id;
    private String wordDes;
    private TextView textView7 ;

    private String publishState ;

    private ImageView info_vip ;

    private MyProgressDialog dialog  ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_find_info);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.bbb);//通知栏所需颜色

        final SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);

        final SharedPreferences myNumber = getSharedPreferences("myNumber", MODE_PRIVATE);
        spphoneNumber = myNumber.getString("myNumber", null);

        final SharedPreferences role = getSharedPreferences("role", MODE_PRIVATE);
        root = role.getString("role", null);

        SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
        isLogin = sp.getBoolean("isLogin", false);


        //实例化组件
        initView();

        //得到上个页面传过来的title和id
        final Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        //设置title
        info_title.setText(title);

        if (isLogin) {
//            Log.e("benbne", "------------------");
//            Log.e("benbne", root);
//            switch (root) {
//                case "0":
//                case "2":
//                    //电话私聊抢单人消失
//                    linearLayout.setVisibility(View.GONE);
//                    info_call.setVisibility(View.GONE);
//                    info_sendMessage.setVisibility(View.GONE);
//                    //收藏按钮消失
//                    info_collect.setVisibility(View.GONE);
//                    break;
//                case "1":
//                    linearLayout.setVisibility(View.VISIBLE);
//                    go.setVisibility(View.VISIBLE);
//                    info_call.setVisibility(View.VISIBLE);
//                    info_sendMessage.setVisibility(View.VISIBLE);
//
//                    go.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //ToastUtils.shortToast(DetailsFindInfoActivity.this, "服务方，申请抢单，功能待开发。");
//
//                            String urls = String.format(Url.Rush, login);
//                            HttpUtils httpUtils = new HttpUtils() ;
//                            RequestParams params = new RequestParams() ;
//                            params.addBodyParameter("ProjectID" , id );
//                            httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
//                                @Override
//                                public void onSuccess(ResponseInfo<String> responseInfo) {
//                                    Log.e("benben", responseInfo.result) ;
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(responseInfo.result ) ;
//                                        String msg = jsonObject.getString("msg");
//                                        switch (msg){
//                                            case "您已抢单，请不要重复抢单！" :
//                                                ToastUtils.shortToast(DetailsFindInfoActivity.this , "您已抢单，请不要重复抢单！");
//                                                break;
//                                            case "抢单成功":
//                                                ToastUtils.shortToast(DetailsFindInfoActivity.this , "抢单成功");
//                                                break;
//                                            default:
//                                                break;
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(HttpException error, String msg) {
//                                    ToastUtils.shortToast(DetailsFindInfoActivity.this , "申请抢单失败");
//                                    error.printStackTrace();
//                                }
//                            });
//                        }
//                    });
//
//                    break;
//                default:
//                    break;
//            }
        }else {
            linearLayout.setVisibility(View.VISIBLE);
            info_call.setVisibility(View.GONE);
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
        //加载数据
        loadData(id);

    }

//    @TargetApi(19)
//    private void setTranslucentStatus(boolean on) {
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }

    private void loadData(final String id) {

        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(DetailsFindInfoActivity.this , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();

        String urls = String.format(Url.Details_info, id ,login );
        HttpUtils httpUtils = new HttpUtils();
        final RequestParams params = new RequestParams();
        params.addQueryStringParameter("access_token", "token");
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benbne", responseInfo.result);

                if (dialog!=null){
                    dialog.dismiss();
                }

                info_scroll.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);

                    String typeName = jsonObject.getString("TypeName");
                    //String projectID = jsonObject.getString("ProjectID");
                    //String TypeID = jsonObject.getString("TypeID");
                    //String userID = jsonObject.getString("UserID");
                    //String publishState = jsonObject.getString("PublishState");
                    //String CertifyState = jsonObject.getString("CertifyState");
                    //String publishTime = jsonObject.getString("PublishTime");
                    phoneNumber = jsonObject.getString("PhoneNumber");
                    //String serviceID = jsonObject.getString("ServiceID");
                    //String viewCount = jsonObject.getString("ViewCount");
                    //String collectionCount = jsonObject.getString("CollectionCount");
                    String proArea = jsonObject.getString("ProArea");
                    wordDes = jsonObject.getString("WordDes");
                    final String voiceDes = jsonObject.getString("VoiceDes");
                    final String pictureDes1 = jsonObject.getString("PictureDes1");
                    final String pictureDes2 = jsonObject.getString("PictureDes2");
                    final String pictureDes3 = jsonObject.getString("PictureDes3");
                    String totalMoney = jsonObject.getString("TotalMoney");
                    //String fromWhere = jsonObject.getString("FromWhere");
                    String assetType = jsonObject.getString("AssetType");
                    //String assetList = jsonObject.getString("AssetList");
                    String transferMoney = jsonObject.getString("TransferMoney");
                    //String rushCount = jsonObject.getString("RushCount");
                    final String collectFlag = jsonObject.getString("CollectFlag");
                    String userPicture = jsonObject.getString("UserPicture");
                    String projectNumber = jsonObject.getString("ProjectNumber");
                    final String rushFlag = jsonObject.getString("RushFlag");

                    final String userID = jsonObject.getString("UserID");

                    publishState = jsonObject.getString("PublishState");
                    String Member = jsonObject.getString("Member");

                    if (!TextUtils.isEmpty(Member) && Member.equals("1")){
                        info_vip.setVisibility(View.VISIBLE);
                    }


                    switch (collectFlag) {
                        case "0":
                            Drawable drawable01 = getResources().getDrawable(R.mipmap.collect_un);
                            /// 这一步必须要做,否则不会显示.
                            drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
                            info_collect.setCompoundDrawables(null , drawable01, null, null);
                            break;
                        case "1":
                            Drawable drawable02 = getResources().getDrawable(R.mipmap.collect);
                            /// 这一步必须要做,否则不会显示.
                            drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
                            info_collect.setCompoundDrawables(null , drawable02 , null, null);

                            break;
                        default:
                            break;
                    }


                    //设置头像
                    BitmapUtils bitmapUtils = new BitmapUtils(DetailsFindInfoActivity.this);
                    bitmapUtils.display(info_icon, Url.FileIP + userPicture);
                    //设置title
                    info_type.setText(typeName);
                    //设置编号
                    info_no.setText("---" + projectNumber);
                    //设置不同的类型显示不同的信息
                    switch (typeName) {
                        case "资产包转让":
                            String fromWhere = jsonObject.getString("FromWhere");
                            info_details_two.setText(totalMoney + "万");
                            info_details_four.setText(transferMoney + "万");
                            info_details_six.setText(proArea);
                            info_details_eight.setText(fromWhere);
                            info_details_ten.setText(assetType);
                            info_upload.setVisibility(View.VISIBLE);
                            break;
                        case "委外催收":
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
                            break;
                        case "法律服务":
                            String requirement = jsonObject.getString("Requirement");
                            niu.setVisibility(View.GONE);
                            niu_niu.setVisibility(View.GONE);
                            info_details_five.setTextColor(Color.rgb(192, 20, 68));
                            info_details_five.setText("需求：");
                            info_details_six.setText(requirement);
                            info_details_seven.setText("地区：");
                            info_details_eight.setText(proArea);
                            info_details_ten.setText(assetType);
                            break;
                        case "商业保理":
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
                            break;
                        case "融资需求":
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
                            break;
                        case "典当担保":

                            niu.setVisibility(View.GONE);
                            niu_niu.setVisibility(View.GONE);
                            info_details_five.setText("金额：");
                            info_details_five.setTextColor(Color.rgb(192, 20, 68));
                            info_details_six.setText(totalMoney + "万");
                            info_details_seven.setText("地区：");
                            info_details_eight.setText(proArea);
                            info_details_ten.setText(assetType);

                            break;
                        case "悬赏信息":
                            niu.setVisibility(View.GONE);
                            niu_niu.setVisibility(View.GONE);
                            info_details_five.setText("金额：");
                            info_details_five.setTextColor(Color.rgb(192, 20, 68));
                            info_details_six.setText(totalMoney + "元");
                            info_details_seven.setText("地区：");
                            info_details_eight.setText(proArea);
                            info_details_ten.setText(assetType);
                            break;
                        case "尽职调查":

                            String informant = jsonObject.getString("Informant");
                            niu.setVisibility(View.GONE);
                            niu_niu.setVisibility(View.GONE);
                            info_details_five.setText("被调查方：");
                            info_details_five.setTextColor(Color.rgb(192, 20, 68));
                            info_details_six.setText(informant);
                            info_details_seven.setText("地区：");
                            info_details_eight.setText(proArea);
                            info_details_ten.setText(assetType);
                            break;
                        case "固产转让":
                            niu.setVisibility(View.GONE);
                            niu_niu.setVisibility(View.GONE);
                            info_details_five.setText("转让价：");
                            info_details_five.setTextColor(Color.rgb(192, 20, 68));
                            info_details_six.setText(transferMoney + "万");
                            info_details_seven.setText("地区：");
                            info_details_eight.setText(proArea);
                            info_details_ten.setText(assetType);
                            break;
                        case "资产求购":
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
                            break;
                        case "债权转让":
                            info_details_one.setText("金额：");
                            info_details_two.setText(totalMoney + "万");
                            info_details_four.setText(transferMoney + "万");
                            info_details_five.setText("类型：");
                            info_details_six.setText(assetType);
                            info_details_seven.setText("地区：");
                            info_details_eight.setText(proArea);
                            info_details_nine.setVisibility(View.GONE);
                            info_details_ten.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }
                    //信息描述
                    info_text_des.setText(wordDes);
                    BitmapUtils bitmapUtils1 = new BitmapUtils(DetailsFindInfoActivity.this);
                    bitmapUtils1.display(info_img_one, Url.FileIP + pictureDes1);
                    info_img_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(pictureDes1)){
                                Intent intent = new Intent(DetailsFindInfoActivity.this, ShowImageViewActivity.class);
                                intent.putExtra("url", Url.FileIP + pictureDes1);
                                startActivity(intent);
                            }

                        }
                    });
                    BitmapUtils bitmapUtils2 = new BitmapUtils(DetailsFindInfoActivity.this);
                    bitmapUtils2.display(info_img_two, Url.FileIP + pictureDes2);
                    info_img_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(pictureDes2)){
                                Intent intent = new Intent(DetailsFindInfoActivity.this, ShowImageViewActivity.class);
                                intent.putExtra("url", Url.FileIP + pictureDes2);
                                startActivity(intent);
                            }

                        }
                    });
                    BitmapUtils bitmapUtils3 = new BitmapUtils(DetailsFindInfoActivity.this);
                    bitmapUtils3.display(info_img_three, Url.FileIP + pictureDes3);
                    info_img_three.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(pictureDes3)){
                                Intent intent = new Intent(DetailsFindInfoActivity.this, ShowImageViewActivity.class);
                                intent.putExtra("url", Url.FileIP + pictureDes3);
                                startActivity(intent);
                            }
                        }
                    });
                    //判断是否登录
                    if (isLogin) {
                        //ToastUtils.shortToast(DetailsFindInfoActivity.this, login);

                        Log.e("benbenbenbenben", phoneNumber);
                        if (!TextUtils.isEmpty(spphoneNumber)) {
                            Log.e("benbenbenben", spphoneNumber);

                            //判断是否是自己发出的信息
                            if (spphoneNumber.equals(phoneNumber)) {
                                linearLayout.setVisibility(View.GONE);
                                search_person.setVisibility(View.VISIBLE);
                                search_person.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent1 = new Intent(DetailsFindInfoActivity.this, RushPersonActivity.class);
                                        intent1.putExtra("id" , id  ) ;
                                        intent1.putExtra("status" , publishState ) ;
                                        startActivity(intent1);
                                    }
                                });
                            } else {

                                Log.e("benbne", "------------------");
                                Log.e("benbne", root);
                                switch (root) {
                                    case "0":
                                    case "2":
                                        //电话私聊抢单人消失
                                        linearLayout.setVisibility(View.GONE);
                                        info_call.setVisibility(View.GONE);
                                        info_sendMessage.setVisibility(View.GONE);
                                        //收藏按钮消失
                                        info_collect.setVisibility(View.GONE);
                                        break;
                                    case "1":
                                        linearLayout.setVisibility(View.VISIBLE);
                                        go.setVisibility(View.VISIBLE);
                                        info_call.setVisibility(View.GONE);
                                        info_sendMessage.setVisibility(View.VISIBLE);

                                        if (!TextUtils.isEmpty(rushFlag)){
                                            switch (rushFlag){
                                                case "0" :
                                                    go.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            //ToastUtils.shortToast(DetailsFindInfoActivity.this, "服务方，申请抢单，功能待开发。");

                                                            String urls = String.format(Url.Rush, login);
                                                            HttpUtils httpUtils = new HttpUtils() ;
                                                            RequestParams params = new RequestParams() ;
                                                            params.addBodyParameter("ProjectID" , id );
                                                            httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                                                                @Override
                                                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                                                    Log.e("benben", responseInfo.result) ;
                                                                    try {
                                                                        JSONObject jsonObject = new JSONObject(responseInfo.result ) ;
                                                                        String msg = jsonObject.getString("msg");
                                                                        switch (msg){
                                                                            case "您已抢单，请不要重复抢单！" :
                                                                                ToastUtils.shortToast(DetailsFindInfoActivity.this , "您已抢单，请不要重复抢单！");
                                                                                break;
                                                                            case "抢单成功":
                                                                                ToastUtils.shortToast(DetailsFindInfoActivity.this , "抢单成功");
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
                                                                    ToastUtils.shortToast(DetailsFindInfoActivity.this , "申请抢单失败");
                                                                    error.printStackTrace();
                                                                }
                                                            });
                                                        }
                                                    });
                                                    break;
                                                case "1" :
                                                    textView7.setText("已抢单");
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }

                                //linearLayout.setVisibility(View.VISIBLE);
                                //ToastUtils.shortToast(DetailsFindInfoActivity.this, "不是本人发出的消息");
                            }
                        }
                    } else {
                        //ToastUtils.shortToast(DetailsFindInfoActivity.this, "还未登陆");
                    }
                    //拨打电话
                    info_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isLogin) {
                                final CustomDialog.Builder builder01 = new CustomDialog.Builder(DetailsFindInfoActivity.this);
                                builder01.setTitle("亲爱的用户");
                                builder01.setMessage("您确定要联系" + phoneNumber + "?");
                                builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //拨打电话
                                        //为拨打电话添加监听事件
                                        String str = "tel:" + phoneNumber;
                                        //直接拨打电话
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
                                        if (ActivityCompat.checkSelfPermission(DetailsFindInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.
                                            ToastUtils.shortToast(DetailsFindInfoActivity.this, "请在管理中心，给予直接拨打电话权限。");
                                            return;
                                        }
                                        startActivity(intent);
                                        //跳转到拨号页面
                                        //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(str));
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        //startActivity(intent);

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
                                Intent intent = new Intent(DetailsFindInfoActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                    //下载清单页面
                    info_upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.shortToast(DetailsFindInfoActivity.this, "请登录到PC端进行下载");
                        }
                    });
                    //对分享页面进行的监听
                    info_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showShare();
                        }
                    });
                    //对收藏页面的监听
                    info_collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isLogin) {
                                //收藏功能接口的调用
                                HttpUtils utils = new HttpUtils();
                                RequestParams params1 = new RequestParams();
                                //params1.addQueryStringParameter("token",login );
                                params1.addBodyParameter("itemID", id);
                                params1.addBodyParameter("type", "1");
                                String a = String.format(Url.Collect, login);
                                Log.e("benben_id", id);
                                Log.e("benben_login", login);
                                Log.e("benben_a", a);
                                //params1.addQueryStringParameter();
                                utils.send(HttpRequest.HttpMethod.POST, a, params1, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        Log.e("benbne", responseInfo.result);
                                        try {
                                            JSONObject object = new JSONObject(responseInfo.result);
                                            String msg = object.getString("msg");
                                            switch (msg) {
                                                case "取消收藏成功！":
                                                    Toast.makeText(DetailsFindInfoActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                                                    Drawable drawable01 = getResources().getDrawable(R.mipmap.collect_un);
                                                    /// 这一步必须要做,否则不会显示.
                                                    drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
                                                    info_collect.setCompoundDrawables(null ,drawable01 , null, null);
                                                    break;
                                                case "收藏成功！":
                                                    Toast.makeText(DetailsFindInfoActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
                                                    Drawable drawable02 = getResources().getDrawable(R.mipmap.collect);
                                                    /// 这一步必须要做,否则不会显示.
                                                    drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
                                                    info_collect.setCompoundDrawables(null ,drawable02 , null, null);
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
                                        ToastUtils.shortToast(DetailsFindInfoActivity.this, "收藏失败");
                                    }
                                });
                            } else {
                                Intent intent = new Intent(DetailsFindInfoActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                    //进入私聊的页面
                    info_sendMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isLogin) {
                                //启动会话界面
                                if (RongIM.getInstance() != null)
                                    RongIM.getInstance().startPrivateChat(DetailsFindInfoActivity.this,  userID ,"聊天详情" );
                            } else {
                                Intent intent = new Intent(DetailsFindInfoActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }

                        }
                    });
                    //对信息完整度的一个设定,默认五颗星。
                    //info_rating.setRating((float) 4.5);
                    //设置声音

                    Log.e("benbenvoice" , voiceDes )  ;
                    info_audio_des.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ToastUtils.shortToast(DetailsFindInfoActivity.this , "暂时不存在");
                            if (!TextUtils.isEmpty(voiceDes) && !voiceDes.equals("")) {
                                if (info_audio_des.getText().equals("播放")) {
                                    info_audio_des.setText("停止");
                                    //String url = "http://baobab.wdjcdn.com/1451897812703c.mp4";
                                    String url = Url.FileIPAudio + voiceDes ;
                                    //String url = "http://files.zll.science" + voiceDes;
                                    Log.e("benben", url);
                                    player.playUrl(url);

                                    //设置声音的时长
                                    int durationTime = player.getDurationTime();
                                    String s = MyTimeFormat.changeTimeMS(durationTime);
                                    info_audio_des_duration.setText(s);

                                } else {
                                    player.stop();
                                }
                            } else {
                                ToastUtils.shortToast(DetailsFindInfoActivity.this, "不存在音频文件");
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                error.printStackTrace();
                Log.e("benben", "------------网络连接失败----------------");
                ToastUtils.shortToast(DetailsFindInfoActivity.this, "网络连接失败");
                if (dialog!=null){
                    dialog.dismiss();
                }
            }
        });

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
        oks.setUrl(Url.ShareInfo + id );
        // 启动分享GUI
        oks.show(this);
    }

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("找信息详情页面");
        //统计时长
        MobclickAgent.onResume(this);

        //player.play();


    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("找信息详情页面");
        //统计时长
        MobclickAgent.onPause(this);

        //player.pause();
    }

    private void initView() {

        niu = (LinearLayout) findViewById(R.id.niu);
        niu_niu = (LinearLayout) findViewById(R.id.niu_niu);
        search_person = (LinearLayout) findViewById(R.id.search_person);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        info_scroll = (ScrollView) findViewById(R.id.info_scroll);

        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        pre.setOnClickListener(this);
        info_title = (TextView) findViewById(R.id.info_title);

        info_icon = (ImageView) findViewById(R.id.info_icon);
        info_vip = (ImageView) findViewById(R.id.info_vip);
        info_type = (TextView) findViewById(R.id.info_type);
        info_no = (TextView) findViewById(R.id.info_no);

        //info_rating = (RatingBar) findViewById(R.id.info_rating);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }
}
