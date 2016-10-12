package com.ziyawang.ziya.tools;

import com.ziyawang.ziya.entity.GoldDetailsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/9/24.
 */


public class Json_GoldDetails {
    public static List<GoldDetailsEntity> getParse(String jsonStr) throws JSONException {
        List<GoldDetailsEntity> data = new ArrayList<GoldDetailsEntity>();

        JSONObject jsonObject = new JSONObject(jsonStr) ;
        JSONArray array = jsonObject.getJSONArray("data") ;

        GoldDetailsEntity goldDetailsEntity = null;
        for(int i = 0;i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);

            String created_at1 = object.getString("created_at") ;
            String money = object.getString("Money");
            //1是充值，2是消费
            String type = object.getString("Type");
            String orderNumber = object.getString("OrderNumber");
            String operates = object.getString("Operates");

            goldDetailsEntity = new GoldDetailsEntity(created_at1,money , type ,orderNumber ,operates);
            data.add(goldDetailsEntity) ;
        }
        return data;
    }
}