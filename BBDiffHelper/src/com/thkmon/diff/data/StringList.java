package com.thkmon.diff.data;

import java.util.ArrayList;

public class StringList extends ArrayList<String> {

	public String getNotError(int idx) {
		int lastIndex = this.size() - 1;
		if (idx > lastIndex) {
			return "";
		}
		
		return this.get(idx);
	}
}