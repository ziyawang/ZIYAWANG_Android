package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsSystemInfoActivity;
import com.ziyawang.ziya.activity.V2DetailsFindInfoActivity;
import com.ziyawang.ziya.entity.SystemEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/20.
 */
public class SystemAdapter extends BaseAdapter {

    private Context context ;
    private List<SystemEntity> list ;

    public SystemAdapter(){}

    public SystemAdapter(Context context , List<SystemEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.system_info_items, parent , false);
            holder = new ViewHolder() ;
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.time = (TextView)convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //未读消息
        if ("0".equals(list.get(position).getStatus())){
            convertView.setBackgroundColor(Color.rgb(255,253,242));
            TextPaint tp = holder.time.getPaint();
            tp.setFakeBoldText(true);
            TextPaint tp1 = holder.title.getPaint() ;
            tp1.setFakeBoldText(true);
        }else {
            convertView.setBackgroundColor(Color.rgb(255, 255, 255));
        }

        if ("0".equals(list.get(position).getType())){
            holder.title.setText(list.get(position).getTitle());
        }else {
            holder.title.setText(list.get(position).getText());
        }

        holder.time.setText(list.get(position).getTime());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("0".equals(list.get(position).getType()) ){
                    Intent intent = new Intent(context, DetailsSystemInfoActivity.class);
                    intent.putExtra("status", list.get(position).getStatus());
                    intent.putExtra("text", list.get(position).getText());
                    intent.putExtra("textId", list.get(position).getTextID());
                    intent.putExtra("time", list.get(position).getTime());
                    intent.putExtra("title", list.get(position).getTitle());
                    context.startActivity(intent);
                }else if ("1".equals(list.get(position).getType()) ){
                    Intent intent = new Intent(context , V2DetailsFindInfoActivity.class );
                    intent.putExtra("id", list.get(position).getProjectId());
                    context.startActivity(intent);
                }else {
                    ToastUtils.shortToast(context , "数据存在异常");
                }

            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                SharedPreferences loginCode = context.getSharedPreferences("loginCode", context.MODE_PRIVATE);
                String login = loginCode.getString("loginCode", null);

                String urls = String.format(Url.DelMessage, login ) ;

                HttpUtils httpUtils = new HttpUtils() ;
                RequestParams params = new RequestParams() ;
                params.addBodyParameter("TextID" , list.get(position).getTextID());
                httpUtils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("benben" , responseInfo.result ) ;
                        try {
                            JSONObject object = new JSONObject(responseInfo.result) ;
                            String status_codes = object.getString("status_code");
                            switch (status_codes){
                                case "200" :
                                    ToastUtils.shortToast(context , "删除系统消息成功");
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    break;
                                default:
                                    ToastUtils.shortToast(context , "删除系统消息失败");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.shortToast(context, "网络连接异常");
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                }) ;

                return true;
            }
        });

        return convertView;
    }

    static class  ViewHolder{
        TextView title ;
        TextView time ;

    }

    public void addAll(Collection<? extends SystemEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }
}