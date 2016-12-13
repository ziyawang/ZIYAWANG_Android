package com.ziyawang.ziya.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Button;

import com.ziyawang.ziya.R;

/**
 * Created by Administrator on 2016/2/20.
 */
public class NotificationButton extends Button {
    private int notificationNumber;
    private float redCircleSize = 5;
    private Paint paint;
    private RectF rectF;
    private boolean isChange;
    private int textColor, circleColor;

    public NotificationButton(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public NotificationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProperty(context, attrs);
    }

    public NotificationButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProperty(context, attrs);
    }

    private void initProperty(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.notButton);
        redCircleSize = typedArray.getDimension(R.styleable.notButton_circleSize, 5);
        circleColor = typedArray.getColor(R.styleable.notButton_circleBgColor, Color.RED);
        textColor = typedArray.getColor(R.styleable.notButton_textColor, Color.WHITE);
        paint = new Paint();
        paint.setAntiAlias(true);
        typedArray.recycle();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        //canvas.scale((getWidth() - redCircleSize) / (float) getWidth(), (getHeight() - redCircleSize) / (float) getHeight());
        //canvas.scale(getWidth() , getHeight());
        //canvas.translate(0, redCircleSize);
        super.draw(canvas);
        canvas.restore();
        drawRedCircle(canvas);
    }

    private void drawRedCircle(Canvas canvas) {
        if (notificationNumber <= 0) return;
        paint.setColor(circleColor);
        if (rectF == null || isChange) {
            if (notificationNumber < 10) {
                rectF = new RectF(getWidth() - redCircleSize * 2 - 20 , 0, getWidth() - 20 , redCircleSize * 2);
            } else if (notificationNumber < 100) {
                rectF = new RectF(getWidth() - redCircleSize * 3 - 30 , 0, getWidth() - 30 , redCircleSize * 2);
            } else {
                rectF = new RectF(getWidth() - redCircleSize * 4 - 40 , 0, getWidth() - 40, redCircleSize * 2);
            }
        }
        canvas.drawRoundRect(rectF, redCircleSize, redCircleSize, paint);
        paint.setColor(textColor);
        paint.setTextSize(redCircleSize * 3 / 2);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (int) ((rectF.bottom + rectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
        if (notificationNumber < 100) {
            canvas.drawText(String.valueOf(notificationNumber), getWidth() - rectF.width() - 1 / 2 + 1, baseline - 2, paint);
        } else {
            canvas.drawText("99+", getWidth() - rectF.width() - 5 / 2 + 1, baseline - 2, paint);
        }
    }

    public void setNotificationNumber(int notificationNumber) {
        if (notificationNumber != this.notificationNumber) {
            isChange = true;
        }
        this.notificationNumber = notificationNumber;
        invalidate();
    }
}
