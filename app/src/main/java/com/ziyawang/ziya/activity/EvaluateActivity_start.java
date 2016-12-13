package com.ziyawang.ziya.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.CashierInputFilter;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.view.WheelView;

public class EvaluateActivity_start extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    //评测金额
    private EditText total_money ;
    //地区
    private RelativeLayout evaluate_from_relative ;
    //地区显示
    private TextView evaluate_from_text ;
    //类型
    private RelativeLayout evaluate_type_relative ;
    //债权人类型
    private RelativeLayout evaluate_person_type_relative ;
    //开始测评按钮
    private Button evaluate_start ;
    private TextView evaluate_type_text ;

    private LinearLayout whole ;
    private TextView evaluate_person_type_text ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_evaluate_activity_start);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        total_money = (EditText) findViewById(R.id.total_money);
        total_money.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        InputFilter[] filters = {new CashierInputFilter()};
        total_money.setFilters(filters);
        evaluate_from_relative = (RelativeLayout)findViewById(R.id.evaluate_from_relative ) ;
        evaluate_from_text = (TextView)findViewById(R.id.evaluate_from_text ) ;
        evaluate_type_text = (TextView)findViewById(R.id.evaluate_type_text ) ;
        evaluate_type_relative = (RelativeLayout)findViewById(R.id.evaluate_type_relative) ;
        evaluate_person_type_relative = (RelativeLayout)findViewById(R.id.evaluate_person_type_relative) ;
        whole = (LinearLayout)findViewById(R.id.whole) ;
        evaluate_start = (Button)findViewById(R.id.evaluate_start ) ;
        evaluate_person_type_text = (TextView)findViewById(R.id.evaluate_person_type_text ) ;
    }

    @Override
    public void initListeners() {
        evaluate_from_relative.setOnClickListener(this );
        evaluate_type_relative.setOnClickListener(this );
        evaluate_person_type_relative.setOnClickListener(this );
        evaluate_start.setOnClickListener(this );
        pre.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.evaluate_from_relative :
                goGetPart() ;
                break;
            case R.id.evaluate_type_relative :
                popUpWindow(evaluate_type_relative) ;
                break;
            case R.id.evaluate_person_type_relative :
                popUpWindow(evaluate_person_type_relative) ;
                break;
            case R.id.evaluate_start :
                goEvaluateActivity_ing() ;
                break;
            case R.id.pre :
                finish();
                break;
            default:
                break;
        }
    }

    private void popUpWindow(final RelativeLayout tv) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.common_window_wheel, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final WheelView picker= (WheelView) view.findViewById(R.id.wheel);
        final WheelView picker02= (WheelView) view.findViewById(R.id.wheel02);
        final TextView wheel_title= (TextView) view.findViewById(R.id.wheel_title);
        TextView left = (TextView) view.findViewById(R.id.left);
        TextView right = (TextView) view.findViewById(R.id.right);
        picker02.setVisibility(View.GONE);
        wheel_title.setText("");
        switch (tv.getId()){
            case R.id.evaluate_type_relative :
                picker.addData("个人债权");
                picker.addData("企业商账");
                break;
            case R.id.evaluate_person_type_relative :
                picker.addData("个人");
                picker.addData("企业");
                break;
            default:
                break;
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object centerItem = picker.getCenterItem();
                if (tv.getId() == evaluate_type_relative.getId()){
                    evaluate_type_text.setText(centerItem.toString());
                }else if (tv.getId() == evaluate_person_type_relative.getId()){
                    evaluate_person_type_text.setText(centerItem.toString());
                }
                window.dismiss();
            }
        });
        //public void setLineColor(int lineColor);            //设置中间线条的颜色
        //public void setTextColor(int textColor);            //设置文字的颜色
        //public void setTextSize(float textSize);
        // 设置文字大小
        picker.setCenterItem(0);
        picker.setTextColor(Color.rgb(249, 144, 0));
        picker.setLineColor(Color.rgb(249, 144, 0)) ;
        picker02.setCenterItem(10);
        picker02.setCircle(true);
        picker02.setTextColor(Color.rgb(249, 144, 0));
        picker02.setLineColor(Color.rgb(249, 144, 0) ) ;
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(pre, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.4f);
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

    private void goEvaluateActivity_ing() {
        if (checkGoPermission()){
            Intent intent = new Intent(EvaluateActivity_start.this , EvaluateActivity_ing02.class ) ;
            intent.putExtra("Money" , total_money.getText().toString()) ;
            intent.putExtra("Area" , evaluate_from_text.getText()) ;
            intent.putExtra("AssetType" , evaluate_type_text.getText()) ;
            intent.putExtra("Type" , evaluate_person_type_text.getText()) ;
            startActivity(intent);
            finish();
        }
    }

    private boolean checkGoPermission() {
        if (TextUtils.isEmpty(total_money.getText().toString())){
            ToastUtils.shortToast(this , "请输入债权金额");
            return false ;
        }
        if ("".equals(evaluate_from_text.getText().toString())){
            ToastUtils.shortToast(this , "请选择债权人所在地");
            return false ;
        }
        if ("".equals(evaluate_type_text.getText().toString())){
            ToastUtils.shortToast(this , "请选择债权类型");
            return false ;
        }
        if ("".equals(evaluate_person_type_text.getText().toString())){
            ToastUtils.shortToast(this , "请选择债务人类型");
            return false ;
        }
        return true ;
    }

    private void goGetPart() {
        startActivityForResult(new Intent(EvaluateActivity_start.this, PartActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && null != data) {
                    String result01 = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    evaluate_from_text.setText(result01);
                }
                break;
            default:
                break;
        }
    }
}
