package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.StartActivity;
import com.ziyawang.ziya.entity.FastEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.view.JustifyTextView;

import java.util.List;

/**
 * Created by 牛海丰 on 2018/5/19.
 */

public class FastAdapter extends RecyclerView.Adapter<FastAdapter.FastViewHolder> {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据集合
     */
    private List<FastEntity> data;

    private OnItemClickListener mOnItemClickListener ;

    public interface OnItemClickListener{
        void onClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener = onItemClickListener;
    }

    public FastAdapter(List<FastEntity> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public FastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fast_items, parent, false);
        return new FastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FastViewHolder holder, final int position) {
        //将数据设置到item上
        final FastEntity beauty = data.get(position);
        holder.text_time.setText(beauty.getTime());
        holder.text_des.setText(beauty.getContent());
        holder.text_time01.setText(beauty.getCreated_at());

        holder.text_time.setTypeface(StartActivity.mtypeface);

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class FastViewHolder extends RecyclerView.ViewHolder {
        TextView text_time ;
        JustifyTextView text_des ;
        ImageView image_share ;
        TextView text_time01 ;

        FastViewHolder(View itemView) {
            super(itemView);
            text_time = (TextView) itemView.findViewById(R.id.text_time);
            text_des = (JustifyTextView)itemView.findViewById(R.id.text_des);
            image_share = (ImageView)itemView.findViewById(R.id.image_share);
            text_time01 = (TextView) itemView.findViewById(R.id.text_time01);
        }
    }
}
