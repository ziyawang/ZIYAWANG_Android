package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziyawang.ziya.R;

public class GoldDetails02Activity extends BenBenActivity implements View.OnClickListener {

    //回退按钮
    private RelativeLayout pre ;
    //种类
    private TextView item_niu ;
    //花费金额
    private TextView item_money ;
    //单位
    private TextView item_color ;
    //明细的类型
    private TextView item_type ;
    //明细的时间
    private TextView item_time ;
    //明细的订单号
    private TextView item_orderNumber ;
    //明细的操作
    private TextView item_do ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_gold_details02);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        item_money = (TextView)findViewById(R.id.item_money ) ;
        item_color = (TextView)findViewById(R.id.item_color ) ;
        item_type = (TextView)findViewById(R.id.item_type ) ;
        item_time = (TextView)findViewById(R.id.item_time ) ;
        item_orderNumber = (TextView)findViewById(R.id.item_orderNumber ) ;
        item_do = (TextView)findViewById(R.id.item_do ) ;
        item_niu = (TextView)findViewById(R.id.item_niu ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
    }

    @Override
    public void initData() {

        Intent intent = getIntent() ;
        String money = intent.getStringExtra("money");
        String type = intent.getStringExtra("type");
        String time = intent.getStringExtra("time");
        String orderNumber = intent.getStringExtra("orderNumber");
        String operates = intent.getStringExtra("operates");

        //1是充值，2是消费
        if ("1".equals(type)){
            item_niu.setText("芽币充值");
            item_money.setText("+" + money);
            item_money.setTextColor(Color.rgb(144, 195, 31));
            item_color.setTextColor(Color.rgb(144,195,31));
            item_type.setText("充值");
        }else if ("2".equals(type)){
            item_niu.setText("付费约谈");
            item_money.setText("-" + money);
            item_money.setTextColor(Color.rgb(235, 102, 90));
            item_color.setTextColor(Color.rgb(235,102,90));
            item_type.setText("付费");
        }

        item_time.setText(time);
        item_orderNumber.setText(orderNumber) ;
        item_do.setText(operates);
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
