package com.ziyawang.ziya.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.CashierInputFilter;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.MusicPlayer;
import com.ziyawang.ziya.tools.SDUtil;
import com.ziyawang.ziya.tools.SystemBarTintManager;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.MyProgressDialog;

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

public class ReleaseDetailsActivity extends BenBenActivity implements View.OnClickListener {

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
    //发布的title
    private TextView title ;
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
    //类型区域
    private RelativeLayout relative_type;
    //类型
    private TextView textView_type;
    //来源区域
    private RelativeLayout relative_from;
    //来源
    private TextView text_from;
    //地区区域
    private RelativeLayout relative_part;
    //地区
    private TextView text_part;
    //地区的文字
    private TextView release_text_part;
    //发布的内容描述
    private EditText release_edit;
    //发布的类型
    private static String title_t;
    //类型区域的显示文字
    private TextView release_one;
    //总金额区域
    private RelativeLayout relative_total;
    //总金额文字
    private TextView text_total;
    //总金额文字编辑区域
    private EditText total_money;
    //转让价区域
    private RelativeLayout relative_trans;
    //转让价文字
    private TextView text_trans;
    //来源文字
    private TextView text_from_a;
    //转让价文字编辑区域
    private EditText trans_edit;
    //佣金区域 状态区域
    private RelativeLayout relative_yognjin, relative_status;
    //佣金 状态
    private TextView yognjin, text_yongjin, text_status ;

