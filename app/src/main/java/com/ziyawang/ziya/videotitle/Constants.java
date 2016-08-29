package com.ziyawang.ziya.videotitle;

import java.util.ArrayList;


public class Constants {
	public static ArrayList<NewsClassify> getData() {
		ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
		NewsClassify classify = new NewsClassify();
		classify.setId(0);
		classify.setTitle("推荐");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1);
		classify.setTitle("资芽一分钟");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(2);
		classify.setTitle("行业说");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(3);
		classify.setTitle("资芽哈哈哈");
		newsClassify.add(classify);
//		classify = new NewsClassify();
//		classify.setId(4);
//		classify.setTitle("资芽一分钟");
//		newsClassify.add(classify);

		return newsClassify;
	}
}

