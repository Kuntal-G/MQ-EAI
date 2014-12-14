package com.general.mq.cache.management;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.conf.RedisConfig;

import redis.clients.jedis.Jedis;


public class RedisServiceProvider {
	private static final RedisServiceProvider INSTANCE = new RedisServiceProvider();

	public static RedisServiceProvider instance() {
		return INSTANCE;
	}

	/**
	 * Method used for storing Message+MsgID in cache
	 * @param cacheKey
	 * @param cacheValue
	 */

	public  void storeMessage(String cacheKey,Map<String,String> cacheValue) {
		MQLogger.l.debug("Entering  RedisServiceProvider.storeMessage with cacheKey: "+cacheKey);
		Jedis resource=CacheManager.getResource();
		resource.hmset(cacheKey,cacheValue);
		CacheManager.closeNReturnResource(resource);
		MQLogger.l.debug("Leaving  RedisServiceProvider.storeMessage");

	}

	/**
	 * Method to insert processing time corresponding to message
	 * @param cacheKey
	 * @param ttl
	 */

	public  void storeDTAG(String cacheKey,int ttl,int priority, String customAttrib) {
		MQLogger.l.debug("Entering  RedisServiceProvider.storeDTAG with cacheKey: "+cacheKey+" TTL: "+ttl+" Priority: "+priority+" Custom Attribute: "+customAttrib);
		Jedis resource=CacheManager.getResource();
		Map<String,String> map = new HashMap<String, String>();
		map.put(RedisConfig.PRIORITY,Integer.toString(priority));
		if(customAttrib!=null){
			map.put(RedisConfig.CUSTOM_ATTRIB,customAttrib);
		}
		resource.hmset(cacheKey, map);
		resource.pexpire(cacheKey, ttl);
		CacheManager.closeNReturnResource(resource);
		MQLogger.l.debug("Leaving  RedisServiceProvider.storeDTAG");

	}


	public  boolean keyExist(String cacheKey) {
		MQLogger.l.debug("Entering  RedisServiceProvider.keyExist");
		Jedis resource=CacheManager.getResource();
		boolean keyExist=resource.exists(cacheKey);
		CacheManager.closeNReturnResource(resource);
		MQLogger.l.debug("Leaving  RedisServiceProvider.keyExist with: "+keyExist);
		return keyExist;
	}


	/**
	 * Method used for decrementing the max attempt value for particular MSG.
	 * @param cacheKey
	 * @param cacheField
	 * @param changeByValue - for incrementing or decrementing(in-case of attempt)
	 */
	public  void updateMessage(String cacheKey,String cacheField,int changeByValue) {
		MQLogger.l.debug("Entering  RedisServiceProvider.updateMessage with CacheKey: "+cacheKey+"CacheField: "+cacheField+" ChangeByValue:"+changeByValue);
		Jedis resource=CacheManager.getResource();
		resource.hincrBy(cacheKey, cacheField,changeByValue);
		CacheManager.closeNReturnResource(resource);
		MQLogger.l.debug("Leaving  RedisServiceProvider.updateMessage");
	}


	/**
	 * Method used for Storing ParentID and its processed message Count status
	 * @param cacheKey
	 * @param cacheValue
	 */
	public  void storeParentId(String cacheKey,Map<String,String> cacheValue) {
		MQLogger.l.debug("Entering  RedisServiceProvider.storeParentId with Key: "+cacheKey);
		Jedis resource=CacheManager.getResource();
		resource.hmset(cacheKey,cacheValue);
		CacheManager.closeNReturnResource(resource);
		MQLogger.l.debug("Leaving  RedisServiceProvider.storeParentId");
	}

	/**
	 * Method used for updating ParentID MSG processed count value
	 * @param cacheKey
	 * @param processedCount
	 * @param successCount
	 * @param failedCount
	 */
	public  void updateParentId(String cacheKey,boolean isProcessedCount,boolean isSuccessCount,boolean isFailedCount) {
		MQLogger.l.debug("Entering  RedisServiceProvider.updateParentId with Key: "+cacheKey);
		Jedis resource=CacheManager.getResource();
		if(isProcessedCount){
			resource.hincrBy(cacheKey, RedisConfig.PROCCESSED_COUNT, 1);
		}else if(isSuccessCount){
			resource.hincrBy(cacheKey, RedisConfig.SUCCESS_COUNT, 1);
		}else if(isFailedCount){
			resource.hincrBy(cacheKey, RedisConfig.FAILURE_COUNT, 1);
		}
		CacheManager.closeNReturnResource(resource);
		MQLogger.l.debug("Leaving  RedisServiceProvider.updateParentId");
	}

	/**
	 * Method used for updating Message processing Time To Live (in MilliSeconds)
	 * @param cacheKey
	 * @param milliseconds
	 */

	public  void updateTTL(String cacheKey,long milliseconds) {
		MQLogger.l.debug("Entering  RedisServiceProvider.updateTTL with Key : "+cacheKey+" TTL in Millis : "+milliseconds);
		Jedis resource=CacheManager.getResource();
		resource.pexpire(cacheKey, (int)milliseconds);
		CacheManager.closeNReturnResource(resource);
		MQLogger.l.debug("Leaving  RedisServiceProvider.updateTTL");
	}

	public List<String> getHashMsg(String key,String...fields)	{
		MQLogger.l.debug("Entering  RedisServiceProvider.getHashMsg with Key: "+key+" Fields: "+fields);
		Jedis resource=CacheManager.getResource();
		List<String> valueList = resource.hmget(key, fields);
		CacheManager.closeNReturnResource(resource);
		MQLogger.l.debug("Leaving  RedisServiceProvider.getHashMsg with List size: "+valueList.size());
		return valueList;

	}


	public void setMsg(String key,String value)	{ 
		MQLogger.l.debug("Entering  RedisServiceProvider.setMsg with Key: "+key+" Value:"+value);
	Jedis resource=CacheManager.getResource();
	resource.set(key,value);
	CacheManager.closeNReturnResource(resource);
	MQLogger.l.debug("Leaving  RedisServiceProvider.setMsg");
	}

	public String getMsg(String key){
		MQLogger.l.debug("Entering  RedisServiceProvider.getMsg with Key: "+key);
		Jedis resource=CacheManager.getResource();
		String value = resource.get(key);
		CacheManager.closeNReturnResource(resource);
		MQLogger.l.debug("Leaving  RedisServiceProvider.getMsg : "+value);
		return value;

	}

	public long deleteKey(String cacheKey){
		MQLogger.l.debug("Entering  RedisServiceProvider.deleteKey  with Key: "+cacheKey);
		Jedis resource=CacheManager.getResource();
		Long delkeyStatus=resource.del(cacheKey);
		CacheManager.closeNReturnResource(resource);
		MQLogger.l.debug("Leaving  RedisServiceProvider.deleteKey");
		return delkeyStatus;

	}
	
}
