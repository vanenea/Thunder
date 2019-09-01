package com.chen.hotNews.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.chen.hotNews.bean.NewsData;

public interface HotNewsService {

	/**
	 * 获取知乎热门
	 * @return
	 */
	 List<NewsData> getZhiHuHotNews();
	 
	 /**
	  * 获取虎扑热门
	  * @return
	  */
	 List<JSONObject> getHuPuHotNews();
}
