package com.chen.util;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.chen.weather.service.impl.GoodsServiceImpl;

public class CommonUtil {

	/**
	 * 日志
	 */
	private static final Logger LOGGER = Logger.getLogger(GoodsServiceImpl.class);
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Value("#{pro.toMail}")
	private String toMail;
	
	@Value("#{pro.username}")
	private String fromMail;
	
	@Value("#{pro.urlApi}")
	private String urlApi;
	
	@Value("#{pro.flag}")
	private String mailFlag;
	
	public int sendMail(String item, String text) {
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
				helper.setSubject(item);// 主题
				helper.setText(text);// 正文
				mailSender.send(mime);
			}
			return 1;
		} catch (MessagingException e) {
			LOGGER.error("###### 发送邮件失败 ######", e);
			throw new RuntimeException("发送邮件失败", e);
		}
	}
}
