package com.ziyawang.ziya.tools;

import com.ziyawang.ziya.entity.FindNewsEntity;
import com.ziyawang.ziya.entity.FindServiceEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/10/31.
 */
public class Json_FindNews {
    public static List<FindNewsEntity> getParse(String jsonStr) throws JSONException {

        List<FindNewsEntity> data = new ArrayList<FindNewsEntity>();

        JSONObject jsonObject = new JSONObject(jsonStr) ;
        JSONArray array = jsonObject.getJSONArray("data") ;


        FindNewsEntity findNewsEntity = null;
        for(int i = 0;i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);

            String newsID = object.getString("NewsID");
            String NewsTitle = object.getString("NewsTitle") ;
            String NewsContent = object.getString("NewsContent") ;
            String NewsLogo = object.getString("NewsLogo") ;
            String NewsThumb = object.getString("NewsThumb") ;
            String NewsLabel = object.getString("NewsLabel") ;
            String PublishTime = object.getString("PublishTime") ;
            String NewsAuthor = object.getString("NewsAuthor") ;
            String ViewCount = object.getString("ViewCount") ;
            String CollectionCount = object.getString("CollectionCount") ;
            String Brief = object.getString("Brief") ;

            findNewsEntity = new FindNewsEntity(newsID,NewsTitle,NewsContent,NewsLogo,NewsThumb,NewsLabel,PublishTime,NewsAuthor,ViewCount,CollectionCount,Brief ) ;
            data.add(findNewsEntity) ;



        }
        return data;
    }
}
