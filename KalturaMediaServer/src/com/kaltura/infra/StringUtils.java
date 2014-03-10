package com.kaltura.infra;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class StringUtils {
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}

	public static String join(Collection<?> list) {
		return join(list, null);
	}

	public static boolean contains(String delimitedString, String value) {
		return contains(delimitedString, value, ",");
	}

	public static boolean contains(String delimitedString, String value, String delimiter) {
		String[] items = delimitedString.split(delimiter);
		List<String> itemsList = Arrays.asList(items);
		return itemsList.contains(value);
	}
	
	public static String join(Collection<?> list, String delimiter) {
		if(delimiter == null)
			delimiter = ",";
		
		String str = "";
		boolean isFirst = true;
		for(Object item : list){
			if(!isFirst)
				str += delimiter;
			
			str += item;
			isFirst = false;
		}
		
		return str;
	}

	public static String getUniqueId() {
		SecureRandom prng;
		MessageDigest sha;
		try {
			prng = SecureRandom.getInstance("SHA1PRNG");
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ex) {
			return null;
		}

		String randomNum = new Integer(prng.nextInt()).toString();
		byte[] bytes = sha.digest(randomNum.getBytes());
		
		StringBuilder result = new StringBuilder();
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		for (int idx = 0; idx < bytes.length; ++idx) {
			byte b = bytes[idx];
			result.append(digits[(b & 0xf0) >> 4]);
			result.append(digits[b & 0x0f]);
		}
		return result.toString();
	}
}
