package com.ziyawang.ziya.tools;

import com.ziyawang.ziya.entity.FindInfoEntity;
import com.ziyawang.ziya.entity.SystemEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/8/20.
 */
public class Json_System {

    public static List<SystemEntity> getParse(String jsonStr) throws JSONException {

        List<SystemEntity> data = new ArrayList<SystemEntity>();

        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray array = jsonObject.getJSONArray("data");

        SystemEntity systemEntity = null;
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);


            String textID = object.getString("TextID");
            String title = object.getString("Title");
            String text = object.getString("Text");
            String time = object.getString("Time");
            String status = object.getString("Status");
            String type = object.getString("type");
            String ProjectId = object.getString("ProjectId");

            systemEntity = new SystemEntity(textID, title, text, time, status , type , ProjectId );
            data.add(systemEntity);
        }
        return data;
    }
}
