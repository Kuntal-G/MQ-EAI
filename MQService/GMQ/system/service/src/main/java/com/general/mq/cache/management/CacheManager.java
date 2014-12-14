package com.general.mq.cache.management;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.general.mq.common.error.SystemCode;
import com.general.mq.common.exception.SystemException;
import com.general.mq.common.util.conf.MQConfig;

public class CacheManager {

	private static JedisPool connectionPool = null;

	/**
	 * Configuring Connection pool
	 * @throws DaoException 
	 */
	public static void configureConnPool()  {

		try {
			JedisPoolConfig redisPoolConfig=new JedisPoolConfig();			
			redisPoolConfig.setMaxTotal(MQConfig.CACHE_MAX_ACTIVE);
			redisPoolConfig.setMaxIdle(MQConfig.CACHE_MAX_IDLE);
			redisPoolConfig.setMinIdle(MQConfig.CACHE_MIN_IDLE);
			redisPoolConfig.setTestOnBorrow(MQConfig.CACHE_TEST_ON_BORROW);
			redisPoolConfig.setTestOnReturn(MQConfig.CACHE_TEST_ON_RETURN);
			redisPoolConfig.setTestWhileIdle(MQConfig.CACHE_TEST_WHILE_IDLE);
			redisPoolConfig.setNumTestsPerEvictionRun(MQConfig.CACHE_NUM_TEST_PER_EVICTIONRUN);
			redisPoolConfig.setTimeBetweenEvictionRunsMillis(MQConfig.CACHE_TIME_BETWEEN_EVICTIONRUNS_MILLIS);
			redisPoolConfig.setMaxWaitMillis(MQConfig.CACHE_MAX_WAIT);
			redisPoolConfig.setMinEvictableIdleTimeMillis(MQConfig.CACHE_MIN_EVICTIONABLE_IDLETIME_MILLIS);
			redisPoolConfig.setSoftMinEvictableIdleTimeMillis(MQConfig.CACHE_SOFT_MIN_EVICTIONABLE_IDLETIME_MILLIS);
			redisPoolConfig.setBlockWhenExhausted(MQConfig.CACHE_WHEN_EXHAUSTED_ACTION);
			redisPoolConfig.setMaxWaitMillis(MQConfig.CACHE_MAX_WAIT);

			// setup the connection pool
			connectionPool = new JedisPool(redisPoolConfig,MQConfig.CACHE_HOST,MQConfig.CACHE_PORT,MQConfig.CACHE_TIMEOUT);
			CacheManager.setConnectionPool(connectionPool);

		} catch (Exception e) {

			throw new SystemException("Creating Redis Connection Pool",e,SystemCode.UNABLE_TO_EXECUTE);
		}

	}



	/**
	 * Get Redis Resource from Redis Connection pool
	 * @throws RuntimeApplicationException 
	 */
	public static Jedis getResource() {

		Jedis resource = null;
		try {
			resource = getConnectionPool().getResource();
		} catch (Exception e) {

			throw new SystemException("Getting Resource from Redis Connection Pool",e,SystemCode.UNABLE_TO_EXECUTE);
		}
		return resource;

	}



	/**
	 * Connection will be released and kept in pool
	 */
	public static void closeNReturnResource(Jedis resource) {
		try {
			if (resource != null){
				connectionPool.returnResource(resource);
			}
		} catch (Exception e) {

			throw new SystemException("Releasing Resource to Redis Connection Pool",e,SystemCode.UNABLE_TO_EXECUTE);
		}

	}


	/**
	 * Connection will be released and kept in pool
	 */
	public static void returnBrokenResource(Jedis resource) {
		try {
			if (resource != null){
				connectionPool.returnBrokenResource(resource);
			}
		} catch (Exception e) {

			throw new SystemException("Releasing Broken Resource to Redis Connection Pool",e,SystemCode.UNABLE_TO_EXECUTE);

		}

	}


	public static JedisPool getConnectionPool()  {
		if(connectionPool==null) { 
			CacheManager.configureConnPool(); 
		}
		return connectionPool;
	}

	public static void setConnectionPool(JedisPool connectionPool) {
		CacheManager.connectionPool = connectionPool;
	}


}