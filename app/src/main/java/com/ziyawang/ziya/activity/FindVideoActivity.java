package com.ziyawang.ziya.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.application.MyApplication;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.fragment.MovieOneFragment;
import com.ziyawang.ziya.fragment.MovieThreeFragment;
import com.ziyawang.ziya.fragment.MovieTwoFragment;
import com.ziyawang.ziya.tools.SystemBarTintManager;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.videotitle.BaseTools;
import com.ziyawang.ziya.videotitle.ColumnHorizontalScrollView;
import com.ziyawang.ziya.videotitle.Constants;
import com.ziyawang.ziya.videotitle.NewsClassify;
import com.ziyawang.ziya.videotitle.NewsFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FindVideoActivity extends FragmentActivity implements View.OnClickListener{

    private SharedPreferences spVideoID ;
    private RelativeLayout search_video ;

    private MyApplication app;
    /** 自定义HorizontalScrollView */
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private ColumnHorizontalScrollView mColumnHorizontalScrollView01;
    LinearLayout mRadioGroup_content;
    LinearLayout mRadioGroup_content01;
    LinearLayout ll_more_columns;
    LinearLayout ll_more_columns01;
    RelativeLayout rl_column;
    RelativeLayout rl_column01;
    RelativeLayout pre ;
    private ViewPager mViewPager;
    private ImageView button_more_columns;
    /** 新闻分类列表*/
    private ArrayList<NewsClassify> newsClassify=new ArrayList<NewsClassify>();
    /** 当前选中的栏目*/
    private int columnSelectIndex = 0;
    /** 左阴影部分*/
    public ImageView shade_left;
    public ImageView shade_left01;
    /** 右阴影部分 */
    public ImageView shade_right;
    public ImageView shade_right01;
    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /** Item宽度 */
    private int mItemWidth = 0;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_video);

        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.aaa);//通知栏所需颜色

        mScreenWidth = BaseTools.getWindowsWidth(this);
        mItemWidth = mScreenWidth / 4;// 一个Item宽度为屏幕的1/4
        //实例化组件
        initView();
        //注册监听事件
        initListeners() ;
        //加载最新的视频ID
        LoadVideoID() ;

    }

    private void initListeners() {
        search_video.setOnClickListener(this);
    }

    private void LoadVideoID() {
        HttpUtils httpUtils = new HttpUtils()  ;
        RequestParams params = new RequestParams() ;

        params.addQueryStringParameter("pagecount" , "1");
        httpUtils.configCurrentHttpCacheExpiry(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, Url.GetMovie, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject jsonObj = JSON.parseObject(responseInfo.result);
                JSONArray result = jsonObj.getJSONArray("data");
                List<FindVideoEntity> list = JSON.parseArray(result.toJSONString(), FindVideoEntity.class);
                if (list.size() == 1) {
                    String videoID = list.get(0).getVideoID();
                    //拿到用户缓存的spVideoID的值,用户看过的最新的VideoID
                    spVideoID = getSharedPreferences("VideoID", MODE_PRIVATE);
                    spVideoID.edit().putString("VideoID", videoID).commit();

                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();

            }
        }) ;
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }



    /**
     * 初始化layout控件
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void initView() {

        search_video = (RelativeLayout)findViewById(R.id.search_video ) ;

        mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
        mColumnHorizontalScrollView01 =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView01);

        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        mRadioGroup_content01 = (LinearLayout) findViewById(R.id.mRadioGroup_content01);

        ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
        ll_more_columns01 = (LinearLayout) findViewById(R.id.ll_more_columns01);

        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        rl_column01 = (RelativeLayout) findViewById(R.id.rl_column01);

        pre = (RelativeLayout) findViewById(R.id.pre);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_left01 = (ImageView) findViewById(R.id.shade_left01);

        shade_right = (ImageView) findViewById(R.id.shade_right);
        shade_right01 = (ImageView) findViewById(R.id.shade_right01);

        button_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(FindVideoActivity.this  , MovieListActivity.class ) ;
                intent.putExtra("title" , "最新发布") ;
                startActivity(intent);
            }
        });
        setChangelView();
    }
    /**
     *  当栏目项发生变化时候调用
     **/
    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }
    /** 获取Column栏目 数据*/
    private void initColumnData() {
        newsClassify = Constants.getData();
    }

    /**
     *  初始化Column栏目项
     * */
    private void initTabColumn() {

        mRadioGroup_content.removeAllViews();
        mRadioGroup_content01.removeAllViews();

        int count =  newsClassify.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        mColumnHorizontalScrollView01.setParam(this, mScreenWidth, mRadioGroup_content01, shade_left01, shade_right01, ll_more_columns01, rl_column01);

        for(int i = 0; i< count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;

            TextView localTextView = new TextView(this);
            localTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
            //localTextView.setBackgroundResource(R.drawable.radio_button_bg);
            localTextView.setGravity(Gravity.CENTER);
            localTextView.setPadding(5, 0, 5, 0);
            localTextView.setId(i);
            localTextView.setText(newsClassify.get(i).getTitle());
            localTextView.setTextColor(getResources().getColorStateList(R.color.aaa));
            if(columnSelectIndex == i){
                localTextView.setSelected(true);
            }

            localTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else{
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                    //Toast.makeText(getApplicationContext(), newsClassify.get(v.getId()).getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(localTextView, i, params);
            //mRadioGroup_content01.addView(localTextView, i ,params);
        }

        for(int i = 0; i< count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , ViewGroup.LayoutParams.MATCH_PARENT);
            params.leftMargin = 10;
            params.rightMargin = 10;

            TextView localTextView = new TextView(this);
            localTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
            localTextView.setBackgroundResource(R.drawable.radio_button_bg);
            localTextView.setGravity(Gravity.CENTER);
            localTextView.setPadding(5, 0, 5, 0);
            localTextView.setId(i);
            localTextView.setText(newsClassify.get(i).getTitle());
            localTextView.setTextColor(getResources().getColorStateList(R.color.aaa));
            if(columnSelectIndex == i){
                localTextView.setSelected(true);
            }

            localTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else{
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                    //Toast.makeText(getApplicationContext(), newsClassify.get(v.getId()).getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
            //mRadioGroup_content.addView(localTextView, i, params);
            mRadioGroup_content01.addView(localTextView, i ,params);
        }

    }
    /**
     *  选择的Column里面的Tab
     * */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }
        for (int i = 0; i < mRadioGroup_content01.getChildCount(); i++) {
            View checkView01 = mRadioGroup_content01.getChildAt(tab_postion);
            int k = checkView01.getMeasuredWidth();
            int l = checkView01.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mColumnHorizontalScrollView01.smoothScrollTo(i2, 0);
        }
        //判断是否选中
        for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
        for (int j = 0; j <  mRadioGroup_content01.getChildCount(); j++) {
            View checkView01 = mRadioGroup_content01.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView01.setSelected(ischeck);
        }
    }
    /**
     *  初始化Fragment
     * */
    private void initFragment() {
        int count =  newsClassify.size();
        for(int i = 0; i< count;i++){
            Bundle data = new Bundle();
            data.putString("text", newsClassify.get(i).getTitle());
        }

        MovieThreeFragment threeFragment = new MovieThreeFragment() ;

        Bundle data01 = new Bundle();
        data01.putString("title", "资芽一分钟");
        MovieTwoFragment twoFragment01 = new MovieTwoFragment() ;
        twoFragment01.setArguments(data01);

        Bundle data02 = new Bundle();
        data02.putString("title", "行业说");
        MovieTwoFragment twoFragment02 = new MovieTwoFragment() ;
        twoFragment02.setArguments(data02);

        Bundle data03 = new Bundle();
        data03.putString("title", "资芽哈哈哈");
        MovieTwoFragment twoFragment03 = new MovieTwoFragment() ;
        twoFragment03.setArguments(data03);

//        Bundle data04 = new Bundle();
//        data04.putString("title", "资芽一分钟");
//        MovieTwoFragment twoFragment04 = new MovieTwoFragment() ;
//        twoFragment04.setArguments(data04);


//        MovieTwoFragment twoFragment = new MovieTwoFragment() ;
//        //MovieThreeFragment threeFragment = new MovieThreeFragment() ;
//        Bundle data1 = new Bundle();
//        data1.putString("title", " 行业说");
//        MovieTwoFragment twoFragment = new MovieTwoFragment() ;
//        //MovieFourFragment fourFragment = new MovieFourFragment() ;
//        Bundle data2 = new Bundle();
//        data2.putString("title", " 大咖秀");
//        MovieTwoFragment twoFragment = new MovieTwoFragment() ;
//        //MovieFiveFragment fiveFragment = new MovieFiveFragment() ;
//        Bundle data3 = new Bundle();
//        data3.putString("title", " 资芽一分钟");

        //fragments.add(oneFragment) ;
        fragments.add(threeFragment) ;
        fragments.add(twoFragment01) ;
        fragments.add(twoFragment02) ;
        fragments.add(twoFragment03) ;
        //fragments.add(twoFragment04) ;


        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }
    /**
     *  ViewPager切换监听方法
     * */
    public ViewPager.OnPageChangeListener pageListener= new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_video :
                goSearchVideoActivity() ;
                break;
            default:
                break;
        }
    }

    private void goSearchVideoActivity() {
        Intent intent = new Intent(FindVideoActivity.this, SearchVideoActivity.class);
        startActivity(intent);
    }
}

