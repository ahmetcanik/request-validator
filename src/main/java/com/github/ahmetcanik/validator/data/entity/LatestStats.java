package com.github.ahmetcanik.validator.data.entity;

public class LatestStats {
	private long requestCount;
	private long invalidCount;

	public LatestStats() {
	}

	public LatestStats(long requestCount, long invalidCount) {
		this.requestCount = requestCount;
		this.invalidCount = invalidCount;
	}

	public long getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(long requestCount) {
		this.requestCount = requestCount;
	}

	public long getInvalidCount() {
		return invalidCount;
	}

	public void setInvalidCount(long invalidCount) {
		this.invalidCount = invalidCount;
	}
}
