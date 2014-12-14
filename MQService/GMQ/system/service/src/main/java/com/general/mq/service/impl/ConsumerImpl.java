package com.general.mq.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.general.mq.cache.management.RedisServiceProvider;
import com.general.mq.common.error.ApplicationCode;
import com.general.mq.common.error.SystemCode;
import com.general.mq.common.error.ValidationCode;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.common.exception.ValidationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.DateUtils;
import com.general.mq.common.util.NumberUtils;
import com.general.mq.common.util.StringUtils;
import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.common.util.conf.RedisConfig;
import com.general.mq.common.util.conf.StatusConfig;
import com.general.mq.dao.ClientDao;
import com.general.mq.dao.HistoryDao;
import com.general.mq.dao.MsgChannelDao;
import com.general.mq.dao.impl.MySqlMsgChannelDao;
import com.general.mq.dao.model.Client;
import com.general.mq.dao.model.History;
import com.general.mq.dao.model.MsgChannel;
import com.general.mq.dao.transform.ClientTransformer;
import com.general.mq.dao.transform.HistoryTransformer;
import com.general.mq.dao.transform.MsgChannelTransformer;
import com.general.mq.dao.util.ClientType;
import com.general.mq.data.factory.DaoFactory;
import com.general.mq.dto.ClientDto;
import com.general.mq.dto.HistoryDto;
import com.general.mq.dto.MsgChannelDto;
import com.general.mq.dto.QueueDetailDto;
import com.general.mq.management.QueueChannel;
import com.general.mq.management.QueueChannelFactory;
import com.general.mq.rest.rqrsp.AcknowledgeDetail;
import com.general.mq.rest.rqrsp.Acknowledgement;
import com.general.mq.rest.rqrsp.ConsumerRequest;
import com.general.mq.rest.rqrsp.ConsumerResponse;
import com.general.mq.rest.rqrsp.MessageResponse;
import com.general.mq.rest.rqrsp.ProducerRequest;
import com.general.mq.service.IConsumer;
import com.general.mq.service.cache.adaptor.ChannelAdaptor;
import com.general.mq.service.cache.adaptor.ChannelObjAdaptor;
import com.general.mq.service.cache.adaptor.ClientAdaptor;
import com.general.mq.service.cache.adaptor.QueueAdaptor;
import com.general.mq.service.util.ServiceUtil;
import com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;

public class ConsumerImpl implements IConsumer{

	//Transformer's for synchronizing domain & dto
	private final ClientTransformer clientTransformer = new ClientTransformer();
	private final HistoryTransformer historyTransformer = new HistoryTransformer();
	private final MsgChannelTransformer msgChannelTransformer = new MsgChannelTransformer();

	private final DaoFactory mySqlFactory = DaoFactory.getDAOFactory();
	//Create a Client DAO
	private final ClientDao clientDao = mySqlFactory.getClientDao();
	private final HistoryDao historyDao = mySqlFactory.getHistoryDao();
	private final MsgChannelDao msgChannelDao = mySqlFactory.getChannelDao();

	//RMQ Channel Objects
	private QueueChannel qChannel = null;

	@Override
	public ConsumerResponse registerConsumer(ConsumerRequest consumerReq)throws ApplicationException {
		MQLogger.l.info("Entering ConsumerImpl.registerConsumer");
		ConsumerResponse response=new ConsumerResponse();
		ClientDto cDto=createClient(consumerReq);
		response.setClientId(cDto.clientId);
		MQLogger.l.info("Leaving ConsumerImpl.registerConsumer");
		return response;
	}


	

