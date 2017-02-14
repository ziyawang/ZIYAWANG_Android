package com.ziyawang.ziya.tools;

import java.io.File;

/**
 * Created by 牛海丰 on 2016/07/19 0029.
 */
public class Url {
    
    //测试服数据接口
//    private static final String IP = "http://apitest.ziyawang.com" ;
//    public static final String FileIP =  "http://imagestest.ziyawang.com";
//    public static final String FileIPAudio =  "http://filestest.ziyawang.com";
//    public static final String FileIPVideos =  "http://videos.ziyawang.com";
    //正式服数据接口
    //public static final String FileIP =  "http://images.ziyawang.com";
    //public static final String FileIPAudio =  "http://files.ziyawang.com";
    //public static final String FileIPVideos =  "http://videos.ziyawang.com";
    //private static final String IP = "http://api.ziyawang.com" ;
    public static final String FileIP =  "http://images.ziyawang.com";
    public static final String FileIPAudio =  "http://files.ziyawang.com";
    public static final String FileIPVideos =  "http://videos.ziyawang.com";
    private static final String IP = "https://apis.ziyawang.com/zll/" ;
    /**
     * benben
     */
    public static final String Rule = "http://files.ziyawang.com/law.html" ;
    public static final String Gold = "http://files.ziyawang.com/rechargeproto.html" ;
    public static final String ShareVideo = "http://ziyawang.com/video/" ;
    public static final String ShareInfo = "http://ziyawang.com/project/" ;
    public static final String ShareService = "http://ziyawang.com/service/" ;
    public static final String ShareNews = "http://ziyawang.com/news/" ;

    public static final String PayCharge = IP + "pay?access_token=token&token=%s" ;
    public static final String ChangeNickName = IP + "auth/chusername?access_token=token&token=%s" ;
    public static final String GoldDetails = IP + "mybill?access_token=token&token=%s" ;
    public static final String RechargeType = IP + "pay/list?access_token=token" ;
    public static final String Pay = IP + "app/consume?access_token=token&token=%s" ;
    public static final String Login = IP + "auth/login";
    public static final String GetSMS = IP + "auth/getsmscode";
    public static final String Register = IP + "auth/register";
    public static final String FindPwd = IP + "auth/resetpwd";
    public static final String ChangeIcon = IP + "upload?access_token=token&token=%s" ;
    public static final String NOGetInfo = IP + "project/list?access_token=token&token=%s";

    public static final String GetService = IP + "service/list?access_token=token";
    public static final String Details_service = IP + "service/list/%s?token=%s";
    public static final String GetMovie = IP + "video/list?access_token=token";
    public static final String Details_movie = IP + "video/list/%s?token=%s";
    public static final String ReleaseInfo = IP + "uploadfile?access_token=token&token=%s";
    public static final String Collect = IP + "collect?access_token=token&token=%s";
    public static final String MyRelease = IP + "project/mypro?access_token=token&token=%s";
    public static final String MyTeamWork = IP + "project/coolist?access_token=token&token=%s";
    public static final String MyCollectList = IP + "app/collect/list?access_token=token&token=%s";
    public static final String Cancle_TeamWork = IP + "project/cancel?access_token=token&token=%s";
    public static final String Cancle_Order = IP + "project/rushcancel?access_token=token&token=%s";
    public static final String Rush = IP + "project/rush?access_token=token&token=%s";
    public static final String RushList = IP + "project/rushlist/%s?access_token=token&token=%s";
    public static final String MyRush = IP + "project/myrush?access_token=token&token=%s";
    public static final String MyGoTeam = IP + "project/cooperate?access_token=token&token=%s";
    public static final String ServiceRegister = IP + "app/service/confirm?access_token=token&token=%s" ;
    public static final String ServiceReRegister = IP + "app/service/reconfirm?access_token=token&token=%s" ;
    public static final String Myicon = IP + "auth/me?access_token=token&token=%s" ;
    public static final String ChangePwd = IP + "auth/chpwd?access_token=token&token=%s" ;
    public static final String FeedBack = IP + "app/advice?access_token=token&token=%s" ;
    public static final String Search = IP + "searchs?access_token=token" ;
    public static final String Banner = IP + "app/banner?access_token=token" ;
    public static final String BannerTWO = IP + "app/twobanner?access_token=token" ;
    public static final String VideoCommentList = IP + "video/comment/list?access_token=token" ;
    public static final String NewsCommentList = IP + "news/comment/list?access_token=token" ;
    public static final String VideoCommentSend = IP + "video/comment/create?access_token=token&token=%s" ;
    public static final String NewsCommentSend = IP + "news/comment/create?access_token=token&token=%s" ;
    public static final String CheckUpdata = IP + "app/update?access_token=token" ;
    public static final String RCToken = IP + "rctoken?access_token=token&token=%s" ;
    public static final String RongIcon = IP + "app/uinfo?access_token=token&UserID=%s" ;
    public static final String GetMessage = IP + "getmessage?access_token=token&token=%s" ;
    public static final String ReadMessage = IP + "readmessage?access_token=token&token=%s" ;
    public static final String DelMessage = IP + "delmessage?access_token=token&token=%s" ;
    public static final String ISPay = IP + "ispay?access_token=token&token=%s" ;
    public static final String Report = IP + "report?access_token=token&token=%s" ;
    public static final String ServiceAccount = IP + "count/service?access_token=token&token=%s" ;
    public static final String NewsLists = IP + "news/list?access_token=token" ;
    public static final String DetailsNews = IP + "news/list/%s?access_token=token&token=%s" ;
    public static final String GetQuestions = IP + "test/paper?access_token=token" ;
    public static final String SubmitQuestions = IP + "test/result?access_token=token&token=%s" ;

    //头像路径
    public static final String IconPath = SDUtil.getSDPath() + File.separator + "ziya"+ File.separator + "icon.png" ;

    //资芽V2的新接口
    public static final String V2Publish = IP + "uploadfile?access_token=token&token=%s";
    public static final String V2OtherPublish = IP + "entrust?access_token=token&token=%s";
    public static final String GetInfo = IP + "project/list?access_token=token&token=%s";
    public static final String V2MyCollectList = IP + "app/collect/list?access_token=token&token=%s";
    public static final String Details_info = IP + "project/list/%s?token=%s";

    public static final String V202PayCharge = IP + "pay?access_token=token&token=%s" ;
    public static final String V202VipRecord = IP + "pay/member/list?access_token=token&token=%s" ;
    public static final String V203StarRegister04 = IP + "lds/star?access_token=token&token=%s" ;

    public static final String V203StarList = IP + "star/list?access_token=token" ;

}
