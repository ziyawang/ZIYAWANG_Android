package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.VideoActivity;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.entity.VideoCommentsEntity;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyMovieImageView;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/12.
 */
public class VideoCommentsAdapter  extends BaseAdapter {

    private Context context ;
    private List<VideoCommentsEntity> list ;

    public VideoCommentsAdapter(){}

    public VideoCommentsAdapter(Context context , List<VideoCommentsEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.video_comments_items, parent , false);
            holder = new ViewHolder() ;
            holder.comments_icon = (ImageView)convertView.findViewById(R.id.comments_icon);
            holder.comments_title = (TextView)convertView.findViewById(R.id.comments_title);
            holder.comments_time = (TextView)convertView.findViewById(R.id.comments_time);
            holder.comments_des = (TextView)convertView.findViewById(R.id.comments_des);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        BitmapUtils bitmapUtils = new BitmapUtils(context ) ;
        bitmapUtils.display(holder.comments_icon , Url.FileIP + list.get(position).getUserPicture());
        holder.comments_title.setText(list.get(position).getUserName());
        holder.comments_time.setText(list.get(position).getPubTime());
        holder.comments_des.setText( list.get(position).getContent());
        return convertView;
    }

    static class  ViewHolder{

        ImageView comments_icon ;
        TextView comments_title ;
        TextView comments_time ;
        TextView comments_des ;

    }

    public void addAll(Collection<? extends VideoCommentsEntity> collection) {

        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){

        list.clear();
        notifyDataSetChanged();
    }
}
