package com.general.mq.dao;

import java.util.List;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.dao.model.Client;

public interface ClientDao {
	
	public  void createClient(Client client) throws ApplicationException ;
	public  void updateClient(Client client)throws ApplicationException  ;
    public  Client findClient(Client clientId) throws ApplicationException  ;
    public  List<Client> findAllClient() throws ApplicationException ;
 }
