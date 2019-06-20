package com.invengo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NFCUtilies {

	/*
	 * char[]必须是0,1的数组，且大小必须为4的倍数
	 */
	public static String convertCharArrayToHexString(char[] char_array){
		int length = char_array.length;
		if(length <= 0 || (length % 4 != 0)){
			return null;
		}

		StringBuffer hexString = new StringBuffer();
		for(int i = 0; i < length; i += 4){
			String hex = Integer.toHexString(Integer.parseInt(
					String.valueOf(char_array[i])
							+ String.valueOf(char_array[i + 1])
							+ String.valueOf(char_array[i + 2])
							+ String.valueOf(char_array[i + 3]), 2));
			hexString.append(hex);
		}

		return hexString.toString();
	}

	public static String formatIntArrayToHexString(int[] intArray){
		if(intArray.length <= 0){
			return null;
		}
		StringBuffer hexString = new StringBuffer();
//		String[] hexString = new String[intArray.length];
		for(int i = 0; i < intArray.length; i++){
			String temp = Integer.toHexString(intArray[i]);
			if(temp.length() == 3){
				temp = "0" + temp;
			}else if(temp.length() == 2){
				temp = "00" + temp;
			}else if(temp.length() == 1){
				temp = "000" + temp;
			}
			hexString.append(temp);
		}

		return hexString.toString();
	}

	//hexString 为对称字符串,长度为2的倍数
	public static char[] convertHexStringToCharArray(String hexString){
		if(null == hexString || "".equals(hexString)){
			return null;
		}

		StringBuffer sb = new StringBuffer();
		String hex = hexString.replace(" ", "");
		for(int i = 0; i < hex.length(); i += 2){
			int tempData = Integer.parseInt(String.valueOf(hex.charAt(i)) + String.valueOf(hex.charAt(i + 1)), 16);
			sb.append(getEightBinaryString(Integer.toBinaryString(tempData)));
		}

		return sb.toString().toCharArray();
	}

	private static String getEightBinaryString(String oldBinaryString){
		if(null == oldBinaryString || "".equals(oldBinaryString) || oldBinaryString.length() == 0){
			return null;
		}
		String newBinaryString = null;
		switch (oldBinaryString.length()) {
			case 1:
				newBinaryString = "0000000" + oldBinaryString;
				break;
			case 2:
				newBinaryString = "000000" + oldBinaryString;
				break;
			case 3:
				newBinaryString = "00000" + oldBinaryString;
				break;
			case 4:
				newBinaryString = "0000" + oldBinaryString;
				break;
			case 5:
				newBinaryString = "000" + oldBinaryString;
				break;
			case 6:
				newBinaryString = "00" + oldBinaryString;
				break;
			case 7:
				newBinaryString = "0" + oldBinaryString;
				break;
			case 8:
				newBinaryString = oldBinaryString;
				break;
		}

		return newBinaryString;
	}

	public static String convertByteArrayToHexString(byte[] byte_array) {
		String s = "";
		if (byte_array == null)
			return s;

		for (int i = 0; i < byte_array.length; i++) {
			String hex = Integer.toHexString(byte_array[i] & 0xff);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			s = s + hex;
		}
		return s.toUpperCase();
	}

	public static byte[] convertHexStringToByteArray(String str) {
		str = str.replaceAll(" ", "");
		double fLen = str.length();
		int nSize = (int) Math.ceil(fLen / 2);

		String strArray = null;
		byte[] bytes = new byte[nSize];

		// Keep the string oven length.
		if (nSize * 2 > fLen) {
			strArray = str + "0";
		} else {
			strArray = str;
		}
		for (int i = 0; i < nSize; i++) {
			int index = i * 2;
			char[] cArr = new char[] { strArray.charAt(index),
					strArray.charAt(index + 1) };
			String s = new String(cArr);
			try {
				int j = Integer.parseInt(s, 16);
				bytes[i] = (byte) j;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}

	public static String formatDateToString(Date date){
		return formatDateToString(date, "yyyy-MM-dd hh:mm:ss");
	}

	public static String formatDateToString(Date date, String pattern){
		String dateStr = "";
		if(null == date || (null == pattern || "".equals(pattern))){
			return dateStr;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		dateStr = formatter.format(date);

		return dateStr;
	}

}
