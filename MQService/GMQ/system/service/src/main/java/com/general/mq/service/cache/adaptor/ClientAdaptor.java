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
import com.general.mq.dao.ClientDao;
import com.general.mq.dao.model.Client;
import com.general.mq.dao.transform.ClientTransformer;
import com.general.mq.data.factory.DaoFactory;
import com.general.mq.dto.ClientDto;

public class ClientAdaptor {
	
	private static final ClientAdaptor INSTANCE = new ClientAdaptor();
	private final Map<String, ClientDto> clientMap;
	private final ReadWriteLock clientLock = new ReentrantReadWriteLock();
	private final ClientTransformer clientTransformer = new ClientTransformer();
	DaoFactory mySqlFactory = DaoFactory.getDAOFactory();

	// Create a Client DAO
	ClientDao clientDao = mySqlFactory.getClientDao();

	private ClientAdaptor() {
		clientMap = new ConcurrentHashMap<String, ClientDto>(16, 0.90f, 1);
	}

	public static ClientAdaptor instance() {
		return INSTANCE;
	}

	
	public List<ClientDto> getAllClient() throws ApplicationException {
		MQLogger.l.debug("Entering  ClientAdaptor.getAllClient");

		List<ClientDto> clients = new ArrayList<ClientDto>();
		try {
			clientLock.readLock().lock();
			if (clientMap.isEmpty()) {
				findAllClients();
			}
		} finally {
			clientLock.readLock().unlock();
		}
		Set<Entry<String, ClientDto>> entrySet = clientMap.entrySet();
		for (Entry<String, ClientDto> entry : entrySet) {
			ClientDto value = entry.getValue();
			clients.add(value);
		}
		MQLogger.l.debug("Leaving  ClientAdaptor.getAllClient with list size: "+clients.size());
		return clients;
	}

	private void findAllClients() throws ApplicationException {
		MQLogger.l.debug("Entering  ClientAdaptor.findAllClients");
		List<ClientDto> dtos = new ArrayList<ClientDto>();
		List<Client>domains=clientDao.findAllClient();
		clientTransformer.syncToDto(domains, dtos);
		indexClients(dtos);
		MQLogger.l.debug("Leaving  ClientAdaptor.findAllClients");
	}

	private void indexClients(List<ClientDto> clients) {
		MQLogger.l.debug("Entering  ClientAdaptor.indexClients");
		for (ClientDto client : clients) {
			if (client.clientId!= null) {
				clientMap.put(client.clientId, client);
			}
		}
		MQLogger.l.debug("Leaving  ClientAdaptor.indexClients");
	}


	public void refreshClients(final ClientDto client) throws ApplicationException {
		MQLogger.l.debug("Entering  ClientAdaptor.refreshClients with ClientID: "+client.clientId);
		try {
			clientLock.readLock().lock();
			if (clientMap.isEmpty()) {
				findAllClients();
			}else if(clientMap.containsKey(client.clientId)){
				clientMap.remove(client.clientId);
				updateClients(client);
			}else{
				updateClients(client);
			}
		} finally {
			clientLock.readLock().unlock();
		}
		MQLogger.l.debug("Leaving  ClientAdaptor.refreshClients");
	}
	
	
	public void deleteClient(final String clientId) throws ApplicationException {
		MQLogger.l.debug("Entering  ClientAdaptor.deleteClient with Client ID:  "+clientId);
		try {
			clientLock.readLock().lock();
			if (clientMap.isEmpty()) {
				findAllClients();
			}else if(clientMap.containsKey(clientId)){
				clientMap.remove(clientId);
			}
		}finally {
			clientLock.readLock().unlock();
		}
		MQLogger.l.debug("Leaving  ClientAdaptor.deleteClient");
	}
	
	


	private void updateClients(final ClientDto dto) throws ApplicationException {
		MQLogger.l.debug("Entering  ClientAdaptor.updateClients ");
		Client domain= new Client();
		ClientDto clientDto= new ClientDto();
		clientTransformer.syncToDomain(dto, domain);
		Client client=clientDao.findClient(domain);
		clientTransformer.syncToDto(client, clientDto);
		if (clientDto.clientId!=null) {
			clientMap.put(clientDto.clientId, clientDto);
		}
		MQLogger.l.debug("Leaving  ClientAdaptor.updateClients ");

	}
	
	public ClientDto getClientbyId(final String clientId){
		MQLogger.l.debug("Entering  ClientAdaptor.getClientbyId with ClientID: "+clientId);
		ClientDto client = null ;
		if (clientMap.containsKey(clientId)) {
			client= clientMap.get(clientId);
			}
		MQLogger.l.debug("Leaving  ClientAdaptor.getClientbyId");
		return client;
		
		
	}
	
	public void resetClients() throws ApplicationException{
		try{
		clientLock.readLock().lock();
		findAllClients();
		}finally{
		clientLock.readLock().unlock();
		}
	}


}
