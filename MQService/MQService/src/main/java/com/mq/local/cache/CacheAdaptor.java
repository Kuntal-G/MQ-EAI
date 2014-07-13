package com.mq.local.cache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mq.util.CacheObject;

public class CacheAdaptor {

	private static final CacheAdaptor INSTANCE = new CacheAdaptor();
	
	private final Cache <String, CacheObject> cache;
	private final MessageRemovalListener listener;
		
	public CacheAdaptor(){
		 listener= new MessageRemovalListener();
		
		 cache = CacheBuilder.newBuilder()
				.maximumSize(100)
			    .expireAfterWrite(60, TimeUnit.SECONDS)
			    .removalListener(listener)
			    .build();
	}
	
	public static CacheAdaptor instance() {
		return INSTANCE;
	}

	
	public CacheObject getEntry( String key ) {
		    return cache.getIfPresent(key);
		  }
		 
	
	public void loadCache(String msgId, CacheObject cacheValue){
		cache.put(msgId, cacheValue);
		
	}
	
	public void evictCache(String msgId){
		cache.invalidate(msgId);	
		}
	
}
