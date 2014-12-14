package com.general.mq.monitoring;


public interface IHealth
{
	public MonitorCollector<?> collect();
}