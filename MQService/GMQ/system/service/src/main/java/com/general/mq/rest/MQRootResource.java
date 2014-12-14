package com.general.mq.rest;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.server.impl.wadl.WadlResource;
import com.sun.jersey.spi.resource.Singleton;

/**
 * An RESTful root resource that facilitates all  MQ API sub-resources. This is helpful for segregating
 * different base URL for different set of RESTful web services.
 * 
 */
@Path("/v1/rest")
@Singleton
public class MQRootResource {

	@Context
	private ResourceContext resourceContext;

	@Path("application.wadl")
	public WadlResource getWadlResource() {
		return resourceContext.getResource(WadlResource.class);
	}
	
	//TODO: To be removed Later
	@Path("hello")
	public HelloResource gethelloResource() {
		return resourceContext.getResource(HelloResource.class);
	}
	
	@Path("queue")
	public QueueResource getQueueResource() {
		return resourceContext.getResource(QueueResource.class);
	}
	
	@Path("producer")
	public ProducerResource getProducerResource() {
		return resourceContext.getResource(ProducerResource.class);
	}

	@Path("consumer")
	public ConsumerResource getConsumerResource() {
		return resourceContext.getResource(ConsumerResource.class);
	}
	
	@Path("monitor")
	public MonitoringResource getMonitoringResource() {
		return resourceContext.getResource(MonitoringResource.class);
	}

	@Path("history")
	public HistoryResource getHistoryResource() {
		return resourceContext.getResource(HistoryResource.class);
	}
	
	@Path("reset")
	public CacheResetResource getCacheResetResource(){
		return resourceContext.getResource(CacheResetResource.class);
	}


}