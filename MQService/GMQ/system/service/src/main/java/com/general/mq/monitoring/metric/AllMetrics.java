package com.general.mq.monitoring.metric;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AllMetrics{

	private CacheMetrics cacheMetrics;
	private JvmMetrics jvmMetrics;
	private DBMetrics dbMetrics;
	private QueueMetrics queueMetrics;
	
	public CacheMetrics getCacheMetrics() {
		return cacheMetrics;
	}
	public void setCacheMetrics(CacheMetrics cacheMetrics) {
		this.cacheMetrics = cacheMetrics;
	}
	public JvmMetrics getJvmMetrics() {
		return jvmMetrics;
	}
	public void setJvmMetrics(JvmMetrics jvmMetrics) {
		this.jvmMetrics = jvmMetrics;
	}
	public DBMetrics getDbMetrics() {
		return dbMetrics;
	}
	public void setDbMetrics(DBMetrics dbMetrics) {
		this.dbMetrics = dbMetrics;
	}
	public QueueMetrics getQueueMetrics() {
		return queueMetrics;
	}
	public void setQueueMetrics(QueueMetrics queueMetrics) {
		this.queueMetrics = queueMetrics;
	}


	
}
