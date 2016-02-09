package com.kaltura.media.quality.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by asher.saban on 3/8/2015.
 */
public class StringUtils {

    private static final Calendar calendarInstance = Calendar.getInstance();

    public static String generateRandomSuffix() {
        return new SimpleDateFormat("yyMMdd_HHmmss").format(calendarInstance.getTime());
    }

//	public static String md5(String str) throws NoSuchAlgorithmException {
//		MessageDigest m = MessageDigest.getInstance("MD5");
//		byte[] digest = m.digest(str.getBytes());
//		BigInteger bigInt = new BigInteger(1,digest);
//		return bigInt.toString(16);
//	}

	public static String join(Collection<?> list, String delimiter) {
		return join(list.toArray(), delimiter);
	}

	public static String join(Object[] list, String delimiter) {
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
}
