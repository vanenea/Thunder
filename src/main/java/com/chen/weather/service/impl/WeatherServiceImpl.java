package com.chen.weather.service.impl;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chen.util.HttpUtil;
import com.chen.weather.model.WeatherData;
import com.chen.weather.service.WeatherService;

@Service
public class WeatherServiceImpl implements WeatherService {
	
	/**
	 * 日志
	 */
	private final static Logger LOGGER = Logger.getLogger(WeatherServiceImpl.class); 
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Value("#{pro.toMail}")
	private String toMail;
	
	@Value("#{pro.username}")
	private String fromMail;
	
	@Value("#{pro.urlApi}")
	private String urlApi;
	
	@Scheduled(cron = "0 20 22 * * ? ")
	public int sendMail() {
		LOGGER.info("获取天气数据");
		HttpUtil http = new HttpUtil();
		WeatherData data = new WeatherData();
		try {
			String result = http.httpGet(urlApi,"utf-8");
			JSONObject jo = JSON.parseObject(result);
			JSONArray ja = jo.getJSONArray("HeWeather6");
			data.setLocation(ja.getJSONObject(0).getJSONObject("basic").getString("location"));
			jo = ja.getJSONObject(0).getJSONArray("daily_forecast").getJSONObject(1);
			data.setCond_code_d(jo.getString("cond_code_d"));
			data.setCond_code_n(jo.getString("cond_code_n"));
			data.setCond_txt_d(jo.getString("cond_txt_d"));
			data.setCond_txt_n(jo.getString("cond_txt_n"));
			data.setDate(jo.getString("date"));
			data.setPop(jo.getString("pop"));
			data.setHum(jo.getString("hum"));
			data.setTmp_max(jo.getString("tmp_max"));
			data.setTmp_min(jo.getString("tmp_min"));
			data.setUv_index(jo.getString("uv_index"));
			data.setVis(jo.getString("vis"));
			data.setWind_deg(jo.getString("wind_deg"));
			data.setWind_dir(jo.getString("wind_dir"));
			data.setWind_sc(jo.getString("wind_sc"));
			data.setWind_spd(jo.getString("wind_spd"));
			LOGGER.info("天气信息:"+data.toString());
		} catch (Exception e1) {
			LOGGER.error(e1.getMessage());
			LOGGER.info("获取天气数据异常");
			return -2;
		}
		
		LOGGER.info("发送的目的邮箱 [ "+toMail+" ]");
		MimeMessage mime = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			String[] users = toMail.split(";");
			for (int i = 0; i < users.length; i++) {
				String[] user = users[i].split(",");
				helper = new MimeMessageHelper(mime, true, "utf-8");
				helper.setTo(user[0]);// 收件人邮箱地址
				helper.setFrom(fromMail);// 
				helper.setSentDate(new Date());
				helper.setSubject("天气预报");// 主题
				String text = "您好"+user[1]+"。"+data.getLocation()+"明天["+data.getDate()+"]天气预报,白天"+data.getCond_txt_d()+",晚间"+data.getCond_txt_n()+",最高温度"+data.getTmp_max()+",最低温度"+data.getTmp_min()+",降水概率"+data.getPop()+",请注意天气变化！";
				helper.setText(text);// 正文
				mailSender.send(mime);
			}
		} catch (MessagingException e) {
			LOGGER.error(e.getMessage());
			LOGGER.info("发送邮件失败");
			return -1;
		}
		return 0;
	}

}
