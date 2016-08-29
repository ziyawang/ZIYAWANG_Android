package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.ToastUtils;

import java.util.HashSet;
import java.util.Set;

public class ServicePartActivity extends BaseActivity {

    private RelativeLayout pre;

    //private StringBuffer stringBuffer = new StringBuffer() ;
    private String part = "" ;

    private int count = 0 ;

    //private Set set = new HashSet();

    private TextView submit;


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
        setContentView(R.layout.activity_service_part);

        //实例化组件
        initView();

        final CheckBox a = (CheckBox) findViewById(R.id.a);
        final CheckBox b = (CheckBox) findViewById(R.id.b);
        final CheckBox c = (CheckBox) findViewById(R.id.c);
        final CheckBox d = (CheckBox) findViewById(R.id.d);
        final CheckBox e = (CheckBox) findViewById(R.id.e);
        final CheckBox f = (CheckBox) findViewById(R.id.f);
        final CheckBox g = (CheckBox) findViewById(R.id.g);
        final CheckBox h = (CheckBox) findViewById(R.id.h);
        final CheckBox i = (CheckBox) findViewById(R.id.i);
        final CheckBox j = (CheckBox) findViewById(R.id.j);
        final CheckBox k = (CheckBox) findViewById(R.id.k);
        final CheckBox l = (CheckBox) findViewById(R.id.l);
        final CheckBox m = (CheckBox) findViewById(R.id.m);
        final CheckBox n = (CheckBox) findViewById(R.id.n);
        final CheckBox o = (CheckBox) findViewById(R.id.o);
        final CheckBox p = (CheckBox) findViewById(R.id.p);
        final CheckBox q = (CheckBox) findViewById(R.id.q);
        final CheckBox r = (CheckBox) findViewById(R.id.r);
        final CheckBox s = (CheckBox) findViewById(R.id.s);
        final CheckBox t = (CheckBox) findViewById(R.id.t);
        final CheckBox u = (CheckBox) findViewById(R.id.u);
        final CheckBox vv = (CheckBox) findViewById(R.id.v);
        final CheckBox w = (CheckBox) findViewById(R.id.w);
        final CheckBox x = (CheckBox) findViewById(R.id.x);
        final CheckBox y = (CheckBox) findViewById(R.id.y);
        final CheckBox z = (CheckBox) findViewById(R.id.z);
        final CheckBox aa = (CheckBox) findViewById(R.id.aa);
        final CheckBox bb = (CheckBox) findViewById(R.id.bb);
        final CheckBox cc = (CheckBox) findViewById(R.id.cc);
        final CheckBox dd = (CheckBox) findViewById(R.id.dd);
        final CheckBox ee = (CheckBox) findViewById(R.id.ee);
        final CheckBox ff = (CheckBox) findViewById(R.id.ff);

        //添加回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //对保存添加监听事件
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a.isChecked()) {
                    part = "全国";
                }else {
                    if (b.isChecked()){
                        part += b.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (c.isChecked()){
                        part += c.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (d.isChecked()){
                        part += d.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (e.isChecked()){
                        part += e.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (f.isChecked()){
                        part += f.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (g.isChecked()){
                        part += g.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (h.isChecked()){
                        part += h.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (i.isChecked()){
                        part += i.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (j.isChecked()){
                        part += j.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (k.isChecked()){
                        part += k.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (l.isChecked()){
                        part += l.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (m.isChecked()){
                        part += m.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (n.isChecked()){
                        part += n.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (o.isChecked()){
                        part += o.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (p.isChecked()){
                        part += p.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (q.isChecked()){
                        part += q.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (r.isChecked()){
                        part += r.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (s.isChecked()){
                        part += s.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (t.isChecked()){
                        part += t.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (u.isChecked()){
                        part += u.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (vv.isChecked()){
                        part += vv.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (w.isChecked()){
                        part += w.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (x.isChecked()){
                        part += x.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (y.isChecked()){
                        part += y.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (z.isChecked()){
                        part += z.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (aa.isChecked()){
                        part += aa.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (bb.isChecked()){
                        part += bb.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (cc.isChecked()){
                        part += cc.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (dd.isChecked()){
                        part += dd.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (ee.isChecked()){
                        part += ee.getText().toString() + " " ;
                        count += 1 ;
                    }
                    if (ff.isChecked()){
                        part += ff.getText().toString() + " " ;
                        count += 1 ;
                    }

                    if (count > 5 ){
                        part = "全国" ;
                    }

                }
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result", part);
                Log.e("benben",  part  );
                //设置返回数据
                ServicePartActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ServicePartActivity.this.finish();
            }
        });
    }


    private void initView() {

        pre = (RelativeLayout) findViewById(R.id.pre);
        submit = (TextView) findViewById(R.id.submit);

    }
}
