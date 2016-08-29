package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.CustomDialog;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/7.
 */

public class MyRushAdapter extends BaseAdapter {

    private Context context ;
    private List<FindInfoEntity> list ;
    private String login ;

    public MyRushAdapter(){}

    public MyRushAdapter(Context context , List<FindInfoEntity> list , String login ){
        super();
        this.context = context;
        this.list = list;
        this.login = login ;
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

            convertView = LayoutInflater.from(context).inflate(R.layout.my_rush_items, parent , false);
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
            holder.my_temawork_cancel = (TextView)convertView.findViewById(R.id.my_temawork_cancel);
            holder.my_temawork_relative02 = (RelativeLayout)convertView.findViewById(R.id.my_temawork_relative02);
            holder.money_transfer_linear = (RelativeLayout)convertView.findViewById(R.id.money_transfer_linear);
            holder.imageView2 = (ImageView)convertView.findViewById(R.id.imageView2);

            holder.niu_one = (ImageView)convertView.findViewById(R.id.niu_one);
            holder.niu_two = (ImageView)convertView.findViewById(R.id.niu_two);
            holder.niu_three = (TextView)convertView.findViewById(R.id.niu_three);
            holder.niu_four = (TextView)convertView.findViewById(R.id.niu_four);
            holder.info_vip = (ImageView)convertView.findViewById(R.id.info_vip ) ;

            holder.money_transfer_info_02 = (LinearLayout)convertView.findViewById(R.id.money_transfer_info_02 ) ;
            holder.money_transfer_info_03 = (LinearLayout)convertView.findViewById(R.id.money_transfer_info_03 ) ;

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.my_temawork_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CustomDialog.Builder builder01 = new CustomDialog.Builder(context);
                builder01.setTitle("亲爱的用户");
                builder01.setMessage("您确定要取消抢单 ？" );
                builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        //取消抢单的按钮。
                        String projectID = list.get(position).getProjectID();
                        String urls = String.format(Url.Cancle_Order, login);
                        Log.e("benben" , login ) ;
                        HttpUtils httpUtils = new HttpUtils() ;
                        RequestParams params = new RequestParams() ;
                        params.addBodyParameter("ProjectID" , projectID );
                        httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                if (dialog!=null){
                                    dialog.dismiss();
                                }
                                ToastUtils.shortToast(context, "取消抢单成功");
                                list.remove(position) ;
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                error.printStackTrace();
                                ToastUtils.shortToast(context , "取消抢单失败");
                                if (dialog!=null){
                                    dialog.dismiss();
                                }
                            }
                        }) ;
                    }
                });
                builder01.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder01.create().show();
            }
        });


        holder.my_temawork_relative02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = list.get(position).getProjectID() ;
                Intent intent = new Intent(context , DetailsFindInfoActivity.class ) ;
                intent.putExtra("id" ,id) ;
                intent.putExtra("title" , list.get(position).getTypeName()) ;
                context.startActivity(intent);

            }
        });
        holder.money_transfer_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = list.get(position).getProjectID() ;
                Intent intent = new Intent(context , DetailsFindInfoActivity.class ) ;
                intent.putExtra("id" ,id) ;
                intent.putExtra("title" , list.get(position).getTypeName()) ;
                context.startActivity(intent);

            }
        });

        if ("1".equals(list.get(position).getMember())){
            holder.info_vip.setVisibility(View.VISIBLE);
        }else {
            holder.info_vip.setVisibility(View.GONE);
        }
        switch (list.get(position).getTypeName()){
            case "资产包转让" :
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText( list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
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
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText(list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                holder.money_transfer_area_left.setText("债务人所在地：");
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.money_transfer_from_left.setText("状态：");
                holder.money_transfer_from_right.setText(list.get(position).getStatus());
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getTotalMoney());
                //holder.money_transfer_money_down.setText("金额");
                holder.money_transfer_money_02_up.setText(list.get(position).getRate());
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
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
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
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
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
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
                holder.money_transfer_type_left.setText("方式：");
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getTotalMoney());
                //holder.money_transfer_money_down.setText("金额");
                holder.money_transfer_money_02_up.setText(list.get(position).getRate()+"%");
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
            case "典当担保" :
                holder.niu_three.setText("万");
                holder.money_transfer_no.setText(list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
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
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
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
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
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
                holder.money_transfer_no.setText(  list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
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
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
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
                holder.money_transfer_no.setText( list.get(position).getProjectNumber());
                holder.money_transfer_title.setText(list.get(position).getTypeName());
                holder.money_transfer_area_right.setText(list.get(position).getProArea());
                holder.niu.setVisibility(View.GONE);
                holder.money_transfer_from_left.setVisibility(View.GONE);
                holder.money_transfer_from_right.setVisibility(View.GONE);
                holder.money_transfer_type_right.setText(list.get(position).getAssetType());
                holder.money_transfer_money_top.setText(list.get(position).getTotalMoney());
                //holder.money_transfer_money_down.setText("总金额");
                holder.money_transfer_money_02_up.setText(list.get(position).getTransferMoney());
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
        return convertView;
    }

    static class  ViewHolder{

        TextView money_transfer_title ;
        TextView money_transfer_no ;
        TextView money_transfer_area_left ;
        TextView money_transfer_area_right ;
        TextView money_transfer_from_left ;
        TextView money_transfer_from_right ;
        TextView money_transfer_type_left ;
        TextView money_transfer_type_right ;
        TextView money_transfer_money_top ;
        TextView money_transfer_money_down ;
        TextView money_transfer_money_02_up ;
        TextView money_transfer_money_02_down ;
        TextView my_temawork_cancel ;
        ImageView imageView2 ;
        RelativeLayout my_temawork_relative02 ;
        RelativeLayout money_transfer_linear ;

        ImageView niu_one ;
        ImageView niu_two ;
        TextView niu_three ;
        TextView niu_four ;
        ImageView info_vip ;

        LinearLayout money_transfer_info_02 ;
        LinearLayout money_transfer_info_03 ;

        LinearLayout niu ;

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
