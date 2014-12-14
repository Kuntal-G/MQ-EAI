package com.general.mq.base.test;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.service.cache.adaptor.ChannelAdaptor;
import com.general.mq.service.cache.adaptor.ClientAdaptor;
import com.general.mq.service.cache.adaptor.QueueAdaptor;

/**
 * A Base Test Case class for initializing local cache.
 * All Test Class in Service module must extends this base class.
 *
 */
public abstract class BaseTestCase {

	/**
	 * Initialize all local cache on Start up of each Service class test case execution.
	 * @return 
	 * @throws ApplicationException
	 */

	public static void startUp(){
		try {
			ClientAdaptor.instance().getAllClient();
			QueueAdaptor.instance().loadAllExchanges();
			QueueAdaptor.instance().loadAllQueues();	
			ChannelAdaptor.instance().findAllChannels();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
}
