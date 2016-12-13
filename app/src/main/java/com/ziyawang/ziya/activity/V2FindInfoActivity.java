package com.ziyawang.ziya.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.V2FindInfoAdapter;
import com.ziyawang.ziya.entity.V2InfoEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BitmapHelp;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class V2FindInfoActivity extends BenBenActivity implements View.OnClickListener {

    /**
     * 获取bitmapUtils单例
     */
    public static BitmapUtils bitmapUtils;

    private RelativeLayout relative_details_type;
    private String part_a;
    private RelativeLayout relative_part;
    private TextView details_type;
    private TextView part;
    private RelativeLayout relative_find_type;
    private TextView find_type;
    private RelativeLayout find_vip_type;
    private ListView listView;
    private MyProgressDialog dialog;
    private V3FindInfoAdapter adapter;
    private RelativeLayout pre;
    private TextView niuniuniuniu;
    private String typeName;
    private String vip_type = "";
    private String params_add01;
    private String params_add02;
    private List<V2InfoEntity> data = new ArrayList<V2InfoEntity>();
    private int page;
    private int count = 1;
    private Boolean isOK = true;

    private View footView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加分页加载
//        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
//            @Override
//            public void onScroll(int scrollY) {
//                View childView = scrollView.getChildAt(0);
//                if (childView.getMeasuredHeight() <= scrollY + scrollView.getHeight()) {
//                    mHandler.sendEmptyMessage(201);
//                }
//            }
//        });
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_v2_find_info);
    }

    @Override
    public void initViews() {
        relative_find_type = (RelativeLayout) findViewById(R.id.relative_find_type);
        niuniuniuniu = (TextView) findViewById(R.id.niuniuniuniu);
        pre = (RelativeLayout) findViewById(R.id.pre);
        listView = (ListView) findViewById(R.id.listView);
        //scrollView = (MyScrollView)findViewById(R.id.scrollView ) ;
        listView.setDividerHeight(0);
        LayoutInflater infla = LayoutInflater.from(this);
        footView = infla.inflate(R.layout.my_footview, null);
        listView.addFooterView(footView, null, true);

        find_vip_type = (RelativeLayout) findViewById(R.id.find_vip_type);
        find_type = (TextView) findViewById(R.id.find_type);
        part = (TextView) findViewById(R.id.part);
        relative_part = (RelativeLayout) findViewById(R.id.relative_part);
        details_type = (TextView) findViewById(R.id.details_type);
        relative_details_type = (RelativeLayout) findViewById(R.id.relative_details_type);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        assert floatingActionButton != null;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSearchActivity();
            }
        });

        // 获取bitmapUtils单例
        bitmapUtils = BitmapHelp.getBitmapUtils(this);
        /**
         * 设置默认的图片展现、加载失败的图片展现
         */
        bitmapUtils.configDefaultLoadingImage(R.mipmap.fast_error);
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.error_imgs_big);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

    }

    private void goSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("type", "信息");
        intent.putExtra("title", "搜索信息");
        startActivity(intent);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        find_vip_type.setOnClickListener(this);
        relative_find_type.setOnClickListener(this);
        relative_part.setOnClickListener(this);
        relative_details_type.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String typeName_a = intent.getStringExtra("type");
            if (!TextUtils.isEmpty(typeName_a)) {
                switch (typeName_a) {
                    case "资产包":
                        typeName = "1";
                        find_type.setText("资产包");
                        break;
                    case "融资信息":
                        typeName = "rzxx";
                        find_type.setText("融资信息");
                        break;
                    case "固定资产":
                        typeName = "gdzc";
                        find_type.setText("固定资产");
                        break;
                    case "企业商账":
                        typeName = "18";
                        find_type.setText("企业商账");
                        break;
                    case "法拍资产":
                        typeName = "fpzc";
                        find_type.setText("法拍资产");
                        break;
                    case "个人债权":
                        typeName = "19";
                        find_type.setText("个人债权");
                        break;
                    case "处置公告":
                        typeName = "czgg";
                        find_type.setText("处置公告");
                        break;
                    default:
                        break;
                }
            }
        }
        loadData(typeName, part_a, vip_type, params_add01, params_add02);
    }

    private void loadData(final String typeName,final String part_a,final String vip_type,final String params_add01,final String params_add02) {
        dialog = new MyProgressDialog(V2FindInfoActivity.this, "加载数据中请稍后。。。");
        dialog.show();
        String urls;
        if (GetBenSharedPreferences.getIsLogin(V2FindInfoActivity.this)) {
            urls = String.format(Url.GetInfo, GetBenSharedPreferences.getTicket(V2FindInfoActivity.this));
        } else {
            urls = String.format(Url.GetInfo, "");
        }
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("Vip", vip_type);
        params.addQueryStringParameter("pagecount", "7");
        if (TextUtils.isEmpty(params_add01)) {
            params.addQueryStringParameter("TypeID", typeName);
            params.addQueryStringParameter("ProArea", part_a);
            params.addQueryStringParameter("startpage", "" + count);
        } else {
            params.addQueryStringParameter("TypeID", typeName);
            params.addQueryStringParameter("ProArea", part_a);
            params.addQueryStringParameter("startpage", "" + count);
            params.addQueryStringParameter(params_add01, params_add02);
        }
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("GetInfo", responseInfo.result);
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
                    List<V2InfoEntity> v2InfoEntities = JSON.parseArray(data01.toJSONString(), V2InfoEntity.class);
                    data.addAll(v2InfoEntities);
                    adapter = new V3FindInfoAdapter(V2FindInfoActivity.this, data);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


//                    if (count > 2){
//                        listView.setSelection((count-2)*7 + 1) ;
//                    }

//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Intent intent = new Intent(V2FindInfoActivity.this , V2DetailsFindInfoActivity.class ) ;
//                            intent.putExtra("id" , data.get(position).getProjectID() ) ;
//                            startActivity(intent);
//                        }
//                    });
                    listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true, new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            switch (scrollState) {
                                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 当不迁移转变时
                                    //scrolledX = listView.getScrollX();
                                    //scrolledY = listView.getScrollY();
                                    //Log.e("X" , scrolledX + "" ) ;
                                    //Log.e("Y" , scrolledY + "" ) ;
                                    // 断定迁移转变到底部
                                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                                        if (isOK) {
                                            if (count <= page) {
                                                isOK = false;
                                                //loadData(typeName, part_a, vip_type, params_add01, params_add02);
                                                addData(typeName, part_a, vip_type, params_add01, params_add02);
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
                                                ToastUtils.shortToast(V2FindInfoActivity.this, "信息没有更多数据");
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
        });
    }

    private void addData(String typeName, String part_a, String vip_type, String params_add01, String params_add02) {
        dialog = new MyProgressDialog(V2FindInfoActivity.this, "加载数据中请稍后。。。");
        dialog.show();
        String urls;
        if (GetBenSharedPreferences.getIsLogin(V2FindInfoActivity.this)) {
            urls = String.format(Url.GetInfo, GetBenSharedPreferences.getTicket(V2FindInfoActivity.this));
        } else {
            urls = String.format(Url.GetInfo, "");
        }
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("Vip", vip_type);
        params.addQueryStringParameter("pagecount", "7");
        if (TextUtils.isEmpty(params_add01)) {
            params.addQueryStringParameter("TypeID", typeName);
            params.addQueryStringParameter("ProArea", part_a);
            params.addQueryStringParameter("startpage", "" + count);
        } else {
            params.addQueryStringParameter("TypeID", typeName);
            params.addQueryStringParameter("ProArea", part_a);
            params.addQueryStringParameter("startpage", "" + count);
            params.addQueryStringParameter(params_add01, params_add02);
        }
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("GetInfo", responseInfo.result);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(responseInfo.result);
                String pages = object.getString("pages");
                page = Integer.parseInt(pages);
                count++;
                com.alibaba.fastjson.JSONArray data01 = object.getJSONArray("data");
                List<V2InfoEntity> v2InfoEntities = JSON.parseArray(data01.toJSONString(), V2InfoEntity.class);
                data.addAll(v2InfoEntities);
                adapter.notifyDataSetChanged();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre:
                finish();
                break;
            case R.id.find_vip_type:
                if ("处置公告".equals(find_type.getText().toString())) {
                    ToastUtils.shortToast(V2FindInfoActivity.this, "处置公告无法筛选资源类型");
                } else {
                    vipTypeGet();
                }
                break;
            case R.id.relative_find_type:
                typeGet();
                break;
            case R.id.relative_part:
                if ("处置公告".equals(find_type.getText().toString())) {
                    ToastUtils.shortToast(V2FindInfoActivity.this, "处置公告无法筛选地区");
                } else {
                    partGet();
                }
                break;
            case R.id.relative_details_type:
                partTwoGet();
                break;
            default:
                break;
        }
    }

    private void partTwoGet() {
        String niu_ben = find_type.getText().toString();
        switch (niu_ben) {
            case "资产包":
                popUpWindow01();
                break;
            case "融资信息":
                popUpWindow02();
                break;
            case "固定资产":
                popUpWindow03();
                break;
            case "企业商账":
                popUpWindow04();
                break;
            case "法拍资产":
                popUpWindow05();
                break;
            case "个人债权":
                popUpWindow04();
                break;
            case "处置公告":
                ToastUtils.shortToast(V2FindInfoActivity.this, "处置公告无法筛选更多类型");
                break;
            default:
                ToastUtils.shortToast(V2FindInfoActivity.this, "请先选择您的发布类型");
                break;
        }
    }

    private void popUpWindow05() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_more_five, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button) view.findViewById(R.id.one_one);
        final Button b = (Button) view.findViewById(R.id.one_two);
        final Button c = (Button) view.findViewById(R.id.one_three);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(a.getText().toString());
                params_add01 = "";
                params_add02 = "";
                typeName = "21";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(b.getText().toString());
                params_add01 = "";
                params_add02 = "";
                typeName = "20";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(c.getText().toString());
                params_add01 = "";
                params_add02 = "";
                typeName = "22";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
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
        final Button a = (Button) view.findViewById(R.id.one_one);
        final Button b = (Button) view.findViewById(R.id.one_two);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(a.getText().toString());
                params_add01 = "Law";
                params_add02 = "1";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(b.getText().toString());
                params_add01 = "UnLaw";
                params_add02 = "1";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
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
        final Button a = (Button) view.findViewById(R.id.one_one);
        final Button b = (Button) view.findViewById(R.id.one_two);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(a.getText().toString());
                params_add01 = "";
                params_add02 = "";
                typeName = "16";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(b.getText().toString());
                params_add01 = "";
                params_add02 = "";
                typeName = "12";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
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
        final Button a = (Button) view.findViewById(R.id.one_one);
        final Button b = (Button) view.findViewById(R.id.one_two);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(a.getText().toString());
                params_add01 = "";
                params_add02 = "";
                typeName = "17";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(b.getText().toString());
                params_add01 = "";
                params_add02 = "";
                typeName = "6";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
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
        final Button a = (Button) view.findViewById(R.id.one_one);
        final Button b = (Button) view.findViewById(R.id.one_two);
        final Button c = (Button) view.findViewById(R.id.one_three);
        final Button d = (Button) view.findViewById(R.id.one_four);
        final Button e = (Button) view.findViewById(R.id.one_five);
        final Button f = (Button) view.findViewById(R.id.one_six);
        final Button g = (Button) view.findViewById(R.id.one_seven);
        final Button h = (Button) view.findViewById(R.id.one_eight);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(a.getText().toString());
                params_add01 = "AssetType";
                params_add02 = a.getText().toString();
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(b.getText().toString());
                params_add01 = "AssetType";
                params_add02 = b.getText().toString();
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(c.getText().toString());
                params_add01 = "AssetType";
                params_add02 = c.getText().toString();
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(d.getText().toString());
                params_add01 = "AssetType";
                params_add02 = d.getText().toString();
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(e.getText().toString());
                params_add01 = "FromWhere";
                params_add02 = e.getText().toString();
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(f.getText().toString());
                params_add01 = "FromWhere";
                params_add02 = f.getText().toString();
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(g.getText().toString());
                params_add01 = "FromWhere";
                params_add02 = g.getText().toString();
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                details_type.setText(h.getText().toString());
                params_add01 = "FromWhere";
                params_add02 = h.getText().toString();
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
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

    private void partGet() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_part_service, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button a = (Button) view.findViewById(R.id.a);
        Button b = (Button) view.findViewById(R.id.b);
        Button c = (Button) view.findViewById(R.id.c);
        Button d = (Button) view.findViewById(R.id.d);
        Button e = (Button) view.findViewById(R.id.e);
        Button f = (Button) view.findViewById(R.id.f);
        Button g = (Button) view.findViewById(R.id.g);
        Button h = (Button) view.findViewById(R.id.h);
        Button i = (Button) view.findViewById(R.id.i);
        Button j = (Button) view.findViewById(R.id.j);
        Button k = (Button) view.findViewById(R.id.k);
        Button l = (Button) view.findViewById(R.id.l);
        Button m = (Button) view.findViewById(R.id.m);
        Button n = (Button) view.findViewById(R.id.n);
        Button o = (Button) view.findViewById(R.id.o);
        Button p = (Button) view.findViewById(R.id.p);
        Button q = (Button) view.findViewById(R.id.q);
        Button r = (Button) view.findViewById(R.id.r);
        Button s = (Button) view.findViewById(R.id.s);
        Button t = (Button) view.findViewById(R.id.t);
        Button u = (Button) view.findViewById(R.id.u);
        Button v = (Button) view.findViewById(R.id.v);
        Button w = (Button) view.findViewById(R.id.w);
        Button x = (Button) view.findViewById(R.id.x);
        Button y = (Button) view.findViewById(R.id.y);
        Button z = (Button) view.findViewById(R.id.z);
        Button aa = (Button) view.findViewById(R.id.aa);
        Button bb = (Button) view.findViewById(R.id.bb);
        Button cc = (Button) view.findViewById(R.id.cc);
        Button dd = (Button) view.findViewById(R.id.dd);
        Button ee = (Button) view.findViewById(R.id.ee);
        Button ff = (Button) view.findViewById(R.id.ff);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                part.setText("全国");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("北京");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "北京";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("上海");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "上海";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("广东");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "广东";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("江苏");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "江苏";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("浙江");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "浙江";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("河南");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "河南";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("河北");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "河北";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("辽宁");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "辽宁";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("四川");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "四川";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("湖北");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "湖北";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("湖南");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "湖南";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("福建");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "福建";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("安徽");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "安徽";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("陕西");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "陕西";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("天津");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "天津";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("江西");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "江西";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("重庆");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "重庆";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("吉林");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "吉林";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("云南");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "云南";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("山西");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "山西";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("新疆");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "新疆";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("贵州");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "贵州";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("甘肃");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "甘肃";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("海南");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "海南";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("宁夏");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "宁夏";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        aa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("青海");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "青海";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("西藏");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "西藏";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("黑龙江");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "黑龙江";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("内蒙古");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "内蒙古";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        ee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("山东");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "山东";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part.setText("广西");
                window.dismiss();
                data.clear();
                count = 1;
                part_a = "广西";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
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

    private void typeGet() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_type_v2, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button a = (Button) view.findViewById(R.id.a);
        final Button b = (Button) view.findViewById(R.id.b);
        final Button c = (Button) view.findViewById(R.id.c);
        final Button d = (Button) view.findViewById(R.id.d);
        final Button e = (Button) view.findViewById(R.id.e);
        final Button f = (Button) view.findViewById(R.id.f);
        final Button g = (Button) view.findViewById(R.id.g);

        switch (find_type.getText().toString()) {
            case "资产包":
                a.setSelected(true);
                break;
            case "融资信息":
                b.setSelected(true);
                break;
            case "固定资产":
                c.setSelected(true);
                break;
            case "企业商账":
                d.setSelected(true);
                break;
            case "法拍资产":
                e.setSelected(true);
                break;
            case "个人债权":
                f.setSelected(true);
                break;
            case "处置公告":
                g.setSelected(true);
                break;
            default:
                break;
        }
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("资产包");
                window.dismiss();
                data.clear();
                typeName = "1";
                params_add01 = "";
                params_add02 = "";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("融资信息");
                window.dismiss();
                data.clear();
                typeName = "rzxx";
                params_add01 = "";
                params_add02 = "";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("固定资产");
                window.dismiss();
                data.clear();
                typeName = "gdzc";
                params_add01 = "";
                params_add02 = "";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("企业商账");
                window.dismiss();
                data.clear();
                typeName = "18";
                params_add01 = "";
                params_add02 = "";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("法拍资产");
                window.dismiss();
                data.clear();
                typeName = "fpzc";
                params_add01 = "";
                params_add02 = "";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("个人债权");
                window.dismiss();
                data.clear();
                typeName = "19";
                params_add01 = "";
                params_add02 = "";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_type.setText("处置公告");
                window.dismiss();
                data.clear();
                typeName = "czgg";
                params_add01 = "";
                params_add02 = "";
                vip_type = "";
                count = 1;
                part.setText("地区");
                details_type.setText("更多");
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
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

    private void vipTypeGet() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_vip_type, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final RadioButton a = (RadioButton) view.findViewById(R.id.one_one);
        final RadioButton b = (RadioButton) view.findViewById(R.id.one_two);
        final RadioButton c = (RadioButton) view.findViewById(R.id.one_three);
        final RadioButton d = (RadioButton) view.findViewById(R.id.one_four);
        if ("0".equals(vip_type)) {
            b.setChecked(true);
        } else if ("1".equals(vip_type)) {
            c.setChecked(true);
        } else if ("2".equals(vip_type)) {
            d.setChecked(true);
        } else if ("".equals(vip_type)) {
            a.setChecked(true);
        }

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                vip_type = "";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                vip_type = "0";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                vip_type = "1";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                count = 1;
                vip_type = "2";
                loadData(typeName, part_a, vip_type, params_add01, params_add02);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(find_vip_type);
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
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    public class V3FindInfoAdapter extends BaseAdapter {

        private Activity context;
        private List<V2InfoEntity> list;

        private boolean scrollState = false;

        public void setScrollState(boolean scrollState) {
            this.scrollState = scrollState;
        }

        public V3FindInfoAdapter() {
        }

        public V3FindInfoAdapter(Activity context, List<V2InfoEntity> list) {
            super();
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.v2_find_info_items, parent, false);
                holder = new ViewHolder();
                holder.news_title = (TextView) convertView.findViewById(R.id.news_title);
                holder.info_part = (TextView) convertView.findViewById(R.id.info_part);
                holder.news_image = (ImageView) convertView.findViewById(R.id.news_image);
                holder.info_part_one = (TextView) convertView.findViewById(R.id.info_part_one);
                holder.info_part_two = (TextView) convertView.findViewById(R.id.info_part_two);
                holder.info_no = (TextView) convertView.findViewById(R.id.info_no);
                holder.info_bee = (TextView) convertView.findViewById(R.id.info_bee);
                holder.info_danwei = (TextView) convertView.findViewById(R.id.info_danwei);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                holder.liangdian = (LinearLayout) convertView.findViewById(R.id.liangdian);
                holder.liangdian01 = (TextView) convertView.findViewById(R.id.liangdian01);
                holder.liangdian02 = (TextView) convertView.findViewById(R.id.liangdian02);
                holder.liangdian03 = (TextView) convertView.findViewById(R.id.liangdian03);
                holder.image_01 = (ImageView) convertView.findViewById(R.id.image_01);
                holder.image_02 = (ImageView) convertView.findViewById(R.id.image_02);
                holder.text_01 = (TextView) convertView.findViewById(R.id.text_01);
                holder.text_02 = (TextView) convertView.findViewById(R.id.text_02);

                holder.czgg_relative = (RelativeLayout) convertView.findViewById(R.id.czgg_relative);
                holder.relative_info = (RelativeLayout) convertView.findViewById(R.id.relative_info);
                holder.czgg_image = (ImageView) convertView.findViewById(R.id.czgg_image);
                holder.czgg_title = (TextView) convertView.findViewById(R.id.czgg_title);
                holder.news_des = (TextView) convertView.findViewById(R.id.news_des);
                holder.news_time = (TextView) convertView.findViewById(R.id.news_time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if ("99".equals(list.get(position).getTypeID())) {
                holder.czgg_relative.setVisibility(View.VISIBLE);
                holder.relative_info.setVisibility(View.GONE);
                holder.czgg_title.setText(list.get(position).getNewsTitle());
                holder.news_des.setText(Html.fromHtml(list.get(position).getBrief().replace("　　", "")));
                String substring01 = list.get(position).getPublishTime().substring(5, 10);
                String substring02 = list.get(position).getPublishTime().substring(11, 16);
                holder.news_time.setText(substring01 + "/" + substring02);

                /**
                 * display参数 (ImageView container, String uri,
                 * BitmapLoadCallBack<ImageView> callBack)
                 */
                bitmapUtils.display(holder.czgg_image, Url.FileIP + list.get(position).getNewsLogo(), new CustomBitmapLoadCallBack(holder));

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailsNewsActivity.class);
                        intent.putExtra("id", list.get(position).getNewsID());
                        intent.putExtra("type" , "处置公告" ) ;
                        context.startActivity(intent);
                    }
                });

            } else {
                /**
                 * display参数 (ImageView container, String uri,
                 * BitmapLoadCallBack<ImageView> callBack)
                 */
                bitmapUtils.display(holder.news_image, Url.FileIP + list.get(position).getPictureDes1(), new CustomBitmapLoadCallBack(holder));

                holder.czgg_relative.setVisibility(View.GONE);
                holder.relative_info.setVisibility(View.VISIBLE);
                switch (list.get(position).getTypeID()) {
                    case "1":
                        holder.image_01.setImageResource(R.mipmap.v2shichang);
                        holder.image_02.setImageResource(R.mipmap.v2zhuanrang);
                        if (list.get(position).getTotalMoney().length() >= 9 && !TextUtils.isEmpty(list.get(position).getTotalMoney())){
                            holder.text_01.setText(list.get(position).getTotalMoney().substring( 0 , list.get(position).getTotalMoney().length()-3) + "万");
                        }else {
                            holder.text_01.setText(list.get(position).getTotalMoney() + "万");
                        }

                        if (list.get(position).getTransferMoney().length() >= 9 && !TextUtils.isEmpty(list.get(position).getTransferMoney())){
                            holder.text_02.setText(list.get(position).getTransferMoney().substring( 0 , list.get(position).getTransferMoney().length()-3) + "万");
                        }else {
                            holder.text_02.setText(list.get(position).getTransferMoney() + "万");
                        }
                        break;
                    case "6":
                        holder.image_01.setImageResource(R.mipmap.v2rongzi);
                        holder.image_02.setVisibility(View.GONE);
                        holder.text_01.setText(list.get(position).getTotalMoney() + "万");
                        holder.text_02.setVisibility(View.GONE);
                        break;
                    case "12":
                        if (list.get(position).getMarketPrice().length() >= 9 && !TextUtils.isEmpty(list.get(position).getMarketPrice())){
                            holder.text_01.setText(list.get(position).getMarketPrice().substring( 0 , list.get(position).getMarketPrice().length()-3) + "万");
                        }else {
                            holder.text_01.setText(list.get(position).getMarketPrice() + "万");
                        }

                        if (list.get(position).getTransferMoney().length() >= 9 && !TextUtils.isEmpty(list.get(position).getTransferMoney())){
                            holder.text_02.setText(list.get(position).getTransferMoney().substring( 0 , list.get(position).getTransferMoney().length()-3) + "万");
                        }else {
                            holder.text_02.setText(list.get(position).getTransferMoney() + "万");
                        }

                        holder.image_01.setImageResource(R.mipmap.v2shichang);
                        holder.image_02.setImageResource(R.mipmap.v2zhuanrang);
                        break;
                    case "16":
                        holder.image_01.setImageResource(R.mipmap.v2zhuanrang);
                        holder.image_02.setVisibility(View.GONE);
                        holder.text_01.setText(list.get(position).getTransferMoney() + "万");
                        holder.text_02.setVisibility(View.GONE);
                        break;
                    case "17":
                        holder.image_01.setImageResource(R.mipmap.v2rongzi);
                        holder.image_02.setVisibility(View.GONE);
                        holder.text_01.setText(list.get(position).getMoney() + "万");
                        holder.text_02.setVisibility(View.GONE);
                        break;
                    case "18":
                        holder.image_01.setImageResource(R.mipmap.v2zhaiquan);
                        holder.image_02.setVisibility(View.GONE);
                        holder.text_01.setText(list.get(position).getMoney() + "万");
                        holder.text_02.setVisibility(View.GONE);
                        break;
                    case "19":
                        holder.image_01.setImageResource(R.mipmap.v2zongjine);
                        holder.image_02.setVisibility(View.GONE);
                        holder.text_01.setText(list.get(position).getTotalMoney() + "万");
                        holder.text_02.setVisibility(View.GONE);
                        break;
                    case "20":
                    case "21":
                    case "22":
                        holder.image_01.setImageResource(R.mipmap.v2qipai);
                        holder.image_02.setVisibility(View.GONE);
                        holder.text_01.setText(list.get(position).getMoney() + "万");
                        holder.text_02.setVisibility(View.GONE);
                        break;

                }
                if (list.get(position).getProArea().contains("-")) {
                    String[] split = list.get(position).getProArea().split("-");
                    holder.info_part.setText(split[0].toString().trim());
                } else {
                    holder.info_part.setText(list.get(position).getProArea());
                }
                holder.news_title.setText(list.get(position).getTitle());
                holder.info_part_one.setText(list.get(position).getTypeName());
                holder.info_part_two.setText(list.get(position).getAssetType());
                holder.info_no.setText(list.get(position).getProjectNumber());

                String proLabel = list.get(position).getProLabel();
                if (TextUtils.isEmpty(proLabel) || "".equals(proLabel)) {
                    holder.liangdian.setVisibility(View.GONE);
                } else {
                    holder.liangdian.setVisibility(View.VISIBLE);
                    String[] split = proLabel.split(",");
                    switch (split.length) {
                        case 1:
                            holder.liangdian01.setVisibility(View.VISIBLE);
                            holder.liangdian02.setVisibility(View.GONE);
                            holder.liangdian03.setVisibility(View.GONE);
                            holder.liangdian01.setText(split[0]);
                            break;
                        case 2:
                            holder.liangdian01.setVisibility(View.VISIBLE);
                            holder.liangdian01.setText(split[0]);
                            holder.liangdian02.setVisibility(View.VISIBLE);
                            holder.liangdian02.setText(split[1]);
                            holder.liangdian03.setVisibility(View.GONE);
                            break;
                        default:
                            holder.liangdian01.setVisibility(View.VISIBLE);
                            holder.liangdian01.setText(split[0]);
                            holder.liangdian02.setVisibility(View.VISIBLE);
                            holder.liangdian02.setText(split[1]);
                            holder.liangdian03.setVisibility(View.VISIBLE);
                            holder.liangdian03.setText(split[2]);

                            break;
                    }
                }
                String member = list.get(position).getMember();
                if (member.equals("1")) {
                    holder.img.setVisibility(View.VISIBLE);
                    holder.info_bee.setVisibility(View.GONE);
                    holder.info_danwei.setVisibility(View.GONE);
                    holder.img.setImageResource(R.mipmap.v2vipresources);
                } else if (member.equals("2")) {
                    holder.img.setVisibility(View.VISIBLE);
                    holder.info_bee.setVisibility(View.VISIBLE);
                    holder.info_danwei.setVisibility(View.VISIBLE);
                    holder.img.setImageResource(R.mipmap.v2moneyresources);
                    holder.info_bee.setText(list.get(position).getPrice());
                } else {
                    holder.img.setVisibility(View.GONE);
                    holder.info_bee.setVisibility(View.GONE);
                    holder.info_danwei.setVisibility(View.GONE);
                }

                final View finalConvertView = convertView;
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //未登录，去登陆，登陆之后，判断是否是收费资源，是收费资源，那么直接调用anthme 的接口，自己的
                        finalConvertView.setEnabled(false);
                        if ("2".equals(list.get(position).getMember())) {
                            if (GetBenSharedPreferences.getIsLogin(context)) {
                                //调用authme的接口，拿到自己的role 和 account
                                loadData(position, finalConvertView);

                            } else {
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                                finalConvertView.setEnabled(true);
                            }

                        } else if ("1".equals(list.get(position).getMember())) {
                            showForVipPop(finalConvertView);
                        } else {
                            String id = list.get(position).getProjectID();
                            Intent intent = new Intent(context, V2DetailsFindInfoActivity.class);
                            intent.putExtra("id", id);
                            context.startActivity(intent);
                            finalConvertView.setEnabled(true);
                        }

                    }
                });
            }
            return convertView;
        }

        private void showForVipPop(final View convertView) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.popupwindow_not_vip, null);
            final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.relative);
            final Button submit = (Button) view.findViewById(R.id.submit);
            final ImageButton cancel = (ImageButton) view.findViewById(R.id.cancel);
            //取消
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                }
            });
            //消费
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                }
            });

            window.setFocusable(true);
            //点击空白的地方关闭PopupWindow
            window.setBackgroundDrawable(new BitmapDrawable());
            window.setAnimationStyle(R.style.mypopwindow_anim_style);
            // 在底部显示
            window.showAtLocation(relative, Gravity.CENTER, 0, 0);
            // 设置popWindow的显示和消失动画

            backgroundAlpha(0.2f);
            convertView.setEnabled(true);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                    convertView.setEnabled(true);
                }
            });
        }

        private void loadData(final int position, final View finalConvertView) {
            String urls = String.format(Url.Myicon, GetBenSharedPreferences.getTicket(context));
            HttpUtils utils = new HttpUtils();
            final RequestParams params = new RequestParams();
            utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //处理请求成功后的数据
                    try {
                        final JSONObject jsonObject = new JSONObject(responseInfo.result);
                        JSONObject user = jsonObject.getJSONObject("user");
                        final String account = user.getString("Account");
                        String role = jsonObject.getString("role");
                        if (GetBenSharedPreferences.getUserId(context).equals(list.get(position).getUserID())) {
                            String id = list.get(position).getProjectID();
                            Intent intent = new Intent(context, V2DetailsFindInfoActivity.class);
                            intent.putExtra("id", id);
                            context.startActivity(intent);
                            finalConvertView.setEnabled(true);
                        } else {
                            if ("1".equals(role)) {
                                String urls = String.format(Url.ISPay, GetBenSharedPreferences.getTicket(context));
                                HttpUtils httpUtils = new HttpUtils();
                                RequestParams params1 = new RequestParams();
                                params1.addBodyParameter("ProjectID", list.get(position).getProjectID());
                                httpUtils.send(HttpRequest.HttpMethod.POST, urls, params1, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        try {
                                            JSONObject jsonObject1 = new JSONObject(responseInfo.result);
                                            String payFlag = jsonObject1.getString("PayFlag");
                                            if ("1".equals(payFlag)) {
                                                String id = list.get(position).getProjectID();
                                                Intent intent = new Intent(context, V2DetailsFindInfoActivity.class);
                                                intent.putExtra("id", id);
                                                context.startActivity(intent);
                                                finalConvertView.setEnabled(true);
                                            } else {
                                                showPopUpWindow(position, account, finalConvertView);

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(HttpException error, String msg) {
                                        finalConvertView.setEnabled(true);
                                    }
                                });

                            } else {
                                ToastUtils.shortToast(context, "您需要先通过服务方认证才可查看收费类信息");
                                finalConvertView.setEnabled(true);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    //打印用户的失败回调
                    error.printStackTrace();
                    ToastUtils.shortToast(context, "网络连接异常");
                }
            });
        }

        private void showPopUpWindow(final int position, String account, final View convertView) {
            // 利用layoutInflater获得View
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.popupwindow_publish, null);
            final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.relative);
            final TextView info_type = (TextView) view.findViewById(R.id.info_type);
            final TextView info_title = (TextView) view.findViewById(R.id.info_title);
            final TextView shejian_price = (TextView) view.findViewById(R.id.shejian_price);
            final TextView shejian_balance = (TextView) view.findViewById(R.id.shejian_balance);
            final TextView balance_type = (TextView) view.findViewById(R.id.balance_type);
            final Button shejian_pay = (Button) view.findViewById(R.id.shejian_pay);
            final Button shejian_recharge = (Button) view.findViewById(R.id.shejian_recharge);
            final ImageButton pay_cancel = (ImageButton) view.findViewById(R.id.pay_cancel);
            final LinearLayout shejian_two = (LinearLayout) view.findViewById(R.id.shejian_two);
            TextPaint tp = shejian_price.getPaint();
            tp.setFakeBoldText(true);
            TextPaint tp01 = shejian_balance.getPaint();
            tp01.setFakeBoldText(true);

            //消费
            shejian_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //去消费
                    goPay(window, position);
                }
            });
            //充值
            shejian_recharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到充值页面
                    goRechargeActivity(window);
                }
            });

            //Member 2收费信息 其他为不收费信息
            info_type.setText("该信息为付费资源");
            info_title.setText("消耗芽币可查看详细信息");
            shejian_two.setVisibility(View.VISIBLE);
            shejian_price.setText(list.get(position).getPrice());
            shejian_balance.setText(account);
            if (Integer.parseInt(account) < Integer.parseInt(list.get(position).getPrice())) {
                balance_type.setVisibility(View.VISIBLE);
            } else {
                balance_type.setVisibility(View.GONE);
            }

            pay_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                }
            });
            window.setFocusable(true);
            //点击空白的地方关闭PopupWindow
            window.setBackgroundDrawable(new BitmapDrawable());
            window.setAnimationStyle(R.style.mypopwindow_anim_style);
            // 在底部显示
            window.showAtLocation(relative, Gravity.CENTER, 0, 0);
            // 设置popWindow的显示和消失动画

            backgroundAlpha(0.2f);
            convertView.setEnabled(true);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                    convertView.setEnabled(true);
                }
            });

        }

        /**
         * 设置添加屏幕的背景透明度
         */
        public void backgroundAlpha(float bgAlpha) {
            WindowManager.LayoutParams lp = context.getWindow().getAttributes();
            lp.alpha = bgAlpha;
            context.getWindow().setAttributes(lp);
        }

        private void goPay(final PopupWindow window, final int position) {
            String url = String.format(Url.Pay, GetBenSharedPreferences.getTicket(context));
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("ProjectID", list.get(position).getProjectID());
            httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("benben", responseInfo.result);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseInfo.result);
                        String status_code = jsonObject.getString("status_code");
                        switch (status_code) {
                            case "200":
                                list.get(position).setPayFlag("1");
                                window.dismiss();
                                ToastUtils.shortToast(context, "购买成功");
                                String id = list.get(position).getProjectID();
                                Intent intent = new Intent(context, V2DetailsFindInfoActivity.class);
                                intent.putExtra("id", id);
                                context.startActivity(intent);
                                break;
                            case "416":
                                ToastUtils.shortToast(context, "非收费信息");
                                break;
                            case "417":
                                ToastUtils.shortToast(context, "您已经支付过该条信息");
                                break;
                            case "418":
                                ToastUtils.shortToast(context, "余额不足，请充值。");
                                break;
                            default:
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    error.printStackTrace();
                    ToastUtils.shortToast(context, "网络连接异常，支付失败");
                }
            });
        }

        private void goRechargeActivity(PopupWindow window) {
            Intent intent = new Intent(context, RechargeActivity.class);
            context.startActivity(intent);
            window.dismiss();
        }

        public void addAll(Collection<? extends V2InfoEntity> collection) {
            list.addAll(collection);
            notifyDataSetChanged();

        }

        public void clear() {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public class ViewHolder {
        ImageView news_image;
        ImageView img;
        TextView news_title;
        TextView info_part;
        TextView info_part_one;
        TextView info_part_two;
        TextView info_no;
        TextView info_bee;
        TextView info_danwei;
        LinearLayout liangdian;
        TextView liangdian01;
        TextView liangdian02;
        TextView liangdian03;
        LinearLayout jiage;
        ImageView image_01;
        ImageView image_02;
        TextView text_01;
        TextView text_02;

        RelativeLayout czgg_relative;
        RelativeLayout relative_info;
        ImageView czgg_image;
        TextView czgg_title;
        TextView news_des;
        TextView news_time;

    }

    /**
     * 接口
     *
     * @author Administrator
     */
    public class CustomBitmapLoadCallBack extends DefaultBitmapLoadCallBack<ImageView> {
        private final ViewHolder holder;

        public CustomBitmapLoadCallBack(ViewHolder holder) {
            this.holder = holder;
        }

        /**
         * 加载过程中，进行进度展示
         */
        @Override
        public void onLoading(ImageView container, String uri, BitmapDisplayConfig config, long total, long current) {
            // 百分比展示进度
            //this.holder.imgPb.setProgress((int) (current * 100 / total));
        }

        /**
         * 加载图片完毕
         */
        @Override
        public void onLoadCompleted(ImageView imageview, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
            // 设置图片到listview
            fadeInDisplay(imageview, bitmap);
            // 加载完毕，修改进度值
            //this.holder.imgPb.setProgress(100);
        }
    }

    /**
     * 动画效果--渐变效果展示
     *
     * @param imageView
     * @param bitmap
     */
    private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
        final TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{TRANSPARENT_DRAWABLE,
                new BitmapDrawable(imageView.getResources(), bitmap)});
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(500);
    }

    private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(Color.argb(0, 0, 0, 0));

}
