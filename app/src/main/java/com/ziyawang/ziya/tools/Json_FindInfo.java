package com.ziyawang.ziya.tools;


import com.ziyawang.ziya.entity.FindInfoEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/07/19 0029.
 */

public class Json_FindInfo {
    public static List<FindInfoEntity> getParse(String jsonStr) throws JSONException {

        List<FindInfoEntity> data = new ArrayList<FindInfoEntity>();

        JSONObject jsonObject = new JSONObject(jsonStr) ;
        JSONArray array = jsonObject.getJSONArray("data") ;

        FindInfoEntity findInfoEntity = null;
        for(int i = 0;i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);

            String projectId = object.getString("ProjectID") ;
            String proArea = object.getString("ProArea");
            String fromWhere = object.getString("FromWhere");
            String assetType = object.getString("AssetType");
            String totalMoney = object.getString("TotalMoney");
            String transferMoney = object.getString("TransferMoney");
            String status = object.getString("Status");
            String rate = object.getString("Rate");
            String requirement = object.getString("Requirement");
            String buyerNature = object.getString("BuyerNature");
            String informant = object.getString("Informant");
            String buyer = object.getString("Buyer");
            String typeName = object.getString("TypeName");
            String projectNumber = object.getString("ProjectNumber");
            String member = object.getString("Member");
            String certifyState = object.getString("CertifyState");
            String publishState = object.getString("PublishState");
            String Corpore = object.getString("Corpore");
            String wordDes = object.getString("WordDes");
            String investType = object.getString("InvestType");
            String year = object.getString("Year");
            String userID = object.getString("UserID");
            String price = object.getString("Price");
            String payFlag = object.getString("PayFlag");

            findInfoEntity = new FindInfoEntity(projectId , proArea,fromWhere,assetType,totalMoney,transferMoney,status,rate,requirement,buyerNature,
                    informant,buyer,typeName,projectNumber,member,certifyState , publishState ,Corpore,wordDes , investType, year , userID , price , payFlag );
            data.add(findInfoEntity) ;
        }
        return data;
    }
}
