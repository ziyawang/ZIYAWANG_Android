package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.SystemInformationActivity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;

/**
 * Created by 牛海丰 on 2016/7/19.
 */

public class InformationFragment extends Fragment implements View.OnClickListener{

    //系统消息的按钮
    private RelativeLayout info_sys ;
    //资芽小助手的按钮
    private RelativeLayout info_help ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        //在frameLayout中添加会话列表
        loadConversationList() ;
        return view;
    }

    private void loadConversationList() {
        ConversationListFragment fragment = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.conversationlist);
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//设置系统会话非聚合显示
                .build();
        fragment.setUri(uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实例化组件
        initView(view) ;
        //得到sp中暂存的isLogin Status
        boolean isLogin = GetBenSharedPreferences.getIsLogin(getActivity()) ;
        //未登录状态下，资芽小助手和系统消息消失
        initData(isLogin) ;
        //注册监听事件
        initListeners() ;
    }

    private void initListeners() {
        info_help.setOnClickListener(this);
        info_sys.setOnClickListener(this);
    }

    private void initData(boolean isLogin) {
        if (!isLogin){
            info_help.setVisibility(View.GONE);
            info_sys.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
        info_help = (RelativeLayout)view.findViewById(R.id.info_help) ;
        info_sys = (RelativeLayout)view.findViewById(R.id.info_sys) ;
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
            default:
                break;
        }
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
        RongIM.getInstance().startCustomerServiceChat(getActivity(), "KEFU147175411050867", "资芽客服", csInfo);
    }

    private void goSystemInformationActivity() {
        Intent intent = new Intent(getActivity(), SystemInformationActivity.class);
        startActivity(intent);
    }

}
