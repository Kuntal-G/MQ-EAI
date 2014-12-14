package com.general.mq.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.general.mq.common.error.ApplicationCode;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.util.conf.SQLConfig;
import com.general.mq.common.util.conf.TableColumnConfig;
import com.general.mq.dao.ClientDao;
import com.general.mq.dao.model.Client;
import com.general.mq.data.factory.MySqlDaoFactory;

public class MySqlClientDao implements ClientDao{

	PreparedStatement clientPstmt = null;
	ResultSet rSet = null;

	@Override
	public void createClient(Client client) throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		try {
			conn.setAutoCommit(false);
			if(client!=null){
			clientPstmt = conn.prepareStatement(SQLConfig.CREATE_CLIENT);
			clientPstmt.setString(1, client.getClientId());
			clientPstmt.setInt(2, client.getClientType());
			clientPstmt.setInt(3, client.getQueueId());
			clientPstmt.setInt(4, client.getIsActive());
			clientPstmt.setString(5, client.getRoutingKey());
			clientPstmt.setLong(6, client.getTimeToLive());
			clientPstmt.executeUpdate();
			}
			conn.commit();

		} catch (SQLIntegrityConstraintViolationException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException(e1,ApplicationCode.TRANSACTION_FAILURE);
			}
			throw new ApplicationException(e,ApplicationCode.DATA_INTEGRITY);
		}catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException(e1,ApplicationCode.TRANSACTION_FAILURE);
			}
			throw new ApplicationException(e,ApplicationCode.DATA_PROCESS_FAIL);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(clientPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}

	}




	@Override
	public Client findClient(Client clientId)throws ApplicationException  {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		Client client=null;
		try {
			clientPstmt = conn.prepareStatement(SQLConfig.FIND_CLIENT);
			clientPstmt.setString(1, clientId.getClientId());
			rSet = clientPstmt.executeQuery();
			if(rSet!=null){
				while (rSet.next()) {
					client=new Client();
					client.setClientId(rSet.getString(TableColumnConfig.CLIENT_ID));
					client.setClientType(rSet.getInt(TableColumnConfig.CLIENT_TYPE));
					client.setQueueId(rSet.getInt(TableColumnConfig.QUEUE_ID));
					client.setIsActive(rSet.getInt(TableColumnConfig.IS_ACTIVE));
					client.setRoutingKey(rSet.getString(TableColumnConfig.ROUTING_KEY));
					client.setTimeToLive(rSet.getLong(TableColumnConfig.TIME_TO_LIVE));
				}

				return client;
			}

		} catch (SQLException e) {
			throw new ApplicationException(e,ApplicationCode.DATA_ACCESS);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(clientPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}
		return client;
	}

	@Override
	public List<Client> findAllClient() throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		Client client=null;
		List<Client> clients = null;
		try {
			clientPstmt = conn.prepareStatement(SQLConfig.LOAD_ALL_CLIENT);
			rSet = clientPstmt.executeQuery();
			if(rSet!=null){
				clients=new ArrayList<Client>();
				while (rSet.next()) {
					client=new Client();
					client.setClientId(rSet.getString(TableColumnConfig.CLIENT_ID));
					client.setClientType(rSet.getInt(TableColumnConfig.CLIENT_TYPE));
					client.setQueueId(rSet.getInt(TableColumnConfig.QUEUE_ID));
					client.setIsActive(rSet.getInt(TableColumnConfig.IS_ACTIVE));
					client.setRoutingKey(rSet.getString(TableColumnConfig.ROUTING_KEY));
					client.setTimeToLive(rSet.getLong(TableColumnConfig.TIME_TO_LIVE));
					clients.add(client);
				}

				return clients;
			}

		} catch (SQLException e) {
			throw new ApplicationException(e,ApplicationCode.DATA_ACCESS);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(clientPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}
		return clients;
	}



	

	@Override
	public void updateClient(Client client) throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		try {
			clientPstmt = conn.prepareStatement(SQLConfig.UPDATE_CLIENT);
			clientPstmt.setInt(1, client.getQueueId());
			clientPstmt.setString(2, client.getRoutingKey());
			clientPstmt.setString(3, client.getClientId());
			clientPstmt.executeUpdate();	
			conn.commit();
		} catch (SQLIntegrityConstraintViolationException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException(e1,ApplicationCode.TRANSACTION_FAILURE);
			}
			throw new ApplicationException(e,ApplicationCode.DATA_INTEGRITY);
		}catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException(e1,ApplicationCode.TRANSACTION_FAILURE);
			}
			throw new ApplicationException(e,ApplicationCode.DATA_PROCESS_FAIL);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(clientPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}

	}



}