package com.ziyawang.ziya.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.SDUtil;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class FeedBackActivity extends BenBenActivity implements View.OnClickListener {
    //缓存到本地的用户的ticket
    private static String login ;
    //回退按钮
    private RelativeLayout pre ;
    //意见反馈图片描述
    private ImageView feedBack_imageVIew ;
    //意见反馈的意见输入框
    private EditText feedBack_editText ;
    //意见反馈的添加凭证按钮
    private TextView feedBack_textView ;
    //获取图片的后缀名
    private String imgstr ;
    //图片的暂存文件
    private File file ;
    //需要发送的图片的文件
    private File uploadfile ;
    //提交按钮
    private TextView feedBack_submit ;
    //数据加载的dialog
    private MyProgressDialog dialog ;
    //图片的整体frame布局
    private FrameLayout feedBack_frame ;
    //图片的取消按钮
    private ImageView feedback_img_cancel ;

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("反馈页面");
        //统计时长
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("反馈页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_feed_back);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        feedBack_editText = (EditText)findViewById(R.id.feedBack_editText) ;
        feedBack_textView = (TextView)findViewById(R.id.feedBack_textView) ;
        feedBack_submit = (TextView)findViewById(R.id.feedBack_submit) ;
        feedBack_imageVIew = (ImageView)findViewById(R.id.feedBack_imageVIew ) ;
        feedback_img_cancel = (ImageView)findViewById(R.id.feedback_img_cancel ) ;
        feedBack_frame = (FrameLayout)findViewById(R.id.feedBack_frame) ;
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        feedBack_textView.setOnClickListener(this);
        feedback_img_cancel.setOnClickListener(this);
        feedBack_submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        login = GetBenSharedPreferences.getTicket(this ) ;
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

                if(ActivityCompat.checkSelfPermission(FeedBackActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ToastUtils.shortToast(FeedBackActivity.this, "请在管理中心，给予相机相应权限。");
                    window.dismiss();
                } else {
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
        intent.putExtra("aspectX", 1);//裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 128);// 输出图片大小
        intent.putExtra("outputY", 128);
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
        bit.compress(temp, 100, bos);//参数100表示不压缩
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
                    Toast.makeText(FeedBackActivity.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(FeedBackActivity.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
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
                        feedBack_imageVIew.setImageBitmap(bitmap);
                        feedBack_frame.setVisibility(View.VISIBLE);
                        feedBack_textView.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(FeedBackActivity.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.pre :
                finish();
                break;
            //添加图片的按钮
            case R.id.feedBack_textView :
                iconGet();
                break;
            //删除图片的按钮
            case R.id.feedback_img_cancel :
                goDelPic() ;
                break;
            //提交按钮
            case R.id.feedBack_submit :
                goSubmit() ;
                break;
            default:
                break;
        }
    }

    private void goSubmit() {
        if (!TextUtils.isEmpty(feedBack_editText.getText().toString().trim())) {
            //提交反馈意见
            goLoadSubmit() ;
        } else {
            ToastUtils.shortToast(FeedBackActivity.this, "请添加您的意见");
        }
    }

    private void goLoadSubmit() {
        //使dialog可见
        showBenDialog();
        String urls = String.format(Url.FeedBack, login);
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        //通过判断是否可见，决定是否添加图片
        if (feedBack_frame.getVisibility() == View.VISIBLE) {
            params.addBodyParameter("Picture", uploadfile);
        }
        params.addBodyParameter("Content", feedBack_editText.getText().toString().trim());
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //隐藏dialog
                hiddenBenDialog();
                //处理成功回调的信息
                dealResult(responseInfo.result) ;
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //隐藏dialog
                hiddenBenDialog();
                ToastUtils.shortToast(FeedBackActivity.this, "网络连接异常");
                //打印失败回调的信息
                error.printStackTrace();
            }
        });
    }

    private void dealResult(String result) {
        try {
            JSONObject object = new JSONObject(result);
            String status_code = object.getString("status_code");
            switch (status_code) {
                case "200":
                    ToastUtils.shortToast(FeedBackActivity.this, "反馈意见已提交");
                    finish();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void goDelPic() {
        feedBack_frame.setVisibility(View.GONE);
        feedBack_textView.setVisibility(View.VISIBLE);
    }
    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(FeedBackActivity.this, "提交数据中请稍后。。。");
        // 不可以用“返回键”取消
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 隐藏数据加载框
     */
    private void hiddenBenDialog() {
        //关闭dialog
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
