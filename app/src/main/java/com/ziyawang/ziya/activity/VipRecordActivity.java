package com.ziyawang.ziya.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.VipRecordAdapter;
import com.ziyawang.ziya.entity.VipRecordEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import java.util.List;

public class VipRecordActivity extends BenBenActivity implements View.OnClickListener {
    //返回按钮
    private RelativeLayout pre ;
    private ListView listView ;
    private TextView niuniuniuniu ;
    private VipRecordAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData() ;
    }

    private void loadData() {
        String urls = String.format(Url.V202VipRecord, GetBenSharedPreferences.getTicket(this)) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("V202VipRecord" , responseInfo.result ) ;
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(responseInfo.result);
                com.alibaba.fastjson.JSONArray data01 = object.getJSONArray("data");
                List<VipRecordEntity> vipRecordEntities = JSON.parseArray(data01.toJSONString(), VipRecordEntity.class);
                if (vipRecordEntities.size()==0){
                    listView.setVisibility(View.GONE);
                    niuniuniuniu.setVisibility(View.VISIBLE);
                }else {
                    listView.setVisibility(View.VISIBLE);
                    niuniuniuniu.setVisibility(View.GONE);
                    adapter = new VipRecordAdapter(VipRecordActivity.this , vipRecordEntities ) ;
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(VipRecordActivity.this , "网络连接异常");
            }
        }) ;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_vip_record);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        listView = (ListView)findViewById(R.id.listView ) ;
        niuniuniuniu = (TextView)findViewById(R.id.niuniuniuniu ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this );
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
            default:
                break;
        }
    }
}
