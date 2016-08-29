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

            findInfoEntity = new FindInfoEntity(projectId , proArea,fromWhere,assetType,totalMoney,transferMoney,status,rate,requirement,buyerNature,
                    informant,buyer,typeName,projectNumber,member,certifyState , publishState );
            data.add(findInfoEntity) ;
//            switch (type){
//                case "资产包转让" :
//
//                    String a_a = object.getString("ProArea");
//                    String a_b = object.getString("FromWhere");
//                    String a_c = object.getString("AssetType");
//                    String a_d = object.getString("TotalMoney");
//                    String a_e = object.getString("TransferMoney");
//                    String a_f = "";
//                    String a_g = "";
//                    String a_h = "";
//                    String a_m = "";
//                    String a_j = "";
//                    String a_k = "";
//
//                    findInfoEntity = new FindInfoEntity(projectId , a_a,a_b,a_c,a_d,a_e,a_f,a_g,a_h,a_m,a_j,a_k,type );
//                    data.add(findInfoEntity);
//
//
//                    break;
//                case "委外催收" :
//
//                    String b_a = object.getString("ProArea");
//                    String b_b = "" ;
//                    String b_c = object.getString("AssetType");
//                    String b_d = object.getString("TotalMoney");
//                    String b_e = "";
//                    String b_f = object.getString("Status");
//                    String b_g = object.getString("Rate");
//                    String b_h = "";
//                    String b_m = "";
//                    String b_j = "";
//                    String b_k = "";
//
//                    findInfoEntity = new FindInfoEntity(projectId,b_a,b_b,b_c,b_d,b_e,b_f,b_g,b_h,b_m,b_j,b_k,type );
//                    data.add(findInfoEntity);
//
//                    break;
//                case "法律服务" :
//
//
//                    String c_a = object.getString("ProArea");
//                    String c_b = "" ;
//                    String c_c = object.getString("AssetType");
//                    String c_d = "";
//                    String c_e = "";
//                    String c_f = "";
//                    String c_g = "";
//                    String c_h = object.getString("Requirement");
//                    String c_m = "";
//                    String c_j = "";
//                    String c_k = "";
//
//                    findInfoEntity = new FindInfoEntity(projectId,c_a,c_b,c_c,c_d,c_e,c_f,c_g,c_h,c_m,c_j,c_k,type );
//                    data.add(findInfoEntity);
//
//                    break;
//                case "商业保理" :
//
//                    String d_a = object.getString("ProArea");
//                    String d_b = "";
//                    String d_c = "";
//                    String d_d = object.getString("TotalMoney");
//                    String d_e = "";
//                    String d_f = "";
//                    String d_g = "";
//                    String d_h = "";
//                    String d_m = object.getString("BuyerNature");
//                    String d_j = "";
//                    String d_k = "";
//
//                    findInfoEntity = new FindInfoEntity(projectId,d_a,d_b,d_c,d_d,d_e,d_f,d_g,d_h,d_m,d_j,d_k,type );
//                    data.add(findInfoEntity);
//
//                    break;
//                case "融资借贷" :
//
//                    String e_a = object.getString("ProArea");
//                    String e_b = "";
//                    String e_c = object.getString("AssetType");
//                    String e_d = object.getString("TotalMoney");
//                    String e_e = "";
//                    String e_f = "";
//                    String e_g = object.getString("Rate");
//                    String e_h = "";
//                    String e_m = "";
//                    String e_j = "";
//                    String e_k = "";
//
//                    findInfoEntity = new FindInfoEntity(projectId,e_a,e_b,e_c,e_d,e_e,e_f,e_g,e_h,e_m,e_j,e_k,type );
//                    data.add(findInfoEntity);
//
//                    break;
//                case "典当担保" :
//
//                    String f_a = object.getString("ProArea");
//                    String f_b = "";
//                    String f_c = object.getString("AssetType");
//                    String f_d = object.getString("TotalMoney");
//                    String f_e = "";
//                    String f_f = "";
//                    String f_g = "";
//                    String f_h = "";
//                    String f_m = "";
//                    String f_j = "";
//                    String f_k = "";
//
//                    findInfoEntity = new FindInfoEntity(projectId,f_a,f_b,f_c,f_d,f_e,f_f,f_g,f_h,f_m,f_j,f_k,type );
//                    data.add(findInfoEntity);
//
//                    break;
//                case "悬赏信息" :
//
//                    String aa_a = object.getString("ProArea");
//                    String aa_b = "";
//                    String aa_c = object.getString("AssetType");
//                    String aa_d = object.getString("TotalMoney");
//                    String aa_e = "";
//                    String aa_f = "";
//                    String aa_g = "";
//                    String aa_h = "";
//                    String aa_m = "";
//                    String aa_j = "";
//                    String aa_k = "";
//
//                    findInfoEntity = new FindInfoEntity(projectId,aa_a,aa_b,aa_c,aa_d,aa_e,aa_f,aa_g,aa_h,aa_m,aa_j,aa_k,type );
//                    data.add(findInfoEntity);
//
//                    break;
//                case "尽职调查" :
//
//                    String bb_a = object.getString("ProArea");
//                    String bb_b = "";
//                    String bb_c = object.getString("AssetType");
//                    String bb_d = "";
//                    String bb_e = "";
//                    String bb_f = "";
//                    String bb_g = "";
//                    String bb_h = "";
//                    String bb_m = "";
//                    String bb_j = object.getString("Informant");
//                    String bb_k = "";
//
//                    findInfoEntity = new FindInfoEntity(projectId,bb_a,bb_b,bb_c,bb_d,bb_e,bb_f,bb_g,bb_h,bb_m,bb_j,bb_k,type );
//                    data.add(findInfoEntity);
//
//                    break;
//                case "固产转让" :
//
//                    String bc_a = object.getString("ProArea");
//                    String bc_b = "";
//                    String bc_c = object.getString("AssetType");
//                    String bc_d = "";
//                    String bc_e = object.getString("TransferMoney");
//
//                    String bc_f = "";
//                    String bc_g = "";
//                    String bc_h = "";
//                    String bc_m = "";
//                    String bc_j = "";
//                    String bc_k = "";
//
//                    findInfoEntity = new FindInfoEntity(projectId,bc_a,bc_b,bc_c,bc_d,bc_e,bc_f,bc_g,bc_h,bc_m,bc_j,bc_k,type );
//                    data.add(findInfoEntity);
//
//                    break;
//                case "资产求购" :
//
//                    String bd_a = object.getString("ProArea");
//                    String bd_b = "";
//                    String bd_c = object.getString("AssetType");
//                    String bd_d = "";
//                    String bd_e = "";
//
//                    String bd_f = "";
//                    String bd_g = "";
//                    String bd_h = "";
//                    String bd_m = "";
//                    String bd_j = "";
//                    String bd_k = object.getString("Buyer");
//
//                    findInfoEntity = new FindInfoEntity(projectId,bd_a,bd_b,bd_c,bd_d,bd_e,bd_f,bd_g,bd_h,bd_m,bd_j,bd_k,type );
//                    data.add(findInfoEntity);
//
//                    break;
//                case "债权转让" :
//
//                    String be_a = object.getString("ProArea");
//                    String be_b = "";
//                    String be_c = object.getString("AssetType");
//                    String be_d = object.getString("TotalMoney");
//                    String be_e = object.getString("TransferMoney");
//
//                    String be_f = "";
//                    String be_g = "";
//                    String be_h = "";
//                    String be_m = "";
//                    String be_j = "";
//                    String be_k = "";
//
//                    findInfoEntity = new FindInfoEntity(projectId,be_a,be_b,be_c,be_d,be_e,be_f,be_g,be_h,be_m,be_j,be_k,type );
//                    data.add(findInfoEntity);
//
//                    break;
//
//            }


        }
        return data;
    }
}
