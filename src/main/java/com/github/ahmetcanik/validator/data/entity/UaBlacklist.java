package com.github.ahmetcanik.validator.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ua_blacklist")
public class UaBlacklist {
	private String ua;

	@Id
	@Column(name = "ua")
	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UaBlacklist that = (UaBlacklist) o;
		return Objects.equals(ua, that.ua);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ua);
	}
}
