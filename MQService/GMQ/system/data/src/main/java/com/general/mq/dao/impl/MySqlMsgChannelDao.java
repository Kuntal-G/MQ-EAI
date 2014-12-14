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
import com.general.mq.dao.MsgChannelDao;
import com.general.mq.dao.model.MsgChannel;
import com.general.mq.data.factory.MySqlDaoFactory;
import com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException;

public class MySqlMsgChannelDao implements MsgChannelDao {

	PreparedStatement channelPstmt = null;
	ResultSet rSet = null;

	@Override
	public void createChannel(MsgChannel channel) throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		try {
			conn.setAutoCommit(false);
			if(channel!=null){
				channelPstmt = conn.prepareStatement(SQLConfig.CREATE_CHANNEL);
				channelPstmt.setString(1,channel.getClientId());
				channelPstmt.setString(2,channel.getChannelId());
				channelPstmt.setTimestamp(3,channel.getCreateTime());
				channelPstmt.setInt(4,channel.getChannelStatus());
				channelPstmt.setLong(5,channel.getMaxUpdatedTTL());
				channelPstmt.setTimestamp(6,channel.getLastUpdatedTime());
				channelPstmt.setTimestamp(7,channel.getLastMsgDlvrdTime());
				channelPstmt.executeUpdate();
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
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(channelPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}

	}
	
	
	
	@Override
	public List<MsgChannel> findAllChannels() throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		MsgChannel msgChannel=null;
		List<MsgChannel> msgChannels = null;
		try {
			channelPstmt = conn.prepareStatement(SQLConfig.LOAD_ALL_CHANNEL);
			rSet = channelPstmt.executeQuery();
			if(rSet!=null){
				msgChannels=new ArrayList<MsgChannel>();
				while (rSet.next()) {
					msgChannel=new MsgChannel();
					msgChannel.setClientId(rSet.getString(TableColumnConfig.CLIENT_ID));
					msgChannel.setChannelId(rSet.getString(TableColumnConfig.CHANNEL_ID));
					msgChannel.setCreateTime(rSet.getTimestamp(TableColumnConfig.CREATE_TIME));
					msgChannel.setChannelStatus(rSet.getInt(TableColumnConfig.CHANNEL_STATUS));
					msgChannel.setMaxUpdatedTTL(rSet.getLong(TableColumnConfig.MAX_UPDATED_TTL));
					msgChannel.setLastMsgDlvrdTime(rSet.getTimestamp(TableColumnConfig.LAST_MSG_DLVRD_TIME));
					msgChannel.setLastUpdatedTime(rSet.getTimestamp(TableColumnConfig.LAST_UPDATED_TIME));
					msgChannels.add(msgChannel);
				}

				return msgChannels;
			}

		} catch (SQLException e) {
			throw new ApplicationException(e,ApplicationCode.DATA_ACCESS);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(channelPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}
		return msgChannels;
	}



	

	@Override
	public void updateChannel(MsgChannel channel,boolean channelStatus, boolean maxTTL, boolean lastUpdateTime, boolean lastDelveredTime) throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		try {
			conn.setAutoCommit(false);
			if(channelStatus){
				channelPstmt = conn.prepareStatement(SQLConfig.UPDATE_CHANNEL_STALE);
				channelPstmt.setString(1,channel.getChannelId());
				channelPstmt.executeUpdate();
			}else if(maxTTL || lastUpdateTime){
				if(maxTTL && lastUpdateTime)
				{
					channelPstmt = conn.prepareStatement(SQLConfig.UPDATE_CHANNEL_MAXTTLANDLASTUPDATEDTIME);
					channelPstmt.setLong(1,channel.getMaxUpdatedTTL());
					channelPstmt.setTimestamp(2,channel.getLastUpdatedTime());
					channelPstmt.setString(3, channel.getChannelId());
					channelPstmt.executeUpdate();
					
				}else if(maxTTL){
					channelPstmt = conn.prepareStatement(SQLConfig.UPDATE_CHANNEL_MAXTTL);
					channelPstmt.setLong(1,channel.getMaxUpdatedTTL());
					channelPstmt.setString(2, channel.getChannelId());
					channelPstmt.executeUpdate();
					
				}else{
					channelPstmt = conn.prepareStatement(SQLConfig.UPDATE_CHANNEL_LASTUPDATETIME);
					channelPstmt.setTimestamp(1,channel.getLastMsgDlvrdTime());
					channelPstmt.setString(2, channel.getChannelId());
					}
				
			}else{
				channelPstmt = conn.prepareStatement(SQLConfig.UPDATE_CHANNEL_LASTDELVERDTIME);
				channelPstmt.setTimestamp(1,channel.getLastMsgDlvrdTime());
				channelPstmt.setString(2, channel.getChannelId());
				channelPstmt.executeUpdate();
			}
			conn.commit();

		}catch(MySQLTransactionRollbackException e)
		{
			throw new ApplicationException(e,ApplicationCode.TRANSACTION_FAILURE);
		}
		catch (SQLIntegrityConstraintViolationException e) {
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
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(channelPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}
	}


	

	@Override
	public List<MsgChannel> findAllChannelByFilter(MsgChannel filter)throws ApplicationException  {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		MsgChannel msgChannel=null;
		List<MsgChannel> msgChannels = null;
		try {
			channelPstmt = conn.prepareStatement(SQLConfig.FIND_CHANNEL_BY_FILTER);
			channelPstmt.setString(1,filter.getClientId());
			rSet = channelPstmt.executeQuery();
			if(rSet!=null){
				msgChannels=new ArrayList<MsgChannel>();
				while (rSet.next()) {
					msgChannel=new MsgChannel();
					msgChannel.setClientId(rSet.getString(TableColumnConfig.CLIENT_ID));
					msgChannel.setChannelId(rSet.getString(TableColumnConfig.CHANNEL_ID));
					msgChannel.setCreateTime(rSet.getTimestamp(TableColumnConfig.CREATE_TIME));
					msgChannel.setChannelStatus(rSet.getInt(TableColumnConfig.CHANNEL_STATUS));
					msgChannel.setMaxUpdatedTTL(rSet.getLong(TableColumnConfig.MAX_UPDATED_TTL));
					msgChannel.setLastMsgDlvrdTime(rSet.getTimestamp(TableColumnConfig.LAST_MSG_DLVRD_TIME));
					msgChannel.setLastUpdatedTime(rSet.getTimestamp(TableColumnConfig.LAST_UPDATED_TIME));
					msgChannels.add(msgChannel);
				}

				return msgChannels;
			}

		} catch (SQLException e) {
			throw new ApplicationException(e,ApplicationCode.DATA_ACCESS);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(channelPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}
		return msgChannels;
	}
	
	@Override
	public void deleteChannel(MsgChannel channel) throws ApplicationException {
		// TODO Auto-generated method stub

	}

	@Override
	public MsgChannel findChannel(MsgChannel id)throws ApplicationException  {
		// TODO Auto-generated method stub
		return null;
	}

}
