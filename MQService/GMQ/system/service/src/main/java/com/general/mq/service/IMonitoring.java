package com.general.mq.service;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.rest.rqrsp.DefaultConfigResponse;
import com.general.mq.rest.rqrsp.MonitorResponse;



public interface IMonitoring {
	
	MonitorResponse heartBeat() throws ApplicationException ;
	MonitorResponse systemHealthCheck() throws ApplicationException ;
	MonitorResponse clearConfigCache() throws ApplicationException ;
	MonitorResponse resetLocalCache() throws ApplicationException ;
	DefaultConfigResponse loadDefaultConfig() throws ApplicationException ;
}
