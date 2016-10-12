package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.entity.GoldDetailsEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/9/24.
 */

public class GoldDetailsAdapter extends BaseAdapter {

    private Context context;
    private List<GoldDetailsEntity> list;

    public GoldDetailsAdapter() {
    }

    public GoldDetailsAdapter(Context context, List<GoldDetailsEntity> list) {
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

            convertView = LayoutInflater.from(context).inflate(R.layout.gold_details_items, parent, false);
            holder = new ViewHolder();
            holder.gold_items_type = (TextView) convertView.findViewById(R.id.gold_items_type);
            holder.gold_items_time = (TextView) convertView.findViewById(R.id.gold_items_time);
            holder.gold_items_balance = (TextView) convertView.findViewById(R.id.gold_items_balance);
            holder.gold_items_color = (TextView) convertView.findViewById(R.id.gold_items_color);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //1是充值，2是消费
        if ("1".equals(list.get(position).getType())){
            holder.gold_items_type.setText("芽币充值");
            holder.gold_items_balance.setText("+" + list.get(position).getMoney());
            holder.gold_items_balance.setTextColor(Color.rgb(144,195,31));
            holder.gold_items_color.setTextColor(Color.rgb(144,195,31));
        }else if ("2".equals(list.get(position).getType())){
            holder.gold_items_type.setText("付费约谈");
            holder.gold_items_balance.setText("-" + list.get(position).getMoney());
            holder.gold_items_balance.setTextColor(Color.rgb(235,102,90));
            holder.gold_items_color.setTextColor(Color.rgb(235,102,90));
        }
        holder.gold_items_time.setText(list.get(position).getCreated_at());
        holder.gold_items_color.setText("芽币");
        return convertView;
    }

    static class ViewHolder {
        TextView gold_items_type ;
        TextView gold_items_time ;
        TextView gold_items_balance ;
        TextView gold_items_color ;
    }

    public void addAll(Collection<? extends GoldDetailsEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}
