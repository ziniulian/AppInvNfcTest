package com.invengo.activity;

public class UnConnectedException extends RuntimeException {

	private static final long serialVersionUID = 9168928833112799453L;

	public UnConnectedException(){
		
	}
	
	public UnConnectedException(String message){
		super(message);
	}
	
}
