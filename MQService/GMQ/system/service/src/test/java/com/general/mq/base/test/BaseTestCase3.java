package com.general.mq.base.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BaseTestCase3 extends BaseTestCase{
	
	@BeforeClass
	public static void  setUp(){
		startUp();
			
	}	
	
	@AfterClass
	public static void tearDown(){
		//nothing to do right now
	}

}
