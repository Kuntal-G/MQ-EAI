package com.general.mq.monitoring.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.general.mq.common.logger.MQLogger;
import com.general.mq.monitoring.IHealth;
import com.general.mq.monitoring.MonitorCollector;
import com.general.mq.monitoring.metric.AllMetrics;
import com.general.mq.monitoring.metric.BaseMetrics;
import com.general.mq.monitoring.metric.CacheMetrics;
import com.general.mq.monitoring.metric.DBMetrics;
import com.general.mq.monitoring.metric.JvmMetrics;
import com.general.mq.monitoring.metric.QueueMetrics;

public class MonitorHealth 
{
	private static MonitorHealth singleton = null;
	public static MonitorHealth getInstance() {
		if ( null != singleton) {return singleton;}
		synchronized (MonitorHealth.class.getName()) {
			if ( null != singleton){ return singleton;}
			singleton = new MonitorHealth();
		}
		return singleton;
	}
	
	
	public boolean INFO_ENABLED = MQLogger. l.isInfoEnabled();

	private HealthCheckAgent hcAgent = null;
	private List<IHealth> healthAgents = new ArrayList<IHealth>();
	
	private  MonitorHealth() {
	 MQLogger.l.info("Initializing monitor checking*****");
		this.hcAgent = new HealthCheckAgent();
	}
	
	public void cancel() {
		this.hcAgent.cancel();
	}
	
	public void register(IHealth healthAgent){
		 MQLogger.l.info("Registering: " + healthAgent.getClass().getName());
		healthAgents.add(healthAgent);
	}

	
	//--------------Monitoring Metrics Check Module-------------------
	public List<AllMetrics> healthCheck() {
		List<AllMetrics> allMetrics= new ArrayList<AllMetrics>();
		AllMetrics metrics;
		for (IHealth health : healthAgents) {
			metrics=new AllMetrics();
			MonitorCollector<?> collector = health.collect();
			if ( null == collector){ continue;}
			
			for (BaseMetrics mm : collector.getMetrics()) {	
				if(mm instanceof CacheMetrics){
					metrics.setCacheMetrics((CacheMetrics)mm);
				}else if(mm instanceof JvmMetrics){
					metrics.setJvmMetrics((JvmMetrics)mm);
				}else if(mm instanceof DBMetrics){
					metrics.setDbMetrics((DBMetrics)mm);
				}else if(mm instanceof QueueMetrics){
					metrics.setQueueMetrics((QueueMetrics)mm);
				}		
				
			}
			allMetrics.add(metrics);
		}
		
		return allMetrics;
	}
	
	
	
	private class HealthCheckAgent extends TimerTask {
		public void run() {
			try {
				if ( INFO_ENABLED) { MQLogger.l.info(healthCheck().toString()); }
			} 
			catch(Exception e) 	{
				 MQLogger.l.warn("Error in running monitoring check", e);
			}
		}
	}
}
