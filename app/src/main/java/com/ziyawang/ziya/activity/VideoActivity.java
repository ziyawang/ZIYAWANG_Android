package com.ziyawang.ziya.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.VideoCommentsAdapter;
import com.ziyawang.ziya.application.MyApplication;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.entity.VideoCommentsEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.NotificationButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class VideoActivity extends Activity implements View.OnClickListener {
    private MyApplication app;
    private SuperVideoPlayer mSuperVideoPlayer;
    private View mPlayBtnView;
    private RelativeLayout pre ;
    private FrameLayout video_frame ;
    private static String url ;
    private RelativeLayout details_release_head ;
    private TextView movie_title ;
    TextView movie_look ;
    TextView movie_time ;
    TextView movie_des ;
    private RelativeLayout relative_commit ;
    private RelativeLayout relative_collect ;
    private RelativeLayout relative_share ;
    private ImageView movie_collect ;
    FindVideoEntity findVideoEntity  ;
    private Boolean isLogin ;
    private static String id ;
    private String login ;
    private VideoCommentsAdapter adapter ;
    private ListView listView  ;
    private MyProgressDialog dialog  ;
    private TextView niu_top ;
    private TextView textView_show_noData ;


    public void onResume() {
        //获得用户的ticket
        login = GetBenSharedPreferences.getTicket(this);
        //获得用户的isLogin
        isLogin = GetBenSharedPreferences.getIsLogin(this) ;
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("视频详情页面");
        //统计时长
        MobclickAgent.onResume(this);
        //播放器播放的视频重新播放
        mSuperVideoPlayer.goOnPlay();
    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("视频详情页面");
        //统计时长
        MobclickAgent.onPause(this);
        //当activity暂停时，播放器播放的视频暂停播放
        mSuperVideoPlayer.pausePlay(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        //获得activity的Task路径
        getActivityTask() ;
        //获得数据
        initData() ;
        //实例化组件
        initView();
        //加载数据
        loadData(id);
        //加载评论
        loadComment() ;
        //回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //发送评论的按钮
        relative_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommitWindow() ;
            }
        });

    }

    private void showCommitWindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_comment, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final EditText editText = (EditText)view.findViewById(R.id.editText);
        final TextView text_commit = (TextView)view.findViewById(R.id.text_commit ) ;
        text_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComments(editText, window);
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }

        }, 100);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())){
                    text_commit.setSelected(true);
                }else {
                    text_commit.setSelected(false);
                }
            }
        });
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.setFocusable(true);
        window.showAtLocation(details_release_head, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        isLogin = GetBenSharedPreferences.getIsLogin(this) ;
        login = GetBenSharedPreferences.getTicket(this) ;
    }

    private void getActivityTask() {
        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);
    }

    private void sendComments(EditText editText, final PopupWindow window) {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())){
            String urls = String.format(Url.VideoCommentSend, login ) ;
            HttpUtils httpUtils = new HttpUtils() ;
            RequestParams params = new RequestParams() ;
            params.addBodyParameter("VideoID" , id );
            params.addBodyParameter("Content" , editText.getText().toString().trim() );
            httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("benben" , responseInfo.result ) ;
                    try {
                        JSONObject jsonObject = new JSONObject(responseInfo.result) ;
                        String status_code = jsonObject.getString("status_code");
                        switch (status_code){
                            case "200" :
                                ToastUtils.shortToast(VideoActivity.this, "评论发表成功");
                                loadComment();
                                window.dismiss();
                                break;
                            default:
                                break;
                        }
                    } catch (JSONException e) {
                    }
                }
                @Override
                public void onFailure(HttpException error, String msg) {
                    error.printStackTrace();
                    ToastUtils.shortToast(VideoActivity.this, "网络连接异常");
                }
            }) ;
        }else {
            ToastUtils.shortToast(VideoActivity.this , "请输入您的评论");
        }
    }

    private void loadComment() {

        HttpUtils utils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("VideoID", id);
        utils.configCurrentHttpCacheExpiry(1000) ;
        utils.send(HttpRequest.HttpMethod.GET, Url.VideoCommentList, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result) ;

                com.alibaba.fastjson.JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");
                List<VideoCommentsEntity> list = JSON.parseArray(result.toJSONString(), VideoCommentsEntity.class);

                adapter = new VideoCommentsAdapter(VideoActivity.this , list ) ;
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                niu_top.setText("热门评论(" + list.size() + ")");
                if (list.size()==0){
                    textView_show_noData.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE );
                }else {
                    textView_show_noData.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE );
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
             }
        }) ;

    }

    private void loadData(final String id) {
        //在开始进行网络连接时显示进度条对话框
        showBenDialog() ;
        final String urls = String.format(Url.Details_movie, id  ,login ) ;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("access_token", "token");
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //隐藏dialog
                hiddenDialog() ;
                //数据处理
                dealResult(responseInfo.result) ;
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //打印失败回调
                error.printStackTrace();
                //通知用户
                ToastUtils.shortToast(VideoActivity.this, "网络连接失败");
                //隐藏dialog
                hiddenDialog() ;
            }
        });
    }

    private void hiddenDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void dealResult(String result) {
        Log.e("benbne", result);
        findVideoEntity = JSON.parseObject(result, FindVideoEntity.class);
        String niu = findVideoEntity.getVideoLink2();
        if (!niu.equals("")) {
            url = Url.FileIPVideos + niu;
        } else {
            url = Url.FileIPVideos + findVideoEntity.getVideoLink();
        }
        BitmapUtils bitmapUtils = new BitmapUtils(VideoActivity.this);
        bitmapUtils.display(video_frame, Url.FileIP + findVideoEntity.getVideoLogo());
        //显示数据
        movie_title.setText(findVideoEntity.getVideoTitle());
        movie_look.setText(findVideoEntity.getViewCount() + "次");
        movie_time.setText(findVideoEntity.getPublishTime());
        movie_des.setText("简介：" + findVideoEntity.getVideoDes());
        switch (findVideoEntity.getCollectFlag()) {
            case "0":
                movie_collect.setImageResource(R.mipmap.v2shoucang);
                break;
            case "1":
                movie_collect.setImageResource(R.mipmap.v2shouc);
                break;
            default:
                break;
        }

        //分享按钮
        relative_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        //收藏按钮的监听
        relative_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    //收藏功能接口的调用
                    HttpUtils utils = new HttpUtils();
                    RequestParams params1 = new RequestParams();
                    params1.addBodyParameter("itemID", id);
                    params1.addBodyParameter("type", "2");
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
                                        Toast.makeText(VideoActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                                        movie_collect.setImageResource(R.mipmap.v2shoucang);
                                        break;
                                    case "收藏成功！":
                                        Toast.makeText(VideoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                        movie_collect.setImageResource(R.mipmap.v2shouc);
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
                            ToastUtils.shortToast(VideoActivity.this, "网络连接异常");
                        }
                    });
                } else {
                    Intent intent = new Intent(VideoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void showBenDialog() {
        dialog = new MyProgressDialog(VideoActivity.this , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitleUrl(Url.ShareVideo + findVideoEntity.getVideoID());


        String shareTitle ;
        if (findVideoEntity.getVideoTitle().length() >= 60){
            shareTitle = findVideoEntity.getVideoTitle().substring( 0 , 60 ) ;
        }else{
            shareTitle = findVideoEntity.getVideoTitle() ;
        }

        String shareText ;
        if (findVideoEntity.getVideoDes().length() >= 60){
            shareText = findVideoEntity.getVideoDes().substring( 0 , 60 ) ;
        }else{
            shareText = findVideoEntity.getVideoDes() ;
        }




        oks.setTitle(shareTitle);
        oks.setImageUrl("http://images.ziyawang.com/Applogo/logo.png");
        oks.setText(shareText);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url.ShareVideo + findVideoEntity.getVideoID());
        // 启动分享GUI
        oks.show(this);
    }

    private void initView() {
        mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
        mPlayBtnView = findViewById(R.id.play_btn);
        mPlayBtnView.setOnClickListener(this);
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        video_frame = (FrameLayout)findViewById(R.id.video_frame ) ;
        pre = (RelativeLayout)findViewById(R.id.pre) ;
        details_release_head = (RelativeLayout)findViewById(R.id.details_release_head) ;
        movie_title = (TextView) findViewById(R.id.movie_title) ;
        movie_look = (TextView) findViewById(R.id.movie_look) ;
        movie_time = (TextView) findViewById(R.id.movie_time) ;
        movie_des = (TextView) findViewById(R.id.movie_des) ;
        relative_share = (RelativeLayout)findViewById(R.id.relative_share ) ;
        relative_collect = (RelativeLayout)findViewById(R.id.relative_collect ) ;
        movie_collect = (ImageView) findViewById(R.id.movie_collect) ;
        relative_commit = (RelativeLayout) findViewById(R.id.relative_commit) ;
        listView = (ListView)findViewById(R.id.listView) ;
        listView.setDivider(new ColorDrawable(Color.argb(0, 244, 244, 244)));
        listView.setDividerHeight(1);
        niu_top = (TextView)findViewById(R.id.niu_top ) ;
        textView_show_noData = (TextView)findViewById(R.id.textView_show_noData ) ;
    }
    /**
     * 播放器的回调函数
     */
    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        /**
         * 播放器关闭按钮回调
         */
        @Override
        public void onCloseVideo() {
            mSuperVideoPlayer.close();//关闭VideoView
            mPlayBtnView.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.setVisibility(View.GONE);
            resetPageToPortrait();
        }
        /**
         * 播放器横竖屏切换回调
         */
        @Override
        public void onSwitchPageType() {

            //判断此时的
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                //横屏的状态
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
            } else {
                //竖屏的状态
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
            }
        }
        /**
         * 播放完成回调
         */
        @Override
        public void onPlayFinish() {}
    };

    @Override
    public void onClick(View view) {
        mPlayBtnView.setVisibility(View.GONE);
        mSuperVideoPlayer.setVisibility(View.VISIBLE);
        mSuperVideoPlayer.setAutoHideController(false);
        Uri uri = Uri.parse(url);
        mSuperVideoPlayer.loadAndPlay(uri, 0);
    }
    /***
     * 旋转屏幕之后回调
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null == mSuperVideoPlayer) return;
        /***
         * 根据屏幕方向重新设置播放器的大小
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            mSuperVideoPlayer.getLayoutParams().height = (int) width;
            mSuperVideoPlayer.getLayoutParams().width = (int) height;
            details_release_head.setVisibility(View.GONE);
            relative_commit.setVisibility(View.GONE);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            mSuperVideoPlayer.getLayoutParams().height = (int) height;
            mSuperVideoPlayer.getLayoutParams().width = (int) width;
            details_release_head.setVisibility(View.VISIBLE);
            relative_commit.setVisibility(View.VISIBLE);
        }
    }
    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
        }
    }

}