	@Override
	public ConsumerResponse updateProcessingTime(ConsumerRequest request)throws ApplicationException {
		MQLogger.l.info("Entering ConsumerImpl.updateProcessingTime");
		ConsumerResponse response =new ConsumerResponse();
		String consumerId = request.getClientId();					
		MsgChannelDto activeChannel=ChannelAdaptor.instance().findActiveChannelByClientId(consumerId);
		if(activeChannel!=null){
			String key=StringUtils.prepareCacheKey(activeChannel.channelId,request.getMessageId());
			boolean keyExists=RedisServiceProvider.instance().keyExist(key);
			if(keyExists){
				long ttl=NumberUtils.toMiliSeconds(request.getProcessTime(), request.getTimeUnit());
				RedisServiceProvider.instance().updateTTL(key,ttl);
				// update in msgChannel Table in Database
				if((activeChannel.lastUpdatedTime.getTime()+activeChannel.maxUpdatedTTL) < (System.currentTimeMillis()+request.getProcessTime())){					
					activeChannel.maxUpdatedTTL=request.getProcessTime();
					activeChannel.lastUpdatedTime=new Date();		
					MsgChannel msgChannelDomain = new MsgChannel();
					msgChannelTransformer.syncToDomain(activeChannel, msgChannelDomain);
					msgChannelDao.updateChannel(msgChannelDomain, false, true, true, false);
				}
				response.status=StatusConfig.TTL_UPDATE_SUCCESS;
			}else{
				throw new ApplicationException(ApplicationCode.REDIS_DATA_NOT_FOUND).set(StatusConfig.TTL_ERROR, StatusConfig.TTL_UPDATE_FAIL);
			}		
		}
		MQLogger.l.info("Leaving ConsumerImpl.updateProcessingTime");
		return response;
	}

	@Override
	public ConsumerResponse consumeMessage(String clientId,Integer batchSize)throws ApplicationException {
		MQLogger.l.info("Entering ConsumerImpl.consumeMessage");
		if(batchSize ==null){
			batchSize=1;	
		}
		//Get ClientDto by using clientID in consumerReq.
		ClientDto cDto = ClientAdaptor.instance().getClientbyId(clientId);
		if(cDto!=null && cDto.clientType!=ClientType.Consumer)		{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.CLIENT_ID,StatusConfig.CLIENT_NOT_CONSUMER);
		}
		//Get QueueDTO to get the name of the Queue in rabbitmq.
		QueueDetailDto qDto = QueueAdaptor.instance().findByQueueId(cDto.queueId);

		//ttl to set the ttl in redis.
		long ttl = cDto.timeToLive;
		String channelId;
		String qName = qDto.qName;
		ConsumerResponse conResp = new ConsumerResponse();
		List<MessageResponse> msgList ;
		MsgChannel msgChannelDomain=null;
		QueueChannel qChannelObj=null;

		/**Get msgChannel Dto. if msgChannel Dto is not null i.e. a channel is already
		 * assigned to that particular client. If msgChannel Dto is null or 
		 * msgChanneldto.channel status is stale, create a new channel and make a entry 
		 * in db as well as refresh channel Adaptor.
		 */
		MsgChannelDto msgChannelDto =null;
		boolean cleaned = false;
		//Get the list of channel for a the given clienId.
		List<MsgChannelDto> msgChannelDtoList = ChannelAdaptor.instance().findChannelsByClientId(clientId);
		//if list size > 0 , find the valid channel for the client.
		if(msgChannelDtoList!=null && !msgChannelDtoList.isEmpty()){
			for(int i=0;i<msgChannelDtoList.size();i++){
				msgChannelDto = msgChannelDtoList.get(i);
				if(msgChannelDto.channelStatus!=0){
					break;
				}
			}
		}
		//msgChannelDto is null or channelStatus is stale. Create the new channel.
		if(msgChannelDto==null || msgChannelDto.channelStatus==0){			
			//Generate new Channel ID.
			channelId = StringUtils.generateMsgID();
			qChannelObj = getNewQChannel(cDto,channelId);
			qChannelObj.lock.lock();
		}else{
			channelId = msgChannelDto.channelId;
			msgChannelDomain = new MsgChannel();
			msgChannelDomain.setChannelId(channelId);
			msgChannelDomain.setLastMsgDlvrdTime(new Timestamp(new Date().getTime()));
			try{
				//This lastdlvrTime update will not be updated if channelCleaner 
				//hold the lock. If channel cleaner remove the object the object this will 
				//throw an exception during the update.
				msgChannelDao.updateChannel(msgChannelDomain, false,false,false,true);
				qChannelObj=ChannelObjAdaptor.instance().getChannelObjById(channelId);
			}catch(ApplicationException e){
				MQLogger.l.info("Channel is Deleted By Cleaner: "+e);
				if ( e.getCause() instanceof MySQLTransactionRollbackException) {
					qChannelObj = null;	
					cleaned = true;
					ChannelAdaptor.instance().refreshChannels(msgChannelDto);
				}
			}

			//if channelObj is null in local cache mark it stale and create new channel.
			if(qChannelObj==null){
				if(!cleaned){
					msgChannelDomain = new MsgChannel();
					msgChannelDomain.setChannelId(channelId);
					msgChannelDao.updateChannel(msgChannelDomain,true,false,false,false);
				}
				//Generate new Channel ID.
				channelId = StringUtils.generateMsgID();
				qChannelObj = getNewQChannel(cDto,channelId);
				qChannelObj.lock.lock();
			}else{
				qChannelObj.lock.lock();
			}
		}

