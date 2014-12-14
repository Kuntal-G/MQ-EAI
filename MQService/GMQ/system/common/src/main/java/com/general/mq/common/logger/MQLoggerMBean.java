package com.general.mq.common.logger;

public  interface MQLoggerMBean {

	// a read-write attribute called level of type String
	public String getLevel();
	public void setLevel(String level);

	}
