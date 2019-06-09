package com.chen.weather.model;

public class Goods {

	private String id;
	private String name;
	private String title;
	private String price;
	private String isSold;
	private Integer skuQuantity;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getIsSold() {
		return isSold;
	}
	public void setIsSold(String isSold) {
		this.isSold = isSold;
	}
	public Integer getSkuQuantity() {
		return skuQuantity;
	}
	public void setSkuQuantity(Integer skuQuantity) {
		this.skuQuantity = skuQuantity;
	}
	@Override
	public String toString() {
		return "Goods [id=" + id + ", name=" + name + ", title=" + title + ", price=" + price + ", isSold=" + isSold
				+ ", skuQuantity=" + skuQuantity + "]";
	}
	
}
