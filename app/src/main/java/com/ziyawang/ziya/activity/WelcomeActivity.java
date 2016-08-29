package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.WelcomeAdapter;
import com.ziyawang.ziya.application.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends BaseActivity {

    private MyApplication app ;

    private ViewPager viewPager;
    private List<ImageView> list;
    private WelcomeAdapter adapter;
    private int[] imageIds = new int[]{R.mipmap.pic01 ,R.mipmap.pic02 ,R.mipmap.pic03, R.mipmap.pic04};
    private ImageView[] icons = new ImageView[4];

    private Button jump_main ;

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("引导页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("引导页面");
        //统计时长
        MobclickAgent.onPause(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        app = (MyApplication) getApplication();
//        this.getApplication() ;
//        app.addActivity(this);

        //实例化组件
        initView();

        //加载数据
        loadData();

        adapter = new WelcomeAdapter(this ,list  );
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < icons.length; i++) {
                    icons[i].setEnabled(false);
                }
                icons[position].setEnabled(true);
                if (position == 3) {

                    list.get(position).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
//                    jump_main.setVisibility(View.VISIBLE);
//                    jump_main.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        icons[0] = (ImageView) findViewById(R.id.icon1);
        icons[1] = (ImageView) findViewById(R.id.icon2);
        icons[2] = (ImageView) findViewById(R.id.icon3);
        icons[3] = (ImageView) findViewById(R.id.icon4);
        jump_main = (Button) findViewById(R.id.jump_main ) ;
    }

    private void loadData() {

        list = new ArrayList<ImageView>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageIds[i]);
            imageView.setScaleType(ScaleType.FIT_XY);
            list.add(imageView);

            icons[i].setEnabled(false);
        }
        icons[0].setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        list.get(0).destroyDrawingCache();
        list.get(1).destroyDrawingCache();
        list.get(2).destroyDrawingCache();
        list.get(3).destroyDrawingCache();
        list.clear();

    }
}
