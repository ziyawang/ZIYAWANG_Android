package com.ziyawang.ziya.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
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
import com.ziyawang.ziya.activity.DetailsNewsActivity;
import com.ziyawang.ziya.activity.LoginActivity;
import com.ziyawang.ziya.activity.RechargeActivity;
import com.ziyawang.ziya.activity.V2DetailsFindInfoActivity;
import com.ziyawang.ziya.activity.VipCenterActivity;
import com.ziyawang.ziya.entity.V2InfoEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BitmapHelp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/11/26.
 */
public class V2FindInfoAdapter extends BaseAdapter {

    public static BitmapUtils bitmapUtils;

    private Activity context;
    private List<V2InfoEntity> list;

    private SharedPreferences right ;

    private boolean scrollState = false;

    public void setScrollState(boolean scrollState) {
        this.scrollState = scrollState;
    }

    public V2FindInfoAdapter() {
    }

    public V2FindInfoAdapter(Activity context, List<V2InfoEntity> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
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

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.v2_find_info_items, parent, false);
            holder = new ViewHolder();
            holder.news_title = (TextView) convertView.findViewById(R.id.news_title);
            holder.info_part = (TextView) convertView.findViewById(R.id.info_part);
            holder.news_image = (ImageView) convertView.findViewById(R.id.news_image);
            holder.info_part_one = (TextView) convertView.findViewById(R.id.info_part_one);
            holder.info_part_two = (TextView) convertView.findViewById(R.id.info_part_two);
            holder.info_no = (TextView) convertView.findViewById(R.id.info_no);
            holder.info_bee = (TextView) convertView.findViewById(R.id.info_bee);
            holder.info_danwei = (TextView) convertView.findViewById(R.id.info_danwei);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.liangdian = (LinearLayout) convertView.findViewById(R.id.liangdian);
            holder.liangdian01 = (TextView) convertView.findViewById(R.id.liangdian01);
            holder.liangdian02 = (TextView) convertView.findViewById(R.id.liangdian02);
            holder.liangdian03 = (TextView) convertView.findViewById(R.id.liangdian03);
            holder.image_01 = (ImageView) convertView.findViewById(R.id.image_01);
            holder.image_02 = (ImageView) convertView.findViewById(R.id.image_02);
            holder.text_01 = (TextView) convertView.findViewById(R.id.text_01);
            holder.text_02 = (TextView) convertView.findViewById(R.id.text_02);

