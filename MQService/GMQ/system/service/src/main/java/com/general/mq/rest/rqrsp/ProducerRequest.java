package com.general.mq.rest.rqrsp;

import java.util.List;

import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.rest.rqrsp.common.BaseRequest;

public class ProducerRequest extends BaseRequest{
	//queueName is actually the exchangeName
	private String queueName;
	private String routingKey=MQConfig.DEFAULT_ROUTINGKEY;
	private String clientId;
	private List<String> messages;
	private String attributeName;
	private String attributeValue;
	private String parentId;
	private int priority=4;
		
	
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
		
		if(routingKey!=null&&!routingKey.equals(""))
		this.routingKey = routingKey;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String producerId) {
		this.clientId = producerId;
	}
	
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	
	
	
}
