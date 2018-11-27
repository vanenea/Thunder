package com.chen.weather.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.chen.util.JsonResult;
import com.chen.weather.service.WeatherService;


@RestController
@RequestMapping("/weather")
public class WeatherController {
	
	private static final Logger LOGGER = Logger.getLogger(WeatherController.class); 
	
	@Autowired
	private WeatherService weatherService;
	
	@ResponseBody
	@RequestMapping("/sendMail")
	public JsonResult<Void> sendMail() {
		LOGGER.info("开始发送邮箱消息");
		int status = weatherService.sendMail();
		if(status==0) {
			LOGGER.info("发送邮箱成功！！！");
			return new JsonResult<Void>("0000", "发送邮箱成功");
		} else {
			LOGGER.info("发送邮箱失败！！！");
			return new JsonResult<Void>("1111", "发送邮箱失败");
		}
	}
	
}
