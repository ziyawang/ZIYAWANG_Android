package com.ziyawang.ziya.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.FindInfoAdapter;
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.Json_FindInfo;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindInfoActivity extends BaseActivity {

    private List<FindInfoEntity> list ;
    private FindInfoAdapter adapter ;
    private BenListView listView ;
    private RelativeLayout pre ;
    private List<FindInfoEntity> data  = new ArrayList<FindInfoEntity>();
    private int page  ;
    private int count = 1 ;
    private Boolean isOK = true ;
    private MyProgressDialog dialog ;
    private MyScrollView scrollView ;
    private TextView find_type ;
    private String typeName ;
    private String part_a ;
    private TextView part ;
    private TextView details_type ;
    private String  params_three ;
    private String params_four ;
    private String vip_type = ""  ;
    private TextView find_vip_type ;
    private TextView niuniuniuniu ;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 201) {
                Log.e("HomeINfo", count + "====================================" +page ) ;
                if (isOK){
                    if (count <= page ){
                        isOK = false ;
                        dialog = new MyProgressDialog( FindInfoActivity.this , "加载数据中请稍后。。。") ;
                        dialog.show();
                        findinfo(typeName , part_a , params_three ,params_four, vip_type);
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    isOK = true ;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }else {
                        ToastUtils.shortToast( FindInfoActivity.this, "信息没有更多数据");
                    }
                }
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release);
        //实例化组件
        initView() ;
        Intent intent = getIntent() ;
        if (intent != null ){
            String typeName_a = intent.getStringExtra("typeName");
            if (!TextUtils.isEmpty(typeName_a)){
                switch (typeName_a){
                    case "ben" :
                        typeName = "05" ;
                        find_type.setText("担保信息");
                        params_three = "担保" ;
                        params_four = "AssetType" ;
                        break;
                    case "01" :
                        typeName = typeName_a ;
                        find_type.setText("资产包转让");
                        break;
                    case "02" :
                        typeName = typeName_a ;
                        find_type.setText("委外催收");
                        break;
                    case "03" :
                        typeName = typeName_a ;
                        find_type.setText("法律服务");
                        break;
                    case "04" :
                        typeName = typeName_a ;
                        find_type.setText("商业保理");
                        break;
                    case "05" :
                        typeName = typeName_a ;
                        find_type.setText("典当信息");
                        params_three = "典当" ;
                        params_four = "AssetType" ;
                        break;
                    case "06" :
                        typeName = typeName_a ;
                        find_type.setText("融资需求");
                        break;
                    case "10" :
                        typeName = typeName_a ;
                        find_type.setText("尽职调查");
                        break;
                    case "12" :
                        typeName = typeName_a ;
                        find_type.setText("固产转让");
                        break;
                    case "13" :
                        typeName = typeName_a ;
                        find_type.setText("资产求购");
                        break;
                    case "14" :
                        typeName = typeName_a ;
                        find_type.setText("债权转让");
                        break;
                    case "09" :
                        typeName = typeName_a ;
                        find_type.setText("悬赏信息");
                        break;
                    case "15" :
                        typeName = typeName_a ;
                        find_type.setText("投资需求");
                        break;
                    default:
                        break;
                }
            }
        }
        //加载数据
        findinfo(this.typeName, part_a, params_three, params_four, vip_type) ;
        //对信息类型的选定
        find_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeGet();
            }
        });
        //获得地区的分类
        part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partGet();
            }
        });
        //获得类型的分类
        details_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partTwoGet();
            }
        });
        //VIp筛选的分类
        find_vip_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vipTypeGet();
            }
        });
        //添加回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //添加分页加载
        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                View childView = scrollView.getChildAt(0);
                if (childView.getMeasuredHeight() <= scrollY + scrollView.getHeight()) {
                    mHandler.sendEmptyMessage(201);
                }
            }
        });
    }

    private void vipTypeGet() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_vip_type, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final RadioButton a = (RadioButton)view.findViewById(R.id.one_one);
        final RadioButton b = (RadioButton)view.findViewById(R.id.one_two);
        final RadioButton c = (RadioButton)view.findViewById(R.id.one_three);
        final RadioButton d = (RadioButton)view.findViewById(R.id.one_four);
        if ("0".equals(vip_type)){
            b.setChecked(true);
        }else if ("1".equals(vip_type)){
            c.setChecked(true);
        }else if ("2".equals(vip_type)){
            d.setChecked(true);
        }else if ("".equals(vip_type)){
            a.setChecked(true);
        }

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                vip_type = "" ;
                //find_vip_type.setText("全部");
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                vip_type = "0";
                //find_vip_type.setText("普通");
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                vip_type = "1";
                //find_vip_type.setText("VIP");
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                vip_type = "2";
                //find_vip_type.setText("收费");
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void partTwoGet() {
        String niu_ben = find_type.getText().toString() ;
        switch (niu_ben){
            case "资产包转让" :
                popUpWindow01() ;
                break;
            case "债权转让" :
                popUpWindow02() ;
                break;
            case "固产转让" :
                popUpWindow03() ;
                break;
            case "商业保理" :
                popUpWindow04() ;
                break;
            case "资产求购" :
                popUpWindow05() ;
                break;
            case "融资需求" :
                popUpWindow06() ;
                break;
            case "法律服务" :
                popUpWindow07() ;
                break;
            case "悬赏信息" :
                popUpWindow08() ;
                break;
            case "尽职调查" :
                popUpWindow09() ;
                break;
            case "委外催收" :
                popUpWindow10() ;
                break;
            case "典当信息" :
                popUpWindow11() ;
                break;
            case "担保信息" :
                popUpWindow12() ;
                break;
            case "投资需求" :
                popUpWindow13() ;
                break;
            default:
                ToastUtils.shortToast(FindInfoActivity.this , "请先选择您的发布类型");
                break;
        }
    }

    private void popUpWindow13() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_requirement, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        final Button d = (Button)view.findViewById(R.id.one_four);

        final Button i = (Button)view.findViewById(R.id.one_nine);
        final Button j = (Button)view.findViewById(R.id.one_ten);
        final Button k = (Button)view.findViewById(R.id.one_eleven);

        final Button v104_01 = (Button)view.findViewById(R.id.v104_01);
        final Button v104_02 = (Button)view.findViewById(R.id.v104_02);
        final Button v104_03 = (Button)view.findViewById(R.id.v104_03);
        final Button v104_04 = (Button)view.findViewById(R.id.v104_04);
        final Button v104_05 = (Button)view.findViewById(R.id.v104_05);
        final Button v104_06 = (Button)view.findViewById(R.id.v104_06);
        final Button v104_07 = (Button)view.findViewById(R.id.v104_07);
        final Button v104_08 = (Button)view.findViewById(R.id.v104_08);
        final Button v104_09 = (Button)view.findViewById(R.id.v104_09);
        final Button v104_10 = (Button)view.findViewById(R.id.v104_10);
        v104_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                count = 1;
                details_type.setText(v104_01.getText().toString());
                params_three = v104_01.getText().toString().replace("年" ,"");
                params_four = "Year";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
                window.dismiss();
            }
        });
        v104_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(v104_02.getText().toString());
                params_three = v104_02.getText().toString().replace("年" ,"");
                params_four = "Year";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        v104_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(v104_03.getText().toString());
                params_three = v104_03.getText().toString().replace("年" ,"");
                params_four = "Year";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        v104_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(v104_04.getText().toString());
                params_three = v104_04.getText().toString().replace("年" ,"");
                params_four = "Year";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        v104_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(v104_05.getText().toString());
                params_three = v104_05.getText().toString().replace("年" ,"");
                params_four = "Year";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        v104_06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(v104_06.getText().toString());
                params_three = v104_06.getText().toString().replace("年" ,"");
                params_four = "Year";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        v104_07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(v104_07.getText().toString());
                params_three = v104_07.getText().toString().replace("年" ,"");
                params_four = "Year";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        v104_08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(v104_08.getText().toString());
                params_three = v104_08.getText().toString().replace("年" ,"");
                params_four = "Year";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        v104_09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(v104_09.getText().toString());
                params_three = v104_09.getText().toString().replace("年" ,"");
                params_four = "Year";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        v104_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(v104_10.getText().toString());
                params_three = v104_10.getText().toString().replace("年" ,"");
                params_four = "Year";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(c.getText().toString());
                params_three = c.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(d.getText().toString());
                params_three = d.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(i.getText().toString());
                params_three = i.getText().toString();
                params_four = "InvestType";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(j.getText().toString());
                params_three = j.getText().toString();
                params_four = "InvestType";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(k.getText().toString());
                params_three = k.getText().toString();
                params_four = "InvestType";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void popUpWindow12() {
        data.clear();
        count = 1 ;
        details_type.setText("担保");
        params_three = "担保" ;
        params_four = "AssetType" ;
        findinfo(typeName, part_a, params_three, params_four , vip_type);
    }

    private void popUpWindow11() {
        data.clear();
        count = 1 ;
        details_type.setText("典当");
        params_three = "典当" ;
        params_four = "AssetType" ;
        findinfo(typeName, part_a, params_three, params_four , vip_type);

    }

    private void popUpWindow10() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_ten, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        final Button d = (Button)view.findViewById(R.id.one_four);
        final Button e = (Button)view.findViewById(R.id.one_five);
        final Button f = (Button)view.findViewById(R.id.one_six);
        final Button g = (Button)view.findViewById(R.id.one_seven);
        final Button h = (Button)view.findViewById(R.id.one_eight);
        final Button k = (Button)view.findViewById(R.id.one_104);
        final Button i = (Button)view.findViewById(R.id.one_nine);
        final Button j = (Button)view.findViewById(R.id.one_ten);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(c.getText().toString());
                params_three = c.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(d.getText().toString());
                params_three = d.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(e.getText().toString());
                params_three = e.getText().toString() ;
                params_four = "Rate" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(f.getText().toString());
                params_three = f.getText().toString() ;
                params_four = "Rate" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(g.getText().toString());
                params_three = g.getText().toString() ;
                params_four = "Rate" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(h.getText().toString());
                params_three = h.getText().toString();
                params_four = "Rate";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(k.getText().toString());
                params_three = k.getText().toString();
                params_four = "Rate";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(i.getText().toString());
                params_three = i.getText().toString();
                params_four = "Status";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(j.getText().toString());
                params_three = j.getText().toString();
                params_four = "Status";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void popUpWindow09() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_nine, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        final Button d = (Button)view.findViewById(R.id.one_four);
        final Button e = (Button)view.findViewById(R.id.one_five);
        final Button f = (Button)view.findViewById(R.id.one_six);
        final Button g = (Button)view.findViewById(R.id.one_seven);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(c.getText().toString());
                params_three = c.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(d.getText().toString());
                params_three = d.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(e.getText().toString());
                params_three = e.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(f.getText().toString());
                params_three = f.getText().toString() ;
                params_four = "Informant" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(g.getText().toString());
                params_three = g.getText().toString() ;
                params_four = "Informant" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void popUpWindow08() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_eight, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });

        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });


    }

    private void popUpWindow07() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_seven, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        final Button d = (Button)view.findViewById(R.id.one_four);
        final Button e = (Button)view.findViewById(R.id.one_five);
        final Button f = (Button)view.findViewById(R.id.one_six);
        final Button g = (Button)view.findViewById(R.id.one_seven);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(c.getText().toString());
                params_three = c.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(d.getText().toString());
                params_three = d.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(e.getText().toString());
                params_three = e.getText().toString() ;
                params_four = "Requirement" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(f.getText().toString());
                params_three = f.getText().toString() ;
                params_four = "Requirement" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(g.getText().toString());
                params_three = g.getText().toString() ;
                params_four = "Requirement" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void popUpWindow06() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_six, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        final Button d = (Button)view.findViewById(R.id.one_four);
        final Button e = (Button)view.findViewById(R.id.one_five);
        final Button f = (Button)view.findViewById(R.id.one_six);
        final Button g = (Button)view.findViewById(R.id.one_seven);
        final Button h = (Button)view.findViewById(R.id.one_eight);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(c.getText().toString());
                params_three = c.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(d.getText().toString());
                params_three = d.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(e.getText().toString());
                params_three = e.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(f.getText().toString());
                params_three = f.getText().toString();
                params_four = "AssetType";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(g.getText().toString());
                params_three = g.getText().toString();
                params_four = "AssetType";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(h.getText().toString());
                params_three = h.getText().toString();
                params_four = "AssetType";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });

        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void popUpWindow05() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_five, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        final Button d = (Button)view.findViewById(R.id.one_four);
        final Button e = (Button)view.findViewById(R.id.one_five);
        final Button f = (Button)view.findViewById(R.id.one_six);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(c.getText().toString());
                params_three = c.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(d.getText().toString());
                params_three = d.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(e.getText().toString());
                params_three = e.getText().toString() ;
                params_four = "Buyer" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(f.getText().toString());
                params_three = f.getText().toString() ;
                params_four = "Buyer" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });

        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void popUpWindow04() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_four, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        final Button d = (Button)view.findViewById(R.id.one_four);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "BuyerNature" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "BuyerNature" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(c.getText().toString());
                params_three = c.getText().toString() ;
                params_four = "BuyerNature" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(d.getText().toString());
                params_three = d.getText().toString() ;
                params_four = "BuyerNature" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void popUpWindow03() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_three, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        final Button d = (Button)view.findViewById(R.id.one_four);
        final Button e = (Button)view.findViewById(R.id.one_five);
        final Button f = (Button)view.findViewById(R.id.one_six);
        final Button g = (Button)view.findViewById(R.id.one_seven);
        final Button h = (Button)view.findViewById(R.id.one_eight);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(c.getText().toString());
                params_three = c.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(d.getText().toString());
                params_three = d.getText().toString() ;
                params_four = "Corpore" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(e.getText().toString());
                params_three = e.getText().toString() ;
                params_four = "Corpore" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(f.getText().toString());
                params_three = f.getText().toString();
                params_four = "Corpore";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(g.getText().toString());
                params_three = g.getText().toString();
                params_four = "Corpore";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(h.getText().toString());
                params_three = h.getText().toString();
                params_four = "Corpore";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void popUpWindow02() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_two, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(c.getText().toString());
                params_three = c.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void popUpWindow01() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_one, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        final Button d = (Button)view.findViewById(R.id.one_four);
        final Button e = (Button)view.findViewById(R.id.one_five);
        final Button f = (Button)view.findViewById(R.id.one_six);
        final Button g = (Button)view.findViewById(R.id.one_seven);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(a.getText().toString());
                params_three = a.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(b.getText().toString());
                params_three = b.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(c.getText().toString());
                params_three = c.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(d.getText().toString());
                params_three = d.getText().toString() ;
                params_four = "AssetType" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(e.getText().toString());
                params_three = e.getText().toString() ;
                params_four = "FromWhere" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type );
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(f.getText().toString());
                params_three = f.getText().toString() ;
                params_four = "FromWhere" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1 ;
                details_type.setText(g.getText().toString());
                params_three = g.getText().toString() ;
                params_four = "FromWhere" ;
                findinfo(typeName, part_a, params_three, params_four , vip_type);
            }
        });

        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void findinfo(String typeName , String  part , String params_three , String params_four , String vip ) {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(FindInfoActivity.this , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();
        String urls ;
        if (GetBenSharedPreferences.getIsLogin(FindInfoActivity.this)){
            urls = String.format(Url.GetInfo, GetBenSharedPreferences.getTicket(FindInfoActivity.this) ) ;
        }else {
            urls = String.format(Url.GetInfo, "" ) ;
        }
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("Vip", vip);
        if (TextUtils.isEmpty(params_four)){
            params.addQueryStringParameter("TypeID", typeName );
            params.addQueryStringParameter("ProArea", part );
            params.addQueryStringParameter("startpage" , "" + count );
        }else {
            params.addQueryStringParameter("TypeID", typeName );
            params.addQueryStringParameter("ProArea", part );
            params.addQueryStringParameter("startpage" , "" + count );
            params.addQueryStringParameter(params_four , params_three );
        }
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, urls , params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("benben_home_info", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    String pages = jsonObject.getString("pages");
                    page = Integer.parseInt(pages);
                    count++;
                    String counts = jsonObject.getString("counts");
                    if (!TextUtils.isEmpty(counts) && counts.equals("0")) {
                        scrollView.setVisibility(View.GONE);
                        niuniuniuniu.setVisibility(View.VISIBLE);
                    } else {
                        scrollView.setVisibility(View.VISIBLE);
                        niuniuniuniu.setVisibility(View.GONE);
                        try {
                            List<FindInfoEntity> list = Json_FindInfo.getParse(responseInfo.result);
                            data.addAll(list);
                            Log.e("benben", "数据的个数" + data.size());
                            adapter = new FindInfoAdapter(FindInfoActivity.this, data);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                error.printStackTrace();
            }
        });
    }


    private void initView() {
        listView = (BenListView)findViewById(R.id.listView ) ;

        listView.setDivider(new ColorDrawable(Color.argb(0, 244, 244, 244)));
        listView.setDividerHeight(1);

        scrollView = (MyScrollView)findViewById(R.id.scrollView ) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;

        find_type = (TextView)findViewById(R.id.find_type ) ;
        part = (TextView)findViewById(R.id.part ) ;
        details_type = (TextView)findViewById(R.id.details_type ) ;
        find_vip_type = (TextView)findViewById(R.id.find_vip_type ) ;
        niuniuniuniu = (TextView)findViewById(R.id.niuniuniuniu ) ;

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab) ;
        assert floatingActionButton != null;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSearchActivity();
            }
        });
    }

    private void goSearchActivity() {
        Intent intent = new Intent( this, SearchActivity.class);
        intent.putExtra("type", "信息");
        intent.putExtra("title", "搜索信息");
        startActivity(intent);
    }

    private void typeGet() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_type, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button)view.findViewById(R.id.a);
        final Button b = (Button)view.findViewById(R.id.b);
        final Button c = (Button)view.findViewById(R.id.c);
        final Button d = (Button)view.findViewById(R.id.d);
        final Button e = (Button)view.findViewById(R.id.e);
        final Button f = (Button)view.findViewById(R.id.f);
        final Button g = (Button)view.findViewById(R.id.g);
        final Button h = (Button)view.findViewById(R.id.h);
        final Button i = (Button)view.findViewById(R.id.i);
        final Button j = (Button)view.findViewById(R.id.j);
        final Button k = (Button)view.findViewById(R.id.k);
        final Button l = (Button)view.findViewById(R.id.l);
        final Button m = (Button)view.findViewById(R.id.m);

        switch (find_type.getText().toString()){
            case "资产包转让" :
                a.setSelected(true);
                break;
            case "债权转让" :
                b.setSelected(true);
                break;
            case "固产转让" :
                c.setSelected(true);
                break;
            case "商业保理" :
                d.setSelected(true);
                break;
            case "融资需求" :
                g.setSelected(true);
                break;
            case "悬赏信息" :
                h.setSelected(true);
                break;
            case "尽职调查" :
                i.setSelected(true);
                break;
            case "委外催收" :
                j.setSelected(true);
                break;
            case "法律服务" :
                k.setSelected(true);
                break;
            case "资产求购" :
                l.setSelected(true);
                break;
            case "投资需求" :
                m.setSelected(true);
                break;
            default:
                break;
        }

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("资产包转让");
                window.dismiss();
                data.clear();
                typeName = "01";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("债权转让");
                window.dismiss();
                data.clear();
                typeName = "14";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("固产转让");
                window.dismiss();
                data.clear();
                typeName = "12";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("商业保理");
                window.dismiss();
                data.clear();
                typeName = "04";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("典当信息");
                window.dismiss();
                data.clear();
                typeName = "05";
                count = 1;
                part.setText("地区");
                details_type.setText("典当");
                part_a = "";
                params_four = "AssetType";
                params_three = "典当";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("担保信息");
                window.dismiss();
                data.clear();
                typeName = "05";
                count = 1;
                part.setText("地区");
                details_type.setText("担保");
                part_a = "";
                params_four = "AssetType";
                params_three = "担保";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("融资需求");
                window.dismiss();
                data.clear();
                typeName = "06";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("悬赏信息");
                window.dismiss();
                data.clear();
                typeName = "09";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("尽职调查");
                window.dismiss();
                data.clear();
                typeName = "10";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("委外催收");
                window.dismiss();
                data.clear();
                typeName = "02";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("法律服务");
                window.dismiss();
                data.clear();
                typeName = "03";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("资产求购");
                window.dismiss();
                data.clear();
                typeName = "13";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params_four = "";
                find_type.setText("投资需求");
                window.dismiss();
                data.clear();
                typeName = "15";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                part_a = "";
                params_four = "";
                params_three = "";
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });

        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示

        window.showAsDropDown(find_type);
        backgroundAlpha(0.6f);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void partGet() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_part_service, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button a = (Button)view.findViewById(R.id.a);
        Button b = (Button)view.findViewById(R.id.b);
        Button c = (Button)view.findViewById(R.id.c);
        Button d = (Button)view.findViewById(R.id.d);
        Button e = (Button)view.findViewById(R.id.e);
        Button f = (Button)view.findViewById(R.id.f);
        Button g = (Button)view.findViewById(R.id.g);
        Button h = (Button)view.findViewById(R.id.h);
        Button i = (Button)view.findViewById(R.id.i);
        Button j = (Button)view.findViewById(R.id.j);
        Button k = (Button)view.findViewById(R.id.k);
        Button l = (Button)view.findViewById(R.id.l);
        Button m = (Button)view.findViewById(R.id.m);
        Button n = (Button)view.findViewById(R.id.n);
        Button o = (Button)view.findViewById(R.id.o);
        Button p = (Button)view.findViewById(R.id.p);
        Button q = (Button)view.findViewById(R.id.q);
        Button r = (Button)view.findViewById(R.id.r);
        Button s = (Button)view.findViewById(R.id.s);
        Button t = (Button)view.findViewById(R.id.t);
        Button u = (Button)view.findViewById(R.id.u);
        Button v = (Button)view.findViewById(R.id.v);
        Button w = (Button)view.findViewById(R.id.w);
        Button x = (Button)view.findViewById(R.id.x);
        Button y = (Button)view.findViewById(R.id.y);
        Button z = (Button)view.findViewById(R.id.z);
        Button aa = (Button)view.findViewById(R.id.aa);
        Button bb = (Button)view.findViewById(R.id.bb);
        Button cc = (Button)view.findViewById(R.id.cc);
        Button dd = (Button)view.findViewById(R.id.dd);
        Button ee = (Button)view.findViewById(R.id.ee);
        Button ff = (Button)view.findViewById(R.id.ff);

