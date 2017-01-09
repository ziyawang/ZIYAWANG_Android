package com.ziyawang.ziya.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 牛海丰 on 2016/9/2.
 */
public class GetBenSharedPreferences {
    /**
     * 得到用户的ticket
     * @param context 静态上下文
     * @return login
     */
    public static String getTicket(Context context){
        SharedPreferences loginCode = context.getSharedPreferences("loginCode", context.MODE_PRIVATE);
        String login = loginCode.getString("loginCode", null);
        return login ;
    }
    /**
     * 得到用户的spphoneNumber
     * @param context 静态上下文
     * @return spphoneNumber
     */
    public static String getSpphoneNumber(Context context){
        final SharedPreferences myNumber = context.getSharedPreferences("myNumber", context.MODE_PRIVATE);
        String spphoneNumber = myNumber.getString("myNumber", null);
        return spphoneNumber ;
    }
    /**
     * 得到用户的role
     * @param context 静态上下文
     * @return role
     */
    public static String getRole(Context context){
        final SharedPreferences role = context.getSharedPreferences("role", context.MODE_PRIVATE);
        String root = role.getString("role", null);
        return root ;
    }
    /**
     * 得到用户的userId
     * @param context 静态上下文
     * @return userId
     */
    public static String getUserId(Context context){
        final SharedPreferences userId = context.getSharedPreferences("userId", context.MODE_PRIVATE);
        String userID = userId.getString("userId", null);
        return userID ;
    }
    /**
     * 得到用户的isLogin
     * @param context 静态上下文
     * @return isLogin
     */
    public static boolean getIsLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences("isLogin", context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        return isLogin ;
    }

    /**
     * 得到用户的isThree
     * @param context 静态上下文
     * @return isThree
     */
    public static boolean getisThree(Context context){
        SharedPreferences sp = context.getSharedPreferences("isThree", context.MODE_PRIVATE);
        boolean isThree = sp.getBoolean("isThree", false);
        return isThree ;
    }

    /**
     * 得到用户的right(会员类型)
     * @param context 静态上下文
     * @return right
     */
    public static String getRight (Context context){
        final SharedPreferences right01 = context.getSharedPreferences("right", context.MODE_PRIVATE);
        String right = right01.getString("right", null);
        return right ;
    }


    /**
     * 得到用户的level(星级认证)
     * @param context 静态上下文
     * @return level
     */
    public static String getLevel (Context context){
        final SharedPreferences level01 = context.getSharedPreferences("level", context.MODE_PRIVATE);
        String level = level01.getString("level", null );
        return level ;
    }


}
