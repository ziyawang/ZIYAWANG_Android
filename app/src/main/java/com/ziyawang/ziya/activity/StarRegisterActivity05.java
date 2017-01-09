package com.ziyawang.ziya.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.SDUtil;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StarRegisterActivity05 extends BenBenActivity implements View.OnClickListener {
    //返回按钮
    private RelativeLayout pre ;
    //通过相机暂时存储的图片文件
    private File file;
    //发布时的图片1
    private File file_img01;
    //发布时的图片2
    private File file_img02;
    //发布时的图片3
    private File file_img03;
    // 图片1,2,3
    private ImageView release_img_one, release_img_two, release_img_three;
    //取消图片1,2,3
    private ImageView release_img_cancel_one, release_img_cancel_two, release_img_cancel_three;
    //图片区域1,2,3
    private FrameLayout release_frame_one, release_frame_two, release_frame_three;
    //增加图片按钮
    private TextView release_img_add;
    //图片的后缀名
    private String imgstr;
    //上传
    private Button submit ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_star_register05);
    }

    @Override
    public void initViews() {
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        release_frame_one = (FrameLayout) findViewById(R.id.release_frame_one);
        release_frame_two = (FrameLayout) findViewById(R.id.release_frame_two);
        release_frame_three = (FrameLayout) findViewById(R.id.release_frame_three);
        release_img_one = (ImageView) findViewById(R.id.release_img_one);
        release_img_two = (ImageView) findViewById(R.id.release_img_two);
        release_img_three = (ImageView) findViewById(R.id.release_img_three);
        release_img_cancel_one = (ImageView) findViewById(R.id.release_img_cancel_one);
        release_img_cancel_two = (ImageView) findViewById(R.id.release_img_cancel_two);
        release_img_cancel_three = (ImageView) findViewById(R.id.release_img_cancel_three);
        release_img_add = (TextView) findViewById(R.id.release_img_add);
        submit = (Button) findViewById(R.id.submit);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this );
        submit.setOnClickListener(this );
        release_img_add.setOnClickListener(this );
        release_img_cancel_one.setOnClickListener(this );
        release_img_cancel_two.setOnClickListener(this );
        release_img_cancel_three.setOnClickListener(this );
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
            case R.id.release_img_add :
                picGet01();
                break;
            case R.id.release_img_cancel_one :
                if (release_frame_two.getVisibility() == View.VISIBLE) {
                    ToastUtils.shortToast(StarRegisterActivity05.this, "无法删除第一张图片");
                } else {
                    release_frame_one.setVisibility(View.GONE);
                }
                break;
            case R.id.release_img_cancel_two :
                if (release_frame_three.getVisibility() == View.VISIBLE) {
                    ToastUtils.shortToast(StarRegisterActivity05.this, "无法删除第二张图片");
                } else {
                    release_frame_two.setVisibility(View.GONE);
                }
                break;
            case R.id.release_img_cancel_three :
                release_frame_three.setVisibility(View.GONE);
                release_img_add.setVisibility(View.VISIBLE);
                break;
            case R.id.submit :
                if (isSubmit()){
                    loadData() ;
                }
                break;
            default:
                break;
        }
    }

    private void loadData() {
        String urls = String.format(Url.V203StarRegister04, GetBenSharedPreferences.getTicket(this) ) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("StarID" ,"5" );
        params.addBodyParameter("PayName" ,"三证认证" );
        if (release_frame_one.getVisibility() == View.VISIBLE) {
            params.addBodyParameter("PictureDes1", file_img01);
            if (release_frame_two.getVisibility() == View.VISIBLE) {
                params.addBodyParameter("PictureDes2", file_img02);
                if (release_frame_three.getVisibility() == View.VISIBLE) {
                    params.addBodyParameter("PictureDes3", file_img03);
                }
            }
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("V203StarRegister04", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result) ;
                    String status_code = jsonObject.getString("status_code");
                    switch (status_code){
                        case "200" :
                            ToastUtils.shortToast(StarRegisterActivity05.this , "已提交审核，客服人员稍后会与您联系！");
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
                ToastUtils.shortToast(StarRegisterActivity05.this, "网络连接异常");
            }
        }) ;
    }

    private boolean isSubmit() {
        if (release_frame_one.getVisibility() != View.VISIBLE) {
            ToastUtils.shortToast(StarRegisterActivity05.this , "请至少添加一张图片");
            return false ;
        }else {
            return true;
        }

    }

    private void picGet01() {
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
                String str = null;
                Date date = null;
                final String fileName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";

                if (ActivityCompat.checkSelfPermission(StarRegisterActivity05.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.shortToast(StarRegisterActivity05.this, "请在管理中心，给予相机相应权限。");
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
                    Toast.makeText(StarRegisterActivity05.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(StarRegisterActivity05.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (resultCode == Activity.RESULT_OK) {

                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bitmap = extras.getParcelable("data");
                        final byte[] aByte = getByte(bitmap, imgstr);


                        if (release_frame_one.getVisibility() == View.VISIBLE) {
                            if (release_frame_two.getVisibility() == View.VISIBLE) {

                                //3不可见将，bitmap传给3
                                release_frame_three.setVisibility(View.VISIBLE);
                                release_img_add.setVisibility(View.GONE);
                                release_img_three.setImageBitmap(bitmap);


                                //将二进制流存储成文件
                                final long time = System.currentTimeMillis();
                                SDUtil.saveDataInfoSDCard(aByte, "ziya", time + "." + imgstr);
                                String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + time + "." + imgstr;
                                file_img03 = new File(path);

                            } else {
                                //2不可见将，bitmap传给2
                                release_frame_two.setVisibility(View.VISIBLE);
                                release_img_two.setImageBitmap(bitmap);

                                //将二进制流存储成文件
                                final long time = System.currentTimeMillis();
                                SDUtil.saveDataInfoSDCard(aByte, "ziya", time + "." + imgstr);
                                String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + time + "." + imgstr;
                                file_img02 = new File(path);
                            }
                        } else {
                            //1不可见将，bitmap传给1
                            release_frame_one.setVisibility(View.VISIBLE);
                            release_img_one.setImageBitmap(bitmap);

                            //将二进制流存储成文件
                            final long time = System.currentTimeMillis();
                            SDUtil.saveDataInfoSDCard(aByte, "ziya", time + "." + imgstr);
                            String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + time + "." + imgstr;
                            file_img01 = new File(path);
                        }
                    }

                } else {
                    Toast.makeText(StarRegisterActivity05.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
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
        bit.compress(temp, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}
