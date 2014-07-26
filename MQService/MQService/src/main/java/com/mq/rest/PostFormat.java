package com.mq.rest;

import java.io.Serializable;

public class PostFormat implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String clientId;
	private String message;
	private Integer priority;
	
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	

}
