package com.thkmon.diff.data;

public class SVNLogData {

	private String path = "";
	private char type = 'M';
	private long oldRevision = 0;
	private long newRevision = 0;
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public char getType() {
		return type;
	}
	
	public void setType(char type) {
		this.type = type;
	}

	public long getOldRevision() {
		return oldRevision;
	}

	public void setOldRevision(long oldRevision) {
		this.oldRevision = oldRevision;
	}

	public long getNewRevision() {
		return newRevision;
	}

	public void setNewRevision(long newRevision) {
		this.newRevision = newRevision;
	}
}