package com.general.mq.cache.management;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.general.mq.cache.management.CacheManager;

import redis.clients.jedis.Jedis;

public class CacheManagerTest {
		
	@Test
	public void testgetResource(){
		Jedis resource=CacheManager.getResource();
		assertNotNull(resource);
		CacheManager.closeNReturnResource(resource);
	}
	
	

}
