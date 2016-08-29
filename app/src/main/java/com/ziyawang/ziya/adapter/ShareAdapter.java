package com.ziyawang.ziya.adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;


/**
 * Created by 牛海丰 on 2016/7/21.
 */
public class ShareAdapter extends AuthorizeAdapter{


    public void onCreate() {


        //隐藏标题栏的 “Powered by ShareSDK”
        hideShareSDKLogo();


        //页面弹出时动画的修改
        disablePopUpAnimation();
        View rv = (View) getBodyView().getParent();
        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(1000);//动画时间
        rv.setAnimation(ta);

    }



}
