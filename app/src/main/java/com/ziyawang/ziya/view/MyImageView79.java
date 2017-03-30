package com.ziyawang.ziya.view;

/**
 * Created by 牛海丰 on 2017/3/28.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/3/16 0016.
 */

public class MyImageView79 extends ImageView {

    private static int width ;
    private static int height ;

    public MyImageView79(Context context) {
        super(context);
    }

    public MyImageView79(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView79(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获得游戏布局的边长
        width =  MeasureSpec.getSize(widthMeasureSpec);
        height = width / 7 * 10  ;

        setMeasuredDimension(width, height);
    }

}
