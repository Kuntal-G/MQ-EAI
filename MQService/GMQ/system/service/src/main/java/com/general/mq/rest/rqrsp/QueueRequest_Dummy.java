package com.general.mq.rest.rqrsp;

import com.general.mq.rest.rqrsp.common.BaseRequest;

public class QueueRequest_Dummy extends BaseRequest{
	
	private Integer queueId;
	//queueName is actually the exchangeName
	private String queueName;
	private String qName;
	private int maxAttmpt;
	private long nxtAttmptDly;
	private int msgPriorityAttmpt;
	private String routingKey;
	public Integer getQueueId() {
		return queueId;
	}
	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String exchangeName) {
		this.queueName = exchangeName;
	}
	public String getqName() {
		return qName;
	}
	public void setqName(String queueName) {
		this.qName = queueName;
	}
	public int getMaxAttmpt() {
		return maxAttmpt;
	}
	public void setMaxAttmpt(int maxAttmpt) {
		this.maxAttmpt = maxAttmpt;
	}
	public long getNxtAttmptDly() {
		return nxtAttmptDly;
	}
	public void setNxtAttmptDly(long nxtAttmptDly) {
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
