package com.github.ahmetcanik.validator.data.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "hourly_stats")
public class HourlyStats {
	private int id;
	private int customerId;
	private Timestamp time;
	private long requestCount;
	private long invalidCount;

	@Id
	@Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Basic
	@Column(name = "customer_id")
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@Basic
	@Column(name = "time")
	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Basic
	@Column(name = "request_count")
	public long getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(long requestCount) {
		this.requestCount = requestCount;
	}

	@Basic
	@Column(name = "invalid_count")
	public long getInvalidCount() {
		return invalidCount;
	}

	public void setInvalidCount(long invalidCount) {
		this.invalidCount = invalidCount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HourlyStats that = (HourlyStats) o;
		return id == that.id &&
				customerId == that.customerId &&
				requestCount == that.requestCount &&
				invalidCount == that.invalidCount &&
				Objects.equals(time, that.time);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, customerId, time, requestCount, invalidCount);
	}
}
