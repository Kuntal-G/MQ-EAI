package com.mq.ehcache;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import com.mq.util.CacheObject;
import com.mq.util.EvictElement;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

public class EhCacheEventListener implements CacheEventListener  {
	
	public static final CacheEventListener INSTANCE = new EhCacheEventListener();

	@Override
	public void notifyElementEvicted(Ehcache cache, Element element) {
		System.out.println("Key value notifyElementEvicted: "+element.getKey()+" -- "+element.getValue());
		
		
	}

	@Override
	public void notifyElementExpired(Ehcache cache, Element element) {
		System.out.println("Key value notifyElementExpired: "+element.getKey()+" -- "+element.getValue());
		
		if (element.getValue() != null) {
			CacheObject cacheValue = (CacheObject) element.getValue();
			System.out.println("notifyElementExpired connection and Session value>>"+cacheValue.getConnection()+ " ** "+cacheValue.getSession());
			Connection connection = cacheValue.getConnection();
			Session session = cacheValue.getSession();
			
			try {
				session.rollback();
				session.close();
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
			cache.remove(element.getKey());
		}

	}

	@Override
	public void notifyElementPut(Ehcache cache, Element element)throws CacheException {
		System.out.println("Key value notifyElementPut: "+element.getKey()+" -- "+element.getValue());
		//Remove Null Object from Cache Store
		if (element.getObjectValue() == null) {
			cache.remove(element.getKey());
			}
		
		
	}

	@Override
	public void notifyElementRemoved(Ehcache cache, Element element)throws CacheException {
		System.out.println("Key value notifyElementRemoved: "+element.getKey()+" -- "+element.getValue());
		
		
	}

	@Override
	public void notifyElementUpdated(Ehcache cache, Element element)throws CacheException {
		System.out.println("Key value notifyElementUpdated: "+element.getKey()+" -- "+element.getValue());
		
		
	}

	@Override
	public void notifyRemoveAll(Ehcache cache) {
		System.out.println("Key value notifyRemoveAll: ");
			
		
	}
	
	@Override
	public void dispose() {}
	
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException("Singleton instance");
	}


}
