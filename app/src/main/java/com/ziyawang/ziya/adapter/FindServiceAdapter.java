package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsFindInfoActivity;
import com.ziyawang.ziya.activity.DetailsFindServiceActivity;
import com.ziyawang.ziya.entity.FindServiceEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/7/25.
 */
public class FindServiceAdapter extends BaseAdapter {

    private Context context ;
    private List<FindServiceEntity> list ;

    public FindServiceAdapter(){}

    public FindServiceAdapter(Context context , List<FindServiceEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.find_service_items, parent , false);
            holder = new ViewHolder() ;
            holder.service_title = (TextView)convertView.findViewById(R.id.service_title);
            holder.service_part = (TextView)convertView.findViewById(R.id.service_part);
            holder.service_type = (TextView)convertView.findViewById(R.id.service_type);
            holder.service_level = (TextView)convertView.findViewById(R.id.service_level);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.service_title.setText(list.get(position).getServiceName());
        holder.service_part.setText(list.get(position).getServiceLocation());
        holder.service_type.setText(list.get(position).getServiceType());
        holder.service_level.setText(list.get(position).getServiceLevel());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = list.get(position).getServiceID() ;
                Intent intent = new Intent(context , DetailsFindServiceActivity.class ) ;
                intent.putExtra("id" ,id) ;
                context.startActivity(intent);

            }
        });
        return convertView;
    }

    static class  ViewHolder{

        TextView service_title ;
        TextView service_part ;
        TextView service_type ;
        TextView service_level ;

    }

    public void addAll(Collection<? extends FindServiceEntity> collection) {

        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){

        list.clear();
        notifyDataSetChanged();
    }
}
