package com.ziyawang.ziya.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.entity.StarListEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import java.io.IOException;
import java.util.List;

public class StarRegisterActivity01_03 extends BenBenActivity implements View.OnClickListener {
    private static String YOUR_URL = Url.V202PayCharge ;
    public static final String URL = YOUR_URL;
    //返回按钮
    private RelativeLayout pre ;
    // 标题title
    private TextView text_title ;
    //type
    private TextView text_type_02 ;
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

    private TextView text_remark ;

    private String money01 ;
    private String money02 ;
    private String money03 ;

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
    private int vip_money = 200000 ;

    private String payname ;
    private String payid ;

    private TextView ben ;

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, "wxd414950a5c59eded");
        api.registerApp("wxd414950a5c59eded");

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_star_register01_03);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre) ;
        text_title = (TextView)findViewById(R.id.text_title ) ;
        text_type_02 = (TextView)findViewById(R.id.text_type_02 ) ;
        wx_relative = (RelativeLayout)findViewById(R.id.wx_relative ) ;
        alipay_relative = (RelativeLayout)findViewById(R.id.alipay_relative ) ;
        upacp_relative = (RelativeLayout)findViewById(R.id.upacp_relative ) ;
        upacp_select = (ImageView)findViewById(R.id.upacp_select ) ;
        alipay_select = (ImageView)findViewById(R.id.alipay_select ) ;
        wx_select = (ImageView)findViewById(R.id.wx_select ) ;
        recharge = (Button) findViewById(R.id.recharge);
        text_remark = (TextView) findViewById(R.id.text_remark);
        ben = (TextView) findViewById(R.id.ben);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        wx_relative.setOnClickListener(this);
        alipay_relative.setOnClickListener(this);
        upacp_relative.setOnClickListener(this);
        recharge.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        String title = intent.getStringExtra("title");
        text_title.setText(title);
        text_type_02.setText(title);
        switch (title){
            case "保证金认证" :
                payname = "保证金认证" ;
                payid = "1" ;
                text_remark.setVisibility(View.GONE);
                break;
            case "实地认证" :
                payname = "实地认证" ;
                payid = "2" ;
                text_remark.setVisibility(View.VISIBLE);
                text_remark.setText("支付成功后资芽网客服将及时与您联系，并安排认证人员前往与你处开启认证之旅。(客服电话：400-898-8557)");
                break;
            case "视频认证" :
                payname = "视频认证" ;
                payid = "3" ;
                text_remark.setVisibility(View.VISIBLE);
                text_remark.setText("支付成功后资芽网客服将及时与您联系，并告知您拍摄须知及相关事宜。(客服电话：400-898-8557)");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
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
            case R.id.recharge :
                //goRecharge() ;
                goRecharge02() ;
                break;
            default:
                break;
        }
    }

    private void goRecharge02() {

        //正式
        HttpUtils httpUtils = new HttpUtils() ;
        String urls = String.format(Url.getCharge, GetBenSharedPreferences.getTicket( StarRegisterActivity01_03.this)) ;
        RequestParams params = new RequestParams() ;
        //充值金额 以分为单位
        params.addBodyParameter("paytype" , "star");
        params.addBodyParameter("payname" , payname );
        params.addBodyParameter("payid" , payid );
        //支付渠道
        params.addBodyParameter("tradeType" , "APP");
        httpUtils.send(HttpRequest.HttpMethod.POST, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("getCharge" , responseInfo.result ) ;
                if (api!=null){
                    payVX(responseInfo.result) ;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast( StarRegisterActivity01_03.this , "网络连接异常");
            }
        }) ;

        //测试
//        HttpUtils httpUtils = new HttpUtils() ;
//        RequestParams params = new RequestParams() ;
//        httpUtils.send(HttpRequest.HttpMethod.GET, "http://wxpay.wxutil.com/pub_v2/app/app_pay.php" , params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("测试getCharge" , responseInfo.result ) ;
//
//                JSONObject json = JSON.parseObject(responseInfo.result);
//
//                PayReq req = new PayReq();
//                req.appId			= json.getString("appid");
//                req.partnerId		= json.getString("partnerid");
//                req.prepayId		= json.getString("prepayid");
//                req.nonceStr		= json.getString("noncestr");
//                req.timeStamp		= json.getString("timestamp");
//                req.packageValue	= json.getString("package");
//                req.sign			= json.getString("sign");
//                req.extData			= "app data"; // optional
//                Toast.makeText(RechargeActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
//                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                api.sendReq(req);
//
//
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                error.printStackTrace();
//                ToastUtils.shortToast( RechargeActivity.this , "网络连接异常");
//            }
//        }) ;

    }

    private void payVX(String result) {
        JSONObject object = JSON.parseObject(result);
        JSONObject data = object.getJSONObject("data");
        String return_code = data.getString("return_code");
        if ("SUCCESS".equals(return_code)){
            String nonce_str = data.getString("nonce_str");
            String prepay_id = data.getString("prepay_id");
            String sign = data.getString("sign");
            String timestamp = data.getString("timestamp");

            //long time = System.currentTimeMillis() / 1000 ;
            PayReq req = new PayReq();
            req.appId = "wxd414950a5c59eded" ;
            req.partnerId = "1389032102" ;
            req.prepayId  = prepay_id ;
            req.nonceStr  = nonce_str ;
            req.timeStamp  =  timestamp  ;
            req.packageValue = "Sign=WXPay" ;
            req.sign = sign ;
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        }else {
            ToastUtils.shortToast(StarRegisterActivity01_03.this , "支付失败");
        }
    }



