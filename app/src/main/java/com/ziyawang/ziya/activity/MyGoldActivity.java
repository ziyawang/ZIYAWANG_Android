package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import org.json.JSONException;
import org.json.JSONObject;

public class MyGoldActivity extends BenBenActivity implements View.OnClickListener {

    //回退按钮
    private RelativeLayout pre ;
    //充值按钮
    private TextView recharge ;
    //明细按钮
    private TextView gold_details ;
    //账户余额
    private TextView my_gold_balance_textView ;
    //资芽币公约
    private TextView my_gold_rule ;
    @Override
    public boolean shouldShowRequestPermissionRationale(String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新加载数据
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //加载数据
        loadData() ;
    }

    private void loadData() {
        if (GetBenSharedPreferences.getIsLogin(MyGoldActivity.this)){
            String urls = String.format(Url.Myicon, GetBenSharedPreferences.getTicket(MyGoldActivity.this)) ;
            HttpUtils utils = new HttpUtils() ;
            RequestParams params = new RequestParams() ;
            utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //处理请求成功后的数据
                    dealResult(responseInfo.result);
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    //打印用户的失败回调
                    error.printStackTrace();
                    ToastUtils.shortToast( MyGoldActivity.this, "网络连接异常");
                }
            }) ;
        }
    }

    private void dealResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject user = jsonObject.getJSONObject("user");
            String account = user.getString("Account");
            my_gold_balance_textView.setText(account);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_my_gold);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout) findViewById(R.id.pre) ;
        recharge = (TextView)findViewById(R.id.recharge )  ;
        gold_details = (TextView)findViewById(R.id.gold_details )  ;
        my_gold_balance_textView = (TextView)findViewById(R.id.my_gold_balance_textView )  ;
        my_gold_rule = (TextView)findViewById(R.id.my_gold_rule )  ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        recharge.setOnClickListener(this);
        gold_details.setOnClickListener(this);
        my_gold_rule.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        String account = intent.getStringExtra("account");
        my_gold_balance_textView.setText(account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.recharge :
                goRechargeActivity() ;
                break;
            case R.id.gold_details :
                goGoldDetailsActivity() ;
                break;
            case R.id.my_gold_rule :
                goRuleActivity() ;
                break;
            default:
                break;
        }
    }

    private void goRuleActivity() {
        Intent intent = new Intent( MyGoldActivity.this , MyRuleActivity.class ) ;
        intent.putExtra("type" , "gold" ) ;
        startActivity(intent);
    }

    private void goGoldDetailsActivity() {
        Intent intent = new Intent(MyGoldActivity.this , GoldDetailsActivity.class ) ;
        startActivity(intent);
    }

    private void goRechargeActivity() {
        Intent intent = new Intent(MyGoldActivity.this , RechargeActivity.class ) ;
        startActivity(intent);
    }
}
