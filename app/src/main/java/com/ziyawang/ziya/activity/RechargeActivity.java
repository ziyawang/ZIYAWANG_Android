package com.ziyawang.ziya.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.RechargeTypeAdapter;
import com.ziyawang.ziya.entity.RechargeTypeEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.Json_RechargeType;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyGridView;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;

import java.util.List;

public class RechargeActivity extends BenBenActivity implements View.OnClickListener {

    private static String YOUR_URL = Url.PayCharge ;

    public static final String URL = YOUR_URL;
    private MyGridView gridView ;
    List<RechargeTypeEntity> list ;
    RechargeTypeAdapter adapter ;


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
    //返回按钮
    private RelativeLayout pre ;
    //确认支付按钮
    private Button recharge ;
    //充值的金额
    private int money = 1000;
    //充值的芽币
    private String ybcount = "100" ;
    //充值的渠道
    private String channel = CHANNEL_WECHAT ;
    //数据加载的dialog
    private MyProgressDialog dialog ;

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //正式
        api = WXAPIFactory.createWXAPI(this, "wxd414950a5c59eded");
        api.registerApp("wxd414950a5c59eded");

        //测试
//        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
//        api.registerApp("wxb4ba3c02aa476ea1");

        //加载数据
        loadData() ;
    }
    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(RechargeActivity.this, "数据获取中。。。");
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


    private void loadData() {
        //使dialog可见
        showBenDialog();
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.RechargeType, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //关闭dialog
                hiddenBenDialog();
                Log.e("RechargeType", responseInfo.result);
                try {
                    list = Json_RechargeType.getParse(responseInfo.result);
                    for (int i = 0; i < list.size(); i++) {
                        if ("1".equals(list.get(i).getSelected())) {
                            money = Integer.parseInt(list.get(i).getRealMoney());
                            ybcount = list.get(i).getYBCount();
                            recharge.setText("确认支付  ￥" + Integer.parseInt(list.get(i).getRealMoney()) / 100);
                            break;
                        }
                    }
                    adapter = new RechargeTypeAdapter(RechargeActivity.this, list);
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).setSelected("0");
                            }
                            list.get(position).setSelected("1");
                            money = Integer.parseInt(list.get(position).getRealMoney());
                            ybcount = list.get(position).getYBCount();
                            recharge.setText("确认支付  ￥" + Integer.parseInt(list.get(position).getRealMoney()) / 100);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //关闭dialog
                hiddenBenDialog();
                error.printStackTrace();
                ToastUtils.shortToast(RechargeActivity.this, "网络连接异常");
            }
        }) ;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_recharge);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        wx_relative = (RelativeLayout)findViewById(R.id.wx_relative ) ;
        alipay_relative = (RelativeLayout)findViewById(R.id.alipay_relative ) ;
        upacp_relative = (RelativeLayout)findViewById(R.id.upacp_relative ) ;
        upacp_select = (ImageView)findViewById(R.id.upacp_select ) ;
        alipay_select = (ImageView)findViewById(R.id.alipay_select ) ;
        wx_select = (ImageView)findViewById(R.id.wx_select ) ;
        gridView = (MyGridView) findViewById(R.id.gridView);
        recharge = (Button) findViewById(R.id.recharge);

    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        recharge.setOnClickListener(this);
        wx_relative.setOnClickListener(this);
        alipay_relative.setOnClickListener(this);
        upacp_relative.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.wx_relative :
                select(wx_select ,CHANNEL_WECHAT ) ;
                break;
            case R.id.alipay_relative :
                select(alipay_select , CHANNEL_ALIPAY ) ;
                break;
            case R.id.upacp_relative :
                select(upacp_select , CHANNEL_UPACP) ;
                break;
            case R.id.recharge :
                //goRecharge() ;
                /**
                 * 20171213更改支付逻辑 弃用ping++
                 */
                goRecharge02() ;
                break;
            default:
                break;
        }

    }

    private void goRecharge02() {
        //正式
        HttpUtils httpUtils = new HttpUtils() ;
        String urls = String.format(Url.getCharge, GetBenSharedPreferences.getTicket( RechargeActivity.this)) ;
        RequestParams params = new RequestParams() ;
        //充值金额 以分为单位
        params.addBodyParameter("amount" , money + "");
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
                ToastUtils.shortToast( RechargeActivity.this , "网络连接异常");
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
            ToastUtils.shortToast(RechargeActivity.this , "支付失败");
        }
    }



//    private void goRecharge() {
//        //充值的金额money , 充值的渠道channel
//        Log.e("benben", "渠道:" + channel + "---" + "金额：" + money) ;
//        //付款
//        new PaymentTask().execute(new PaymentRequest(channel, money , ybcount ));
//
//    }

    private void select(ImageView v , String str) {

        wx_select.setImageResource(R.mipmap.unselect);
        alipay_select.setImageResource(R.mipmap.unselect);
        upacp_select.setImageResource(R.mipmap.unselect);
        v.setImageResource(R.mipmap.rechargeselect);
        //选择支付渠道
        channel = str ;
    }


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
//                data = postJson(URL, json , RechargeActivity.this );
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
//                ToastUtils.shortToast(RechargeActivity.this  , "请检查网络连接");
//                return;
//            }
//            Log.d("charge", data);
//            Pingpp.createPayment(RechargeActivity.this, data);
//        }
//
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
//                    ToastUtils.shortToast(this , "支付成功！");
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
//
//    public void showMsg(String title, String msg1, String msg2) {
//        String str = title;
//        if (null !=msg1 && msg1.length() != 0) {
//            str += "\n" + msg1;
//        }
//        if (null !=msg2 && msg2.length() != 0) {
//            str += "\n" + msg2;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(str);
//        builder.setTitle("提示");
//        builder.setPositiveButton("OK", null);
//        builder.create().show();
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
//        String channel;
//        int amount;
//        String ybcount  ;
//
//        public PaymentRequest(String channel, int amount , String ybcount ) {
//            this.channel = channel;
//            this.amount = amount;
//            this.ybcount = ybcount ;
//        }
//    }

}
