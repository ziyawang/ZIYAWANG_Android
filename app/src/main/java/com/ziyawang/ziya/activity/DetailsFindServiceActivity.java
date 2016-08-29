package com.ziyawang.ziya.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.MyImageView;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.rong.imkit.RongIM;

public class DetailsFindServiceActivity extends BaseActivity {


    private static String spphoneNumber;
    private static String ConnectPhone;
    private static String login;
    private static boolean isLogin;
    private static String root;


    private ScrollView service_scroll;
    private RelativeLayout pre;
    private TextView service_title;
    private ImageView service_icon;
    /**
     * 服务的类型
     */
    private TextView service_type;
    /**
     * 服务的编号
     */
    private TextView service_no;
    /**
     * 服务的信息完整度
     */
    //private RatingBar service_rating;
    /**
     * 服务的收藏
     */
    private TextView service_collect;
    /**
     * 服务的分享
     */
    private TextView service_share;

    private TextView service_details_two;
    private TextView service_details_four;
    private TextView service_details_six;


    private TextView service_text_des;
    private TextView service_for_part;
    private TextView service_des_type;

    private MyImageView service_img_one;
    private MyImageView service_img_two;
    private MyImageView service_img_three;

    private RelativeLayout service_call;
    private RelativeLayout service_sendMessage;

    private LinearLayout search_person;
    private LinearLayout linearLayout;

    private static String id;
    private String ServiceName;

