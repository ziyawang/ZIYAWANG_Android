package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.view.JustifyTextView;

public class EvaluateResultActivity extends BenBenActivity implements View.OnClickListener {

    //返回按钮
    private RelativeLayout pre ;
    private static String score ;
    private static String result ;

    private TextView evaluate_score ;
    private JustifyTextView evaluate_des ;
    //重新测评
    private Button again ;

    //确认按钮
    private Button sub ;

    private SharedPreferences isThree ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_evaluate_result);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        evaluate_score = (TextView)findViewById(R.id.evaluate_score ) ;
        evaluate_des = (JustifyTextView)findViewById(R.id.evaluate_des ) ;
        sub = (Button)findViewById(R.id.sub ) ;
        again = (Button)findViewById(R.id.again ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        sub.setOnClickListener(this);
        again.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        score = intent.getStringExtra("score");
        result = intent.getStringExtra("result");
        evaluate_score.setText("评测分数: " + score + "分");
        evaluate_des.setText(result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.sub :
                isThree = getSharedPreferences("isThree" , MODE_PRIVATE ) ;
                isThree.edit().putBoolean("isThree", true).commit();
                finish();
                break;
            case R.id.again :
                Intent intent = new Intent(EvaluateResultActivity.this , EvaluateActivity_start.class ) ;
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
