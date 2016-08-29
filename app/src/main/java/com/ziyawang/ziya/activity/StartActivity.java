package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.application.MyApplication;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends BaseActivity {

    private SharedPreferences sp;

    private MyApplication app ;

    private int recLen = 3;
    Timer timer = new Timer() ;
    private Button start_time ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);

        initView() ;

        timer.schedule(task, 1000, 1000);

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity() ;
                timer.cancel();
            }
        });


    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recLen--;
                    start_time.setText(recLen+"s");
                    if (recLen == 0){
                        timer.cancel();
                        changeActivity();
                    }
                }
            });

        }
    };

    private void changeActivity() {

        sp = getSharedPreferences("isFirst" , 0 ) ;
        boolean isFirst = sp.getBoolean("isFirst" , true ) ;
        Intent intent ;
        if (isFirst){
            sp.edit().putBoolean("isFirst" , false ).commit() ;
            intent = new Intent(StartActivity.this , WelcomeActivity.class ) ;
            startActivity(intent) ;
            //finish();
        }else{
            intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            //finish();
        }

    }

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("欢迎页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("欢迎页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    private void initView() {

        start_time = (Button) findViewById(R.id.start_time ) ;


    }


//    private void changeActivity() {
//        sp = this.getSharedPreferences("isFirst", 0);
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            public void run() {
//                boolean isFirst = sp.getBoolean("isFirst", true);
//                Intent intent;
//                if(isFirst) {
//                    sp.edit().putBoolean("isFirst", false).commit();
//                    intent = new Intent(StartActivity.this, WelcomeActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    intent = new Intent(StartActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//            }
//        };
//        timer.schedule(task, 1100L);
//    }

}


