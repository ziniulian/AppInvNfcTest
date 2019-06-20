package com.invengo.entity;

import java.util.Date;

public class ResultInfoEntity {

	private String uid;
	private int result;
	private Date authenticateDate;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public Date getAuthenticateDate() {
		return authenticateDate;
	}
	public void setAuthenticateDate(Date authenticateDate) {
		this.authenticateDate = authenticateDate;
	}
	
}
