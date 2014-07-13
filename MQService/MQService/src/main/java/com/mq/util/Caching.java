package com.mq.util;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class Caching {
	 
	  private static final long MAX_SIZE = 100;
	 
	  private final Cache <Integer, Person> cache;
	 
	  public Caching() {
	    cache = CacheBuilder.newBuilder().maximumSize( MAX_SIZE ).build( );
	  }
	 
	  public Person getEntry( Integer key ) {

	    return cache.getIfPresent(key);
	  }
	 
	  public void loadCache(Map<Integer,Person> map) {
		  
	
			//cache.put(key, p); 
			cache.putAll(map);
	  }
		 
	

	}
