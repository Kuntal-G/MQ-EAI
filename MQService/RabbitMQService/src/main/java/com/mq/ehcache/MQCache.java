package com.mq.ehcache;

import java.util.UUID;

import com.arc.util.CacheObject;
import com.arc.util.EvictElement;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;



public class MQCache { 
    private  final CacheManager cacheManager;
    
    private static final MQCache INSTANCE =new MQCache();
     
   
    private Ehcache mqCache;
     
    private MQCache()
    
    {
    	cacheManager = CacheManager.create(Thread.currentThread().getContextClassLoader().getResourceAsStream("ehcache.xml"));
    	mqCache = cacheManager.getEhcache("MQCache");
    	EvictElement thread= new EvictElement();
		thread.start();
    }
  
    public static MQCache instance(){
    
	   return INSTANCE;
   }
    
  
    public void add(UUID channeid, CacheObject cacheValue)
    {
        Element element = new Element(channeid, cacheValue);
        mqCache.put(element);
    }
     
  
    public CacheObject get(UUID channelid)
    {
        Element element = mqCache.get(channelid);
        if (element != null)
        {            
            return (CacheObject) element.getValue();
        }
         
        return null;
    }
    
    public void evictCache(UUID key){
    	mqCache.remove(key);
		}
    
    public void expireCache(){
    	mqCache.getKeysWithExpiryCheck();
    	
    }
}
