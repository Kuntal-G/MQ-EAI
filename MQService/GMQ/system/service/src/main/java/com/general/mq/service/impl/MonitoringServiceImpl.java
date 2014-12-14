package com.general.mq.service.impl;

import java.util.List;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.conf.ConfigManager;
import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.common.util.conf.StatusConfig;
import com.general.mq.monitoring.impl.MonitorHealth;
import com.general.mq.monitoring.metric.AllMetrics;
import com.general.mq.rest.rqrsp.DefaultConfigResponse;
import com.general.mq.rest.rqrsp.MonitorResponse;
import com.general.mq.service.IMonitoring;
import com.general.mq.service.cache.adaptor.ChannelAdaptor;
import com.general.mq.service.cache.adaptor.ClientAdaptor;
import com.general.mq.service.cache.adaptor.QueueAdaptor;

public class MonitoringServiceImpl implements IMonitoring {

	@Override
	public MonitorResponse heartBeat() throws ApplicationException   {
		MQLogger.l.info("Entering MonitoringServiceImpl.heartBeat");
		MonitorResponse response=new MonitorResponse();
		response.status="OK";
		MQLogger.l.info("Leaving MonitoringServiceImpl.heartBeat");
		return response;
	}

	@Override
	public MonitorResponse systemHealthCheck() throws ApplicationException {
		MQLogger.l.info("Entering MonitoringServiceImpl.systemHealthCheck");
		MonitorResponse response=new MonitorResponse();
		List<AllMetrics> metrics=MonitorHealth.getInstance().healthCheck();
		response.setMetrics(metrics);
		MQLogger.l.info("Leaving MonitoringServiceImpl.systemHealthCheck");
		return response;
	}

	@Override
	public MonitorResponse clearConfigCache() throws ApplicationException {
		MQLogger.l.info("Entering MonitoringServiceImpl.clearConfigCache");
		MonitorResponse response=new MonitorResponse();
		ConfigManager.instance().clearCache();
		response.status=StatusConfig.UPDATE_CONFIG;
		MQLogger.l.info("Leaving MonitoringServiceImpl.clearConfigCache");
		return response;
	}


	@Override
	public MonitorResponse resetLocalCache() throws ApplicationException {
		MQLogger.l.info("Entering MonitoringServiceImpl.resetLocalCache");
		MonitorResponse response=new MonitorResponse();
		ChannelAdaptor.instance().resetChannels();
		ClientAdaptor.instance().resetClients();
		QueueAdaptor.instance().resetQueuesAndExchanges();
		response.status=StatusConfig.UPDATE_LOCAL_CACHE;
		MQLogger.l.info("Leaving MonitoringServiceImpl.resetLocalCache");
		return response;
	}

	@Override
	public DefaultConfigResponse loadDefaultConfig() throws ApplicationException{
		DefaultConfigResponse configResp=new DefaultConfigResponse();
		configResp.processTime=MQConfig.MESSAGE_PROCESSING_TIME;
		configResp.processTimeUnit=MQConfig.MESSAGE_PROCESSING_TIMEUNIT;
		configResp.maxAtmpt=MQConfig.DEFAULT_MAXATTMPT;
		return configResp;
	}

}
