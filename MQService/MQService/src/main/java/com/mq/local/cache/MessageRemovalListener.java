package com.mq.local.cache;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.mq.util.CacheObject;


public class MessageRemovalListener  implements RemovalListener<String, CacheObject>{
	
	public void onRemoval(RemovalNotification<String, CacheObject> notification) {
			try {
			System.out.println("Starting to call CleanUp Utility for rollback for MSGID : "+notification.getKey());
			 Connection connection = notification.getValue().getConnection();
		     Session session =notification.getValue().getSession();
		     session.rollback();
		     session.close();
		     connection.close();
			
		} catch (JMSException e) {
			e.printStackTrace();
		}catch(ClassCastException cle){
			cle.printStackTrace();
		}
		
	}
	 
		 
		
	 
	}