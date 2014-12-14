package com.general.mq.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.general.mq.common.error.ValidationCode;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.common.exception.ValidationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.ValidationUtils;
import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.common.util.conf.StatusConfig;
import com.general.mq.dao.impl.MySqlHistoryDao;
import com.general.mq.dao.model.History;
import com.general.mq.rest.rqrsp.AcknowledgeDetail;
import com.general.mq.rest.rqrsp.AcknowledgeRequest;
import com.general.mq.rest.rqrsp.AcknowledgeResponse;
import com.general.mq.rest.rqrsp.Acknowledgement;
import com.general.mq.rest.rqrsp.ConsumerRequest;
import com.general.mq.rest.rqrsp.ConsumerResponse;
import com.general.mq.service.builder.ResponseBuilder;
import com.general.mq.service.impl.ConsumerImpl;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsumerResource {

	private final ConsumerImpl consumerService= new ConsumerImpl();

	@POST
	@Path("/register")
	public ConsumerResponse registerConsumer(ConsumerRequest request)throws ApplicationException,SystemException {
		MQLogger.l.info("Entering ConsumerResource.registerConsumer");
		MQLogger.l.debug("Capturing ConsumerResource.registerConsumer.InputParams : QueueName="+request.getQueueName()+", RoutingKey="+request.getRoutingKey()+", ProcessingTime="+request.getProcessTime()+", Time Unit="+request.getTimeUnit());
		boolean qName=ValidationUtils.isValidQName(request.getQueueName());
		boolean rKey=ValidationUtils.isValidRoutingKey(request.getRoutingKey());
		boolean tUnit=ValidationUtils.isValidTimeUnit(request.getTimeUnit());
		ConsumerResponse response=null;
		if(qName&&rKey&&tUnit){
			response = consumerService.registerConsumer(request);			
		}			
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving ConsumerResource.registerConsumer");
		return response;
	}

	

	@POST
	@Path("/updateMsgProcessingTime")
	public ConsumerResponse updateMsgProcessingTime(ConsumerRequest request) throws ApplicationException,SystemException{
		MQLogger.l.info("Entering ConsumerResource.updateMsgProcessingTime");
		MQLogger.l.debug("Capturing ConsumerResource.updateMsgProcessingTime.InputParams : Client Id="+request.getClientId()+", Processing Time="+request.getProcessTime()+", Time Unit="+request.getTimeUnit()+", Msg Id="+request.getMessageId());
		ConsumerResponse response=null;
		boolean clientId=ValidationUtils.isValidClientId(request.getClientId());
		boolean tUnit=ValidationUtils.isValidTimeUnit(request.getTimeUnit());
		boolean msgId=ValidationUtils.isValidMsgId(request.getMessageId());
		if(clientId&&tUnit&&msgId){
			response = consumerService.updateProcessingTime(request);
		}			
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving ConsumerResource.updateMsgProcessingTime");
		return response;
	}


	@GET
	@Path("/consume")
	public ConsumerResponse consumeMsg(@QueryParam("clientId") String clientId,@QueryParam("batchSize") Integer  batchSize) throws ApplicationException,SystemException{
		MQLogger.l.info("Entering ConsumerResource.consumeMsg");
		MQLogger.l.debug("Capturing ConsumerResource.consumeMsg.InputParams : Client Id="+clientId+", batch size="+batchSize);
		ConsumerResponse response=null;		
		if(ValidationUtils.isValidClientId(clientId)){
			response = consumerService.consumeMessage(clientId,batchSize);
			if(response.getMsgs()!=null && response.getMsgs().isEmpty()){
				response.status="No msg in RMQ.";
			}
		}			
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving ConsumerResource.consumeMsg");
		return response;

	}


	@POST
	@Path("/acknowledge")
	public AcknowledgeResponse msgAcknowledge(AcknowledgeRequest request)throws ApplicationException,SystemException{
		MQLogger.l.info("Entering ConsumerResource.msgAcknowledge");
		MQLogger.l.debug("Capturing ConsumerResource.msgAcknowledge.InputParams : Client Id="+request.getClientId()+", Ack Status="+request.getAckDetail().get(0).getAckStatus());
		AcknowledgeResponse response=new AcknowledgeResponse();
		List<Acknowledgement> ackProcStatus=new ArrayList<Acknowledgement>();
		List<History> historyList = null;
		if(ValidationUtils.isValidClientId(request.getClientId())){
			if(request.getAckDetail()!=null && !request.getAckDetail().isEmpty()){
				boolean isBatchHistory = false;
				int ackListSize = request.getAckDetail().size();
				int totalAck = 0;
				if(ackListSize>1){
					isBatchHistory = true;
					historyList = new ArrayList<History>();
				}
				for(AcknowledgeDetail ackDetail:request.getAckDetail()){
					try {	
						if(ackDetail.getAckStatus()!=null && ackDetail.getMessageId()!=null){
							if(ackDetail.getAckStatus()==StatusConfig.SUCCESS_ACK_STATUS)	{
								totalAck++;
								Acknowledgement acknowledgement = consumerService.successAck(request.getClientId(), ackDetail,false,historyList);
								ackProcStatus.add(acknowledgement);
								response.status=StatusConfig.ACK_ACCEPTED;
							}else if(ackDetail.getAckStatus()==StatusConfig.FAILURE_ACK_STATUS){
								totalAck++;
								Acknowledgement acknowledgement = consumerService.failureAck(request.getClientId(), ackDetail,historyList);
								ackProcStatus.add(acknowledgement);
								response.status=StatusConfig.NACK_ACCEPTED;
							}else{
								totalAck++;
								response.status=StatusConfig.INVALID_ACK_STATUS;	
							}						
						}else{						
							response=new AcknowledgeResponse();
							response.status=StatusConfig.INVALID_ACK_FORMAT;	
						}
					}catch (ValidationException e) {
						throw e;						
					}catch (ApplicationException e) {
						MQLogger.l.trace("ApplicationException occured"+ e);
						Acknowledgement acknowledgement = ResponseBuilder.buildResponse(e, new Acknowledgement());
						ackProcStatus.add(acknowledgement);
					} catch (SystemException e) {
						MQLogger.l.trace("SystemException occured"+ e);
						Acknowledgement acknowledgement = ResponseBuilder.buildResponse(e, new Acknowledgement());
						ackProcStatus.add(acknowledgement);
					} 
					if(isBatchHistory && ((historyList.size() == MQConfig.MSG_BATCH_SIZE || (totalAck == ackListSize)))){
						MySqlHistoryDao msqlDao = new MySqlHistoryDao();
						msqlDao.saveBatchHistory(historyList);
						historyList.clear();
					}

				}
				if(ackProcStatus!=null){
					response.setAckProcStatus(ackProcStatus);
					response.status ="true";
				}

			}else{
				throw new ValidationException(ValidationCode.MANDATORY_REQUIRED).set("Acknowlegement Details", " are Mandatory");
			}
		}
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving ConsumerResource.msgAcknowledge");
		return response;

	}
	
}
