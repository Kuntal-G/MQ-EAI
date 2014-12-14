package com.general.mq.base.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.util.conf.StatusConfig;
import com.general.mq.rest.ConsumerResourceTest;
import com.general.mq.rest.ProducerResourceTest;
import com.general.mq.rest.QueueResourceTest;

public class BaseTestCase1 extends BaseTestCase{

	@BeforeClass
	public static void  setUp(){
		startUp();
		ConsumerResourceTest crt = new ConsumerResourceTest();
		QueueResourceTest qrt = new QueueResourceTest();
		ProducerResourceTest prt = new ProducerResourceTest();

		//Queue Creation.
		try{
			qrt.testCreateQueue("DefaultQueue.properties");			
		}catch(ApplicationException e){
			if(e.get(StatusConfig.QUEUE_ERROR).equals(StatusConfig.QUEUE_ALREADY_EXIST)){
				System.out.println("Queue already exist.");
			}
		}
		try{
		//Producer Registraion.
		prt.registerProducerDefaultQ();
		//Consumer Registraion.
		crt.registerConsumerDefaultQ();
		}catch(ApplicationException e){
			e.printStackTrace();

		}
	}

	@AfterClass
	public static void tearDown(){
		//nothing to do right now
	}

}
