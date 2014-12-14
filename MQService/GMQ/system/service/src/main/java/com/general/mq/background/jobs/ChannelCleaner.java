package com.general.mq.background.jobs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.DateUtils;
import com.general.mq.common.util.conf.SQLConfig;
import com.general.mq.common.util.conf.TableColumnConfig;
import com.general.mq.data.factory.MySqlDaoFactory;
import com.general.mq.dto.ClientDto;
import com.general.mq.dto.MsgChannelDto;
import com.general.mq.management.QueueChannel;
import com.general.mq.service.IChannelCleaner;
import com.general.mq.service.cache.adaptor.ChannelAdaptor;
import com.general.mq.service.cache.adaptor.ChannelObjAdaptor;
import com.general.mq.service.cache.adaptor.ClientAdaptor;

public class ChannelCleaner implements IChannelCleaner{

	private PreparedStatement channelPstmt = null;
	private ResultSet rSet = null;
	private static final ChannelCleaner ChnlCleaner = new ChannelCleaner();
	

	private ChannelCleaner(){
		try {
			ClientAdaptor.instance().getAllClient();
			
		} catch (ApplicationException e) {
			MQLogger.l.error("Exception while fetching Channel Cleaner detail", e);
			
		}
	}

	public static ChannelCleaner instance()	{
		return ChnlCleaner;
	}

	public static void startChannelCleaner(final long delay){
		Runnable  startCleaner = new Runnable() {

			@Override
			public void run() {
				ChannelCleaner ob = ChannelCleaner.instance();
				while(true){
					try {
						ob.cleanChannel();
						Thread.sleep(delay);
					} catch (ApplicationException e) {
						MQLogger.l.error("Exception while Channel Cleaner", e);
					} catch (InterruptedException e) {
						MQLogger.l.error("Exception while Channel Cleaner", e);
						
					}
				}
			}
		};
		Thread startCleanerThrd = new Thread(startCleaner);
		startCleanerThrd.start();

		
	}
	@Override
	public void cleanChannel() throws ApplicationException {
		final Connection conn = MySqlDaoFactory.getDAOFactory().createConnection();
		try {
			channelPstmt = conn.prepareStatement(SQLConfig.CHNL_CLN_ALL_CHANNEL ,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			rSet = channelPstmt.executeQuery();
			while(rSet.next()){
				Date lastUpdatedTime = DateUtils.sqlToUtil(rSet.getTimestamp(TableColumnConfig.LAST_UPDATED_TIME));
				long maxUpdatedTTL = rSet.getLong(TableColumnConfig.MAX_UPDATED_TTL);
				long currTime = System.currentTimeMillis();
				if(lastUpdatedTime.getTime()+maxUpdatedTTL<currTime){
					String clientId = rSet.getString(TableColumnConfig.CLIENT_ID);

					ClientDto cDto = ClientAdaptor.instance().getClientbyId(clientId);
					if(cDto!=null){
						long defTTL = cDto.timeToLive;
						long lastDelvrdTime = rSet.getLong(TableColumnConfig.LAST_MSG_DLVRD_TIME);
						long tmp = lastDelvrdTime+defTTL+SQLConfig.BUFFER_TIME_CLEAN;
						currTime = System.currentTimeMillis();
						if(tmp<currTime){
							String chnlId =  rSet.getString(TableColumnConfig.CHANNEL_ID);
							QueueChannel qChannel = ChannelObjAdaptor.instance().getChannelObjById(chnlId);
							if(qChannel!=null){
								qChannel.lock.lock();
								qChannel.closeChannel();
								ChannelObjAdaptor.instance().removeChannelObjById(chnlId);
								qChannel.lock.unlock();
							}
							rSet.deleteRow();
							conn.commit();
							MsgChannelDto msgDto = new MsgChannelDto();
							msgDto.clientId=  clientId;
							ChannelAdaptor.instance().refreshChannels(msgDto);
						}
					}
				}
			}

		}catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			MySqlDaoFactory.getDAOFactory().closeResultSet(rSet);
			MySqlDaoFactory.getDAOFactory().closePreparedStatement(channelPstmt);
			MySqlDaoFactory.getDAOFactory().closeConnection(conn);
		}

	}

	
}
