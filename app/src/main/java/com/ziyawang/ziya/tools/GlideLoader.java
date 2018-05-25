package com.ziyawang.ziya.tools;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaiky.imagespickers.ImageLoader;


/**
 * Created by 牛海丰 on 2017/8/10.
 */

public class GlideLoader implements ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .placeholder(com.jaiky.imagespickers.R.mipmap.imageselector_photo )
                .centerCrop()
                .into(imageView);
    }
}
