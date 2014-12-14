package com.general.mq.dao.model;

import java.io.Serializable;

public class Client implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String clientId;
	private int clientType;
	private Integer queueId;
	private int isActive;
	private String routingKey;
	private Long timeToLive;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public int getClientType() {
		return clientType;
	}
	public void setClientType(int clientType) {
		this.clientType = clientType;
	}
	public Integer getQueueId() {
		return queueId;
	}
	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	public Long getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(Long timeToLive) {
		this.timeToLive = timeToLive;
	}
	

}
