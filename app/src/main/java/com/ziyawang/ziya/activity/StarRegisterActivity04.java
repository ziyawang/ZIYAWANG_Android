package com.ziyawang.ziya.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.DownLoadDocManager;
import com.ziyawang.ziya.tools.DownLoadManager;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.SDUtil;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

public class StarRegisterActivity04 extends BenBenActivity implements View.OnClickListener {
    //返回按钮
    private RelativeLayout pre ;
    //下载承诺书
    private Button download ;
    //上传承诺书
    private Button upload ;
    //需要上传到后台的图片文件路径
    private File file;
    //需要发送的图片的文件
    private File uploadfile ;
    //拿到图片文件的后缀名
    private String imgstr;
    private FrameLayout star_frame ;
    private ImageView star_imageVIew ;
    private ImageView star_img_cancel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_start_register04);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        download = (Button)findViewById(R.id.download ) ;
        upload = (Button)findViewById(R.id.upload ) ;
        star_frame = (FrameLayout)findViewById(R.id.star_frame ) ;
        star_imageVIew = (ImageView)findViewById(R.id.star_imageVIew ) ;
        star_img_cancel = (ImageView)findViewById(R.id.star_img_cancel ) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this );
        download.setOnClickListener(this );
        upload.setOnClickListener(this );
        star_img_cancel.setOnClickListener(this );
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre :
                finish();
                break;
            case R.id.download :
                    download() ;
                break;
            case R.id.upload :
                if ("上传承诺书".equals(upload.getText())){
                    iconGet();
                }else {
                    //ToastUtils.shortToast(StarRegisterActivity04.this , "点击上传");
                    submit() ;
                }
                break;
            //删除图片的按钮
            case R.id.star_img_cancel :
                goDelPic() ;
                break;
            default:
                break;
        }
    }

    private void submit() {
        String urls = String.format(Url.V203StarRegister04, GetBenSharedPreferences.getTicket(this ) ) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("StarID" ,"4" );
        params.addBodyParameter("PayName" ,"承诺书认证" );
        params.addBodyParameter("PictureDes1" , uploadfile );
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("V203StarRegister04", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result) ;
                    String status_code = jsonObject.getString("status_code");
                    switch (status_code){
                        case "200" :
                            ToastUtils.shortToast(StarRegisterActivity04.this , "已提交审核，客服人员稍后会与您联系！");
                            finish();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.shortToast(StarRegisterActivity04.this, "网络连接异常");
            }
        }) ;
    }

    private void download(){
            HttpUtils http = new HttpUtils();
            http.download( Url.FileIP + "/star/ziya.doc", "/sdcard/资芽网承诺书.doc", true, true, new RequestCallBack<File>() {

                @Override
                public void onStart() {
                    download.setText("正在连接...");
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    download.setText(current + "/" + total);
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    if ("maybe the file has downloaded completely".equals(msg)){
                        download.setText("文件位置：/sdcard/资芽网承诺书.doc");
                        ToastUtils.shortToast(StarRegisterActivity04.this  , "文件已下载");
                    }
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    // TODO Auto-generated method stub
                    download.setText("文件位置：" + responseInfo.result.getPath());
                    ToastUtils.shortToast(StarRegisterActivity04.this, "文件下载成功");
                }
            });

        }

    private void iconGet() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_icon, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button picture = (Button) view.findViewById(R.id.picture);
        Button camera = (Button) view.findViewById(R.id.camera);
        Button clear = (Button) view.findViewById(R.id.clear);
        //对图库按钮设置监听事件
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, 1);
                window.dismiss();
            }
        });
        //对打开相机按钮设置监听事件
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fileName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                if (ActivityCompat.checkSelfPermission(StarRegisterActivity04.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.shortToast(StarRegisterActivity04.this, "请在管理中心，给予相机相应权限。");
                    window.dismiss();
                }else {
                    file = new File(Environment.getExternalStorageDirectory(), fileName);
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent1, 2);
                    window.dismiss();
                }
            }
        });
        //对取消操作按钮设置监听事件
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(picture, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.5f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    //构造剪裁图片意图
    private Intent getCropImageIntent(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        return intent;
    }

    //开启剪裁图片意图
    protected void doCropPhoto(Uri uri) {
        Intent intent = getCropImageIntent(uri);
        startActivityForResult(intent, 3);
    }

    //得到图片的类型
    public String getSubStr(String str) {
        int position = str.lastIndexOf(".");
        str = str.substring(position + 1);
        return str;
    }

    //将bitmap转化为byte[]
    public byte[] getByte(Bitmap bit, String str) {
        Bitmap.CompressFormat temp = null;
        if ("jpg".equals(str)) {
            temp = Bitmap.CompressFormat.JPEG;
        } else if ("png".equals(str)) {
            temp = Bitmap.CompressFormat.PNG;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(temp, 80, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    //打开页面后的处理。
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Uri uri = Uri.fromFile(new File(picturePath));
                    imgstr = getSubStr(picturePath);
                    file = new File(picturePath);
                    Log.e("benben图库后缀01", imgstr);
                    Log.e("benben图库路径01", picturePath);
                    //打开图片的裁剪意图
                    doCropPhoto(uri);
                } else {
                    Toast.makeText(StarRegisterActivity04.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    imgstr = getSubStr(file.getPath());
                    Log.e("benben相机后缀01", imgstr);
                    Log.e("benben相机路径01", file.getPath());
                    //剪裁图片为方形
                    doCropPhoto(Uri.fromFile(file));
                } else {
                    Toast.makeText(StarRegisterActivity04.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        final Bitmap bitmap = extras.getParcelable("data");
                        final byte[] aByte = getByte(bitmap, imgstr);
                        long l = System.currentTimeMillis();
                        SDUtil.saveDataInfoSDCard(aByte, "ziya", l +"."+ imgstr ) ;
                        String path = SDUtil.getSDPath() + File.separator + "ziya"+ File.separator + l +"." + imgstr ;
                        uploadfile = new File(path);
                        star_imageVIew.setImageBitmap(bitmap);
                        star_frame.setVisibility(View.VISIBLE);
                        upload.setText("点击上传");
                    }
                } else {
                    Toast.makeText(StarRegisterActivity04.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void goDelPic() {
        star_frame.setVisibility(View.GONE);
        upload.setText("上传承诺书");
    }


}