		//Get the List of msgs.
		msgList = getMsgListFrmQueue(qChannelObj.getChannel(), batchSize,qName, channelId, ttl,cDto.queueId);
		qChannelObj.lock.unlock();
		conResp.setClientId(clientId);
		conResp.setMsgs(msgList);
		conResp.setMsgCount(msgList.size());
		//Update the last delivered time channel.
		if(!msgList.isEmpty()){
			msgChannelDomain = new MsgChannel();
			MQLogger.l.debug("channleid: "+channelId);
			msgChannelDomain.setChannelId(channelId);
			Date date  = new Date();
			msgChannelDomain.setLastMsgDlvrdTime(DateUtils.utilToSql(date));
			msgChannelDao.updateChannel(msgChannelDomain, false,false,false,true);
			if(msgChannelDto==null){
				msgChannelDto = new MsgChannelDto();				
			}
			msgChannelDto.clientId=clientId;
			ChannelAdaptor.instance().refreshChannels(msgChannelDto);
		}
		MQLogger.l.info("Leaving ConsumerImpl.consumeMessage");
		return conResp;
	}

	//For Handling Positive Acknowledgement
	@Override
	public Acknowledgement successAck(String clientId,AcknowledgeDetail ackDetail, boolean isFailure, List<History> batchHistory)throws ApplicationException {
		MQLogger.l.info("Entering ConsumerImpl.successAck");
		Acknowledgement acknowlegement=new Acknowledgement();
		//Validate the client id
		ClientDto clientDto=ClientAdaptor.instance().getClientbyId(clientId);
		String channelId=null;
		String dtagKey=null;
		if(clientDto!=null){
		channelId = getValidChannelId(clientId, ackDetail.getMessageId());
		if(channelId!=null){
			dtagKey=StringUtils.prepareCacheKey(channelId,ackDetail.getMessageId());
			//Get Channel Object and Send Ack to RMQ	
			sendACKToRMQ(ackDetail.getMessageId(),channelId);
			// Check message has parent Id or not
			String msgId=StringUtils.extractID(ackDetail.getMessageId(), 0);
			chkUpdateParentID(clientDto,msgId,isFailure);
			
			if(isFailure){
				saveHistory(clientDto,msgId,dtagKey,ackDetail.getRemark(),false,isFailure,batchHistory);
			}else{
				saveHistory(clientDto,msgId,dtagKey,ackDetail.getRemark(),null,false,batchHistory);
			}
			
			//Delete Dtag key from redis
			RedisServiceProvider.instance().deleteKey(dtagKey);
		
			//Delete message from redis
			RedisServiceProvider.instance().deleteKey(StringUtils.prepareCacheKey(clientDto.queueId.toString(),msgId));
			acknowlegement.setAckSuccess(true);
			acknowlegement.setMessageId(ackDetail.getMessageId());
			if(isFailure){
				acknowlegement.setStatus(StatusConfig.NACK_ACCEPTED);
			}else{
				acknowlegement.setStatus(StatusConfig.ACK_ACCEPTED);
			}
		}else{
			acknowlegement.setAckSuccess(false);
			acknowlegement.setStatus(StatusConfig.ACK_REJECTED);
		}
		MQLogger.l.info("Leaving ConsumerImpl.successAck");
		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.CLIENT_ERROR,StatusConfig.INVALID_CLIENT_ID);
		}
		return acknowlegement;
		
	}






	/**
	 * To save message processing details in Database
	 * @param clientDto
	 * @param msgId
	 * @param dtagKey
	 * @throws ApplicationException
	 */
	private void saveHistory(ClientDto clientDto, String msgId, String dtagKey,String reason, Boolean isRetry, boolean isFailure, List<History> batchHistory) throws ApplicationException {
		MQLogger.l.info("Entering ConsumerImpl.saveHistory");
		HistoryDto historyDto=new HistoryDto();
		String qName = QueueAdaptor.instance().findByQueueId(clientDto.queueId).qName;
		historyDto.queueName=qName;
		historyDto.clientId=clientDto.clientId;
		List<String> msgProp = RedisServiceProvider.instance().getHashMsg(StringUtils.prepareCacheKey(clientDto.queueId.toString(),msgId), RedisConfig.MESSAGE,RedisConfig.PARENT_ID);
		if(msgProp!=null && !msgProp.isEmpty()){
		historyDto.message = msgProp.get(0);
		historyDto.parentId = msgProp.get(1);
		}
		//Check attributeName and value exist or not
		List<String> msgAttributeList=RedisServiceProvider.instance().getHashMsg(dtagKey,RedisConfig.CUSTOM_ATTRIB);
		if(msgAttributeList!=null && !msgAttributeList.isEmpty() && msgAttributeList.get(0)!=null){
			historyDto.msgAttrName=StringUtils.extractID(msgAttributeList.get(0), 0);
			historyDto.msgAttrValue=StringUtils.extractID(msgAttributeList.get(0), 1);
		}
		if(reason!=null){
			historyDto.remark=reason;			
		}
		if(isFailure && isRetry!=null){
			if(isRetry){
				historyDto.status=StatusConfig.FAILURE_ACK_STATUS;
			}else{
				historyDto.status=StatusConfig.FAILURE_ACK_RTY_STATUS;
			}
				
		}else{
			historyDto.status=StatusConfig.SUCCESS_ACK_STATUS;
		}
		History historyDomain=new History();
		historyTransformer.syncToDomain(historyDto, historyDomain);
		if(batchHistory!=null){
			batchHistory.add(historyDomain);
		}else{
			historyDao.saveHistory(historyDomain);
		}
		MQLogger.l.info("Leaving ConsumerImpl.saveHistory");
	}

	/**
	 * Check and Update ParentId success count in Redis
	 * @param clientDto
	 * @param msgId
	 */
	private void chkUpdateParentID(ClientDto clientDto, String msgId, boolean isFailureInc) {
		MQLogger.l.info("Entering ConsumerImpl.chkUpdateParentID");
		List<String> msgDetail= RedisServiceProvider.instance().getHashMsg(StringUtils.prepareCacheKey(clientDto.queueId.toString(),msgId), RedisConfig.PARENT_ID);
		if(msgDetail!=null && !msgDetail.isEmpty() && msgDetail.get(0)!=null){
			String parentId=msgDetail.get(0);
			// Update parentId success count
			if(isFailureInc){
				RedisServiceProvider.instance().updateParentId(StringUtils.prepareCacheKey(clientDto.queueId.toString(),parentId), false, false, true);
			}else{
				RedisServiceProvider.instance().updateParentId(StringUtils.prepareCacheKey(clientDto.queueId.toString(),parentId), false, true, false);
			}

		}
		MQLogger.l.info("Leaving ConsumerImpl.chkUpdateParentID");
	}

	private String getValidChannelId(String clientId, String msgId) throws ApplicationException{
		MQLogger.l.info("Entering ConsumerImpl.getValidChannelId");
		String channelId=null;
		// Get Channel Id for the Client
		List<MsgChannelDto> channels=ChannelAdaptor.instance().findChannelsByClientId(clientId);
		if(channels!=null && !channels.isEmpty()){
			for(MsgChannelDto channel:channels){
				//Validate if Key Exist in Redis
				boolean keyExists=RedisServiceProvider.instance().keyExist(StringUtils.prepareCacheKey(channel.channelId,msgId));
				if(keyExists){
					channelId=channel.channelId;
					break;
				}		
			}
		}
		MQLogger.l.info("Leaving ConsumerImpl.getValidChannelId");
		return channelId;
	}

	/**
	 * Sending success ack to RMQ
	 * @param messageId
	 * @param channelId
	 * @throws ApplicationException
	 */
	private void sendACKToRMQ(String messageId, String channelId) throws ApplicationException {
		MQLogger.l.info("Entering ConsumerImpl.sendACKToRMQ");
		QueueChannel qChannelObj=ChannelObjAdaptor.instance().getChannelObjById(channelId);
		if(qChannelObj==null){
			throw new ApplicationException(SystemCode.RESOURCE_NOT_FOUND);
		}else{
			qChannelObj.lock.lock();
			Channel channelObj = qChannelObj.getChannel();

			String dTag=StringUtils.extractID(messageId, 1);
			try {
				channelObj.basicAck(Long.parseLong(dTag), false);

			} catch (NumberFormatException e) {
				throw new ApplicationException(e,ValidationCode.INVALID_NUMBER_FORMAT);
			}catch(IOException e){
				throw new ApplicationException(e,SystemCode.RESOURCE_NOT_FOUND).set(StatusConfig.TTL_ERROR,StatusConfig.TTL_UPDATE_REJECT);
			}finally{
				qChannelObj.lock.unlock();
			}
		}
		MQLogger.l.info("Leaving ConsumerImpl.sendACKToRMQ");
	}


	/**
	 * Sending  Nack to RMQ
	 * @param clientId
	 * @param ackDetail
	 * @throws ApplicationException
	 */
	@Override
	public Acknowledgement failureAck(String clientId,AcknowledgeDetail ackDetail, List<History> batchHistory)throws ApplicationException {
		MQLogger.l.info("Entering ConsumerImpl.failureAck");
		boolean isRetriable = ackDetail.getRetriable();
		Acknowledgement acknw = null;

		if(isRetriable){
			ClientDto cDto = ClientAdaptor.instance().getClientbyId(clientId);
			if(cDto==null){
				throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.CLIENT_ERROR,StatusConfig.INVALID_CLIENT_ID);
			}
			QueueDetailDto qDto = QueueAdaptor.instance().findByQueueId(cDto.queueId);
			int qId = cDto.queueId;
			String attmptStr = getMsgProperties(qId, StringUtils.extractID(ackDetail.getMessageId(),0),true,false,false);
			int attmpt=-1;
			if(attmptStr!=null){
				attmpt = Integer.parseInt(attmptStr);
			}else{
				throw new ApplicationException(StatusConfig.MSG_ATTMPT_NOT_FOUND,ApplicationCode.REDIS_DATA_NOT_FOUND);
			}
			//If attmpt=0, send Ack to RMQ and save in history.
			if(attmpt==0){
				ackDetail.setAckStatus(1);
				acknw = successAck(clientId, ackDetail,true,batchHistory);
			}else{
				//Get the valid channelId to get the current msgID.
				String channelId = getValidChannelId(clientId, ackDetail.getMessageId());
				System.out.println("Redis KEy: "+channelId);
				if(channelId==null){
					throw new ApplicationException(SystemCode.RESOURCE_NOT_FOUND).set(StatusConfig.TTL_ERROR,StatusConfig.TTL_UPDATE_REJECT);
				}
				String redisDTadKey = StringUtils.prepareCacheKey(channelId,ackDetail.getMessageId());
				//To track every failure, this will be dump into db.
				saveHistory(cDto,StringUtils.extractID(ackDetail.getMessageId(), 0),redisDTadKey,ackDetail.getRemark(),true,true,batchHistory);
				List<String> dtagProps = RedisServiceProvider.instance().getHashMsg(redisDTadKey,RedisConfig.PRIORITY,RedisConfig.CUSTOM_ATTRIB);
				//Calculate new priority.
				int recalPriority = 0;
				if(dtagProps!=null && dtagProps.get(0)!=null){
					recalPriority = ServiceUtil.getRecalculatePriority(qId,Integer.parseInt(dtagProps.get(0)));
				}else{
					throw new SystemException(SystemCode.RESOURCE_NOT_FOUND).set(StatusConfig.TTL_ERROR,StatusConfig.TTL_UPDATE_REJECT);
				}
				//Send Ack to RMQ
				sendACKToRMQ(ackDetail.getMessageId(), channelId);
				//Delete Dtag key from redis
				RedisServiceProvider.instance().deleteKey(redisDTadKey);
				//Does delay required?
				long delay = qDto.nxtAttmptDly;
				//Delay!=0 publish in Original Queue else publish in delay Queue.
				if(delay!=0){
					//If there is new requested delay.. set the new ttl for msg in delay Queue.
					Integer reqDelay = ackDetail.getNextAttmptDelay();
					ProducerRequest prodReq = prepareProdReq(dtagProps, recalPriority);
					//Publish in delay Queue.
					String queueName = StringUtils.getQueueName(cDto.routingKey,qDto.queueName);
					String delayQName = StringUtils.getDelayQueueName(queueName);
					String msgId = StringUtils.extractID(ackDetail.getMessageId(),0);
					ProducerImpl prodImpl = new ProducerImpl();
					//For the delay Qname and ExchangeName is same. If reqDelay
					//is null. TTL per msg will not be set otherwise it wil.
					prodImpl.publishMsgToRMQ(prodReq, delayQName,"", msgId, reqDelay);
				}else{
					//Publis to the original Queue.					
					ProducerRequest prodReq = prepareProdReq(dtagProps, recalPriority);
					//Parse the msgID from ackDetail.
					String msgId = StringUtils.extractID(ackDetail.getMessageId(),0);
					ProducerImpl prodImpl = new ProducerImpl();
					prodImpl.publishMsgToRMQ(prodReq, qDto.queueName,cDto.routingKey, msgId, null);
				}
			}
			acknw = new Acknowledgement();
			acknw.setNackSuccess(true);
			acknw.setMessageId(ackDetail.getMessageId());
			acknw.setStatus(StatusConfig.NACK_ACCEPTED);

		}else{
			ackDetail.setAckStatus(1);
			acknw = successAck(clientId, ackDetail,true,batchHistory);			
		}
		MQLogger.l.info("Leaving ConsumerImpl.failureAck");
		return acknw;
	}

	private ProducerRequest prepareProdReq(List<String> dtagProps, int recalPriority){
		MQLogger.l.info("Entering ConsumerImpl.prepareProdReq");
		ProducerRequest prodReq = new ProducerRequest();
		prodReq.setPriority(recalPriority);
		//Get the custom Attrib
		if(dtagProps.get(1)!=null){
			String[] customAttrib = StringUtils.getStrings(dtagProps.get(1),MQConfig.CACHE_KEY_SEPARATOR);
			prodReq.setAttributeName(customAttrib[0]);
			prodReq.setAttributeValue(customAttrib[1]);
		}
		MQLogger.l.info("Leaving ConsumerImpl.prepareProdReq");
		return prodReq;
	}

	private ClientDto createClient(ConsumerRequest consumerReq) throws ApplicationException{
		MQLogger.l.info("Entering ConsumerImpl.createClient");
		//QueueAdaptor keeps the records preloaded from db. After making some
		//changes this must be refreshed.
		QueueDetailDto qDto=QueueAdaptor.instance().findQueueByName(StringUtils.getQueueName(consumerReq.getRoutingKey(), consumerReq.getQueueName()));
		ClientDto cDto=null;
		if(qDto!=null){			
			cDto=new ClientDto();
			cDto.clientId=StringUtils.generateClientID();
			cDto.queueId=qDto.queueId;
			cDto.clientType=ClientType.Consumer;
			cDto.timeUnit=consumerReq.getTimeUnit();
			if(consumerReq.getRoutingKey()!=null){
				cDto.routingKey= consumerReq.getRoutingKey();
			}
			if(consumerReq.getProcessTime()!=0){
				cDto.timeToLive = consumerReq.getProcessTime();
			}
			Client clientDomain=new Client();
			clientTransformer.syncToDomain(cDto, clientDomain);
			clientDao.createClient(clientDomain);	
			ClientAdaptor.instance().refreshClients(cDto);
		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.RK_NOT_EXIST);
		}
		MQLogger.l.info("Leaving ConsumerImpl.createClient");
		return cDto;	

	}

	private QueueChannel getNewQChannel(ClientDto cDto,String channelId) throws ApplicationException{
		MQLogger.l.info("Entering ConsumerImpl.getNewQChannel");
		QueueChannelFactory channelFactory = QueueChannelFactory.getChannelFactory();
		qChannel = channelFactory.getQueueChannel();
		//Prepare MsgChannelDto.
		MsgChannelDto msgChnlDto = new MsgChannelDto();
		msgChnlDto.channelId = channelId;
		msgChnlDto.clientId = cDto.clientId;
		msgChnlDto.maxUpdatedTTL = cDto.timeToLive;
		//Get MsgChannelDomain and sync it to dto.
		MsgChannel msgChannelDomain = new MsgChannel();
		msgChannelTransformer.syncToDomain(msgChnlDto, msgChannelDomain);
		//Create msgChannelDao to make entry in DB.
		MySqlMsgChannelDao msgChannelDao = new MySqlMsgChannelDao();
		msgChannelDao.createChannel(msgChannelDomain);
		//Refresh Channel Adaptor.
		ChannelAdaptor.instance().refreshChannels(msgChnlDto);
		//Store the newly created object in cache for ACk or Nack p
		ChannelObjAdaptor.instance().storeChannelObj(channelId, qChannel);
		MQLogger.l.info("Leaving ConsumerImpl.getNewQChannel");
		return qChannel;
	}

	private List<MessageResponse> getMsgListFrmQueue(Channel channel,int batchSize, String qName, String channelId,long ttl, int queueId) throws ApplicationException{
		MQLogger.l.info("Entering ConsumerImpl.getMsgListFrmQueue");
		List<MessageResponse> msgList = new ArrayList<MessageResponse>();
		try {
			for(int i = 1; i<=batchSize;i++){
				GetResponse resp = channel.basicGet(qName, false);
				//Get the custom Attribute's key and value.
				String customAttrib=null;
				if(resp!=null && resp.getMessageCount()>=0){
					Map<String,Object> headerObj = resp.getProps().getHeaders();
					//headerObj.
					if(headerObj!=null && headerObj.containsKey(RedisConfig.CUSTOM_HEADER)){
						customAttrib = String.valueOf(headerObj.get(RedisConfig.CUSTOM_HEADER));
					}
					int priority = resp.getProps().getPriority();
					//This msgId id being null sometimes.
					String msgId = new String(resp.getBody());
					MQLogger.l.info("resp.getBody(): "+msgId);
					long dTag = resp.getEnvelope().getDeliveryTag();
					MQLogger.l.info("dTag: "+dTag);
					String qIDMsgIdKey = StringUtils.prepareCacheKey(Integer.toString(queueId),msgId);
					MQLogger.l.debug("qID_MsgId_Key: "+qIDMsgIdKey);
					List<String> msgProperties = RedisServiceProvider.instance().getHashMsg(qIDMsgIdKey,RedisConfig.MAX_ATTEMPT,RedisConfig.PARENT_ID,RedisConfig.MESSAGE);
					MessageResponse msgResp = new MessageResponse();
					String msgIdDtag = StringUtils.prepareMsgId(msgId, Long.toString(dTag));
					MQLogger.l.debug("msgId_Dtag: "+msgIdDtag);
					msgResp.setMsgid(msgIdDtag);
					msgResp.setMsg(msgProperties.get(2));
					//Add msg in the list.
					msgList.add(msgResp);
					//if ParentId is not null and not empty.
					if(msgProperties.get(1)!=null && !msgProperties.get(1).isEmpty()){
						//Check wether key exist in redis or not? If yes, just updateParentID key, else insert new entry.
						String qIdParentIdKey = StringUtils.prepareCacheKey(Integer.toString(queueId),msgProperties.get(1));
						boolean doesKeyExist = RedisServiceProvider.instance().keyExist(qIdParentIdKey);
						if(doesKeyExist){
							RedisServiceProvider.instance().updateParentId(qIdParentIdKey,true,false,false);
						}else{
							Map<String,String> processTrack = new HashMap<String, String>();
							processTrack.put(RedisConfig.PROCCESSED_COUNT,"1");
							processTrack.put(RedisConfig.SUCCESS_COUNT, "0");
							processTrack.put(RedisConfig.FAILURE_COUNT, "0");
							RedisServiceProvider.instance().storeParentId(qIdParentIdKey, processTrack);

						}
					}
					//Now store the channelId:msgid:dtag in redis. This is store for redis
					//timeout scenario.
					String chnmsgdTagKey = StringUtils.prepareCacheKey(channelId,msgId,Long.toString(dTag));
					MQLogger.l.debug("chn_msg_dTag_Key: "+chnmsgdTagKey);
					RedisServiceProvider.instance().storeDTAG(chnmsgdTagKey,(int)ttl,priority,customAttrib);
					//Decrease the max-attempt.
					RedisServiceProvider.instance().updateMessage(qIDMsgIdKey,RedisConfig.MAX_ATTEMPT, -1);
					//if in Queue no more messages are available break the loop.
					if(resp.getMessageCount()==0){
						break;
					}
				}else{						
					break;
				}
			}
		} catch (IOException e) {
			throw new ApplicationException(e,ApplicationCode.RMQ_DATA_ACCESS_FAIL);

		}
		MQLogger.l.info("Leaving ConsumerImpl.getMsgListFrmQueue");
		return msgList;
	}

	private String getMsgProperties(int queueId, String msgId, boolean isAttemptReq, boolean isParentReq, boolean isMsgReq){
		MQLogger.l.info("Entering ConsumerImpl.getMsgProperties");
		String qIDMsgIdKey = StringUtils.prepareCacheKey(Integer.toString(queueId),msgId);
		List<String> msgProperties = RedisServiceProvider.instance().getHashMsg(qIDMsgIdKey,RedisConfig.MAX_ATTEMPT,RedisConfig.PARENT_ID,RedisConfig.MESSAGE);
		String value=null;
		if(isAttemptReq){
			value = msgProperties.get(0);
		}else if(isParentReq){
			value = msgProperties.get(1);
		}else{
			value = msgProperties.get(2);
		}
		MQLogger.l.info("Leaving ConsumerImpl.getMsgProperties");
		return value;
	}

	
}
