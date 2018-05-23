package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsNewsActivity;
import com.ziyawang.ziya.entity.NewsEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2018/5/21.
 */

public class RecommendAdapter extends BaseAdapter {
    private Context context ;
    private List<NewsEntity> list ;

    public RecommendAdapter(){}

    public RecommendAdapter(Context context , List<NewsEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.recommend_news_items, parent , false);
            holder = new ViewHolder() ;
            holder.text_left01 = (TextView)convertView.findViewById(R.id.text_left01 ) ;
            holder.text_left02 = (TextView)convertView.findViewById(R.id.text_left02 ) ;
            holder.text_right = (TextView)convertView.findViewById(R.id.text_right ) ;
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0 || position == 0 ){
            holder.text_left01.setVisibility(View.VISIBLE);
            holder.text_left02.setVisibility(View.GONE);
        }else {
            holder.text_left01.setVisibility(View.GONE);
            holder.text_left02.setVisibility(View.VISIBLE);
        }
        holder.text_right.setText(list.get(position).getNewsTitle());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailsNewsActivity.class ) ;
                intent.putExtra("id" , list.get(position).getNewsID()) ;
                intent.putExtra("target" , list.get(position).getTarget()) ;
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class  ViewHolder{

        TextView text_left01 ;
        TextView text_left02 ;
        TextView text_right ;

    }

    public void addAll(Collection<? extends NewsEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }
}
