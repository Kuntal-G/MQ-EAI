package com.general.mq.dao.model;

import java.io.Serializable;
import java.sql.Timestamp;


public class History implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String queueName;
	private String clientId;
	private String message;
	private String msgAttrName;
	private String msgAttrValue;
	private int status;
	private String parentId;
	private Timestamp loggingTime;
	private String reason;
	
	private Timestamp fromTime;
	private Timestamp toTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
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
	public String getMsgAttrName() {
		return msgAttrName;
	}
	public void setMsgAttrName(String msgAttrName) {
		this.msgAttrName = msgAttrName;
	}
	public String getMsgAttrValue() {
		return msgAttrValue;
	}
	public void setMsgAttrValue(String msgAttrValue) {
		this.msgAttrValue = msgAttrValue;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Timestamp getLoggingTime() {
		return loggingTime;
	}
	public void setLoggingTime(Timestamp loggingTime) {
		this.loggingTime = loggingTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String failureReason) {
		this.reason = failureReason;
	}
	public Timestamp getFromTime() {
		return fromTime;
	}
	public Timestamp getToTime() {
		return toTime;
	}
	public void setToTime(Timestamp toTime) {
		this.toTime = toTime;
	}
	public void setFromTime(Timestamp fromTime) {
		this.fromTime = fromTime;
	}
	

}
