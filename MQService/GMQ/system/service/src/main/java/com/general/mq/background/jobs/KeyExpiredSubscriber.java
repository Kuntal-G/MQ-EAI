package com.general.mq.background.jobs;

import redis.clients.jedis.Jedis;

import com.general.mq.cache.management.CacheManager;
import com.general.mq.cache.management.KeyExpiredListener;
import com.general.mq.common.util.conf.RedisConfig;

public class KeyExpiredSubscriber {
	
	private static final KeyExpiredSubscriber INSTANCE = new KeyExpiredSubscriber();

	public static KeyExpiredSubscriber instance() {
		return INSTANCE;
	}

	public void subscribe(){
		Jedis resource=CacheManager.getResource();
		resource.psubscribe(new KeyExpiredListener(), RedisConfig.KEYSPACE_CHANNEL);
		CacheManager.closeNReturnResource(resource);
		
	}

}
