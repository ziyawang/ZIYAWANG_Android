package com.ziyawang.ziya.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.GoldDetailsAdapter;
import com.ziyawang.ziya.adapter.MyRushAdapter;
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.entity.GoldDetailsEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.Json_FindInfo;
import com.ziyawang.ziya.tools.Json_GoldDetails;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BenListView;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoldDetailsActivity extends BenBenActivity implements View.OnClickListener {

    private List<GoldDetailsEntity> data  = new ArrayList<GoldDetailsEntity>();
    private MyScrollView scrollView ;
    private int page  ;
    private int count = 1 ;
    private Boolean isOK = true ;
    private MyProgressDialog dialog ;
    //返回安按钮
    private RelativeLayout pre ;
    //筛选按钮
    private TextView gold_type ;
    //明细数据。
    List<GoldDetailsEntity> list ;
    //适配器
    GoldDetailsAdapter adapter ;
    BenListView gold_details_listView ;
    //搜索type
    private String type  ;
    //没有更多数据页面
    private TextView niuniuniuniu ;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 10401) {
                Log.e("HomeINfo", count + "====================================" + page) ;
                if (isOK){
                    if (count <= page ){
                        isOK = false ;
                        dialog = new MyProgressDialog( GoldDetailsActivity.this , "加载数据中请稍后。。。") ;
                        dialog.show();
                        loadData(type);
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
                        ToastUtils.shortToast(GoldDetailsActivity.this, "没有更多数据");
                    }
                }
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载数据
        loadData(type) ;

        //添加分页加载
        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {

                View childView = scrollView.getChildAt(0);
                if (childView.getMeasuredHeight() <= scrollY + scrollView.getHeight()) {
                    mHandler.sendEmptyMessage(10401);
                }
            }
        });
    }

    private void loadData(String type ) {
        String urls = String.format(Url.GoldDetails, GetBenSharedPreferences.getTicket(GoldDetailsActivity.this)) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("Type" , type );
        params.addQueryStringParameter("startpage", "" + count);
        params.addQueryStringParameter("pagecount", "10" );
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (dialog != null) {
                    dialog.dismiss();
                }
                //处理数据
                try {
                    dealResult(responseInfo.result);
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
                ToastUtils.shortToast(GoldDetailsActivity.this, "网络连接异常");
            }
        }) ;
    }

    private void dealResult(String result) throws JSONException {
        Log.e("benben", result) ;
        list = Json_GoldDetails.getParse(result);

        if (list.size()==0){
            scrollView.setVisibility(View.GONE);
            niuniuniuniu.setVisibility(View.VISIBLE);
        }else {
            scrollView.setVisibility(View.VISIBLE);
            niuniuniuniu.setVisibility(View.GONE);
            JSONObject jsonObject = new JSONObject(result);
            String pages = jsonObject.getString("pages");
            page = Integer.parseInt(pages);
            count++;
            Log.e("benbne", "当前页：" + count + "-------------总页数：" + pages);

            data.addAll(list);
            adapter = new GoldDetailsAdapter(GoldDetailsActivity.this , data ) ;
            gold_details_listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            gold_details_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //ToastUtils.shortToast(GoldDetailsActivity.this, list.get(position).getOrderNumber());
                    Intent intent = new Intent(GoldDetailsActivity.this, GoldDetails02Activity.class);
                    //1是充值，2是消费
                    intent.putExtra("money", data.get(position).getMoney());
                    intent.putExtra("type", data.get(position).getType());
                    intent.putExtra("time", data.get(position).getCreated_at());
                    intent.putExtra("orderNumber", data.get(position).getOrderNumber());
                    intent.putExtra("operates", data.get(position).getOperates());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_gold_details);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre) ;
        gold_type = (TextView)findViewById(R.id.gold_type) ;
        niuniuniuniu = (TextView)findViewById(R.id.niuniuniuniu) ;
        gold_details_listView = (BenListView)findViewById(R.id.gold_details_listView) ;
        scrollView = (MyScrollView)findViewById(R.id.scrollView) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this) ;
        gold_type.setOnClickListener(this) ;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.gold_type :
                showPopUpWindow() ;
                break;
            default:
                break;
        }
    }

    private void showPopUpWindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_gold_type, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RelativeLayout my_gold_relative = (RelativeLayout)findViewById(R.id.my_gold_relative) ;
        final Button a = (Button)view.findViewById(R.id.one_one);
        final Button b = (Button)view.findViewById(R.id.one_two);
        final Button c = (Button)view.findViewById(R.id.one_three);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                gold_type.setText("全部");
                type = "" ;
                count = 1 ;
                loadData(type);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                gold_type.setText("充值");
                type = "1" ;
                count = 1 ;
                loadData(type);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                data.clear();
                gold_type.setText("付费");
                type = "2" ;
                count = 1 ;
                loadData(type);
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.animation);
        // 在底部显示
        window.showAsDropDown(my_gold_relative);
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
