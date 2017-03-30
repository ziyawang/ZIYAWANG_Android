package com.ziyawang.ziya.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.CirclePageIndicator;
import com.ziyawang.ziya.tools.HackyViewPager;
import com.ziyawang.ziya.tools.PageIndicator;
import com.ziyawang.ziya.tools.ToastUtils;

import io.rong.imageloader.cache.disc.naming.Md5FileNameGenerator;
import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imageloader.core.ImageLoaderConfiguration;
import io.rong.imageloader.core.assist.FailReason;
import io.rong.imageloader.core.assist.ImageScaleType;
import io.rong.imageloader.core.assist.QueueProcessingType;
import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imageloader.core.listener.SimpleImageLoadingListener;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class Image216Activity extends Activity {

    //实例化ImageLoader
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    HackyViewPager pager;
    PageIndicator mIndicator;

    private static final String ONE = "1" ;
    private static final String TWO = "2" ;
    private static final String THREE = "3" ;
    private String[] imageUrls ;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image216);
        //初始化ImageLoader
        initImageLoader(this);

        Intent intent = getIntent();
        String index = intent.getStringExtra("count");
        String pic_number = intent.getStringExtra("pic_number");

        if (ONE.equals(pic_number)){
            imageUrls = new String[]{intent.getStringExtra("pic1") } ;
        }else if (TWO.equals(pic_number)){
            imageUrls = new String[]{intent.getStringExtra("pic1") , intent.getStringExtra("pic2") } ;
        }else if (THREE.equals(pic_number)){
            imageUrls = new String[]{intent.getStringExtra("pic1") , intent.getStringExtra("pic2") , intent.getStringExtra("pic3") } ;
        }

        //设置ImageLoader加载图片的属性
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        //使用的是HackViewPager,目的就是为了防止滑动的时候抛异常
        pager = (HackyViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ImagePagerAdapter(imageUrls));


        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        //指针绑定ViewPager
        mIndicator.setViewPager(pager);
        mIndicator.setCurrentItem((Integer.parseInt(index)-1));
    }

    public void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        imageLoader.init(config);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private String[] images;
        private LayoutInflater inflater;


        ImagePagerAdapter(String[] images) {
            this.images = images;
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);

            PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

            imageLoader.displayImage(images[position], imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    ToastUtils.shortToast(Image216Activity.this , message );
                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });
            (view).addView(imageLayout, 0);
            imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    finish();
                }
            });
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}