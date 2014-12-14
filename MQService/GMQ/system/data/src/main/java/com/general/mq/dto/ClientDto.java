package com.general.mq.dto;

import com.general.mq.common.util.conf.MQConfig;

public class ClientDto {
	
	public String clientId;
	public int clientType;
	public Integer queueId;
	public int isActive=1;
	public String routingKey=MQConfig.DEFAULT_ROUTINGKEY;
	//TODO:Default is 3 sec,to be configurable
	public Long timeToLive=3000l;
	public int timeUnit = 1;

}
