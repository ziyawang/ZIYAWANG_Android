package com.ziyawang.ziya.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BitmapHelp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailsStarRegisterActivity extends Activity implements View.OnClickListener {
    public static BitmapUtils bitmapUtils;
    //返回按钮
    private RelativeLayout pre ;
    private ImageView image_01 , image_02 , image_03 , image_04 , image_05 ;
    private TextView text_01 , text_02 , text_03 , text_04 , text_05 ;
    //视频认证开始按钮
    private View mPlayBtnView;
    //视频播放
    private SuperVideoPlayer mSuperVideoPlayer;
    //视频源
    private static String url ;

    private RelativeLayout details_service_head ;
    private LinearLayout top ;
    private LinearLayout down ;

    //实地认证
    private ImageView sd_img01 , sd_img02 , sd_img03 ;
    //承诺书认证
    private ImageView cns_img01 , cns_img02 , cns_img03 ;
    //三证认证
    private ImageView sz_img01 , sz_img02 , sz_img03 ;

    private LinearLayout linear_sd ;
    private FrameLayout video_frame ;
    private LinearLayout linear_cns ;
    private LinearLayout linear_sz ;

    private String sd_pic01 ;
    private String sd_pic02 ;
    private String sd_pic03 ;

    private String cns_pic01 ;

    private String sz_pic01 ;
    private String sz_pic02 ;
    private String sz_pic03 ;

    private String sd_num ;
    private String sz_num ;


    @Override
    protected void onResume() {
        super.onResume();
        //播放器播放的视频重新播放
        mSuperVideoPlayer.goOnPlay();
    }

    public void onPause() {
        super.onPause();
        //当activity暂停时，播放器播放的视频暂停播放
        mSuperVideoPlayer.pausePlay(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 定制流程
        setContentView();
        initViews();
        initListeners();
        initData();
    }

    public void setContentView() {
        setContentView(R.layout.activity_details_star_register);
    }

    public void initViews() {
        sd_img01 = (ImageView)findViewById(R.id.sd_img01 ) ;
        sd_img02 = (ImageView)findViewById(R.id.sd_img02 ) ;
        sd_img03 = (ImageView)findViewById(R.id.sd_img03 ) ;

        cns_img01 = (ImageView)findViewById(R.id.cns_img01 ) ;
        cns_img02 = (ImageView)findViewById(R.id.cns_img02 ) ;
        cns_img03 = (ImageView)findViewById(R.id.cns_img03 ) ;

        sz_img01 = (ImageView)findViewById(R.id.sz_img01 ) ;
        sz_img02 = (ImageView)findViewById(R.id.sz_img02 ) ;
        sz_img03 = (ImageView)findViewById(R.id.sz_img03 ) ;

        pre =  (RelativeLayout)findViewById(R.id.pre ) ;
        image_01 =  (ImageView)findViewById(R.id.image_01 ) ;
        image_02 =  (ImageView)findViewById(R.id.image_02 ) ;
        image_03 =  (ImageView)findViewById(R.id.image_03 ) ;
        image_04 =  (ImageView)findViewById(R.id.image_04 ) ;
        image_05 =  (ImageView)findViewById(R.id.image_05 ) ;
        text_01 =  (TextView)findViewById(R.id.text_01 ) ;
        text_02 =  (TextView)findViewById(R.id.text_02 ) ;
        text_03 =  (TextView)findViewById(R.id.text_03 ) ;
        text_04 =  (TextView)findViewById(R.id.text_04 ) ;
        text_05 =  (TextView)findViewById(R.id.text_05 ) ;
        down =  (LinearLayout)findViewById(R.id.down ) ;
        top =  (LinearLayout)findViewById(R.id.top ) ;
        details_service_head =  (RelativeLayout)findViewById(R.id.details_service_head ) ;
        mPlayBtnView = findViewById(R.id.play_btn);
        mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);

        // 获取bitmapUtils单例
        bitmapUtils = BitmapHelp.getBitmapUtils(this);
        /**
         * 设置默认的图片展现、加载失败的图片展现
         */
        bitmapUtils.configDefaultLoadingImage(R.mipmap.fast_error);
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.error_imgs_big);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

        linear_sd = (LinearLayout)findViewById(R.id.linear_sd ) ;
        video_frame = (FrameLayout)findViewById(R.id.video_frame ) ;
        linear_cns = (LinearLayout)findViewById(R.id.linear_cns ) ;
        linear_sz = (LinearLayout)findViewById(R.id.linear_sz ) ;
    }

    public void initListeners() {
        pre.setOnClickListener(this);
    }

    public void initData() {
        Intent intent =getIntent() ;
        String id = intent.getStringExtra("id");
        loadData(id) ;
    }

    private void loadData(final String id) {
        //开启网络请求
        String urls = String.format(Url.Details_service, id, GetBenSharedPreferences.getTicket(this));
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("access_token", "token");
        utils.configCurrentHttpCacheExpiry(1000);
        utils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result);
                //成功接口的回调
                dealResult(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //打印错误信息
                error.printStackTrace();
            }
        });

    }

    private void dealResult(String result) {
        //数据解析
        try {
            JSONObject object = new JSONObject(result);
            String level = object.getString("Level");
            url = object.getString("starvideo");
            //加载认证的类型
            loadLevel(level, object) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadsz(JSONObject object) throws JSONException {
        JSONArray starsz = object.getJSONArray("starsz");
        if (starsz.length() == 1){
            bitmapUtils.display( sz_img01, starsz.getString(0) );
            sz_pic01 = starsz.getString(0)  ;
            sz_num = "1" ;
            sz_img01.setOnClickListener(this);
        }else if (starsz.length() == 2){
            bitmapUtils.display(sz_img01, starsz.getString(0));
            bitmapUtils.display( sz_img02, starsz.getString(1) );
            sz_pic01 = starsz.getString(0)  ;
            sz_pic02 = starsz.getString(1)  ;
            sz_num = "2" ;
            sz_img01.setOnClickListener(this);
            sz_img02.setOnClickListener(this);
        }else if (starsz.length() == 3) {
            bitmapUtils.display( sz_img01, starsz.getString(0) );
            bitmapUtils.display( sz_img02, starsz.getString(1) );
            bitmapUtils.display( sz_img03, starsz.getString(2) );
            sz_pic01 = starsz.getString(0)  ;
            sz_pic02 = starsz.getString(1)  ;
            sz_pic03 = starsz.getString(2)  ;
            sz_num = "3" ;
            sz_img01.setOnClickListener(this);
            sz_img02.setOnClickListener(this);
            sz_img03.setOnClickListener(this);
        }
    }

    private void loadcns(JSONObject object) throws JSONException {
        String starcns = object.getString("starcns");
        /**
         * display参数 (ImageView container, String uri,
         * BitmapLoadCallBack<ImageView> callBack)
         */
        bitmapUtils.display( cns_img01, starcns );
        cns_pic01 = starcns ;
        cns_img01.setOnClickListener(this);
    }

    private void loadsd(JSONObject object) throws JSONException {
        JSONArray starsd = object.getJSONArray("starsd");
        if (starsd.length() == 1){
            bitmapUtils.display(sd_img01, starsd.getString(0));
            sd_pic01 = starsd.getString(0) ;
            sd_num = "1" ;
            sd_img01.setOnClickListener(this);
        }else if (starsd.length() == 2){
            bitmapUtils.display(sd_img01, starsd.getString(0));
            bitmapUtils.display(sd_img02, starsd.getString(1));
            sd_pic01 = starsd.getString(0) ;
            sd_pic02 = starsd.getString(1) ;
            sd_num = "2" ;
            sd_img01.setOnClickListener(this);
            sd_img02.setOnClickListener(this);
        }else if (starsd.length() == 3) {
            bitmapUtils.display( sd_img01, starsd.getString(0) );
            bitmapUtils.display( sd_img02, starsd.getString(1) );
            bitmapUtils.display( sd_img03, starsd.getString(2) );
            sd_pic01 = starsd.getString(0) ;
            sd_pic02 = starsd.getString(1) ;
            sd_pic03 = starsd.getString(2) ;
            sd_num = "3" ;
            sd_img01.setOnClickListener(this);
            sd_img02.setOnClickListener(this);
            sd_img03.setOnClickListener(this);
        }

    }

    private void loadLevel(String level, JSONObject object) throws JSONException {
        if (!TextUtils.isEmpty(level) ){
            String[] split = level.split(",");
            for (int i = 0; i < split.length; i++) {
                if ("1".equals(split[i].toString())){
                    image_01.setImageResource(R.mipmap.v203_0101);
                    text_01.setText("已认证");
                    text_01.setTextColor(Color.rgb(255,0,0));
                }
                if ("2".equals(split[i].toString())){
                    image_02.setImageResource(R.mipmap.v203_0102);
                    text_02.setText("已认证");
                    text_02.setTextColor(Color.rgb(255, 0, 0));
                    linear_sd.setVisibility(View.VISIBLE);
                    //加载实地认证的图片
                    loadsd(object) ;
                }
                if ("3".equals(split[i].toString()) ){
                    image_03.setImageResource(R.mipmap.v203_0103);
                    text_03.setText("已认证");
                    text_03.setTextColor(Color.rgb(255, 0, 0));
                    video_frame.setVisibility(View.VISIBLE);
                    mPlayBtnView.setOnClickListener(this);
                }
                if ("4".equals(split[i].toString()) ){
                    image_04.setImageResource(R.mipmap.v203_0104);
                    text_04.setText("已认证");
                    text_04.setTextColor(Color.rgb(255, 0, 0));
                    linear_cns.setVisibility(View.VISIBLE);
                    //加载承诺书认证的图片
                    loadcns(object) ;
                }
                if ("5".equals(split[i].toString())){
                    image_05.setImageResource(R.mipmap.v203_0105);
                    text_05.setText("已认证");
                    text_05.setTextColor(Color.rgb(255, 0, 0));
                    linear_sz.setVisibility(View.VISIBLE);
                    //加载三证认证的图片
                    loadsz(object) ;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.play_btn :
                loadVideo() ;
                break;
            case R.id.sd_img01 :
                subData("1", sd_num , "实地") ;
                break;
            case R.id.sd_img02 :
                subData("2", sd_num , "实地") ;
                break;
            case R.id.sd_img03 :
                subData("3", sd_num , "实地") ;
                break;
            case R.id.cns_img01 :
                subData("1", "1" , "承诺书") ;
                break;
            case R.id.sz_img01 :
                subData("1", sz_num , "三证") ;
                break;
            case R.id.sz_img02 :
                subData("2", sz_num , "三证") ;
                break;
            case R.id.sz_img03 :
                subData("3", sz_num , "三证") ;
                break;
            default:
                break;
        }
    }

    private void subData( String index, String pic_num , String type ) {
        Intent intent = new Intent(DetailsStarRegisterActivity.this, ShowImageViewActivity.class);
        intent.putExtra("count" ,index) ;
        intent.putExtra("pic_number", pic_num) ;
        switch (type){
            case "实地" :
                if (Integer.parseInt(pic_num) == 3){
                    intent.putExtra("pic1" ,sd_pic01 ) ;
                    intent.putExtra("pic2" ,sd_pic02 ) ;
                    intent.putExtra("pic3" ,sd_pic03 ) ;
                }else if (Integer.parseInt(pic_num) == 2){
                    intent.putExtra("pic1" ,sd_pic01 ) ;
                    intent.putExtra("pic2" ,sd_pic02 ) ;
                }else if (Integer.parseInt(pic_num) == 1){
                    intent.putExtra("pic1" ,sd_pic01 ) ;
                }
                break;
            case "承诺书" :
                intent.putExtra("pic1" ,cns_pic01 ) ;
                break;
            case "三证" :
                if (Integer.parseInt(pic_num) == 3){
                    intent.putExtra("pic1" ,sz_pic01 ) ;
                    intent.putExtra("pic2" ,sz_pic02 ) ;
                    intent.putExtra("pic3" ,sz_pic03 ) ;
                }else if (Integer.parseInt(pic_num) == 2){
                    intent.putExtra("pic1" ,sz_pic01 ) ;
                    intent.putExtra("pic2" ,sz_pic02 ) ;
                }else if (Integer.parseInt(pic_num) == 1){
                    intent.putExtra("pic1" ,sz_pic01 ) ;
                }
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    private void loadVideo() {
        mPlayBtnView.setVisibility(View.GONE);
        mSuperVideoPlayer.setVisibility(View.VISIBLE);
        mSuperVideoPlayer.setAutoHideController(false);
        Uri uri = Uri.parse(url);
        mSuperVideoPlayer.loadAndPlay(uri, 0);
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
            details_service_head.setVisibility(View.GONE);
            down.setVisibility(View.GONE);
            top.setVisibility(View.GONE);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            mSuperVideoPlayer.getLayoutParams().height = (int) height;
            mSuperVideoPlayer.getLayoutParams().width = (int) width;
            details_service_head.setVisibility(View.VISIBLE);
            down.setVisibility(View.VISIBLE);
            top.setVisibility(View.VISIBLE);
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
