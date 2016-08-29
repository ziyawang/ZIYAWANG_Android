package com.ziyawang.ziya.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mob.tools.utils.UIHandler;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.CheckCache;
import com.ziyawang.ziya.tools.DownLoadManager;
import com.ziyawang.ziya.tools.NetUtils;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import io.rong.imkit.RongIM;

public class MySetActivity extends BaseActivity implements PlatformActionListener, Handler.Callback {

    private RelativeLayout pre ;
    private RelativeLayout set_clean_relative ;
    private TextView set_clean_text ;
    private TextView set_ziya_rule ;
    private TextView set_ziya_share ;
    private String totalCacheSize ;

    private SharedPreferences loginCode ;

    private SharedPreferences sharedPreferences ;
    private SharedPreferences myNumber ;
    private SharedPreferences role ;
    private SharedPreferences r_token ;

    private TextView logout ;

    private TextView app_check ;
    private TextView app_info ;

    private final  static int SHARE_SUCCESS = 1;
    private final  static int SHARE_CANCEL = 2;
    private final  static int SHARE_ERROR = 3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_set);
        //实例化组件
        initView() ;

        SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);


        if (isLogin){
            logout.setVisibility(View.VISIBLE);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    r_token = getSharedPreferences("r_token", MODE_PRIVATE);
                    loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
                    sharedPreferences = getSharedPreferences("isLogin", MODE_PRIVATE);
                    myNumber = getSharedPreferences("myNumber", MODE_PRIVATE);
                    role = getSharedPreferences("role", MODE_PRIVATE);

                    //将ticket的值擦除
                    r_token.edit().putString("r_token", null).commit();
                    //sharedPreferences.edit().putBoolean("isLogin", false).commit();
                    loginCode.edit().putString("loginCode", null).commit();
                    myNumber.edit().putString("myNumber", null).commit();
                    role.edit().putString("role", null).commit();

                    SharedPreferences isLogin = getSharedPreferences("isLogin" , MODE_PRIVATE);
                    isLogin.edit().putBoolean("isLogin", false).commit();

                    MobclickAgent.onProfileSignOff() ;

                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().logout();
                    }

                    Intent intent = new Intent(MySetActivity.this, MainActivity.class);
                    startActivity(intent);

                    ToastUtils.shortToast(MySetActivity.this , "退出登录成功");

                }
            });
        }

        try {
            totalCacheSize = CheckCache.getTotalCacheSize(MySetActivity.this);
            set_clean_text.setText("" + totalCacheSize);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //添加回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //对资芽公约的监听事件
        set_ziya_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySetActivity.this , MyRuleActivity.class ) ;
                startActivity(intent);
            }
        });
        //把资芽推荐给朋友
        set_ziya_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare() ;
            }
        });
        //清除缓存
        set_clean_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalCacheSize.equals("0K")){
                    Toast.makeText(MySetActivity.this, "已经清理至最佳状态", Toast.LENGTH_SHORT).show();
                }else {
                    CheckCache.clearAllCache(MySetActivity.this);
                    Toast.makeText(MySetActivity.this, "缓存已清空", Toast.LENGTH_SHORT).show();
                    try {
                        totalCacheSize = CheckCache.getTotalCacheSize(MySetActivity.this);
                        set_clean_text.setText("" + totalCacheSize);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        //版本信息
        app_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取packagemanager的实例
                PackageManager packageManager = getPackageManager();
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                PackageInfo packInfo = null;
                try {
                    packInfo = packageManager.getPackageInfo(getPackageName(),0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
//                final int versionCode = packInfo.versionCode;
//                Toast.makeText(SoftwareInformationActivity.this, ""+versionCode, Toast.LENGTH_SHORT).show();
                String version = packInfo.versionName;

                final CustomDialog.Builder builder = new CustomDialog.Builder(MySetActivity.this);
                builder.setTitle("www.ziyawang.com");
                builder.setMessage("当前版本:  " + version + "  ,我们将尽我们最大的努力，提供给您最优质的体验。");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });
        //检查更新
        app_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtils.shortToast(MySetActivity.this , "已经是最新版本");
                HttpUtils utils = new HttpUtils() ;
                RequestParams params = new RequestParams() ;
                utils.send(HttpRequest.HttpMethod.GET, Url.CheckUpdata, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("benben" , responseInfo.result ) ;
                        try {
                            //JSONObject jsonObject = new JSONObject(responseInfo.result) ;

                            JSONArray array = new JSONArray(responseInfo.result) ;
                            JSONObject jsonObject = array.getJSONObject(0);
                            String versionCode = jsonObject.getString("VersionCode");
                            String versionName = jsonObject.getString("VersionName");
                            final String UpdateUrl = jsonObject.getString("UpdateUrl");
                            String UpdateTitle = jsonObject.getString("UpdateTitle");
                            String UpdateDes = jsonObject.getString("UpdateDes");

                            int num_web = Integer.parseInt(versionCode);
                            // 获取packagemanager的实例
                            PackageManager packageManager = getPackageManager();
                            // getPackageName()是你当前类的包名，0代表是获取版本信息
                            PackageInfo packInfo = null;
                            try {
                                packInfo = packageManager.getPackageInfo(getPackageName(),0);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            //String version = packInfo.versionName;
                            final int num_local = packInfo.versionCode;

                            if (num_web > num_local){
                                final CustomDialog.Builder builder = new CustomDialog.Builder(MySetActivity.this);
                                builder.setTitle(UpdateTitle);
                                builder.setMessage(UpdateDes);
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        int networkType = NetUtils.getNetworkType(MySetActivity.this);
                                        switch (networkType){
                                            case NetUtils.NETTYPE_CMNET :
                                            case NetUtils.NETTYPE_CMWAP:
                                                ToastUtils.shortToast(MySetActivity.this , "为了节约您的流量，请连接wifi后下载");
                                                break;
                                            case NetUtils.NETTYPE_WIFI:

                                                final ProgressDialog pd;    //进度条对话框
                                                pd = new  ProgressDialog(MySetActivity.this);
                                                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                pd.setMessage("正在下载更新");
                                                pd.setCancelable(true);//设置进度条是否可以按退回键取消

                                                //设置点击进度对话框外的区域对话框不消失
                                                pd.setCanceledOnTouchOutside(false) ;
                                                pd.show();
                                                new Thread(){
                                                @Override
                                                public void run() {
                                                    try {
                                                        File file = DownLoadManager.getFileFromServer(Url.FileIP + UpdateUrl, pd);
                                                        sleep(3000);
                                                        installApk(file);
                                                        pd.dismiss(); //结束掉进度条对话框
                                                        } catch (Exception e) {
                                                        e.printStackTrace();
                                                        }
                                                    }}.start();
                                                break;
                                        }
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }) ;
                                builder.create().show();
                            }else {
                                ToastUtils.shortToast(MySetActivity.this , "已经是最新版本");
                            }
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
        });
    }

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("设置页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("抢单页面");
        //统计时长
        MobclickAgent.onPause(this);
    }


    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void initView() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        set_clean_relative = (RelativeLayout)findViewById(R.id.set_clean_relative ) ;
        set_clean_text = (TextView)findViewById(R.id.set_clean_text ) ;
        set_ziya_rule = (TextView)findViewById(R.id.set_ziya_rule ) ;
        set_ziya_share = (TextView)findViewById(R.id.set_ziya_share) ;
        logout = (TextView)findViewById(R.id.logout) ;

        app_info = (TextView)findViewById(R.id.app_info ) ;
        app_check = (TextView)findViewById(R.id.app_check ) ;
    }


    private void showShare() {

        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle("资芽");
        //应用宝的官网的资芽的位置
        //oks.setTitleUrl("http://android.myapp.com/myapp/detail.htm?apkName=com.ziyawang.ziya");
        oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.ziyawang.ziya");
        oks.setImageUrl("http://images.ziyawang.com/Applogo/logo.png") ;
        oks.setText("资芽网正式上线啦，小伙伴们赶快加入吧。");
        // url仅在微信（包括好友和朋友圈）中使用
        //oks.setUrl("http://android.myapp.com/myapp/detail.htm?apkName=com.ziyawang.ziya" );
        oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.ziyawang.ziya" );
        // 启动分享GUI
        oks.show(this);

    }

    @Override
    public void onCancel(Platform platf, int arg1) {
        Log.i("", "onCancel");
        if(arg1 == Platform.ACTION_SHARE){
            UIHandler.sendEmptyMessage(SHARE_CANCEL, this );
        }
    }

    @Override
    public void onComplete(Platform platf, int arg1,
                           HashMap<String, Object> arg2) {
        Log.i("", "onComplete");
        if(arg1 == Platform.ACTION_SHARE){
            UIHandler.sendEmptyMessage(SHARE_SUCCESS, this);
            Log.i("", "响应分享事件");
        }
        if(arg1 == Platform.SHARE_TEXT){
            Log.i("", "响应分享文本事件");
            UIHandler.sendEmptyMessage(SHARE_SUCCESS, this);
            Log.i("", "..");
        }
    }

    @Override
    public void onError(Platform platf, int arg1, Throwable arg2) {
        // TODO Auto-generated method stub
        Log.i("", "onError");
        if(arg1 == Platform.ACTION_SHARE){
            UIHandler.sendEmptyMessage(SHARE_ERROR, this);
        }
    }
    @Override
    public boolean handleMessage(Message msg) {
        Log.i("", "what = " + msg.what);
        switch(msg.what) {
            case SHARE_SUCCESS: {
                Log.i("", "arg1 = " + "分享成功");
                Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
            }
            break;
            case SHARE_CANCEL: {
                Log.i("", "arg1 = " + "取消分享");
                Toast.makeText(this, "取消分享", Toast.LENGTH_SHORT).show();
            }
            break;
            case SHARE_ERROR: {
                Log.i("", "arg1 = " + "分享错误");
                Toast.makeText(this, "分享错误", Toast.LENGTH_SHORT).show();
            }
            break;

        }

        return false;
    }


}
