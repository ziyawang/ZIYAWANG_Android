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
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;

/**
 * Created by 牛海丰 on 2016/7/19.
 */
public class ReleaseFragment extends Fragment implements View.OnClickListener {

    //资产包转让按钮
    private Button Transfer;
    //债权转让按钮
    private Button Withdraw;
    //固产转让按钮
    private Button recharge;
    //商业保理按钮
    private Button QMoney;
    //典当信息按钮
    private Button shop;
    //担保信息按钮
    private Button huankuan;
    //融资需求按钮
    private Button chuxing;
    //悬赏信息按钮
    private Button daijia;
    //尽职调查按钮
    private Button huoyun;
    //委外催收按钮
    private Button waimai;
    //法律服务按钮
    private Button tuangou;
    //资产求购按钮
    private Button lvxing;
    //投资需求按钮
    private Button xu ;
    //用户缓存的isLogin的状态
    private boolean isLogin ;
    //无参构造
    public ReleaseFragment() {}

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
        //拿到isLogin的状态
        initData()  ;
        //注册监听事件
        initListener() ;
    }

    private void initListener() {
        Withdraw.setOnClickListener(this);
        Transfer.setOnClickListener(this);
        recharge.setOnClickListener(this);
        QMoney.setOnClickListener(this);
        shop.setOnClickListener(this);
        huankuan.setOnClickListener(this);
        chuxing.setOnClickListener(this);
        daijia.setOnClickListener(this);
        huoyun.setOnClickListener(this);
        waimai.setOnClickListener(this);
        tuangou.setOnClickListener(this);
        lvxing.setOnClickListener(this);
        xu.setOnClickListener(this);
    }

    private void initData() {
        isLogin = GetBenSharedPreferences.getIsLogin(getActivity());
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
        xu = (Button) v.findViewById(R.id.xv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Withdraw :
                judgeWithdraw() ;
                break;
            case R.id.Transfer :
                judgeTransfer() ;
                break;
            case R.id.recharge :
                judgerecharge() ;
                break;
            case R.id.QMoney :
                judgeQMoney() ;
                break;
            case R.id.shop :
                judgeshop() ;
                break;
            case R.id.huankuan :
                judgehuankuan() ;
                break;
            case R.id.chuxing :
                judgechuxing() ;
                break;
            case R.id.daijia :
                judgedaijia() ;
                break;
            case R.id.huoyun :
                judgehuoyun() ;
                break;
            case R.id.waimai :
                judgewaimai() ;
                break;
            case R.id.tuangou :
                judgetuangou() ;
                break;
            case R.id.lvxing :
                judgelvxing() ;
                break;
            case R.id.xv :
                judgexv() ;
                break;
            default:
                break;

        }
    }

    private void judgexv() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "投资需求");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgelvxing() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "资产求购");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgetuangou() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "法律服务");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgewaimai() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "委外催收");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgehuoyun() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "尽职调查");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgedaijia() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "悬赏信息");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgechuxing() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "融资需求");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgehuankuan() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "担保信息");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgeshop() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "典当信息");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgeQMoney() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "商业保理");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgerecharge() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "固产转让");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgeTransfer() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "资产包转让");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void judgeWithdraw() {
        if (isLogin){
            Intent intent = new Intent(getActivity(), ReleaseDetailsActivity.class);
            intent.putExtra("title", "债权转让");
            startActivity(intent);
        }else {
            goLoginActivity() ;
        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

}
