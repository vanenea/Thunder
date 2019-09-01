package com.chen.hotNews.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.chen.hotNews.bean.NewsData;
import com.chen.hotNews.service.HotNewsService;
import com.chen.util.JsonResult;

@Controller
@RequestMapping("hot")
public class HotNewsController {

	/**
	 * 日志打印
	 */
	private static final Logger LOGGER = Logger.getLogger(HotNewsController.class); 
	
	@Autowired
	private HotNewsService hotNewsService;
	/**
	 * 获取知乎热门
	 * @return
	 */
	@RequestMapping(value = "getZhiHuHotNews", headers = {"Access-Control-Allow-Origin=*", "Access-Control-Allow-Credentials=true"})
	@ResponseBody
	public JsonResult<List<NewsData>> getZhiHuHotNews(){
		LOGGER.info("====== 获取热门 >> 知乎 ======");
		try {
			List<NewsData> datas = hotNewsService.getZhiHuHotNews();
			return new JsonResult<List<NewsData>>("0000","获取知乎热门成功", datas);
		} catch (Exception e) {
			return new JsonResult<List<NewsData>>("1111",e.getMessage());
		}
		
	}
	
	/**
	 * 获取虎扑热门
	 * @return
	 */
	@RequestMapping(value = "getHuPuHotNews", headers = {"Access-Control-Allow-Origin=*", "Access-Control-Allow-Credentials=true"})
	@ResponseBody
	public JsonResult<List<JSONObject>> getHuPuHotNews(){
		LOGGER.info("====== 获取热门 >> 虎扑 ======");
		try {
			List<JSONObject> datas = hotNewsService.getHuPuHotNews();
			return new JsonResult<List<JSONObject>>("0000","获取虎扑热门成功", datas);
		} catch (Exception e) {
			return new JsonResult<List<JSONObject>>("1111",e.getMessage());
		}
		
	}
	
}
