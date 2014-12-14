package com.general.mq.service.test;

import org.junit.Assert;
import org.junit.Test;

import com.general.mq.base.test.BaseTestCase2;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.rest.QueueResourceTest;
import com.general.mq.rest.rqrsp.QueueResponse;

public class TestCase_3 extends BaseTestCase2{
	
	@Test
	public void testMessageCount() {
		QueueResourceTest qrt = new QueueResourceTest();
		QueueResponse qrp;
		try {
			qrp=qrt.testGetMessageCount("DelayQueue.properties", "ConsumerDelay.properties");
			Assert.assertEquals(true, qrp.success);
		}catch (SystemException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (ApplicationException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

}
