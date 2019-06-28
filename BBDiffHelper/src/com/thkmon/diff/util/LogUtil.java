package com.thkmon.diff.util;

public class LogUtil {
	
	
	public static void debug(String str) {
		debugCore(str);
	}
	
	
	public static void debug(Throwable throwable) {
		debugCore(getStackTraceString(throwable));
	}
	
	
	public static void debugCore(String str) {
		System.out.println(str);
	}
	
	
	private static String getStackTraceString(Throwable throwable) {
	    if (throwable == null) {
	        return "";
	    }
	  
	    StackTraceElement[] elems = throwable.getStackTrace();
	    if (elems == null || elems.length == 0) {
	        return "";
	    }
	  
	    StringBuffer buff = new StringBuffer();
	    buff.append(throwable.getClass().getName() + ": " + throwable.getMessage());
	  
	    int elemCount = elems.length;
	    for (int i=0; i<elemCount; i++) {
	        if (elems[i] != null) {
	        	buff.append("\n\tat " + elems[i].toString());
	        }
	    }
	  
	    return buff.toString();
	}
}