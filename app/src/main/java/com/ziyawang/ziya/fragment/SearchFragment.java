package com.ziyawang.ziya.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.FindInfoActivity;
import com.ziyawang.ziya.activity.FindServiceActivity;
import com.ziyawang.ziya.activity.FindVideoActivity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.view.NiuLinear;

/**
 * Created by 牛海丰 on 2016/7/29.
 */
public class SearchFragment extends Fragment {

    private RelativeLayout search_relative01 , search_relative02 , search_relative03 ;
    private TextView niu_111 , niu_113 , niu_112 ;


    public SearchFragment(){}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //实例化组件
        initView(view) ;



        search_relative01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity() , FindInfoActivity.class ) ;
                startActivity(intent);

            }
        });
        search_relative02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity() , FindServiceActivity.class ) ;
                startActivity(intent);
            }
        });
        search_relative03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity() , FindVideoActivity.class ) ;
                startActivity(intent);
            }
        });


    }

    private void initView(View view) {

        search_relative01 = (RelativeLayout)view.findViewById(R.id.search_relative01) ;
        search_relative02 = (RelativeLayout)view.findViewById(R.id.search_relative02) ;
        search_relative03 = (RelativeLayout)view.findViewById(R.id.search_relative03) ;
        niu_111 = (TextView)view.findViewById(R.id.niu_111) ;
        niu_113 = (TextView)view.findViewById(R.id.niu_113) ;
        niu_112 = (TextView)view.findViewById(R.id.niu_112) ;

        TextPaint tp = niu_111.getPaint();
        tp.setFakeBoldText(true);
        TextPaint tp1 = niu_113.getPaint();
        tp1.setFakeBoldText(true);
        TextPaint tp2 = niu_112.getPaint();
        tp2.setFakeBoldText(true);

    }

}