//        AutoCompleteTextView act=(AutoCompleteTextView) findViewById(R.id.AutoCompleteTextView01);
//        List<String> countries = new ArrayList<String>();
//        countries.add("Afghanistan");
//        countries.add("Albania");
//        countries.add("Algeria");
//        countries.add("American");
//        countries.add("Andorra");
//        countries.add("Anguilla");
//        countries.add("Angola");
//        countries.add("Antarctica");
//        countries.add("China");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
//
//        assert act != null;
//        act.setAdapter(adapter);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                part.setText("全国");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "" ;
                findinfo(typeName, part_a , params_three , params_four, vip_type );

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("北京");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "北京" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("上海");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "上海" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("广东");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "广东" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("江苏");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "江苏" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("浙江");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "浙江" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("河南");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "河南" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("河北");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "河北" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("辽宁");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "辽宁" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("四川");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "四川" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("湖北");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "湖北" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("湖南");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "湖南" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("福建");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "福建" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("安徽");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "安徽" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("陕西");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "陕西" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("天津");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "天津" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("江西");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "江西" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("重庆");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "重庆" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("吉林");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "吉林" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("云南");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "云南" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("山西");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "山西" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("新疆");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "新疆" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("贵州");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "贵州" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("甘肃");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "甘肃" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("海南");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "海南" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("宁夏");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "宁夏" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        aa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("青海");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "青海" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("西藏");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "西藏" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("黑龙江");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "黑龙江" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("内蒙古");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "内蒙古" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        ee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("山东");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "山东" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });
        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("广西");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "广西" ;
                findinfo(typeName, part_a, params_three, params_four, vip_type);
            }
        });

        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        //window.showAtLocation(find_type , Gravity.TOP, 0, 0);
        window.showAsDropDown(find_type);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("找信息页面");
        //统计时长
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("找信息页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}