//    private void goRecharge() {
//            //充值的金额money , 充值的渠道channel
//            Log.e("benben", "渠道:" + channel + "---" + "金额：" + (vip_money) + "名称" + payname + "类型id" + payid) ;
//            //付款
//            new PaymentTask().execute(new PaymentRequest("star", payname, payid, channel));
//    }
//
//    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            //按键点击之后的禁用，防止重复点击
//            recharge.setOnClickListener(null);
//        }
//
//        @Override
//        protected String doInBackground(PaymentRequest... pr) {
//
//            PaymentRequest paymentRequest = pr[0];
//            String data = null;
//            String json = new Gson().toJson(paymentRequest);
//            try {
//                //向Your Ping++ Server SDK请求数据
//                data = postJson(URL, json , StarRegisterActivity01_03.this );
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return data;
//        }
//
//        /**
//         * 获得服务端的charge，调用ping++ sdk。
//         */
//        @Override
//        protected void onPostExecute(String data) {
//            if(null == data){
//                //showMsg("请求出错", "请检查URL", "URL无法获取charge");
//                ToastUtils.shortToast(StarRegisterActivity01_03.this  , "请检查网络连接");
//                return;
//            }
//            Log.d("charge", data);
//            Pingpp.createPayment(StarRegisterActivity01_03.this, data);
//        }
//
//    }
//
//    private static String postJson(String url, String json , Context context) throws IOException {
//
//        String urls = String.format(url, GetBenSharedPreferences.getTicket(context));
//        MediaType type = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(type, json);
//        Request request = new Request.Builder().url(urls).post(body).build();
//
//        OkHttpClient client = new OkHttpClient();
//        Response response = client.newCall(request).execute();
//
//        return response.body().string();
//    }
//
//    class PaymentRequest {
//        String paytype ;
//        String payname ;
//        String payid ;
//        String channel;
//
//        public PaymentRequest(String paytype , String payname , String payid ,  String channel) {
//            this.paytype = paytype ;
//            this.payname = payname ;
//            this.payid = payid ;
//            this.channel = channel;
//        }
//    }
//
//    /**
//     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
//     * 最终支付成功根据异步通知为准
//     */
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        //重新获得点击事件
//        recharge.setOnClickListener(this);
//        //支付页面返回处理
//        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
//            if (resultCode == Activity.RESULT_OK) {
//                String result = data.getExtras().getString("pay_result");
//                /* 处理返回值
//                 * "success" - payment succeed
//                 * "fail"    - payment failed
//                 * "cancel"  - user canceld
//                 * "invalid" - payment plugin not installed
//                 */
//                //String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                //String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                //showMsg(result, errorMsg, extraMsg);
//                if ("success".equals(result)){
//                    ToastUtils.shortToast(this , "已提交审核，客服人员稍后会与您联系！");
//                }else if ("fail".equals(result)){
//                    ToastUtils.shortToast(this , "支付失败！");
//                }else  if ("cancel".equals(result)){
//                    ToastUtils.shortToast(this , "取消支付！");
//                }else if ("invalid".equals(result)){
//                    //ToastUtils.shortToast(this , "该支付方式正在开发，请您选择其他支付方式");
//                    ToastUtils.shortToast(this , "未安装该客户端");
//                }
//
//            }
//        }
//    }

    private void select(ImageView v , String str) {

        wx_select.setImageResource(R.mipmap.unselect);
        alipay_select.setImageResource(R.mipmap.unselect);
        upacp_select.setImageResource(R.mipmap.unselect);
        v.setImageResource(R.mipmap.rechargeselect);
        //选择支付渠道
        channel = str ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData() ;
    }

    private void loadData() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams requestParams = new RequestParams() ;
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.V203StarList, requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("V203StarList", responseInfo.result) ;
                JSONArray array = JSON.parseArray(responseInfo.result ) ;
                List<StarListEntity> starListEntities = JSON.parseArray(array.toJSONString(), StarListEntity.class);
                money01 = starListEntities.get(0).getPrice().substring(0 , starListEntities.get(0).getPrice().length()-2 ) ;
                money02 = starListEntities.get(1).getPrice().substring(0 , starListEntities.get(1).getPrice().length()-2 ) ;
                money03 = starListEntities.get(2).getPrice().substring(0 , starListEntities.get(2).getPrice().length()-2 ) ;
                Intent intent = getIntent() ;
                String title = intent.getStringExtra("title");
                switch (title){
                    case "保证金认证" :
                        ben.setText("￥" + money01);
                        break;
                    case "实地认证" :
                        ben.setText("￥" + money02);
                        break;
                    case "视频认证" :
                        ben.setText("￥" + money03);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
            }
        }) ;
    }
}
