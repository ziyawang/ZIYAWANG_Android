package com.ziyawang.ziya.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

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
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class EvaluateActivity_end extends BenBenActivity implements View.OnClickListener {

    //返回按钮
    private RelativeLayout pre ;
    private XEditText editText ;
    //提交按钮
    private Button evaluate_submit ;

    private static String Money ;
    private static String Area ;
    private static String AssetType ;
    private static String Type ;
    private static String Answer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_evaluate_activity_end);
    }

    @Override
    public void initViews() {
        editText = (XEditText)findViewById(R.id.editText ) ;
        evaluate_submit = (Button)findViewById(R.id.evaluate_submit ) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        evaluate_submit.setOnClickListener(this);

    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        Money = intent.getStringExtra("Money");
        Area = intent.getStringExtra("Area");
        AssetType = intent.getStringExtra("AssetType");
        Type = intent.getStringExtra("Type");
        Answer = intent.getStringExtra("Answer");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                goPre();
                break;
            case R.id.evaluate_submit :
                goSubmit() ;
                break;
            default:
                break;
        }
    }

    private void goSubmit() {
        if (judgePwd(editText.getText().toString().trim())){
           submitData() ;
        }
    }

    private void submitData() {
        String urls ;
        if (GetBenSharedPreferences.getIsLogin(this)){
            urls = String.format(Url.SubmitQuestions, GetBenSharedPreferences.getTicket(this)) ;
        }else {
            urls = String.format(Url.SubmitQuestions, "" ) ;
        }
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("Money" ,Money  );
        params.addBodyParameter("Area" ,Area  );
        params.addBodyParameter("AssetType" ,AssetType  );
        params.addBodyParameter("Type", Type);
        params.addBodyParameter("PhoneNumber", editText.getText().toString().trim());
        params.addBodyParameter("Channel", "ANDROID");

//        String[] split = Answer.split(",");
//        String temp_answer = "" ;
//        for (int i = 0; i < split.length; i++) {
//            String[] split1 = split[i].split("=");
//            if (split1[1].toString().contains("@")){
//                String replace = split1[1].toString().replace("@", ",");
//                temp_answer = temp_answer + split1[0] + ":" + replace + "," ;
//            }else {
//                temp_answer = temp_answer + split1[0] + ":" + split1[1] + "," ;
//            }
//
//        }
        Log.e("temp_answer" , Answer ) ;
        params.addBodyParameter("Answer" ,Answer  );
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("提交结果" , responseInfo.result ) ;
                try {
                    JSONObject object = new JSONObject(responseInfo.result) ;
                    String status_code = object.getString("status_code");
                    switch (status_code){
                        case "200" :
                            String score = object.getString("score");
                            String result = object.getString("result");
                            Intent intent = new Intent(EvaluateActivity_end.this , EvaluateResultActivity.class ) ;
                            intent.putExtra("score" , score ) ;
                            intent.putExtra("result" , result ) ;
                            startActivity(intent);
                            finish();
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
                ToastUtils.shortToast(EvaluateActivity_end.this , "网络连接异常");
            }
        }) ;
    }

    private boolean judgePwd(String edit_phoneNumber) {
        if (TextUtils.isEmpty(edit_phoneNumber)){
            ToastUtils.longToast(EvaluateActivity_end.this, "请输入手机号");
            return false ;
        }
        if (!edit_phoneNumber.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){
            ToastUtils.longToast(EvaluateActivity_end.this , "请输入正确的手机号码");
            return false ;
        }
        return true ;
    }

    private void goPre() {
        final CustomDialog.Builder builder01 = new CustomDialog.Builder(EvaluateActivity_end.this);
        builder01.setTitle("温馨提示");
        builder01.setMessage("只差一步您就可以获取您的测评结果，确认退出？");
        builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder01.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder01.create().show();
    }

    @Override
    public void onBackPressed() {
        goPre();
    }
}