    private RelativeLayout relative_head01;
    private TextView head01;
    private TextView linearLayout_release;
    //ticket
    private static String login;
    //是否登录状态
    private static boolean isLogin;
    //dialog
    private MyProgressDialog dialog;
    //第一个文字
    private TextView release_wan;
    //第二个文字
    private TextView release_wan01;
    //声音撤销按钮
    private TextView voice_cancel;
    //清单上传区域
    private RelativeLayout info_upload;
    //intent 开启
    private static final int REQUEST_PERMISSION_SETTING = 0x002;

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("发布信息页面");
        //统计时长
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("发布信息页面");
        //统计时长
        MobclickAgent.onPause(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //添加对页面的回退事件
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击PC提示
        info_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请到pc端进行上传");
            }
        });

        //获得地区的分类
        relative_part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ReleaseDetailsActivity.this, PartActivity.class), 6);
            }
        });

        //如果判断语音框没有信息，点击这个布局出现。
        release_audio_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (release_audio_des.getVisibility() == View.GONE ){
                    release_audio_des.setVisibility(View.VISIBLE);
                }
            }
        });
        //根据title拿到不同的类型
        switch (title_t) {
            case "资产包转让":
                //获得资产包的类型
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "资产包类型");
                        intent.putExtra("type", "1");
                        startActivityForResult(intent, 4);
                    }
                });
                //获得来源的类型
                relative_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, FromWhereActivity.class);
                        intent.putExtra("title", "来源");
                        intent.putExtra("type", "1");
                        startActivityForResult(intent, 5);
                    }
                });
                info_upload.setVisibility(View.VISIBLE);
                break;

            case "债权转让":
                relative_from.setVisibility(View.GONE);
                release_one.setText("类型");
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "类型");
                        intent.putExtra("type", "2");
                        startActivityForResult(intent, 4);
                    }
                });
                break;
            case "固产转让":
                release_one.setText("类型");
                relative_total.setVisibility(View.GONE);
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "类型");
                        intent.putExtra("type", "3");
                        startActivityForResult(intent, 4);
                    }
                });
                relative_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, FromWhereActivity.class);
                        intent.putExtra("title", "标的物");
                        intent.putExtra("type", "8");
                        startActivityForResult(intent, 5);
                    }
                });
                text_from_a.setText("标的物");
                break;
            case "商业保理":
                trans_edit.setHint("请输入金额");
                release_one.setText("买方性质");
                relative_from.setVisibility(View.GONE);
                relative_total.setVisibility(View.GONE);
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "买方性质");
                        intent.putExtra("type", "4");
                        startActivityForResult(intent, 4);
                    }
                });
                text_trans.setText("合同金额");
                break;
            case "典当信息":
                relative_head01.setVisibility(View.VISIBLE);
                relative_type.setVisibility(View.GONE);
                relative_from.setVisibility(View.GONE);
                relative_total.setVisibility(View.GONE);
                text_trans.setText("金额");
                trans_edit.setHint("请输入金额");
                break;
            case "担保信息":
                trans_edit.setHint("请输入金额");
                relative_type.setVisibility(View.GONE);
                release_one.setText("类型");
                relative_head01.setVisibility(View.VISIBLE);
                head01.setText("担保");
                relative_from.setVisibility(View.GONE);
                relative_total.setVisibility(View.GONE);
                text_trans.setText("金额");
                break;
            case "融资需求":
                trans_edit.setHint("可接受的月化信息");
                release_one.setText("方式");
                relative_from.setVisibility(View.GONE);
                text_total.setText("金额");
                text_trans.setText("回报率");
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "方式");
                        intent.putExtra("type", "6");
                        startActivityForResult(intent, 4);
                    }
                });
                relative_from.setVisibility(View.GONE);
                release_wan.setText("%");
                break;
            case "悬赏信息":
                release_one.setText("类型");
                relative_from.setVisibility(View.GONE);
                relative_total.setVisibility(View.GONE);
                text_trans.setText("金额");
                trans_edit.setHint("悬赏佣金");
                release_text_part.setHint("目标地区");
                release_wan.setText("元");
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "类型");
                        intent.putExtra("type", "7");
                        startActivityForResult(intent, 4);
                    }
                });
                relative_from.setVisibility(View.GONE);
                break;
            case "尽职调查":
                release_text_part.setHint("目标地区");
                release_one.setText("类型");
                relative_total.setVisibility(View.GONE);
                relative_trans.setVisibility(View.GONE);
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "类型");
                        intent.putExtra("type", "8");
                        startActivityForResult(intent, 4);
                    }
                });
                relative_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, FromWhereActivity.class);
                        intent.putExtra("title", "来源");
                        intent.putExtra("type", "2");
                        startActivityForResult(intent, 5);
                    }
                });
                text_from_a.setText("被调查方");
                break;

            case "委外催收":
                release_one.setText("类型");
                relative_from.setVisibility(View.GONE);
                relative_total.setVisibility(View.GONE);
                text_trans.setText("金额");
                trans_edit.setHint("请输入");
                relative_yognjin.setVisibility(View.VISIBLE);
                relative_yognjin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, FromWhereActivity.class);
                        intent.putExtra("title", "佣金比例");
                        intent.putExtra("type", "6");
                        startActivityForResult(intent, 7);

                    }
                });
                relative_status.setVisibility(View.VISIBLE);
                relative_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, FromWhereActivity.class);
                        intent.putExtra("title", "状态");
                        intent.putExtra("type", "7");
                        startActivityForResult(intent, 8);
                    }
                });
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "类型");
                        intent.putExtra("type", "9");
                        startActivityForResult(intent, 4);
                    }
                });
                relative_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, FromWhereActivity.class);
                        intent.putExtra("title", "状态");
                        intent.putExtra("type", "3");
                        startActivityForResult(intent, 5);
                    }
                });
                text_from_a.setText("佣金比例");
                text_trans.setText("金额");
                break;
            case "法律服务":
                release_one.setText("类型");
                relative_total.setVisibility(View.GONE);
                relative_trans.setVisibility(View.GONE);
                text_from_a.setText("需求");
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "民事");
                        intent.putExtra("type", "10");
                        startActivityForResult(intent, 4);
                    }
                });
                relative_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, FromWhereActivity.class);
                        intent.putExtra("title", "需求");
                        intent.putExtra("type", "4");
                        startActivityForResult(intent, 5);
                    }
                });
                break;
            case "资产求购":
                release_one.setText("类型");
                relative_total.setVisibility(View.GONE);
                relative_trans.setVisibility(View.GONE);
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "类型");
                        intent.putExtra("type", "11");
                        startActivityForResult(intent, 4);
                    }
                });
                relative_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, FromWhereActivity.class);
                        intent.putExtra("title", "求购方");
                        intent.putExtra("type", "5");
                        startActivityForResult(intent, 5);
                    }
                });
                text_from_a.setText("求购方");
                break;
            case "投资需求":
                release_one.setText("投资类型");
                relative_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, MoneyTypeActivity.class);
                        intent.putExtra("title", "投资类型");
                        intent.putExtra("type", "12");
                        startActivityForResult(intent, 4);
                    }
                });
                relative_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, FromWhereActivity.class);
                        intent.putExtra("title", "投资方式");
                        intent.putExtra("type", "9");
                        startActivityForResult(intent, 5);
                    }
                });
                text_from_a.setText("投资方式");
                text_total.setText("预期回报率");
                //text_trans.setText("投资期限");
                release_wan01.setText("%");
                //release_wan.setText("年");
                total_money.setHint("请输入回报率");
                //trans_edit.setHint("输入投资期限");
                relative_trans.setVisibility(View.GONE);
                relative_yognjin.setVisibility(View.VISIBLE);
                yognjin.setText("投资期限");
                relative_yognjin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReleaseDetailsActivity.this, FromWhereActivity.class);
                        intent.putExtra("title", "投资期限");
                        intent.putExtra("type", "10");
                        startActivityForResult(intent, 7);

                    }
                });
                break;

        }
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
                if (release_frame_two.getVisibility() == View.VISIBLE && release_frame_three.getVisibility() == View.VISIBLE) {
                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "无法删除第一张图片");
                } else {
                    release_frame_one.setVisibility(View.GONE);
                }
            }
        });
        release_img_cancel_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (release_frame_three.getVisibility() == View.VISIBLE) {
                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "无法删除第二张图片");
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
        if (ActivityCompat.checkSelfPermission(ReleaseDetailsActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请在管理中心，给予录音相应权限。");
        } else if (ActivityCompat.checkSelfPermission(ReleaseDetailsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请在管理中心，给予使用相机权限。");
        } else {
            release_audio_des.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (ActivityCompat.checkSelfPermission(ReleaseDetailsActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
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
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "时间太短，无法录制");
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
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "时间过长，无法继续录制");
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
                                                mPlayer = new MusicPlayer(ReleaseDetailsActivity.this);
                                                String s = mPlayer.playMicFile(recAudioFile);
                                                Log.e("benbne", s);
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "正在播放音频");
                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "已经取消上传");
                                            }
                                        }
                                    });
                                    voice_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hasVoice = false;
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "撤销语音成功，请重新录制");
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
                                                mPlayer = new MusicPlayer(ReleaseDetailsActivity.this);
                                                String s = mPlayer.playMicFile(recAudioFile);
                                                Log.e("benbne", s);
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "正在播放音频");
                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "已经取消上传");
                                            }
                                        }
                                    });
                                    voice_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hasVoice = false;
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "撤销语音成功，请重新录制");
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
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "时间太短，无法录制");
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
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "时间过长，无法继续录制");
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
                                                mPlayer = new MusicPlayer(ReleaseDetailsActivity.this);
                                                String s = mPlayer.playMicFile(recAudioFile);
                                                Log.e("benbne", s);
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "正在播放音频");
                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "已经取消上传");
                                            }
                                        }
                                    });
                                    voice_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hasVoice = false;
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "撤销语音成功，请重新录制");
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
                                                mPlayer = new MusicPlayer(ReleaseDetailsActivity.this);
                                                String s = mPlayer.playMicFile(recAudioFile);
                                                Log.e("benbne", s);
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "正在播放音频");
                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "已经取消上传");
                                            }
                                        }
                                    });
                                    voice_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            hasVoice = false;
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "撤销语音成功，请重新录制");
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

        //对发布按钮的监听
        linearLayout_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否登录
                if (isLogin) {

                    final String url = String.format(Url.ReleaseInfo, login);

                    //买方性质
                    final String s1 = textView_type.getText().toString().trim();

                    //合同金额
                    final String s2 = trans_edit.getText().toString().trim();
                    //地区
                    final String s3 = text_part.getText().toString().trim();
                    //文字描述
                    final String s4 = release_edit.getText().toString().trim();

                    //来源
                    String s = text_from.getText().toString().trim();

                    //总金额
                    String s5 = total_money.getText().toString().trim();

                    //佣金
                    String s6 = text_yongjin.getText().toString().trim();

                    //状态
                    String s7 = text_status.getText().toString().trim();

                    Log.e("benben", login);

                    switch (title_t) {
                        /***************************************发布 资产求购******************************************/
                        case "资产求购":

                            // s1 , s , s3 , s4
                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s) && !s.equals("请选择")) {
                                    if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                        if (!TextUtils.isEmpty(s4)) {

                                            //if (release_frame_one.getVisibility() == View.VISIBLE) {

                                                dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                                dialog.show();
                                                HttpUtils utils = new HttpUtils();
                                                RequestParams params = new RequestParams();
                                                params.addBodyParameter("ProArea", s3);
                                                params.addBodyParameter("WordDes", s4);
                                                params.addBodyParameter("TypeID", "13");
                                                params.addBodyParameter("Buyer", s);
                                                params.addBodyParameter("AssetType", s1);
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
                                                utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                    @Override
                                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                                        if (dialog != null) {
                                                            dialog.dismiss();
                                                        }
                                                        Log.e("benbne", responseInfo.result);
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                            String status_code = jsonObject.getString("status_code");
                                                            switch (status_code) {
                                                                case "200":
                                                                    final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                    builder01.setTitle("发布成功，等待审核。");
                                                                    builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                    builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            finish();
                                                                        }
                                                                    });
                                                                    builder01.create().show();

                                                                    break;
                                                                case "499":
                                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                        if (dialog != null) {
                                                            dialog.dismiss();
                                                        }
                                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                        error.printStackTrace();
                                                        Log.e("benbne", msg);
                                                    }
                                                });

