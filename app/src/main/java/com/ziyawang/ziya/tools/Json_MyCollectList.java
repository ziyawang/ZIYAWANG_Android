package com.ziyawang.ziya.tools;

import com.ziyawang.ziya.entity.MyCollectListEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/9.
 */
public class Json_MyCollectList {

    public static List<MyCollectListEntity> getParse(String jsonStr) throws JSONException {

        List<MyCollectListEntity> data = new ArrayList<MyCollectListEntity>();

        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray array = jsonObject.getJSONArray("data");


        MyCollectListEntity myCollectListEntity = null;
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            final String itemID = object.getString("ItemID");
            final String collectTime = object.getString("CollectTime");
            final String typeID = object.getString("TypeID");
            switch (typeID){
                case "1" :
                    //收藏信息模块
                    String typeName = object.getString("TypeName");
                    String proArea = object.getString("ProArea");
                    String wordDes = object.getString("WordDes");
                    String pictureDes1 = object.getString("PictureDes1");
                    String projectNumber = object.getString("ProjectNumber");

                    myCollectListEntity = new MyCollectListEntity(typeID , collectTime , itemID , typeName , proArea , wordDes , pictureDes1 ,
                            projectNumber , "" , "" , "" , "" , "" , "" , "" , "" , "" , "" , "") ;
                    data.add(myCollectListEntity) ;


                    break;
                case "2":
                    //收藏视频模块
                    String videoTitle = object.getString("VideoTitle");
                    String viewCount = object.getString("ViewCount");
                    String videoDes = object.getString("VideoDes");
                    String videoLogo = object.getString("VideoLogo");

                    myCollectListEntity = new MyCollectListEntity(typeID , collectTime , itemID , "" , "" , "" , "" , "" ,
                            videoTitle , viewCount , videoDes , videoLogo ,
                            "" , "" , "" , "" , "" , "" , "") ;
                    data.add(myCollectListEntity) ;

                    break;
                case "4":
                    //收藏服务模块
                    String serviceName = object.getString("ServiceName");
                    String serviceType = object.getString("ServiceType");
                    String serviceArea = object.getString("ServiceArea");
                    String userPicture = object.getString("UserPicture");

                    myCollectListEntity = new MyCollectListEntity(typeID , collectTime , itemID , "" , "" , "" , "" ,
                            "" , "" , "" , "" , "" , serviceName , serviceType , serviceArea , userPicture  , "" , "" , "") ;
                    data.add(myCollectListEntity) ;
                    break;
                case "3":
                    //收藏新闻模块
                    String newsTitle = object.getString("NewsTitle");
                    String brief = object.getString("Brief");
                    String newsLogo = object.getString("NewsLogo");

                    myCollectListEntity = new MyCollectListEntity(typeID , collectTime , itemID , "" , "" , "" , "" ,
                            "" , "" , "" , "" , "" , "" , "" , "" , ""  , newsTitle , brief , newsLogo ) ;
                    data.add(myCollectListEntity) ;
                    break;
                default:
                    break;
            }

        }
        return data;
    }
}
