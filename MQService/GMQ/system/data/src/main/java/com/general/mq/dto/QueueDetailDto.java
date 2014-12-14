package com.general.mq.dto;

import com.general.mq.common.util.conf.MQConfig;

public class QueueDetailDto {
	
	public Integer queueId;
	//queueName is actually the exchangeName
	public String queueName;
	public String qName;
	public int maxAttmpt=MQConfig.DEFAULT_MAXATTMPT;
	public long nxtAttmptDly=MQConfig.DEFAULT_NXTATTMPTDLY;
	public int msgPriorityAttmpt=MQConfig.DEFAULT_MSGPRIORITYATTMPT;
	private String routingKey=MQConfig.DEFAULT_ROUTINGKEY;
	private int timeUnit=1;	
	
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		
		if(!routingKey.equals(""))
		this.routingKey = routingKey;
	}
	public int getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(int timeUnit) {
		if(timeUnit>0&&timeUnit<4)
		this.timeUnit = timeUnit;
	}
	
	
	
	
	
	
}
