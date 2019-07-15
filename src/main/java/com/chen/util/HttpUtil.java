package com.chen.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	
	private String charset = "UTF-8";
	private Map<String, String> header;
	
	HttpClient client = null; 
	
	public HttpUtil() {
		client = HttpClientBuilder.create().build();
	}
	
	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(String key, String value) {
		if(header == null) {
			header = new HashMap<String, String>();
		}
		header.put(key, value);
	}

	/**
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String httpGet(String url, String encode) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		if(header != null) {
			for (String key : header.keySet()) {
				get.addHeader(key, header.get(key));
			}
		}
		HttpResponse response = this.client.execute(get);
		String str = EntityUtils.toString(response.getEntity(),encode);
		return str;
	}
	
	
	public byte[] httpGet(String url) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		HttpResponse response = this.client.execute(get);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toByteArray(entity);
	}
}
