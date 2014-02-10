package com.kaltura.infra;

import java.util.Collection;

public class StringUtils {

	public static String join(Collection<?> list) {
		return join(list, null);
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
}
