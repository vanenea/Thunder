package com.chen.weather.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.chen.weather.model.WeatherData;

public interface WeatherService {

	/**
	 * 发送邮件
	 * @return
	 */
	int sendMail();
	
	/**
	 * 发送短信
	 * @return
	 */
	int sendSMS();
	
	/**
	 * 获取天气数据
	 */
	WeatherData getWeatherData(String urlApi, String encode) throws ClientProtocolException, IOException;
}
