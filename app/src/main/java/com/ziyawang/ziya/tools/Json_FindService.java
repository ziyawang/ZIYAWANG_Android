package com.ziyawang.ziya.tools;

import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.entity.FindServiceEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/7/25.
 */
public class Json_FindService {

    public static List<FindServiceEntity> getParse(String jsonStr) throws JSONException {

        List<FindServiceEntity> data = new ArrayList<FindServiceEntity>();

        JSONObject jsonObject = new JSONObject(jsonStr) ;
        JSONArray array = jsonObject.getJSONArray("data") ;


        FindServiceEntity findServiceEntity = null;
        for(int i = 0;i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);

            String ServiceID = object.getString("ServiceID") ;
            String ServiceName = object.getString("ServiceName") ;
            String ServiceLocation = object.getString("ServiceArea") ;
            String ServiceType = object.getString("ServiceType") ;
            String ServiceLevel = object.getString("ServiceLevel") ;
            String ConnectPerson = object.getString("ConnectPerson") ;

            findServiceEntity = new FindServiceEntity(ServiceID ,ServiceName , ServiceLocation,ServiceType , ServiceLevel , ConnectPerson ) ;
            data.add(findServiceEntity) ;



        }
        return data;
    }

}
