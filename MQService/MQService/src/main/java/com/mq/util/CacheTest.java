package com.mq.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class CacheTest {
	public static void main(String[] args) {
		Caching cache=new Caching();
		
		 Map<Integer,Person> mapper=new ConcurrentHashMap<Integer,Person>();
		
		for(int i=6;i<10;i++){
			 Person p= new Person();
			
			p.setId(i);
			p.setName("TEST Hello-"+i);
			
			mapper.put(i, p);
		
		}
		cache.loadCache(mapper);
		System.out.println("ID- 0 has value"+cache.getEntry(6).getId());

		
	}

}
