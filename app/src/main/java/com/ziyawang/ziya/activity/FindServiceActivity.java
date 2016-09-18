package com.ziyawang.ziya.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.FindServiceAdapter;
import com.ziyawang.ziya.entity.FindServiceEntity;
import com.ziyawang.ziya.tools.Json_FindService;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FindServiceActivity extends BaseActivity {

    private FindServiceAdapter adapter ;
    private BenListView listView ;
    private RelativeLayout pre ;
    private List<FindServiceEntity> data  = new ArrayList<FindServiceEntity>();
    private int page  ;
    private int count = 1 ;
    private Boolean isOK = true ;
    private MyProgressDialog dialog ;
    private MyScrollView scrollView ;
    private TextView part , find_type ;
    private String typeName ;
    private String part_a ;
    private TextView details_type ;
    private TextView niuniuniuniu ;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 202) {
                Log.e("HomeINfo", count + "====================================" +page ) ;
                if (isOK){
                    if (count <= page ){
                        isOK = false ;
                        dialog = new MyProgressDialog( FindServiceActivity.this , "加载数据中请稍后。。。") ;
                        dialog.show();
                        findService(typeName , part_a ) ;
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
                        ToastUtils.shortToast(FindServiceActivity.this, "服务没有更多数据");
                    }
                }

            }
            super.handleMessage(msg);
        }
    };

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("找服务页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("找服务页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_service);
        //实例化组件
        initView() ;
        //get Intent
        Intent intent = getIntent() ;
        if (intent != null ){
            String typeName_a = intent.getStringExtra("typeName");
            if (!TextUtils.isEmpty(typeName_a)){
                switch (typeName_a){
                    case "ben" :
                        typeName = "05" ;
                        find_type.setText("担保公司");
                        break;
                    case "01" :
                        typeName = typeName_a ;
                        find_type.setText("资产包收购");
                        break;
                    case "02" :
                        typeName = typeName_a ;
                        find_type.setText("催收机构");
                        break;
                    case "03" :
                        typeName = typeName_a ;
                        find_type.setText("律师事务所");
                        break;
                    case "04" :
                        typeName = typeName_a ;
                        find_type.setText("保理公司");
                        break;
                    case "05" :
                        typeName = typeName_a ;
                        find_type.setText("典当公司");
                        break;
                    case "06" :
                        typeName = typeName_a ;
                        find_type.setText("投融资服务");
                        break;
                    case "10" :
                        typeName = typeName_a ;
                        find_type.setText("尽职调查");
                        break;
                    case "12" :
                        typeName = typeName_a ;
                        find_type.setText("资产收购");
                        break;
                    case "14" :
                        typeName = typeName_a ;
                        find_type.setText("债权收购");
                        break;
                    default:
                        break;
                }
            }
        }
        //加载数据
        findService(typeName, part_a) ;
        //获得地区的分类
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

        //添加回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //VIP等级的分类
        details_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsTypeGet();
            }
        });

        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                View childView = scrollView.getChildAt(0);
                if (childView.getMeasuredHeight() <= scrollY + scrollView.getHeight()) {

                    mHandler.sendEmptyMessage(202);
                }
            }
        });
    }

    private void detailsTypeGet() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_details_type_service, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button a = (Button)view.findViewById(R.id.a);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
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

    private void findService(String typeName , String  part) {

        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(FindServiceActivity.this , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();

        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        params.addQueryStringParameter("startpage", "" + count);
        params.addQueryStringParameter("ServiceType", typeName);
        params.addQueryStringParameter("ServiceArea", part);

        httpUtils.send(HttpRequest.HttpMethod.GET, Url.GetService, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (dialog != null) {
                    dialog.dismiss();
                }

                //Log.e("benben", responseInfo.result);

                Log.e("benben_home_service", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    String pages = jsonObject.getString("pages");

                    page = Integer.parseInt(pages);
                    count++;

                    Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);

                    String counts = jsonObject.getString("counts");
                    if (!TextUtils.isEmpty(counts) && counts.equals("0")){
                        scrollView.setVisibility(View.GONE);
                        niuniuniuniu.setVisibility(View.VISIBLE);
                    }else {

                        scrollView.setVisibility(View.VISIBLE);
                        niuniuniuniu.setVisibility(View.GONE);
                        try {
                            List<FindServiceEntity> list = Json_FindService.getParse(responseInfo.result);

                            data.addAll(list);

                            Log.e("benben_service_info", list.get(0).getServiceID());

                            adapter = new FindServiceAdapter(FindServiceActivity.this, data);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

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
        }) ;

    }

    private void initView() {

        listView = (BenListView)findViewById(R.id.listView) ;

        listView.setDivider(new ColorDrawable(Color.argb(0, 244, 244, 244)));
        listView.setDividerHeight(1);

        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        scrollView = (MyScrollView)findViewById(R.id.scrollView ) ;

        find_type = (TextView)findViewById(R.id.find_type ) ;
        part = (TextView)findViewById(R.id.part ) ;
        details_type = (TextView) findViewById(R.id.details_type ) ;
        niuniuniuniu = (TextView) findViewById(R.id.niuniuniuniu ) ;



    }

    private void typeGet() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_type_service, null);
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


        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                find_type.setText("资产包收购");
                window.dismiss();
                data.clear();
                typeName = "01" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                findService(typeName, part_a);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("催收机构");
                window.dismiss();
                data.clear();
                typeName = "02" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                findService(typeName, part_a);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("律师事务所");
                window.dismiss();
                data.clear();
                typeName = "03" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                findService(typeName, part_a);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("保理公司");
                window.dismiss();
                data.clear();
                typeName = "04" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                findService(typeName, part_a);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("典当公司");
                window.dismiss();
                data.clear();
                typeName = "05" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                findService(typeName, part_a);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("担保公司");
                window.dismiss();
                data.clear();
                typeName = "05" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                findService(typeName, part_a);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("投融资服务");
                window.dismiss();
                data.clear();
                typeName = "06" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                findService(typeName, part_a );
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("尽职调查");
                window.dismiss();
                data.clear();
                typeName = "10" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                findService(typeName, part_a);
            }
        });
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("资产收购");
                window.dismiss();
                data.clear();
                typeName = "12" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                findService(typeName, part_a);
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("债权收购");
                window.dismiss();
                data.clear();
                typeName = "14";
                count = 1;
                part.setText("地区");
                part_a = "";
                findService(typeName, part_a);
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


        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                part.setText("全国");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "" ;
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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
                findService(typeName, part_a);
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

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }




}
