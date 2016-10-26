package com.ziyawang.ziya.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsFindInfoActivity;
import com.ziyawang.ziya.activity.FindInfoActivity;
import com.ziyawang.ziya.activity.LoginActivity;
import com.ziyawang.ziya.activity.RechargeActivity;
import com.ziyawang.ziya.application.MyApplication;
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.tools.GetBenSharedPreferences;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Member;
import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/7/23.
 */
public class FindInfoAdapter extends BaseAdapter {

    private Activity context ;
    private List<FindInfoEntity> list ;

    public FindInfoAdapter(){}

    public FindInfoAdapter(Activity context , List<FindInfoEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.find_info_items, parent , false);
            holder = new ViewHolder() ;

            holder.niu = (LinearLayout)convertView.findViewById(R.id.niu) ;
            holder.money_transfer_title = (TextView)convertView.findViewById(R.id.money_transfer_title);
            holder.money_transfer_no = (TextView)convertView.findViewById(R.id.money_transfer_no);
            holder.money_transfer_area_left = (TextView)convertView.findViewById(R.id.money_transfer_area_left);
            holder.money_transfer_area_right = (TextView)convertView.findViewById(R.id.money_transfer_area_right);
            holder.money_transfer_from_left = (TextView)convertView.findViewById(R.id.money_transfer_from_left);
            holder.money_transfer_from_right = (TextView)convertView.findViewById(R.id.money_transfer_from_right);
            holder.money_transfer_type_left = (TextView)convertView.findViewById(R.id.money_transfer_type_left);
            holder.money_transfer_type_right = (TextView)convertView.findViewById(R.id.money_transfer_type_right);
            holder.money_transfer_money_top = (TextView)convertView.findViewById(R.id.money_transfer_money_top);
            //holder.money_transfer_money_down = (TextView)convertView.findViewById(R.id.money_transfer_money_down);
            holder.money_transfer_money_02_up = (TextView)convertView.findViewById(R.id.money_transfer_money_02_up);
            //holder.money_transfer_money_02_down = (TextView)convertView.findViewById(R.id.money_transfer_money_02_down);
            holder.imageView2 = (ImageView)convertView.findViewById(R.id.imageView2);
            holder.niu_one = (ImageView)convertView.findViewById(R.id.niu_one);
            holder.niu_two = (ImageView)convertView.findViewById(R.id.niu_two);
            holder.niu_three = (TextView)convertView.findViewById(R.id.niu_three);
            holder.niu_four = (TextView)convertView.findViewById(R.id.niu_four);
            holder.info_vip = (ImageView)convertView.findViewById(R.id.info_vip);
            holder.money_transfer_info_02 = (LinearLayout)convertView.findViewById(R.id.money_transfer_info_02 ) ;
            holder.money_transfer_info_03 = (LinearLayout)convertView.findViewById(R.id.money_transfer_info_03 ) ;
            holder.wordDes = (TextView)convertView.findViewById(R.id.wordDes) ;
            holder.info_account_textView = (TextView)convertView.findViewById(R.id.info_account_textView) ;
            holder.info_account_linear = (LinearLayout)convertView.findViewById(R.id.info_account_linear) ;
            //字体加粗设置
            TextPaint tp = holder.info_account_textView.getPaint();
            tp.setFakeBoldText(true);
            TextPaint tp1 = holder.money_transfer_money_top.getPaint();
            tp1.setFakeBoldText(true);
            TextPaint tp2 = holder.money_transfer_money_02_up.getPaint();
            tp2.setFakeBoldText(true);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();

        }

        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //未登录，去登陆，登陆之后，判断是否是收费资源，是收费资源，那么直接调用anthme 的接口，自己的
                finalConvertView.setEnabled(false);
                if ("2".equals(list.get(position).getMember())){
                    if (GetBenSharedPreferences.getIsLogin(context)){
                        //调用authme的接口，拿到自己的role 和 account
                        loadData(position , finalConvertView) ;

                    }else {
                        Intent intent = new Intent(context , LoginActivity.class ) ;
                        context.startActivity(intent );
                        finalConvertView.setEnabled(true);
                    }

                }else {
                    String id = list.get(position).getProjectID();
                    Intent intent = new Intent(context, DetailsFindInfoActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("title", list.get(position).getTypeName());
                    Log.e("benben01", list.get(position).getProjectID());
                    Log.e("benben01", list.get(position).getTypeName());
                    context.startActivity(intent);
                    finalConvertView.setEnabled(true);
                }

            }
        });

        if (list.get(position).getProArea().contains("-")){
            String[] split = list.get(position).getProArea().split("-");
            holder.money_transfer_area_right.setText(split[0].toString().trim());
        }else {
            holder.money_transfer_area_right.setText(list.get(position).getProArea());
        }
        holder.wordDes.setText(list.get(position).getWordDes());
        switch (list.get(position).getTypeName()){
            case "投资需求" :
                holder.niu_three.setText("%");
                holder.niu_four.setText("年");
                holder.money_transfer_no.setText(list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.money_transfer_from_right.setText(list.get(position).getInvestType());
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getRate());
                holder.money_transfer_money_02_up.setText(list.get(position).getYear());
                holder.niu_one.setImageResource(R.mipmap.icon24);
                holder.niu_two.setImageResource(R.mipmap.year32);

                holder.money_transfer_area_left.setText("投资地区：");
                holder.money_transfer_from_left.setText("投资方式：");
                holder.money_transfer_type_left.setText("投资类型：");
                holder.niu.setVisibility(View.VISIBLE);
                holder.money_transfer_from_left.setVisibility(View.VISIBLE);
                holder.money_transfer_from_right.setVisibility(View.VISIBLE);

                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.niu_three.setVisibility(View.VISIBLE);
                holder.niu_four.setVisibility(View.VISIBLE);
                holder.money_transfer_money_top.setVisibility(View.VISIBLE);
                holder.money_transfer_money_02_up.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.VISIBLE);

                break;
            case "资产包转让" :

                holder.money_transfer_from_left.setVisibility(View.VISIBLE);
                holder.money_transfer_from_right.setVisibility(View.VISIBLE);
                holder.money_transfer_area_left.setText("地区：");
                holder.money_transfer_from_left.setText("来源：");
                holder.money_transfer_type_left.setText("类型：");
                holder.niu.setVisibility(View.VISIBLE);
                holder.niu_four.setText("万");
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText( list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.money_transfer_from_right.setText(list.get(position).getFromWhere());
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getTotalMoney());
                holder.money_transfer_money_02_up.setText(list.get(position).getTransferMoney());
                holder.niu_one.setImageResource(R.mipmap.icon22);
                holder.niu_two.setImageResource(R.mipmap.icon17);

                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.niu_three.setVisibility(View.VISIBLE);
                holder.niu_four.setVisibility(View.VISIBLE);
                holder.money_transfer_money_top.setVisibility(View.VISIBLE);
                holder.money_transfer_money_02_up.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.VISIBLE);

                break;
            case "委外催收" :
                holder.niu.setVisibility(View.VISIBLE);
                holder.money_transfer_from_left.setVisibility(View.VISIBLE);
                holder.money_transfer_from_right.setVisibility(View.VISIBLE);
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText(list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                holder.money_transfer_area_left.setText("债务人所在地：");
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.money_transfer_from_left.setText("状态：");
                holder.money_transfer_from_right.setText(list.get(position).getStatus());
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getTotalMoney());
                //holder.money_transfer_money_down.setText("金额");
                holder.money_transfer_money_02_up.setText(list.get(position).getRate());
                holder.money_transfer_money_02_up.setVisibility(View.VISIBLE);
                holder.niu_four.setVisibility(View.GONE);

                holder.niu_one.setImageResource(R.mipmap.icon16);
                holder.niu_two.setImageResource(R.mipmap.icon21);

                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.niu_three.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.VISIBLE);
                //holder.money_transfer_money_02_down.setText("佣金比例");
                //Drawable drawable01= context.getResources().getDrawable(R.mipmap.info_yongjin);
                /// 这一步必须要做,否则不会显示.
                //drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
                //holder.money_transfer_money_02_down.setCompoundDrawables(drawable01, null, null, null);
                break;
            case "法律服务" :
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText(list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getRequirement());
                holder.niu_three.setVisibility(View.GONE);
                holder.niu_two.setVisibility(View.GONE);
                holder.niu_four.setVisibility(View.GONE);

                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.niu_four.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.GONE);

                holder.niu_one.setImageResource(R.mipmap.icon25);
                //holder.money_transfer_money_down.setText("需求");
                //Drawable drawable= context.getResources().getDrawable(R.mipmap.info_reqiirement);
                /// 这一步必须要做,否则不会显示.
                //drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                //holder.money_transfer_money_down.setCompoundDrawables(drawable,null,null,null);
                //holder.money_transfer_money_02_up.setVisibility(View.INVISIBLE);
                //holder.money_transfer_money_02_down.setVisibility(View.INVISIBLE);
                break;
            case "商业保理" :
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText(list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
                holder.money_transfer_type_left.setText("买方性质：");
                holder.money_transfer_type_right.setText(list.get(position).getBuyerNature());
                holder.money_transfer_money_top.setText(list.get(position).getTotalMoney());
                //holder.money_transfer_money_down.setText("合同金额");
                holder.niu_one.setImageResource(R.mipmap.icon18);
                holder.niu_two.setVisibility(View.GONE);
                holder.niu_four.setVisibility(View.GONE);
                holder.money_transfer_money_02_up.setVisibility(View.INVISIBLE);
                //holder.money_transfer_money_02_down.setVisibility(View.INVISIBLE);
                holder.niu_two.setVisibility(View.GONE);
                holder.niu_four.setVisibility(View.GONE);

                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.niu_three.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.GONE);
                break;
            case "融资需求" :
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText( list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
                holder.money_transfer_type_left.setText("方式：");
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getTotalMoney());
                //holder.money_transfer_money_down.setText("金额");
                holder.money_transfer_money_02_up.setText(list.get(position).getRate()+"%");
                holder.money_transfer_money_02_up.setVisibility(View.VISIBLE);
                //holder.money_transfer_money_02_down.setText("回报率");
                //Drawable drawable02= context.getResources().getDrawable(R.mipmap.info_re_rata);
                /// 这一步必须要做,否则不会显示.
                //drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
                //holder.money_transfer_money_02_down.setCompoundDrawables(drawable02, null, null, null);
                holder.niu_one.setImageResource(R.mipmap.icon16);
                holder.niu_two.setImageResource(R.mipmap.icon24);

                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.niu_three.setVisibility(View.VISIBLE);
                holder.niu_four.setVisibility(View.GONE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.VISIBLE);
                break;
            //功能隐藏
            case "典当担保" :
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText(list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
                holder.money_transfer_money_02_up.setVisibility(View.INVISIBLE);
                //holder.money_transfer_money_02_down.setVisibility(View.INVISIBLE);
                //holder.money_transfer_money_down.setText("金额");
                holder.money_transfer_money_top.setText(list.get(position).getTotalMoney());
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.niu_two.setVisibility(View.GONE);
                holder.niu_four.setVisibility(View.GONE);

                holder.niu_one.setImageResource(R.mipmap.icon16);

                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.niu_three.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.GONE);
                break;
            case "悬赏信息" :
                holder.money_transfer_no.setText(  list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                holder.money_transfer_area_left.setText("目标地区：");
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getTotalMoney());
                //holder.money_transfer_money_down.setText("金额");
                holder.money_transfer_money_02_up.setVisibility(View.INVISIBLE);
                holder.niu_three.setText("元");
                //holder.money_transfer_money_02_down.setVisibility(View.INVISIBLE);
                holder.niu_two.setVisibility(View.GONE);
                holder.niu_four.setVisibility(View.GONE);

                holder.niu_one.setImageResource(R.mipmap.icon16);
                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.niu_three.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.GONE);
                break;
            case "尽职调查" :
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText(  list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getInformant());
                //holder.money_transfer_money_down.setText("被调查方");
                //Drawable drawable03= context.getResources().getDrawable(R.mipmap.info_be_diaocha);
                /// 这一步必须要做,否则不会显示.
                //drawable03.setBounds(0, 0, drawable03.getMinimumWidth(), drawable03.getMinimumHeight());
                //holder.money_transfer_money_down.setCompoundDrawables(drawable03, null, null, null);
                holder.money_transfer_money_02_up.setVisibility(View.INVISIBLE);
                //holder.money_transfer_money_02_down.setVisibility(View.INVISIBLE);
                holder.niu_one.setImageResource(R.mipmap.icon23);
                holder.niu_two.setVisibility(View.GONE);
                holder.niu_four.setVisibility(View.GONE);
                holder.niu_three.setVisibility(View.GONE);

                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_four.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.GONE);
                break;
            case "固产转让" :
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText(list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.VISIBLE);
                holder.money_transfer_from_left.setVisibility(View.VISIBLE);
                holder.money_transfer_from_left.setText("标的物：");
                holder.money_transfer_from_right.setVisibility(View.VISIBLE);
                holder.money_transfer_from_right.setText(list.get(position).getCorpore());
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getTransferMoney() );
                //holder.money_transfer_money_down.setText("转让价");
                //Drawable drawable04= context.getResources().getDrawable(R.mipmap.info_trans_money);
                /// 这一步必须要做,否则不会显示.
                //drawable04.setBounds(0, 0, drawable04.getMinimumWidth(), drawable04.getMinimumHeight());
                //holder.money_transfer_money_down.setCompoundDrawables(drawable04, null, null, null);
                holder.money_transfer_money_02_up.setVisibility(View.INVISIBLE);
                //holder.money_transfer_money_02_down.setVisibility(View.INVISIBLE);

                holder.niu_one.setImageResource(R.mipmap.icon17);
                holder.niu_two.setVisibility(View.GONE);
                holder.niu_four.setVisibility(View.GONE);

                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.niu_three.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.GONE);
                break;
            case "资产求购" :
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText(  list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getBuyer());
                holder.niu_one.setImageResource(R.mipmap.icon20);
                holder.niu_two.setVisibility(View.GONE);
                holder.niu_four.setVisibility(View.GONE);
                holder.niu_three.setVisibility(View.GONE);

                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.GONE);
                //holder.money_transfer_money_down.setText("求购方");
                //Drawable drawable05= context.getResources().getDrawable(R.mipmap.info_buy);
                /// 这一步必须要做,否则不会显示.
                //drawable05.setBounds(0, 0, drawable05.getMinimumWidth(), drawable05.getMinimumHeight());
                //holder.money_transfer_money_down.setCompoundDrawables(drawable05, null, null, null);
                holder.money_transfer_money_02_up.setVisibility(View.INVISIBLE);
                //holder.money_transfer_money_02_down.setVisibility(View.INVISIBLE);
                break;
            case "债权转让" :
                holder.niu_three.setText("万");
                holder.niu_four.setText("万");
                holder.money_transfer_no.setText( list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                //holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getTotalMoney());
                //holder.money_transfer_money_down.setText("总金额");
                holder.money_transfer_money_02_up.setText(list.get(position).getTransferMoney());
                holder.money_transfer_money_02_up.setVisibility(View.VISIBLE);
                holder.niu_one.setImageResource(R.mipmap.icon16);
                holder.niu_two.setImageResource(R.mipmap.icon17);
                holder.niu_one.setVisibility(View.VISIBLE);
                holder.niu_two.setVisibility(View.VISIBLE);
                holder.niu_three.setVisibility(View.VISIBLE);
                holder.niu_four.setVisibility(View.VISIBLE);
                holder.money_transfer_info_02.setVisibility(View.VISIBLE);
                holder.money_transfer_info_03.setVisibility(View.VISIBLE);

                break;
        }

        String member = list.get(position).getMember();
        if ( member.equals("1")){
            holder.info_vip.setVisibility(View.VISIBLE);
            holder.info_vip.setImageResource(R.mipmap.vipresources);
            holder.info_account_linear.setVisibility(View.GONE);
        }else if (member.equals("2")){
            holder.info_vip.setVisibility(View.VISIBLE);
            holder.info_vip.setImageResource(R.mipmap.moneyresources);
            holder.info_account_linear.setVisibility(View.VISIBLE);
            holder.info_account_textView.setText(list.get(position).getPrice());
        }else {
            holder.info_vip.setVisibility(View.GONE);
            holder.info_account_linear.setVisibility(View.GONE);
        }


        return convertView;
    }

    private void loadData(final int position, final View finalConvertView) {
        if (GetBenSharedPreferences.getIsLogin(context)){
            String urls = String.format(Url.Myicon, GetBenSharedPreferences.getTicket(context)) ;
            HttpUtils utils = new HttpUtils() ;
            final RequestParams params = new RequestParams() ;
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
                            Intent intent = new Intent(context, DetailsFindInfoActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("title", list.get(position).getTypeName());
                            Log.e("benben01", list.get(position).getProjectID());
                            Log.e("benben01", list.get(position).getTypeName());
                            context.startActivity(intent);
                            finalConvertView.setEnabled(true);
                        } else {
                            if ("1".equals(role)) {
//                                if ("1".equals(list.get(position).getPayFlag())){
//                                    String id = list.get(position).getProjectID();
//                                    Intent intent = new Intent(context, DetailsFindInfoActivity.class);
//                                    intent.putExtra("id", id);
//                                    intent.putExtra("title", list.get(position).getTypeName());
//                                    Log.e("benben01", list.get(position).getProjectID());
//                                    Log.e("benben01", list.get(position).getTypeName());
//                                    context.startActivity(intent);
//                                }else {
//                                    showPopUpWindow(position, account);
//                                }
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
                                                Intent intent = new Intent(context, DetailsFindInfoActivity.class);
                                                intent.putExtra("id", id);
                                                intent.putExtra("title", list.get(position).getTypeName());
                                                Log.e("benben01", list.get(position).getProjectID());
                                                Log.e("benben01", list.get(position).getTypeName());
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

                            } else {
                                ToastUtils.shortToast(context, "您需要先通过服务方认证才可查看收费类信息");
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
            }) ;
        }else {
            Intent intent = new Intent(context , LoginActivity.class ) ;
            context.startActivity(intent);
        }
    }

    private void showPopUpWindow(final int position, String account, final View convertView) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_publish, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RelativeLayout relative = (RelativeLayout)view.findViewById(R.id.relative ) ;
        final TextView info_type = (TextView)view.findViewById(R.id.info_type ) ;
        final TextView info_title = (TextView)view.findViewById(R.id.info_title ) ;
        final TextView shejian_price = (TextView)view.findViewById(R.id.shejian_price ) ;
        final TextView shejian_balance = (TextView)view.findViewById(R.id.shejian_balance ) ;
        final TextView balance_type = (TextView)view.findViewById(R.id.balance_type ) ;
        final Button shejian_pay = (Button)view.findViewById(R.id.shejian_pay ) ;
        final Button shejian_recharge = (Button)view.findViewById(R.id.shejian_recharge ) ;
        final ImageButton pay_cancel = (ImageButton)view.findViewById(R.id.pay_cancel ) ;
        final LinearLayout shejian_two = (LinearLayout)view.findViewById(R.id.shejian_two ) ;
        TextPaint tp = shejian_price.getPaint();
        tp.setFakeBoldText(true);
        TextPaint tp01 = shejian_balance.getPaint();
        tp01.setFakeBoldText(true);

        //消费
        shejian_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去消费
                goPay(window , position );
            }
        });
        //充值
        shejian_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到充值页面
                goRechargeActivity(window) ;
            }
        });

        //Member 2收费信息 其他为不收费信息
        info_type.setText("该信息为付费资源");
        info_title.setText("消耗芽币可查看详细信息");
        shejian_two.setVisibility(View.VISIBLE);
        shejian_price.setText(list.get(position).getPrice());
        shejian_balance.setText(account);
        if (Integer.parseInt(account) < Integer.parseInt(list.get(position).getPrice())){
            balance_type.setVisibility(View.VISIBLE);
        }else {
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
                            Intent intent = new Intent(context, DetailsFindInfoActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("title", list.get(position).getTypeName());
                            Log.e("benben01", list.get(position).getProjectID());
                            Log.e("benben01", list.get(position).getTypeName());
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


    static class ViewHolder{

        TextView money_transfer_title ;
        TextView money_transfer_no ;
        TextView money_transfer_area_left ;
        TextView money_transfer_area_right ;
        TextView money_transfer_from_left ;
        TextView money_transfer_from_right ;
        TextView money_transfer_type_left ;
        TextView money_transfer_type_right ;
        TextView money_transfer_money_top ;
        TextView niu_three ;
        TextView money_transfer_money_02_up ;
        TextView niu_four ;
        ImageView imageView2 ;
        ImageView niu_one ;
        ImageView niu_two ;
        ImageView info_vip ;

        LinearLayout money_transfer_info_02 ;
        LinearLayout money_transfer_info_03 ;

        LinearLayout niu ;
        LinearLayout info_account_linear ;
        TextView info_account_textView ;

        TextView wordDes ;

    }

    public void addAll(Collection<? extends FindInfoEntity> collection) {

        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){

        list.clear();
        notifyDataSetChanged();
    }
}
