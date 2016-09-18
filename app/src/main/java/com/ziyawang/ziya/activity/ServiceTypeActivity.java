package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;

public class ServiceTypeActivity extends BaseActivity {

    private RelativeLayout pre ;
    private TextView submit ;
    private StringBuffer stringBuffer = new StringBuffer()  ;

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
        setContentView(R.layout.activity_service_type);
        //实例化组建
        initView() ;

        final CheckBox a = (CheckBox)findViewById(R.id.a);
        final CheckBox b = (CheckBox)findViewById(R.id.b);
        final CheckBox c = (CheckBox)findViewById(R.id.c);
        final CheckBox d = (CheckBox)findViewById(R.id.d);
        final CheckBox e = (CheckBox)findViewById(R.id.e);
        final CheckBox f = (CheckBox)findViewById(R.id.f);
        final CheckBox g = (CheckBox)findViewById(R.id.g);
        final CheckBox h = (CheckBox)findViewById(R.id.h);
        final CheckBox i = (CheckBox)findViewById(R.id.i);
        final CheckBox j = (CheckBox)findViewById(R.id.j);

//        a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked && !stringBuffer.toString().contains("01")) {
//                    stringBuffer.append("01");
//                    stringBuffer.append(",") ;
//                }
//            }
//        });
//        b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked && !stringBuffer.toString().contains("02")) {
//                    stringBuffer.append("02");
//                    stringBuffer.append(",") ;
//                }
//            }
//        });
//        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked && !stringBuffer.toString().contains("03")) {
//                    stringBuffer.append("03") ;
//                    stringBuffer.append(",") ;
//                }
//            }
//        });
//        d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked && !stringBuffer.toString().contains("04")) {
//                    stringBuffer.append("04");
//                    stringBuffer.append(",") ;
//                }
//            }
//        });
//        e.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked && !stringBuffer.toString().contains("06")) {
//                    stringBuffer.append("06");
//                    stringBuffer.append(",") ;
//                }
//            }
//        });
//        f.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked && !stringBuffer.toString().contains("10")) {
//                    stringBuffer.append("10");
//                    stringBuffer.append(",") ;
//                }
//            }
//        });
//        g.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked && !stringBuffer.toString().contains("05")) {
//                    stringBuffer.append("05");
//                    stringBuffer.append(",") ;
//                }
//            }
//        });
//        h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked && !stringBuffer.toString().contains("ben")) {
//                    stringBuffer.append("ben");
//                    stringBuffer.append(",") ;
//                }
//            }
//        });
//        i.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked && !stringBuffer.toString().contains("12")) {
//                    stringBuffer.append("12");
//                    stringBuffer.append(",") ;
//                }
//            }
//        });
//        j.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked && !stringBuffer.toString().contains("14")) {
//                    stringBuffer.append("14");
//                    stringBuffer.append(",") ;
//                }
//            }
//        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (a.isChecked() ) {
                    stringBuffer.append("01");
                    stringBuffer.append(",") ;
                }
                if (b.isChecked() ) {
                    stringBuffer.append("02");
                    stringBuffer.append(",") ;
                }
                if (c.isChecked() ) {
                    stringBuffer.append("03");
                    stringBuffer.append(",") ;
                }
                if (d.isChecked() ) {
                    stringBuffer.append("04");
                    stringBuffer.append(",") ;
                }
                if (e.isChecked() ) {
                    stringBuffer.append("06");
                    stringBuffer.append(",") ;
                }
                if (f.isChecked() ) {
                    stringBuffer.append("10");
                    stringBuffer.append(",") ;
                }
                if (g.isChecked() ) {
                    stringBuffer.append("05");
                    stringBuffer.append(",") ;
                }
                if (h.isChecked() ) {
                    stringBuffer.append("ben");
                    stringBuffer.append(",") ;
                }
                if (i.isChecked() ) {
                    stringBuffer.append("12");
                    stringBuffer.append(",") ;
                }
                if (j.isChecked() ) {
                    stringBuffer.append("14");
                    stringBuffer.append(",") ;
                }

                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result", stringBuffer.toString() );
                Log.e("benben", stringBuffer.toString()) ;
                //设置返回数据
                ServiceTypeActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ServiceTypeActivity.this.finish();

            }
        });
    }

    private void initView() {

        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        submit = (TextView)findViewById(R.id.submit ) ;
    }
}
