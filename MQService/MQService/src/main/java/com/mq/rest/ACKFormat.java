package com.mq.rest;

import java.io.Serializable;

public class ACKFormat implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String clientId;
	private String msgId;
	private String ack;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getAck() {
		return ack;
	}
	public void setAck(String ack) {
		this.ack = ack;
	}
	
}
