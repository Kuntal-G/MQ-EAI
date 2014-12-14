package com.general.mq.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.general.mq.common.error.ApplicationCode;
import com.general.mq.common.error.ValidationCode;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.ValidationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.NumberUtils;
import com.general.mq.common.util.StringUtils;
import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.common.util.conf.RabbitConfig;
import com.general.mq.common.util.conf.StatusConfig;
import com.general.mq.dao.QueueDetailDao;
import com.general.mq.dao.model.QueueDetail;
import com.general.mq.dao.transform.QueueDetailTransformer;
import com.general.mq.data.factory.DaoFactory;
import com.general.mq.dto.ClientDto;
import com.general.mq.dto.QueueDetailDto;
import com.general.mq.management.QueueChannel;
import com.general.mq.management.QueueChannelFactory;
import com.general.mq.rest.rqrsp.QueueRequest;
import com.general.mq.rest.rqrsp.QueueResponse;
import com.general.mq.service.IQueue;
import com.general.mq.service.cache.adaptor.ClientAdaptor;
import com.general.mq.service.cache.adaptor.QueueAdaptor;
import com.general.mq.service.util.ServiceUtil;
import com.rabbitmq.client.Channel;

public class QueueImpl implements IQueue{

	//Transformer's for synchronizing domain & dto
	private final QueueDetailTransformer queueTransformer = new QueueDetailTransformer();

	DaoFactory mySqlFactory = DaoFactory.getDAOFactory();
	//Create a QueueDetail DAO
	QueueDetailDao queueDao = mySqlFactory.getQueueDetailDao();


	@Override
	public QueueResponse createQueue(QueueRequest queueReq)throws ApplicationException {
		MQLogger.l.info("Entering QueueImpl.createQueue");
		QueueResponse response =new QueueResponse();
		QueueDetailDto queueDetail = queueReq.getQueueDetail();
		String ROUTING_KEY=queueReq.getQueueDetail().getRoutingKey();
		String WORK_QUEUE= ServiceUtil.getQueueName(queueReq.getQueueDetail());
		QueueDetailDto qDto  = QueueAdaptor.instance().findQueueByName(WORK_QUEUE);
		if(qDto!=null){
			throw new ApplicationException(ApplicationCode.DATA_INTEGRITY).set(StatusConfig.QUEUE_ERROR,StatusConfig.QUEUE_ALREADY_EXIST);
		}
		String WORK_EXCHANGE = queueDetail.queueName;
		QueueChannelFactory channelFactory = QueueChannelFactory.getChannelFactory();
		QueueChannel qChannel = channelFactory.getQueueChannel();
		Channel queCreatChannel = qChannel.getChannel();

		try {
			queCreatChannel.exchangeDeclare(WORK_EXCHANGE, "direct", true);
			//Max Priority settings on queue
			Map<String,Object> priorityMax=new HashMap<String, Object>();
			priorityMax.put(RabbitConfig.MAX_PRIORITY, MQConfig.MQ_MAX_PRIORITY);
			queCreatChannel.queueDeclare(WORK_QUEUE, true, false, false, priorityMax);
			queCreatChannel.queueBind(WORK_QUEUE, WORK_EXCHANGE,ROUTING_KEY, null);
			long delay = NumberUtils.toMiliSeconds(queueDetail.nxtAttmptDly, queueDetail.getTimeUnit());

			if (delay != 0) {
				String DELAY_QUEUE =StringUtils.getDelayQueueName(WORK_QUEUE);
				String DELAY_EXCHANGE = DELAY_QUEUE;
				Map<String, Object> args = new HashMap<String, Object>();
				args.put(RabbitConfig.X_DEAD_LETTER_EXCHANGE, WORK_EXCHANGE);
				args.put(RabbitConfig.X_MESSAGE_TTL, delay);
				args.put(RabbitConfig.X_DEAD_LETTER_ROUTING_KEY,queueDetail.getRoutingKey());
				queCreatChannel.exchangeDeclare(DELAY_EXCHANGE, RabbitConfig.EXCHANGE_TYPE, true);
				queCreatChannel.queueDeclare(DELAY_QUEUE, true, false, false,args);
				queCreatChannel.queueBind(DELAY_QUEUE, DELAY_EXCHANGE, "", null);

			}
		} catch (IOException e) {
			throw new ApplicationException(e);

		}finally{
			qChannel.closeChannel();
		}
		//Update in DB
		createQueueInDB(queueReq);
		response.status=StatusConfig.QUEUE_CREATE;
		MQLogger.l.info("Leaving QueueImpl.createQueue");
		return response;
	}

