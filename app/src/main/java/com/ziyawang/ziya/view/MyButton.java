package com.ziyawang.ziya.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 牛海丰 on 2016/8/13.
 */
public class MyButton extends ImageView {

    private static int width ;
    private static int height ;

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获得游戏布局的边长
        height =  MeasureSpec.getSize(widthMeasureSpec);
        height = width ;

        setMeasuredDimension(width, height);
    }

}