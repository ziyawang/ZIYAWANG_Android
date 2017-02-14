package com.ziyawang.ziya.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import java.io.IOException;

public class VipRechargeActivity extends BenBenActivity implements View.OnClickListener {

    private static String YOUR_URL = Url.V202PayCharge ;

    public static final String URL = YOUR_URL;

    //返回按钮
    private RelativeLayout pre ;
    //查看特权
    private TextView know ;
    private String type ;
    //图标
    private ImageView img_vip_type ;
    //说明
    private TextView vip_recharge_title ;
    //微信支付选择区域
    private RelativeLayout wx_relative ;
    //微信支付选择提示框
    private ImageView wx_select ;
    //支付宝支付选择区域
    private RelativeLayout alipay_relative ;
    //支付宝支付选择提示框
    private ImageView alipay_select ;
    //银联支付选择区域
    private RelativeLayout upacp_relative ;
    //银联支付选择提示框
    private ImageView upacp_select ;
    //季费
    private TextView recharge_money01 ;
    //年费
    private TextView recharge_money02 ;
    //金额01区域
    private RelativeLayout money_relative01 ;
    //金额01选择提示框
    private ImageView img_select01 ;
    //金额02区域
    private RelativeLayout money_relative02 ;
    //金额02选择提示框
    private ImageView img_select02 ;
    private TextView vip_type ;

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付宝支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    //充值的渠道
    private String channel = CHANNEL_WECHAT ;
    //确认支付按钮
    private Button recharge ;
    //充值的金额
    private int vip_money = 1000 ;

    private boolean isSelected = false ;

