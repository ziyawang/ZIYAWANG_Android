package com.ziyawang.ziya.tools;

import com.ziyawang.ziya.entity.FindNewsEntity;
import com.ziyawang.ziya.entity.QuestionsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/11/4.
 */
public class Json_Questions {
    public static List<QuestionsEntity> getParse(String jsonStr) throws JSONException {

        List<QuestionsEntity> data = new ArrayList<QuestionsEntity>();
        JSONArray array = new JSONArray(jsonStr) ;
        QuestionsEntity QuestionsEntity = null;
        for(int i = 0;i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);

            String ID = object.getString("ID");
            String Question = object.getString("Question") ;
            //String NewsContent = object.getString("Choices") ;
            JSONArray choices = object.getJSONArray("Choices");
            List list = new ArrayList() ;
            for (int j = 0; j < choices.length() ; j++) {
                list.add(choices.getString(j)) ;
            }
            String Type = object.getString("Type") ;
            String Sort = object.getString("Sort") ;
            String Paper = object.getString("Paper") ;
            JSONArray Choicesno = object.getJSONArray("Choicesno");
            List list02 = new ArrayList() ;
            for (int j = 0; j < Choicesno.length() ; j++) {
                list02.add(Choicesno.getString(j)) ;
            }
            String input = object.getString("Input") ;

            QuestionsEntity = new QuestionsEntity(ID , Question , list , Type , Sort , Paper ,list02 , input) ;
            data.add(QuestionsEntity) ;



        }
        return data;
    }
}
