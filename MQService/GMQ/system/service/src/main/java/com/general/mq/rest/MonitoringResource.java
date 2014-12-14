package com.general.mq.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//import com.sun.jersey.spi.resource.Singleton;







import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.rest.rqrsp.DefaultConfigResponse;
import com.general.mq.rest.rqrsp.MonitorResponse;
import com.general.mq.service.builder.ResponseBuilder;
import com.general.mq.service.impl.MonitoringServiceImpl;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MonitoringResource {

	private final MonitoringServiceImpl monitorService= new MonitoringServiceImpl();


	@GET
	@Path("/clearConfigCache")
	public MonitorResponse clearConfigCache()throws ApplicationException,SystemException{
		MQLogger.l.info("Entering MonitoringResource.clearConfigCache");
		MonitorResponse response = monitorService.clearConfigCache();
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving MonitoringResource.clearConfigCache");
		return response;


	}

	@GET
	@Path("/checkHeartBeat")
	public MonitorResponse heartBeat() throws ApplicationException,SystemException{
		MQLogger.l.info("Entering MonitoringResource.heartBeat");
		MonitorResponse response = monitorService.heartBeat();
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving MonitoringResource.heartBeat");
		return response;
	}


	@GET
	@Path("/checkHealth")
	public MonitorResponse systemHealthCheck()throws ApplicationException,SystemException{
		MQLogger.l.info("Entering MonitoringResource.healthCheck");
		MonitorResponse response = monitorService.systemHealthCheck();
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving MonitoringResource.healthCheck");
		return response;
	}
	
	
	@GET
	@Path("/resetLocalCache")
	public MonitorResponse resetLocalCache()throws ApplicationException,SystemException{
		MQLogger.l.info("Entering MonitoringResource.resetLocalCache");
		MonitorResponse response = monitorService.resetLocalCache();
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving MonitoringResource.resetLocalCache");
		return response;


	}
	
	@GET
	@Path("/loadDefaultConfig")
	public DefaultConfigResponse loadDefaultConfig()throws ApplicationException,SystemException{
		MQLogger.l.info("Entering MonitoringResource.loadDefaultConfig");
		DefaultConfigResponse response = monitorService.loadDefaultConfig();
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving MonitoringResource.loadDefaultConfig");
		return response;


	}
	
	
}
