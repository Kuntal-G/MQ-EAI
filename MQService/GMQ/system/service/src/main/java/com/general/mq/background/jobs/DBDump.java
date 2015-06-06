package com.general.mq.background.jobs;

import java.util.ArrayList;
import java.util.List;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.dao.HistoryDao;
import com.general.mq.dao.model.History;
import com.general.mq.data.factory.DaoFactory;
import com.general.mq.monitoring.QueueDump;

public class DBDump implements Runnable{

	private final DaoFactory mySqlFactory = DaoFactory.getDAOFactory();
	private final HistoryDao historyDao = mySqlFactory.getHistoryDao();


	@Override
	public void run() {

		while(true){
			try {
				if(!QueueDump.accessQueue().isEmpty()){
					List<History> histories=new  ArrayList<History>();
					if(QueueDump.accessQueue().size()>=MQConfig.DUMP_BATCH_SIZE){
						for(int i=0;i<QueueDump.accessQueue().size();i++){
							History history=QueueDump.accessQueue().poll();
							if(history!=null){
							histories.add(history);
							}

						}			
					}else{				
						Thread.sleep(MQConfig.DUMP_THREAD_SLEEP);						
						for(int i=0;i<QueueDump.accessQueue().size();i++){
							History history=QueueDump.accessQueue().poll();
							if(history!=null){
								histories.add(history);
							}

						}	

					}
					historyDao.saveBatchHistory(histories);	

				}
			} catch (InterruptedException e) {
				MQLogger.l.error("Database Dump background thread interrupted",e);
			} catch (ApplicationException dbe) {
				MQLogger.l.error("Database Dump SQL exception: ",dbe);
			}
		}
	}

}