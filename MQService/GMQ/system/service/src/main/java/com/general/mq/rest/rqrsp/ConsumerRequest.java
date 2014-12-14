package com.general.mq.rest.rqrsp;

import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.rest.rqrsp.common.BaseRequest;

public class ConsumerRequest extends BaseRequest{
	
	//queueName is actually the exchangeName
	private String queueName;
	private String routingKey=MQConfig.DEFAULT_ROUTINGKEY;
	private String clientId;
	private long processTime=MQConfig.MESSAGE_PROCESSING_TIME;
	private String messageId;	
	private int timeUnit=MQConfig.MESSAGE_PROCESSING_TIMEUNIT;
	private String  failureReason;
	
	
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String exchangeName) {
		this.queueName = exchangeName;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String consumerId) {
		this.clientId = consumerId;
	}
	public long getProcessTime() {
		return processTime;
	}
	public void setProcessTime(long processTime) {
		this.processTime = processTime;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public int getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(int timeUnit) {
		this.timeUnit = timeUnit;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
		
	
	

}
