package com.chen.hotNews.bean;

public class NewsData {

	private String id;
	private String title;
	private String url;
	private String hot;
	private String description;
	private String commentCount;
	private String createdTime;
	private String lastCommentTime;
	private String createdUser;
	private String lastCommentUser;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getLastCommentTime() {
		return lastCommentTime;
	}
	public void setLastCommentTime(String lastCommentTime) {
		this.lastCommentTime = lastCommentTime;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getLastCommentUser() {
		return lastCommentUser;
	}
	public void setLastCommentUser(String lastCommentUser) {
		this.lastCommentUser = lastCommentUser;
	}
	
	public String getHot() {
		return hot;
	}
	public void setHot(String hot) {
		this.hot = hot;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	@Override
	public String toString() {
		return "NewsData [id=" + id + ", title=" + title + ", url=" + url + ", hot=" + hot + ", description="
				+ description + ", commentCount=" + commentCount + ", createdTime=" + createdTime + ", lastCommentTime="
				+ lastCommentTime + ", createdUser=" + createdUser + ", lastCommentUser=" + lastCommentUser + "]";
	}
	
}