            holder.czgg_relative = (RelativeLayout)convertView.findViewById(R.id.czgg_relative ) ;
            holder.relative_info = (RelativeLayout)convertView.findViewById(R.id.relative_info ) ;
            holder.czgg_image = (ImageView)convertView.findViewById(R.id.czgg_image ) ;
            holder.czgg_title = (TextView)convertView.findViewById(R.id.czgg_title ) ;
            holder.news_des = (TextView)convertView.findViewById(R.id.news_des ) ;
            holder.news_time = (TextView)convertView.findViewById(R.id.news_time ) ;
            holder.new_author = (TextView)convertView.findViewById(R.id.new_author ) ;
            holder.news_cooperateState = (ImageView)convertView.findViewById(R.id.news_cooperateState ) ;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 获取bitmapUtils单例
        bitmapUtils = BitmapHelp.getBitmapUtils(context);
        /**
         * 设置默认的图片展现、加载失败的图片展现
         */
        bitmapUtils.configDefaultLoadingImage(R.mipmap.fast_error);
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.error_imgs_big);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

        if ("99".equals(list.get(position).getTypeID())){
            holder.czgg_relative.setVisibility(View.VISIBLE);
            holder.relative_info.setVisibility(View.GONE);
            /**
             * display参数 (ImageView container, String uri,
             * BitmapLoadCallBack<ImageView> callBack)
             */

            bitmapUtils.display(holder.czgg_image, Url.FileIP + list.get(position).getNewsLogo());
            holder.czgg_title.setText(list.get(position).getNewsTitle());
            holder.news_des.setText(Html.fromHtml(list.get(position).getBrief().replace("　　", "")));
            String substring01 = list.get(position).getPublishTime().substring(0, 10);

            //String substring02 = list.get(position).getPublishTime().substring(11, 16);
            holder.news_time.setText(substring01);
            holder.new_author.setText("来源：" + list.get(position).getNewsAuthor().trim());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsNewsActivity.class);
                    intent.putExtra("id", list.get(position).getNewsID());
                    intent.putExtra("type", "处置公告");
                    intent.putExtra("author", list.get(position).getNewsAuthor());
                    context.startActivity(intent);
                }
            });
        }else {
            /**
             * display参数 (ImageView container, String uri,
             * BitmapLoadCallBack<ImageView> callBack)
             */
            bitmapUtils.display(holder.news_image, Url.FileIP + list.get(position).getPictureDes1() );
            holder.czgg_relative.setVisibility(View.GONE);
            holder.relative_info.setVisibility(View.VISIBLE);
            switch (list.get(position).getCooperateState()){
                //正常状态的信息
                case "0" :
                    holder.news_cooperateState.setVisibility(View.GONE);
                    break;
                //合作中的信息
                case "1" :
                    holder.news_cooperateState.setVisibility(View.VISIBLE);
                    holder.news_cooperateState.setImageResource(R.mipmap.v2140201);
                    break;
                //处置完成的信息
                case "2" :
                    holder.news_cooperateState.setVisibility(View.VISIBLE);
                    if ("6".equals(list.get(position).getTypeID()) || "17".equals(list.get(position).getTypeID())){
                        holder.news_cooperateState.setImageResource(R.mipmap.v2140202);
                    }else {
                        holder.news_cooperateState.setImageResource(R.mipmap.v2140203);
                    }
                    break;
                default:
                    holder.news_cooperateState.setVisibility(View.GONE);
                    break;
            }
            switch (list.get(position).getTypeID()) {
                case "1":
                    holder.image_01.setImageResource(R.mipmap.v2zongjine);
                    holder.image_02.setImageResource(R.mipmap.v2zhuanrang);
                    if (list.get(position).getTotalMoney().length() >= 9 && !TextUtils.isEmpty(list.get(position).getTotalMoney())){
                        holder.text_01.setText(list.get(position).getTotalMoney().substring( 0 , list.get(position).getTotalMoney().length()-3) + "万");
                    }else {
                        holder.text_01.setText(list.get(position).getTotalMoney() + "万");
                    }

                    if (list.get(position).getTransferMoney().length() >= 9 && !TextUtils.isEmpty(list.get(position).getTransferMoney())){
                        holder.text_02.setText(list.get(position).getTransferMoney().substring( 0 , list.get(position).getTransferMoney().length()-3) + "万");
                    }else {
                        holder.text_02.setText(list.get(position).getTransferMoney() + "万");
                    }
                    break;
                case "6":
                    holder.image_01.setImageResource(R.mipmap.v2rongzi);
                    holder.image_02.setVisibility(View.GONE);
                    holder.text_01.setText(list.get(position).getTotalMoney() + "万");
                    holder.text_02.setVisibility(View.GONE);
                    break;
                case "12":
                    holder.image_01.setImageResource(R.mipmap.v2shichang);
                    holder.image_02.setImageResource(R.mipmap.v2zhuanrang);
                    if (list.get(position).getMarketPrice().length() >= 9 && !TextUtils.isEmpty(list.get(position).getMarketPrice())){
                        holder.text_01.setText(list.get(position).getMarketPrice().substring( 0 , list.get(position).getMarketPrice().length()-3) + "万");
                    }else {
                        holder.text_01.setText(list.get(position).getMarketPrice() + "万");
                    }

                    if (list.get(position).getTransferMoney().length() >= 9 && !TextUtils.isEmpty(list.get(position).getTransferMoney())){
                        holder.text_02.setText(list.get(position).getTransferMoney().substring( 0 , list.get(position).getTransferMoney().length()-3) + "万");
                    }else {
                        holder.text_02.setText(list.get(position).getTransferMoney() + "万");
                    }
                    break;
                case "16":
                    holder.image_01.setImageResource(R.mipmap.v2zhuanrang);
                    holder.image_02.setVisibility(View.GONE);
                    holder.text_01.setText(list.get(position).getTransferMoney() + "万");
                    holder.text_02.setVisibility(View.GONE);
                    break;
                case "17":
                    holder.image_01.setImageResource(R.mipmap.v2rongzi);
                    holder.image_02.setVisibility(View.GONE);
                    holder.text_01.setText(list.get(position).getMoney() + "万");
                    holder.text_02.setVisibility(View.GONE);
                    break;
                case "18":
                    holder.image_01.setImageResource(R.mipmap.v2zhaiquan);
                    holder.image_02.setVisibility(View.GONE);
                    holder.text_01.setText(list.get(position).getMoney() + "万");
                    holder.text_02.setVisibility(View.GONE);
                    break;
                case "19":
                    holder.image_01.setImageResource(R.mipmap.v2zongjine);
                    holder.image_02.setVisibility(View.GONE);
                    holder.text_01.setText(list.get(position).getTotalMoney() + "万");
                    holder.text_02.setVisibility(View.GONE);
                    break;
                case "20":
                case "21":
                case "22":
                    holder.image_01.setImageResource(R.mipmap.v2qipai);
                    holder.image_02.setVisibility(View.GONE);
                    holder.text_01.setText(list.get(position).getMoney() + "万");
                    holder.text_02.setVisibility(View.GONE);
                    break;

            }
            if (list.get(position).getProArea().contains("-")) {
                String[] split = list.get(position).getProArea().split("-");
                holder.info_part.setText(split[0].toString().trim());
            } else {
                holder.info_part.setText(list.get(position).getProArea());
            }
            holder.news_title.setText(list.get(position).getTitle());
            holder.info_part_one.setText(list.get(position).getTypeName());
            holder.info_part_two.setText(list.get(position).getAssetType());
            holder.info_no.setText(list.get(position).getProjectNumber());

            String proLabel = list.get(position).getProLabel();
            if (TextUtils.isEmpty(proLabel) || "".equals(proLabel)) {
                holder.liangdian.setVisibility(View.GONE);
            } else {
                holder.liangdian.setVisibility(View.VISIBLE);
                String[] split = proLabel.split(",");
                switch (split.length) {
                    case 1:
                        holder.liangdian01.setVisibility(View.VISIBLE);
                        holder.liangdian02.setVisibility(View.GONE);
                        holder.liangdian03.setVisibility(View.GONE);
                        holder.liangdian01.setText(split[0]);
                        break;
                    case 2:
                        holder.liangdian01.setVisibility(View.VISIBLE);
                        holder.liangdian01.setText(split[0]);
                        holder.liangdian02.setVisibility(View.VISIBLE);
                        holder.liangdian02.setText(split[1]);
                        holder.liangdian03.setVisibility(View.GONE);
                        break;
                    default:
                        holder.liangdian01.setVisibility(View.VISIBLE);
                        holder.liangdian01.setText(split[0]);
                        holder.liangdian02.setVisibility(View.VISIBLE);
                        holder.liangdian02.setText(split[1]);
                        holder.liangdian03.setVisibility(View.VISIBLE);
                        holder.liangdian03.setText(split[2]);

                        break;
                }
            }
            String member = list.get(position).getMember();
            if (member.equals("1")) {
                holder.img.setVisibility(View.VISIBLE);
                holder.info_bee.setVisibility(View.GONE);
                holder.info_danwei.setVisibility(View.GONE);
                holder.img.setImageResource(R.mipmap.v2vipresources);
            } else if (member.equals("2")) {
                holder.img.setVisibility(View.VISIBLE);
                holder.info_bee.setVisibility(View.VISIBLE);
                holder.info_danwei.setVisibility(View.VISIBLE);
                holder.img.setImageResource(R.mipmap.v2moneyresources);
                holder.info_bee.setText(list.get(position).getPrice());
            } else {
                holder.img.setVisibility(View.GONE);
                holder.info_bee.setVisibility(View.GONE);
                holder.info_danwei.setVisibility(View.GONE);
            }

            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //未登录，去登陆，登陆之后，判断是否是收费资源，是收费资源，那么直接调用anthme 的接口，自己的
                    finalConvertView.setEnabled(false);

//                    if ("2".equals(list.get(position).getMember())) {
//                        if (GetBenSharedPreferences.getIsLogin(context)) {
//                            //调用authme的接口，拿到自己的role 和 account
//                            loadData(position, finalConvertView);
//
//                        } else {
//                            Intent intent = new Intent(context, LoginActivity.class);
//                            context.startActivity(intent);
//                            finalConvertView.setEnabled(true);
//                        }
//
//                    } else if ("1".equals(list.get(position).getMember())){
//                        showForVipPop(finalConvertView) ;
//                    } else{
//                        String id = list.get(position).getProjectID();
//                        Intent intent = new Intent(context, V2DetailsFindInfoActivity.class);
//                        intent.putExtra("id", id);
//                        context.startActivity(intent);
//                        finalConvertView.setEnabled(true);
//                    }
                    if ( "1".equals(list.get(position).getCooperateState()) || "2".equals(list.get(position).getCooperateState()) ||"0".equals(list.get(position).getMember())){
                        goV2DetailsFindInfoActivity(position , finalConvertView ) ;
                    }else {
                        String right = GetBenSharedPreferences.getRight(context);
                        if (!TextUtils.isEmpty(right) ){
                            String[] split = right.split(",");
                            boolean isPermission = false ;
                            for (int i = 0; i < split.length; i++) {
                                if (list.get(position).getTypeID().equals(split[i].toString())){
                                    isPermission = true ;
                                    break;
                                }
                            }
                            if (isPermission){
                                goV2DetailsFindInfoActivity(position , finalConvertView ) ;
                            }else {
                                judgeMember(position , finalConvertView) ;
                            }
                        }else {
                            judgeMember(position , finalConvertView) ;

                        }
                    }
                }
            });
        }
        return convertView;
    }

    private void judgeMember(int position, View finalConvertView) {
//        if ("2".equals(list.get(position).getMember())) {
//            if (GetBenSharedPreferences.getIsLogin(context)) {
//                //调用authme的接口，拿到自己的role 和 account
//                loadData(position, finalConvertView);
//            } else {
//                Intent intent = new Intent(context, LoginActivity.class);
//                context.startActivity(intent);
//                finalConvertView.setEnabled(true);
//            }
//        } else if ("1".equals(list.get(position).getMember())){
//            showForVipPop(finalConvertView) ;
//        }
        if (GetBenSharedPreferences.getIsLogin(context)) {
                //调用authme的接口，拿到自己的role 和 account
                loadData(position, finalConvertView);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                finalConvertView.setEnabled(true);
            }
    }

    private void goV2DetailsFindInfoActivity(int position, View finalConvertView) {
        String id = list.get(position).getProjectID();
        Intent intent = new Intent(context, V2DetailsFindInfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
        finalConvertView.setEnabled(true);
    }

    private void showForVipPop(final View convertView, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_not_vip, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.relative);
        final Button submit = (Button) view.findViewById(R.id.submit);
        final Button to_recharge = (Button) view.findViewById(R.id.to_recharge);
        final ImageButton cancel = (ImageButton) view.findViewById(R.id.cancel);
        final TextView title = (TextView)view.findViewById(R.id.title ) ;
        String typeID = list.get(position).getTypeID();
        switch (typeID){
            case "1" :
                title.setText("本条VIP信息只针对资产包会员免费开放，详情请咨询会员专线：010-56052557");
                break;
            case "6" :
            case "17" :
                title.setText("本条VIP信息只针对融资信息会员免费开放，详情请咨询会员专线：010-56052557");
                break;
            case "12" :
            case "16" :
                title.setText("本条VIP信息只针对固定资产会员免费开放，详情请咨询会员专线：010-56052557");
                break;
            case "18" :
                title.setText("本条VIP信息只针对企业商账会员免费开放，详情请咨询会员专线：010-56052557");
                break;
            case "19" :
                title.setText("本条VIP信息只针对个人债权会员免费开放，详情请咨询会员专线：010-56052557");
                break;
            default:
                break;
        }
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        //消费
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        to_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAuthMe() ;
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
        convertView.setEnabled(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                convertView.setEnabled(true);
            }
        });
    }

    private void loadAuthMe() {
        String urls = String.format(Url.Myicon, GetBenSharedPreferences.getTicket(context)) ;
        HttpUtils utils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //处理请求成功后的数据
                try {
                    dealResult(responseInfo.result);
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
        }) ;
    }

    private void dealResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        JSONObject user1 = jsonObject.getJSONObject("user");
        String right01 = user1.getString("right") ;
        right = context.getSharedPreferences("right", context.MODE_PRIVATE) ;
        right.edit().putString("right", right01).commit();
        JSONObject showright = user1.getJSONObject("showright");
        String type_01 = showright.optString("资产包");
        String type_02 = showright.optString("企业商账");
        String type_03 = showright.optString("固定资产");
        String type_04 = showright.optString("融资信息");
        String type_05 = showright.optString("个人债权");
        Intent intent = new Intent(context , VipCenterActivity.class ) ;
        intent.putExtra("type_01" , type_01 ) ;
        intent.putExtra("type_02" , type_02 ) ;
        intent.putExtra("type_03" , type_03 ) ;
        intent.putExtra("type_04" , type_04 ) ;
        intent.putExtra("type_05" , type_05 ) ;
        context.startActivity(intent);
    }

    private void loadData(final int position, final View finalConvertView) {
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
                    String role = jsonObject.getString("role");
                    if (GetBenSharedPreferences.getUserId(context).equals(list.get(position).getUserID())) {
                        String id = list.get(position).getProjectID();
                        Intent intent = new Intent(context, V2DetailsFindInfoActivity.class);
                        intent.putExtra("id", id);
                        context.startActivity(intent);
                        finalConvertView.setEnabled(true);
                    } else {
                        if ("1".equals(role)) {
                            if ("2".equals(list.get(position).getMember())) {
                                String urls = String.format(Url.ISPay, GetBenSharedPreferences.getTicket(context));
                                HttpUtils httpUtils = new HttpUtils();
                                RequestParams params1 = new RequestParams();
                                params1.addBodyParameter("ProjectID", list.get(position).getProjectID());
                                httpUtils.send(HttpRequest.HttpMethod.POST, urls, params1, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        try {
                                            JSONObject jsonObject1 = new JSONObject(responseInfo.result);
                                            String payFlag = jsonObject1.getString("PayFlag");
                                            if ("1".equals(payFlag)) {
                                                String id = list.get(position).getProjectID();
                                                Intent intent = new Intent(context, V2DetailsFindInfoActivity.class);
                                                intent.putExtra("id", id);
                                                context.startActivity(intent);
                                                finalConvertView.setEnabled(true);
                                            } else {
                                                showPopUpWindow(position, account, finalConvertView);

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(HttpException error, String msg) {
                                        finalConvertView.setEnabled(true);
                                    }
                                });
                            } else if ("1".equals(list.get(position).getMember())){
                                showForVipPop(finalConvertView , position ) ;
                            }

                        } else {
                            ToastUtils.shortToast(context, "您需要先通过服务方认证才可查看此条信息");
                            finalConvertView.setEnabled(true);
                        }
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
    }

    private void showPopUpWindow(final int position, String account, final View convertView) {
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
                goPay(window, position);
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
        info_type.setText("该信息为付费资源");
        info_title.setText("消耗芽币可查看详细信息");
        shejian_two.setVisibility(View.VISIBLE);
        shejian_price.setText(list.get(position).getPrice());
        shejian_balance.setText(account);
        if (Integer.parseInt(account) < Integer.parseInt(list.get(position).getPrice())) {
            balance_type.setVisibility(View.VISIBLE);
        } else {
            balance_type.setVisibility(View.GONE);
        }

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
        convertView.setEnabled(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                convertView.setEnabled(true);
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

    private void goPay(final PopupWindow window, final int position) {
        String url = String.format(Url.Pay, GetBenSharedPreferences.getTicket(context) ) ;
        HttpUtils httpUtils = new HttpUtils() ;
        RequestParams params = new RequestParams() ;
        params.addBodyParameter("ProjectID" , list.get(position).getProjectID() );
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("benben" , responseInfo.result ) ;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseInfo.result);
                    String status_code = jsonObject.getString("status_code");
                    switch (status_code){
                        case "200" :
                            list.get(position).setPayFlag("1");
                            window.dismiss();
                            ToastUtils.shortToast(context, "购买成功");
                            String id = list.get(position).getProjectID();
                            Intent intent = new Intent(context, V2DetailsFindInfoActivity.class);
                            intent.putExtra("id", id);
                            context.startActivity(intent);
                            break;
                        case "416" :
                            ToastUtils.shortToast(context , "非收费信息");
                            break;
                        case "417" :
                            ToastUtils.shortToast(context , "您已经支付过该条信息");
                            break;
                        case "418" :
                            ToastUtils.shortToast(context , "余额不足，请充值。");
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
                ToastUtils.shortToast( context , "网络连接异常，支付失败");
            }
        }) ;
    }

    private void goRechargeActivity(PopupWindow window) {
        Intent intent = new Intent(context , RechargeActivity.class ) ;
        context.startActivity(intent);
        window.dismiss();
    }

    static class ViewHolder {
        ImageView news_image;
        ImageView img;
        TextView news_title;
        TextView info_part;
        TextView info_part_one;
        TextView info_part_two;
        TextView info_no;
        TextView info_bee;
        TextView info_danwei;
        LinearLayout liangdian;
        TextView liangdian01;
        TextView liangdian02;
        TextView liangdian03;
        LinearLayout jiage;
        ImageView image_01;
        ImageView image_02;
        TextView text_01;
        TextView text_02;

        RelativeLayout czgg_relative ;
        RelativeLayout relative_info ;
        ImageView czgg_image ;
        TextView czgg_title ;
        TextView news_des ;
        TextView news_time ;
        TextView new_author ;
        ImageView news_cooperateState ;

    }

    public void addAll(Collection<? extends V2InfoEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}