package com.ziyawang.ziya.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.VideoCommentsAdapter;
import com.ziyawang.ziya.entity.VideoCommentsEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.NotificationButton;
import com.ziyawang.ziya.view.NotificationButton02;
import com.ziyawang.ziya.view.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class DetailsNewsActivity extends BenBenActivity implements View.OnClickListener {
    private TextView text_head ;
    private NotificationButton02 button_up ;
    private TextView write ;
    //滑动布局
    private ScrollView scrollView ;
    //新闻详情的描述
    private WebView details_news_des ;
    //回退事件
    private RelativeLayout pre ;
    //title
    private TextView news_title ;
    //来源
    private TextView news_from ;
    //时间
    private TextView news_time ;
    //收藏
    private RelativeLayout relative_collect ;
    private ImageView news_collect ;
    //分享
    private RelativeLayout relative_share ;
    //摘要
    private TextView news_brief ;

    private static String newsContent ;

    private RelativeLayout ben_height ;

    private static String news_id ;
    private static String title ;
    private static String brief ;
    private static String head ;
    private BenListView listView ;
    private VideoCommentsAdapter adapter ;

    private TextView niu_top ;
    private TextView textView_show_noData ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_details_news);
    }

    @Override
    public void initViews() {
        text_head = (TextView)findViewById(R.id.head ) ;
        button_up = (NotificationButton02)findViewById(R.id.button_up ) ;
        write = (TextView)findViewById(R.id.write ) ;
        ben_height = (RelativeLayout)findViewById(R.id.ben_height ) ;
        scrollView = (ScrollView)findViewById(R.id.scrollView ) ;
        details_news_des = (WebView)findViewById(R.id.details_news_des ) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        news_title = (TextView)findViewById(R.id.news_title ) ;
        news_from = (TextView)findViewById(R.id.news_from ) ;
        news_time = (TextView)findViewById(R.id.news_time ) ;
        relative_collect = (RelativeLayout)findViewById(R.id.relative_collect ) ;
        relative_share = (RelativeLayout)findViewById(R.id.relative_share ) ;
        news_collect = (ImageView)findViewById(R.id.news_collect ) ;
        news_brief = (TextView)findViewById(R.id.news_brief ) ;
        niu_top = (TextView)findViewById(R.id.niu_top ) ;
        textView_show_noData = (TextView)findViewById(R.id.textView_show_noData ) ;
        listView = (BenListView)findViewById(R.id.listView ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        relative_collect.setOnClickListener(this);
        relative_share.setOnClickListener(this);
        write.setOnClickListener(this);
        button_up.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent =getIntent() ;
        news_id = intent.getStringExtra("id");
        if (!TextUtils.isEmpty(intent.getStringExtra("type"))){
            head = intent.getStringExtra("type");
            text_head.setText(head);
            switch (head){
                case "处置公告" :
                    String author = intent.getStringExtra("author");
                    news_from.setText("来源：" + author.trim());
                    break;
                default:
                    break;
            }
        }
        loadData(news_id) ;
        loadComment(news_id) ;
    }

    private void loadComment(String id) {
        HttpUtils utils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addQueryStringParameter("NewsID", id);
        utils.configCurrentHttpCacheExpiry(1000) ;
        utils.send(HttpRequest.HttpMethod.GET, Url.NewsCommentList, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result);

                com.alibaba.fastjson.JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");
                List<VideoCommentsEntity> list = JSON.parseArray(result.toJSONString(), VideoCommentsEntity.class);

                adapter = new VideoCommentsAdapter(DetailsNewsActivity.this, list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                niu_top.setText("热门评论(" + list.size() + ")");
                button_up.setNotificationNumber(list.size());
                if (list.size() == 0) {
                    textView_show_noData.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    textView_show_noData.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        }) ;
    }

    private void loadData(String id) {
        String urls ;
        if (GetBenSharedPreferences.getIsLogin(this)){
            urls = String.format(Url.DetailsNews, id, GetBenSharedPreferences.getTicket(this));
        }else{
            urls = String.format(Url.DetailsNews, id, "");
        }

        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        httpUtils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    newsContent = data.getString("NewsContent");
                    String html = "<head><style>body{max-width:94%important;margin:0 auto;overflow:hidden!important;}img{max-width:320px !important;height:auto!important;margin:0 auto;width:100%!important;display:block;}</style></head>" + "<body>" + newsContent + "</body>";
                    //details_news_des.setText(Html.fromHtml(newsContent, new URLImageParser(details_news_des), null)  );
                    WebSettings ws = details_news_des.getSettings();
                    //ws.setJavaScriptEnabled(false);
                    //ws.setAllowFileAccess(true);
                    //ws.setBuiltInZoomControls(false);
                    //ws.setSupportZoom(false);
                    //ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                    //ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    //ws.setDefaultTextEncodingName("utf-8"); //设置文本编码
                    //ws.setAppCacheEnabled(true);
                    //ws.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式</span>
                    ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                    details_news_des.setVerticalScrollBarEnabled(false);
                    details_news_des.loadDataWithBaseURL(null, html, "text/html", "unicode", null);

                    news_title.setText(data.getString("NewsTitle"));
                    String publishTime = data.getString("PublishTime");
                    String substring01 = publishTime.substring(0, 10);
                    news_time.setText("时间：" + substring01 );
                    news_brief.setText(data.getString("Brief"));
                    String collectFlag = data.getString("CollectFlag");
                    switch (collectFlag){
                        case "1" :
                            news_collect.setImageResource(R.mipmap.v2shouc);
                            break;
                        default:
                            news_collect.setImageResource(R.mipmap.v2shoucang);
                            break;
                    }
                    news_id = data.getString("NewsID");
                    title = data.getString("NewsTitle");
                    brief = data.getString("Brief");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(DetailsNewsActivity.this, "网络连接异常");
            }
        }) ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.relative_share :
                goShare() ;
                break;
            case R.id.relative_collect :
                goCollect();
                break;
            case R.id.write :
                showCommitWindow() ;
                break;
            case R.id.button_up :
                scrollView.smoothScrollTo(0, news_title.getHeight() + ben_height.getHeight() + 120 + details_news_des.getHeight());
                break;
            default:
                break;
        }
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
                goSubmit(editText, window);
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
        window.showAtLocation(scrollView, Gravity.BOTTOM, 0, 0);
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

    private void goSubmit(final EditText editText, final PopupWindow window) {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())){
            String urls ;
            if (GetBenSharedPreferences.getIsLogin(this)){
                urls = String.format(Url.NewsCommentSend, GetBenSharedPreferences.getTicket(this));
            }else{
                urls = String.format(Url.NewsCommentSend, "");
            }
            HttpUtils httpUtils = new HttpUtils() ;
            RequestParams params = new RequestParams() ;
            params.addBodyParameter("NewsID" , news_id );
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
                                ToastUtils.shortToast(DetailsNewsActivity.this, "评论发表成功");
                                loadComment(news_id);
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
                    ToastUtils.shortToast(DetailsNewsActivity.this, "网络连接异常");
                }
            }) ;
        }else {
            ToastUtils.shortToast(DetailsNewsActivity.this , "请输入您的评论");
        }
    }

    private void goCollect() {
        if (GetBenSharedPreferences.getIsLogin(this)) {
            //收藏功能接口的调用
            goLoadCollect() ;
        } else {
            //未登录，跳转到登陆页面
            goLoginActivity();
        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent( DetailsNewsActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goLoadCollect() {
        HttpUtils utils = new HttpUtils();
        RequestParams params1 = new RequestParams();
        params1.addBodyParameter("itemID", news_id );
        params1.addBodyParameter("type", "3");
        String a = String.format(Url.Collect, GetBenSharedPreferences.getTicket(this));
        utils.send(HttpRequest.HttpMethod.POST, a, params1, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benbne", responseInfo.result);
                dealCollectResult(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(DetailsNewsActivity.this, "网络连接异常");
            }
        });
    }

    private void dealCollectResult(String result) {
        try {
            JSONObject object = new JSONObject(result);
            String msg = object.getString("msg");
            switch (msg) {
                case "取消收藏成功！":
                    ToastUtils.shortToast(DetailsNewsActivity.this, "取消收藏");
                    news_collect.setImageResource(R.mipmap.v2shoucang);
                    break;
                case "收藏成功！":
                    ToastUtils.shortToast(DetailsNewsActivity.this, "收藏成功");
                    news_collect.setImageResource(R.mipmap.v2shouc);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void goShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitleUrl(Url.ShareNews + news_id);
        oks.setTitle(title);
        oks.setImageUrl("http://images.ziyawang.com/Applogo/logo.png");
        oks.setText(brief);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Url.ShareNews + news_id);
        // 启动分享GUI
        oks.show(this);
    }
}
