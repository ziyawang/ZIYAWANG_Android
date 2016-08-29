package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.LoginActivity;
import com.ziyawang.ziya.activity.ReleaseDetailsActivity;
import com.ziyawang.ziya.tools.ToastUtils;

/**
 * Created by 牛海丰 on 2016/7/19.
 */
public class ReleaseFragment extends Fragment {

    private Button Transfer;
    private Button Withdraw;
    private Button recharge;
    private Button QMoney;
    private Button shop;
    private Button huankuan;
    private Button chuxing;
    private Button daijia;
    private Button huoyun;
    private Button waimai;
    private Button tuangou;
    private Button lvxing;

    private ScrollView scrollView ;

    public ReleaseFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_release, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //实例化组件
        initView(view);

        SharedPreferences sp = getActivity().getSharedPreferences("isLogin", getActivity().MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);

        if (isLogin){
            //资产包转让
            Transfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.shortToast(getActivity(), "----------------资产包转让----------------");

                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "资产包转让");
                    startActivity(intent);
                }
            });
            //债权转让
            Withdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.shortToast(getActivity() , "----------------债权转让----------------");
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "债权转让");
                    startActivity(intent);
                }
            });

            //固产转化
            recharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.shortToast(getActivity() , "------------------固产转化--------------");
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "固产转让");
                    startActivity(intent);
                }
            });
            //商业保理
            QMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ToastUtils.shortToast(getActivity() , "-----------------商业保理---------------");
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "商业保理");
                    startActivity(intent);
                }
            });
            //典当信息
            shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.shortToast(getActivity(), "----------------典当信息----------------");
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "典当信息");
                    startActivity(intent);
                }
            });
            //担保信息
            huankuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.shortToast(getActivity() , "---------------担保信息-----------------");
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "担保信息");
                    startActivity(intent);
                }
            });
            //融资需求
            chuxing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.shortToast(getActivity() , "---------------融资需求-----------------");
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "融资需求");
                    startActivity(intent);
                }
            });
            //悬赏信息
            daijia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.shortToast(getActivity() , "----------------悬赏信息----------------");
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "悬赏信息");
                    startActivity(intent);
                }
            });
            //尽职调查
            huoyun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.shortToast(getActivity() , "----------------尽职调查----------------");
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "尽职调查");
                    startActivity(intent);
                }
            });
            //委外催收
            waimai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.shortToast(getActivity() , "----------------委外催收----------------");
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "委外催收");
                    startActivity(intent);
                }
            });
            //法律服务
            tuangou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.shortToast(getActivity() , "-----------------法律服务---------------");
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "法律服务");
                    startActivity(intent);
                }
            });
            //固产求购
            lvxing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
                    intent.putExtra("title", "资产求购");
                    startActivity(intent);
                }
            });
        }else {
            Transfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            Withdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            recharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            daijia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            chuxing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            huankuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            QMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            lvxing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            tuangou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            waimai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            huoyun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }




    }

    private void initView(View v) {

        Withdraw = (Button) v.findViewById(R.id.Withdraw);
        Transfer = (Button) v.findViewById(R.id.Transfer);
        recharge = (Button) v.findViewById(R.id.recharge);

        QMoney = (Button) v.findViewById(R.id.QMoney);
        shop = (Button) v.findViewById(R.id.shop);
        huankuan = (Button) v.findViewById(R.id.huankuan);

        chuxing = (Button) v.findViewById(R.id.chuxing);
        daijia = (Button) v.findViewById(R.id.daijia);
        huoyun = (Button) v.findViewById(R.id.huoyun);

        waimai = (Button) v.findViewById(R.id.waimai);
        tuangou = (Button) v.findViewById(R.id.tuangou);
        lvxing = (Button) v.findViewById(R.id.lvxing);

        scrollView = (ScrollView)v.findViewById(R.id.scrollView ) ;

    }


    public void aaa(View v) {
        switch (v.getId()) {
            case R.id.Withdraw:
                //ToastUtils.shortToast(getActivity(), "--------------------------------");
                break;
        }
    }
}
