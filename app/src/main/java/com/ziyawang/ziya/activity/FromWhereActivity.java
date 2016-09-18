package com.ziyawang.ziya.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.SystemBarTintManager;

public class FromWhereActivity extends BaseActivity {

    private RelativeLayout pre ;
    private RadioGroup group,group2 ,group3,group4,group5,group6,group7,group8,group9,group10;
    private RadioButton button01 , button02 ,button03,button04,button05 ,button06,button07,button08,button09,button10,button11,button13,button14,button15,button16,button17,button18,button12;
    private RadioButton button19,button20,button21,button22,button23,button24,button25,button26 ;
    private RadioButton v102button1,v102button2,v102button3,v102button4,v102button5,v102button6,v102button7,v102button8,v102button9,v102button10 ;
    String fromWhere  ;
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
        setContentView(R.layout.activity_from_where);

        //实例化组件
        initView() ;

        Intent intent = getIntent()  ;
        String title_a = intent.getStringExtra("title");
        String type = intent.getStringExtra("type");

        title.setText(title_a);
        switch (type){
            case "1" :
                group.setVisibility(View.VISIBLE);
                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.button01:
                                fromWhere = "银行";
                                break;
                            case R.id.button02:
                                fromWhere = "非银行金融机构";
                                break;
                            case R.id.button03:
                                fromWhere = "企业";
                                break;
                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", fromWhere);
                        //设置返回数据
                        FromWhereActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        FromWhereActivity.this.finish();
                    }
                });
                break;
            case "2" :
                group2.setVisibility(View.VISIBLE);
                group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.button04:
                                fromWhere = "企业";
                                break;
                            case R.id.button05:
                                fromWhere = "个人";
                                break;

                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", fromWhere);
                        //设置返回数据
                        FromWhereActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        FromWhereActivity.this.finish();
                    }
                });
                break;
            case "3" :
                group3.setVisibility(View.VISIBLE);
                group3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.button06:
                                fromWhere = "已诉讼";
                                break;
                            case R.id.button07:
                                fromWhere = "未诉讼";
                                break;

                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", fromWhere);
                        //设置返回数据
                        FromWhereActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        FromWhereActivity.this.finish();
                    }
                });
                break;
            case "4" :
                group4.setVisibility(View.VISIBLE);
                group4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.button08:
                                fromWhere = "咨询";
                                break;
                            case R.id.button09:
                                fromWhere = "诉讼";
                                break;
                            case R.id.button10:
                                fromWhere = "其他";
                                break;

                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", fromWhere);
                        //设置返回数据
                        FromWhereActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        FromWhereActivity.this.finish();
                    }
                });
                break;
            case "5" :
                group5.setVisibility(View.VISIBLE);
                group5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.button11:
                                fromWhere = "个人";
                                break;
                            case R.id.button12:
                                fromWhere = "企业";
                                break;

                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", fromWhere);
                        //设置返回数据
                        FromWhereActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        FromWhereActivity.this.finish();
                    }
                });
                break;
            case "6" :
                group6.setVisibility(View.VISIBLE);
                group6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.button13:
                                fromWhere = "5%-15%";
                                break;
                            case R.id.button14:
                                fromWhere = "15%-30%";
                                break;
                            case R.id.button15:
                                fromWhere = "30%-50%";
                                break;
                            case R.id.button16:
                                fromWhere = "50%以上";
                                break;

                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", fromWhere);
                        //设置返回数据
                        FromWhereActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        FromWhereActivity.this.finish();
                    }
                });
                break;
            case "7" :
                group7.setVisibility(View.VISIBLE);
                group7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.button17:
                                fromWhere = "已诉讼";
                                break;
                            case R.id.button18:
                                fromWhere = "未诉讼";
                                break;


                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", fromWhere);
                        //设置返回数据
                        FromWhereActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        FromWhereActivity.this.finish();
                    }
                });
                break;
            case "8" :
                group8.setVisibility(View.VISIBLE);
                group8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.button19:
                                fromWhere = "土地";
                                break;
                            case R.id.button20:
                                fromWhere = "房产";
                                break;
                            case R.id.button21:
                                fromWhere = "汽车";
                                break;
                            case R.id.button22:
                                fromWhere = "项目";
                                break;
                            case R.id.button23:
                                fromWhere = "其他";
                                break;
                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", fromWhere);
                        //设置返回数据
                        FromWhereActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        FromWhereActivity.this.finish();
                    }
                });
                break;
            case "9" :
                group9.setVisibility(View.VISIBLE);
                group9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.button24:
                                fromWhere = "债权";
                                break;
                            case R.id.button25:
                                fromWhere = "股权";
                                break;
                            case R.id.button26:
                                fromWhere = "其他";
                                break;
                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", fromWhere);
                        //设置返回数据
                        FromWhereActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        FromWhereActivity.this.finish();
                    }
                });
                break;
            case "10" :
                group10.setVisibility(View.VISIBLE);
                group10.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.v102button1:
                                fromWhere = "1年";
                                break;
                            case R.id.v102button2:
                                fromWhere = "2年";
                                break;
                            case R.id.v102button3:
                                fromWhere = "3年";
                                break;
                            case R.id.v102button4:
                                fromWhere = "4年";
                                break;
                            case R.id.v102button5:
                                fromWhere = "5年";
                                break;
                            case R.id.v102button6:
                                fromWhere = "6年";
                                break;
                            case R.id.v102button7:
                                fromWhere = "7年";
                                break;
                            case R.id.v102button8:
                                fromWhere = "8年";
                                break;
                            case R.id.v102button9:
                                fromWhere = "9年";
                                break;
                            case R.id.v102button10:
                                fromWhere = "10年";
                                break;
                            default:
                                break;
                        }

                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("result", fromWhere);
                        //设置返回数据
                        FromWhereActivity.this.setResult(RESULT_OK, intent);
                        //关闭Activity
                        FromWhereActivity.this.finish();
                    }
                });
                break;
            default:
                break;
        }

        //设置通知栏的颜色
        changeTitleCocor() ;

        //对布局的回退事件添加监听事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    private void initView() {

        title = (TextView)findViewById(R.id.title ) ;

        pre = (RelativeLayout)findViewById(R.id.pre ) ;
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
        button01 = (RadioButton)findViewById(R.id.button01 ) ;
        button02 = (RadioButton)findViewById(R.id.button02 ) ;
        button03 = (RadioButton)findViewById(R.id.button03 ) ;
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
        button25 = (RadioButton)findViewById(R.id.button25 ) ;
        button26 = (RadioButton)findViewById(R.id.button26 ) ;
        v102button1 = (RadioButton)findViewById(R.id.v102button1 ) ;
        v102button2 = (RadioButton)findViewById(R.id.v102button2 ) ;
        v102button3 = (RadioButton)findViewById(R.id.v102button3 ) ;
        v102button4 = (RadioButton)findViewById(R.id.v102button4 ) ;
        v102button5 = (RadioButton)findViewById(R.id.v102button5 ) ;
        v102button6 = (RadioButton)findViewById(R.id.v102button6 ) ;
        v102button7 = (RadioButton)findViewById(R.id.v102button7 ) ;
        v102button8 = (RadioButton)findViewById(R.id.v102button8 ) ;
        v102button9 = (RadioButton)findViewById(R.id.v102button9 ) ;
        v102button10 = (RadioButton)findViewById(R.id.v102button10 ) ;

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
