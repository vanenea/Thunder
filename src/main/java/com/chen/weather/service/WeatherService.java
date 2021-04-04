package com.chen.weather.service;

import java.io.IOException;
import java.util.List;

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
	List<WeatherData> getWeatherData(String urlApi) throws ClientProtocolException, IOException;
}
