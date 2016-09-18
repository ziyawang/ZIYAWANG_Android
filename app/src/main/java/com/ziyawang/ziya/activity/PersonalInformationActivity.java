package com.ziyawang.ziya.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.ziyawang.ziya.view.MyIconImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Locale;

/**
 * 个人信息页面
 */
public class PersonalInformationActivity extends BenBenActivity implements View.OnClickListener {

    //修改密码按钮
    private TextView basic_changePwd;
    //回退按钮
    private RelativeLayout pre ;
    //更改头像的按钮
    private RelativeLayout basic_change_icon;
    //拿到图片文件的后缀名
    private String imgstr;
    //需要上传到后台的图片文件路径
    private File file;
    //头像文件的bigmap
    private Bitmap bitmap;
    //头像显示组件
    private MyIconImageView my_icon;
    //sp中缓存的ticket
    private static String login;
    //联系电话显示组件
    private TextView phoneNumber_textView ;

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("个人信息页面");
        //统计时长
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("个人信息页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //先拿到用户的缓存的头像,存在进行加载。
        initIcon() ;
    }

    private void initIcon() {
        File files = new File(Url.IconPath);
        if (files.exists()) {
            final byte[] icons = SDUtil.getDataFromSDCard("icon.png");
            bitmap = BitmapFactory.decodeByteArray(icons, 0, icons.length);
            my_icon.setImageBitmap(bitmap);
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_personal_information);
    }

    @Override
    public void initViews() {
        basic_changePwd = (TextView) findViewById(R.id.basic_changePwd);
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        basic_change_icon = (RelativeLayout) findViewById(R.id.basic_change_icon);
        my_icon = (MyIconImageView) findViewById(R.id.my_icon);
        phoneNumber_textView = (TextView)findViewById(R.id.phoneNumber ) ;
    }

    @Override
    public void initListeners() {
        basic_change_icon.setOnClickListener(this);
        pre.setOnClickListener(this);
        basic_changePwd.setOnClickListener(this);
    }

    @Override
    public void initData() {
        login = GetBenSharedPreferences.getTicket(this) ;
        //拿到传过来的intent
        Intent intent = getIntent() ;
        String phoneNumber = intent.getStringExtra("phoneNumber");
        phoneNumber_textView.setText(phoneNumber);
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
                if (ActivityCompat.checkSelfPermission(PersonalInformationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.shortToast(PersonalInformationActivity.this, "请在管理中心，给予相机相应权限。");
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
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Uri uri = Uri.fromFile(new File(picturePath));
                    imgstr = getSubStr(picturePath);
                    file = new File(picturePath);
                    //打开图片的裁剪意图
                    doCropPhoto(uri);
                } else {
                    Toast.makeText(PersonalInformationActivity.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PersonalInformationActivity.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        final Bitmap bitmap = extras.getParcelable("data");
                        final byte[] aByte = getByte(bitmap, imgstr);
                        SDUtil.saveDataInfoSDCard(aByte, "ziya", "icon.png") ;
                        String path = SDUtil.getSDPath() + File.separator + "ziya"+ File.separator + "icon.png" ;
                        File files = new File(path);
                        String urls = String.format(Url.ChangeIcon, login) ;
                        HttpUtils utils = new HttpUtils()  ;
                        RequestParams params = new RequestParams() ;
                        params.addBodyParameter("UserPicture" , files );
                        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Log.e("benben", responseInfo.result);
                                try {
                                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                                    String status_code = jsonObject.getString("status_code");
                                    switch (status_code) {
                                        case "200":
                                            ToastUtils.shortToast(PersonalInformationActivity.this, "更换头像成功");
                                            my_icon.setImageBitmap(bitmap);
                                            break;
                                        default:
                                            ToastUtils.shortToast(PersonalInformationActivity.this, "更换头像失败");
                                            break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ToastUtils.shortToast(PersonalInformationActivity.this, "网络连接异常");
                                }
                            }
                            @Override
                            public void onFailure(HttpException error, String msg) {
                                error.printStackTrace();
                                ToastUtils.shortToast(PersonalInformationActivity.this, "更换头像失败");
                            }
                        }) ;
                    }
                } else {
                    Toast.makeText(PersonalInformationActivity.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //回退事件
            case R.id.pre :
                finish();
                break;
            //更改头像事件的监听
            case R.id.basic_change_icon :
                iconGet();
                break;
            //更改密码
            case R.id.basic_changePwd :
                goChangePwdActivity() ;
                break;
            default:
                break;
        }
    }

    private void goChangePwdActivity() {
        Intent intent = new Intent(PersonalInformationActivity.this, ChangePwdActivity.class);
        startActivity(intent);
    }
}
