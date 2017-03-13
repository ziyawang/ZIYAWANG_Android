package com.ziyawang.ziya.adapter;

import android.app.Activity;
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

import com.lidroid.xutils.BitmapUtils;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.V2DetailsFindInfoActivity;
import com.ziyawang.ziya.entity.V2InfoEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/11/30.
 */
public class V2MyReleaseAdapter extends BaseAdapter {

    private Activity context;
    private List<V2InfoEntity> list;

    public V2MyReleaseAdapter() {
    }

    public V2MyReleaseAdapter(Activity context, List<V2InfoEntity> list) {
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
            holder.czgg_relative = (RelativeLayout) convertView.findViewById(R.id.czgg_relative);
            holder.news_cooperateState = (ImageView)convertView.findViewById(R.id.news_cooperateState ) ;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.czgg_relative.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(list.get(position).getPictureDes1())){
            BitmapUtils bitmapUtils = new BitmapUtils(context);
            bitmapUtils.display(holder.news_image, Url.FileIP + list.get(position).getPictureDes1());
        }
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
                if ("6".equals(list.get(position).getTypeID()) || "17".equals(list.get(position).getTypeID()) ||"20".equals(list.get(position).getTypeID())||"21".equals(list.get(position).getTypeID())||"22".equals(list.get(position).getTypeID())){
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
                holder.image_01.setImageResource(R.mipmap.v2shichang);
                holder.image_02.setImageResource(R.mipmap.v2zhuanrang);
                holder.text_01.setText(list.get(position).getTotalMoney() + "万");
                holder.text_02.setText(list.get(position).getTransferMoney() + "万");
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
                holder.text_01.setText(list.get(position).getMarketPrice() + "万");
                holder.text_02.setText(list.get(position).getTransferMoney() + "万");
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
        holder.info_bee.setVisibility(View.GONE);
        holder.img.setVisibility(View.VISIBLE);
        String certifyState = list.get(position).getCertifyState();
        switch (certifyState){
            case "0" :
                holder.img.setImageResource(R.mipmap.icon96);
                break;
            case "1" :
                holder.img.setImageResource(R.mipmap.icon94);
                break;
            case "2" :
                holder.img.setImageResource(R.mipmap.icon95);
                break;
            default:
                holder.img.setVisibility(View.GONE);
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
                        Intent intent = new Intent(context , V2DetailsFindInfoActivity.class ) ;
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

        return convertView;
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
