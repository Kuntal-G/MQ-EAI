package com.general.mq.service;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.rest.rqrsp.ProducerRequest;
import com.general.mq.rest.rqrsp.ProducerResponse;

public interface IProducer {
	
	ProducerResponse registerProducer(ProducerRequest prodReq) throws ApplicationException ;
	ProducerResponse publishMessage(ProducerRequest prodReq) throws ApplicationException ;
	

}
