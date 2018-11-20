package com.github.ahmetcanik.validator.services.entity;

public class StatsQuery {
	private Integer customerID;
	private Long timestamp;

	public StatsQuery(Integer customerID, Long timestamp) {
		this.customerID = customerID;
		this.timestamp = timestamp;
	}

	public Integer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
