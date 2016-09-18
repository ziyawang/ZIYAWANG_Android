package com.ziyawang.ziya.adapter;

/**
 * Created by 牛海丰 on 2016/8/18.
 */
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsFindInfoActivity;
import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.tools.ToastUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/7/23.
 */
public class MyReleaseAdapter extends BaseAdapter {

    private Context context ;
    private List<FindInfoEntity> list ;

    public MyReleaseAdapter(){}

    public MyReleaseAdapter(Context context , List<FindInfoEntity> list ){
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
        final ViewHolder holder ;
        if(convertView == null ){
            convertView = LayoutInflater.from(context).inflate(R.layout.my_release_items, parent , false);
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

            holder.a_one = (ImageView)convertView.findViewById(R.id.a_one);
            holder.a_two = (ImageView)convertView.findViewById(R.id.a_two);
            holder.a_three = (ImageView)convertView.findViewById(R.id.a_three);

            holder.money_transfer_info_02 = (LinearLayout)convertView.findViewById(R.id.money_transfer_info_02);
            holder.money_transfer_info_03 = (LinearLayout)convertView.findViewById(R.id.money_transfer_info_03);

            holder.wordDes = (TextView)convertView.findViewById(R.id.wordDes) ;

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();

        }

        String certifyState = list.get(position).getCertifyState();
        switch (certifyState){
            case "0" :
                holder.a_three.setVisibility(View.VISIBLE);
                holder.a_one.setVisibility(View.GONE);
                holder.a_two.setVisibility(View.GONE);
                break;
            case "1" :
                holder.a_one.setVisibility(View.VISIBLE);
                holder.a_three.setVisibility(View.GONE);
                holder.a_two.setVisibility(View.GONE);
                break;
            case "2" :
                holder.a_two.setVisibility(View.VISIBLE);
                holder.a_three.setVisibility(View.GONE);
                holder.a_one.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String certifyState = list.get(position).getCertifyState();
                switch (certifyState){
                    case "0" :
                        ToastUtils.shortToast(context , "正在审核中");
                        break;
                    case "1" :
                        String id = list.get(position).getProjectID() ;
                        Intent intent = new Intent(context , DetailsFindInfoActivity.class ) ;
                        intent.putExtra("id" ,id) ;
                        intent.putExtra("title" , list.get(position).getTypeName()) ;

                        Log.e("benben01", list.get(position).getProjectID()) ;
                        Log.e("benben01", list.get(position).getTypeName()) ;
                        context.startActivity(intent);
                        break;
                    case "2" :
                        ToastUtils.shortToast(context , "审核未通过，信息不符，重新提交。");
                        break;
                    default:
                        break;
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

                holder.money_transfer_area_left.setText("地区：");
                holder.money_transfer_from_left.setText("方式：");
                holder.money_transfer_type_left.setText("类型：");

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
        }else {
            holder.info_vip.setVisibility(View.INVISIBLE);
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
        TextView niu_three ;
        TextView money_transfer_money_02_up ;
        TextView niu_four ;
        ImageView imageView2 ;
        ImageView niu_one ;
        ImageView niu_two ;
        ImageView info_vip ;
        ImageView a_one ;
        ImageView a_two ;
        ImageView a_three ;

        LinearLayout money_transfer_info_02 ;
        LinearLayout money_transfer_info_03 ;

        LinearLayout niu ;

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
