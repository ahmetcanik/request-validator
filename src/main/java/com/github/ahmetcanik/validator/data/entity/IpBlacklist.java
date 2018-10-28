package com.github.ahmetcanik.validator.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ip_blacklist")
public class IpBlacklist {
	private int ip;

	@Id
	@Column(name = "ip")
	public int getIp() {
		return ip;
	}

	public void setIp(int ip) {
		this.ip = ip;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		IpBlacklist that = (IpBlacklist) o;
		return ip == that.ip;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ip);
	}
}
