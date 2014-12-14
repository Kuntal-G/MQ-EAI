package com.general.mq.monitoring.impl;



import java.util.ArrayList;
import java.util.StringTokenizer;

import redis.clients.jedis.Jedis;

import com.general.mq.cache.management.CacheManager;
import com.general.mq.monitoring.IHealth;
import com.general.mq.monitoring.MonitorCollector;
import com.general.mq.monitoring.metric.CacheMetrics;



public class MonitorCachePool implements IHealth {

	private static MonitorCachePool singleton = null;
	
	public static MonitorCachePool getInstance() {
		if (singleton!=null){return singleton;	}
		synchronized (MonitorCachePool.class.getName()) {
			if (singleton!=null){return singleton;}
			singleton = new MonitorCachePool();
		}
		return singleton;
	}

	

	@Override
	public MonitorCollector<CacheMetrics> collect() {
		MonitorCollector<CacheMetrics> collector = new MonitorCollector<CacheMetrics>();
		Jedis resource = CacheManager.getConnectionPool().getResource();
		if (resource!=null) {
			CacheMetrics cacheMetrics=prepareCacheMetric(resource.info());
			cacheMetrics.setPing(resource.ping());
			collector.addMetrics(cacheMetrics);
			CacheManager.getConnectionPool().returnResource(resource);
		}
		return collector;
	}

	//TODO:This method neeeds to be modified for optimization of String parsing.
	//Also removing hardcorded tring value to constant variable
	private static CacheMetrics prepareCacheMetric(String information){
		CacheMetrics metric=new CacheMetrics();
		metric.setName("CACHE-POOL-INFO");
		String[] infos=information.split("\n");

		for(int infoLength=0;infoLength<infos.length;infoLength++){

			if(infos[infoLength].startsWith("redis_version")){
				metric.setRedis_version(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("process_id")){
				metric.setProcess_id(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("tcp_port")){
				metric.setTcp_port(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("uptime_in_seconds")){
				metric.setUptime_in_seconds(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("uptime_in_days")){
				metric.setUptime_in_days(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("connected_clients")){
				metric.setConnected_clients(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("used_memory_human")){
				metric.setUsed_memory_human(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("used_memory_rss")){
				metric.setUsed_memory_rss(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("used_memory_peak_human")){
				metric.setUsed_memory_peak_human(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("used_memory_lua")){
				metric.setUsed_memory_lua(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("mem_fragmentation_ratio")){
				metric.setMem_fragmentation_ratio(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("rdb_last_bgsave_status")){
				metric.setRdb_last_bgsave_status(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("aof_enabled")){
				metric.setAof_enabled(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("aof_last_write_status")){
				metric.setAof_last_write_status(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("aof_current_size")){
				metric.setAof_current_size(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("total_connections_received")){
				metric.setTotal_connections_received(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("total_commands_processed")){
				metric.setTotal_commands_processed(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("rejected_connections")){
				metric.setRejected_connections(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("expired_keys")){
				metric.setExpired_keys(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("evicted_keys")){
				metric.setEvicted_keys(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("keyspace_hits")){
				metric.setKeyspace_hits(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("keyspace_misses")){
				metric.setKeyspace_misses(extractInfovalue(infos[infoLength],":"));
			}else if(infos[infoLength].startsWith("db0:keys")){
				metric.setKeyspaceInfo(extractInfovalue(infos[infoLength],","));
			}
		}

		return metric;
	}

	private static String extractInfovalue(String info,String seprator){
		String infovalue="";
		if(seprator.equals(":")){
		StringTokenizer strTkn = new StringTokenizer(info, seprator);
		ArrayList<String> arrLis = new ArrayList<String>(info.length());
		while(strTkn.hasMoreTokens())
		arrLis.add(strTkn.nextToken());
		infovalue=arrLis.get(1).trim();
		}else {
			String[] keySpaceSplit=info.split(",");
			infovalue=keySpaceSplit[0].replace("db0:", "total-").trim();
			}
		return infovalue;
	}

}