	private void createQueueInDB(QueueRequest queueReq) throws ApplicationException {
		MQLogger.l.info("Entering QueueImpl.createQueueInDB");
		QueueDetailDto queueDto=queueReq.getQueueDetail();
		QueueDetail qDomain= new QueueDetail();
		queueTransformer.syncToDomain(queueDto, qDomain);
		queueDao.createQueue(qDomain);
		queueDto.qName=ServiceUtil.getQueueName(queueDto);
		//Reset Queue  Adaptor Cache
		QueueAdaptor.instance().refreshQueues(queueDto);
		QueueAdaptor.instance().refreshExchanges(queueDto);
		MQLogger.l.info("Leaving QueueImpl.createQueueInDB");

	}

	

	@Override
	public QueueResponse loadAllQueue() throws ApplicationException {
		MQLogger.l.info("Entering QueueImpl.loadAllQueue");
		QueueResponse response =new QueueResponse();
		//Load from Queue Adaptor Cache
		List<QueueDetailDto> dtos=QueueAdaptor.instance().loadAllQueues();
		response.setQueueDetails(dtos);
		MQLogger.l.info("Leaving QueueImpl.loadAllQueue");
		return response;
	}

	@Override
	public QueueResponse loadAllExchange() throws ApplicationException {
		MQLogger.l.info("Entering QueueImpl.loadAllExchange");
		QueueResponse response =new QueueResponse();
		//Load from Queue Adaptor Cache
		List<QueueDetailDto> dtos=QueueAdaptor.instance().loadAllExchanges();
		response.setQueueDetails(dtos);
		MQLogger.l.info("Leaving QueueImpl.loadAllExchange");
		return response;
	}

	@Override
	public QueueResponse queueMsgCount(String clientId,String exchangeName,String routingKey)throws ApplicationException {
		MQLogger.l.info("Entering QueueImpl.queueMsgCount");
		QueueResponse response =new QueueResponse();
		String qName=null;
		QueueDetailDto queueDto=null;
		ClientDto cDto;
		//Get queue Details 
		if(clientId!=null){
			cDto=ClientAdaptor.instance().getClientbyId(clientId);
			if(cDto!=null){				
				queueDto=QueueAdaptor.instance().findByQueueId(cDto.queueId);
				qName=queueDto.qName;
			}else{
				throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.CLIENT_ID, StatusConfig.INVALID_CLIENT_ID);
		}			
		}else{
			
			qName=StringUtils.getQueueName(routingKey,exchangeName);
			queueDto=QueueAdaptor.instance().findQueueByName(qName);
		}	
		
		if(queueDto!=null){
			QueueChannelFactory channelFactory = QueueChannelFactory.getChannelFactory();
			QueueChannel qChannel = channelFactory.getQueueChannel();
			Channel channel=qChannel.getChannel();

			try {
				int queuemessageSize=channel.queueDeclarePassive(qName).getMessageCount();
				response.setTotCount(queuemessageSize);				
			} catch (IOException e) {
				throw new ApplicationException(e);
			}finally{
				qChannel.closeChannel();
			}

		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.QUEUE_ERROR, StatusConfig.QUEUE_NOT_EXIST);
		}
		MQLogger.l.info("Leaving QueueImpl.queueMsgCount");
		return response;
	}


	
	@Override
	public QueueResponse getKeysByExchange(String exchangeName)throws ApplicationException {
		MQLogger.l.info("Entering QueueImpl.getKeysByExchange");
		QueueResponse response =new QueueResponse();
		List<QueueDetailDto> dtos=QueueAdaptor.instance().getKeysByExchange(exchangeName);
		response.setQueueDetails(dtos);
		MQLogger.l.info("Leaving QueueImpl.getKeysByExchange");
		return response;
	}
	
	@Override
	public QueueResponse getQueueByName(String queueName, String routingKey) throws ApplicationException {
		MQLogger.l.info("Entering QueueImpl.getQueueByName");
		QueueResponse response =new QueueResponse();
		QueueDetailDto qDto=QueueAdaptor.instance().findQueueByName(StringUtils.getQueueName(routingKey, queueName));
		response.setQueueDetail(qDto);
		MQLogger.l.info("Leaving QueueImpl.getQueueByName");
		return response;
	}

}