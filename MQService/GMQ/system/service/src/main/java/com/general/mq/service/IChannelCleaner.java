package com.general.mq.service;

import com.general.mq.common.exception.ApplicationException;

public interface IChannelCleaner {
	
	public void cleanChannel() throws ApplicationException;

}
