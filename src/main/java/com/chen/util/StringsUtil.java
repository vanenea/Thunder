package com.chen.util;

import java.util.Calendar;

/**
 * 工具类
 * @author Administrator
 *
 */
public class StringsUtil {

	/**
	 * 判断是否是工作日
	 * @return
	 */
	public static synchronized boolean isWorkDay() {
		Calendar c = Calendar.getInstance();
		
		int i = c.get(Calendar.DAY_OF_WEEK);
		if(i==6 || i==7) {
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(isWorkDay());
	}
	
}
