package com.ziyawang.ziya.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.FindServiceAdapter;
import com.ziyawang.ziya.entity.FindServiceEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BitmapHelp;
import com.ziyawang.ziya.view.MyProgressDialog;

import java.util.ArrayList;
import java.util.List;


public class FindServiceActivity extends BenBenActivity implements View.OnClickListener {

    public static BitmapUtils bitmapUtils;
    //服务方类型区域
    private RelativeLayout relative_find_type ;
    //服务方地区区域
    private RelativeLayout relative_part ;
    //服务方等级区域
    private RelativeLayout relative_details_type ;
    //服务方类型
    private TextView find_type ;
    //服务方地区
    private TextView part ;
    //服务方等级
    private TextView details_type ;
    //搜索服务方的按钮
    private RelativeLayout search_service ;
    private FindServiceAdapter adapter ;
    private ListView listView ;
    //返回按钮
    private RelativeLayout pre ;
    private List<FindServiceEntity> data  = new ArrayList<FindServiceEntity>();
    private int page  ;
    private int count = 1 ;
    private Boolean isOK = true ;
    private MyProgressDialog dialog ;

    private String typeName ;
    private String part_a ;

    private TextView niuniuniuniu ;

    private View footView ;

    private String niu = "不限" ;
    private String ServiceLevel = "" ;


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
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_find_service);
    }

    @Override
    public void initViews() {
        listView = (ListView)findViewById(R.id.listView) ;
        listView.setDivider(new ColorDrawable(Color.argb(0, 244, 244, 244)));
        listView.setDividerHeight(1);
        LayoutInflater infla = LayoutInflater.from(this);
        footView = infla.inflate(R.layout.my_footview, null);
        listView.addFooterView(footView, null, true);
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        find_type = (TextView)findViewById(R.id.find_type ) ;
        part = (TextView)findViewById(R.id.part ) ;
        details_type = (TextView) findViewById(R.id.details_type ) ;
        niuniuniuniu = (TextView) findViewById(R.id.niuniuniuniu ) ;
        search_service = (RelativeLayout)findViewById(R.id.search_service ) ;
        relative_find_type = (RelativeLayout)findViewById(R.id.relative_find_type ) ;
        relative_part = (RelativeLayout)findViewById(R.id.relative_part ) ;
        relative_details_type = (RelativeLayout)findViewById(R.id.relative_details_type ) ;

        // 获取bitmapUtils单例
        bitmapUtils = BitmapHelp.getBitmapUtils(this);
        /**
         * 设置默认的图片展现、加载失败的图片展现
         */
        bitmapUtils.configDefaultLoadingImage(R.mipmap.fast_error);
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.error_imgs_big);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        search_service.setOnClickListener(this);
        relative_find_type.setOnClickListener(this);
        relative_part.setOnClickListener(this);
        relative_details_type.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent() ;
        if (intent != null ){
            String typeName_a = intent.getStringExtra("typeName");
            if (!TextUtils.isEmpty(typeName_a)){
                switch (typeName_a){
                    case "01" :
                        typeName = typeName_a ;
                        find_type.setText("收购资产包");
                        break;
                    case "02" :
                        typeName = typeName_a ;
                        find_type.setText("委外催收");
                        break;
                    case "03" :
                        typeName = typeName_a ;
                        find_type.setText("法律服务");
                        break;
                    case "06" :
                        typeName = typeName_a ;
                        find_type.setText("投融资服务");
                        break;
                    case "12" :
                        typeName = typeName_a ;
                        find_type.setText("收购固产");
                        break;
                    default:
                        break;
                }
            }
        }
        //加载数据
        loadData(typeName, part_a) ;
    }

    private void detailsTypeGet() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_details_type_service, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RadioButton one_one = (RadioButton)view.findViewById(R.id.one_one);
        RadioButton one_two = (RadioButton)view.findViewById(R.id.one_two);
        if ("不限".equals(niu)) {
            one_one.setChecked(true);
        } else if ("会员".equals(niu)) {
            one_two.setChecked(true);
        }
        one_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                ServiceLevel = "" ;
                niu = "不限" ;
                loadData(typeName, part_a) ;
            }
        });
        one_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                ServiceLevel = "1" ;
                niu = "会员" ;
                loadData(typeName, part_a) ;
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

    private void loadData(final String typeName , String part) {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(FindServiceActivity.this , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();

        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        params.addQueryStringParameter("pagecount", "7");
        params.addQueryStringParameter("startpage", "" + count);
        params.addQueryStringParameter("ServiceType", typeName);
        params.addQueryStringParameter("ServiceArea", part);
        params.addQueryStringParameter("ServiceLevel", ServiceLevel );

        httpUtils.send(HttpRequest.HttpMethod.GET, Url.GetService, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("GetService", responseInfo.result);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(responseInfo.result);
                String pages = object.getString("pages");
                page = Integer.parseInt(pages);
                count++;
                String counts = object.getString("counts");
                if (!TextUtils.isEmpty(counts) && counts.equals("0")) {
                    listView.setVisibility(View.GONE);
                    niuniuniuniu.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    niuniuniuniu.setVisibility(View.GONE);
                    com.alibaba.fastjson.JSONArray data01 = object.getJSONArray("data");
                    List<FindServiceEntity> loadDataEntity = JSON.parseArray(data01.toJSONString(), FindServiceEntity.class);
                    data.addAll(loadDataEntity);
                    adapter = new FindServiceAdapter(FindServiceActivity.this, data);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true, new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            switch (scrollState) {
                                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 当不迁移转变时
                                    // 断定迁移转变到底部
                                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                                        if (isOK) {
                                            if (count <= page) {
                                                isOK = false;
                                                addData(typeName, part_a);
                                                new Thread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            Thread.sleep(2000);
                                                            isOK = true;
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }).start();
                                            } else {
                                                ToastUtils.shortToast(FindServiceActivity.this, "服务没有更多数据");
                                            }
                                        }
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }
                    }));
                }
                Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);

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

    private void addData(String typeName, String part) {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(FindServiceActivity.this , "数据加载中，请稍后。。。");
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.show();

        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.configCurrentHttpCacheExpiry(1000) ;
        params.addQueryStringParameter("pagecount", "7");
        params.addQueryStringParameter("startpage", "" + count);
        params.addQueryStringParameter("ServiceType", typeName);
        params.addQueryStringParameter("ServiceArea", part);
        params.addQueryStringParameter("ServiceLevel", ServiceLevel );

        httpUtils.send(HttpRequest.HttpMethod.GET, Url.GetService, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("GetService", responseInfo.result);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(responseInfo.result);
                String pages = object.getString("pages");
                page = Integer.parseInt(pages);
                count++;
                String counts = object.getString("counts");
                if (!TextUtils.isEmpty(counts) && counts.equals("0")) {
                    listView.setVisibility(View.GONE);
                    niuniuniuniu.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    niuniuniuniu.setVisibility(View.GONE);
                    com.alibaba.fastjson.JSONArray data01 = object.getJSONArray("data");
                    List<FindServiceEntity> loadDataEntity = JSON.parseArray(data01.toJSONString(), FindServiceEntity.class);
                    data.addAll(loadDataEntity);
                    adapter.notifyDataSetChanged();
                }
                Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);

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

    private void goSearchActivity() {
        Intent intent = new Intent( this, SearchActivity.class);
        intent.putExtra("type", "服务");
        intent.putExtra("title", "搜索服务");
        startActivity(intent);
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

        Log.e("benbne", find_type.getText().toString()) ;

        switch (find_type.getText().toString()){
            case "收购资产包" :
                a.setSelected(true);
                break;
            case "委外催收" :
                b.setSelected(true);
                break;
            case "法律服务" :
                c.setSelected(true);
                break;
            case "投融资服务" :
                d.setSelected(true);
                break;
            case "收购固产" :
                e.setSelected(true);
                break;
            case "保理公司" :
                f.setSelected(true);
                break;
            case "典当公司" :
                g.setSelected(true);
                break;
            case "尽职调查" :
                h.setSelected(true);
                break;
            case "债权收购" :
                i.setSelected(true);
                break;
            case "担保公司" :
                j.setSelected(true);
                break;
            default:
                break;
        }

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("收购资产包");
                window.dismiss();
                data.clear();
                typeName = "01" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                loadData(typeName, part_a);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("委外催收");
                window.dismiss();
                data.clear();
                typeName = "02";
                count = 1;
                part.setText("地区");
                part_a = "";
                loadData(typeName, part_a);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("法律服务");
                window.dismiss();
                data.clear();
                typeName = "03" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                loadData(typeName, part_a);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("投融资服务");
                window.dismiss();
                data.clear();
                typeName = "06" ;
                count = 1 ;
                part.setText("地区");
                part_a = "" ;
                loadData(typeName, part_a);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("收购固产");
                window.dismiss();
                data.clear();
                typeName = "12";
                count = 1;
                part.setText("地区");
                part_a = "";
                loadData(typeName, part_a);
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("保理公司");
                window.dismiss();
                data.clear();
                typeName = "04";
                count = 1;
                part.setText("地区");
                part_a = "";
                loadData(typeName, part_a);
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("典当公司");
                window.dismiss();
                data.clear();
                typeName = "05";
                count = 1;
                part.setText("地区");
                part_a = "";
                loadData(typeName, part_a);
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("尽职调查");
                window.dismiss();
                data.clear();
                typeName = "10";
                count = 1;
                part.setText("地区");
                part_a = "";
                loadData(typeName, part_a);
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("债权收购");
                window.dismiss();
                data.clear();
                typeName = "14";
                count = 1;
                part.setText("地区");
                part_a = "";
                loadData(typeName, part_a);
            }
        });

        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("担保公司");
                window.dismiss();
                data.clear();
                typeName = "05";
                count = 1;
                part.setText("地区");
                part_a = "";
                loadData(typeName, part_a);
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

        if (!TextUtils.isEmpty(part_a)){
            switch (part_a){
                case "" :
                    a.setSelected(true);
                    break;
                case "北京" :
                    b.setSelected(true);
                    break;
                case "上海" :
                    c.setSelected(true);
                    break;
                case "广东" :
                    d.setSelected(true);
                    break;
                case "江苏" :
                    e.setSelected(true);
                    break;
                case "浙江" :
                    f.setSelected(true);
                    break;
                case "河南" :
                    g.setSelected(true);
                    break;
                case "河北" :
                    h.setSelected(true);
                    break;
                case "辽宁" :
                    i.setSelected(true);
                    break;
                case "四川" :
                    j.setSelected(true);
                    break;
                case "湖北" :
                    k.setSelected(true);
                    break;
                case "湖南" :
                    l.setSelected(true);
                    break;
                case "福建" :
                    m.setSelected(true);
                    break;
                case "安徽" :
                    n.setSelected(true);
                    break;
                case "陕西" :
                    o.setSelected(true);
                    break;
                case "天津" :
                    p.setSelected(true);
                    break;
                case "江西" :
                    q.setSelected(true);
                    break;
                case "重庆" :
                    r.setSelected(true);
                    break;
                case "吉林" :
                    s.setSelected(true);
                    break;
                case "云南" :
                    t.setSelected(true);
                    break;
                case "山西" :
                    u.setSelected(true);
                    break;
                case "新疆" :
                    v.setSelected(true);
                    break;
                case "贵州" :
                    w.setSelected(true);
                    break;
                case "甘肃" :
                    x.setSelected(true);
                    break;
                case "海南" :
                    y.setSelected(true);
                    break;
                case "宁夏" :
                    z.setSelected(true);
                    break;
                case "青海" :
                    aa.setSelected(true);
                    break;
                case "西藏" :
                    bb.setSelected(true);
                    break;
                case "黑龙江" :
                    cc.setSelected(true);
                    break;
                case "内蒙古" :
                    dd.setSelected(true);
                    break;
                case "山东" :
                    ee.setSelected(true);
                    break;
                case "广西" :
                    ff.setSelected(true);
                    break;
                default:
                    break;
            }
        }else {
            a.setSelected(true);
        }



        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("全国");
                window.dismiss();
                data.clear();
                count = 1 ;
                part_a = "" ;
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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
                loadData(typeName, part_a);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.search_service :
                goSearchActivity() ;
                break;
            case R.id.relative_find_type :
                typeGet();
                break;
            case R.id.relative_part :
                partGet();
                break;
            case R.id.relative_details_type :
                detailsTypeGet();
                break;
            default:
                break;
        }
    }

}
