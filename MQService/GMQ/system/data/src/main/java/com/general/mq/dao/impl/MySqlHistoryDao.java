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
import com.general.mq.dao.HistoryDao;
import com.general.mq.dao.model.History;
import com.general.mq.data.factory.DaoFactory;
import com.general.mq.data.factory.MySqlDaoFactory;

public class MySqlHistoryDao implements HistoryDao {

	PreparedStatement historyPstmt = null;
	ResultSet rSet = null;

	@Override
	public void saveHistory(History history) throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		try {
			conn.setAutoCommit(false);
			if(history!=null){
				historyPstmt = conn.prepareStatement(SQLConfig.SAVE_MSG_HISTORY);
				historyPstmt.setString(1, history.getQueueName());
				historyPstmt.setString(2, history.getClientId());
				historyPstmt.setString(3, history.getMessage());
				historyPstmt.setString(4, history.getMsgAttrName());
				historyPstmt.setString(5, history.getMsgAttrValue());
				historyPstmt.setInt(6, history.getStatus());
				historyPstmt.setString(7, history.getParentId());
				historyPstmt.setTimestamp(8, history.getLoggingTime());
				historyPstmt.setString(9, history.getReason());
				historyPstmt.executeUpdate();
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
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(historyPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}

	}

	@Override
	public void saveBatchHistory(List<History> histories)throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		try {
			conn.setAutoCommit(false);
			if(!histories.isEmpty()){
				historyPstmt = conn.prepareStatement(SQLConfig.SAVE_MSG_HISTORY);
				for(History history:histories){
					historyPstmt.setString(1, history.getQueueName());
					historyPstmt.setString(2, history.getClientId());
					historyPstmt.setString(3, history.getMessage());
					historyPstmt.setString(4, history.getMsgAttrName());
					historyPstmt.setString(5, history.getMsgAttrValue());
					historyPstmt.setInt(6, history.getStatus());
					historyPstmt.setString(7, history.getParentId());
					historyPstmt.setTimestamp(8, history.getLoggingTime());
					historyPstmt.setString(9, history.getReason());
					historyPstmt.addBatch();
				}
				historyPstmt.executeBatch();
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
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(historyPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}
	}

	@Override
	public List<History> findAllHistory(int start,int limit)  throws ApplicationException {
		final Connection conn = DaoFactory.getDAOFactory().createConnection();
		List<History> histories = null;
		History history=null;
		try {
			historyPstmt = conn.prepareStatement(SQLConfig.LOAD_ALL_MSG_HISTORY);
			historyPstmt.setInt(1, start);
			historyPstmt.setInt(2, limit);

			rSet=historyPstmt.executeQuery();
			if(rSet!=null){
				histories=new ArrayList<History>();
				while(rSet.next()){
					history=new History();					
					history.setId(rSet.getInt(TableColumnConfig.ID));
					history.setQueueName(rSet.getString(TableColumnConfig.QUEUE_NAME));
					history.setClientId(rSet.getString(TableColumnConfig.CLIENT_ID));
					history.setMessage(rSet.getString(TableColumnConfig.MESSAGE));
					history.setMsgAttrName(rSet.getString(TableColumnConfig.MSG_ATTR_NAME));
					history.setMsgAttrValue(rSet.getString(TableColumnConfig.MSG_ATTR_VALUE));
					history.setStatus(rSet.getInt(TableColumnConfig.STATUS));
					history.setParentId(rSet.getString(TableColumnConfig.PARENT_ID));
					history.setLoggingTime(rSet.getTimestamp(TableColumnConfig.LOGGING_TIME));
					history.setReason(rSet.getString(TableColumnConfig.REMARK));
					histories.add(history);

				}
			}

		} catch (SQLException e) {
			throw new ApplicationException(e,ApplicationCode.DATA_ACCESS);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(historyPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}
		return histories;
	}

	


}
