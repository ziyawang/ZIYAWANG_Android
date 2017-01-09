package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.entity.VipRecordEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/12/15.
 */
public class VipRecordAdapter extends BaseAdapter {

    private Context context;
    private List<VipRecordEntity> list;

    public VipRecordAdapter() {}

    public VipRecordAdapter(Context context, List<VipRecordEntity> list) {
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

            convertView = LayoutInflater.from(context).inflate(R.layout.vip_record_items, parent, false);
            holder = new ViewHolder();
            holder.gold_items_type = (TextView) convertView.findViewById(R.id.gold_items_type);
            holder.gold_items_time = (TextView) convertView.findViewById(R.id.gold_items_time);
            holder.gold_items_balance = (TextView) convertView.findViewById(R.id.gold_items_balance);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if ("0".equals(list.get(position).getPayMoney())){
            holder.gold_items_balance.setText("+0￥");
        }else {
            holder.gold_items_balance.setText("+" + list.get(position).getPayMoney().substring( 0 , list.get(position).getPayMoney().length()-2) + "￥");
        }

        holder.gold_items_balance.setTextColor(Color.rgb(144, 195, 31));
        holder.gold_items_type.setText(list.get(position).getOperates());
        holder.gold_items_time.setText("开通时间：" + list.get(position).getStartTime() );
        holder.time.setText("到期时间：" + list.get(position).getEndTime() );
        return convertView;
    }

    static class ViewHolder {
        TextView gold_items_type ;
        TextView gold_items_time ;
        TextView gold_items_balance ;
        TextView time ;
    }

    public void addAll(Collection<? extends VipRecordEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}

