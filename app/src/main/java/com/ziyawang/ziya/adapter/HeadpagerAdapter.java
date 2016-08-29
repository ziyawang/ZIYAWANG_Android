package com.ziyawang.ziya.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/11.
 */
public class HeadpagerAdapter extends PagerAdapter {
    private List<ImageView> head_image;
    //private List<PlayFragment_AD> list ;
    private Context context;


    public HeadpagerAdapter(  List<ImageView> head_image, Context context) {
        //this.list = list ;
        this.head_image = head_image;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {


        container.addView(head_image.get(position % head_image.size()));
        //container.addView(head_image.get(position % 3));


//        head_image.get(position % head_image.size()).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(context, "" + position % head_image.size(), Toast.LENGTH_SHORT).show();
//
//                final String type = list.get(position % list.size()).getType();
//                switch (type){
//                    case "0" :
//                        final String linkurl = list.get(position % list.size()).getLinkurl();
//
//                        Intent intent = new Intent(context , CompanyWWWActivity.class ) ;
//                        intent.putExtra( "web" , linkurl )  ;
//                        context.startActivity(intent);
//                        break;
//                    case "1" :
//                        final String company_id = list.get(position % list.size()).getAdvertiseId();
//                        Intent intent01 = new Intent(context , DetailsCompanyActivity.class ) ;
//                        intent01.putExtra( "id" , company_id )  ;
//                        context.startActivity(intent01);
//                        break;
//                    case "2" :
//                        final String product_id = list.get(position % list.size()).getAdvertiseId();
//                        Intent intent02 = new Intent(context , DetailsProductsActivity.class ) ;
//                        intent02.putExtra( "id" , product_id )  ;
//                        context.startActivity(intent02);
//                        break;
//                    case "3" :
//                        final String fefer_id = list.get(position % list.size()).getAdvertiseId();
//                        Intent intent03 = new Intent(context , DetailsReferActivity.class ) ;
//                        intent03.putExtra( "id" , fefer_id )  ;
//                        context.startActivity(intent03);
//                        break;
//                }
//
//
//            }
//        });

        return head_image.get(position % head_image.size());
        //return head_image.get(position % 3);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(head_image.get(position % head_image.size()));
        //container.removeView(head_image.get(position % 3));

    }
}


