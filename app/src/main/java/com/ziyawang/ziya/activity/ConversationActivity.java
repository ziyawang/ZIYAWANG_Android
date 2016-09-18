package com.ziyawang.ziya.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.ChangeNotifyColor;

import io.rong.imkit.fragment.ConversationFragment;

public class ConversationActivity extends FragmentActivity {

    //返回按钮
    private RelativeLayout pre ;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        //改变通知栏的颜色
        ChangeNotifyColor.change(R.color.color_login_head, this);
        //实例化组件
        initView() ;
        //添加监听事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
        if(!fragment.onBackPressed()) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 实例化组件
     */
    private void initView() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
    }

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("会话页面");
        //统计时长
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("会话页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

}
