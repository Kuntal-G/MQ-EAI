package com.general.mq.data.factory;

import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.dao.ClientDao;
import com.general.mq.dao.HistoryDao;
import com.general.mq.dao.MsgChannelDao;
import com.general.mq.dao.QueueDetailDao;
import com.general.mq.dao.util.DatabaseType;

public abstract class DaoFactory implements IDaoFactory{


	/* There will be a method for each DAO that can be created.
	 *  The concrete factories will have to  implement these methods.
	 */
	public abstract QueueDetailDao getQueueDetailDao();
	public abstract ClientDao getClientDao();
	public abstract MsgChannelDao getChannelDao();
	public abstract HistoryDao getHistoryDao();
	

	public static DaoFactory getDAOFactory() {

		switch (MQConfig.DATABASE_TYPE) {
		case DatabaseType.MYSQL: 
			return new MySqlDaoFactory();
		case DatabaseType.OTHER: 
			return new OtherDaoFactory();      
		default           : 
			return new MySqlDaoFactory();
		}
	}
}