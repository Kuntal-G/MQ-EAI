package com.general.mq.data.factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.general.mq.dao.ClientDao;
import com.general.mq.dao.HistoryDao;
import com.general.mq.dao.MsgChannelDao;
import com.general.mq.dao.QueueDetailDao;

public class OtherDaoFactory extends DaoFactory {

	@Override
	public Connection createConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeConnection(Connection conn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeStatement(Statement stmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closePreparedStatement(PreparedStatement pStmt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeResultSet(ResultSet rSet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QueueDetailDao getQueueDetailDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDao getClientDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MsgChannelDao getChannelDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HistoryDao getHistoryDao() {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
