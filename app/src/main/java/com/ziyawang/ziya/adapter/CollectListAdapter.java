package com.ziyawang.ziya.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsFindInfoActivity;
import com.ziyawang.ziya.activity.DetailsFindServiceActivity;
import com.ziyawang.ziya.activity.MovieListActivity;
import com.ziyawang.ziya.activity.VideoActivity;
import com.ziyawang.ziya.entity.MyCollectListEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/9.
 */
public class CollectListAdapter extends BaseAdapter {

    private Context context;
    private List<MyCollectListEntity> list;

    public CollectListAdapter() {
    }

    public CollectListAdapter(Context context, List<MyCollectListEntity> list) {
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

        ViewHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.collect_info, parent, false);
            holder = new ViewHolder();
            holder.aaa = (TextView) convertView.findViewById(R.id.aaa);
            holder.bbb = (TextView) convertView.findViewById(R.id.bbb);
            holder.collect_info_title = (TextView) convertView.findViewById(R.id.collect_info_title);
            holder.collect_part = (TextView) convertView.findViewById(R.id.collect_part);
            holder.collect_des = (TextView) convertView.findViewById(R.id.collect_des);
            holder.collect_movie_img = (ImageView) convertView.findViewById(R.id.collect_movie_img);
            holder.collect_movie_title = (TextView) convertView.findViewById(R.id.collect_movie_title);
            holder.collect_movie_des = (TextView) convertView.findViewById(R.id.collect_movie_des);
            holder.relative_01 = (RelativeLayout) convertView.findViewById(R.id.relative_01);
            holder.relative_02 = (RelativeLayout) convertView.findViewById(R.id.relative_02);
            holder.collect_time = (TextView) convertView.findViewById(R.id.collect_time);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.collect_time.setText(list.get(position).getCollectTime());
        String typeID = list.get(position).getTypeID();
        switch (typeID){

            case "1" :
                holder.relative_01.setVisibility(View.VISIBLE);
                holder.relative_02.setVisibility(View.GONE);
                holder.aaa.setText("地区：");
                holder.bbb.setText("文字描述：");
                holder.collect_info_title.setText(list.get(position).getTypeName());
                holder.collect_part.setText(list.get(position).getProArea());
                holder.collect_des.setText(list.get(position).getWordDes());
                break;
            case "2" :
                holder.relative_01.setVisibility(View.GONE);
                holder.relative_02.setVisibility(View.VISIBLE);
                BitmapUtils bitmapUtils = new BitmapUtils(context) ;
                bitmapUtils.display(holder.collect_movie_img , Url.FileIP +list.get(position).getVideoLogo());
                holder.collect_movie_title.setText(list.get(position).getVideoTitle());
                holder.collect_movie_des.setText(list.get(position).getVideoDes());
                break;
            case "4" :
                holder.relative_01.setVisibility(View.VISIBLE);
                holder.relative_02.setVisibility(View.GONE);
                holder.aaa.setText("服务地区：");
                holder.bbb.setText("服务类型：");
                holder.collect_info_title.setText(list.get(position).getServiceName());
                holder.collect_part.setText(list.get(position).getServiceArea());
                holder.collect_des.setText(list.get(position).getServiceType());
                break;
            default:
                break;

        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeID = list.get(position).getTypeID();
                switch (typeID){
                    case "1" :
                        Intent intent = new Intent(context , DetailsFindInfoActivity.class  ) ;
                        intent.putExtra("id" , list.get(position).getItemID() ) ;
                        intent.putExtra("title" , list.get(position).getTypeName() ) ;
                        context.startActivity(intent);

                        break;
                    case "2" :
                        Intent intent02 = new Intent(context , VideoActivity.class  ) ;
                        intent02.putExtra("id" , list.get(position).getItemID() ) ;
                        context.startActivity(intent02);
                        break;
                    case "4" :
                        Intent intent04 = new Intent(context , DetailsFindServiceActivity.class  ) ;
                        intent04.putExtra("id" , list.get(position).getItemID() ) ;
                        context.startActivity(intent04);
                        break;
                    default:
                        break;
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final SharedPreferences loginCode = context.getSharedPreferences("loginCode", context.MODE_PRIVATE);
                final String login = loginCode.getString("loginCode", null);


                final CustomDialog.Builder builder01 = new CustomDialog.Builder(context);
                builder01.setTitle("亲爱的用户");
                builder01.setMessage("您确定要取消收藏?");
                builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        //收藏功能接口的调用
                        HttpUtils utils = new HttpUtils();
                        RequestParams params1 = new RequestParams();
                        switch (list.get(position).getTypeID()) {
                            case "1":
                                params1.addBodyParameter("type", "1");
                                break;
                            case "2":
                                params1.addBodyParameter("type", "2");
                                break;
                            case "4":
                                params1.addBodyParameter("type", "4");
                                break;
                            default:
                                break;
                        }
                        params1.addBodyParameter("itemID", list.get(position).getItemID());
                        String a = String.format(Url.Collect, login);
                        //params1.addQueryStringParameter();
                        utils.send(HttpRequest.HttpMethod.POST, a, params1, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Log.e("benbne", responseInfo.result);
                                try {
                                    JSONObject object = new JSONObject(responseInfo.result);
                                    String msg = object.getString("msg");
                                    switch (msg) {
                                        case "取消收藏成功！":
                                            ToastUtils.shortToast(context , "取消收藏成功");
                                            list.remove(position);
                                            dialog.dismiss();
                                            notifyDataSetInvalidated();
                                            break;
                                        case "收藏成功！":
                                            break;
                                        default:
                                            dialog.dismiss();
                                            break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                error.printStackTrace();
                                ToastUtils.shortToast( context, "取消收藏失败");
                            }
                        });


                    }
                });
                builder01.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder01.create().show();
                return true;
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView aaa;
        TextView bbb;
        TextView collect_info_title;
        TextView collect_part;
        TextView collect_des;
        ImageView collect_movie_img;
        TextView collect_movie_title;
        TextView collect_movie_des;
        RelativeLayout relative_01;
        RelativeLayout relative_02;
        TextView collect_time ;

    }


    public void addAll(Collection<? extends MyCollectListEntity> collection) {

        list.addAll(collection);
        notifyDataSetChanged();

    }

    public void clear() {

        list.clear();
        notifyDataSetChanged();
    }
}
