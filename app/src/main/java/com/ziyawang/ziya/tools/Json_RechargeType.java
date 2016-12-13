package com.ziyawang.ziya.tools;

import com.ziyawang.ziya.entity.RechargeTypeEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/9/25.
 */
public class Json_RechargeType {

    public static List<RechargeTypeEntity> getParse(String jsonStr) throws JSONException {
        List<RechargeTypeEntity> data = new ArrayList<RechargeTypeEntity>();

        JSONArray array = new JSONArray(jsonStr) ;

        RechargeTypeEntity rechargeTypeEntity = null;
        for(int i = 0;i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);

            String realMoney = object.getString("RealMoney") ;
            String ybCount = object.getString("YBCount");
            String selected = object.getString("selected");
            String add = object.getString("add");

            rechargeTypeEntity = new RechargeTypeEntity( realMoney , ybCount , selected , add  );
            data.add(rechargeTypeEntity) ;
        }
        return data;
    }

}
