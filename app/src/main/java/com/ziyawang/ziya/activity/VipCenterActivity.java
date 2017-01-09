package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.ziyawang.ziya.tools.SDUtil;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyIconImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class VipCenterActivity extends BenBenActivity implements View.OnClickListener{

    //返回按钮
    private RelativeLayout pre ;
    //充值记录
    private TextView vip_record ;
    //会员类型选择
    private Button vip_btn01 , vip_btn02 , vip_btn03 , vip_btn04 , vip_btn05 ;

    //头像
    private MyIconImageView vip_center_icon ;
    //头像在内存中的缓存bitmap
    private Bitmap bitmap ;

    private String type_01 , type_02 , type_03, type_04 , type_05 ;

    private TextView ben ;

    private static final String ASSETS = "01" ;
    private static final String COMPANY = "02" ;
    private static final String FIXED = "03" ;
    private static final String FINANCE = "04" ;
    private static final String PERSON = "05" ;

    private SharedPreferences right ;

    private ImageView image_01 , image_02 , image_03 , image_04 , image_05 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_vip_center);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        vip_record = (TextView)findViewById(R.id.vip_record ) ;
        vip_btn01 = (Button)findViewById(R.id.vip_btn01 ) ;
        vip_btn02 = (Button)findViewById(R.id.vip_btn02 ) ;
        vip_btn03 = (Button)findViewById(R.id.vip_btn03 ) ;
        vip_btn04 = (Button)findViewById(R.id.vip_btn04 ) ;
        vip_btn05 = (Button)findViewById(R.id.vip_btn05 ) ;
        vip_center_icon = (MyIconImageView)findViewById(R.id.vip_center_icon ) ;

        image_01 = (ImageView)findViewById(R.id.image_01 ) ;
        image_02 = (ImageView)findViewById(R.id.image_02 ) ;
        image_03 = (ImageView)findViewById(R.id.image_03 ) ;
        image_04 = (ImageView)findViewById(R.id.image_04 ) ;
        image_05 = (ImageView)findViewById(R.id.image_05 ) ;

        ben = (TextView)findViewById(R.id.ben ) ;

    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        vip_record.setOnClickListener(this);
        vip_btn01.setOnClickListener(this);
        vip_btn02.setOnClickListener(this);
        vip_btn03.setOnClickListener(this);
        vip_btn04.setOnClickListener(this);
        vip_btn05.setOnClickListener(this);

        image_01.setOnClickListener(this);
        image_02.setOnClickListener(this);
        image_03.setOnClickListener(this);
        image_04.setOnClickListener(this);
        image_05.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAuthMe() ;
    }

    private void loadAuthMe() {
        String urls = String.format(Url.Myicon, GetBenSharedPreferences.getTicket(this)) ;
        HttpUtils utils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("会员中心OnResume" , responseInfo.result ) ;
                //处理请求成功后的数据
                try {
                    dealResult(responseInfo.result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //打印用户的失败回调
                error.printStackTrace();
                ToastUtils.shortToast(VipCenterActivity.this, "网络连接异常");
            }
        }) ;
    }

    private void dealResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        JSONObject user1 = jsonObject.getJSONObject("user");
        String right01 = user1.getString("right") ;
        right = getSharedPreferences("right", MODE_PRIVATE) ;
        right.edit().putString("right", right01).commit();
        JSONObject showright = user1.getJSONObject("showright");
        type_01 = showright.optString("资产包");
        type_02 = showright.optString("企业商账");
        type_03 = showright.optString("固定资产");
        type_04 = showright.optString("融资信息");
        type_05 = showright.optString("个人债权");
        String right = GetBenSharedPreferences.getRight(this);
        if (!TextUtils.isEmpty(right) ){
            String[] split = right.split(",");
            for (int i = 0; i < split.length; i++) {
                if ("1".equals(split[i].toString())){
                    image_01.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("18".equals(split[i].toString())){
                    image_02.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("12".equals(split[i].toString()) ){
                    image_03.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("6".equals(split[i].toString()) ){
                    image_04.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("19".equals(split[i].toString())){
                    image_05.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void initData() {
        initIcon() ;

        Intent intent = getIntent() ;
        type_01 = intent.getStringExtra("type_01");
        type_02 = intent.getStringExtra("type_02");
        type_03 = intent.getStringExtra("type_03");
        type_04 = intent.getStringExtra("type_04");
        type_05 = intent.getStringExtra("type_05");

        String right = GetBenSharedPreferences.getRight(this);
        if (!TextUtils.isEmpty(right) ){
            String[] split = right.split(",");
            for (int i = 0; i < split.length; i++) {
                if ("1".equals(split[i].toString())){
                    image_01.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("18".equals(split[i].toString())){
                    image_02.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("12".equals(split[i].toString()) ){
                    image_03.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("6".equals(split[i].toString()) ){
                    image_04.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
                if ("19".equals(split[i].toString())){
                    image_05.setVisibility(View.VISIBLE);
                    ben.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initIcon() {
        if (GetBenSharedPreferences.getIsLogin(this)){
            //先拿到用户的缓存的头像,存在进行加载，不能重新操作。
            File files = new File(Url.IconPath);
            if (files.exists()) {
                final byte[] icons = SDUtil.getDataFromSDCard("icon.png");
                bitmap = BitmapFactory.decodeByteArray(icons, 0, icons.length);
                vip_center_icon.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.pre :
                finish();
                break;
            //充值记录
            case R.id.vip_record :
                goVipRecordActivity() ;
                break;
            case R.id.vip_btn01 :
                goVipRechargeActivity(ASSETS) ;
                break;
            case R.id.vip_btn02 :
                goVipRechargeActivity(COMPANY) ;
                break;
            case R.id.vip_btn03 :
                goVipRechargeActivity(FIXED) ;
                break;
            case R.id.vip_btn04 :
                goVipRechargeActivity(FINANCE) ;
                break;
            case R.id.vip_btn05 :
                goVipRechargeActivity(PERSON) ;
                break;
            case R.id.image_01 :
                if (!TextUtils.isEmpty(type_01)){
                    ToastUtils.longToast(VipCenterActivity.this, "资产包会员到期时间：" + type_01);
                }
                break;
            case R.id.image_02 :
                if (!TextUtils.isEmpty(type_02)){
                    ToastUtils.longToast(VipCenterActivity.this , "企业商账会员到期时间：" + type_02);
                }
                break;
            case R.id.image_03 :
                if (!TextUtils.isEmpty(type_03)){
                    ToastUtils.longToast(VipCenterActivity.this , "固定资产会员到期时间：" + type_03);
                }
                break;
            case R.id.image_04 :
                if (!TextUtils.isEmpty(type_04)){
                    ToastUtils.longToast(VipCenterActivity.this , "融资信息会员到期时间：" + type_04);
                }
                break;
            case R.id.image_05 :
                if (!TextUtils.isEmpty(type_05)){
                    ToastUtils.longToast(VipCenterActivity.this , "个人债权会员到期时间：" + type_05);
                }
                break;
            default:
                break;
        }
    }

    private void goVipRechargeActivity(String type) {
        if (GetBenSharedPreferences.getIsLogin(this)){
            Intent intent = new Intent(VipCenterActivity.this , VipRechargeActivity.class ) ;
            intent.putExtra("type" , type ) ;
            startActivity(intent);
        }else {
            Intent intent = new Intent(VipCenterActivity.this , LoginActivity.class );
            startActivity(intent);
        }
    }

    private void goVipRecordActivity() {
        Intent intent = new Intent(VipCenterActivity.this , VipRecordActivity.class ) ;
        startActivity(intent);
    }
}
