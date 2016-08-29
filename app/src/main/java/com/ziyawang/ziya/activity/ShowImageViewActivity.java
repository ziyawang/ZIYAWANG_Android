package com.ziyawang.ziya.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.application.MyApplication;
import com.ziyawang.ziya.tools.SDUtil;

import java.io.ByteArrayOutputStream;

public class ShowImageViewActivity extends Activity {


    private ImageView imageView ;
    private MyApplication app;

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("查看图片页面");
        //统计时长
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("查看图片页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_view);

        app = (MyApplication) getApplication();
        this.getApplication() ;
        app.addActivity(this);

        imageView = (ImageView)findViewById(R.id.imageView ) ;
        Intent intent = getIntent();
        final String url = intent.getStringExtra("url");
        BitmapUtils bitmapUtils = new BitmapUtils(ShowImageViewActivity.this) ;
        bitmapUtils.display(imageView , url );

        Log.e("benben" , url ) ;

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ShowImageViewActivity.this, "保存", Toast.LENGTH_SHORT).show();
                //得到后缀名。
                final String subStr = getSubStr(url);

                imageView.setDrawingCacheEnabled(true);
                Bitmap bitmap_icon = Bitmap.createBitmap(imageView.getDrawingCache());
                imageView.setDrawingCacheEnabled(false);
                final byte[] icon_data = getByte(bitmap_icon, subStr ) ;

                final long l = System.currentTimeMillis();
                SDUtil.saveDataInfoSDCard(icon_data, "ziya", l + "." + subStr) ;

                return true ;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.head_out , 0 );
            }
        });

    }

    //得到图片的类型
    public String getSubStr(String str){
        int position = str.lastIndexOf(".");
        str = str.substring(position + 1);
        return str;
    }


    //将bitmap转化为byte[]
    public byte[] getByte(Bitmap bit , String str){
        Bitmap.CompressFormat temp = null;
        if ("jpg".equals(str)){
            temp = Bitmap.CompressFormat.JPEG;
        }else if ("png".equals(str)){
            temp = Bitmap.CompressFormat.PNG;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(temp, 80, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return bytes;
    }




}
