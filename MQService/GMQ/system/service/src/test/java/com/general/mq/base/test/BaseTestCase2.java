package com.general.mq.base.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.util.conf.StatusConfig;
import com.general.mq.rest.ConsumerResourceTest;
import com.general.mq.rest.ProducerResourceTest;
import com.general.mq.rest.QueueResourceTest;

public class BaseTestCase2 extends BaseTestCase{

	@BeforeClass
	public static void  setUp(){
		startUp();
		ConsumerResourceTest crt = new ConsumerResourceTest();
		QueueResourceTest qrt = new QueueResourceTest();
		ProducerResourceTest prt = new ProducerResourceTest();	
		
		try{
			//Queue Creation.
			qrt.testCreateQueue("DelayQueue.properties");		
		}catch(ApplicationException e){
			System.out.println(e.get(StatusConfig.REASON));
			if(e.get(StatusConfig.QUEUE_ERROR).equals(StatusConfig.QUEUE_ALREADY_EXIST)){
				System.out.println("Queue already exist.");
			}else{
				System.out.println(e.get(StatusConfig.REASON));
			}
		}
		try{			
			//Producer Registraion.
			prt.registerProducertoDelayQ();
			//Consumer Registraion.
			crt.registerConsumerDelayQ();
		}catch(ApplicationException e){
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDown(){
		//nothing to do right now
	}

}



