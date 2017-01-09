package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.application.MyApplication;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends BaseActivity {

    private SharedPreferences sp;
    private SharedPreferences sprole;
    private MyApplication app;
    private int recLen = 3;
    Timer timer = new Timer();
    private Button start_time;

    private SharedPreferences role ;
    private SharedPreferences right ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        app = (MyApplication) getApplication();
        this.getApplication();
        app.addActivity(this);

        initView();

        timer.schedule(task, 1000, 1000);

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
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
                    start_time.setText(recLen + "s");
                    if (recLen == 0) {
                        timer.cancel();
                        changeActivity();
                    }
                }
            });

        }
    };

    private void changeActivity() {

        sp = getSharedPreferences("isFirst", MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("isFirst", true);
        sprole = getSharedPreferences("isFirstRole", MODE_PRIVATE);
        boolean isFirstRole = sprole.getBoolean("isFirstRole", true);

        if (GetBenSharedPreferences.getIsLogin(this)) {
            if (isFirstRole) {
                loadRoleData(isFirst);
            } else {
                goSelect(isFirst);
            }
        } else {
            goSelect(isFirst);
        }

    }

    private void goSelect(boolean isFirst) {
        Intent intent;
        if (isFirst) {
            sp.edit().putBoolean("isFirst", false).commit();
            intent = new Intent(StartActivity.this, WelcomeActivity.class);
            startActivity(intent);
            //Log.e("第一次StartActivity" , GetBenSharedPreferences.getRole(this)) ;
            //finish();
        } else {
            intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            //Log.e("第二次StartActivity", GetBenSharedPreferences.getRole(this)) ;
            //finish();
        }
    }

    private void loadRoleData(final boolean isFirst) {
            String urls = String.format(Url.Myicon, GetBenSharedPreferences.getTicket(this));
            HttpUtils utils = new HttpUtils();
            RequestParams params = new RequestParams();
            utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //处理请求成功后的数据
                    try {
                        dealResult(responseInfo.result , isFirst);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    //打印用户的失败回调
                    error.printStackTrace();
                }
            });
    }

    private void dealResult(String result, boolean isFirst) throws JSONException {
        final JSONObject jsonObject = new JSONObject(result);
        String role_a = jsonObject.getString("role");
        role = getSharedPreferences("role" , MODE_PRIVATE ) ;
        role.edit().putString("role", role_a).commit();
        sprole.edit().putBoolean("isFirstRole", false).commit();

        JSONObject user101 = jsonObject.getJSONObject("user");
        String right01 = user101.getString("right") ;
        right = getSharedPreferences("right" , MODE_PRIVATE ) ;
        right.edit().putString("right", right01).commit();

        goSelect(isFirst);
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

        start_time = (Button) findViewById(R.id.start_time);

    }

}


