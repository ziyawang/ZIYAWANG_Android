package com.ziyawang.ziya.newstitle;

import com.ziyawang.ziya.videotitle.NewsClassify;

import java.util.ArrayList;

/**
 * Created by 牛海丰 on 2016/11/11.
 */
public class ConstantsNews {
    public static ArrayList<NewsClassify> getData() {
        ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
        NewsClassify classify = new NewsClassify();
        classify.setId(0);
        classify.setTitle("行业资讯");
        newsClassify.add(classify);
        classify = new NewsClassify();
        classify.setId(1);
        classify.setTitle("资芽讲堂");
        newsClassify.add(classify);
        classify = new NewsClassify();
        classify.setId(2);
        classify.setTitle("行业研究");
        newsClassify.add(classify);
        return newsClassify;
    }
}