//                                            } else {
//                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请至少添加一张图片");
//                                            }


                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请添加文字描述");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                    }
                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择求购方");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择资产求购的类型");
                            }

                            break;
                        /***************************************发布 法律服务******************************************/
                        case "法律服务":
                            // s1 s s3 s4

                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s) && !s.equals("请选择")) {
                                    if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                        if (!TextUtils.isEmpty(s4)) {

                                            dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                            dialog.show();
                                            HttpUtils utils = new HttpUtils();
                                            RequestParams params = new RequestParams();
                                            params.addBodyParameter("ProArea", s3);
                                            params.addBodyParameter("WordDes", s4);
                                            params.addBodyParameter("TypeID", "03");
                                            params.addBodyParameter("Requirement", s);
                                            params.addBodyParameter("AssetType", s1);
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
                                            utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                @Override
                                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    Log.e("benbne", responseInfo.result);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                        String status_code = jsonObject.getString("status_code");
                                                        switch (status_code) {
                                                            case "200":
                                                                final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                builder01.setTitle("发布成功，等待审核。");
                                                                builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        finish();
                                                                    }
                                                                });
                                                                builder01.create().show();
                                                                break;
                                                            case "499":
                                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                    error.printStackTrace();
                                                    Log.e("benbne", msg);
                                                }
                                            });


                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请添加文字描述");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                    }
                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择您的需求");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择法律服务的类型");
                            }
                            break;
                        /***************************************发布 委外催收******************************************/
                        case "委外催收":
                            // s1 , s2 ,  s6, s7 , s3
                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s2) && !s2.equals("0") && !s2.equals("0.0") && !s2.equals("0.")) {
                                    if (!TextUtils.isEmpty(s6) && !s6.equals("请选择")) {
                                        if (!TextUtils.isEmpty(s7) && !s7.equals("请选择")) {
                                            if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                                if (!TextUtils.isEmpty(s4)) {

                                                    if (release_frame_one.getVisibility() == View.VISIBLE) {

                                                        dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                                        dialog.show();
                                                        HttpUtils utils = new HttpUtils();
                                                        RequestParams params = new RequestParams();
                                                        params.addBodyParameter("ProArea", s3);
                                                        params.addBodyParameter("WordDes", s4);
                                                        params.addBodyParameter("TypeID", "02");
                                                        params.addBodyParameter("TotalMoney", s2);
                                                        params.addBodyParameter("AssetType", s1);
                                                        params.addBodyParameter("Rate", s6);
                                                        params.addBodyParameter("Status", s7);
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
                                                        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                            @Override
                                                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                                                if (dialog != null) {
                                                                    dialog.dismiss();
                                                                }
                                                                Log.e("benbne", responseInfo.result);
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                                    String status_code = jsonObject.getString("status_code");
                                                                    switch (status_code) {
                                                                        case "200":
                                                                            final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                            builder01.setTitle("发布成功，等待审核。");
                                                                            builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                            builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                    finish();
                                                                                }
                                                                            });
                                                                            builder01.create().show();
                                                                            break;
                                                                        case "499":
                                                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                                if (dialog != null) {
                                                                    dialog.dismiss();
                                                                }
                                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                                error.printStackTrace();
                                                                Log.e("benbne", msg);
                                                            }
                                                        });
                                                    } else {
                                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请至少添加一张图片");
                                                    }

                                                } else {
                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请添加文字描述");
                                                }
                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                            }
                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择状态");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择佣金比例");
                                    }
                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入金额");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择委外催收的类型");
                            }
                            break;
                        /***************************************发布 尽职调查******************************************/
                        case "尽职调查":
                            //  s1 , s , s3 s4
                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s) && !s.equals("请选择")) {
                                    if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                        if (!TextUtils.isEmpty(s4)) {

                                            dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                            dialog.show();
                                            HttpUtils utils = new HttpUtils();
                                            RequestParams params = new RequestParams();
                                            params.addBodyParameter("ProArea", s3);
                                            params.addBodyParameter("WordDes", s4);
                                            params.addBodyParameter("TypeID", "10");
                                            params.addBodyParameter("Informant", s);
                                            params.addBodyParameter("AssetType", s1);
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
                                            utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                @Override
                                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    Log.e("benbne", responseInfo.result);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                        String status_code = jsonObject.getString("status_code");
                                                        switch (status_code) {
                                                            case "200":
                                                                final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                builder01.setTitle("发布成功，等待审核。");
                                                                builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        finish();
                                                                    }
                                                                });
                                                                builder01.create().show();
                                                                break;
                                                            case "499":
                                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                    error.printStackTrace();
                                                    Log.e("benbne", msg);
                                                }
                                            });


                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请添加文字描述");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                    }
                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择被调查方");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择尽职调查的的类型");
                            }
                            break;
                        /***************************************发布 悬赏信息******************************************/
                        case "悬赏信息":
                            // s1  s2   s3  s4
                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s2) && !s2.equals("0") && !s2.equals("0.0") && !s2.equals("0.")) {
                                    if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                        if (!TextUtils.isEmpty(s4)) {
                                            if (release_frame_one.getVisibility() == View.VISIBLE) {


                                                dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                                dialog.show();
                                                HttpUtils utils = new HttpUtils();
                                                RequestParams params = new RequestParams();
                                                params.addBodyParameter("ProArea", s3);
                                                params.addBodyParameter("WordDes", s4);
                                                params.addBodyParameter("TypeID", "09");
                                                params.addBodyParameter("TotalMoney", s2);
                                                params.addBodyParameter("AssetType", s1);
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
                                                utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                    @Override
                                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                                        if (dialog != null) {
                                                            dialog.dismiss();
                                                        }
                                                        Log.e("benbne", responseInfo.result);
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                            String status_code = jsonObject.getString("status_code");
                                                            switch (status_code) {
                                                                case "200":
                                                                    final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                    builder01.setTitle("发布成功，等待审核。");
                                                                    builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                    builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            finish();
                                                                        }
                                                                    });
                                                                    builder01.create().show();
                                                                    break;
                                                                case "499":
                                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                        if (dialog != null) {
                                                            dialog.dismiss();
                                                        }
                                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                        error.printStackTrace();
                                                        Log.e("benbne", msg);
                                                    }
                                                });
                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请至少添加一张图片");
                                            }
                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请添加文字描述");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                    }
                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入悬赏佣金");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择悬赏信息的类型");
                            }
                            break;
                        /***************************************发布 融资需求******************************************/
                        case "融资需求":
                            // s1 s5  s2  s3  s4
                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s5) && !s5.equals("0") && !s5.equals("0.0") && !s5.equals("0.")) {
                                    if (!TextUtils.isEmpty(s2) && !s2.equals("0") && !s2.equals("0.0") && !s2.equals("0.")) {
                                        if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                            if (!TextUtils.isEmpty(s4)) {
                                                dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                                dialog.show();
                                                HttpUtils utils = new HttpUtils();
                                                RequestParams params = new RequestParams();
                                                params.addBodyParameter("ProArea", s3);
                                                params.addBodyParameter("WordDes", s4);
                                                params.addBodyParameter("TypeID", "06");
                                                params.addBodyParameter("TotalMoney", s5);
                                                params.addBodyParameter("AssetType", s1);
                                                params.addBodyParameter("Rate", s2);
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
                                                utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                    @Override
                                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                                        if (dialog != null) {
                                                            dialog.dismiss();
                                                        }
                                                        Log.e("benbne", responseInfo.result);
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                            String status_code = jsonObject.getString("status_code");
                                                            switch (status_code) {
                                                                case "200":
                                                                    final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                    builder01.setTitle("发布成功，等待审核。");
                                                                    builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                    builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            finish();
                                                                        }
                                                                    });
                                                                    builder01.create().show();
                                                                    break;
                                                                case "499":
                                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                        if (dialog != null) {
                                                            dialog.dismiss();
                                                        }
                                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                        error.printStackTrace();
                                                        Log.e("benbne", msg);
                                                    }
                                                });


                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请添加文字描述");
                                            }
                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "可接受的月化信息");
                                    }
                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入金额");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择融资需求的方式");
                            }

                            break;


                        /***************************************发布 典当信息******************************************/
                        case "典当信息":
                            // s1 s2  s3 s4
                            if (!TextUtils.isEmpty(s2) && !s2.equals("0") && !s2.equals("0.0") && !s2.equals("0.")) {
                                if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                    if (!TextUtils.isEmpty(s4)) {
                                        if (release_frame_one.getVisibility() == View.VISIBLE) {


                                            dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                            dialog.show();
                                            HttpUtils utils = new HttpUtils();
                                            RequestParams params = new RequestParams();
                                            params.addBodyParameter("ProArea", s3);
                                            params.addBodyParameter("WordDes", s4);
                                            params.addBodyParameter("TypeID", "05");
                                            params.addBodyParameter("TotalMoney", s2);
                                            params.addBodyParameter("AssetType", "典当");
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
                                            utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                @Override
                                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    Log.e("benbne", responseInfo.result);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                        String status_code = jsonObject.getString("status_code");
                                                        switch (status_code) {
                                                            case "200":
                                                                final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                builder01.setTitle("发布成功，等待审核。");
                                                                builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        finish();
                                                                    }
                                                                });
                                                                builder01.create().show();
                                                                break;
                                                            case "499":
                                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                    error.printStackTrace();
                                                    Log.e("benbne", msg);
                                                }
                                            });
                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请至少添加一张图片");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请添加文字描述");
                                    }

                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入金额");
                            }

                            break;
                        /***************************************发布 担保信息******************************************/
                        case "担保信息":

                            // s1 s2  s3 s4
                            if (!TextUtils.isEmpty(s2) && !s2.equals("0") && !s2.equals("0.0") && !s2.equals("0.")) {
                                if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                    if (!TextUtils.isEmpty(s4)) {
                                        if (release_frame_one.getVisibility() == View.VISIBLE) {


                                            dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                            dialog.show();
                                            HttpUtils utils = new HttpUtils();
                                            RequestParams params = new RequestParams();
                                            params.addBodyParameter("ProArea", s3);
                                            params.addBodyParameter("WordDes", s4);
                                            params.addBodyParameter("TypeID", "05");
                                            params.addBodyParameter("TotalMoney", s2);
                                            params.addBodyParameter("AssetType", "担保");
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
                                            utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                @Override
                                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    Log.e("benbne", responseInfo.result);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                        String status_code = jsonObject.getString("status_code");
                                                        switch (status_code) {
                                                            case "200":
                                                                final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                builder01.setTitle("发布成功，等待审核。");
                                                                builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        finish();
                                                                    }
                                                                });
                                                                builder01.create().show();
                                                                break;
                                                            case "499":
                                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                    error.printStackTrace();
                                                    Log.e("benbne", msg);
                                                }
                                            });
                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请至少添加一张图片");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请添加文字描述");
                                    }

                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入金额");
                            }

                            break;


                        /***************************************发布 固产转让******************************************/
                        case "固产转让":

                            // s1 s2  s3 s4
                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s) && !s.equals("请选择")) {
                                    if (!TextUtils.isEmpty(s2) && !s2.equals("0") && !s2.equals("0.0") && !s2.equals("0.")) {
                                        if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                            if (!TextUtils.isEmpty(s4)) {
                                                if (release_frame_one.getVisibility() == View.VISIBLE) {


                                                    dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                                    dialog.show();
                                                    HttpUtils utils = new HttpUtils();
                                                    RequestParams params = new RequestParams();
                                                    params.addBodyParameter("ProArea", s3);
                                                    params.addBodyParameter("WordDes", s4);
                                                    params.addBodyParameter("TypeID", "12");
                                                    params.addBodyParameter("Corpore", s );
                                                    params.addBodyParameter("TransferMoney", s2);
                                                    params.addBodyParameter("AssetType", s1);
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
                                                    utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                        @Override
                                                        public void onSuccess(ResponseInfo<String> responseInfo) {
                                                            if (dialog != null) {
                                                                dialog.dismiss();
                                                            }
                                                            Log.e("benbne", responseInfo.result);
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                                String status_code = jsonObject.getString("status_code");
                                                                switch (status_code) {
                                                                    case "200":
                                                                        final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                        builder01.setTitle("发布成功，等待审核。");
                                                                        builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                        builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {

                                                                                finish();
                                                                            }
                                                                        });
                                                                        builder01.create().show();
                                                                        break;
                                                                    case "499":
                                                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                            if (dialog != null) {
                                                                dialog.dismiss();
                                                            }
                                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                            error.printStackTrace();
                                                            Log.e("benbne", msg);
                                                        }
                                                    });
                                                } else {
                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请至少添加一张图片");
                                                }
                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请添加文字描述");
                                            }

                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入转让价");
                                    }
                                }else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择标的物");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择固产转让类型");
                            }

                            break;
                        /***************************************发布 债权转让******************************************/
                        case "债权转让":
                            //s1 s5 s2 s3 s4
                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s5) && !s5.equals("0") && !s5.equals("0.0") && !s5.equals("0.")) {
                                    if (!TextUtils.isEmpty(s2) && !s2.equals("0") && !s2.equals("0.0") && !s2.equals("0.")) {
                                        if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                            if (!TextUtils.isEmpty(s4)) {

                                                if (release_frame_one.getVisibility() == View.VISIBLE) {


                                                    dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                                    dialog.show();
                                                    HttpUtils utils = new HttpUtils();
                                                    RequestParams params = new RequestParams();
                                                    params.addBodyParameter("ProArea", s3);
                                                    params.addBodyParameter("WordDes", s4);
                                                    params.addBodyParameter("TypeID", "14");
                                                    params.addBodyParameter("TotalMoney", s5);
                                                    params.addBodyParameter("TransferMoney", s2);
                                                    params.addBodyParameter("AssetType", s1);
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
                                                    utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                        @Override
                                                        public void onSuccess(ResponseInfo<String> responseInfo) {
                                                            if (dialog != null) {
                                                                dialog.dismiss();
                                                            }
                                                            Log.e("benbne", responseInfo.result);
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                                String status_code = jsonObject.getString("status_code");
                                                                switch (status_code) {
                                                                    case "200":
                                                                        final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                        builder01.setTitle("发布成功，等待审核。");
                                                                        builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                        builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {

                                                                                finish();
                                                                            }
                                                                        });
                                                                        builder01.create().show();
                                                                        break;
                                                                    case "499":
                                                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                            if (dialog != null) {
                                                                dialog.dismiss();
                                                            }
                                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                            error.printStackTrace();
                                                            Log.e("benbne", msg);
                                                        }
                                                    });
                                                } else {
                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请至少添加一张图片");
                                                }
                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请添加文字描述");
                                            }
                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入具体折扣");
                                    }

                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入金额");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "类型不能为空");
                            }
                            break;
                        /***************************************发布 资产包转让****************************************/
                        case "资产包转让":
                            // s1 s s5  s2 s3 s4 file_upload
                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s) && !s.equals("请选择")) {
                                    if (!TextUtils.isEmpty(s5) && !s5.equals("0") && !s5.equals("0.0") && !s5.equals("0.")) {
                                        if (!TextUtils.isEmpty(s2) && !s2.equals("0") && !s2.equals("0.0") && !s2.equals("0.")) {
                                            if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                                if (!TextUtils.isEmpty(s4)) {

                                                    if (release_frame_one.getVisibility() == View.VISIBLE) {


                                                        dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                                        dialog.show();
                                                        HttpUtils utils = new HttpUtils();
                                                        RequestParams params = new RequestParams();
                                                        params.addBodyParameter("ProArea", s3);
                                                        params.addBodyParameter("WordDes", s4);
                                                        params.addBodyParameter("TypeID", "01");
                                                        params.addBodyParameter("TotalMoney", s5);
                                                        params.addBodyParameter("FromWhere", s);
                                                        params.addBodyParameter("TransferMoney", s2);
                                                        params.addBodyParameter("AssetType", s1);
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
                                                        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                            @Override
                                                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                                                if (dialog != null) {
                                                                    dialog.dismiss();
                                                                }
                                                                Log.e("benbne", responseInfo.result);
                                                                //ToastUtils.shortToast(ReleaseDetailsActivity.this, "OK");
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                                    String status_code = jsonObject.getString("status_code");
                                                                    switch (status_code) {
                                                                        case "200":
                                                                            final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                            builder01.setTitle("发布成功，等待审核。");
                                                                            builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                            builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                    finish();
                                                                                }
                                                                            });
                                                                            builder01.create().show();
                                                                            break;
                                                                        case "499":
                                                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                                if (dialog != null) {
                                                                    dialog.dismiss();
                                                                }
                                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                                error.printStackTrace();
                                                                Log.e("benbne", msg);
                                                            }
                                                        });
                                                    } else {
                                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请至少添加一张图片");
                                                    }
                                                } else {
                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入文字描述");
                                                }
                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                            }
                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入转让金额");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入总金额");
                                    }
                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择来源");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择资产包类型");
                            }

                            break;


                        /***************************************发布 商业保理****************************************/
                        case "商业保理":
                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s2) && !s2.equals("0") && !s2.equals("0.0") && !s2.equals("0.")) {
                                    if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                        if (!TextUtils.isEmpty(s4)) {
                                            dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                            dialog.show();
                                            HttpUtils utils = new HttpUtils();
                                            RequestParams params = new RequestParams();
                                            params.addBodyParameter("ProArea", s3);
                                            params.addBodyParameter("WordDes", s4);
                                            params.addBodyParameter("TypeID", "04");
                                            params.addBodyParameter("TotalMoney", s2);
                                            params.addBodyParameter("BuyerNature", s1);
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
                                            utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                @Override
                                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    Log.e("benbne", responseInfo.result);
                                                    //ToastUtils.shortToast(ReleaseDetailsActivity.this, "OK");
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                        String status_code = jsonObject.getString("status_code");
                                                        switch (status_code) {
                                                            case "200":
                                                                final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                builder01.setTitle("发布成功，等待审核。");
                                                                builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        finish();
                                                                    }
                                                                });
                                                                builder01.create().show();
                                                                break;
                                                            case "499":
                                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                    if (dialog != null) {
                                                        dialog.dismiss();
                                                    }
                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                    error.printStackTrace();
                                                    Log.e("benbne", msg);
                                                }
                                            });
                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入内容描述");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择发布地区");
                                    }
                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入合同金额");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择买方性质");
                            }
                            break;
                        /***************************************发布 投资需求****************************************/
                        case "投资需求":
                            // s1 s s5  s2 s3 s4 file_upload
                            if (!TextUtils.isEmpty(s1) && !s1.equals("请选择")) {
                                if (!TextUtils.isEmpty(s) && !s.equals("请选择")) {
                                    if (!TextUtils.isEmpty(s5) && !s5.equals("0") && !s5.equals("0.0") && !s5.equals("0.")) {
                                        if (!TextUtils.isEmpty(s6) && !s6.equals("请选择")) {
                                            if (!TextUtils.isEmpty(s3) && !s3.equals("请选择")) {
                                                if (!TextUtils.isEmpty(s4)) {
                                                        //ToastUtils.shortToast(ReleaseDetailsActivity.this , "接口待开发");
                                                        dialog = new MyProgressDialog(ReleaseDetailsActivity.this, "正在发布中。。。");
                                                        dialog.show();
                                                        HttpUtils utils = new HttpUtils();
                                                        RequestParams params = new RequestParams();
                                                        params.addBodyParameter("ProArea", s3);
                                                        params.addBodyParameter("WordDes", s4);
                                                        params.addBodyParameter("TypeID", "15");
                                                        params.addBodyParameter("Rate", s5);
                                                        params.addBodyParameter("InvestType", s);
                                                        params.addBodyParameter("Year", s6.replace("年" ,"") );
                                                        params.addBodyParameter("AssetType", s1);
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
                                                        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                                            @Override
                                                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                                                if (dialog != null) {
                                                                    dialog.dismiss();
                                                                }
                                                                Log.e("benbne", responseInfo.result);
                                                                //ToastUtils.shortToast(ReleaseDetailsActivity.this, "OK");
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                                                                    String status_code = jsonObject.getString("status_code");
                                                                    switch (status_code) {
                                                                        case "200":
                                                                            final CustomDialog.Builder builder01 = new CustomDialog.Builder(ReleaseDetailsActivity.this);
                                                                            builder01.setTitle("发布成功，等待审核。");
                                                                            builder01.setMessage("请到个人中心“我的发布”板块去查看");
                                                                            builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                    finish();
                                                                                }
                                                                            });
                                                                            builder01.create().show();
                                                                            break;
                                                                        case "499":
                                                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "发布失败");
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
                                                                if (dialog != null) {
                                                                    dialog.dismiss();
                                                                }
                                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "网络连接失败");
                                                                error.printStackTrace();
                                                                Log.e("benbne", msg);
                                                            }
                                                        });
                                                } else {
                                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入文字描述");
                                                }
                                            } else {
                                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择地区");
                                            }
                                        } else {
                                            ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择投资期限");
                                        }
                                    } else {
                                        ToastUtils.shortToast(ReleaseDetailsActivity.this, "请输入预期回报率");
                                    }
                                } else {
                                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择投资方式");
                                }
                            } else {
                                ToastUtils.shortToast(ReleaseDetailsActivity.this, "请选择投资类型");
                            }

                            break;
                        default:
                            break;
                    }
                } else {
                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "您还未登陆。");
                }
            }
        });

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_release_details);
    }

    @Override
    public void initViews() {
        total_money = (EditText) findViewById(R.id.total_money);
        total_money.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        InputFilter[] filters = {new CashierInputFilter()};
        total_money.setFilters(filters);
        relative_status = (RelativeLayout) findViewById(R.id.relative_status);
        info_upload = (RelativeLayout) findViewById(R.id.info_upload);
        relative_yognjin = (RelativeLayout) findViewById(R.id.relative_yognjin);
        relative_head01 = (RelativeLayout) findViewById(R.id.relative_head01);
        yognjin = (TextView) findViewById(R.id.yognjin);
        release_audio_des_duration = (TextView) findViewById(R.id.release_audio_des_duration);
        head01 = (TextView) findViewById(R.id.head01);
        text_yongjin = (TextView) findViewById(R.id.text_yongjin);
        text_status = (TextView) findViewById(R.id.text_status);
        release_text_part = (TextView) findViewById(R.id.release_text_part);
        //niu_audio = (TextView)findViewById(R.id.niu_audio) ;
        trans_edit = (EditText) findViewById(R.id.trans_edit);
        trans_edit.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        trans_edit.setFilters(filters);
        title = (TextView) findViewById(R.id.title);
        voice_cancel = (TextView) findViewById(R.id.voice_cancel);
        pre = (RelativeLayout) findViewById(R.id.pre);
        //details_audio_get = (ImageView)findViewById(R.id.details_audio_get) ;
        //details_pic_get = (ImageView)findViewById(R.id.details_pic_get) ;
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
        relative_type = (RelativeLayout) findViewById(R.id.relative_type);
        textView_type = (TextView) findViewById(R.id.textView_type);
        text_from = (TextView) findViewById(R.id.text_from);
        relative_from = (RelativeLayout) findViewById(R.id.relative_from);
        text_part = (TextView) findViewById(R.id.text_part);
        relative_part = (RelativeLayout) findViewById(R.id.relative_part);
        relative_total = (RelativeLayout) findViewById(R.id.relative_total);
        release_edit = (EditText) findViewById(R.id.release_edit);
        release_one = (TextView) findViewById(R.id.release_one);
        text_trans = (TextView) findViewById(R.id.text_trans);
        text_total = (TextView) findViewById(R.id.text_total);
        relative_trans = (RelativeLayout) findViewById(R.id.relative_trans);
        text_from_a = (TextView) findViewById(R.id.text_from_a);
        linearLayout_release = (TextView) findViewById(R.id.linearLayout_release);
        release_wan01 = (TextView) findViewById(R.id.release_wan01);
        release_wan = (TextView) findViewById(R.id.release_wan);
        // 当文件不存在的时候创建文件
        String path = SDUtil.getSDPath() + File.separator + "ziya";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        //recAudioFile = new File(SDUtil.getSDPath() + File.separator + "ziya", "temp.amr");
        recAudioFile = new File(SDUtil.getSDPath() + File.separator + "ziya", "temp.aac");
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
        isLogin = GetBenSharedPreferences.getIsLogin(this ) ;
        login = GetBenSharedPreferences.getTicket(this ) ;
        //通过页面跳转，拿到该页面的title，并进行显示
        loadTitle();
    }

    @Override
    public void onClick(View v) {

    }


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

    private void loadTitle() {
        Intent intent = getIntent();
        title_t = intent.getStringExtra("title");
        title.setText(title_t);
    }

    private void changeTitleCocor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.aaa);//通知栏所需颜色

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    private void andioGet() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_audio, null);
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final Button audio = (Button)view.findViewById(R.id.audio);

