package com.thkmon.diff.main;

import com.thkmon.diff.form.DiffForm;
import com.thkmon.diff.util.LogUtil;

public class BBDiffHelper {

	public static void main(String[] args) {
		LogUtil.debug("BBDiffHelper");
		
		try {
			DiffForm form = new DiffForm();
			
		} catch (Exception e) {
			LogUtil.debug(e);
		}
	}
}