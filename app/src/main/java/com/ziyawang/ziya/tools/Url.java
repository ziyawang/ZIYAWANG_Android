package com.ziyawang.ziya.tools;

import java.io.File;

/**
 * Created by 牛海丰 on 2016/07/19 0029.
 */
public class Url {
    
    //测试服数据接口
    private static final String IP = "http://apitest.ziyawang.com" ;
    public static final String FileIP =  "http://imagestest.ziyawang.com";
    public static final String FileIPAudio =  "http://filestest.ziyawang.com";
    public static final String FileIPVideos =  "http://videos.ziyawang.com";
    //正式服数据接口
//    public static final String FileIP =  "http://images.ziyawang.com";
//    public static final String FileIPAudio =  "http://files.ziyawang.com";
//    public static final String FileIPVideos =  "http://videos.ziyawang.com";
//    private static final String IP = "http://api.ziyawang.com" ;
    /**
     * benben
     */
    public static final String Rule = "http://files.ziyawang.com/law.html" ;
    public static final String Gold = "http://files.ziyawang.com/rechargeproto.html" ;
    public static final String ShareVideo = "http://ziyawang.com/video/" ;
    public static final String ShareInfo = "http://ziyawang.com/project/" ;
    public static final String ShareService = "http://ziyawang.com/service/" ;

    public static final String PayCharge = IP + "/v1/pay?access_token=token&token=%s" ;
    public static final String ChangeNickName = IP + "/v1/auth/chusername?access_token=token&token=%s" ;
    public static final String GoldDetails = IP + "/v1/mybill?access_token=token&token=%s" ;
    public static final String RechargeType = IP + "/v1/pay/list?access_token=token" ;
    public static final String Pay = IP + "/v1/app/consume?access_token=token&token=%s" ;
    public static final String Login = IP + "/v1/auth/login";
    public static final String GetSMS = IP + "/v1/auth/getsmscode";
    public static final String Register = IP + "/v1/auth/register";
    public static final String FindPwd = IP + "/v1/auth/resetpwd";
    public static final String ChangeIcon = IP + "/v1/upload?access_token=token&token=%s" ;
    public static final String GetInfo = IP + "/v1/project/lists?access_token=token";
    public static final String Details_info = IP + "/v1/project/list/%s?token=%s";
    public static final String GetService = IP + "/v1/service/list?access_token=token";
    public static final String Details_service = IP + "/v1/service/list/%s?token=%s";
    public static final String GetMovie = IP + "/v1/video/list?access_token=token";
    public static final String Details_movie = IP + "/v1/video/list/%s?token=%s";
    public static final String ReleaseInfo = IP + "/v1/uploadfile?access_token=token&token=%s";
    public static final String Collect = IP + "/v1/collect?access_token=token&token=%s";
    public static final String MyRelease = IP + "/v1/project/mypro?access_token=token&token=%s";
    public static final String MyTeamWork = IP + "/v1/project/coolist?access_token=token&token=%s";
    public static final String MyCollectList = IP + "/v1/app/collect/list?access_token=token&token=%s";
    public static final String Cancle_TeamWork = IP + "/v1/project/cancel?access_token=token&token=%s";
    public static final String Cancle_Order = IP + "/v1/project/rushcancel?access_token=token&token=%s";
    public static final String Rush = IP + "/v1/project/rush?access_token=token&token=%s";
    public static final String RushList = IP + "/v1/project/rushlist/%s?access_token=token&token=%s";
    public static final String MyRush = IP + "/v1/project/myrush?access_token=token&token=%s";
    public static final String MyGoTeam = IP + "/v1/project/cooperate?access_token=token&token=%s";
    public static final String ServiceRegister = IP + "/v1/app/service/confirm?access_token=token&token=%s" ;
    public static final String ServiceReRegister = IP + "/v1/app/service/reconfirm?access_token=token&token=%s" ;
    public static final String Myicon = IP + "/v1/auth/me?access_token=token&token=%s" ;
    public static final String ChangePwd = IP + "/v1/auth/chpwd?access_token=token&token=%s" ;
    public static final String FeedBack = IP + "/v1/app/advice?access_token=token&token=%s" ;
    public static final String Search = IP + "/v1/search?access_token=token" ;
    public static final String Banner = IP + "/v1/app/banner?access_token=token" ;
    public static final String BannerTWO = IP + "/v1/app/twobanner?access_token=token" ;
    public static final String VideoCommentList = IP + "/v1/video/comment/list?access_token=token" ;
    public static final String VideoCommentSend = IP + "/v1/video/comment/create?access_token=token&token=%s" ;
    public static final String CheckUpdata = IP + "/v1/app/update?access_token=token" ;
    public static final String RCToken = IP + "/v1/rctoken?access_token=token&token=%s" ;
    public static final String RongIcon = IP + "/v1/app/uinfo?access_token=token&UserID=%s" ;
    public static final String GetMessage = IP + "/v1/getmessage?access_token=token&token=%s" ;
    public static final String ReadMessage = IP + "/v1/readmessage?access_token=token&token=%s" ;
    public static final String DelMessage = IP + "/v1/delmessage?access_token=token&token=%s" ;

    //头像路径
    public static final String IconPath = SDUtil.getSDPath() + File.separator + "ziya"+ File.separator + "icon.png" ;



}
