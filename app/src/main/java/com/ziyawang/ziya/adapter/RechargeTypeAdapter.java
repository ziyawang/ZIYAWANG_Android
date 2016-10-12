package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.entity.RechargeTypeEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/9/25.
 */
public class RechargeTypeAdapter extends BaseAdapter {

    private Context context;
    private List<RechargeTypeEntity> list;

    public RechargeTypeAdapter() {
    }

    public RechargeTypeAdapter(Context context, List<RechargeTypeEntity> list) {
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

            convertView = LayoutInflater.from(context).inflate(R.layout.rechargetype, parent, false);
            holder = new ViewHolder();
            holder.recharge_type_yabi = (TextView) convertView.findViewById(R.id.recharge_type_yabi);
            holder.recharge_type_renminbi = (TextView) convertView.findViewById(R.id.recharge_type_renminbi);
            holder.recharge_type_select = (ImageView) convertView.findViewById(R.id.recharge_type_select);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.recharge_type_yabi.setText(list.get(position).getYBCount() + "芽币");
        holder.recharge_type_renminbi.setText("充值：" + Integer.parseInt(list.get(position).getRealMoney()) / 100 + "元");
        if ("1".equals(list.get(position).getSelected())){
            holder.recharge_type_select.setVisibility(View.VISIBLE);
        }else {
            holder.recharge_type_select.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView recharge_type_yabi ;
        TextView recharge_type_renminbi ;
        ImageView recharge_type_select ;
    }

    public void addAll(Collection<? extends RechargeTypeEntity> collection) {

        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
}