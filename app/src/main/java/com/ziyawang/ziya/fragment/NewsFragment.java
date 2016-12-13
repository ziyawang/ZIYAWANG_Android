package com.ziyawang.ziya.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.videotitle.NewsFragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by 牛海丰 on 2016/10/31.
 */
public class NewsFragment extends Fragment {

    private TabLayout tabs ;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    //无参构造
    public NewsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实例化组件
        initView(view);
    }

    private void initView(View v ) {
        tabs = (TabLayout)v.findViewById(R.id.tabs ) ;
        mViewPager = (ViewPager) v.findViewById(R.id.mViewPager);
        tabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式

        initFragment() ;
    }

    /**
     *  初始化Fragment
     * */
    private void initFragment() {

        Bundle data01 = new Bundle();
        data01.putString("title", "hyzx");
        NewsItemsFragment newsItemsFragment01 = new NewsItemsFragment() ;
        newsItemsFragment01.setArguments(data01);

        Bundle data02 = new Bundle();
        data02.putString("title", "zyjt");
        NewsItemsFragment newsItemsFragment02 = new NewsItemsFragment() ;
        newsItemsFragment02.setArguments(data02);

        Bundle data03 = new Bundle();
        data03.putString("title", "hyyj");
        NewsItemsFragment newsItemsFragment03 = new NewsItemsFragment() ;
        newsItemsFragment03.setArguments(data03);

        fragments.add(newsItemsFragment01) ;
        fragments.add(newsItemsFragment02) ;
        fragments.add(newsItemsFragment03) ;

        final NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        tabs.setupWithViewPager(mViewPager);
    }

}
