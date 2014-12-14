package com.general.mq.monitoring.impl;

import com.general.mq.monitoring.IHealth;
import com.general.mq.monitoring.MonitorCollector;
import com.general.mq.monitoring.metric.JvmMetrics;

public class MonitorJVM implements IHealth{
	
	private static MonitorJVM singleton = null;
	
	public static MonitorJVM getInstance() {
		if (singleton!=null){return singleton;}
		synchronized (MonitorJVM.class.getName()){
			if (singleton!=null){return singleton;}
			singleton = new MonitorJVM();
		}
		return singleton;
	}
	
	
	
	
	@Override
	public MonitorCollector<JvmMetrics> collect() {
		MonitorCollector<JvmMetrics> collector = new MonitorCollector<JvmMetrics>();
		Runtime r = Runtime.getRuntime();
		if(null !=r){
		JvmMetrics jvmMetrics =new JvmMetrics();
		jvmMetrics.setName("JVM-INFO");
		jvmMetrics.setJvm_maxm_mem(String.valueOf(r.maxMemory()/1048576));
		jvmMetrics.setJvm_tot_mem(String.valueOf(r.totalMemory()/1048576));
		jvmMetrics.setJvm_used_mem(String.valueOf( r.freeMemory()/1048576));
		jvmMetrics.setJvm_thread_no(String.valueOf(Thread.activeCount()));
		collector.addMetrics(jvmMetrics);		
				
		}
		return collector;
	}

	
	
}