//        final Timer timer1 = new Timer() ;
//        timer1.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (left_time <= 5){
//                            timer1.cancel();
//                        }else {
//                            int maxAmplitude = mMediaRecorder.getMaxAmplitude();
//                            audio.setText("当前音量" + maxAmplitude + "分贝");
//                        }
//
//                    }
//                });
//            }
//        } , 0 , 500 );
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
                left_time = 30 ;
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

                if (ActivityCompat.checkSelfPermission(ReleaseDetailsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.shortToast(ReleaseDetailsActivity.this, "请在管理中心，给予相机相应权限。");
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
                    Toast.makeText(ReleaseDetailsActivity.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ReleaseDetailsActivity.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ReleaseDetailsActivity.this, "请重新获取图片", Toast.LENGTH_SHORT).show();
                }

                break;
            case 4:

                if (resultCode == RESULT_OK && null != data) {
                    String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    textView_type.setText(result);
                }

                break;

            case 5:
                if (resultCode == RESULT_OK && null != data) {
                    String result01 = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    text_from.setText(result01);
                }

                break;
            case 6:
                if (resultCode == RESULT_OK && null != data) {
                    String result01 = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    text_part.setText(result01);
                }

                break;
            case 7:
                if (resultCode == RESULT_OK && null != data) {
                    String result01 = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    text_yongjin.setText(result01);
                }

                break;
            case 8:
                if (resultCode == RESULT_OK && null != data) {
                    String result01 = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    text_status.setText(result01);
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


}
