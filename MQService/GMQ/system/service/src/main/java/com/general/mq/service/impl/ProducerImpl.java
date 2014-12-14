package com.general.mq.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.general.mq.cache.management.RedisServiceProvider;
import com.general.mq.common.error.ValidationCode;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.ValidationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.StringUtils;
import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.common.util.conf.RedisConfig;
import com.general.mq.common.util.conf.StatusConfig;
import com.general.mq.dao.ClientDao;
import com.general.mq.dao.model.Client;
import com.general.mq.dao.transform.ClientTransformer;
import com.general.mq.dao.util.ClientType;
import com.general.mq.data.factory.DaoFactory;
import com.general.mq.dto.ClientDto;
import com.general.mq.dto.QueueDetailDto;
import com.general.mq.management.QueueChannel;
import com.general.mq.management.QueueChannelFactory;
import com.general.mq.rest.rqrsp.ProducerRequest;
import com.general.mq.rest.rqrsp.ProducerResponse;
import com.general.mq.rest.rqrsp.QueueRequest;
import com.general.mq.service.IProducer;
import com.general.mq.service.cache.adaptor.ClientAdaptor;
import com.general.mq.service.cache.adaptor.QueueAdaptor;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

public class ProducerImpl implements IProducer{

	//Transformer's for synchronizing domain & dto
	private final ClientTransformer clientTransformer = new ClientTransformer();
	private final DaoFactory mySqlFactory = DaoFactory.getDAOFactory();
	//Create a Client DAO
	private final ClientDao clientDao = mySqlFactory.getClientDao();
	//QueueImpl to create new queue bind to exchange based on new Key
	private final QueueImpl queueService= new QueueImpl();

