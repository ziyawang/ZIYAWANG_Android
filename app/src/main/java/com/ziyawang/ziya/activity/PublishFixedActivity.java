package com.ziyawang.ziya.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.CashierInputFilter;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.MusicPlayer;
import com.ziyawang.ziya.tools.SDUtil;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.WheelView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.rong.photoview.scrollerproxy.ScrollerProxy;

public class PublishFixedActivity extends BenBenActivity implements View.OnClickListener{
    //数据加载的dialog
    private MyProgressDialog dialog  ;
    private String Promise ;
    private TextView info_weituo ;
    private Button publish ;
    private ScrollView scrollView ;
    private LinearLayout linear01 ; 
    private LinearLayout linear02 ; 
    //选择身份
    private TextView text_id ;
    // 地区
    private TextView text_part ;
    // 标的物类型
    private TextView text_type ;
    /**
     * 固定资产，土地展示页面，相互独立
     */
    //规划用途
    private TextView text_yongtu ;
    //面积
    private EditText edit_big ;
    //剩余使用年限
    private EditText edit_time ;
    //转让方式
    private TextView text_type02 ;
    /**
     * 固定资产，房产展示页面，相互独立
     */
    //房产类型
    private TextView text_type03 ;
    //规划用途
    private TextView text_yongtu02 ;
    //面积
    private EditText edit_mianji02 ;
    //使用年限
    private EditText edit_time02 ;
    //转让方式
    private TextView text_type04 ;
    //市场价格
    private EditText edit_market_price ;

