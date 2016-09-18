package com.ziyawang.ziya.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.SystemBarTintManager;

public class MoneyTypeActivity extends BaseActivity {

    private RadioGroup group, group2, group3,group4 ,group5,group6,group7 , group8,group9,group10,group11,group12;
    private RadioButton button01 , button02 , button03 ,button001,button04,button05,button06,button07,button08,button09,button10,button11 ,button12,button13,button14,button15;
    private RadioButton button16,button17,button18,button19,button20,button21,button22,button23,button24 ,button25,button26,button27,button28,button29;
    private RadioButton button30,button31,button32,button33,button34,button35,button36,button37,button38,button39,button40 ,v102button1,v102button2,v102button3,v102button4,v102button5,v102button6,v102button7 ;
    private RelativeLayout pre ;
    String type  ;
    private TextView title ;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_type);

        //实例化组建
        group = (RadioGroup)findViewById(R.id.group) ;
        group2 = (RadioGroup)findViewById(R.id.group2) ;
        group3 = (RadioGroup)findViewById(R.id.group3) ;
        group4 = (RadioGroup)findViewById(R.id.group4) ;
        group5 = (RadioGroup)findViewById(R.id.group5) ;
        group6 = (RadioGroup)findViewById(R.id.group6) ;
        group7 = (RadioGroup)findViewById(R.id.group7) ;
        group8 = (RadioGroup)findViewById(R.id.group8) ;
        group9 = (RadioGroup)findViewById(R.id.group9) ;
        group10 = (RadioGroup)findViewById(R.id.group10) ;
        group11 = (RadioGroup)findViewById(R.id.group11) ;
        group12 = (RadioGroup)findViewById(R.id.group12) ;
        button01 = (RadioButton)findViewById(R.id.button01 ) ;
        button02 = (RadioButton)findViewById(R.id.button02 ) ;
        button03 = (RadioButton)findViewById(R.id.button03 ) ;
        button001 = (RadioButton)findViewById(R.id.button001 ) ;
        button04 = (RadioButton)findViewById(R.id.button04 ) ;
        button05 = (RadioButton)findViewById(R.id.button05 ) ;
        button06 = (RadioButton)findViewById(R.id.button06 ) ;
        button07 = (RadioButton)findViewById(R.id.button07 ) ;
        button08 = (RadioButton)findViewById(R.id.button08 ) ;
        button09 = (RadioButton)findViewById(R.id.button09 ) ;
        button10 = (RadioButton)findViewById(R.id.button10 ) ;
        button11 = (RadioButton)findViewById(R.id.button11 ) ;
        button12 = (RadioButton)findViewById(R.id.button12 ) ;
        button13 = (RadioButton)findViewById(R.id.button13 ) ;
        button14 = (RadioButton)findViewById(R.id.button14 ) ;
        button15 = (RadioButton)findViewById(R.id.button15 ) ;
        button16 = (RadioButton)findViewById(R.id.button16 ) ;
        button17 = (RadioButton)findViewById(R.id.button17 ) ;
        button18 = (RadioButton)findViewById(R.id.button18 ) ;
        button19 = (RadioButton)findViewById(R.id.button19 ) ;
        button20 = (RadioButton)findViewById(R.id.button20 ) ;
        button21 = (RadioButton)findViewById(R.id.button21 ) ;
        button22 = (RadioButton)findViewById(R.id.button22 ) ;
        button23 = (RadioButton)findViewById(R.id.button23 ) ;
        button24 = (RadioButton)findViewById(R.id.button24 ) ;
        button29 = (RadioButton)findViewById(R.id.button29 ) ;
        button25 = (RadioButton)findViewById(R.id.button25 ) ;
        button26 = (RadioButton)findViewById(R.id.button26 ) ;
        button27 = (RadioButton)findViewById(R.id.button27 ) ;
        button28 = (RadioButton)findViewById(R.id.button28 ) ;
        button30 = (RadioButton)findViewById(R.id.button30 ) ;
        button31 = (RadioButton)findViewById(R.id.button31 ) ;
        button32 = (RadioButton)findViewById(R.id.button32 ) ;
        button33 = (RadioButton)findViewById(R.id.button33 ) ;
        button34 = (RadioButton)findViewById(R.id.button34 ) ;
        button35 = (RadioButton)findViewById(R.id.button35 ) ;
        button36 = (RadioButton)findViewById(R.id.button36 ) ;
        button37 = (RadioButton)findViewById(R.id.button37 ) ;
        button38 = (RadioButton)findViewById(R.id.button38 ) ;
        button39 = (RadioButton)findViewById(R.id.button39 ) ;
        button40 = (RadioButton)findViewById(R.id.button40 ) ;
        v102button1 = (RadioButton)findViewById(R.id.v102button1 ) ;
        v102button2 = (RadioButton)findViewById(R.id.v102button2 ) ;
        v102button3 = (RadioButton)findViewById(R.id.v102button3 ) ;
        v102button4 = (RadioButton)findViewById(R.id.v102button4 ) ;
        v102button5 = (RadioButton)findViewById(R.id.v102button5 ) ;
        v102button6 = (RadioButton)findViewById(R.id.v102button6 ) ;
        v102button7 = (RadioButton)findViewById(R.id.v102button7 ) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        title = (TextView)findViewById(R.id.title ) ;

        //设置通知栏的颜色
        changeTitleCocor() ;

        //回调监听
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent() ;
        String head = intent.getStringExtra("title");
        String aaa = intent.getStringExtra("type") ;

        title.setText(head);

        switch (aaa){
            case "1" :
                group.setVisibility(View.VISIBLE);
                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button01 :
                                type = "抵押" ;
                                break;
                            case R.id.button02 :
                                type = "信用" ;
                                break;
                            case R.id.button03 :
                                type = "综合类" ;
                                break;
                            case R.id.button001 :
                                type = "其他" ;
                                break;
                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });
                break;
            case "2" :
                group2.setVisibility(View.VISIBLE);
                group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button04 :
                                type = "个人债权" ;
                                break;
                            case R.id.button05 :
                                type = "企业商账" ;
                                break;
                            case R.id.button06 :
                                type = "其他" ;
                                break;
                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });

                break;
            case "3" :
                group3.setVisibility(View.VISIBLE);
                group3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button07 :
                                type = "个人资产" ;
                                break;
                            case R.id.button08 :
                                type = "企业资产" ;
                                break;
                            case R.id.button09 :
                                type = "法拍资产" ;
                                break;
                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });
                break;
            case "4" :
                group4.setVisibility(View.VISIBLE);
                group4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button12 :
                                type = "国企" ;
                                break;
                            case R.id.button13 :
                                type = "民企" ;
                                break;
                            case R.id.button14 :
                                type = "上市公司" ;
                                break;
                            case R.id.button15 :
                                type = "其他" ;
                                break;
                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });

                break;
            case "5" :
                group5.setVisibility(View.VISIBLE);
                group5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button16 :
                                type = "典当" ;
                                break;
                            case R.id.button17 :
                                type = "担保" ;
                                break;
                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });

                break;
            case "6" :
                group6.setVisibility(View.VISIBLE);
                group6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button18 :
                                type = "抵押" ;
                                break;
                            case R.id.button19 :
                                type = "质押" ;
                                break;
                            case R.id.button20 :
                                type = "租赁" ;
                                break;
                            case R.id.button21 :
                                type = "过桥" ;
                                break;
                            case R.id.button22 :
                                type = "信用" ;
                                break;
                            case R.id.v102button1 :
                                type = "股权" ;
                                break;
                            case R.id.v102button2 :
                                type = "担保" ;
                                break;
                            case R.id.v102button3 :
                                type = "其他" ;
                                break;
                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });

                break;
            case "7" :
                group7.setVisibility(View.VISIBLE);
                group7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button23 :
                                type = "找人" ;
                                break;
                            case R.id.button24 :
                                type = "找财产" ;
                                break;

                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });

                break;
            case "8" :
                group8.setVisibility(View.VISIBLE);
                group8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button25 :
                                type = "法律" ;
                                break;
                            case R.id.button26 :
                                type = "财务" ;
                                break;
                            case R.id.button27 :
                                type = "税务" ;
                                break;
                            case R.id.button28 :
                                type = "商业" ;
                                break;
                            case R.id.button29 :
                                type = "其他" ;
                                break;


                            default:
                                break;
                        }
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });

                break;
            case "9" :
                group9.setVisibility(View.VISIBLE);
                group9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button30 :
                                type = "个人债权" ;
                                break;
                            case R.id.button31 :
                                type = "银行贷款" ;
                                break;
                            case R.id.button32 :
                                type = "企业商账" ;
                                break;

                            default:
                                break;
                        }
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });

                break;
            case "10" :
                group10.setVisibility(View.VISIBLE);
                group10.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button33 :
                                type = "民事" ;
                                break;
                            case R.id.button34 :
                                type = "刑事" ;
                                break;
                            case R.id.button35 :
                                type = "经济" ;
                                break;
                            case R.id.button36 :
                                type = "公司" ;
                                break;

                            default:
                                break;
                        }
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });

                break;
            case "11" :
                group11.setVisibility(View.VISIBLE);
                group11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.button37 :
                                type = "土地" ;
                                break;
                            case R.id.button38 :
                                type = "房产" ;
                                break;
                            case R.id.button39 :
                                type = "汽车" ;
                                break;
                            case R.id.button40 :
                                type = "其他" ;
                                break;

                            default:
                                break;
                        }
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });

                break;
            case "12" :
                group12.setVisibility(View.VISIBLE);
                group12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.v102button4 :
                                type = "个人" ;
                                break;
                            case R.id.v102button5 :
                                type = "企业" ;
                                break;
                            case R.id.v102button6 :
                                type = "机构" ;
                                break;
                            case R.id.v102button7 :
                                type = "其他" ;
                                break;
                            default:
                                break;
                        }
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", type );
                        //设置返回数据
                        MoneyTypeActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        MoneyTypeActivity.this.finish();
                    }
                });
                break;
        }
    }


    private void changeTitleCocor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.aaa);//通知栏所需颜色

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


}
