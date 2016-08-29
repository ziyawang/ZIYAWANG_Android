package com.ziyawang.ziya.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by 牛海丰 on 2016/7/29.
 */
public class NiuLinear extends RelativeLayout {

    private Paint paint ;

    private int width ;
    private int height ;

    public NiuLinear(Context context) {
        super(context);
    }

    public NiuLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public NiuLinear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);   //获取ViewGroup宽度
        height = MeasureSpec.getSize(heightMeasureSpec);  //获取ViewGroup高度
        setMeasuredDimension(width, height);    //设置ViewGroup的宽高

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint = new Paint() ;

        paint.setColor(Color.WHITE);// 设置画笔颜色
        paint.setStyle(Paint.Style.FILL);// 设置画笔填充

        //canvas.drawText("画三角形：", 10, 200, paint);
        // 绘制这个三角形,你可以绘制任意多边形
        Path path = new Path();
        //paint.setStyle(Paint.Style.STROKE);//设置空心
        path.moveTo(width-120, 0);// 此点为多边形的起点
        path.lineTo(width, 0);
        path.lineTo(width, 120);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);
    }
}
