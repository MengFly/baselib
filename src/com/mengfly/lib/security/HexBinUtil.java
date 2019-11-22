package com.mengfly.lib.security;

import java.util.Objects;

import com.mengfly.lib.StringUtil;

public class HexBinUtil {
	
	/**
	 * 将字符串转ASCII码后再转十六进制
	 * @param str 返回的十六进制字节码以空格进行分割
	 */
	public static String str2ASCII2Hex(String str) {
		str = Objects.requireNonNullElse(str, "");
		
		StringBuffer result = new StringBuffer(str.length()*3 - 1);
		
		for(char c : str.toCharArray()) {
			result.append(String.format("%02X ", (int)c));
		}
		if(result.length() != 0) {
			result.delete(result.length()-1, result.length());
		}
		return result.toString();
	}
	
	public static String int2Hex(int i, int len) {
		return String.format("%0" + len + "X", i);
	}
	
	public static String int2Bin(int i, int len) {
		String binaryString = Integer.toBinaryString(i);
		return changeLength(binaryString, len);
	}
	
	public static String hex2Bin(String hex, int length) {
		String res = Integer.toBinaryString(Integer.parseInt(hex, 16));
		return changeLength(res, length);
	}
	
	private static String changeLength(String res, int length) {
		String zs = StringUtil.newRepeat("0", length);
		if(res.length() < length) {
			return zs.substring(0, length-res.length()) + res;
		}else if(res.length() > length) {
			return res.substring(0, length);
		}
		return res;
	}
	
	public static void main(String[] args) {
		System.out.println(hex2Bin("1C", 8));
//		System.out.println(String.format("%03d", 4));
	}
}
