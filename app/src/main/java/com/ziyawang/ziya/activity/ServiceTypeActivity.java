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
        //20180305add
        final CheckBox k = (CheckBox)findViewById(R.id.k);
        final CheckBox l = (CheckBox)findViewById(R.id.l);
        final CheckBox m = (CheckBox)findViewById(R.id.m);
        final CheckBox n = (CheckBox)findViewById(R.id.n);
        final CheckBox o = (CheckBox)findViewById(R.id.o);
        final CheckBox p = (CheckBox)findViewById(R.id.p);
        final CheckBox q = (CheckBox)findViewById(R.id.q);
        final CheckBox r = (CheckBox)findViewById(R.id.r);
        final CheckBox s = (CheckBox)findViewById(R.id.s);
        final CheckBox t = (CheckBox)findViewById(R.id.t);
        final CheckBox u = (CheckBox)findViewById(R.id.u);


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

                // 20180305 add
                if (k.isChecked() ) {
                    stringBuffer.append("23");
                    stringBuffer.append(",") ;
                }
                if (l.isChecked() ) {
                    stringBuffer.append("24");
                    stringBuffer.append(",") ;
                }
                if (m.isChecked() ) {
                    stringBuffer.append("25");
                    stringBuffer.append(",") ;
                }
                if (n.isChecked() ) {
                    stringBuffer.append("26");
                    stringBuffer.append(",") ;
                }
                if (o.isChecked() ) {
                    stringBuffer.append("27");
                    stringBuffer.append(",") ;
                }
                if (p.isChecked() ) {
                    stringBuffer.append("28");
                    stringBuffer.append(",") ;
                }
                if (q.isChecked() ) {
                    stringBuffer.append("29");
                    stringBuffer.append(",") ;
                }
                if (r.isChecked() ) {
                    stringBuffer.append("30");
                    stringBuffer.append(",") ;
                }
                if (s.isChecked() ) {
                    stringBuffer.append("31");
                    stringBuffer.append(",") ;
                }
                if (t.isChecked() ) {
                    stringBuffer.append("32");
                    stringBuffer.append(",") ;
                }
                if (u.isChecked() ) {
                    stringBuffer.append("33");
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
