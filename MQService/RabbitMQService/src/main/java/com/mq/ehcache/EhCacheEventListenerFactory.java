package com.mq.ehcache;

import java.util.Properties;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

public class EhCacheEventListenerFactory extends CacheEventListenerFactory {

	@Override
	public CacheEventListener createCacheEventListener(final Properties properties) {
	return EhCacheEventListener.INSTANCE;
	}
	 
	}
