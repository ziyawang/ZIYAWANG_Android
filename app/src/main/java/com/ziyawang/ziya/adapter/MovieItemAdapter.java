package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.lidroid.xutils.BitmapUtils;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.VideoActivity;
import com.ziyawang.ziya.entity.FindVideoEntity;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.MyMovieImageView;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/4.
 */
public class MovieItemAdapter extends BaseAdapter {

    private Context context ;
    private List<FindVideoEntity> list ;

    public MovieItemAdapter(){}

    public MovieItemAdapter(Context context , List<FindVideoEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_items, parent , false);
            holder = new ViewHolder() ;
            holder.movie_img = (MyMovieImageView)convertView.findViewById(R.id.movie_img);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.other = (TextView)convertView.findViewById(R.id.other);
            /************************************播放视频********************************************/
            //holder.mSuperVideoPlayer = (SuperVideoPlayer)convertView.findViewById(R.id.video_player_item_1);
            //holder.mPlayBtnView = convertView.findViewById(R.id.play_btn);
            //holder.video_frame = (FrameLayout)convertView.findViewById(R.id.video_frame) ;
            /************************************播放视频*******************************************/
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        BitmapUtils bitmapUtils = new BitmapUtils(context) ;
        bitmapUtils.display(holder.movie_img, Url.FileIP + list.get(position).getVideoLogo());

        holder.title.setText(list.get(position).getVideoTitle());
        holder.other.setText("已播放" + list.get(position).getViewCount() + "次");


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = list.get(position).getVideoID();
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);

            }
        });
        return convertView;
    }

    static class  ViewHolder{

        MyMovieImageView movie_img ;
        TextView title ;
        TextView other ;

    }

    public void addAll(Collection<? extends FindVideoEntity> collection) {

        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){

        list.clear();
        notifyDataSetChanged();
    }
}
