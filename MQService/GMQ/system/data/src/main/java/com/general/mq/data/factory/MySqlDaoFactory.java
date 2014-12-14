package com.general.mq.data.factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.general.mq.dao.ClientDao;
import com.general.mq.dao.HistoryDao;
import com.general.mq.dao.MsgChannelDao;
import com.general.mq.dao.QueueDetailDao;
import com.general.mq.dao.impl.MySqlClientDao;
import com.general.mq.dao.impl.MySqlHistoryDao;
import com.general.mq.dao.impl.MySqlMsgChannelDao;
import com.general.mq.dao.impl.MySqlQueueDetailDao;

public class MySqlDaoFactory extends DaoFactory {
	

	@Override
	public Connection createConnection()   {
		
		return ConnectionManager.getConnection();
	}
	
	@Override
	public void closeConnection(Connection conn)  {
		ConnectionManager.closeConnection(conn);
		
	}


	@Override
	public void closeStatement(Statement stmt)  {
		ConnectionManager.closeStatement(stmt);
		
	}

	@Override
	public void closePreparedStatement(PreparedStatement pStmt) {
		ConnectionManager.closePreparedStatement(pStmt);
		
	}
	
	@Override
	public void closeResultSet(ResultSet rSet)  {
		ConnectionManager.closeResultSet(rSet);
		
	}

	//Various Dao management
	@Override
	public QueueDetailDao getQueueDetailDao() {
		
		return new MySqlQueueDetailDao();
	}

	@Override
	public ClientDao getClientDao() {
	
		return new MySqlClientDao();
	}

	@Override
	public MsgChannelDao getChannelDao() {
		
		return new MySqlMsgChannelDao();
	}

	@Override
	public HistoryDao getHistoryDao() {
		
		return new MySqlHistoryDao();
	}

	
	
}
