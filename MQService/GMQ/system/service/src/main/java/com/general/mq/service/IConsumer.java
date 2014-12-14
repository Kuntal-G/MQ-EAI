package com.general.mq.service;

import java.util.List;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.dao.model.History;
import com.general.mq.rest.rqrsp.AcknowledgeDetail;
import com.general.mq.rest.rqrsp.Acknowledgement;
import com.general.mq.rest.rqrsp.ConsumerRequest;
import com.general.mq.rest.rqrsp.ConsumerResponse;

public interface IConsumer {
	
	ConsumerResponse registerConsumer(ConsumerRequest consumerReq) throws ApplicationException ;
	ConsumerResponse consumeMessage(String clientId,Integer batchSize) throws ApplicationException ;
	ConsumerResponse updateProcessingTime(ConsumerRequest request)	throws ApplicationException;
	Acknowledgement successAck(String clientId, AcknowledgeDetail ackDetail,boolean isFailure, List<History> batchHistory)throws ApplicationException;
	Acknowledgement failureAck(String clientId, AcknowledgeDetail ackDetail,List<History> batchHistory)throws ApplicationException;
	
}
