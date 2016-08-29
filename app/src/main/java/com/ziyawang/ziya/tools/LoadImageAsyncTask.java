package com.ziyawang.ziya.tools;

/**
 * Created by 牛海丰 on 2016/07/19 0029.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


public class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private CallBack callBack;
    private String url;

    private Bitmap bitmap ;

    public LoadImageAsyncTask(CallBack callBack) {
        super();
        this.callBack = callBack;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params != null) {
            url = params[0];
            byte[] temp = HttpUtils.getdata(url);
            if (temp != null) {

                bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                return bitmap;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (result != null) {
            callBack.setData(result);
        }
    }


    public interface CallBack {
        public void setData(Bitmap bitmap);
    }



}

