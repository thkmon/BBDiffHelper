package com.thkmon.diff.util;

import com.thkmon.diff.data.StringList;

public class StringUtil {

	public static StringList makeStringList(String str) {
		
		StringList resultList = new StringList();
		
		while (str.indexOf("\r\n") > -1) {
			str = str.replace("\r\n", "\n");
		}
		
		while (str.indexOf("\r") > -1) {
			str = str.replace("\r", "\n");
		}
		
		String[] strArr = str.split("\n");
		if (strArr != null && strArr.length > 0) {
			int strCount = strArr.length;
			for (int i=0; i<strCount; i++) {
				resultList.add(strArr[i]);
			}
		}
		
		return resultList;
	}
}
