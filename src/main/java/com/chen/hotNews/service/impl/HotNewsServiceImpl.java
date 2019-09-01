package com.chen.hotNews.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chen.hotNews.bean.NewsData;
import com.chen.hotNews.service.HotNewsService;
import com.chen.util.HttpUtil;
@Service("hotNewsService")
public class HotNewsServiceImpl implements HotNewsService {

	private static final Logger LOGGER = Logger.getLogger(HotNewsServiceImpl.class);
	
	public List<NewsData> getZhiHuHotNews() {
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
		httpUtil.setHeader(":authority", "www.zhihu.com");
		httpUtil.setHeader(":scheme", "https");
		httpUtil.setHeader(":path", "/api/v3/feed/topstory/hot-lists/total?limit=50&desktop=true");
		httpUtil.setHeader("accept-language", "zh-CN,zh;q=0.9");
		httpUtil.setHeader("accept-encoding", "gzip, deflate");
		httpUtil.setHeader("accept", "*/*");
		httpUtil.setHeader(":method", "GET");
		httpUtil.setHeader("referer", "https://www.zhihu.com/hot");
		httpUtil.setHeader("sec-fetch-mode", "cors");
		httpUtil.setHeader("Cookie", "_xsrf=Slqgir316gsn8BvQVqgm5TmbUtCR4536");
	
		String url = "https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=50&desktop=true";
		LOGGER.info("###### 获取热门 >> 知乎 url [ "+ url +" ] ######");
		List<NewsData> newsData = new ArrayList<NewsData>();
		String response = "";
		try {
			response = httpUtil.httpGetForString(url);
			LOGGER.info("###### 获取热门 >> 知乎 response [ "+ response +" ] ######");
		} catch (Exception e) {
			LOGGER.error("###### 获取热门 >> 知乎 出错啦！！！######", e);
			throw new RuntimeException("网络异常");
		}
		try {
			JSONObject jo = JSON.parseObject(response);
			JSONArray ja = JSON.parseArray(jo.getString("data"));
			for (int i = 0; i < ja.size(); i++) {
				jo = ja.getJSONObject(i);
				NewsData nd = new NewsData();
				nd.setHot(jo.getString("detail_text"));
				jo = JSON.parseObject(jo.getString("target"));
				nd.setDescription(jo.getString("excerpt"));
				nd.setId(jo.getString("id"));
				nd.setUrl(jo.getString("url"));
				nd.setTitle(jo.getString("title"));
				nd.setCreatedTime(jo.getString("created"));
				newsData.add(nd);
			}
		} catch (Exception e) {
			LOGGER.error("###### 获取热门 >> 知乎 出错啦！！！######", e);
			throw new RuntimeException("数据异常");
		}
		return newsData;
	}


	public List<JSONObject> getHuPuHotNews() {
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
		String url = "https://bbs.hupu.com/all-gambia";
		LOGGER.info("###### 获取热门 >> 虎扑 url [ "+ url +" ] ######");
		String response = "";
		List<JSONObject> data = new ArrayList<JSONObject>();
		try {
			response = httpUtil.httpGetForString(url);
			LOGGER.info("###### 获取热门 >> 虎扑 response [ "+ response +" ] ######");
		} catch (Exception e) {
			LOGGER.error("###### 获取热门 >> 虎扑 出错啦！！！######", e);
			throw new RuntimeException("网络异常");
		}
		try {
			Document html = Jsoup.parse(response);
			Elements bbsHotPic = html.getElementsByClass("bbsHotPit");
			if(bbsHotPic != null && bbsHotPic.size()>0) {
				bbsHotPic = bbsHotPic.get(0).getElementsByClass("list");
				Iterator<Element> iterator = bbsHotPic.iterator();
				while(iterator.hasNext()) {
					Element elementsByAttribute = iterator.next();
					String headLine = elementsByAttribute.getElementsByTag("h3").get(0).getElementsByTag("span").get(0).text();
					JSONObject jo = new JSONObject();
					Iterator<Element> li = elementsByAttribute.getElementsByTag("li").iterator();
					JSONArray ja = new JSONArray();
					while(li.hasNext()) {
						Element span = li.next().getElementsByTag("span").get(0);
						Element a = span.getElementsByTag("a").get(0);
						JSONObject jo2 = new JSONObject();
						jo2.put("href", a.attr("href"));
						jo2.put("title", a.text());
						jo2.put("hot", span.getElementsByTag("em").get(0).text());
						ja.add(jo2);
					}
					jo.put("headLine", headLine);
					jo.put("data", ja);
					data.add(jo);
				}
			}
		} catch (Exception e) {
			LOGGER.error("###### 获取热门 >> 虎扑 出错啦！！！ ######", e);
			throw new RuntimeException("数据异常");
		}
		
		return data;
	}
	
	public static void main(String[] args) {
		HotNewsServiceImpl own = new HotNewsServiceImpl();
		List<JSONObject> huPuHotNews = own.getHuPuHotNews();
		System.out.println(huPuHotNews);
		char d = 0x52a8;
		System.out.println(d);
		
	}
}
