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
import com.ziyawang.ziya.activity.DetailsAskActivity;
import com.ziyawang.ziya.entity.MyAskEntity;
import com.ziyawang.ziya.tools.FormatCurrentData;
import com.ziyawang.ziya.tools.ToastUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2018/5/22.
 */

public class MyAskAdapter extends BaseAdapter {

    private Context context ;
    private List<MyAskEntity> list ;

    public MyAskAdapter(){}

    public MyAskAdapter(Context context , List<MyAskEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_my_ask_items, parent , false);
            holder = new ViewHolder() ;
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.text_question = (TextView)convertView.findViewById(R.id.text_question);
            holder.text_ok = (TextView)convertView.findViewById(R.id.text_ok);
            holder.text_no = (TextView)convertView.findViewById(R.id.text_no);
            holder.text_delete = (TextView)convertView.findViewById(R.id.text_delete);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text_question.setText(list.get(position).getQuestion());

        switch (list.get(position).getState()){
            case "0" :
                //待解决
                holder.text_no.setVisibility(View.VISIBLE);
                holder.text_ok.setVisibility(View.GONE);
                holder.text_delete.setVisibility(View.GONE);
                holder.image.setImageResource(R.mipmap.my_ask_ing);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.shortToast(context , "暂时没有相关回答");
                    }
                });
                break;
            case "1" :
                //已解决
                holder.text_no.setVisibility(View.GONE);
                holder.text_ok.setVisibility(View.VISIBLE);
                holder.text_delete.setVisibility(View.GONE);
                holder.image.setImageResource(R.mipmap.my_ask_ok);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context , DetailsAskActivity.class ) ;
                        intent.putExtra("id" , list.get(position).getId()) ;
                        context.startActivity(intent);
                    }
                });
                break;
            case "2" :
                //删除
                holder.text_no.setVisibility(View.GONE);
                holder.text_ok.setVisibility(View.GONE);
                holder.text_delete.setVisibility(View.VISIBLE);
                holder.image.setImageResource(R.mipmap.my_ask_delete);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.shortToast(context , "问题已被删除");
                    }
                });
                break;
            default:
                holder.text_no.setVisibility(View.GONE);
                holder.text_ok.setVisibility(View.GONE);
                holder.text_delete.setVisibility(View.GONE);
                holder.image.setImageResource(R.mipmap.my_ask_ing);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.shortToast(context , "数据异常");
                    }
                });
                break;
        }

        return convertView;
    }

    static class  ViewHolder{
        ImageView image ;
        TextView text_question ;
        TextView text_ok ;
        TextView text_no ;
        TextView text_delete ;

    }

    public void addAll(Collection<? extends MyAskEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }
}
