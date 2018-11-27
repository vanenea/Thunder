package com.chen.util;

public class JsonResult<T> {

	private String code;
	private String msg;
	private T data;
	public JsonResult() {
		super();
	}
	public JsonResult(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	public JsonResult(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "JsonResult [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
	
}
