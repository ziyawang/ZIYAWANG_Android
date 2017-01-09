package com.ziyawang.ziya.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.ziyawang.ziya.view.JustifyTextView;
import com.ziyawang.ziya.view.MyIconImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class StarRegisterActivity extends BenBenActivity implements View.OnClickListener {
    //返回按钮
    private RelativeLayout pre ;
    //头像
    private MyIconImageView vip_center_icon ;
    //头像在内存中的缓存bitmap
    private Bitmap bitmap ;
    //详细说明按钮
    private Button type01_btn01 , type02_btn01 , type03_btn01 , type04_btn01 , type05_btn01 ;
    private Button type01_btn02 , type02_btn02 , type03_btn02 , type04_btn02 , type05_btn02 ;

    private String title01 ;
    private String star_register_type01 ;
    private String other_info01 ;

    private SharedPreferences level ;

    private ImageView star_01 , star_02 , star_03 , star_04 , star_05 ;

    private String type_01 , type_02 , type_03, type_04 , type_05 ;

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
                Log.e("星级认证OnResume", responseInfo.result);
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
                ToastUtils.shortToast(StarRegisterActivity.this, "网络连接异常");
            }
        }) ;
    }

    private void dealResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        JSONObject service1 = jsonObject.getJSONObject("service");
        String Level01 = service1.getString("Level") ;
        level = getSharedPreferences("level", MODE_PRIVATE) ;
        level.edit().putString("level", Level01).commit();
        JSONObject showlevelarr = service1.getJSONObject("showlevelarr");
        type_01 = showlevelarr.optString("1");
        type_02 = showlevelarr.optString("2");
        type_03 = showlevelarr.optString("3");
        type_04 = showlevelarr.optString("4");
        type_05 = showlevelarr.optString("5");
        String level = GetBenSharedPreferences.getLevel(this);
        if (!TextUtils.isEmpty(level) ){
            String[] split = level.split(",");
            for (int i = 0; i < split.length; i++) {
                if ("1".equals(split[i].toString())){
                    star_01.setImageResource(R.mipmap.v203_0301);
                }
                if ("2".equals(split[i].toString())){
                    star_02.setImageResource(R.mipmap.v203_0302);
                }
                if ("3".equals(split[i].toString()) ){
                    star_03.setImageResource(R.mipmap.v203_0303);
                }
                if ("4".equals(split[i].toString()) ){
                    star_04.setImageResource(R.mipmap.v203_0304);
                }
                if ("5".equals(split[i].toString())){
                    star_05.setImageResource(R.mipmap.v203_0305);
                }
            }
        }
        showStatus(type_01 , type01_btn02 ) ;
        showStatus(type_02 , type02_btn02 ) ;
        showStatus(type_03 , type03_btn02 ) ;
        showStatus(type_04 , type04_btn02 ) ;
        showStatus(type_05 , type05_btn02 ) ;

    }

    private void showStatus(String type_01, Button type01_btn02) {
        if (!TextUtils.isEmpty(type_01)){
            switch (type_01){
                case "0" :
                    type01_btn02.setText("开通");
                    type01_btn02.setOnClickListener(this);
                    break;
                case "1" :
                    type01_btn02.setText("审核中");
                    type01_btn02.setOnClickListener(null);
                    break;
                case "2" :
                    type01_btn02.setText("已开通");
                    type01_btn02.setOnClickListener(null);
                    break;
                case "3" :
                    type01_btn02.setText("开通");
                    type01_btn02.setOnClickListener(this);
                    break;
                default:
                    break;
            }
        }else {
            type01_btn02.setText("开通");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_star_register);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        vip_center_icon = (MyIconImageView)findViewById(R.id.vip_center_icon ) ;
        type01_btn01 = (Button)findViewById(R.id.type01_btn01 ) ;
        type02_btn01 = (Button)findViewById(R.id.type02_btn01 ) ;
        type03_btn01 = (Button)findViewById(R.id.type03_btn01 ) ;
        type04_btn01 = (Button)findViewById(R.id.type04_btn01 ) ;
        type05_btn01 = (Button)findViewById(R.id.type05_btn01 ) ;

        type01_btn02 = (Button)findViewById(R.id.type01_btn02 ) ;
        type02_btn02 = (Button)findViewById(R.id.type02_btn02 ) ;
        type03_btn02 = (Button)findViewById(R.id.type03_btn02 ) ;
        type04_btn02 = (Button)findViewById(R.id.type04_btn02 ) ;
        type05_btn02 = (Button)findViewById(R.id.type05_btn02 ) ;

        star_01 = (ImageView)findViewById(R.id.star_01 ) ;
        star_02 = (ImageView)findViewById(R.id.star_02 ) ;
        star_03 = (ImageView)findViewById(R.id.star_03 ) ;
        star_04 = (ImageView)findViewById(R.id.star_04 ) ;
        star_05 = (ImageView)findViewById(R.id.star_05 ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        type01_btn01.setOnClickListener(this );
        type02_btn01.setOnClickListener(this );
        type03_btn01.setOnClickListener(this );
        type04_btn01.setOnClickListener(this );
        type05_btn01.setOnClickListener(this );
        type01_btn02.setOnClickListener(this );
        type02_btn02.setOnClickListener(this );
        type03_btn02.setOnClickListener(this );
        type04_btn02.setOnClickListener(this );
        type05_btn02.setOnClickListener(this );

    }

    @Override
    public void initData() {
        initIcon() ;
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
            case R.id.pre :
                finish();
                break;
            case R.id.type01_btn01 :
                title01 = "保证金认证" ;
                star_register_type01 = "本认证是针对服务方在注册/使用资芽网过程中的一项行为认证标准 ，用以保证服务方以合法的操作行为使用本网站，不得盗取本网站的信息从事违法行为、牟取暴利，并保证所填写的相关信息真实有效。开通后将点亮本认证星级，并在服务方展示页面进行展示。" ;
                other_info01 = "注：保证金认证可在缴纳三个月后随时申请取消（取消认证后本网站将全额退还保证金）。" ;
                showForVipPop( star_register_type01 , title01 , other_info01 ) ;
                break;
            case R.id.type02_btn01 :
                title01 = "实地认证" ;
                star_register_type01 = "本认证是由本网站认证人员前往服务方所在地实地考察，并依据本网站实地认证标准现场取证（如：经营场所拍照、人员拍照、实地访谈等）、材料备档。开通认证成功后将点亮本认证星级，并在服务方展示页面进行展示。" ;
                other_info01 = "注：实地认证成功后不可申请取消及退款（如遇地址变更等特殊原因可致电资芽网）。" ;
                showForVipPop( star_register_type01 , title01 , other_info01 ) ;
                break;
            case R.id.type03_btn01 :
                title01 = "视频认证" ;
                star_register_type01 = "本认证是由服务方按资芽网要求自主完成，需由服务方持拍摄设备（摄像机或手机）对服务方的经营场所（如：门头、企业名称、办公环境等）及相关员工，进行视频拍摄（一分钟以内），并同时口述相关拍摄内容（如：这是我们的员工、这是我们的前台或这是我们的会议室等），拍摄完成后，传与资芽网客服人员备档。开通认证成功后将点亮本认证星级，并在服务方展示页面进行展示。" ;
                other_info01 = "注：视频认证成功后不可申请取消及退款（如遇地址变更等特殊原因可致电资芽网）。" ;
                showForVipPop( star_register_type01 , title01 , other_info01 ) ;
                break;
            case R.id.type04_btn01 :
                title01 = "承诺书认证" ;
                star_register_type01 = "本认证不收取任何费用，由服务方点击“开通”按钮，下载承诺书并签字盖章上传至本网站；开通认证成功后将点亮本认证星级，并在服务方展示页面进行展示。" ;
                other_info01 = "注：承诺书认证成功后不可申请取消（如遇企业变更等特殊原因可致电资芽网）。" ;
                showForVipPop( star_register_type01 , title01 , other_info01 ) ;
                break;
            case R.id.type05_btn01 :
                title01 = "三证认证" ;
                star_register_type01 = "本认证不收取任何费用，由服务方点击“开通”按钮，上传三证原件（营业执照、组织机构代码证、税务登记证）或三证合一证件原件；开通认证成功后将点亮本认证星级，并在服务方展示页面进行展示（本网站将做水印和模糊处理，请放心上传）。" ;
                other_info01 = "注：三证认证成功后不可申请取消（如遇企业变更等特殊原因可致电资芽网）。" ;
                showForVipPop( star_register_type01 , title01 , other_info01 ) ;
                break;
            case R.id.type01_btn02 :
                goStarRegisterActivity01_03("保证金认证") ;
                break;
            case R.id.type02_btn02 :
                goStarRegisterActivity01_03("实地认证") ;
                break;
            case R.id.type03_btn02 :
                goStarRegisterActivity01_03("视频认证") ;
                break;
            case R.id.type04_btn02 :
                goStarRegisterActivity04() ;
                break;
            case R.id.type05_btn02 :
                goStarRegisterActivity05() ;
                break;
            default:
                break;
        }
    }

    private void goStarRegisterActivity01_03(String title) {
        Intent intent = new Intent(StarRegisterActivity.this , StarRegisterActivity01_03.class ) ;
        intent.putExtra("title" , title ) ;
        startActivity(intent);
    }

    private void goStarRegisterActivity05() {
        Intent intent = new Intent(StarRegisterActivity.this , StarRegisterActivity05.class ) ;
        startActivity(intent);
    }

    private void goStarRegisterActivity04() {
        Intent intent = new Intent(StarRegisterActivity.this , StarRegisterActivity04.class ) ;
        startActivity(intent);
    }

    private void showForVipPop( String title01 , final String star_register_type01 , final String other_info01 ) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_star_register, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.relative);
        final Button cancel02 = (Button) view.findViewById(R.id.cancel02);
        final ImageButton cancel = (ImageButton) view.findViewById(R.id.cancel);
        final JustifyTextView title = (JustifyTextView)view.findViewById(R.id.title ) ;
        final TextView star_register_type = (TextView)view.findViewById(R.id.star_register_type ) ;
        final JustifyTextView other_info = (JustifyTextView)view.findViewById(R.id.other_info ) ;
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        //取消
        cancel02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        title.setText(title01);
        star_register_type.setText(star_register_type01);
        other_info.setText(other_info01);

        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(relative, Gravity.CENTER, 0, 0);
        // 设置popWindow的显示和消失动画

        backgroundAlpha(0.2f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}
