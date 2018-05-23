package com.ziyawang.ziya.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.HomeFragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by 牛海丰 on 2018/5/18.
 */

public class V3HomePageFragment extends Fragment{

    private TabLayout tabs ;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    public V3HomePageFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_v3_homepage, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实例化组件
        initView(view) ;
    }

    private void initView(View v) {
        tabs = (TabLayout)v.findViewById(R.id.tabs ) ;
        mViewPager = (ViewPager) v.findViewById(R.id.mViewPager);
        //设置tab模式，当前为系统默认模式
        tabs.setTabMode(TabLayout.MODE_FIXED);
        //初始化Fragment
        initFragment() ;
    }

    private void initFragment() {
        V3HomeNewsFragment releaseFragment01 = new V3HomeNewsFragment() ;
        V3HomeFastFragment releaseFragment02= new V3HomeFastFragment() ;
        MovieThreeFragment movieThreeFragment = new MovieThreeFragment() ;
        V3HomeDictionaryFragment releaseFragment04  = new V3HomeDictionaryFragment() ;
        V3HomeAskFragment releaseFragment05 = new V3HomeAskFragment() ;

        fragments.add(releaseFragment01) ;
        fragments.add(releaseFragment02) ;
        fragments.add(movieThreeFragment) ;
        fragments.add(releaseFragment04) ;
        fragments.add(releaseFragment05) ;

        final HomeFragmentPagerAdapter mAdapetr = new HomeFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        tabs.setupWithViewPager(mViewPager);

    }

}