    /**
     * 公共部分
     */
    // 转让价格
    private EditText edit_money ;
    // 有无相关证件
    private TextView text_zhengjian ;
    // 有无法律纠纷
    private TextView text_jiufen ;
    // 有无负债
    private TextView text_fuzhai ;
    // 有无抵押担保
    private TextView text_danbao ;
    // 是否拥有全部产权
    private TextView text_chanquan ;
    // 项目亮点1
    private TextView text_one ;
    // 项目亮点2
    private TextView text_two ;
    // 项目亮点3
    private TextView text_three ;
    // 项目亮点4
    private TextView text_four ;
    //文字描述
    private EditText edit_des ;
    //联系人姓名
    private EditText edit_name ;
    //联系人电话
    private EditText edit_phone ;
    private static boolean one = false ;
    private static boolean two = false ;
    private static boolean three = false ;
    private static boolean four = false ;
    private static boolean five = false ;
    /*************************************************录音和上传照片模块01********************************************/
    //录音倒计时30秒
    private int left_time = 30 ;
    //录音时的弹窗
    private PopupWindow window;
    //声音文件
    private File file_upload;
    //通过相机暂时存储的图片文件
    private File file;
    //发布时的图片1
    private File file_img01;
    //发布时的图片2
    private File file_img02;
    //发布时的图片3
    private File file_img03;
    //音频的时长
    private TextView release_audio_des_duration;
    //返回按钮
    private RelativeLayout pre;
    //语音描述页面
    private RelativeLayout release_audio_relative;
    //语音是否录制完毕
    private static Boolean isOK = true;
    //是否存在声音
    private Boolean hasVoice = false;
    private MusicPlayer mPlayer;
    private MediaRecorder mMediaRecorder;
    //音频缓存文件
    private File recAudioFile;
    //上划移动的Y轴距离
    private float DownY = 0;
    //按下的时间间隔
    private long currentMS;
    //录音按钮
    private Button release_audio_des;
    //语音撤销按钮
    private TextView release_audio_cancel;
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
    //intent 开启
    private static final int REQUEST_PERMISSION_SETTING = 0x002;
    //声音撤销按钮
    private TextView voice_cancel;
    /*************************************************录音和上传照片模块01********************************************/
    /*************************************************录音和上传照片模块02********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果判断语音框没有信息，点击这个布局出现。
        release_audio_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (release_audio_des.getVisibility() == View.GONE) {
                    release_audio_des.setVisibility(View.VISIBLE);
                }
            }
        });
        /***********************************************************获取图片的页面*************************/
        release_img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picGet01();
            }
        });
        release_img_cancel_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (release_frame_two.getVisibility() == View.VISIBLE) {
                    ToastUtils.shortToast(PublishFixedActivity.this, "无法删除第一张图片");
                } else {
                    release_frame_one.setVisibility(View.GONE);
                }
            }
        });
        release_img_cancel_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (release_frame_three.getVisibility() == View.VISIBLE) {
                    ToastUtils.shortToast(PublishFixedActivity.this, "无法删除第二张图片");
                } else {
                    release_frame_two.setVisibility(View.GONE);
                }
            }
        });
        release_img_cancel_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                release_frame_three.setVisibility(View.GONE);
                release_img_add.setVisibility(View.VISIBLE);
            }
        });
        /***********************************************************获取图片的页面*************************/
        /***************************************录音 上划取消*****************************************/
        //检查广播录制的权限
        if (ActivityCompat.checkSelfPermission(PublishFixedActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast(PublishFixedActivity.this, "请在管理中心，给予录音相应权限。");
        } else if (ActivityCompat.checkSelfPermission(PublishFixedActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast(PublishFixedActivity.this, "请在管理中心，给予使用相机权限。");
        } else {
            release_audio_des.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (ActivityCompat.checkSelfPermission(PublishFixedActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                        //进入App设置页面
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }else {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN: {
                                //scrollView.setFocusable(false);
                                andioGet();
                                Log.e("benben", "----------------------按下-----------------------");
                                DownY = event.getY();//float DownY
                                startRecorder();
                                currentMS = System.currentTimeMillis();//long currentMS 获取系统时间
                                break;
                            }
                            case MotionEvent.ACTION_MOVE: {
                                //scrollView.setFocusable(true);
                                float moveY = DownY - event.getY();//y轴距离
                                Log.e("benben", "" + moveY);
                                //int maxAmplitude = mMediaRecorder.getMaxAmplitude();
                                //Log.e("voice" , maxAmplitude + "" ) ;
                                if (moveY > 200) {
                                    Log.e("benben", "----------------------上划取消-----------------------");
                                    window.dismiss();
                                    isOK = false;
                                }
                                break;
                            }
                            case MotionEvent.ACTION_UP: {
                                //scrollView.setFocusable(true);
                                Log.e("benben", "----------------------抬起-----------------------");
                                long moveTime = System.currentTimeMillis() - currentMS;//移动时间
                                if (moveTime < 500) {
                                    release_audio_des.setVisibility(View.GONE);
                                    voice_cancel.setVisibility(View.GONE);
                                    release_audio_cancel.setVisibility(View.GONE);
                                    release_audio_des_duration.setText("");
                                    ToastUtils.shortToast(PublishFixedActivity.this, "时间太短，无法录制");
                                    window.dismiss();
                                    Thread t = new Thread(new MyRunnable());//这里比第一种创建线程对象多了个任务对象
                                    try {
                                        Thread.sleep(500);
                                        //release_audio_des.setVisibility(View.VISIBLE);
                                        t.start();
                                        //release_audio_des.setVisibility(View.GONE);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if (moveTime > 30000){
                                    //release_audio_des.setVisibility(View.GONE);
                                    //voice_cancel.setVisibility(View.GONE);
                                    //release_audio_cancel.setVisibility(View.GONE);
                                    //stopRecorder();
                                    ToastUtils.shortToast(PublishFixedActivity.this, "时间过长，无法继续录制");
                                    //release_audio_des_duration.setText("30");
                                    //window.dismiss();

                                    isOK = true;
                                    hasVoice = true;
                                    release_audio_cancel.setVisibility(View.VISIBLE);
                                    voice_cancel.setVisibility(View.VISIBLE);
                                    release_audio_des_duration.setText("30'");
                                    //final String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + "temp.amr";
                                    final String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + "temp.aac";
                                    file_upload = new File(path);
                                    //成功，加载数据
                                    release_audio_relative.setVisibility(View.VISIBLE);
                                    //点击播放按钮
                                    release_audio_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (isOK) {
                                                //播放录制的音频文件
                                                mPlayer = new MusicPlayer(PublishFixedActivity.this);
                                                String s = mPlayer.playMicFile(recAudioFile);
                                                Log.e("benbne", s);
                                                ToastUtils.shortToast(PublishFixedActivity.this, "正在播放音频");
                                            } else {
                                                ToastUtils.shortToast(PublishFixedActivity.this, "已经取消上传");
                                            }
                                        }
                                    });
                                    voice_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hasVoice = false;
                                            ToastUtils.shortToast(PublishFixedActivity.this, "撤销语音成功，请重新录制");
                                            voice_cancel.setVisibility(View.GONE);
                                            release_audio_cancel.setVisibility(View.GONE);
                                            release_audio_des_duration.setText("0'");
                                        }
                                    });
                                } else {
                                    isOK = true;
                                    hasVoice = true;
                                    release_audio_cancel.setVisibility(View.VISIBLE);
                                    voice_cancel.setVisibility(View.VISIBLE);
                                    release_audio_des_duration.setText(moveTime / 1000 + "'");
                                    stopRecorder();
                                    window.dismiss();
                                    //final String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + "temp.amr";
                                    final String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + "temp.aac";
                                    file_upload = new File(path);
                                    //成功，加载数据
                                    release_audio_relative.setVisibility(View.VISIBLE);
                                    //点击播放按钮
                                    release_audio_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (isOK) {
                                                //播放录制的音频文件
                                                mPlayer = new MusicPlayer(PublishFixedActivity.this);
                                                String s = mPlayer.playMicFile(recAudioFile);
                                                Log.e("benbne", s);
                                                ToastUtils.shortToast(PublishFixedActivity.this, "正在播放音频");
                                            } else {
                                                ToastUtils.shortToast(PublishFixedActivity.this, "已经取消上传");
                                            }
                                        }
                                    });
                                    voice_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hasVoice = false;
                                            ToastUtils.shortToast(PublishFixedActivity.this, "撤销语音成功，请重新录制");
                                            voice_cancel.setVisibility(View.GONE);
                                            release_audio_cancel.setVisibility(View.GONE);
                                            release_audio_des_duration.setText("0'");
                                        }
                                    });

                                }
                                break;
                            }
                            default:
                                Log.e("benben", "----------------------抬起-----------------------");
                                long moveTime = System.currentTimeMillis() - currentMS;//移动时间
                                if (moveTime < 500) {
                                    release_audio_des.setVisibility(View.GONE);
                                    voice_cancel.setVisibility(View.GONE);
                                    release_audio_cancel.setVisibility(View.GONE);
                                    release_audio_des_duration.setText("");
                                    ToastUtils.shortToast(PublishFixedActivity.this, "时间太短，无法录制");
                                    window.dismiss();
                                    Thread t = new Thread(new MyRunnable());//这里比第一种创建线程对象多了个任务对象
                                    try {
                                        Thread.sleep(500);
                                        t.start();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if (moveTime > 30000 ){
                                    //release_audio_des.setVisibility(View.GONE);
                                    //voice_cancel.setVisibility(View.GONE);
                                    //release_audio_cancel.setVisibility(View.GONE);
                                    //stopRecorder();
                                    ToastUtils.shortToast(PublishFixedActivity.this, "时间过长，无法继续录制");
                                    //release_audio_des_duration.setText("30");
                                    //window.dismiss();

                                    isOK = true;
                                    hasVoice = true;
                                    release_audio_cancel.setVisibility(View.VISIBLE);
                                    voice_cancel.setVisibility(View.VISIBLE);
                                    release_audio_des_duration.setText("30'");

                                    //final String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + "temp.amr";
                                    final String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + "temp.aac";
                                    file_upload = new File(path);
                                    //成功，加载数据
                                    release_audio_relative.setVisibility(View.VISIBLE);
                                    //点击播放按钮
                                    release_audio_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (isOK) {
                                                //播放录制的音频文件
                                                mPlayer = new MusicPlayer(PublishFixedActivity.this);
                                                String s = mPlayer.playMicFile(recAudioFile);
                                                Log.e("benbne", s);
                                                ToastUtils.shortToast(PublishFixedActivity.this, "正在播放音频");
                                            } else {
                                                ToastUtils.shortToast(PublishFixedActivity.this, "已经取消上传");
                                            }
                                        }
                                    });
                                    voice_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hasVoice = false;
                                            ToastUtils.shortToast(PublishFixedActivity.this, "撤销语音成功，请重新录制");
                                            voice_cancel.setVisibility(View.GONE);
                                            release_audio_cancel.setVisibility(View.GONE);
                                            release_audio_des_duration.setText("0'");
                                        }
                                    });
                                }else {
                                    isOK = true;
                                    hasVoice = true;
                                    release_audio_cancel.setVisibility(View.VISIBLE);
                                    voice_cancel.setVisibility(View.VISIBLE);
                                    release_audio_des_duration.setText(moveTime / 1000 + "'");
                                    stopRecorder();
                                    window.dismiss();
                                    //final String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + "temp.amr";
                                    final String path = SDUtil.getSDPath() + File.separator + "ziya" + File.separator + "temp.aac";
                                    file_upload = new File(path);
                                    //成功，加载数据
                                    release_audio_relative.setVisibility(View.VISIBLE);
                                    //点击播放按钮
                                    release_audio_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (isOK) {
                                                //播放录制的音频文件
                                                mPlayer = new MusicPlayer(PublishFixedActivity.this);
                                                String s = mPlayer.playMicFile(recAudioFile);
                                                Log.e("benbne", s);
                                                ToastUtils.shortToast(PublishFixedActivity.this, "正在播放音频");
                                            } else {
                                                ToastUtils.shortToast(PublishFixedActivity.this, "已经取消上传");
                                            }
                                        }
                                    });
                                    voice_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hasVoice = false;
                                            ToastUtils.shortToast(PublishFixedActivity.this, "撤销语音成功，请重新录制");
                                            release_audio_cancel.setVisibility(View.GONE);
                                            voice_cancel.setVisibility(View.GONE);
                                            release_audio_des_duration.setText("0'");
                                        }
                                    });

                                }
                                break;
                        }
                    }
                    return true;
                }
            });

        }
        /***************************************录音 上划取消****************************************/
    }
    /*************************************************录音和上传照片模块02********************************************/

    /*************************************************录音和上传照片模块03********************************************/

    public class MyRunnable implements Runnable {
        public void run() {
            //你需要实现的代码
            stopRecorder();
        }
    }

    private void startRecorder() {
        mMediaRecorder = new MediaRecorder();
        if (recAudioFile.exists()) {
            recAudioFile.delete();
        }

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setOutputFile(recAudioFile.getAbsolutePath());

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaRecorder.start();
    }

    private void stopRecorder() {
        if (recAudioFile != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
        }
    }

    private void andioGet() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_audio, null);
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button audio = (Button)view.findViewById(R.id.audio);

        final Timer timer = new Timer() ;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        left_time--;
                        if (left_time <= 5) {
                            audio.setText("剩余时间" + left_time + "s");
                            if (left_time == 0) {
                                timer.cancel();
                                audio.setText("录制时间过长");
                                stopRecorder();
                                window.dismiss();
                            }
                        }

                    }
                });
            }
        }, 1000, 1000) ;
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow的显示和消失动画
        //window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(view, Gravity.CENTER, 0, 0);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                left_time = 30;
                timer.cancel();
                //timer1.cancel();
            }
        });
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

                if (ActivityCompat.checkSelfPermission(PublishFixedActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.shortToast(PublishFixedActivity.this, "请在管理中心，给予相机相应权限。");
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
                    Toast.makeText(PublishFixedActivity.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PublishFixedActivity.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PublishFixedActivity.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if (resultCode == RESULT_OK && null != data) {
                    String result01 = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    text_part.setText(result01);
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
    /*************************************************录音和上传照片模块03********************************************/

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_publish_fixed);
    }

    public boolean isSubmit(){
        if (text_id.getText().toString().equals("请选择")) {
            ToastUtils.shortToast(PublishFixedActivity.this , "请选择您的身份");
            return false ;
        }
        if (text_part.getText().toString().equals("请选择")) {
            ToastUtils.shortToast(PublishFixedActivity.this , "请选择地区");
            return false ;
        }
        if (text_type.getText().toString().equals("请选择")) {
            ToastUtils.shortToast(PublishFixedActivity.this , "请选择标的物类型");
            return false ;
        }
        if (text_type.getText().toString().equals("土地")) {
            if (text_yongtu.getText().toString().equals("请选择")) {
                ToastUtils.shortToast(PublishFixedActivity.this , "请选择规划用途");
                return false ;
            }
            if (TextUtils.isEmpty(edit_big.getText().toString().trim())){
                ToastUtils.shortToast(PublishFixedActivity.this , "请输入面积");
                return false ;
            }
            if (TextUtils.isEmpty(edit_time.getText().toString().trim())){
                ToastUtils.shortToast(PublishFixedActivity.this , "请输入剩余使用年限");
                return false ;
            }
            if (text_type02.getText().toString().equals("请选择")) {
                ToastUtils.shortToast(PublishFixedActivity.this , "请选择转让方式");
                return false ;
            }
        }
        if (text_type.getText().toString().equals("房产")) {

            if (text_type03.getText().toString().equals("请选择")) {
                ToastUtils.shortToast(PublishFixedActivity.this , "请选择房产类型");
                return false ;
            }
            if (text_yongtu02.getText().toString().equals("请选择")) {
                ToastUtils.shortToast(PublishFixedActivity.this , "请选择规划用途");
                return false ;
            }
            if (TextUtils.isEmpty(edit_mianji02.getText().toString().trim())){
                ToastUtils.shortToast(PublishFixedActivity.this , "请输入面积");
                return false ;
            }
            if (TextUtils.isEmpty(edit_time02.getText().toString().trim())){
                ToastUtils.shortToast(PublishFixedActivity.this , "请输入剩余使用年限");
                return false ;
            }
            if (text_type04.getText().toString().equals("请选择")) {
                ToastUtils.shortToast(PublishFixedActivity.this , "请选择转让方式");
                return false ;
            }
            if (TextUtils.isEmpty(edit_market_price.getText().toString().trim())){
                ToastUtils.shortToast(PublishFixedActivity.this , "请输入市场价格");
                return false ;
            }
        }
        if (TextUtils.isEmpty(edit_money.getText().toString().trim())){
            ToastUtils.shortToast(PublishFixedActivity.this , "请输入转让价格");
            return false ;
        }
        if (TextUtils.isEmpty(edit_des.getText().toString().trim())){
            ToastUtils.shortToast(PublishFixedActivity.this , "请输入文字描述");
            return false ;
        }
        if (release_frame_one.getVisibility() != View.VISIBLE) {
            ToastUtils.shortToast(PublishFixedActivity.this , "请至少添加一张图片");
            return false ;
        }
        if (TextUtils.isEmpty(edit_name.getText().toString().trim())){
            ToastUtils.shortToast(PublishFixedActivity.this , "请输入联系人姓名");
            return false ;
        }
        if (TextUtils.isEmpty(edit_phone.getText().toString().trim())){
            ToastUtils.shortToast(PublishFixedActivity.this , "请输入联系方式");
            return false ;
        }
        if (!edit_phone.getText().toString().trim().matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$")){
            ToastUtils.longToast(PublishFixedActivity.this, "请输入正确的手机号码");
            return false ;
        }
        return true ;
    }

    @Override
    public void initViews() {
        info_weituo = (TextView)findViewById(R.id.info_weituo ) ;
        edit_des = (EditText)findViewById(R.id.edit_des ) ;
        edit_name = (EditText)findViewById(R.id.edit_name ) ;
        edit_phone = (EditText)findViewById(R.id.edit_phone ) ;

        publish = (Button)findViewById(R.id.publish ) ;
        linear01 = (LinearLayout)findViewById(R.id.linear01 ) ; 
        linear02 = (LinearLayout)findViewById(R.id.linear02 ) ; 
        pre = (RelativeLayout)findViewById(R.id.pre ) ;
        scrollView = (ScrollView)findViewById(R.id.scrollView ) ;
        text_id = (TextView)findViewById(R.id.text_id ) ;
        text_type = (TextView)findViewById(R.id.text_type ) ;
        text_part = (TextView)findViewById(R.id.text_part ) ;
        text_yongtu = (TextView)findViewById(R.id.text_yongtu ) ;
        edit_time = (EditText)findViewById(R.id.edit_time ) ;
        edit_big = (EditText)findViewById(R.id.edit_big ) ;
        text_type02 = (TextView)findViewById(R.id.text_type02 ) ;
        text_type04 = (TextView)findViewById(R.id.text_type04 ) ;
        text_type03 = (TextView)findViewById(R.id.text_type03 ) ;
        text_yongtu02 = (TextView)findViewById(R.id.text_yongtu02 ) ;
        edit_mianji02 = (EditText)findViewById(R.id.edit_mianji02 ) ;
        edit_time02 = (EditText)findViewById(R.id.edit_time02 ) ;
        edit_market_price = (EditText)findViewById(R.id.edit_market_price ) ;
        edit_money = (EditText)findViewById(R.id.edit_money ) ;
        text_zhengjian = (TextView)findViewById(R.id.text_zhengjian ) ;
        text_jiufen = (TextView)findViewById(R.id.text_jiufen ) ;
        text_fuzhai = (TextView)findViewById(R.id.text_fuzhai ) ;
        text_danbao = (TextView)findViewById(R.id.text_danbao ) ;
        text_chanquan = (TextView)findViewById(R.id.text_chanquan ) ;
        text_one = (TextView)findViewById(R.id.text_one ) ;
        text_two = (TextView)findViewById(R.id.text_two ) ;
        text_three = (TextView)findViewById(R.id.text_three ) ;
        text_four = (TextView)findViewById(R.id.text_four ) ;

        edit_big.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        InputFilter[] filters = {new CashierInputFilter()};
        edit_big.setFilters(filters);

        edit_money.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        edit_money.setFilters(filters);

        edit_mianji02.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        edit_mianji02.setFilters(filters);

        edit_market_price.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        edit_market_price.setFilters(filters);

        /*************************************************录音和上传照片模块04********************************************/
        release_audio_des_duration = (TextView) findViewById(R.id.release_audio_des_duration);
        voice_cancel = (TextView) findViewById(R.id.voice_cancel);
        pre = (RelativeLayout) findViewById(R.id.pre);
        release_audio_relative = (RelativeLayout) findViewById(R.id.release_audio_relative);
        release_audio_des = (Button) findViewById(R.id.release_audio_des);
        release_audio_cancel = (TextView) findViewById(R.id.release_audio_cancel);
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
        text_part = (TextView) findViewById(R.id.text_part);
        // 当文件不存在的时候创建文件
        String path = SDUtil.getSDPath() + File.separator + "ziya";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        recAudioFile = new File(SDUtil.getSDPath() + File.separator + "ziya", "temp.aac");
        /*************************************************录音和上传照片模块04********************************************/
    }

    @Override
    public void initListeners() {
        publish.setOnClickListener(this) ;
        pre.setOnClickListener(this);
        text_id.setOnClickListener(this);
        text_type.setOnClickListener(this);
        text_part.setOnClickListener(this);
        text_yongtu.setOnClickListener(this);
        text_type02.setOnClickListener(this);
        text_type04.setOnClickListener(this);
        text_type03.setOnClickListener(this);
        text_yongtu02.setOnClickListener(this);
        text_zhengjian.setOnClickListener(this);
        text_jiufen.setOnClickListener(this);
        text_fuzhai.setOnClickListener(this);
        text_danbao.setOnClickListener(this);
        text_chanquan.setOnClickListener(this);
        text_one.setOnClickListener(this);
        text_two.setOnClickListener(this);
        text_three.setOnClickListener(this);
        text_four.setOnClickListener(this);
        info_weituo.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    private void loadData() {
        //展示数据加载框
        showBenDialog() ;
        String urls = String.format(Url.V2Publish, GetBenSharedPreferences.getTicket(this)) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("Identity" , text_id.getText().toString() );
        params.addBodyParameter("Promise", Promise);
        params.addBodyParameter("ProArea", text_part.getText().toString());
        params.addBodyParameter("AssetType", text_type.getText().toString());
        if ("土地".equals(text_type.getText().toString())){
            params.addBodyParameter("TypeID" , "16");
            params.addBodyParameter("Usefor" , text_yongtu.getText().toString() );
            params.addBodyParameter("Area" , edit_big.getText().toString() );
            params.addBodyParameter("Year" , edit_time.getText().toString() );
            params.addBodyParameter("TransferType" , text_type02.getText().toString() );

        }
        if ("房产".equals(text_type.getText().toString())){
            params.addBodyParameter("TypeID" , "12");
            params.addBodyParameter("Type" , text_type03.getText().toString() );
            params.addBodyParameter("Usefor" , text_yongtu02.getText().toString() );
            params.addBodyParameter("Area" , edit_mianji02.getText().toString() );

            params.addBodyParameter("Year" , edit_time02.getText().toString() );
            params.addBodyParameter("TransferType" , text_type04.getText().toString() );
            params.addBodyParameter("MarketPrice" , edit_market_price.getText().toString() );
        }
        params.addBodyParameter("TransferMoney", edit_money.getText().toString());

        params.addBodyParameter("Credentials", "请选择".equals(text_zhengjian.getText().toString().trim()) ? "" : text_zhengjian.getText().toString().trim());
        params.addBodyParameter("Dispute", "请选择".equals(text_jiufen.getText().toString().trim()) ? "" : text_jiufen.getText().toString().trim());
        params.addBodyParameter("Debt", "请选择".equals(text_fuzhai.getText().toString().trim()) ? "" : text_fuzhai.getText().toString().trim());
        params.addBodyParameter("Guaranty", "请选择".equals(text_danbao.getText().toString().trim()) ? "" : text_danbao.getText().toString().trim());
        params.addBodyParameter("Property", "请选择".equals(text_chanquan.getText().toString().trim()) ? "" : text_chanquan.getText().toString().trim());
        String proLabel = "" ;
        if (one){
            proLabel = proLabel + text_one.getText().toString() + "," ;
        }
        if (two){
            proLabel = proLabel + text_two.getText().toString() + "," ;
        }
        if (three){
            proLabel = proLabel + text_three.getText().toString() + ","  ;
        }
        if (four){
            proLabel = proLabel + text_four.getText().toString() + "," ;
        }
        params.addBodyParameter("ProLabel", proLabel);
        params.addBodyParameter("WordDes", edit_des.getText().toString().trim());
        if (release_frame_one.getVisibility() == View.VISIBLE) {
            params.addBodyParameter("PictureDes1", file_img01);
            if (release_frame_two.getVisibility() == View.VISIBLE) {
                params.addBodyParameter("PictureDes2", file_img02);
                if (release_frame_three.getVisibility() == View.VISIBLE) {
                    params.addBodyParameter("PictureDes3", file_img03);
                }
            }
        }
        if (hasVoice) {
            params.addBodyParameter("VoiceDes", file_upload);
        }
        params.addBodyParameter("ConnectPerson", edit_name.getText().toString().trim());
        params.addBodyParameter("ConnectPhone", edit_phone.getText().toString().trim());
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //隐藏dialog
                hiddenBenDialog();
                Log.e("V2Publish", responseInfo.result);
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    String status_code = object.getString("status_code");
                    switch (status_code) {
                        case "200":
                            ToastUtils.shortToast(PublishFixedActivity.this, "发布成功");
                            finish();
                            break;
                        default:
                            ToastUtils.shortToast(PublishFixedActivity.this, "发布失败");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //隐藏dialog
                hiddenBenDialog();
                error.printStackTrace();
                ToastUtils.shortToast(PublishFixedActivity.this, "网络连接异常");
            }
        }) ;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish :
                if (isSubmit()){
                    pop() ;
                }
                break;
            case R.id.pre :
                finish();
                break;
            case R.id.text_id :
                popUpWindow(text_id);
                break;
            case R.id.text_type :
                popUpWindow(text_type);
                break;
            case R.id.text_part :
                startActivityForResult(new Intent(PublishFixedActivity.this, PartActivity.class), 4);
                break;
            case R.id.text_yongtu :
                popUpWindow(text_yongtu);
                break;
            case R.id.text_type02 :
                popUpWindow(text_type02);
                break;
            case R.id.text_type03 :
                popUpWindow(text_type03);
                break;
            case R.id.text_type04 :
                popUpWindow(text_type04);
                break;
            case R.id.text_yongtu02 :
                popUpWindow(text_yongtu02);
                break;
            case R.id.text_zhengjian :
                popUpWindow(text_zhengjian);
                break;
            case R.id.text_jiufen :
                popUpWindow(text_jiufen);
                break;
            case R.id.text_fuzhai :
                popUpWindow(text_fuzhai);
                break;
            case R.id.text_danbao :
                popUpWindow(text_danbao);
                break;
            case R.id.text_chanquan :
                popUpWindow(text_chanquan);
                break;
            case R.id.text_one :
                showView(text_one) ;
                break;
            case R.id.text_two :
                showView(text_two ) ;
                break;
            case R.id.text_three :
                showView(text_three ) ;
                break;
            case R.id.text_four :
                showView(text_four ) ;
                break;
            case R.id.info_weituo :
                ToOtherPublish() ;
                break;
            default:
                break;
        }
    }

    private void pop() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_chengnuo, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ImageButton pay_cancel = (ImageButton)view.findViewById(R.id.pay_cancel ) ;
        final TextView submit = (TextView)view.findViewById(R.id.submit ) ;
        TextView cancel = (TextView)view.findViewById(R.id.cancel ) ;
        pay_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Promise = "承诺" ;
                loadData();
                window.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Promise = "不承诺" ;
                loadData();
                window.dismiss();
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(scrollView, Gravity.CENTER, 0, 0);
        // 设置popWindow的显示和消失动画

        backgroundAlpha(0.2f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void ToOtherPublish() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_other_publish, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final EditText person = (EditText)view.findViewById(R.id.person ) ;
        final EditText phone = (EditText)view.findViewById(R.id.phone ) ;
        ImageButton pay_cancel = (ImageButton)view.findViewById(R.id.pay_cancel ) ;
        final TextView submit = (TextView)view.findViewById(R.id.submit ) ;
        TextView cancel = (TextView)view.findViewById(R.id.cancel ) ;
        pay_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOtherSubmit(person , phone)){
                    submit.setEnabled(false);
                    loadOtherPublish(person, phone , submit) ;
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(scrollView, Gravity.CENTER, 0, 0);
        // 设置popWindow的显示和消失动画

        backgroundAlpha(0.2f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void loadOtherPublish(EditText person, EditText phone, final TextView submit) {
        showBenDialog() ;
        String urls = String.format(Url.V2OtherPublish, GetBenSharedPreferences.getTicket(this)) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("TypeID" , "16");
        params.addBodyParameter("ConnectPerson" , person.getText().toString().trim() );
        params.addBodyParameter("ConnectPhone" , phone.getText().toString().trim() );
        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //隐藏dialog
                hiddenBenDialog();
                Log.e("V2OtherPublish", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result) ;
                    String status_code = jsonObject.getString("status_code");
                    switch (status_code){
                        case "200" :
                            submit.setEnabled(true);
                            ToastUtils.longToast(PublishFixedActivity.this , "委托发布成功,请您耐心等待资芽网客服与您联系。");
                            finish();
                            break;
                        default:
                            submit.setEnabled(true);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //隐藏dialog
                hiddenBenDialog();
                submit.setEnabled(true);
                error.printStackTrace();
                ToastUtils.shortToast(PublishFixedActivity.this, "网络连接异常");
            }
        }) ;
    }

    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        /* 显示ProgressDialog */
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(PublishFixedActivity.this , "数据提交中，请稍后。。。");
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

    private boolean isOtherSubmit(EditText person, EditText phone) {
        if (TextUtils.isEmpty(person.getText().toString().trim())){
            ToastUtils.shortToast(PublishFixedActivity.this , "请输入联系人姓名");
            return false ;
        }
        if (TextUtils.isEmpty(phone.getText().toString().trim())){
            ToastUtils.shortToast(PublishFixedActivity.this , "请输入联系电话");
            return false ;
        }
        return true ;
    }

    private void showView(TextView textView ) {
        switch (textView.getId()){
            case R.id.text_one :
                if (one){
                    one = false ;
                    textView.setTextColor(Color.rgb(0, 0, 0));
                    textView.setSelected(false);
                }else {
                    one = true ;
                    textView.setTextColor(Color.rgb(253,208,0));
                    textView.setSelected(true);
                }
                break;
            case R.id.text_two :
                if (two){
                    two = false ;
                    textView.setTextColor(Color.rgb(0,0,0));
                    textView.setSelected(false);
                }else {
                    two = true ;
                    textView.setTextColor(Color.rgb(253,208,0));
                    textView.setSelected(true);
                }
                break;
            case R.id.text_three :
                if (three){
                    three = false ;
                    textView.setTextColor(Color.rgb(0,0,0));
                    textView.setSelected(false);
                }else {
                    three = true ;
                    textView.setTextColor(Color.rgb(253,208,0));
                    textView.setSelected(true);
                }
                break;
            case R.id.text_four :
                if (four){
                    four = false ;
                    textView.setTextColor(Color.rgb(0,0,0));
                    textView.setSelected(false);
                }else {
                    four = true ;
                    textView.setTextColor(Color.rgb(253,208,0));
                    textView.setSelected(true);
                }
                break;
            case R.id.text_five :
                if (five){
                    five = false ;
                    textView.setTextColor(Color.rgb(0,0,0));
                    textView.setSelected(false);
                }else {
                    five = true ;
                    textView.setTextColor(Color.rgb(253,208,0));
                    textView.setSelected(true);
                }
                break;
            default:
                break;
        }
    }

    private void popUpWindow(final TextView tv ) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.common_window_wheel, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final WheelView picker= (WheelView) view.findViewById(R.id.wheel);
        final WheelView picker02= (WheelView) view.findViewById(R.id.wheel02);
        TextView left = (TextView) view.findViewById(R.id.left);
        TextView right = (TextView) view.findViewById(R.id.right);
        picker02.setVisibility(View.GONE);
        switch (tv.getId()){
            case R.id.text_id :
                picker.addData("项目持有者");
                picker.addData("FA（中介）");
                break;
            case R.id.text_type :
                picker.addData("土地");
                picker.addData("房产");
                break;
            case R.id.text_part :
                picker.addData("全国");
                picker.addData("北京市");
                picker.addData("天津市");
                picker.addData("河北省");
                picker.addData("山西省");
                picker.addData("内蒙古");
                picker.addData("辽宁省");
                picker.addData("吉林省");
                picker.addData("黑龙江省");
                picker.addData("上海市");
                picker.addData("江苏省");
                picker.addData("浙江省");
                picker.addData("安徽省");
                picker.addData("福建省");
                picker.addData("江西省");
                picker.addData("山东省");
                picker.addData("河南省");
                picker.addData("湖北省");
                picker.addData("湖南省");
                picker.addData("广东省");
                picker.addData("广西");
                picker.addData("海南省");
                picker.addData("重庆市");
                picker.addData("四川省");
                picker.addData("贵州省");
                picker.addData("云南省");
                picker.addData("西藏");
                picker.addData("陕西省");
                picker.addData("甘肃省");
                picker.addData("青海省");
                picker.addData("宁夏");
                picker.addData("新疆");
                picker.addData("台湾省");
                picker.addData("香港");
                picker.addData("澳门");
                break;
            case R.id.text_yongtu :
                picker.addData("工业");
                picker.addData("商业");
                picker.addData("住宅");
                picker.addData("其他");
                break;
            case R.id.text_type02 :
                picker.addData("产权转让");
                picker.addData("股权转让");
                break;
            case R.id.text_type03 :
                picker.addData("住宅");
                picker.addData("商业");
                picker.addData("厂房");
                picker.addData("其他");
                break;
            case R.id.text_type04 :
                picker.addData("产权转让");
                picker.addData("股权转让");
                break;
            case R.id.text_yongtu02 :
                picker.addData("工业");
                picker.addData("商业");
                picker.addData("住宅");
                picker.addData("其他");
                break;
            case R.id.text_zhengjian :
            case R.id.text_jiufen :
            case R.id.text_fuzhai :
            case R.id.text_danbao :
            case R.id.text_chanquan :
                picker.addData("是");
                picker.addData("否");
                break;
            default:
                break;
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object centerItem = picker.getCenterItem();
                if (tv.getId() == text_type.getId()){
                    tv.setText(centerItem.toString());
                    if ("土地".equals(centerItem.toString())){
                        linear01.setVisibility(View.VISIBLE);
                        linear02.setVisibility(View.GONE);
                    }else {
                        linear01.setVisibility(View.GONE);
                        linear02.setVisibility(View.VISIBLE);
                    }
                }else {
                    tv.setText(centerItem.toString());
                }
                window.dismiss();
            }
        });
        //public void setLineColor(int lineColor);            //设置中间线条的颜色
        //public void setTextColor(int textColor);            //设置文字的颜色
        //public void setTextSize(float textSize);
        // 设置文字大小
        picker.setCenterItem(0);
        picker.setTextColor(Color.rgb(249, 144, 0));
        picker.setLineColor(Color.rgb(249, 144, 0)) ;
        picker02.setCenterItem(10);
        picker02.setCircle(true);
        picker02.setTextColor(Color.rgb(249, 144, 0));
        picker02.setLineColor(Color.rgb(249, 144, 0) ) ;
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
}