    private String payname ;
    private String payid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_vip_recharge);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        know = (TextView)findViewById(R.id.know ) ;
        img_vip_type = (ImageView)findViewById(R.id.img_vip_type ) ;
        vip_recharge_title = (TextView)findViewById(R.id.vip_recharge_title ) ;
        wx_relative = (RelativeLayout)findViewById(R.id.wx_relative ) ;
        alipay_relative = (RelativeLayout)findViewById(R.id.alipay_relative ) ;
        upacp_relative = (RelativeLayout)findViewById(R.id.upacp_relative ) ;
        upacp_select = (ImageView)findViewById(R.id.upacp_select ) ;
        alipay_select = (ImageView)findViewById(R.id.alipay_select ) ;
        wx_select = (ImageView)findViewById(R.id.wx_select ) ;
        recharge = (Button) findViewById(R.id.recharge);
        recharge_money01 = (TextView) findViewById(R.id.recharge_money01);
        recharge_money02 = (TextView) findViewById(R.id.recharge_money02);

        img_select01 = (ImageView) findViewById(R.id.img_select01);
        img_select02 = (ImageView) findViewById(R.id.img_select02);
        money_relative01 = (RelativeLayout) findViewById(R.id.money_relative01);
        money_relative02 = (RelativeLayout) findViewById(R.id.money_relative02);
        vip_type = (TextView) findViewById(R.id.vip_type);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        know.setOnClickListener(this);
        wx_relative.setOnClickListener(this);
        alipay_relative.setOnClickListener(this);
        upacp_relative.setOnClickListener(this);
        recharge.setOnClickListener(this);
        money_relative01.setOnClickListener(this);
        money_relative02.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        if (intent!=null){
            type = intent.getStringExtra("type");
        }

        switch (type){
            case "01" :
                payid = "8" ;
                payname = "资产包" ;
                img_vip_type.setImageResource(R.mipmap.v2020401);
                vip_recharge_title.setText("开通资产包VIP享受精彩特权");
                vip_type.setText("月度会员");
                recharge_money01.setText("￥6498");
                recharge_money02.setText("￥70000");
                break;
            case "02" :
                payid = "10" ;
                payname = "企业商账" ;
                img_vip_type.setImageResource(R.mipmap.v2020402);
                vip_recharge_title.setText("开通企业商账VIP享受精彩特权");
                vip_type.setText("季度会员");
                recharge_money01.setText("￥1498");
                recharge_money02.setText("￥4998");
                break;
            case "03" :
                payid = "6" ;
                payname = "固定资产" ;
                img_vip_type.setImageResource(R.mipmap.v2020403);
                vip_recharge_title.setText("开通固定资产VIP享受精彩特权");
                vip_type.setText("月度会员");
                recharge_money01.setText("￥6498");
                recharge_money02.setText("￥70000");
                break;
            case "04" :
                payid = "4" ;
                payname = "融资信息" ;
                img_vip_type.setImageResource(R.mipmap.v2020404);
                vip_recharge_title.setText("开通融资信息VIP享受精彩特权");
                vip_type.setText("季度会员");
                recharge_money01.setText("￥998");
                recharge_money02.setText("￥2998");
                break;
            case "05" :
                payid = "2" ;
                payname = "个人债权" ;
                img_vip_type.setImageResource(R.mipmap.v2020405);
                vip_recharge_title.setText("开通个人债权VIP享受精彩特权");
                vip_type.setText("季度会员");
                recharge_money01.setText("￥998");
                recharge_money02.setText("￥2998");
                break;
            default:
                break;
        }
        selectMoney(img_select02 , recharge_money02.getText().toString() );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.know :
                goKnowPowerActivity(type) ;
                break;
            case R.id.wx_relative :
                select(wx_select, CHANNEL_WECHAT) ;
                break;
            case R.id.alipay_relative :
                select(alipay_select, CHANNEL_ALIPAY) ;
                break;
            case R.id.upacp_relative :
                select(upacp_select, CHANNEL_UPACP) ;
                break;
            case R.id.money_relative01 :
                selectMoney(img_select01 , recharge_money01.getText().toString() );
                switch (type){
                    case "01" :
                        payid = "7" ;
                        break;
                    case "02" :
                        payid = "9" ;
                        break;
                    case "03" :
                        payid = "5" ;
                        break;
                    case "04" :
                        payid = "3" ;
                        break;
                    case "05" :
                        payid = "1" ;
                        break;
                    default:
                        break;
                }
                break;
            case R.id.money_relative02 :
                selectMoney(img_select02 , recharge_money02.getText().toString() );
                switch (type){
                    case "01" :
                        payid = "8" ;
                        break;
                    case "02" :
                        payid = "10" ;
                        break;
                    case "03" :
                        payid = "6" ;
                        break;
                    case "04" :
                        payid = "4" ;
                        break;
                    case "05" :
                        payid = "2" ;
                        break;
                    default:
                        break;
                }
                break;
            case R.id.recharge :
                goRecharge() ;
                break;
            default:
                break;
        }
    }

    private void selectMoney(ImageView v , String money ) {
        isSelected = true ;
        img_select01.setImageResource(R.mipmap.unselect);
        img_select02.setImageResource(R.mipmap.unselect);
        v.setImageResource(R.mipmap.rechargeselect);
        //vip_money = Integer.parseInt(money);
        String money02 = money.replace("￥", "");
        vip_money = Integer.parseInt(money02) ;
    }

    private void goRecharge() {
        if (isSelected){
            //充值的金额money , 充值的渠道channel
            Log.e("benben", "渠道:" + channel + "---" + "金额：" + (vip_money *1000) + "名称" + payname + "类型id" + payid ) ;
            //付款
            new PaymentTask().execute(new PaymentRequest( "member" , payname , payid , channel ));
        }else {
            ToastUtils.shortToast(this , "请选择会员时间");
        }


    }

    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

        @Override
        protected void onPreExecute() {
            //按键点击之后的禁用，防止重复点击
            recharge.setOnClickListener(null);
        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {

            PaymentRequest paymentRequest = pr[0];
            String data = null;
            String json = new Gson().toJson(paymentRequest);
            try {
                //向Your Ping++ Server SDK请求数据
                data = postJson(URL, json , VipRechargeActivity.this );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if(null == data){
                //showMsg("请求出错", "请检查URL", "URL无法获取charge");
                ToastUtils.shortToast(VipRechargeActivity.this  , "请检查网络连接");
                return;
            }
            Log.d("charge", data);
            Pingpp.createPayment(VipRechargeActivity.this, data);
        }

    }

    private static String postJson(String url, String json , Context context) throws IOException {

        String urls = String.format(url, GetBenSharedPreferences.getTicket(context));
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(urls).post(body).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    class PaymentRequest {
        String paytype ;
        String payname ;
        String payid ;
        String channel;

        public PaymentRequest(String paytype , String payname , String payid ,  String channel) {
            this.paytype = paytype ;
            this.payname = payname ;
            this.payid = payid ;
            this.channel = channel;
        }
    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //重新获得点击事件
        recharge.setOnClickListener(this);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                //String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                //String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                //showMsg(result, errorMsg, extraMsg);
                if ("success".equals(result)){
                    ToastUtils.shortToast(this , "支付成功！");
                }else if ("fail".equals(result)){
                    ToastUtils.shortToast(this , "支付失败！");
                }else  if ("cancel".equals(result)){
                    ToastUtils.shortToast(this , "取消支付！");
                }else if ("invalid".equals(result)){
                    //ToastUtils.shortToast(this , "该支付方式正在开发，请您选择其他支付方式");
                    ToastUtils.shortToast(this , "未安装该客户端");
                }

            }
        }
    }

    private void select(ImageView v , String str) {

        wx_select.setImageResource(R.mipmap.unselect);
        alipay_select.setImageResource(R.mipmap.unselect);
        upacp_select.setImageResource(R.mipmap.unselect);
        v.setImageResource(R.mipmap.rechargeselect);
        //选择支付渠道
        channel = str ;
    }

    private void goKnowPowerActivity(String type) {
        Intent intent = new Intent(VipRechargeActivity.this , KnowPowerActivity.class ) ;
        intent.putExtra("type" , type ) ;
        startActivity(intent);
    }
}
