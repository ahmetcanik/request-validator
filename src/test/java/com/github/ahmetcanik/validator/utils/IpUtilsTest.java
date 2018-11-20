package com.github.ahmetcanik.validator.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IpUtilsTest {

	@Test
	public void ipToLong() {
		String ip = "192.168.1.1";
		long expected = 3232235777L;
		long actual = IpUtils.ipToLong(ip);
		assertEquals(expected, actual);
	}

	@Test
	public void longToIp() {
		long ip = 3232235777L;
		String expected = "192.168.1.1";
		String actual = IpUtils.longToIp(ip);
		assertEquals(expected, actual);
	}
}