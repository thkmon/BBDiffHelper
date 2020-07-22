package com.thkmon.diff.main;

import com.thkmon.diff.form.SVNForm;
import com.thkmon.diff.util.LogUtil;

public class BBSvnHelper {

	public static void main(String[] args) {
		LogUtil.debug("BBSvnHelper");

		try {
			// DiffForm form = new DiffForm();
			SVNForm svnForm = new SVNForm();
			
		} catch (Exception e) {
			LogUtil.debug(e);
		}
	}

	
}