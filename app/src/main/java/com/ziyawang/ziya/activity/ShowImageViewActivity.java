package com.ziyawang.ziya.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.adapter.HeadpagerAdapter;
import com.ziyawang.ziya.adapter.WelcomeAdapter;
import com.ziyawang.ziya.application.MyApplication;
import com.ziyawang.ziya.tools.SDUtil;
import com.ziyawang.ziya.tools.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ShowImageViewActivity extends Activity {

    private ViewPager viewPager ;
    private MyApplication app;
    private List<ImageView> list ;
    private WelcomeAdapter adapter ;
    private TextView pic_index ;

    private static final String ONE = "1" ;
    private static final String TWO = "2" ;
    private static final String THREE = "3" ;
    private static String index ;

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

        viewPager = (ViewPager)findViewById(R.id.viewpager ) ;
        pic_index = (TextView)findViewById(R.id.pic_index ) ;

        Intent intent = getIntent();
        index = intent.getStringExtra("count");

        showImage(intent) ;

//        imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(ShowImageViewActivity.this, "保存", Toast.LENGTH_SHORT).show();
//                //得到后缀名。
//                final String subStr = getSubStr(url);
//
//                imageView.setDrawingCacheEnabled(true);
//                Bitmap bitmap_icon = Bitmap.createBitmap(imageView.getDrawingCache());
//                imageView.setDrawingCacheEnabled(false);
//                final byte[] icon_data = getByte(bitmap_icon, subStr ) ;
//
//                final long l = System.currentTimeMillis();
//                SDUtil.saveDataInfoSDCard(icon_data, "ziya", l + "." + subStr) ;
//
//                return true ;
//            }
//        });
//
//        viewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                overridePendingTransition(R.anim.head_out , 0 );
//            }
//        });

    }

    private void showImage(Intent intent) {
        String pic_number = intent.getStringExtra("pic_number");
        if (ONE.equals(pic_number)){
            showOneImage(intent) ;
        }else if (TWO.equals(pic_number)){
            showTwoImage(intent);
        }else if (THREE.equals(pic_number)){
            showThreeImage(intent);
        }
    }

    private void showThreeImage(Intent intent){
        String pic1 = intent.getStringExtra("pic1");
        String pic2 = intent.getStringExtra("pic2");
        String pic3 = intent.getStringExtra("pic3");

        list = new ArrayList<ImageView>();
        BitmapUtils bitmapUtil = new BitmapUtils(this);
        bitmapUtil.configDefaultLoadFailedImage(R.mipmap.error_imgs) ;
        ImageView img1 = new ImageView(this);
        bitmapUtil.display(img1, pic1);
        list.add(img1);

        ImageView img2 = new ImageView(this);
        BitmapUtils bitmapUtil02 = new BitmapUtils(this);
        bitmapUtil02.configDefaultLoadFailedImage(R.mipmap.error_imgs ) ;
        //Log.e("benbne" , pic2 ) ;
        bitmapUtil02.display(img2, pic2);
        list.add(img2);

        ImageView img3 = new ImageView(this);
        BitmapUtils bitmapUtil03 = new BitmapUtils(this);
        bitmapUtil03.configDefaultLoadFailedImage(R.mipmap.error_imgs ) ;
        bitmapUtil03.display(img3, pic3);
        list.add(img3);

        loadData(list , THREE) ;
    }


    private void showTwoImage(Intent intent) {
        String pic1 = intent.getStringExtra("pic1");
        String pic2 = intent.getStringExtra("pic2");

        list = new ArrayList<ImageView>();

        BitmapUtils bitmapUitl = new BitmapUtils(this);
        ImageView img1 = new ImageView(this);
        bitmapUitl.display(img1, pic1);
        list.add(img1);

        ImageView img2 = new ImageView(this);
        BitmapUtils bitmapUitl02 = new BitmapUtils(this);
        bitmapUitl02.display(img2, pic2);
        list.add(img2);

        loadData(list , TWO) ;
    }

    private void showOneImage(Intent intent) {
        String pic1 = intent.getStringExtra("pic1");

        list = new ArrayList<ImageView>();

        BitmapUtils bitmapUitl = new BitmapUtils(this);
        ImageView img1 = new ImageView(this);
        bitmapUitl.display(img1, pic1);
        list.add(img1);

        loadData(list , ONE) ;
    }

    private void loadData(List<ImageView> list , final String pic_number) {

        pic_index.setText(index +" / "+ pic_number );
        Log.e("benben" , list.size() + "" ) ;
        //实例化适配器
        adapter = new WelcomeAdapter( this , list );
        //为ViewPager添加适配器
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Integer.parseInt(index)-1);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.head_out , 0 );
                }
            });
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pic_index.setText((position + 1) +" / "+ pic_number );
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int arg0) {}
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
