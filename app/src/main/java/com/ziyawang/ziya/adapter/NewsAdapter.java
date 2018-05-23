package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsNewsActivity;
import com.ziyawang.ziya.entity.NewsEntity;
import com.ziyawang.ziya.tools.FormatCurrentData;
import com.ziyawang.ziya.tools.Url;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2018/5/18.
 */

public class NewsAdapter extends BaseAdapter {

    private Context context ;
    private List<NewsEntity> list ;

    public NewsAdapter(){}

    public NewsAdapter(Context context , List<NewsEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_news_items, parent , false);
            holder = new ViewHolder() ;
            holder.image_newsLogo = (ImageView) convertView.findViewById(R.id.image_newsLogo);
            holder.text_newsTitle = (TextView)convertView.findViewById(R.id.text_newsTitle);
            holder.text_newsLabel = (TextView)convertView.findViewById(R.id.text_newsLabel);
            holder.text_newsTime = (TextView)convertView.findViewById(R.id.text_newsTime);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(Url.FileIP + list.get(position).getNewsLogo()).into(holder.image_newsLogo) ;
        //.transform(new GlideRoundTransform(context, 20))
        holder.text_newsTitle.setText(list.get(position).getNewsTitle());
        //标签长度大于十个字 默认显示精选
        if (TextUtils.isEmpty(list.get(position).getTarget())){
            holder.text_newsLabel.setVisibility(View.GONE);
        }else if (list.get(position).getTarget().length() >10 ){
            holder.text_newsLabel.setVisibility(View.VISIBLE);
            holder.text_newsLabel.setText("精选");
        }else {
            holder.text_newsLabel.setVisibility(View.VISIBLE);
            holder.text_newsLabel.setText(list.get(position).getTarget());
        }
        holder.text_newsTime.setText(FormatCurrentData.getTimeRange(list.get(position).getPublishTime()));

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
        ImageView image_newsLogo ;
        TextView text_newsTitle ;
        TextView text_newsLabel ;
        TextView text_newsTime ;

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
