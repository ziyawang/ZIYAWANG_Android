package com.ziyawang.ziya.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class EvaluateActivity_ing02 extends BenBenActivity implements View.OnClickListener {

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
    private List<QuestionsEntity> list_big ;

    private LinearLayout linear_01 ;
    private CheckBox checkbox_01 ;

    private MyProgressDialog dialog ;
    private int current_id = 0 ;
    private Map map_answer  = new HashMap() ;
    private org.json.JSONObject object_answer = new org.json.JSONObject() ;


    private QuestionSingleAdapter adapter ;
    private QuestionDoubleAdapter adapter02 ;
    private BenListView listView ;

    private Map<Integer, Boolean> isSelected;
    private List beSelectedData = new ArrayList();

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
        setContentView(R.layout.activity_evaluate_activity_ing02);
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
        linear_01 = (LinearLayout)findViewById(R.id.linear_01) ;
        checkbox_01 = (CheckBox)findViewById(R.id.checkbox_01 ) ;
        listView = (BenListView)findViewById(R.id.listView ) ;
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
                    list_big = Json_Questions.getParse(responseInfo.result);
                    showData(current_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                hiddenBenDialog();
                error.printStackTrace();
                ToastUtils.shortToast(EvaluateActivity_ing02.this, "网络连接异常");
            }
        }) ;
    }

    private void showData(int current_question) {
        // 清除已经选择的项
        if (beSelectedData.size() > 0) {
            beSelectedData.clear();
        }
        if (list.size()!=0){
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
                linear_01.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                int size = list.get(current_question).getList02().size();
                if (isSelected != null)
                    isSelected = null;
                isSelected = new HashMap<Integer, Boolean>();
                for (int i = 0; i < size ; i++) {
                    isSelected.put(i, false);
                }
                Log.e("benben" , "" + size ) ;
                //加载数据选项
                adapter = new QuestionSingleAdapter(this , list.get(current_question).getList02()) ;
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                adapter.notifyDataSetChanged();
            }else if ("1".equals(list.get(current_question).getType())){
                answer_edit.setVisibility(View.VISIBLE);
                linear_01.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                int size = list.get(current_question).getList02().size();
                if (size == 2){
                    answer_edit.setHint(list.get(current_question).getList02().get(0).toString());
                    checkbox_01.setText(list.get(current_question).getList02().get(1).toString());
                }
            }else if ("2".equals(list.get(current_question).getType())){
                answer_edit.setVisibility(View.GONE);
                linear_01.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                int size = list.get(current_question).getList02().size();
                if (isSelected != null)
                    isSelected = null;
                isSelected = new HashMap<Integer, Boolean>();
                for (int i = 0; i < size ; i++) {
                    isSelected.put(i, false);
                }
                Log.e("benben" , "" + size ) ;
                //加载数据选项
                adapter02 = new QuestionDoubleAdapter(this , list.get(current_question).getList02()) ;
                listView.setAdapter(adapter02);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                adapter02.notifyDataSetChanged();
            }
        }else {
            ToastUtils.longToast(EvaluateActivity_ing02.this , "题库维护中求稍后再试");
        }
    }

    //单选适配器
    class QuestionSingleAdapter extends BaseAdapter {

        private Context context;
        private List list;
        private LayoutInflater inflater;

        public QuestionSingleAdapter(Context context, List data ) {
            this.context = context;
            this.list = data;
            initLayoutInflater();
        }

        void initLayoutInflater() {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position1, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View view = null;
            final int position = position1;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.qusetion_single_items, null);
                holder = new ViewHolder();
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 当前点击的CB
                boolean cu = !isSelected.get(position);
                // 先将所有的置为FALSE
                for (Integer p : isSelected.keySet()) {
                    isSelected.put(p, false);
                }
                // 再将当前选择CB的实际状态
                isSelected.put(position, cu);
                QuestionSingleAdapter.this.notifyDataSetChanged();
                beSelectedData.clear();
                if (cu) beSelectedData.add(list_big.get(current_id).getList().get(position).toString());
                Log.e("benben", beSelectedData.toString());
            }
        });
            holder.checkBox.setChecked(isSelected.get(position));
            holder.checkBox.setText(list.get(position1).toString());
            return convertView;
        }

        class ViewHolder {
            CheckBox checkBox;
        }

    }

    //多选适配器
    class QuestionDoubleAdapter extends BaseAdapter {

        private Context context;
        private List list;
        private LayoutInflater inflater;

        public QuestionDoubleAdapter(Context context, List data ) {
            this.context = context;
            this.list = data;
            initLayoutInflater();
        }

        void initLayoutInflater() {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position1, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View view = null;
            final int position = position1;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.qusetion_single_items, null);
                holder = new ViewHolder();
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            final ViewHolder finalHolder = holder;
            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // 当前点击的CB
                    boolean cu = !isSelected.get(position);
                    finalHolder.checkBox.setChecked(cu);
                    // 再将当前选择CB的实际状态
                    isSelected.put(position, cu);
                    if (cu) {
                        beSelectedData.add(list_big.get(current_id).getList().get(position).toString());
                    } else {
                        if (beSelectedData.contains(list_big.get(current_id).getList().get(position).toString())) {
                            beSelectedData.remove(list_big.get(current_id).getList().get(position).toString());
                        }
                    }

                    Log.e("benben", removeDuplicate(beSelectedData).toString());
                }
            });
            holder.checkBox.setChecked(isSelected.get(position));
            holder.checkBox.setText(list.get(position1).toString());
            return convertView;
        }

        class ViewHolder {
            CheckBox checkBox;
        }

    }

    public List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list ;
    }

    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(EvaluateActivity_ing02.this , "题目获取中，请稍后。。。");
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
//                if (checkIsClick()){
//                    try {
//                        saveData() ;
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    current_id -= 1 ;
//                    showData(current_id);
//                }
                current_id -= 1 ;
                showData(current_id);
                break;
            case R.id.question_right :
                if (checkIsClick()){
                    try {
                        saveData() ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    current_id += 1 ;
                    showData(current_id);
                }
                break;
            case R.id.question_submit :
                if (checkIsClick()){
                    try {
                        saveData() ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    goEvaluateActivity_end() ;
                }
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
        Intent intent = new Intent(EvaluateActivity_ing02.this , EvaluateActivity_end.class ) ;
        intent.putExtra("Money" , Money ) ;
        intent.putExtra("Area" , Area ) ;
        intent.putExtra("AssetType" , AssetType ) ;
        intent.putExtra("Type" , Type ) ;
        //intent.putExtra("Answer" , map_answer.toString() ) ;
        intent.putExtra("Answer" , object_answer.toString() ) ;
        startActivity(intent);
        finish();
    }

    private boolean checkIsClick() {

        String type = list.get(current_id).getType();
        if ("0".equals(type)){
            if (beSelectedData.size() == 0){
                ToastUtils.shortToast(EvaluateActivity_ing02.this, "请至少选择一个答案");
                return false ;
            }else {
                return true ;
            }

        }else if ("1".equals(type)){
            if (checkbox_01.isChecked()){
                return true ;
            }
            if (!TextUtils.isEmpty(answer_edit.getText().toString()) && !"".equals(answer_edit.getText().toString()) ){
                return true ;
            }else {
                ToastUtils.shortToast(EvaluateActivity_ing02.this , "请至少选择一个答案");
                return false ;
            }
        }else if ("2".equals(type)){
            if (beSelectedData.size() == 0){
                ToastUtils.shortToast(EvaluateActivity_ing02.this , "请至少选择一个答案");
                return false ;
            }else {
                return true ;
            }
        }
        ToastUtils.shortToast(EvaluateActivity_ing02.this, "题库维护中");
        return false ;
    }

    //存储用户的答案
    private void saveData() throws JSONException {
        String type = list.get(current_id).getType();
        if ("1".equals(type)){
            JSONArray array = new JSONArray() ;
            if (!TextUtils.isEmpty(answer_edit.getText().toString()) && !"".equals(answer_edit.getText().toString())){
                object_answer.put( "" + (current_id + 1) , array.put(answer_edit.getText().toString())) ;
            }else {
                if (checkbox_01.isChecked()){
                    object_answer.put( "" + ( current_id + 1 ) , array.put(list.get(current_id).getList().get(0).toString()))  ;
                }
            }
        }else if ("0".equals(type)){
            JSONArray array = new JSONArray() ;
            for (int i = 0; i < beSelectedData.size(); i++) {
                array.put(beSelectedData.get(i).toString()) ;
            }
            object_answer.put( "" + (current_id + 1) , array ) ;
        }else if ("2".equals(type)){
            JSONArray array = new JSONArray() ;
            for (int i = 0; i < beSelectedData.size(); i++) {
                array.put(beSelectedData.get(i).toString()) ;
            }
            object_answer.put( "" + (current_id + 1) , array ) ;
        }


        Log.e("答案 答案 ", object_answer.toString()) ;
    }

    private void goPre() {
        final CustomDialog.Builder builder01 = new CustomDialog.Builder(EvaluateActivity_ing02.this);
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
