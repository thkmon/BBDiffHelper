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
	
	
	public static String ltrim(String str, String mark) {
		if (str == null || str.length() == 0) {
			return "";
		}
		
		if (mark == null || mark.length() == 0) {
			return str;
		}
		
		int cutIndex = 0;
		int len = str.length();
		for (int i=0; i<len; i++) {
			if (str.substring(i, i+1).equals(mark)) {
				continue;
			}
			
			cutIndex = i;
			break;
		}
		
		return str.substring(cutIndex);
	}
	
	
	public static String rtrim(String str, String mark) {
		if (str == null || str.length() == 0) {
			return "";
		}
		
		if (mark == null || mark.length() == 0) {
			return str;
		}
		
		int cutIndex = 0;
		int len = str.length();
		int lastIndex = len - 1;
		for (int i=lastIndex; i>=0; i--) {
			if (str.substring(i, i+1).equals(mark)) {
				continue;
			}
			
			cutIndex = i;
			break;
		}
		
		return str.substring(0, cutIndex + 1);
	}
	
	
	public static String trim(String str, String mark) {
		return rtrim(ltrim(str, mark), mark);
	}
	
	
	/**
	 * 공백 라인(띄어쓰기, 탭, 개행으로만 이루어진 문자열)인지 체크하기
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkIsSpaceLine(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		
		if (str.matches("[ \t\r\n]*")) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * StringList 의 일부를 문자열로 리턴. 단, 앞뒤로 공백 라인(띄어쓰기, 탭, 개행으로만 이루어진 라인)은 제거한다.
	 * 
	 * @param strList
	 * @param beginIdx
	 * @param endIdx
	 * @return
	 */
	public static String getStringByList(StringList strList, int beginIdx, int endIdx) {
		
		StringBuffer buff = new StringBuffer();
		
		int newBeginIdx = getIndexOfNotSpaceLine(strList, beginIdx, endIdx);
		if (newBeginIdx == -1) {
			return "";
		}
		
		int newEndIdx = getIndexOfNotSpaceLineBackward(strList, beginIdx, endIdx);
		if (newEndIdx == -1) {
			return "";
		}
		
		for (int i=newBeginIdx; i<=newEndIdx; i++) {
			if (i > newBeginIdx) {
				buff.append("\n");
			}
			buff.append(strList.get(i));
		}
		
		return buff.toString();
	}
	
	
	/**
	 * 공백 라인(띄어쓰기, 탭, 개행으로만 이루어진 라인)이 아닌 인덱스를 지정한 범위 앞에서부터 찾는다.
	 * 
	 * @param strList
	 * @param beginIdx
	 * @param endIdx
	 * @return
	 */
	public static int getIndexOfNotSpaceLine(StringList strList, int beginIdx, int endIdx) {
		if (strList == null || strList.size() == 0) {
			return -1;
		}
		
		if (beginIdx < 0) {
			beginIdx = 0;
		}
		
		if (endIdx < 0) {
			endIdx = 0;
		}
		
		int lastIdx = strList.size() - 1;
		
		if (beginIdx > lastIdx) {
			beginIdx = lastIdx;
		}
		
		if (endIdx > lastIdx) {
			endIdx = lastIdx;
		}
		
		for (int i=beginIdx; i<=endIdx; i++) {
			if (!checkIsSpaceLine(strList.get(i))) {
				return i;
			}
		}
		
		return -1;
	}
	
	
	/**
	 * 공백 라인(띄어쓰기, 탭, 개행으로만 이루어진 라인)이 아닌 인덱스를 지정한 범위 뒤에서부터 찾는다.
	 * 
	 * @param strList
	 * @param beginIdx
	 * @param endIdx
	 * @return
	 */
	public static int getIndexOfNotSpaceLineBackward(StringList strList, int beginIdx, int endIdx) {
		if (strList == null || strList.size() == 0) {
			return -1;
		}
		
		if (beginIdx < 0) {
			beginIdx = 0;
		}
		
		if (endIdx < 0) {
			endIdx = 0;
		}
		
		int lastIdx = strList.size() - 1;
		
		if (beginIdx > lastIdx) {
			beginIdx = lastIdx;
		}
		
		if (endIdx > lastIdx) {
			endIdx = lastIdx;
		}
		
		for (int i=endIdx; i>=beginIdx; i--) {
			if (!checkIsSpaceLine(strList.get(i))) {
				return i;
			}
		}
		
		return -1;
	}
}