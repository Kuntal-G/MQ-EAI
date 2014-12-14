package com.general.mq.monitoring;


import java.util.ArrayList;
import java.util.List;

import com.general.mq.monitoring.metric.BaseMetrics;

public class MonitorCollector<T extends BaseMetrics>{
	List<T> metrics;
	
	public void addMetrics(T t){
		metrics =new ArrayList<T>();
		metrics.add(t);
		
	}

	public List<T> getMetrics() {
		return metrics;
	}

	
}