	@Override
	public ProducerResponse registerProducer(ProducerRequest prodReq)throws ApplicationException {
		MQLogger.l.info("Entering ProducerImpl.registerProducer");
		ProducerResponse response=new ProducerResponse();
		ClientDto cDto=null;
		//Validate whether Routing Key already exist or not in exchange cache
		boolean keyExist=QueueAdaptor.instance().hasRoutingKey(prodReq.getQueueName(), prodReq.getRoutingKey());
		if(keyExist){
			//Just create Client in table and refresh cache
			cDto=createClient(prodReq);
			response.setClientId(cDto.clientId);
		}else{
			//create a new queue with new Routing Key with existing exchange
			createQueue(prodReq,null);
			//Just create Client in table and refresh cache
			cDto=createClient(prodReq);
			response.setClientId(cDto.clientId);
		}
		MQLogger.l.info("Leaving ProducerImpl.registerProducer");
		return response;
	}

	
	@Override
	public ProducerResponse publishMessage(ProducerRequest prodReq)throws ApplicationException {
		MQLogger.l.info("Entering ProducerImpl.publishMessage");
		ProducerResponse response =new ProducerResponse();
		// Validate ProducerId in cache, then fetch ExchangeName and Routing Key.Then publish in Queue.
		ClientDto producerDto=ClientAdaptor.instance().getClientbyId(prodReq.getClientId());
		if(producerDto!=null && producerDto.clientType==ClientType.Producer){
			//Check if ParentId exist in request or not
			if(prodReq.getParentId()!=null){
				//Prepare message and publish
				prepareNsendMsg(producerDto,prodReq,true);

			}else{
				prepareNsendMsg(producerDto,prodReq,false);

			}
			response.status=StatusConfig.PRODUCER_PUBLISH;
		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.CLIENT_ID, StatusConfig.CLIENT_NOT_PRODUCER);
		}
		MQLogger.l.info("Leaving ProducerImpl.publishMessage");
		return response;
	}


	private void createQueue(ProducerRequest prodReq,QueueDetailDto queueDto) throws ApplicationException{
		MQLogger.l.info("Entering ProducerImpl.createQueue");
		QueueRequest queueReq=new QueueRequest();
		QueueDetailDto qDto;
		if(queueDto!=null){
			qDto=queueDto;
		}else {

			qDto=QueueAdaptor.instance().findExchangeByName(prodReq.getQueueName());
		}

		if(prodReq.getRoutingKey()!=null){
			qDto.setRoutingKey(prodReq.getRoutingKey());
		}else{
			qDto.setRoutingKey(MQConfig.DEFAULT_ROUTINGKEY);	
		}
		queueReq.setQueueDetail(qDto);
		queueService.createQueue(queueReq);
		MQLogger.l.info("Leaving ProducerImpl.createQueue");
	}

	private ClientDto createClient(ProducerRequest prodReq) throws ApplicationException{
		MQLogger.l.info("Entering ProducerImpl.createClient");
		QueueDetailDto qDto=QueueAdaptor.instance().findQueueByName(StringUtils.getQueueName(prodReq.getRoutingKey(),prodReq.getQueueName()));
		Client clientDomain=new Client();
		ClientDto cDto=new ClientDto();
		cDto.clientId=StringUtils.generateClientID();
		cDto.queueId=qDto.queueId;
		cDto.clientType=ClientType.Producer;
		cDto.routingKey=prodReq.getRoutingKey();
		clientTransformer.syncToDomain(cDto, clientDomain);

		clientDao.createClient(clientDomain);	
		ClientAdaptor.instance().refreshClients(cDto);
		MQLogger.l.info("Leaving ProducerImpl.createClient");
		return cDto;
	}

	

	private void prepareNsendMsg(ClientDto producerDto,ProducerRequest prodReq,boolean hasParentId) throws ApplicationException{
		MQLogger.l.info("Entering ProducerImpl.prepareNsendMsg");
		//publish message
		for(String message:prodReq.getMessages()){
			QueueDetailDto qDto=QueueAdaptor.instance().findByQueueId(producerDto.queueId);
			Map<String,String> cacheValue=new HashMap<String, String>();
			cacheValue.put(RedisConfig.MAX_ATTEMPT, String.valueOf(qDto.maxAttmpt));
			cacheValue.put(RedisConfig.MESSAGE,message);
			if(hasParentId){
				cacheValue.put(RedisConfig.PARENT_ID, prodReq.getParentId());	
			}
			//Generate message ID and store in redis with message
			String msgID=StringUtils.generateMsgID();
			MQLogger.l.debug("Publisher: msgID - "+msgID);
			//Prepare Cache key based on separator
			String cacheKey=StringUtils.prepareCacheKey(producerDto.queueId.toString(),msgID);
			MQLogger.l.debug("Publisher: CacheKey - "+cacheKey);
			//Store in Redis Cache
			RedisServiceProvider.instance().storeMessage(cacheKey, cacheValue);
			//Send message ID in queue.
			publishMsgToRMQ(prodReq,qDto.queueName,producerDto.routingKey, msgID, null);
		}
		MQLogger.l.info("Leaving ProducerImpl.prepareNsendMsg");
	}

	public void publishMsgToRMQ(ProducerRequest prodReq, String exchange,String routingKey, String msgID, Integer expire)throws ApplicationException {
		MQLogger.l.info("Entering ProducerImpl.publishMsgToRMQ");
		MQLogger.l.debug("MsgID in publisher: "+msgID);
		QueueChannelFactory channelFactory = QueueChannelFactory.getChannelFactory();
		QueueChannel qChannel = channelFactory.getQueueChannel();
		Channel queCreatChannel = qChannel.getChannel();		
		AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder();
		builder.priority(prodReq.getPriority());
		if(expire!=null){
			builder.expiration(expire.toString());
		}
		if(prodReq.getAttributeName()!=null){

			Map<String,Object> headerMap=new HashMap<String, Object>();
			headerMap.put(RedisConfig.CUSTOM_HEADER,StringUtils.prepareCacheKey(prodReq.getAttributeName(), prodReq.getAttributeValue()));
			builder.headers(headerMap);
		}	
		try {
			queCreatChannel.basicPublish(exchange, routingKey,builder.build() ,msgID.getBytes());
		} catch (IOException e) {
			throw new ApplicationException(e);

		}finally{
			qChannel.closeChannel();
		}
		MQLogger.l.info("Leaving ProducerImpl.publishMsgToRMQ");
	}
}