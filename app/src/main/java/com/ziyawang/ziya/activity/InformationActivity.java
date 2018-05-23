package com.ziyawang.ziya.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.application.MyApplication;
import com.ziyawang.ziya.entity.SystemEntity;
import com.ziyawang.ziya.tools.ChangeNotifyColor;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.Json_System;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;

public class InformationActivity extends FragmentActivity implements View.OnClickListener {

    //声明MyApplication变量
    private MyApplication app;
    //系统消息的按钮
    private RelativeLayout info_sys ;
    //资芽小助手的按钮
    private RelativeLayout info_help ;
    //回退按钮
    private RelativeLayout pre ;

    private SharedPreferences spTextID ;
    private ImageView img_red_point02  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 定制流程
        setContentView();
        initViews();
        initListeners();
        initData();
        //获得activity的Task路径
        getActivityTask() ;
        //改变通知栏的颜色
        ChangeNotifyColor.change(R.color.aaa, this);

        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //rong_content 为你要加载的 id
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadVideoID() ;
    }

    private void LoadVideoID() {
        SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        String login = loginCode.getString("loginCode", null);
        String urls = String.format(Url.GetMessage, login ) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("pagecount" , "1" );
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben", responseInfo.result) ;
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result) ;
                    String status_code = jsonObject.getString("status_code");
                    switch (status_code){
                        case "200" :
                            List<SystemEntity> list = Json_System.getParse(responseInfo.result);
                            if (list.size() != 0){
                                String textID = list.get(0).getTextID();

                                spTextID = getSharedPreferences("TextID", MODE_PRIVATE);
                                String spVideoIDString = spTextID.getString("TextID", "");
                                if (!spVideoIDString.equals(textID)) {
                                    img_red_point02.setVisibility(View.VISIBLE);
                                }else {
                                    img_red_point02.setVisibility(View.GONE);
                                }
                            }else {
                                img_red_point02.setVisibility(View.GONE);
                            }
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(InformationActivity.this, "网络连接异常");
            }
        }) ;

    }

    private void getActivityTask() {
        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);
    }

    public void setContentView() {
        setContentView(R.layout.activity_information);
    }

    public void initViews() {
        info_help = (RelativeLayout)findViewById(R.id.info_help) ;
        info_sys = (RelativeLayout)findViewById(R.id.info_sys) ;
        pre = (RelativeLayout)findViewById(R.id.pre) ;
        img_red_point02 = (ImageView) findViewById(R.id.img_red_point02) ;
    }

    public void initListeners() {
        info_help.setOnClickListener(this);
        info_sys.setOnClickListener(this);
        pre.setOnClickListener(this);
    }

    public void initData() {
        //在frameLayout中添加会话列表
        //loadConversationList() ;
        //得到sp中暂存的isLogin Status
        boolean isLogin = GetBenSharedPreferences.getIsLogin(this) ;
        if (!isLogin){
            info_help.setVisibility(View.GONE);
            info_sys.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //资芽小助手
            case R.id.info_help :
                goCustomServiceChat() ;
                break;
            //系统消息
            case R.id.info_sys :
                goSystemInformationActivity() ;
                break;
            //系统消息
            case R.id.pre :
                finish();
                break;
            default:
                break;
        }
    }

    private void loadConversationList() {
        //if(RongIM.getInstance() != null)
            //RongIM.getInstance().startConversationList(InformationActivity.this);
    }

    private void goCustomServiceChat() {
        //首先需要构造使用客服者的用户信息
        CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
        CSCustomServiceInfo csInfo = csBuilder.nickName("犇犇").build();
        /**
         * 启动客户服聊天界面。
         * @param context           静态上下文。
         * @param customerServiceId 要与之聊天的客服Id。
         * @param title             聊天的标题，如果传入空值，则默认显示与之聊天的客服名称。
         * @param customServiceInfo 当前使用客服者的用户信息。{@link io.rong.imlib.model.CSCustomServiceInfo}
         */
        //测试客服
        //RongIM.getInstance().startCustomerServiceChat(getActivity(), "KEFU147159847156854", "资芽客服", csInfo);
        //正式客服
        RongIM.getInstance().startCustomerServiceChat(this , "KEFU147175411050867", "资芽客服", csInfo);
    }

    private void goSystemInformationActivity() {
        Intent intent = new Intent(this , SystemInformationActivity.class);
        startActivity(intent);
    }

}
