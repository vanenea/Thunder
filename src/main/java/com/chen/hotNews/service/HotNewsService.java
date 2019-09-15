package com.chen.hotNews.service;

import java.util.List;

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
	 List<NewsData> getHuPuHotNews();
	 
	 /**
	  * 获取新浪热门
	  * @return
	  */
	 List<NewsData> getSinaNews();
}
