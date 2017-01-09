package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.view.JustifyTextView;

public class KnowPowerActivity extends BenBenActivity implements View.OnClickListener {
    //返回按钮
    private RelativeLayout pre ;
    private ImageView img_vip_type ;
    private TextView text_one , text_two , text_three , text_four ;
    private JustifyTextView justifyText_one , justifyText_two , justifyText_three , justifyText_four ;
    private String type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_know_power);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        img_vip_type = (ImageView)findViewById(R.id.img_vip_type ) ;
        text_one = (TextView)findViewById(R.id.text_one ) ;
        text_two = (TextView)findViewById(R.id.text_two ) ;
        text_three = (TextView)findViewById(R.id.text_three ) ;
        text_four = (TextView)findViewById(R.id.text_four ) ;
        justifyText_one = (JustifyTextView)findViewById(R.id.justifyText_one ) ;
        justifyText_two = (JustifyTextView)findViewById(R.id.justifyText_two ) ;
        justifyText_three = (JustifyTextView)findViewById(R.id.justifyText_three ) ;
        justifyText_four = (JustifyTextView)findViewById(R.id.justifyText_four ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        if (intent != null ){
            type = intent.getStringExtra("type");
        }
        switch (type){
            case "01" :
                img_vip_type.setImageResource(R.mipmap.v2020401);
                text_one.setText("查看权限");
                text_two.setText("赠送芽币");
                text_three.setText("定制推送");
                text_four.setText("联系方式展示");
                justifyText_one.setText("在会员期间内，可免费查看本网站所有资产包类VIP和收费信息。");
                justifyText_two.setText("按办理会员的价格赠送芽币（例：月度会员6498元赠送6498芽币，年度会员70000元赠送70000芽币）。可方便您查看其他类型信息。");
                justifyText_three.setText("系统会根据您的指定需求帮您筛选指定的资产包信息，并推送到您的系统消息或手机短信中（客服人员会主动联系您录入需求）。");
                justifyText_four.setText("升级为会员后，您服务方的展示页面联系方式将公开，会有更多用户第一时间主动联系您。");
                break;
            case "02" :
                img_vip_type.setImageResource(R.mipmap.v2020402);
                text_one.setText("查看权限");
                text_two.setText("赠送芽币");
                text_three.setText("定制推送");
                text_four.setText("联系方式展示");
                justifyText_one.setText("在会员期间内，可免费查看本网站所有企业商账类VIP和收费信息。");
                justifyText_two.setText("按办理会员的价格赠送芽币（例：季度会员1498元赠送1498芽币，年度会员4998元赠送4998芽币）。可方便您查看其他类型信息。");
                justifyText_three.setText("系统会根据您的指定需求帮您筛选指定的企业商账信息，并推送到您的系统消息或手机短信中（客服人员会主动联系您录入需求）。");
                justifyText_four.setText("升级为会员后，您服务方的展示页面联系方式将公开，会有更多用户第一时间主动联系您。");
                break;
            case "03" :
                img_vip_type.setImageResource(R.mipmap.v2020403);
                text_one.setText("查看权限");
                text_two.setText("赠送芽币");
                text_three.setText("定制推送");
                text_four.setText("联系方式展示");
                justifyText_one.setText("在会员期间内，可免费查看本网站所有固定资产类VIP和收费信息。");
                justifyText_two.setText("按办理会员的价格赠送芽币（例：季度会员3998元赠送3998芽币，年度会员12000元赠送12000芽币）。可方便您查看其他类型信息。");
                justifyText_three.setText("系统会根据您的指定需求帮您筛选指定的固定资产信息，并推送到您的系统消息或手机短信中（客服人员会主动联系您录入需求）。");
                justifyText_four.setText("升级为会员后，您服务方的展示页面联系方式将公开，会有更多用户第一时间主动联系您。");
                break;
            case "04" :
                img_vip_type.setImageResource(R.mipmap.v2020404);
                text_one.setText("查看权限");
                text_two.setText("赠送芽币");
                text_three.setText("定制推送");
                text_four.setText("联系方式展示");
                justifyText_one.setText("在会员期间内，可免费查看本网站所有融资信息类VIP和收费信息。");
                justifyText_two.setText("按办理会员的价格赠送芽币（例：季度会员998元赠送998芽币，年度会员2998元赠送2998芽币）。可方便您查看其他类型信息。");
                justifyText_three.setText("系统会根据您的指定需求帮您筛选指定的融资信息信息，并推送到您的系统消息或手机短信中（客服人员会主动联系您录入需求）。");
                justifyText_four.setText("升级为会员后，您服务方的展示页面联系方式将公开，会有更多用户第一时间主动联系您。");
                break;
            case "05" :
                img_vip_type.setImageResource(R.mipmap.v2020405);
                text_one.setText("查看权限");
                text_two.setText("赠送芽币");
                text_three.setText("定制推送");
                text_four.setText("联系方式展示");
                justifyText_one.setText("在会员期间内，可免费查看本网站所有个人债权类VIP和收费信息。");
                justifyText_two.setText("按办理会员的价格赠送芽币（例：季度会员998元赠送998芽币，年度会员2998元赠送2998芽币）。可方便您查看其他类型信息。");
                justifyText_three.setText("系统会根据您的指定需求帮您筛选指定的个人债权信息，并推送到您的系统消息或手机短信中（客服人员会主动联系您录入需求）。");
                justifyText_four.setText("升级为会员后，您服务方的展示页面联系方式将公开，会有更多用户第一时间主动联系您。");
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
            default:
                break;

        }
    }
}
