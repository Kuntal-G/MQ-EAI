package com.general.mq.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;


public class HistoryDto{

	public Integer id;
	public String queueName;
	public String clientId;
	public String message;
	public String msgAttrName;
	public String msgAttrValue;
	public int status;
	public String parentId;
	private Date loggingTime;
	public String remark;
	
	public Date toTime;
	public Date fromTime;
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getLoggingTime() {
		return loggingTime;
	}
	public void setLoggingTime(Date loggingTime) {
		this.loggingTime = loggingTime;
	}
	
}
