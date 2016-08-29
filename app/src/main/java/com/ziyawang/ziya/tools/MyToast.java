package com.ziyawang.ziya.tools;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ziyawang.ziya.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by 牛海丰 on 2016/07/19 0029.
 */
public class MyToast {

    public static void getToast(Context context){
        //自定义Toast
        Toast t = new Toast(context);
        TextView textView = new TextView(context);
        TextView textView2 = new TextView(context);
        textView.setText("    再按一次退出资芽网    \n");
        textView2.setText("");

        ImageView imageView = new ImageView(context);


        //DateFormat df1 = DateFormat.getDateInstance();
        //final Date date = new Date();
        //df1.format(date);
        //final int date_new = date.getDate();

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));


//        switch (mWay){
//            case "1" :
//                imageView.setImageResource(R.mipmap.ic_launcher);
//                break;
//            case "2" :
//                imageView.setImageResource(R.mipmap.ic_launcher);
//                break;
//            case "3" :
//                imageView.setImageResource(R.mipmap.ic_launcher);
//                break;
//            case "4" :
//                imageView.setImageResource(R.mipmap.ic_launcher);
//                break;
//            case "5" :
//                imageView.setImageResource(R.mipmap.ic_launcher);
//                break;
//            case "6" :
//                imageView.setImageResource(R.mipmap.ic_launcher);
//                break;
//            case "7" :
//                imageView.setImageResource(R.mipmap.ic_launcher);
//                break;
//        }

        LinearLayout linearLayout = new LinearLayout(context);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(textView2);
        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        linearLayout.setBackgroundResource(R.drawable.btn_cancel);

            t.setView(linearLayout);

            t.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 250);

            t.setDuration(Toast.LENGTH_SHORT);

            t.show();


    }
}
