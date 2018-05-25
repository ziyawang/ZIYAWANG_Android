package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsAskActivity;
import com.ziyawang.ziya.entity.AskEntity;
import com.ziyawang.ziya.tools.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2018/5/19.
 */

public class V3AskAdapter extends BaseAdapter{

    private Context context ;
    private List<AskEntity> list ;

    public V3AskAdapter(){}

    public V3AskAdapter(Context context , List<AskEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_ask_items, parent , false);
            holder = new ViewHolder() ;
            holder.text_question = (TextView)convertView.findViewById(R.id.text_question);
            holder.text_answer = (TextView)convertView.findViewById(R.id.text_answer);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String quw = StringUtils.replaceBlank(list.get(position).getQuestion());
        String aws = StringUtils.replaceBlank(list.get(position).getAnswer());
        holder.text_question.setText(quw);
        holder.text_answer.setText(aws);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailsAskActivity.class ) ;
                intent.putExtra("id" , list.get(position).getId() ) ;
                context.startActivity(intent);
            }
        });



        return convertView;
    }

    static class  ViewHolder{
        TextView text_question ;
        TextView text_answer ;
    }

    public void addAll(Collection<? extends AskEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }
}
