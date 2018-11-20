package com.github.ahmetcanik.validator.utils;

public class IpUtils {
	public static long ipToLong(String ipAddress) {
		long result = 0;
		String[] ipAddressInArray = ipAddress.split("\\.");

		for (int i = 3; i >= 0; i--) {
			long ip = Long.parseLong(ipAddressInArray[3 - i]);
			//left shifting 24,16,8,0 and bitwise OR
			//1. 192 << 24
			//1. 168 << 16
			//1. 1   << 8
			//1. 2   << 0
			result |= ip << (i * 8);
		}
		return result;
	}

	public static String longToIp(long ip) {
		return ((ip >> 24) & 0xFF) + "."
				+ ((ip >> 16) & 0xFF) + "."
				+ ((ip >> 8) & 0xFF) + "."
				+ (ip & 0xFF);

	}
}
