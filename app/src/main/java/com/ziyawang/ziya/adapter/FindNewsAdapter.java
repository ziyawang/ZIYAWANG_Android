package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsFindServiceActivity;
import com.ziyawang.ziya.entity.FindNewsEntity;
import com.ziyawang.ziya.entity.FindServiceEntity;
import com.ziyawang.ziya.tools.Url;

import java.util.Collection;
import java.util.List;
/**
 * Created by 牛海丰 on 2016/10/31.
 */

/**
 * Created by 牛海丰 on 2016/7/25.
 */
public class FindNewsAdapter extends BaseAdapter {

    private Context context ;
    private List<FindNewsEntity> list ;

    public FindNewsAdapter(){}

    public FindNewsAdapter(Context context , List<FindNewsEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.find_news_items, parent , false);
            holder = new ViewHolder() ;
            holder.news_image = (ImageView)convertView.findViewById(R.id.news_image ) ;
            holder.news_title = (TextView)convertView.findViewById(R.id.news_title ) ;
            holder.news_des = (TextView)convertView.findViewById(R.id.news_des ) ;
            holder.news_time = (TextView)convertView.findViewById(R.id.news_time ) ;
            holder.news_count = (TextView)convertView.findViewById(R.id.news_count ) ;
            TextPaint tp = holder.news_title.getPaint();
            tp.setFakeBoldText(true);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.news_title.setText(list.get(position).getNewsTitle());
        holder.news_des.setText(Html.fromHtml(list.get(position).getBrief().replace("　　", "")));
        String substring01 = list.get(position).getPublishTime().substring(5, 10);
        String substring02 = list.get(position).getPublishTime().substring(11, 16);
        holder.news_time.setText(substring01 + "/" + substring02 );
        holder.news_count.setText(list.get(position).getViewCount());

        if (!TextUtils.isEmpty(list.get(position).getNewsThumb())){
            BitmapUtils bitmapUtils = new BitmapUtils(context ) ;
            bitmapUtils.display(holder.news_image , Url.FileIP + list.get(position).getNewsThumb());
        }

        return convertView;
    }

    static class  ViewHolder{
        ImageView news_image ;
        TextView news_title ;
        TextView news_des ;
        TextView news_time ;
        TextView news_count ;
    }

    public void addAll(Collection<? extends FindNewsEntity> collection) {

        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){

        list.clear();
        notifyDataSetChanged();
    }
}
