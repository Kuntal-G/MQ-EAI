package com.general.mq.monitoring.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.general.mq.common.logger.MQLogger;
import com.general.mq.data.factory.ConnectionManager;
import com.general.mq.monitoring.IHealth;
import com.general.mq.monitoring.MonitorCollector;
import com.general.mq.monitoring.metric.DBMetrics;
import com.jolbox.bonecp.BoneCP;

public class MonitorDatabasePool implements IHealth {
	
private static MonitorDatabasePool singleton = null;
private BoneCP dbConnPool=null;

	
	public static MonitorDatabasePool getInstance() {
		if (singleton!=null){return singleton;}
		synchronized (MonitorDatabasePool.class.getName()) 	{
			if (singleton!=null){return singleton;}
			singleton = new MonitorDatabasePool();
		}
		return singleton;
	}
	



	public MonitorDatabasePool() {
		dbConnPool = ConnectionManager.getConnectionPool();
	}

	@Override
	public MonitorCollector<DBMetrics> collect() {
		MonitorCollector<DBMetrics> collector = new MonitorCollector<DBMetrics>();
		PreparedStatement statement = null;
		ResultSet result = null;
		Connection connection=null;
		if(dbConnPool!=null){
			DBMetrics dbMetrics= new DBMetrics();
			dbMetrics.setName("DATABASE-POOL-INFO");
			dbMetrics.setTot_conn_created(String.valueOf(dbConnPool.getTotalCreatedConnections()));
			dbMetrics.setTot_conn_free(String.valueOf(dbConnPool.getTotalFree()));
			dbMetrics.setTot_conn_leased(String.valueOf(dbConnPool.getTotalLeased()));
			dbMetrics.setConn_wait_time_avg(String.valueOf(dbConnPool.getStatistics().getConnectionWaitTimeAvg()));
			dbMetrics.setStmt_exec_time_avg(String.valueOf(dbConnPool.getStatistics().getStatementExecuteTimeAvg()));
			dbMetrics.setPrep_exec_time_cum(String.valueOf(dbConnPool.getStatistics().getCumulativeStatementPrepareTime()));
			dbMetrics.setStmt_exec_time_cum(String.valueOf(dbConnPool.getStatistics().getCumulativeStatementExecutionTime()));
			//Ping INFO
			try {
				connection =dbConnPool.getConnection();
				statement = connection.prepareStatement("select 1+1 from dual");
				result = statement.executeQuery();
				while (result.next()) {
					dbMetrics.setName("DATABASE-PING-INFO");
					dbMetrics.setPing("SUCCESS");
					collector.addMetrics(dbMetrics);
					break;
				}				
			} catch (Exception e) {
				MQLogger.l.error("Monitor Databse Ping Error: "+e);
			} finally {
			ConnectionManager.closeResultSet(result);
			ConnectionManager.closePreparedStatement(statement);
			ConnectionManager.closeConnection(connection);
			}
			collector.addMetrics(dbMetrics);
		
		}
		return collector;
	}

}
