package com.ziyawang.ziya.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2015/12/29 0029.
 */

public class BitmapUtils {
    /**
     * 根据图片存放路径，对图片可能进行二次采样，不致于加载过大图片出现内存溢出
     */
    public static Bitmap createImageThumbnail(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
        opts.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    /**
     * 根据图片字节数组，对图片可能进行二次采样，不致于加载过大图片出现内存溢出
     */
    public static Bitmap createImageThumbnail_byte(byte[] data) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
        opts.inJustDecodeBounds = false;
        try {
            BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

//    public static Bitmap getBitmapByBytes(byte[] bytes){
//        //对于图片的二次采样,主要得到图片的宽与高
//        int width = 0;
//        int height = 0;
//        int sampleSize = 1; //默认缩放为1
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;  //仅仅解码边缘区域
//        //如果指定了inJustDecodeBounds，decodeByteArray将返回为空
//        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//        //得到宽与高
//        height = options.outHeight;
//        width = options.outWidth;
//
//        //图片实际的宽与高，根据默认最大大小值，得到图片实际的缩放比例
//        while ((height / sampleSize > Cache.IMAGE_MAX_HEIGHT) || (width / sampleSize > Cache.IMAGE_MAX_WIDTH)) {
//            sampleSize *= 2;
//        }
//
//        //不再只加载图片实际边缘
//        options.inJustDecodeBounds = false;
//        //并且制定缩放比例
//        options.inSampleSize = sampleSize;
//        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