    private MyProgressDialog dialog  ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_find_service  );

        final SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);

        final SharedPreferences myNumber = getSharedPreferences("myNumber", MODE_PRIVATE);
        spphoneNumber = myNumber.getString("myNumber", null);

        final SharedPreferences role = getSharedPreferences("role", MODE_PRIVATE);
        root = role.getString("role", null);

        SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
        isLogin = sp.getBoolean("isLogin", false);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        //实例化组件
        initView();

        //加载数据
        loadData(id);

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void loadData(final String id) {

        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(DetailsFindServiceActivity.this , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();

        String urls = String.format(Url.Details_service, id, login);
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("access_token", "token");
        utils.configCurrentHttpCacheExpiry(1000);
        utils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result);

                if (dialog!=null){
                    dialog.dismiss();
                }

                //加载数据之后，设置为可见。
                service_scroll.setVisibility(View.VISIBLE);

                try {
                    JSONObject object = new JSONObject(responseInfo.result);

                    String serviceID = object.getString("ServiceID");
                    ServiceName = object.getString("ServiceName");
                    String ServiceIntroduction = object.getString("ServiceIntroduction");
                    String ServiceLocation = object.getString("ServiceLocation");
                    String ServiceType = object.getString("ServiceType");
                    String ServiceLevel = object.getString("ServiceLevel");
                    String ConnectPerson = object.getString("ConnectPerson");
                    ConnectPhone = object.getString("ConnectPhone");
                    String ServiceArea = object.getString("ServiceArea");
                    final String ConfirmationP1 = object.getString("ConfirmationP1");
                    final String ConfirmationP2 = object.getString("ConfirmationP2");
                    final String ConfirmationP3 = object.getString("ConfirmationP3");
                    String CollectionCount = object.getString("CollectionCount");
                    String ViewCount = object.getString("ViewCount");
                    String Label = object.getString("Label");
                    String created_at = object.getString("created_at");
                    String updated_at = object.getString("updated_at");
                    final String UserID = object.getString("UserID");
                    //String SerCerID = object.getString("SerCerID");
                    //String State = object.getString("State");
                    //String OperateTime = object.getString("OperateTime");
                    //String OperatePerson = object.getString("OperatePerson");
                    //String Remark = object.getString("Remark");
                    String CoNumber = object.getString("CoNumber");
                    String UserPicture = object.getString("UserPicture");
                    String ServiceNumber = object.getString("ServiceNumber");
                    final String CollectFlag = object.getString("CollectFlag");

                    switch (CollectFlag) {
                        case "0":
                            Drawable drawable01 = getResources().getDrawable(R.mipmap.collect_un);
                            /// 这一步必须要做,否则不会显示.
                            drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
                            service_collect.setCompoundDrawables(null, drawable01, null, null);
                            break;
                        case "1":
                            Drawable drawable02 = getResources().getDrawable(R.mipmap.collect);
                            /// 这一步必须要做,否则不会显示.
                            drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
                            service_collect.setCompoundDrawables(null, drawable02, null, null);

                            break;
                        default:
                            break;
                    }

                    //设置头像
                    BitmapUtils bitmapUtils = new BitmapUtils(DetailsFindServiceActivity.this);
                    bitmapUtils.display(service_icon, Url.FileIP + UserPicture);
                    //设置title
                    //service_type.setText(ServiceName);
                    //设置编号
                    service_type.setText("编号：" + ServiceNumber);
                    //设置不同的类型显示不同的信息

                    //信息描述
                    service_text_des.setText("        " + ServiceIntroduction);
                    //服务地区的描述
                    service_for_part.setText(ServiceArea);
                    //服务类型的描述
                    service_des_type.setText(ServiceType);
                    BitmapUtils bitmapUtils1 = new BitmapUtils(DetailsFindServiceActivity.this);
                    bitmapUtils1.display(service_img_one, Url.FileIP + ConfirmationP1);
                    service_img_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(ConfirmationP1)) {
                                Intent intent = new Intent(DetailsFindServiceActivity.this, ShowImageViewActivity.class);
                                intent.putExtra("url", Url.FileIP + ConfirmationP1);
                                startActivity(intent);
                            }
                        }
                    });
                    BitmapUtils bitmapUtils2 = new BitmapUtils(DetailsFindServiceActivity.this);
                    bitmapUtils2.display(service_img_two, Url.FileIP + ConfirmationP2);
                    service_img_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(ConfirmationP2)) {
                                Intent intent = new Intent(DetailsFindServiceActivity.this, ShowImageViewActivity.class);
                                intent.putExtra("url", Url.FileIP + ConfirmationP2);
                                startActivity(intent);
                            }
                        }
                    });
                    BitmapUtils bitmapUtils3 = new BitmapUtils(DetailsFindServiceActivity.this);
                    bitmapUtils3.display(service_img_three, Url.FileIP + ConfirmationP3);
                    service_img_three.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(ConfirmationP3)) {
                                Intent intent = new Intent(DetailsFindServiceActivity.this, ShowImageViewActivity.class);
                                intent.putExtra("url", Url.FileIP + ConfirmationP3);
                                startActivity(intent);
                            }

                        }
                    });

                    //判断是否登录
                    if (isLogin) {
                        //ToastUtils.shortToast(DetailsFindServiceActivity.this, login) ;

                        Log.e("benbenbenbenben", ConnectPhone);
                        if (!TextUtils.isEmpty(spphoneNumber)) {

                            Log.e("benbenbenben", spphoneNumber);


                            //判断是否是自己发出的信息
                            if (spphoneNumber.equals(ConnectPhone)) {
                                linearLayout.setVisibility(View.GONE);
                                search_person.setVisibility(View.VISIBLE);
                                search_person.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent1 = new Intent(DetailsFindServiceActivity.this, MyRushActivity.class);
                                        startActivity(intent1);
                                    }
                                });
                            } else {

                                //ToastUtils.shortToast(DetailsFindServiceActivity.this  , "不是本人发出的消息");
                            }
                        }

                    } else {

                        //ToastUtils.shortToast(DetailsFindServiceActivity.this, "还未登陆");

                    }

                    //拨打电话
                    service_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (isLogin) {
                                final CustomDialog.Builder builder01 = new CustomDialog.Builder(DetailsFindServiceActivity.this);
                                builder01.setTitle("亲爱的用户");
                                builder01.setMessage("您确定要联系" + ConnectPhone + "?");
                                builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //拨打电话
                                        //为拨打电话添加监听事件


                                        String str = "tel:" + ConnectPhone;
                                        //直接拨打电话
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
                                        if (ActivityCompat.checkSelfPermission(DetailsFindServiceActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.
                                            ToastUtils.shortToast(DetailsFindServiceActivity.this, "请在管理中心，给予直接拨打电话权限。");
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
                                Intent intent1 = new Intent(DetailsFindServiceActivity.this, LoginActivity.class);
                                startActivity(intent1);
                            }

                        }
                    });
                    //对分享页面进行的监听
                    service_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showShare();
                        }
                    });

                    //收藏按钮的监听
                    service_collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isLogin) {
                                //收藏功能接口的调用
                                HttpUtils utils = new HttpUtils();
                                RequestParams params1 = new RequestParams();
                                //params1.addQueryStringParameter("token",login );
                                params1.addBodyParameter("itemID", id);
                                params1.addBodyParameter("type", "4");
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
                                                    Toast.makeText(DetailsFindServiceActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                                                    Drawable drawable01 = getResources().getDrawable(R.mipmap.collect_un);
                                                    /// 这一步必须要做,否则不会显示.
                                                    drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
                                                    service_collect.setCompoundDrawables(null, drawable01, null, null);
                                                    break;
                                                case "收藏成功！":
                                                    Toast.makeText(DetailsFindServiceActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
                                                    Drawable drawable02 = getResources().getDrawable(R.mipmap.collect);
                                                    /// 这一步必须要做,否则不会显示.
                                                    drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
                                                    service_collect.setCompoundDrawables(null, drawable02, null, null);
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
                                        ToastUtils.shortToast(DetailsFindServiceActivity.this, "收藏失败");
                                    }
                                });
                            } else {
                                Intent intent = new Intent(DetailsFindServiceActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                    //进入私聊的页面
                    service_sendMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (isLogin) {

                                //启动会话界面
                                if (RongIM.getInstance() != null)
                                    RongIM.getInstance().startPrivateChat(DetailsFindServiceActivity.this, UserID, "聊天详情");
                            } else {
                                Intent intent1 = new Intent(DetailsFindServiceActivity.this, LoginActivity.class);
                                startActivity(intent1);
                            }

                        }
                    });
                    //对信息完整度的一个设定,默认五颗星。
                    //service_rating.setRating((float) 4.5);

                    service_details_two.setText(ServiceName);
                    service_details_four.setText(ServiceLocation);
                    service_details_six.setText(ServiceLevel);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                if (dialog!=null){
                    dialog.dismiss();
                }
            }
        });

    }

    public void onResume() {
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
        oks.setTitle("资芽");
        oks.setTitleUrl("http://www.ziyawang.com");
        oks.setImageUrl("http://images.ziyawang.com/Applogo/logo.png");
        oks.setText(ServiceName);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url.ShareService + id);
        // 启动分享GUI
        oks.show(this);
    }

    private void initView() {

        service_scroll = (ScrollView) findViewById(R.id.service_scroll);
        pre = (RelativeLayout) findViewById(R.id.pre);
        service_icon = (ImageView) findViewById(R.id.service_icon);
        service_title = (TextView) findViewById(R.id.service_title);

        service_type = (TextView) findViewById(R.id.service_type);
        service_no = (TextView) findViewById(R.id.service_no);
        //service_rating = (RatingBar)findViewById(R.id.service_rating ) ;

        service_collect = (TextView) findViewById(R.id.service_collect);
        service_share = (TextView) findViewById(R.id.service_share);
        service_details_two = (TextView) findViewById(R.id.service_details_two);
        service_details_four = (TextView) findViewById(R.id.service_details_four);
        service_details_six = (TextView) findViewById(R.id.service_details_six);

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

    }
}
