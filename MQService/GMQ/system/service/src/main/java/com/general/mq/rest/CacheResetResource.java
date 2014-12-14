package com.general.mq.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.service.cache.adaptor.ChannelAdaptor;
import com.general.mq.service.cache.adaptor.ClientAdaptor;
import com.general.mq.service.cache.adaptor.QueueAdaptor;

public class CacheResetResource {
	
	@GET
	@Path("/queuesandexchanges")
	public void resetQueueCache()throws ApplicationException,SystemException {
		QueueAdaptor.instance().resetQueuesAndExchanges();
	}
	
	@GET
	@Path("/clients")
	public void resetClients()throws ApplicationException,SystemException {
		ClientAdaptor.instance().resetClients();
	}
	
	@GET
	@Path("/channels")
	public void resetChannels()throws ApplicationException,SystemException {
		ChannelAdaptor.instance().resetChannels();
	}
	
	@GET
	@Path("/all")
	public void resetAll()throws ApplicationException,SystemException {
		QueueAdaptor.instance().resetQueuesAndExchanges();
		ClientAdaptor.instance().resetClients();
		ChannelAdaptor.instance().resetChannels();
	}
	

}
