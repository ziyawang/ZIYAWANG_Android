package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.Url;

public class MyRuleActivity extends BaseActivity {

    private RelativeLayout pre;
    private WebView webView;
    private ProgressBar bar;

    private TextView info_title ;

    private String web ;

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("资芽公约页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("资芽公约页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rule);

        //实例化组件
        initView();
        //回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent() ;
        String type = intent.getStringExtra("type");
        if ("rule".equals(type)){
            //WebView加载web资源
            web = Url.Rule ;
            info_title.setText("资芽公约");
        }else if ("gold".equals(type)){
            info_title.setText("充值协议");
            web = Url.Gold ;
        }else if ("other".equals(type)){
            info_title.setText(intent.getStringExtra("title"));
            web = intent.getStringExtra("url");
        }

        webView.loadUrl(web);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });


        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        webView.getSettings().setDisplayZoomControls(false);


    }

    private void initView() {

        pre = (RelativeLayout) findViewById(R.id.pre);
        webView = (WebView) findViewById(R.id.webView);
        bar = (ProgressBar) findViewById(R.id.bar);

        info_title = (TextView)findViewById(R.id.info_title ) ;
    }
}
