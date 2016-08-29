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
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;

/**
 * Created by 牛海丰 on 2016/7/19.
 */

public class InformationFragment extends Fragment {

    private TextView release_head ;

    private RelativeLayout info_sys ;
    private RelativeLayout info_help ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        ConversationListFragment fragment = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        release_head = (TextView)view.findViewById(R.id.release_head ) ;
        info_help = (RelativeLayout)view.findViewById(R.id.info_help) ;
        info_sys = (RelativeLayout)view.findViewById(R.id.info_sys) ;

        SharedPreferences sp = getActivity().getSharedPreferences("isLogin", getActivity().MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);

        if (!isLogin){
            info_help.setVisibility(View.GONE);
            info_sys.setVisibility(View.GONE);
        }
        //资芽小助手
        info_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先需要构造使用客服者的用户信息
                CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
                CSCustomServiceInfo csInfo = csBuilder.nickName("犇犇").build();
                /**
                * 启动客户服聊天界面。
                * @param context           应用上下文。
                * @param customerServiceId 要与之聊天的客服 Id。
                * @param title             聊天的标题，如果传入空值，则默认显示与之聊天的客服名称。
                * @param customServiceInfo 当前使用客服者的用户信息。{@link io.rong.imlib.model.CSCustomServiceInfo}
                */
                //RongIM.getInstance().startCustomerServiceChat(getActivity(), "KEFU147159847156854", "资芽客服", csInfo);
                RongIM.getInstance().startCustomerServiceChat(getActivity(), "KEFU147175411050867", "资芽客服", csInfo);
            }
        });
        //系统消息
        info_sys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity() , SystemInformationActivity.class ) ;
                startActivity(intent);

            }
        });




    }
}
//public class InformationFragment extends Fragment {
//
//    public InformationFragment(){}
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_information, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        /*******************************************融云***************************************************/
//
//        //  18610301342  牛海丰
//        String Token = "ysxnr+lQxBdpEcLAiO+ixUFbtPqxQ3+uKBlZaj5Qs0XrF7dgr9/e3j0KojmodjXxp4Zd2NBK8fQ5NMr6k8ckNkf/qbRFMskM"  ;
//
//        RongIM.connect(Token, new RongIMClient.ConnectCallback() {
//            @Override
//            public void onTokenIncorrect() {
//
//                //Connect Token 失效的状态处理，需要重新获取 Token
//                Log.d("benben", "——————————————————————————————--Token过期————————————————————————————————");
//            }
//
//            @Override
//            public void onSuccess(String userId) {
//
//                Log.e("benben", "—--------------------------------—onSuccess—--------------------------------" + userId);
//
////                //启动会话界面
////                if (RongIM.getInstance() != null)
////                    RongIM.getInstance().startPrivateChat(getActivity(), "18600000000", "犇犇");
//
//                //启动会话列表界面
//                if (RongIM.getInstance() != null)
//                    RongIM.getInstance().startConversationList(getActivity());
//
//
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//
//                Log.e("benben", "——----------------------onError—-------------------------------" + errorCode);
//            }
//        }) ;
//
//        /**********************************************************************************************/
//
//    }
//
//}
