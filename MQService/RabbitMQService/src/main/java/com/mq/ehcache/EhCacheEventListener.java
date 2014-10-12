package com.mq.ehcache;



import java.io.IOException;

import com.arc.util.CacheObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;



public class EhCacheEventListener implements CacheEventListener  {
	
	public static final CacheEventListener INSTANCE = new EhCacheEventListener();

	/*@Override
	public void notifyElementEvicted(Ehcache cache, Element element) {
				
	}

	@Override
	public void notifyElementExpired(Ehcache cache, Element element) {
			
		if (element.getValue() != null) {
			CacheObject cacheValue = (CacheObject) element.getValue();
			Channel channel = cacheValue.getChannel();
			try {
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			cache.remove(element.getKey());
		}

	}

	@Override
	public void notifyElementPut(Ehcache cache, Element element)throws CacheException {
		//Remove Null Object from Cache Store
		if (element.getObjectValue() == null) {
			cache.remove(element.getKey());
			}
		
		
	}

	@Override
	public void notifyElementRemoved(Ehcache cache, Element element)throws CacheException {
		
		
	}

	@Override
	public void notifyElementUpdated(Ehcache cache, Element element)throws CacheException {
			}

	@Override
	public void notifyRemoveAll(Ehcache cache) {
		}
	
	@Override
	public void dispose() {}*/
	
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException("Singleton instance");
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void notifyElementEvicted(Ehcache arg0, Element arg1) {
		// TODO Auto-generated method stub
		
	}

	public void notifyElementExpired(Ehcache cache, Element element) {
		// TODO Auto-generated method stub
		if (element.getValue() != null) {
			CacheObject cacheValue = (CacheObject) element.getValue();
			Channel channel = cacheValue.getChannel();
			Connection conn = channel.getConnection();
			try {
				channel.close();
				conn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			cache.remove(element.getKey());
		}
	}

	public void notifyElementPut(Ehcache arg0, Element arg1)
			throws CacheException {
		// TODO Auto-generated method stub
		
	}

	public void notifyElementRemoved(Ehcache arg0, Element arg1)
			throws CacheException {
		// TODO Auto-generated method stub
		
	}

	public void notifyElementUpdated(Ehcache arg0, Element arg1)
			throws CacheException {
		// TODO Auto-generated method stub
		
	}

	public void notifyRemoveAll(Ehcache arg0) {
		// TODO Auto-generated method stub
		
	}


}
