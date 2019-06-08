package com.chen.weather.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chen.util.JsonResult;
import com.chen.weather.model.Goods;
import com.chen.weather.service.GoodsService;

@RequestMapping("/goods")
public class GoodsInforController {
	
	/**
	 * 日志打印
	 */
	private static final Logger LOGGER = Logger.getLogger(WeatherController.class); 
	
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping("/getGoodsInfor")
	@ResponseBody
	public JsonResult<Goods> getGoodsInfor(){
		LOGGER.info("====== 获取商品信息 ======");
		try {
			Goods goods = goodsService.getGoodsInfor();
			return new JsonResult<Goods>("0000", "查询商品成功", goods);
		} catch (Exception e) {
			return new JsonResult<Goods>("0000", "查询商品失败");
		}
	}
}
