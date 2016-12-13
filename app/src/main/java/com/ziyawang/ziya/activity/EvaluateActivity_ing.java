package com.ziyawang.ziya.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.entity.QuestionsEntity;
import com.ziyawang.ziya.tools.Json_Questions;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluateActivity_ing extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;

    private static String Money ;
    private static String Area ;
    private static String AssetType ;
    private static String Type ;

    //题目id
    private TextView question_id ;
    //题目des
    private TextView question_des ;
    //手打选项
    private EditText answer_edit ;

    //上一题
    private Button question_left ;
    //下一题
    private Button question_right ;
    //提交按钮
    private Button question_submit ;
    private List<QuestionsEntity> list ;

    private RadioGroup radio_group ;
    private RadioButton radio_button_01 , radio_button_02 , radio_button_03 , radio_button_04 , radio_button_05 , radio_button_06 , radio_button_07 , radio_button_08 ;
    private LinearLayout linear_01 ;
    private CheckBox checkbox_01 ;

    private MyProgressDialog dialog ;
    private int current_id = 0 ;
    private Map map_answer  = new HashMap() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //增加双向监听
        addTwoListeners() ;
    }

    private void addTwoListeners() {
        checkbox_01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkbox_01.isChecked()) {
                    answer_edit.setText(null);
                }
            }
        });
        answer_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkbox_01.setChecked(false);
                if (s.toString().length() == 0) {
                    checkbox_01.setChecked(true);
                }
            }
        });
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_evaluate_activity_ing);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout) findViewById(R.id.pre ) ;
        question_id = (TextView)findViewById(R.id.question_id ) ;
        question_des = (TextView)findViewById(R.id.question_des ) ;
        answer_edit = (EditText)findViewById(R.id.answer_edit ) ;
        question_left = (Button)findViewById(R.id.question_left ) ;
        question_right = (Button)findViewById(R.id.question_right ) ;
        question_submit = (Button)findViewById(R.id.question_submit ) ;
        radio_group = (RadioGroup)findViewById(R.id.radio_group ) ;
        radio_button_01 = (RadioButton)findViewById(R.id.radio_button_01 ) ;
        radio_button_02 = (RadioButton)findViewById(R.id.radio_button_02 ) ;
        radio_button_03 = (RadioButton)findViewById(R.id.radio_button_03 ) ;
        radio_button_04 = (RadioButton)findViewById(R.id.radio_button_04 ) ;
        radio_button_05 = (RadioButton)findViewById(R.id.radio_button_05 ) ;
        radio_button_06 = (RadioButton)findViewById(R.id.radio_button_06 ) ;
        radio_button_07 = (RadioButton)findViewById(R.id.radio_button_07 ) ;
        radio_button_08 = (RadioButton)findViewById(R.id.radio_button_08 ) ;
        linear_01 = (LinearLayout)findViewById(R.id.linear_01) ;
        checkbox_01 = (CheckBox)findViewById(R.id.checkbox_01 ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this );
        question_left.setOnClickListener(this);
        question_right.setOnClickListener(this);
        question_submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        Money = intent.getStringExtra("Money");
        Area = intent.getStringExtra("Area");
        AssetType = intent.getStringExtra("AssetType");
        Type = intent.getStringExtra("Type");
        showBenDialog() ;
        getQuestions() ;
    }

    private void getQuestions() {
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        if ("企业".equals(Type)){
            params.addQueryStringParameter("Paper", "2");
        }else if ("个人".equals(Type)){
            params.addQueryStringParameter("Paper", "1");
        }
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.GetQuestions, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                hiddenBenDialog();
                Log.e("海丰", responseInfo.result);
                try {
                    list = Json_Questions.getParse(responseInfo.result);
                    showData(current_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                hiddenBenDialog();
                error.printStackTrace();
                ToastUtils.shortToast(EvaluateActivity_ing.this, "网络连接异常");
            }
        }) ;
    }

    private void showData(int current_question) {
        if (list.size()!=0){
            radio_button_01.setChecked(false);
            radio_button_02.setChecked(false);
            radio_button_03.setChecked(false);
            radio_button_04.setChecked(false);
            radio_button_05.setChecked(false);
            radio_button_06.setChecked(false);
            radio_button_07.setChecked(false);
            radio_button_08.setChecked(false);
//        radio_button_01.setSelected(false);
//        radio_button_02.setSelected(false);
//        radio_button_03.setSelected(false);
//        radio_button_04.setSelected(false);
//        radio_button_05.setSelected(false);
//        radio_button_06.setSelected(false);
//        radio_button_07.setSelected(false);
//        radio_button_08.setSelected(false);
            answer_edit.setText(null);
            linear_01.setVisibility(View.GONE);
            checkbox_01.setChecked(false);
            if (current_question == 0){
                question_left.setVisibility(View.GONE);
                question_right.setVisibility(View.VISIBLE);
                question_submit.setVisibility(View.GONE);
            }else if (current_question == list.size()-1){
                question_left.setVisibility(View.VISIBLE);
                question_right.setVisibility(View.GONE);
                question_submit.setVisibility(View.VISIBLE);
            }else {
                question_left.setVisibility(View.VISIBLE);
                question_right.setVisibility(View.VISIBLE);
                question_submit.setVisibility(View.GONE);
            }
            question_id.setText("第" + list.get(current_question).getSort() + "道题：");
            question_des.setText(list.get(current_question).getQuestion());
            if ("0".equals(list.get(current_question).getType())){
                answer_edit.setVisibility(View.GONE);
                int size = list.get(current_question).getList().size();
                switch (size){
                    case 2 :
                        radio_button_01.setVisibility(View.VISIBLE);
                        radio_button_02.setVisibility(View.VISIBLE);
                        radio_button_03.setVisibility(View.GONE);
                        radio_button_04.setVisibility(View.GONE);
                        radio_button_05.setVisibility(View.GONE);
                        radio_button_06.setVisibility(View.GONE);
                        radio_button_07.setVisibility(View.GONE);
                        radio_button_08.setVisibility(View.GONE);
                        radio_button_01.setText(list.get(current_question).getList().get(0).toString());
                        radio_button_02.setText(list.get(current_question).getList().get(1).toString());
                        break;
                    case 3 :
                        radio_button_01.setVisibility(View.VISIBLE);
                        radio_button_02.setVisibility(View.VISIBLE);
                        radio_button_03.setVisibility(View.VISIBLE);
                        radio_button_04.setVisibility(View.GONE);
                        radio_button_05.setVisibility(View.GONE);
                        radio_button_06.setVisibility(View.GONE);
                        radio_button_07.setVisibility(View.GONE);
                        radio_button_08.setVisibility(View.GONE);
                        radio_button_01.setText(list.get(current_question).getList().get(0).toString());
                        radio_button_02.setText(list.get(current_question).getList().get(1).toString());
                        radio_button_03.setText(list.get(current_question).getList().get(2).toString());
                        break;
                    case 4 :
                        radio_button_01.setVisibility(View.VISIBLE);
                        radio_button_02.setVisibility(View.VISIBLE);
                        radio_button_03.setVisibility(View.VISIBLE);
                        radio_button_04.setVisibility(View.VISIBLE);
                        radio_button_05.setVisibility(View.GONE);
                        radio_button_06.setVisibility(View.GONE);
                        radio_button_07.setVisibility(View.GONE);
                        radio_button_08.setVisibility(View.GONE);
                        radio_button_01.setText(list.get(current_question).getList().get(0).toString());
                        radio_button_02.setText(list.get(current_question).getList().get(1).toString());
                        radio_button_03.setText(list.get(current_question).getList().get(2).toString());
                        radio_button_04.setText(list.get(current_question).getList().get(3).toString());
                        break;
                    case 5 :
                        radio_button_01.setVisibility(View.VISIBLE);
                        radio_button_02.setVisibility(View.VISIBLE);
                        radio_button_03.setVisibility(View.VISIBLE);
                        radio_button_04.setVisibility(View.VISIBLE);
                        radio_button_05.setVisibility(View.VISIBLE);
                        radio_button_06.setVisibility(View.GONE);
                        radio_button_07.setVisibility(View.GONE);
                        radio_button_08.setVisibility(View.GONE);
                        radio_button_01.setText(list.get(current_question).getList().get(0).toString());
                        radio_button_02.setText(list.get(current_question).getList().get(1).toString());
                        radio_button_03.setText(list.get(current_question).getList().get(2).toString());
                        radio_button_04.setText(list.get(current_question).getList().get(3).toString());
                        radio_button_05.setText(list.get(current_question).getList().get(4).toString());
                        break;
                    case 6 :
                        radio_button_01.setVisibility(View.VISIBLE);
                        radio_button_02.setVisibility(View.VISIBLE);
                        radio_button_03.setVisibility(View.VISIBLE);
                        radio_button_04.setVisibility(View.VISIBLE);
                        radio_button_05.setVisibility(View.VISIBLE);
                        radio_button_06.setVisibility(View.VISIBLE);
                        radio_button_07.setVisibility(View.GONE);
                        radio_button_08.setVisibility(View.GONE);
                        radio_button_01.setText(list.get(current_question).getList().get(0).toString());
                        radio_button_02.setText(list.get(current_question).getList().get(1).toString());
                        radio_button_03.setText(list.get(current_question).getList().get(2).toString());
                        radio_button_04.setText(list.get(current_question).getList().get(3).toString());
                        radio_button_05.setText(list.get(current_question).getList().get(4).toString());
                        radio_button_06.setText(list.get(current_question).getList().get(5).toString());
                        break;
                    case 7 :
                        radio_button_01.setVisibility(View.VISIBLE);
                        radio_button_02.setVisibility(View.VISIBLE);
                        radio_button_03.setVisibility(View.VISIBLE);
                        radio_button_04.setVisibility(View.VISIBLE);
                        radio_button_05.setVisibility(View.VISIBLE);
                        radio_button_06.setVisibility(View.VISIBLE);
                        radio_button_07.setVisibility(View.VISIBLE);
                        radio_button_08.setVisibility(View.GONE);
                        radio_button_01.setText(list.get(current_question).getList().get(0).toString());
                        radio_button_02.setText(list.get(current_question).getList().get(1).toString());
                        radio_button_03.setText(list.get(current_question).getList().get(2).toString());
                        radio_button_04.setText(list.get(current_question).getList().get(3).toString());
                        radio_button_05.setText(list.get(current_question).getList().get(4).toString());
                        radio_button_06.setText(list.get(current_question).getList().get(5).toString());
                        radio_button_07.setText(list.get(current_question).getList().get(6).toString());
                        break;
                    case 8 :
                        radio_button_01.setVisibility(View.VISIBLE);
                        radio_button_02.setVisibility(View.VISIBLE);
                        radio_button_03.setVisibility(View.VISIBLE);
                        radio_button_04.setVisibility(View.VISIBLE);
                        radio_button_05.setVisibility(View.VISIBLE);
                        radio_button_06.setVisibility(View.VISIBLE);
                        radio_button_07.setVisibility(View.VISIBLE);
                        radio_button_08.setVisibility(View.VISIBLE);
                        radio_button_01.setText(list.get(current_question).getList().get(0).toString());
                        radio_button_02.setText(list.get(current_question).getList().get(1).toString());
                        radio_button_03.setText(list.get(current_question).getList().get(2).toString());
                        radio_button_04.setText(list.get(current_question).getList().get(3).toString());
                        radio_button_05.setText(list.get(current_question).getList().get(4).toString());
                        radio_button_06.setText(list.get(current_question).getList().get(5).toString());
                        radio_button_07.setText(list.get(current_question).getList().get(6).toString());
                        radio_button_08.setText(list.get(current_question).getList().get(7).toString());
                        break;
                    default:
                        break;
                }
            }else if ("1".equals(list.get(current_question).getType())){
                answer_edit.setVisibility(View.VISIBLE);
                linear_01.setVisibility(View.VISIBLE);
                radio_button_01.setVisibility(View.GONE);
                radio_button_02.setVisibility(View.GONE);
                radio_button_03.setVisibility(View.GONE);
                radio_button_04.setVisibility(View.GONE);
                radio_button_05.setVisibility(View.GONE);
                radio_button_06.setVisibility(View.GONE);
                radio_button_07.setVisibility(View.GONE);
                radio_button_08.setVisibility(View.GONE);
                int size = list.get(current_question).getList().size();
                if (size == 1){
                    answer_edit.setHint(list.get(current_question).getInput());
                    checkbox_01.setText(list.get(current_question).getList().get(0).toString());
                }
            }
        }else {
            ToastUtils.longToast(EvaluateActivity_ing.this , "题库维护中求稍后再试");
        }
    }

    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(EvaluateActivity_ing.this , "题目获取中，请稍后。。。");
        // 不可以用“返回键”取消
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 隐藏数据加载框
     */
    private void hiddenBenDialog() {
        //关闭dialog
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                goPre() ;
                break;
            case R.id.question_left :
                if (checkIsClick()){
                    saveData() ;
                    current_id -= 1 ;
                    showData(current_id);
                }else {
                    ToastUtils.shortToast(this , "请选择您的答案");
                }

                break;
            case R.id.question_right :
                if (checkIsClick()){
                    saveData() ;
                    current_id += 1 ;
                    showData(current_id);
                }else {
                    ToastUtils.shortToast(this , "请选择您的答案");
                }
                break;
            case R.id.question_submit :
                goEvaluateActivity_end() ;
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        goPre();
    }

    private void goEvaluateActivity_end() {
        saveData() ;
        Intent intent = new Intent(EvaluateActivity_ing.this , EvaluateActivity_end.class ) ;
        intent.putExtra("Money" , Money ) ;
        intent.putExtra("Area" , Area ) ;
        intent.putExtra("AssetType" , AssetType ) ;
        intent.putExtra("Type" , Type ) ;
//        //获取map里面的所有数据
//        Set set = map_answer.keySet();
//        Iterator it = set.iterator();
//        while (it.hasNext()) {
//            String key = (String) it.next();
//            String value = (String) map_answer.get(key);
//            System.out.println(key + "=" + value);
//        }
        intent.putExtra("Answer" , map_answer.toString() ) ;
        startActivity(intent);
        finish();
    }

    private boolean checkIsClick() {
        if (checkbox_01.isChecked()){
            return true ;
        }
        if (radio_button_01.isChecked()){
            return true ;
        }
        if (radio_button_02.isChecked()){
            return true ;
        }
        if (radio_button_03.isChecked()){
            return true ;
        }
        if (radio_button_04.isChecked()){
            return true ;
        }
        if (radio_button_05.isChecked()){
            return true ;
        }
        if (radio_button_06.isChecked()){
            return true ;
        }
        if (radio_button_07.isChecked()){
            return true ;
        }
        if (radio_button_08.isChecked()){
            return true ;
        }
        if (!TextUtils.isEmpty(answer_edit.getText().toString()) && !"".equals(answer_edit.getText().toString()) ){
            return true ;
        }
        return false ;
    }

    //存储用户的答案
    private void saveData() {
        if (!TextUtils.isEmpty(answer_edit.getText().toString()) && !"".equals(answer_edit.getText().toString())){
            map_answer.put( "" + current_id , answer_edit.getText().toString() ) ;
        }else {
            if (checkbox_01.isChecked()){
                map_answer.put( "" + current_id ,  "" + 2 ) ;
            }
            if (radio_button_01.isChecked()){
                map_answer.put( "" + current_id ,  "" + 1 ) ;
            }
            if (radio_button_02.isChecked()){
                map_answer.put( "" + current_id ,  "" + 2 ) ;
            }
            if (radio_button_03.isChecked()){
                map_answer.put( "" + current_id ,  "" + 3 ) ;
            }
            if (radio_button_04.isChecked()){
                map_answer.put( "" + current_id ,  "" + 4 ) ;
            }
            if (radio_button_05.isChecked()){
                map_answer.put( "" + current_id ,  "" + 5 ) ;
            }
            if (radio_button_06.isChecked()){
                map_answer.put( "" + current_id ,  "" + 6 ) ;
            }
            if (radio_button_07.isChecked()){
                map_answer.put( "" + current_id ,  "" + 7 ) ;
            }
            if (radio_button_08.isChecked()){
                map_answer.put( "" + current_id ,  "" + 8 ) ;
            }
        }

        Log.e("答案 答案 ", map_answer.toString()) ;
    }

    private void goPre() {
        final CustomDialog.Builder builder01 = new CustomDialog.Builder(EvaluateActivity_ing.this);
        builder01.setTitle("温馨提示");
        builder01.setMessage("您的测评还未完成，确定要退出吗？");
        builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder01.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder01.create().show();
    }
}
