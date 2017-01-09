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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
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

public class ServiceRegisterActivity extends BenBenActivity implements View.OnClickListener {

    private RelativeLayout pre ;
    private EditText service_register_name;
    private EditText service_register_phone;
    private EditText service_register_companyName;
    private EditText service_register_companyDes;
    private TextView service_part_one;
    private TextView service_part_one_des;
    private TextView service_part_two;
    private TextView service_part_two_des;
    private TextView service_type;
    private TextView service_type_des;
    private TextView release_img_add;
    private FrameLayout release_frame_one;
    private FrameLayout release_frame_two;
    private FrameLayout release_frame_three;
    private ImageView release_img_one;
    private ImageView release_img_two;
    private ImageView release_img_three;
    private ImageView release_img_cancel_one;
    private ImageView release_img_cancel_two;
    private ImageView release_img_cancel_three;
    private TextView text_submit;
    private File file;
    private File file_img01;
    private File file_img02;
    private File file_img03;
    private String imgstr;
    private static String login;
    private StringBuffer stringBuffer01 = new StringBuffer();
    private String type = "";
    private String urls ;
    private MyProgressDialog dialog ;
    private String root ;
    //员工人数
    private EditText service_register_count ;
    //注册资金
    private EditText service_register_money ;
    //成立时间
    private TextView text_time ;
    private ScrollView scrollView ;

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("服务方注册页面");
        //统计时长
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("服务方注册页面");
        //统计时长
        MobclickAgent.onPause(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //添加回退事件
            case R.id.pre :
                finish();
                break;
            //对企业所在地的获取
            case R.id.service_part_one :
                startActivityForResult(new Intent(ServiceRegisterActivity.this, PartActivity.class), 6);
                break;
            //对企业类型的服务地区的获取
            case R.id.service_part_two :
                startActivityForResult(new Intent(ServiceRegisterActivity.this, ServicePartActivity.class), 4);
                break;
            //对企业类型的服务类型的获取
            case R.id.service_type :
                startActivityForResult(new Intent(ServiceRegisterActivity.this, ServiceTypeActivity.class), 5);
                break;
            //添加图片
            case R.id.release_img_add :
                if (release_frame_one.getVisibility() == View.VISIBLE) {
                    if (release_frame_two.getVisibility() == View.VISIBLE) {
                        picGet();
                    } else {
                        picGet();
                    }
                } else {
                    picGet();
                }
                break;
            //删除第一张
            case R.id.release_img_cancel_one :
                if (release_frame_two.getVisibility() == View.VISIBLE && release_frame_three.getVisibility() == View.VISIBLE) {
                    ToastUtils.shortToast(ServiceRegisterActivity.this, "无法删除第一张图片");
                } else {
                    release_frame_one.setVisibility(View.GONE);
                }
                break;
            //删除第二张
            case R.id.release_img_cancel_two :
                if (release_frame_three.getVisibility() == View.VISIBLE) {
                    ToastUtils.shortToast(ServiceRegisterActivity.this, "无法删除第二张图片");
                } else {
                    release_frame_two.setVisibility(View.GONE);
                }
                break;
            //删除第三张
            case R.id.release_img_cancel_three :
                release_frame_three.setVisibility(View.GONE);
                release_img_add.setVisibility(View.VISIBLE);
                break;
            //提交按钮
            case R.id.text_submit :
                if (isSubmit()){
                    submit() ;
                }
                break;
            //成立时间
            case R.id.text_time :
                popData(text_time) ;
                break;
            default:
                break;
        }
    }

    private void popData(final TextView text_time) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.common_window_data_picker, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView left = (TextView) view.findViewById(R.id.left);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker ) ;
        TextView right = (TextView) view.findViewById(R.id.right);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();
                text_time.setText(year + "年" + (month+1) + "月" + dayOfMonth + "日");
                window.dismiss();
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(scrollView, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.4f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void submit() {
        if (type.contains("ben")) {
            type.replace("ben", "05");
            type.trim();
            Log.e("benben", type);
        }
        if ("2".equals(root)) {
            //格式化网络连接url
            urls = String.format(Url.ServiceReRegister, login);
        } else {
            urls = String.format(Url.ServiceRegister, login);
        }
        //开启网络请求
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("ConnectPerson", service_register_name.getText().toString());
        params.addBodyParameter("ConnectPhone", service_register_phone.getText().toString());
        params.addBodyParameter("ServiceName", service_register_companyName.getText().toString());
        params.addBodyParameter("ServiceIntroduction", service_register_companyDes.getText().toString());
        params.addBodyParameter("ServiceLocation", service_part_one_des.getText().toString());
        params.addBodyParameter("ServiceArea", service_part_two_des.getText().toString());

        params.addBodyParameter("Size", service_register_count.getText().toString());
        params.addBodyParameter("Founds", service_register_money.getText().toString());
        params.addBodyParameter("RegTime", text_time.getText().toString());

        params.addBodyParameter("ServiceType", type);

        Log.e("benben", login);
        if (release_frame_one.getVisibility() == View.VISIBLE) {
            params.addBodyParameter("ConfirmationP1", file_img01);
            if (release_frame_two.getVisibility() == View.VISIBLE) {
                params.addBodyParameter("ConfirmationP2", file_img02);
                if (release_frame_three.getVisibility() == View.VISIBLE) {
                    params.addBodyParameter("ConfirmationP3", file_img03);
                }
            }
        }
        dialog = new MyProgressDialog(ServiceRegisterActivity.this, "加载提交中请稍后。。。");
        dialog.show();
        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("benben", responseInfo.result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseInfo.result);
                    String status_code = jsonObject.getString("status_code");
                    if (status_code.equals("200")) {
                        ToastUtils.shortToast(ServiceRegisterActivity.this, "服务方认证成功");
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        Intent intent = new Intent(ServiceRegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {

                if (dialog != null) {
                    dialog.dismiss();
                }
                error.printStackTrace();
            }
        });
    }

    private boolean isSubmit() {
        if (TextUtils.isEmpty(service_register_companyName.getText().toString())) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请输入您的企业名称");
            return false ;
        }
        if (TextUtils.isEmpty(service_part_one_des.getText().toString()) && service_part_one_des.getVisibility() != View.VISIBLE) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请选择您的企业所在地");
            return false ;
        }

        if (TextUtils.isEmpty(service_register_count.getText().toString())) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请输入您的企业规模");
            return false ;
        }
        if (TextUtils.isEmpty(service_register_money.getText().toString())) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请输入您的企业注册资金");
            return false ;
        }
        if (TextUtils.isEmpty(text_time.getText().toString()) || "请选择".equals(text_time.getText().toString() )) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请输入您的企业成立时间");
            return false ;
        }

        if (TextUtils.isEmpty(service_register_name.getText().toString())) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请添加您的联系人姓名");
            return false ;
        }
        if (TextUtils.isEmpty(service_register_phone.getText().toString())) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请添加您的联系方式");
            return false ;
        }
        if (TextUtils.isEmpty(service_register_companyDes.getText().toString())) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请输入您的企业简介");
            return false ;
        }
        if (TextUtils.isEmpty(service_part_two_des.getText().toString()) && service_part_two_des.getVisibility() != View.VISIBLE) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请选择您的服务地区");
            return false ;
        }
        if (TextUtils.isEmpty(service_type_des.getText().toString()) && service_type_des.getVisibility() != View.VISIBLE) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请选择您的服务类型");
            return false ;
        }
        if (release_frame_one.getVisibility() != View.VISIBLE) {
            ToastUtils.shortToast(ServiceRegisterActivity.this, "请至少添加一张图片");
            return false ;
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_service_register);
    }

    @Override
    public void initViews() {
        scrollView = (ScrollView)findViewById(R.id.scrollView ) ;
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        service_register_name = (EditText) findViewById(R.id.service_register_name);
        service_register_phone = (EditText) findViewById(R.id.service_register_phone);
        service_register_companyName = (EditText) findViewById(R.id.service_register_companyName);
        service_register_companyDes = (EditText) findViewById(R.id.service_register_companyDes);
        service_part_one = (TextView) findViewById(R.id.service_part_one);
        service_part_one_des = (TextView) findViewById(R.id.service_part_one_des);
        service_part_two = (TextView) findViewById(R.id.service_part_two);
        service_part_two_des = (TextView) findViewById(R.id.service_part_two_des);
        service_type = (TextView) findViewById(R.id.service_type);
        service_type_des = (TextView) findViewById(R.id.service_type_des);
        release_img_add = (TextView) findViewById(R.id.release_img_add);
        release_frame_one = (FrameLayout) findViewById(R.id.release_frame_one);
        release_frame_two = (FrameLayout) findViewById(R.id.release_frame_two);
        release_frame_three = (FrameLayout) findViewById(R.id.release_frame_three);
        release_img_one = (ImageView) findViewById(R.id.release_img_one);
        release_img_two = (ImageView) findViewById(R.id.release_img_two);
        release_img_three = (ImageView) findViewById(R.id.release_img_three);
        release_img_cancel_one = (ImageView) findViewById(R.id.release_img_cancel_one);
        release_img_cancel_two = (ImageView) findViewById(R.id.release_img_cancel_two);
        release_img_cancel_three = (ImageView) findViewById(R.id.release_img_cancel_three);
        text_submit = (TextView) findViewById(R.id.text_submit);

        service_register_money = (EditText) findViewById(R.id.service_register_money);
        service_register_count = (EditText) findViewById(R.id.service_register_count);
        text_time = (TextView) findViewById(R.id.text_time);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        service_part_one.setOnClickListener(this);
        service_part_two.setOnClickListener(this);
        service_type.setOnClickListener(this);
        release_img_add.setOnClickListener(this);
        release_img_cancel_one.setOnClickListener(this);
        release_img_cancel_two.setOnClickListener(this);
        release_img_cancel_three.setOnClickListener(this);
        text_time.setOnClickListener(this);
        text_submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        final SharedPreferences loginCode = getSharedPreferences("loginCode", MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);
        Intent intent = getIntent();
        root = intent.getStringExtra("root");
        switch (root) {
            case "0":
                text_submit.setText("提交");
                break;
            case "1":
                text_submit.setVisibility(View.GONE);

                //企业名称
                //企业简介
                //企业所在地
                //服务类型
                //联系人姓名
                //联系方式
                //图1
                //图2
                //图3
                //服务地区
                String serviceName = intent.getStringExtra("ServiceName");
                String ServiceIntroduction = intent.getStringExtra("ServiceIntroduction");
                String ServiceLocation = intent.getStringExtra("ServiceLocation");
                String ServiceType = intent.getStringExtra("ServiceType");
                String ConnectPerson = intent.getStringExtra("ConnectPerson");
                String ConnectPhone = intent.getStringExtra("ConnectPhone");
                String ConfirmationP1 = intent.getStringExtra("ConfirmationP1");
                String ConfirmationP2 = intent.getStringExtra("ConfirmationP2");
                String ConfirmationP3 = intent.getStringExtra("ConfirmationP3");
                String ServiceArea = intent.getStringExtra("ServiceArea");
                String Size = intent.getStringExtra("Size");
                String Founds = intent.getStringExtra("Founds");
                String Regtime = intent.getStringExtra("RegTime");
                if ("0".equals(Size)){
                    service_register_count.setHint("未填写");

                }else {
                    service_register_count.setHint(Size);
                }
                if ("0".equals(Founds)){
                    service_register_money.setHint("未填写");
                }else {
                    service_register_money.setHint(Founds);
                }
                if ("0000-00-00 00:00:00".equals(Regtime)){
                    text_time.setHint("未填写");
                }else {
                    text_time.setHint(Regtime);
                }
                service_register_name.setText(ConnectPerson);
                service_register_phone.setText(ConnectPhone);
                service_register_companyName.setText(serviceName);
                service_register_companyDes.setText(ServiceIntroduction);
                service_part_one_des.setText(ServiceLocation);
                service_part_two_des.setText(ServiceArea);
                //service_type_des.setText(ServiceType);
                service_part_one_des.setVisibility(View.VISIBLE);
                service_part_two_des.setVisibility(View.VISIBLE);
                service_type_des.setVisibility(View.VISIBLE);

                service_part_one.setClickable(false);
                service_part_two.setClickable(false);
                service_type.setClickable(false);

                release_img_add.setVisibility(View.GONE);

                if (!ConfirmationP1.equals("")) {
                    release_frame_one.setVisibility(View.VISIBLE);
                    BitmapUtils bitmapUtils = new BitmapUtils(ServiceRegisterActivity.this);
                    bitmapUtils.display(release_img_one, Url.FileIP + ConfirmationP1);
                    release_img_cancel_one.setVisibility(View.GONE);
                }
                if (!ConfirmationP2.equals("")) {
                    release_frame_two.setVisibility(View.VISIBLE);
                    BitmapUtils bitmapUtils = new BitmapUtils(ServiceRegisterActivity.this);
                    bitmapUtils.display(release_img_two, Url.FileIP + ConfirmationP2);
                    release_img_cancel_two.setVisibility(View.GONE);
                }
                if (!ConfirmationP3.equals("")) {
                    release_frame_three.setVisibility(View.VISIBLE);
                    BitmapUtils bitmapUtils = new BitmapUtils(ServiceRegisterActivity.this);
                    bitmapUtils.display(release_img_three, Url.FileIP + ConfirmationP3);
                    release_img_cancel_three.setVisibility(View.GONE);
                }

                break;
            case "2":
                String serviceName1 = intent.getStringExtra("ServiceName");
                String ServiceIntroduction1 = intent.getStringExtra("ServiceIntroduction");
                String ServiceLocation1 = intent.getStringExtra("ServiceLocation");
                String ServiceType1 = intent.getStringExtra("ServiceType");
                String ConnectPerson1 = intent.getStringExtra("ConnectPerson");
                String ConnectPhone1 = intent.getStringExtra("ConnectPhone");
                String ConfirmationP11 = intent.getStringExtra("ConfirmationP1");
                String ConfirmationP21 = intent.getStringExtra("ConfirmationP2");
                String ConfirmationP31 = intent.getStringExtra("ConfirmationP3");
                String ServiceArea1 = intent.getStringExtra("ServiceArea");
                String Size01 = intent.getStringExtra("Size");
                String Founds01 = intent.getStringExtra("Founds");
                String Regtime01 = intent.getStringExtra("RegTime");
                if ("0".equals(Size01)){
                    service_register_count.setHint("未填写");

                }else {
                    service_register_count.setHint(Size01);
                }
                if ("0".equals(Founds01)){
                    service_register_money.setHint("未填写");
                }else {
                    service_register_money.setHint(Founds01);
                }
                if ("0000-00-00 00:00:00".equals(Regtime01)){
                    text_time.setHint("未填写");
                }else {
                    text_time.setHint(Regtime01);
                }

                service_register_name.setText(ConnectPerson1);
                service_register_phone.setText(ConnectPhone1);
                service_register_companyName.setText(serviceName1);
                service_register_companyDes.setText(ServiceIntroduction1);
                service_part_one_des.setText(ServiceLocation1);
                service_part_two_des.setText(ServiceArea1);
                service_type_des.setText(ServiceType1);
                service_part_one_des.setVisibility(View.VISIBLE);
                service_part_two_des.setVisibility(View.VISIBLE);
                service_type_des.setVisibility(View.GONE);
                release_img_add.setVisibility(View.VISIBLE);

//                if (ConfirmationP11.equals("")) {
//                    release_frame_one.setVisibility(View.GONE);
//                }else {
//                    BitmapUtils bitmapUtils = new BitmapUtils(ServiceRegisterActivity.this);
//                    bitmapUtils.display(release_img_one, Url.FileIP + ConfirmationP11);
//                }
//
//                if (ConfirmationP21.equals("")) {
//                    release_frame_two.setVisibility(View.GONE);
//                }else {
//                    BitmapUtils bitmapUtils = new BitmapUtils(ServiceRegisterActivity.this);
//                    bitmapUtils.display(release_img_two, Url.FileIP + ConfirmationP21);
//                }
//
//                if (ConfirmationP31.equals("")) {
//                    release_frame_three.setVisibility(View.GONE);
//                }else {
//                    BitmapUtils bitmapUtils = new BitmapUtils(ServiceRegisterActivity.this);
//                    bitmapUtils.display(release_img_three, Url.FileIP + ConfirmationP31);
//                    release_img_add.setVisibility(View.GONE);
//                }

                text_submit.setText("重新提交");
                break;
            default:
                break;
        }
    }


    private void picGet() {
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

                if(ActivityCompat.checkSelfPermission(ServiceRegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ToastUtils.shortToast(ServiceRegisterActivity.this, "请在管理中心，给予相机相应权限。");
                    window.dismiss();
                } else {
                    final String fileName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    Toast.makeText(ServiceRegisterActivity.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
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
                    //}

                } else {
                    Toast.makeText(ServiceRegisterActivity.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
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
                                Log.e("图片3", path);
                                file_img03 = new File(path);

                            } else {
                                //2不可见将，bitmap传给2
                                release_frame_two.setVisibility(View.VISIBLE);
                                release_img_two.setImageBitmap(bitmap);

                                //将二进制流存储成文件
                                final long time = System.currentTimeMillis();
                                SDUtil.saveDataInfoSDCard(aByte, "ziya", time + "." + imgstr);
                                String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + time + "." + imgstr;
                                Log.e("图片2", path);
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
                            Log.e("图片1", path);
                            file_img01 = new File(path);
                        }
                    }

                } else {
                    Toast.makeText(ServiceRegisterActivity.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if (resultCode == RESULT_OK && null != data) {
                    String result02 = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    //Log.e("benben" , result02 ) ;
                    service_part_two_des.setText(result02);
                    service_part_two_des.setVisibility(View.VISIBLE);
                }
                break;
            case 5:
                if (resultCode == RESULT_OK && null != data) {
                    String result03 = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    Log.e("benben" , result03 ) ;
                    //service_type_des.setText(result03);
                    type = result03;
                    if (!TextUtils.isEmpty(stringBuffer01)){
                        stringBuffer01.delete(0, stringBuffer01.length() );
                    }
                    String[] split = result03.split(",");
                    for (int i = 0; i < split.length; i++) {
                        Log.e("benben", split[i]);
                        switch (split[i]) {
                            case "01":
                                stringBuffer01.append("收购资产包");
                                stringBuffer01.append(",");
                                break;
                            case "02":
                                stringBuffer01.append("委外催收");
                                stringBuffer01.append(",");
                                break;
                            case "03":
                                stringBuffer01.append("法律服务");
                                stringBuffer01.append(",");
                                break;
                            case "04":
                                stringBuffer01.append("保理公司");
                                stringBuffer01.append(",");
                                break;
                            case "05":
                                stringBuffer01.append("典当公司");
                                stringBuffer01.append(",");
                                break;
                            case "ben":
                                stringBuffer01.append("担保公司");
                                stringBuffer01.append(",");
                                break;
                            case "06":
                                stringBuffer01.append("投融资服务");
                                stringBuffer01.append(",");
                                break;
                            case "10":
                                stringBuffer01.append("尽职调查");
                                stringBuffer01.append(",");
                                break;
                            case "12":
                                stringBuffer01.append("收购固产");
                                stringBuffer01.append(",");
                                break;
                            case "14":
                                stringBuffer01.append("债权收购");
                                stringBuffer01.append(",");
                                break;
                            default:
                                break;
                        }
                    }
                    service_type_des.setText(stringBuffer01.toString());
                    service_type_des.setVisibility(View.VISIBLE);
                }
                break;
            case 6:
                if (resultCode == RESULT_OK && null != data) {
                    String result01 = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    service_part_one_des.setText(result01);
                    service_part_one_des.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
    }

}
