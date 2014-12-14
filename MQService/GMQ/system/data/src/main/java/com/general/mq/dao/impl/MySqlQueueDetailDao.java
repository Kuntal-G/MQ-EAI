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
import com.general.mq.dao.QueueDetailDao;
import com.general.mq.dao.model.QueueDetail;
import com.general.mq.data.factory.MySqlDaoFactory;

public class MySqlQueueDetailDao implements QueueDetailDao{

	PreparedStatement pStmt = null;
	ResultSet rSet = null;

	@Override
	public void createQueue(QueueDetail queue) throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		try {
			conn.setAutoCommit(false);
			pStmt = conn.prepareStatement(SQLConfig.CREATE_QUEUE);
			pStmt.setString(1, queue.getQueueName());
			pStmt.setString(2, queue.getExchangeName());
			pStmt.setInt(3, queue.getMaxAttmpt());
			pStmt.setLong(4, queue.getNxtAttmptDly());
			pStmt.setInt(5, queue.getMsgPriorityAttmpt());
			pStmt.setString(6, queue.getRoutingKey());
			pStmt.executeUpdate();	
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
		}finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(pStmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}

	}

	

	@Override
	public QueueDetail findQueue(QueueDetail queue) throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		QueueDetail queueDetail=null;
		try {
			pStmt = conn.prepareStatement(SQLConfig.FIND_QUEUE);
			pStmt.setString(1, queue.getQueueName());
			rSet = pStmt.executeQuery();
			if(rSet!=null){
				while (rSet.next()) {
					queueDetail=new QueueDetail();
					queueDetail.setQueueId(rSet.getInt(TableColumnConfig.QUEUE_ID));
					queueDetail.setQueueName(rSet.getString(TableColumnConfig.QUEUE_NAME));
					queueDetail.setExchangeName(rSet.getString(TableColumnConfig.EXCHANGE_NAME));	
					queueDetail.setMaxAttmpt(rSet.getInt(TableColumnConfig.MAX_ATTEMPT));
					queueDetail.setNxtAttmptDly(rSet.getLong(TableColumnConfig.NEXT_ATTEMPT_DELAY));
					queueDetail.setMsgPriorityAttmpt(rSet.getInt(TableColumnConfig.MSG_PRIORITY_ATTEMPT));
					queueDetail.setRoutingKey(rSet.getString(TableColumnConfig.ROUTING_KEY));
				}
				return queueDetail;
			}

		} catch (SQLException e) {
			throw new ApplicationException(e,ApplicationCode.DATA_ACCESS);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(pStmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}
		return queueDetail;
	}

	@Override
	public List<QueueDetail> findAllQueue() throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		QueueDetail queueDetail=null;
		List<QueueDetail> queueDetails = null;
		try {
			pStmt = conn.prepareStatement(SQLConfig.LOAD_ALL_QUEUE);
			rSet = pStmt.executeQuery();
			if(rSet!=null){
				queueDetails=new ArrayList<QueueDetail>();
				while (rSet.next()) {
					queueDetail=new QueueDetail();
					queueDetail.setQueueId(rSet.getInt(TableColumnConfig.QUEUE_ID));
					queueDetail.setQueueName(rSet.getString(TableColumnConfig.QUEUE_NAME));
					queueDetail.setExchangeName(rSet.getString(TableColumnConfig.EXCHANGE_NAME));	
					queueDetail.setMaxAttmpt(rSet.getInt(TableColumnConfig.MAX_ATTEMPT));
					queueDetail.setNxtAttmptDly(rSet.getLong(TableColumnConfig.NEXT_ATTEMPT_DELAY));
					queueDetail.setMsgPriorityAttmpt(rSet.getInt(TableColumnConfig.MSG_PRIORITY_ATTEMPT));
					queueDetail.setRoutingKey(rSet.getString(TableColumnConfig.ROUTING_KEY));
					queueDetails.add(queueDetail);
				}

				return queueDetails;
			}

		} catch (SQLException e) {
			throw new ApplicationException(e,ApplicationCode.DATA_ACCESS);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(pStmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}
		return queueDetails;
	}

	
	@Override
	public List<QueueDetail> findExchange(QueueDetail exchange)	throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		QueueDetail exchangeDetail=null;
		List<QueueDetail> exchangeDetails = null;
		try {
			pStmt = conn.prepareStatement(SQLConfig.FIND_EXCHANGE);
			pStmt.setString(1,exchange.getExchangeName());
			rSet = pStmt.executeQuery();
			if(rSet!=null){
				exchangeDetails=new ArrayList<QueueDetail>();
				while (rSet.next()) {
					exchangeDetail=new QueueDetail();
					exchangeDetail.setQueueId(rSet.getInt(TableColumnConfig.QUEUE_ID));
					exchangeDetail.setQueueName(rSet.getString(TableColumnConfig.QUEUE_NAME));
					exchangeDetail.setExchangeName(rSet.getString(TableColumnConfig.EXCHANGE_NAME));	
					exchangeDetail.setMaxAttmpt(rSet.getInt(TableColumnConfig.MAX_ATTEMPT));
					exchangeDetail.setNxtAttmptDly(rSet.getLong(TableColumnConfig.NEXT_ATTEMPT_DELAY));
					exchangeDetail.setMsgPriorityAttmpt(rSet.getInt(TableColumnConfig.MSG_PRIORITY_ATTEMPT));
					exchangeDetail.setRoutingKey(rSet.getString(TableColumnConfig.ROUTING_KEY));
					exchangeDetails.add(exchangeDetail);
				}

				return exchangeDetails;
			}

		} catch (SQLException e) {
			throw new ApplicationException(e,ApplicationCode.DATA_ACCESS);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(pStmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}
		return exchangeDetails;
	}
		
}
