package com.ziyawang.ziya.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.LoginActivity;
import com.ziyawang.ziya.activity.RechargeActivity;
import com.ziyawang.ziya.activity.V2DetailsFindInfoActivity;
import com.ziyawang.ziya.activity.VideoActivity;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyMovieImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/14.
 */
public class MovieBigItemAdapter extends BaseAdapter {

    private Activity context ;
    private List<FindVideoEntity> list ;

    public MovieBigItemAdapter(){}

    public MovieBigItemAdapter(Activity context , List<FindVideoEntity> list ){
        super();
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder ;
        if(convertView == null ){
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_big_items, parent , false);
            holder = new ViewHolder() ;
            holder.movie_img = (MyMovieImageView)convertView.findViewById(R.id.movie_img);
            holder.movie_img.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            //holder.other = (TextView)convertView.findViewById(R.id.other);
            holder.movie_look = (TextView)convertView.findViewById(R.id.movie_look);

            holder.pay_account = (TextView)convertView.findViewById(R.id.pay_account);
            holder.pay_account_image = (ImageView)convertView.findViewById(R.id.pay_account_image);
            /************************************播放视频********************************************/
            //holder.mSuperVideoPlayer = (SuperVideoPlayer)convertView.findViewById(R.id.video_player_item_1);
            //holder.mPlayBtnView = convertView.findViewById(R.id.play_btn);
            //holder.video_frame = (FrameLayout)convertView.findViewById(R.id.video_frame) ;
            /************************************播放视频*******************************************/
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        BitmapUtils bitmapUtils = new BitmapUtils(context) ;
        bitmapUtils.display(holder.movie_img, Url.FileIP + list.get(position).getVideoLogo());

        holder.title.setText(list.get(position).getVideoTitle());
        //holder.other.setText("已播放" + list.get(position).getViewCount() + "次");

        holder.movie_look.setText(list.get(position).getViewCount());

        if (checkMember(position)){
            holder.pay_account_image.setVisibility(View.VISIBLE);
            holder.pay_account.setText(list.get(position).getPrice() + "芽币");
            holder.pay_account.setTextColor(Color.rgb(252,130,0));
        }else {
            holder.pay_account_image.setVisibility(View.GONE);
            holder.pay_account.setText("免费");
            holder.pay_account.setTextColor(Color.rgb(78,189,102));
        }
        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalConvertView.setEnabled(false);
                if (checkMember(position)){
                    if (!GetBenSharedPreferences.getIsLogin(context)){
                        goLoginActivity(finalConvertView) ;
                    }else {
                        //是否是VIP用户
                        String right = GetBenSharedPreferences.getRight(context);
                        if (!TextUtils.isEmpty(right)){
                            goDetailsVideoActivity(position ,finalConvertView) ;
                        }else {
                            String urls = String.format(Url.ISPay, GetBenSharedPreferences.getTicket(context));
                            HttpUtils httpUtils = new HttpUtils();
                            RequestParams params1 = new RequestParams();
                            params1.addBodyParameter("VideoID", list.get(position).getVideoID());
                            httpUtils.send(HttpRequest.HttpMethod.POST, urls, params1, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(responseInfo.result);
                                        String payFlag = jsonObject1.getString("PayFlag");
                                        if ("1".equals(payFlag)){
                                            goDetailsVideoActivity(position ,finalConvertView) ;
                                        }else {
                                            showPopWindow(position ,finalConvertView ) ;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(HttpException error, String msg) {
                                    finalConvertView.setEnabled(true);
                                    error.printStackTrace();
                                    ToastUtils.shortToast(context , "未知错误");
                                }
                            });
                        }

                    }
                }else {
                    goDetailsVideoActivity(position ,finalConvertView) ;
                }
            }
        });
        return convertView;
    }

    private void showPopWindow(final int position, final View finalConvertView) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_publish, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.relative);
        final TextView info_type = (TextView) view.findViewById(R.id.info_type);
        final TextView info_title = (TextView) view.findViewById(R.id.info_title);
        final TextView shejian_price = (TextView) view.findViewById(R.id.shejian_price);
        final TextView shejian_balance = (TextView) view.findViewById(R.id.shejian_balance);
        final TextView balance_type = (TextView) view.findViewById(R.id.balance_type);
        final Button shejian_pay = (Button) view.findViewById(R.id.shejian_pay);
        final Button shejian_recharge = (Button) view.findViewById(R.id.shejian_recharge);
        final ImageButton pay_cancel = (ImageButton) view.findViewById(R.id.pay_cancel);
        final LinearLayout shejian_two = (LinearLayout) view.findViewById(R.id.shejian_two);
        TextPaint tp = shejian_price.getPaint();
        tp.setFakeBoldText(true);
        TextPaint tp01 = shejian_balance.getPaint();
        tp01.setFakeBoldText(true);

        //消费
        shejian_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去消费
                goPay(position ,finalConvertView , window);
            }
        });
        //充值
        shejian_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到充值页面
                goRechargeActivity(window);
            }
        });

        //Member 2收费信息 其他为不收费信息
        info_type.setText("该视频为付费视频");
        info_title.setText("消耗芽币可观看视频");
        shejian_two.setVisibility(View.VISIBLE);
        shejian_price.setText(list.get(position).getPrice());
        //获取用户的实时余额
        String urls = String.format(Url.Myicon, GetBenSharedPreferences.getTicket(context));
        HttpUtils utils = new HttpUtils();
        final RequestParams params = new RequestParams();
        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //处理请求成功后的数据
                try {
                    final JSONObject jsonObject = new JSONObject(responseInfo.result);
                    JSONObject user = jsonObject.getJSONObject("user");
                    final String account = user.getString("Account");
                    shejian_balance.setText(account);
                    if (Integer.parseInt(account) < Integer.parseInt(list.get(position).getPrice())) {
                        balance_type.setVisibility(View.VISIBLE);
                    } else {
                        balance_type.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //打印用户的失败回调
                error.printStackTrace();
                ToastUtils.shortToast(context, "网络连接异常");
            }
        });
        pay_cancel.setOnClickListener(new View.OnClickListener() {
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
        window.showAtLocation(relative, Gravity.CENTER, 0, 0);
        // 设置popWindow的显示和消失动画

        backgroundAlpha(0.2f);
        finalConvertView.setEnabled(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                finalConvertView.setEnabled(true);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().setAttributes(lp);
    }

    private void goRechargeActivity(PopupWindow window) {
        Intent intent = new Intent(context , RechargeActivity.class ) ;
        context.startActivity(intent);
        window.dismiss();
    }

    private void goPay(final int position, final View finalConvertView , final PopupWindow window) {
        String urls = String.format(Url.V215VideoPay, GetBenSharedPreferences.getTicket(context));
        HttpUtils utils = new HttpUtils();
        final RequestParams params = new RequestParams();
        params.addBodyParameter("VideoID" , list.get(position).getVideoID() );
        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //处理请求成功后的数据
                try {
                    final JSONObject jsonObject = new JSONObject(responseInfo.result);
                    String status_code = jsonObject.getString("status_code");
                    switch (status_code){
                        case "200" :
                            goDetailsVideoActivity(position , finalConvertView);
                            ToastUtils.shortToast(context, "购买成功");
                            window.dismiss();
                            break;
                        case "416" :
                            ToastUtils.shortToast(context, "非收费视频");
                            window.dismiss();
                            break;
                        case "417" :
                            ToastUtils.shortToast(context, "已经支付过该条视频");
                            goDetailsVideoActivity(position , finalConvertView);
                            window.dismiss();
                            break;
                        case "418" :
                            ToastUtils.shortToast(context, "余额不足");
                            window.dismiss();
                            break;
                        default:
                            ToastUtils.shortToast(context, "未知错误");
                            window.dismiss();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //打印用户的失败回调
                error.printStackTrace();
                ToastUtils.shortToast(context, "网络连接异常");
                window.dismiss();
            }
        });
    }

    private void goDetailsVideoActivity(int position , View finalConvertView) {
        String id = list.get(position).getVideoID();
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
        finalConvertView.setEnabled(true);
    }

    private boolean checkMember(int position) {
        return "1".equals(list.get(position).getMember());
    }

    private void goLoginActivity(View finalConvertView) {
        Intent intent = new Intent(context , LoginActivity.class ) ;
        context.startActivity(intent);
        finalConvertView.setEnabled(true);
    }

    static class  ViewHolder{

        MyMovieImageView movie_img ;
        TextView title ;
        //TextView other ;
        TextView movie_look ;
        TextView pay_account ;
        ImageView pay_account_image ;

    }

    public void addAll(Collection<? extends FindVideoEntity> collection) {

        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){

        list.clear();
        notifyDataSetChanged();
    }
}
