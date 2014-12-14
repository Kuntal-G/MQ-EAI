package com.general.mq.dao.model;

import java.io.Serializable;

public class QueueDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer queueId;
	private String exchangeName;
	private String queueName;
	private int maxAttmpt;
	private Long nxtAttmptDly;
	private int msgPriorityAttmpt;
	private String routingKey;
	
	
	public Integer getQueueId() {
		return queueId;
	}
	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}
	public String getExchangeName() {
		return exchangeName;
	}
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public int getMaxAttmpt() {
		return maxAttmpt;
	}
	public void setMaxAttmpt(int maxAttmpt) {
		this.maxAttmpt = maxAttmpt;
	}
	public Long getNxtAttmptDly() {
		return nxtAttmptDly;
	}
	public void setNxtAttmptDly(Long nxtAttmptDly) {
		this.nxtAttmptDly = nxtAttmptDly;
	}
	public int getMsgPriorityAttmpt() {
		return msgPriorityAttmpt;
	}
	public void setMsgPriorityAttmpt(int msgPriorityAttmpt) {
		this.msgPriorityAttmpt = msgPriorityAttmpt;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	
	
	

}
