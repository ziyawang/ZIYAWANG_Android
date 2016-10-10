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
import com.ziyawang.ziya.activity.DetailsFindServiceActivity;
import com.ziyawang.ziya.activity.LoginActivity;
import com.ziyawang.ziya.entity.MyPersonEntity;
import com.ziyawang.ziya.tools.ToastUtils;
import com.ziyawang.ziya.tools.Url;
import com.ziyawang.ziya.view.CustomDialog;
import com.ziyawang.ziya.view.MyIconImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by 牛海丰 on 2016/8/7.
 */
public class MyPersonAdapter extends BaseAdapter {

    private Context context ;
    private List<MyPersonEntity> list ;
    private String id ;

    private String login ;
    private String status ;

    public MyPersonAdapter(){}

    public MyPersonAdapter(Context context , List<MyPersonEntity> list , String id , String status  ){
        super();
        this.context = context;
        this.list = list;
        this.id = id ;
        this.status = status ;
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
        final ViewHolder holder ;
        if(convertView == null ){
            convertView = LayoutInflater.from(context).inflate(R.layout.my_person_items, parent , false);
            holder = new ViewHolder() ;
            holder.my_person_icon = (ImageView)convertView.findViewById(R.id.my_person_icon ) ;
            holder.my_person_no = (TextView)convertView.findViewById(R.id.my_person_no ) ;
            holder.my_person_num = (TextView)convertView.findViewById(R.id.my_person_num ) ;
            holder.text_one = (TextView)convertView.findViewById(R.id.text_one ) ;
            //holder.text_two = (TextView)convertView.findViewById(R.id.text_two ) ;
            holder.text_call = (TextView)convertView.findViewById(R.id.text_call ) ;
            holder.text_three = (TextView)convertView.findViewById(R.id.text_three ) ;
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SharedPreferences loginCode = context.getSharedPreferences("loginCode", context.MODE_PRIVATE);
        login = loginCode.getString("loginCode", null);

        BitmapUtils bitmapUtils = new BitmapUtils(context) ;
        bitmapUtils.display(holder.my_person_icon, Url.FileIP + list.get(position).getUserPicture());

        Log.e("benben", list.get(position).getUserPicture()) ;
        holder.my_person_no.setText(list.get(position).getServiceNumber());
        holder.my_person_num.setText(list.get(position).getConnectPhone());

        String collectFlag = list.get(position).getCollectFlag();

//        switch (status){
//            case "0" :
//                //抢单中
//                holder.text_two.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        final CustomDialog.Builder builder01 = new CustomDialog.Builder( context);
//                        builder01.setTitle("亲爱的用户");
//                        builder01.setMessage("您是否与服务方联系过？并达成合作意向？");
//                        builder01.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(final DialogInterface dialog, int which) {
//
//                                String urls = String.format(Url.MyGoTeam, login);
//                                HttpUtils utils = new HttpUtils();
//                                RequestParams params = new RequestParams();
//                                params.addBodyParameter("ServiceID", list.get(position).getServiceID());
//                                params.addBodyParameter("ProjectID", id);
//                                utils.send(HttpRequest.HttpMethod.POST, urls, params, new RequestCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                                        if (dialog!=null){
//                                            dialog.dismiss();
//                                        }
//                                        Log.e("benben", responseInfo.result);
//                                        ToastUtils.shortToast(context, "合作成功");
//                                    }
//
//                                    @Override
//                                    public void onFailure(HttpException error, String msg) {
//                                        error.printStackTrace();
//                                        ToastUtils.shortToast(context, "合作失败");
//                                        if (dialog!=null){
//                                            dialog.dismiss();
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                        builder01.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        builder01.create().show();
//                    }
//                });
//                break;
//            case "1" :
//                holder.text_two.setVisibility(View.GONE);
//                break;
//            case "2" :
//                holder.text_two.setVisibility(View.VISIBLE);
//                holder.text_two.setText("取消中");
//                holder.text_two.setClickable(false);
//                break;
//            default:
//                break;
//        }

//        if (list.get(position).getCooateFlag() == "1"){
//            holder.text_two.setVisibility(View.VISIBLE);
//            holder.text_two.setText("已合作");
//        }
//        switch (list.get(position).getCooperateFlag()){
//            case "1" :
//                holder.text_two.setVisibility(View.VISIBLE);
//                holder.text_two.setText("已合作");
//                holder.text_two.setClickable(false);
//                break;
//            default:
//                break;
//        }
        switch (collectFlag) {
            case "0":
                Drawable drawable01 = context.getResources().getDrawable(R.mipmap.collect_un);
                /// 这一步必须要做,否则不会显示.
                drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
                holder.text_one.setCompoundDrawables(null, drawable01, null, null);
                break;
            case "1":
                Drawable drawable02 = context.getResources().getDrawable(R.mipmap.collect);
                /// 这一步必须要做,否则不会显示.
                drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
                holder.text_one.setCompoundDrawables(null , drawable02 , null, null);

                break;
            default:
                break;
        }


        holder.text_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCall(list.get(position).getConnectPhone()) ;
            }
        });

        holder.text_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //收藏功能接口的调用
                HttpUtils utils = new HttpUtils();
                RequestParams params1 = new RequestParams();
                //params1.addQueryStringParameter("token",login );
                params1.addBodyParameter("itemID", list.get(position).getServiceID() );
                params1.addBodyParameter("type", "4");
                String a = String.format(Url.Collect, login);
                Log.e("benben_id", id);
                Log.e("benben_login", login);
                Log.e("benben_a", a);
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
                                    Toast.makeText(context, "取消收藏", Toast.LENGTH_SHORT).show();
                                    Drawable drawable01 = context.getResources().getDrawable(R.mipmap.collect_un);
                                    /// 这一步必须要做,否则不会显示.
                                    drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
                                    holder.text_one.setCompoundDrawables(null ,drawable01 , null, null);
                                    break;
                                case "收藏成功！":
                                    Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                                    Drawable drawable02 = context.getResources().getDrawable(R.mipmap.collect);
                                    /// 这一步必须要做,否则不会显示.
                                    drawable02.setBounds(0, 0, drawable02.getMinimumWidth(), drawable02.getMinimumHeight());
                                    holder.text_one.setCompoundDrawables(null ,drawable02 , null, null);
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        error.printStackTrace();
                        ToastUtils.shortToast(context , "网络连接异常");
                    }
                });

            }
        });


        holder.text_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动会话界面
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startPrivateChat(context , list.get(position).getUserID() , "聊天详情");
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailsFindServiceActivity.class ) ;
                intent.putExtra("id" ,list.get(position).getServiceID()) ;
                context.startActivity(intent);

            }
        });

        return convertView;
    }

    private void goCall(String connectPhone) {
        String str = "tel:" + connectPhone ;
        //直接拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.shortToast( context, "请在管理中心，给予直接拨打电话权限。");
            return;
        }
        context.startActivity(intent);
    }

    static class  ViewHolder{

        ImageView my_person_icon ;
        TextView my_person_no ;
        TextView my_person_num ;
        TextView text_one ;
        //TextView text_two ;
        TextView text_call ;
        TextView text_three ;

    }

    public void addAll(Collection<? extends MyPersonEntity> collection) {

        list.addAll(collection);
        notifyDataSetChanged();

    }
    public void clear(){

        list.clear();
        notifyDataSetChanged();
    }
}