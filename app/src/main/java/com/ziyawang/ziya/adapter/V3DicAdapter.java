package com.ziyawang.ziya.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziyawang.ziya.R;
import com.ziyawang.ziya.entity.DictionaryEntity;
import com.ziyawang.ziya.view.JustifyTextView;

import java.util.Collection;
import java.util.List;

/**
 * Created by 牛海丰 on 2018/5/19.
 */

public class V3DicAdapter extends BaseAdapter {

    private Activity context ;
    private List<DictionaryEntity> list ;

    public V3DicAdapter(){}

    public V3DicAdapter(Activity context , List<DictionaryEntity> list ){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.home_dic_items, parent , false);
            holder = new ViewHolder() ;
            holder.text_word = (TextView)convertView.findViewById(R.id.text_word);
            holder.text_des = (TextView)convertView.findViewById(R.id.text_des);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text_word.setText(list.get(position).getName());
        holder.text_des.setText(list.get(position).getContent());

        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(finalConvertView, position ) ;
            }
        });

        return convertView;
    }

    private void showPop(final View convertView, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_dic, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView text_word = (TextView) view.findViewById(R.id.text_word);
        JustifyTextView text_des = (JustifyTextView) view.findViewById(R.id.text_des);
        ImageView image_close = (ImageView) view.findViewById(R.id.image_close);
        text_word.setText(list.get(position).getName());
        text_des.setText(list.get(position).getContent());
        //close
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        window.setFocusable(true);
        //点击空白的地方关闭PopupWindow
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(text_word, Gravity.CENTER, 0, 0);
        // 设置popWindow的显示和消失动画
        backgroundAlpha(0.2f);
        convertView.setEnabled(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                convertView.setEnabled(true);
            }
        });
    }

    static class  ViewHolder{
        TextView text_word ;
        TextView text_des ;
    }

    public void addAll(Collection<? extends DictionaryEntity> collection) {
        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().setAttributes(lp);
    }
}
