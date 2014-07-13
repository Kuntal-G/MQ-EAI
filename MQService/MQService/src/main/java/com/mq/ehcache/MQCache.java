package com.mq.ehcache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.mq.util.CacheObject;
import com.mq.util.EvictElement;

public class MQCache { 
    private  final CacheManager cacheManager;
    
    private static final MQCache INSTANCE =new MQCache();
     
   
    private Ehcache mqCache;
     
    private MQCache()
    
    {
    	System.out.println("URL>>>"+Thread.currentThread().getContextClassLoader().getResource("ehcache.xml").toString());
     cacheManager = CacheManager.create(Thread.currentThread().getContextClassLoader().getResourceAsStream("ehcache.xml"));
    	mqCache = cacheManager.getEhcache("MQCache");
    	EvictElement thread= new EvictElement();
		thread.start();
    }
  
    public static MQCache instance(){
    
	   return INSTANCE;
   }
    
  
    public void add(String msgId, CacheObject cacheValue)
    {
        Element element = new Element(msgId, cacheValue);
        mqCache.put(element);
    }
     
  
    public CacheObject get(String key)
    {
        Element element = mqCache.get(key);
        if (element != null)
        {            
            return (CacheObject) element.getValue();
        }
         
        return null;
    }
    
    public void evictCache(String key){
    	mqCache.remove(key);
		}
    
    public void expireCache(){
    	mqCache.getKeysWithExpiryCheck();
    	
    }
}
