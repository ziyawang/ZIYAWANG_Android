package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.ziyawang.ziya.R;
import com.ziyawang.ziya.activity.DetailsFindInfoActivity;
import com.ziyawang.ziya.activity.DetailsFindServiceActivity;
import com.ziyawang.ziya.entity.FindServiceEntity;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.BitmapHelp;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/7/25.
 */
public class FindServiceAdapter extends BaseAdapter {

    public static BitmapUtils bitmapUtils;

    private Context context ;
    private List<FindServiceEntity> list ;

    public FindServiceAdapter(){}

    public FindServiceAdapter(Context context , List<FindServiceEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.find_service_items, parent , false);
            holder = new ViewHolder() ;
            holder.service_title = (TextView)convertView.findViewById(R.id.service_title);
            holder.service_part = (TextView)convertView.findViewById(R.id.service_part);
            holder.service_type = (TextView)convertView.findViewById(R.id.service_type);
            holder.ben = (TextView)convertView.findViewById(R.id.ben);
            holder.image_01 = (ImageView)convertView.findViewById(R.id.image_01);
            holder.image_02 = (ImageView)convertView.findViewById(R.id.image_02);
            holder.image_03 = (ImageView)convertView.findViewById(R.id.image_03);
            holder.image_04 = (ImageView)convertView.findViewById(R.id.image_04);
            holder.image_05 = (ImageView)convertView.findViewById(R.id.image_05);
            holder.image_06 = (ImageView)convertView.findViewById(R.id.image_06);
            holder.image_07 = (ImageView)convertView.findViewById(R.id.image_07);
            holder.image_08 = (ImageView)convertView.findViewById(R.id.image_08);
            holder.image_09 = (ImageView)convertView.findViewById(R.id.image_09);
            holder.image_10 = (ImageView)convertView.findViewById(R.id.image_10);
            holder.ben_img = (ImageView)convertView.findViewById(R.id.ben_img);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();

        }

        // 获取bitmapUtils单例
        bitmapUtils = BitmapHelp.getBitmapUtils(context);
        /**
         * 设置默认的图片展现、加载失败的图片展现
         */
        bitmapUtils.configDefaultLoadingImage(R.mipmap.fast_error);
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.error_imgs_big);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        /**
         * display参数 (ImageView container, String uri,
         * BitmapLoadCallBack<ImageView> callBack)
         */
        bitmapUtils.display(holder.ben_img, Url.FileIP + list.get(position).getConfirmationP1());

        holder.image_06.setVisibility(View.GONE);
        holder.image_07.setVisibility(View.GONE);
        holder.image_08.setVisibility(View.GONE);
        holder.image_09.setVisibility(View.GONE);
        holder.image_10.setVisibility(View.GONE);
        holder.ben.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(list.get(position).getRight()) ){
            String[] split = list.get(position).getRight().split(",");
            for (int i = 0; i < split.length; i++) {
                if ("1".equals(split[i].toString())){
                    holder.image_06.setVisibility(View.VISIBLE);
                    holder.ben.setVisibility(View.GONE);
                }
                if ("18".equals(split[i].toString())){
                    holder.image_07.setVisibility(View.VISIBLE);
                    holder.ben.setVisibility(View.GONE);
                }
                if ("12".equals(split[i].toString()) ){
                    holder.image_08.setVisibility(View.VISIBLE);
                    holder.ben.setVisibility(View.GONE);
                }
                if ("6".equals(split[i].toString()) ){
                    holder.image_09.setVisibility(View.VISIBLE);
                    holder.ben.setVisibility(View.GONE);
                }
                if ("19".equals(split[i].toString())){
                    holder.image_10.setVisibility(View.VISIBLE);
                    holder.ben.setVisibility(View.GONE);
                }
            }
        }

        holder.image_01.setImageResource(R.mipmap.v203_0401);
        holder.image_02.setImageResource(R.mipmap.v203_0402);
        holder.image_03.setImageResource(R.mipmap.v203_0403);
        holder.image_04.setImageResource(R.mipmap.v203_0404);
        holder.image_05.setImageResource(R.mipmap.v203_0405);

        if (!TextUtils.isEmpty(list.get(position).getLevel()) ){
            String[] split = list.get(position).getLevel().split(",");
            for (int i = 0; i < split.length; i++) {
                if ("1".equals(split[i].toString())){
                    holder.image_01.setImageResource(R.mipmap.v203_0301);
                }
                if ("2".equals(split[i].toString())){
                    holder.image_02.setImageResource(R.mipmap.v203_0302);
                }
                if ("3".equals(split[i].toString()) ){
                    holder.image_03.setImageResource(R.mipmap.v203_0303);
                }
                if ("4".equals(split[i].toString()) ){
                    holder.image_04.setImageResource(R.mipmap.v203_0304);
                }
                if ("5".equals(split[i].toString())){
                    holder.image_05.setImageResource(R.mipmap.v203_0305);
                }
            }
        }

        holder.service_title.setText(list.get(position).getServiceName() + "(" + list.get(position).getConnectPerson() + ")");
        holder.service_part.setText(list.get(position).getServiceArea());
        holder.service_type.setText(list.get(position).getServiceType());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = list.get(position).getServiceID() ;
                Intent intent = new Intent(context , DetailsFindServiceActivity.class ) ;
                intent.putExtra("id" ,id) ;
                context.startActivity(intent);

            }
        });
        return convertView;
    }

    static class  ViewHolder{

        TextView service_title ;
        TextView service_part ;
        TextView service_type ;
        TextView ben ;
        ImageView image_01 , image_02 , image_03 , image_04 , image_05 , image_06 , image_07 , image_08 , image_09 , image_10  , ben_img  ;

    }

    public void addAll(Collection<? extends FindServiceEntity> collection) {

        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){

        list.clear();
        notifyDataSetChanged();
    }
}
