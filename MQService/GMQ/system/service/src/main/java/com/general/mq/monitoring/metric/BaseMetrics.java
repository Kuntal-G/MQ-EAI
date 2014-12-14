package com.general.mq.monitoring.metric;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BaseMetrics 
{
	private  String name;
	private String ping;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPing() {
		return ping;
	}
	public void setPing(String ping) {
		this.ping = ping;
	}

	
}