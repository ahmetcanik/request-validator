package com.github.ahmetcanik.validator.services.entity;

import javax.validation.constraints.NotNull;

public class CollectorRequest {
	@NotNull(message = "customerID cannot be null")
	private Integer customerID;

	@NotNull(message = "tagID cannot be null")
	private Integer tagID;

	@NotNull(message = "userID cannot be null")
	private String userID;

	@NotNull(message = "remoteIP cannot be null")
	private String remoteIP;

	@NotNull(message = "timestamp cannot be null")
	private Long timestamp;

	public Integer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}

	public Integer getTagID() {
		return tagID;
	}

	public void setTagID(Integer tagID) {
		this.tagID = tagID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
