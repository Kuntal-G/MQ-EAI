package com.general.mq.service.cache.adaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.dao.QueueDetailDao;
import com.general.mq.dao.model.QueueDetail;
import com.general.mq.dao.transform.QueueDetailTransformer;
import com.general.mq.data.factory.DaoFactory;
import com.general.mq.dto.QueueDetailDto;

public class QueueAdaptor {

	private static final QueueAdaptor INSTANCE = new QueueAdaptor();
	private final Map<String, QueueDetailDto> queueMap;
	private final Map<String, List<QueueDetailDto>> exchangeMap;
	private final ReadWriteLock queueLock = new ReentrantReadWriteLock();
	private final QueueDetailTransformer queueTransformer = new QueueDetailTransformer();

	DaoFactory mySqlFactory = DaoFactory.getDAOFactory();
	// Create a QueueDetail DAO
	QueueDetailDao queueDao = mySqlFactory.getQueueDetailDao();

	private QueueAdaptor() {
		queueMap = new ConcurrentHashMap<String, QueueDetailDto>(16, 0.90f, 1);
		exchangeMap=new ConcurrentHashMap<String, List<QueueDetailDto>>(16, 0.90f, 1);
	}

	public static QueueAdaptor instance() {
		return INSTANCE;
	}


	public List<QueueDetailDto> loadAllQueues() throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.loadAllQueues ");
		List<QueueDetailDto> queues = new ArrayList<QueueDetailDto>();
		try {
			queueLock.readLock().lock();
			if (queueMap.isEmpty()) {
				findAllQueues();
			}

		} finally {
			queueLock.readLock().unlock();
		}
		Set<Entry<String, QueueDetailDto>> entrySet = queueMap.entrySet();
		for (Entry<String, QueueDetailDto> entry : entrySet) {
			QueueDetailDto value = entry.getValue();
			queues.add(value);
		}
		MQLogger.l.debug("Leaving QueueAdaptor.loadAllQueues with QueueList: "+queues.isEmpty());
		return queues;
	}

	public List<QueueDetailDto> loadAllExchanges() throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.loadAllExchanges ");
		List<QueueDetailDto> exchanges = new ArrayList<QueueDetailDto>();
		QueueDetailDto dto;
		try {
			queueLock.readLock().lock();
			if(exchangeMap.isEmpty()){
				findAllExchanges();
			}
		} finally {
			queueLock.readLock().unlock();
		}

		Set<String> keySet = exchangeMap.keySet();
		for (String exchange:keySet) {
			dto=new QueueDetailDto();
			dto.queueName=exchange;
			exchanges.add(dto);
		}
		MQLogger.l.debug("Leaving QueueAdaptor.loadAllExchanges with QueueList: "+exchanges.isEmpty());
		return exchanges;
	}


	private void findAllQueues() throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.findAllQueues ");
		List<QueueDetailDto> dtos = new ArrayList<QueueDetailDto>();
		List<QueueDetail>domains=queueDao.findAllQueue();
		queueTransformer.syncToDto(domains, dtos);
		indexQueues(dtos);
		MQLogger.l.debug("Leaving QueueAdaptor.findAllQueues ");
	}


	private void indexQueues(List<QueueDetailDto> queues) {
		MQLogger.l.debug("Entering QueueAdaptor.indexQueues ");
		for (QueueDetailDto queue : queues) {
			if (queue.queueId != null) {
				queueMap.put(queue.qName, queue);
			}
		}
		MQLogger.l.debug("Leaving QueueAdaptor.indexQueues ");
	}

	private void findAllExchanges() throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.findAllExchanges ");
		List<QueueDetailDto> dtos = new ArrayList<QueueDetailDto>();
		List<QueueDetail>domains=queueDao.findAllQueue();
		queueTransformer.syncToDto(domains, dtos);
		indexExchanges(dtos);
		MQLogger.l.debug("Leaving QueueAdaptor.findAllExchanges ");
	}



	public void refreshExchanges(final QueueDetailDto exchange) throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.refreshExchanges with exchangeName:  "+exchange.queueName);
		try {
			queueLock.readLock().lock();
			if (exchangeMap.isEmpty()) {
				findAllExchanges();
			}else if(exchangeMap.containsKey(exchange.queueName)){
				exchangeMap.remove(exchange.queueName);
				updateExchanges(exchange);
			}else{
				updateExchanges(exchange);
			}
		} finally {
			queueLock.readLock().unlock();
		}
		MQLogger.l.debug("Leaving QueueAdaptor.refreshExchanges");
	}


	private void updateExchanges(final QueueDetailDto dto) throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.updateExchanges");
		QueueDetail domain= new QueueDetail();
		List<QueueDetailDto> exchDtos= new ArrayList<QueueDetailDto>();
		queueTransformer.syncToDomain(dto, domain);
		List<QueueDetail> exchanges=queueDao.findExchange(domain);
		queueTransformer.syncToDto(exchanges, exchDtos);
		updateIndexExchanges(exchDtos);
		MQLogger.l.debug("Leaving QueueAdaptor.updateExchanges");

	}

	private void indexExchanges(List<QueueDetailDto> exchanges) {
		MQLogger.l.debug("Entering QueueAdaptor.indexExchanges");
		for (QueueDetailDto exchange : exchanges) {
			if (exchange.queueId!= null) {
				if(exchangeMap.containsKey(exchange.queueName)){					
					List<QueueDetailDto> tempDtoList=new ArrayList<QueueDetailDto>();	
					tempDtoList=exchangeMap.get(exchange.queueName);
					tempDtoList.add(exchange);
					exchangeMap.put(exchange.queueName, tempDtoList);

				}else{
					List<QueueDetailDto> tempDtoList=new ArrayList<QueueDetailDto>();		
					tempDtoList.add(exchange);
					exchangeMap.put(exchange.queueName, tempDtoList);
				}
			}
		}
		MQLogger.l.debug("Leaving QueueAdaptor.indexExchanges");
	}
	
	
	private void updateIndexExchanges(List<QueueDetailDto> exchanges) {
		MQLogger.l.debug("Entering QueueAdaptor.updateIndexExchanges");
		for (QueueDetailDto exchange : exchanges) {
			if (exchange.queueId!= null) {
				if(exchangeMap.containsKey(exchange.queueName)){					
					exchangeMap.remove(exchange.queueName);
					exchangeMap.put(exchange.queueName, exchanges);

				}else{
					exchangeMap.put(exchange.queueName, exchanges);
				}
			}
		}
		MQLogger.l.debug("Leaving QueueAdaptor.updateIndexExchanges");	
	}

	public void refreshQueues(final QueueDetailDto queue) throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.refreshQueues with QueueName: "+queue.qName);	
		try {
			queueLock.readLock().lock();
			if (queueMap.isEmpty()) {
				findAllQueues();
			}else if(queueMap.containsKey(queue.qName)){
				queueMap.remove(queue.qName);
				updateQueues(queue);
			}else{
				updateQueues(queue);
			}
		} finally {
			queueLock.readLock().unlock();
		}
		MQLogger.l.debug("Leaving QueueAdaptor.refreshQueues ");	
	}


	private void updateQueues(final QueueDetailDto dto) throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.updateQueues ");	
		QueueDetail domain= new QueueDetail();
		QueueDetailDto qDto= new QueueDetailDto();
		queueTransformer.syncToDomain(dto, domain);
		QueueDetail queue=queueDao.findQueue(domain);
		queueTransformer.syncToDto(queue, qDto);
		if (qDto.queueId != null) {
			queueMap.put(qDto.qName, qDto);
		}
		MQLogger.l.debug("Leaving QueueAdaptor.updateQueues ");	

	}

	public QueueDetailDto findQueueByName(final String queueName) throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.findQueueByName  with: "+queueName);	
		QueueDetailDto queue = null ;
		if (queueMap.containsKey(queueName)) {
			queue= queueMap.get(queueName);

		}
		MQLogger.l.debug("Leaving QueueAdaptor.findQueueByName");
		return queue;
	}


	public QueueDetailDto findExchangeByName(final String exchangeName) throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.findExchangeByName  with: "+exchangeName);	
		QueueDetailDto queueDto = null ;
		for (Entry<String, QueueDetailDto> entry : queueMap.entrySet()) {
			if (exchangeName.equals(entry.getValue().queueName)) {
				queueDto=entry.getValue();
			}
		}
		MQLogger.l.debug("Leaving QueueAdaptor.findExchangeByName");
		return queueDto;
	}

	public QueueDetailDto findByQueueId(final Integer queueId) throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.findByQueueId with queueID: "+queueId);
		QueueDetailDto queueDto = null ;
		for (Entry<String, QueueDetailDto> entry : queueMap.entrySet()) {
			if (queueId==entry.getValue().queueId) {
				queueDto=entry.getValue();
				
			}
		}
		MQLogger.l.debug("Leaving QueueAdaptor.findByQueueId ");
		return queueDto;
	}



	public List<QueueDetailDto> getKeysByExchange(String exchangeName){
		MQLogger.l.debug("Entering QueueAdaptor.getKeysByExchange with ExchangeName: "+exchangeName);
		List<QueueDetailDto> routingKeys = null ;
		if (exchangeMap.containsKey(exchangeName)) {
			routingKeys= exchangeMap.get(exchangeName);
		}
		MQLogger.l.debug("Leaving QueueAdaptor.getKeysByExchange");
		return routingKeys;
	}

	
	public boolean hasRoutingKey(final String exchangeName,final String routingKey){
		MQLogger.l.debug("Entering QueueAdaptor.hasRoutingKey with ExchangeName: "+exchangeName+" Routing Key: "+routingKey);
		boolean keyExist=false;
		List<QueueDetailDto> routingKeys = null ;
		if (exchangeMap.containsKey(exchangeName)) {
			routingKeys= exchangeMap.get(exchangeName);
			for(QueueDetailDto dto:routingKeys){
				if(dto.getRoutingKey().equalsIgnoreCase(routingKey)){
					keyExist=true;
				}

			}
		}
		MQLogger.l.debug("Leaving QueueAdaptor.hasRoutingKey");
		
		return keyExist;
	}


	public QueueDetailDto findByRoutingKey(final String routingKey) throws ApplicationException {
		MQLogger.l.debug("Entering QueueAdaptor.findByRoutingKey with  Routing Key: "+routingKey);
		
		QueueDetailDto queueDto = null ;
		for (Entry<String, QueueDetailDto> entry : queueMap.entrySet()) {
			if (routingKey.equals(entry.getValue().getRoutingKey())) {
				queueDto=entry.getValue();
			}
		}
		MQLogger.l.debug("Leaving QueueAdaptor.findByRoutingKey");
		
		return queueDto;
	}
	
	public void resetQueuesAndExchanges() throws ApplicationException {
		try {
			queueLock.readLock().lock();
		findAllQueues();
		findAllExchanges();
		}finally{
			queueLock.readLock().unlock();
		}
	}

}

