package com.ziyawang.ziya.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 牛海丰 on 2016/8/4.
 */
public class MyMovieImageView extends ImageView {

    private static int width ;
    private static int height ;

    public MyMovieImageView(Context context) {
        super(context);
    }

    public MyMovieImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMovieImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 布局的边长
        width =  MeasureSpec.getSize(widthMeasureSpec);
        height = width /7 * 4  ;

        setMeasuredDimension(width, height);
    }

}
