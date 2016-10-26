package com.ziyawang.ziya.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportActivity extends BenBenActivity implements View.OnClickListener {
    //回退按钮的监听事件
    private RelativeLayout pre ;
    //第一个选择框
    private CheckBox report_one ;
    //第二个选择框
    private CheckBox report_two ;
    //第三个选择框
    private CheckBox report_three ;
    //第四个选择框
    private CheckBox report_four ;
    //第五个选择框
    private CheckBox report_five ;
    //第六个选择框
    private CheckBox report_six ;
    //确认举报的提交按钮
    private Button report_submit ;
    //举报的类型
    private static String type ;
    //举报的id
    private static String id ;
    //举报的原因
    private StringBuffer stringBuffer = new StringBuffer()  ;
    //举报借口的传递参数
    private static String urls ;
    //数据加载的
    private MyProgressDialog dialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_report);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        report_one = (CheckBox)findViewById(R.id.report_one ) ;
        report_two = (CheckBox)findViewById(R.id.report_two ) ;
        report_three = (CheckBox)findViewById(R.id.report_three ) ;
        report_four = (CheckBox)findViewById(R.id.report_four ) ;
        report_five = (CheckBox)findViewById(R.id.report_five ) ;
        report_six = (CheckBox)findViewById(R.id.report_six ) ;
        report_submit = (Button)findViewById(R.id.report_submit ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        report_submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        switch (intent.getStringExtra("type")){
            case "info" :
                type = "1" ;
                break;
            case "service" :
                type = "2" ;
                break;
            default:
                break;
        }
        id = intent.getStringExtra("id");
        if ("info".equals(intent.getStringExtra("type"))){
            report_one.setText("已合作或已处置");
            report_two.setText("虚假信息");
            report_three.setText("泄露私密");
            report_four.setText("垃圾广告");
            report_five.setText("人身攻击");
            report_six.setText("无法联系");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.report_submit :
                goReportSubmit() ;
                break;
            default:
                break;

        }
    }

    private void goReportSubmit() {
        if (report_one.isChecked()){
            stringBuffer.append("1") ;
            stringBuffer.append(",") ;
        }
        if (report_two.isChecked()){
            stringBuffer.append("2") ;
            stringBuffer.append(",") ;
        }
        if (report_three.isChecked()){
            stringBuffer.append("3") ;
            stringBuffer.append(",") ;
        }
        if (report_four.isChecked()){
            stringBuffer.append("4") ;
            stringBuffer.append(",") ;
        }
        if (report_five.isChecked()){
            stringBuffer.append("5") ;
            stringBuffer.append(",") ;
        }
        if (report_six.isChecked()){
            stringBuffer.append("6") ;
            stringBuffer.append(",") ;
        }
        if (isSubmit()){
            loadData() ;
        }else {
            ToastUtils.shortToast(ReportActivity.this, "请至少选择一个举报原因");
        }

    }

    private boolean isSubmit() {
        if (!TextUtils.isEmpty(stringBuffer.toString()) && !"".equals(stringBuffer.toString())){
            return true ;
        }else {
            return false ;
        }

    }

    private void loadData() {
        if (GetBenSharedPreferences.getIsLogin(this)){
            urls = String.format(Url.Report, GetBenSharedPreferences.getTicket(this ) ) ;
        }else {
            urls = String.format(Url.Report, "" ) ;
        }
        //开启网络请求
        showBenDialog();
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("Type" , type );
        params.addBodyParameter("ItemID" , id );
        params.addBodyParameter("ReasonID" , stringBuffer.toString());
        params.addBodyParameter("Channel" , "ANDROID" ) ;
        Log.e("ReasonID", stringBuffer.toString()) ;
        Log.e("Type", type );
        Log.e("ItemID" , id ) ;
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //关闭dialog
                hiddenBenDialog();
                try {
                    dealResult(responseInfo.result) ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //关闭dialog
                hiddenBenDialog();
                ToastUtils.shortToast(ReportActivity.this , "网络连接异常");
            }
        }) ;
    }

    private void dealResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result) ;
        String status_code = jsonObject.getString("status_code");
        switch (status_code){
            case "200" :
                ToastUtils.shortToast(ReportActivity.this , "举报成功，客服人员将尽快进行处理");
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(this, "数据提交中，请稍后。。。");
        // 不可以用“返回键”取消
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 隐藏数据加载框
     */
    private void hiddenBenDialog() {
        //关闭dialog
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
