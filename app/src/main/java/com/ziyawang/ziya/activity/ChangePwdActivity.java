package com.ziyawang.ziya.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.tools.ChangeNotifyColor;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.view.MyProgressDialog;
import com.ziyawang.ziya.view.XEditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 更改密码的页面的实现逻辑
 */
public class ChangePwdActivity extends BenBenActivity implements View.OnClickListener {

    //保存按钮
    private TextView changePwd_ok;
    //返回按钮
    private RelativeLayout pre;
    //编写密码的文本框
    private XEditText changePwd_editText;
    //用户登录的ticket值
    private String login;
    //数据加载的dialog
    private MyProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //改变通知栏的颜色
        ChangeNotifyColor.change(R.color.aaa, this);
        //拿到ticket值
        login = GetBenSharedPreferences.getTicket(this);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_change_pwd);
    }

    @Override
    public void initViews() {
        changePwd_ok = (TextView) findViewById(R.id.changePwd_ok);
        pre = (RelativeLayout) findViewById(R.id.pre);
        changePwd_editText = (XEditText) findViewById(R.id.changePwd_editText);
    }

    @Override
    public void initListeners() {
        pre.setOnClickListener(this);
        changePwd_ok.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //添加回退事件
            case R.id.pre:
                finish();
                break;
            //更改密码并保存
            case R.id.changePwd_ok:
                savePwd();
                break;
            default:
                break;
        }
    }

    /**
     * 保存用户填写的密码
     */
    private void savePwd() {
        //判断密码是否符合标准
        if (judgePwd()) {
            //符合标准后，开启dialog。
            showBenDialog();
            //加载数据。
            loadData();
        }
    }

    /**
     * 展示数据加载框
     */
    private void showBenDialog() {
        //在开始进行网络连接时显示进度条对话框
        dialog = new MyProgressDialog(ChangePwdActivity.this, "修改密码中，请稍后。。。");
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

    /**
     * 开启网络请求
     */
    private void loadData() {
        String urls = String.format(Url.ChangePwd, login);
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("password", changePwd_editText.getText().toString());
        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //隐藏dialog
                hiddenBenDialog();
                Log.e("benben", responseInfo.result);
                //请求成功后处理结果
                dealResult(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //输出失败回调
                error.printStackTrace();
                //隐藏dialog
                hiddenBenDialog();
                //提示用户网络异常
                ToastUtils.shortToast(ChangePwdActivity.this, "网络连接异常");
            }
        });

    }

    /**
     * 处理成功回调后的数据
     * @param result json字符串
     */
    private void dealResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status_code = jsonObject.getString("status_code");
            switch (status_code) {
                case "200":
                    ToastUtils.shortToast(ChangePwdActivity.this, "密码修改成功");
                    finish();
                    break;
                case "410":
                    ToastUtils.shortToast(ChangePwdActivity.this, "密码修改失败");
                    break;
                default:
                    ToastUtils.shortToast(ChangePwdActivity.this, "密码修改失败");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断密码是否符合标准
     * @return
     */
    private boolean judgePwd() {
        //密码是否填写
        if (TextUtils.isEmpty(changePwd_editText.getText().toString().trim())) {
            ToastUtils.shortToast(ChangePwdActivity.this, "请输入密码");
            return false;
        }
        //密码是否小于6位
        if (changePwd_editText.length() < 6) {
            ToastUtils.longToast(ChangePwdActivity.this, "您输入的密码过短");
            return false;
        }
        //密码是否大于16位
        if (changePwd_editText.length() > 16) {
            ToastUtils.longToast(ChangePwdActivity.this, "您输入的密码过长");
            return false;
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart("更改密码页面");
        //统计时长
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        // 统计页面
        MobclickAgent.onPageEnd("更改密码页面");
        //统计时长
        MobclickAgent.onPause(this);
    }

}
