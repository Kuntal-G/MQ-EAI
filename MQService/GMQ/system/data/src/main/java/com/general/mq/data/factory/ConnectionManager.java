package com.general.mq.data.factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.general.mq.common.error.SystemCode;
import com.general.mq.common.exception.SystemException;
import com.general.mq.common.util.conf.MQConfig;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class ConnectionManager {

	private static BoneCP connectionPool = null;

	/**
	 * Configuring Connection pool
	 */
	public static void configureConnPool() {

		try {
			Class.forName(MQConfig.DATABASE_DRIVER);
			// setup the connection pool using BoneCP Configuration
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(MQConfig.DATABASE_URL);
			config.setUsername(MQConfig.DATABASE_USER);
			config.setPassword(MQConfig.DATABASE_PASS);
			config.setMinConnectionsPerPartition(MQConfig.DATABASE_MIN_CONN);
			config.setMaxConnectionsPerPartition(MQConfig.DATABASE_MAX_CONN);
			config.setPartitionCount(MQConfig.DATABASE_MAX_PARTITION);
			config.setDefaultAutoCommit(MQConfig.DATABASE_AUTOCOMMIT);
			// setup the connection pool
			connectionPool = new BoneCP(config);
			ConnectionManager.setConnectionPool(connectionPool);

		} catch (Exception e) {
			throw new SystemException("Creating DB Connection Pool",e,SystemCode.UNABLE_TO_EXECUTE);
		}

	}

	/**
	 * Shutdown Connection pool
	 * This method must be called only,once when the application stops.
	 * Don't need to call it every time when you get a connection from the Connection Pool.
	 */

	public static void shutdownConnPool() {

		try {
			BoneCP connectionPool = ConnectionManager.getConnectionPool();
			if (connectionPool != null) {
				connectionPool.shutdown(); 
			}

		} catch (Exception e) {

			throw new SystemException("Shutting Down DB Connection Pool",e,SystemCode.UNABLE_TO_EXECUTE);	
		}
	}

	/**
	 * Get Connection from Connection pool
	 */
	public static Connection getConnection() {

		Connection conn = null;
		try {
			conn = getConnectionPool().getConnection();
		} catch (Exception e) {

			throw new SystemException("Getting Connection from DB Connection Pool",e,SystemCode.UNABLE_TO_EXECUTE);
		}
		return conn;

	}

	/**
	 * Close Sql statement
	 */
	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null){
				stmt.close();
			}
		} catch (Exception e) {
			throw new SystemException("Closing SQL Statement",e,SystemCode.UNABLE_TO_EXECUTE);

		}

	}

	/**
	 * Close  Sql Prepared statement
	 */
	public static void closePreparedStatement(PreparedStatement pStmt) {
		try {
			if (pStmt != null){
				pStmt.close();
			}
		} catch (Exception e) {

			throw new SystemException("Closing SQL Prepared Statement",e,SystemCode.UNABLE_TO_EXECUTE);

		}

	}

	/**
	 * Close Sql resultset
	 */
	public static void closeResultSet(ResultSet rSet) {
		try {
			if (rSet != null){
				rSet.close();
			}
		} catch (Exception e) {

			throw new SystemException("Closing SQL Resultset",e,SystemCode.UNABLE_TO_EXECUTE);

		}

	}

	/**
	 * Connection will be released and kept in pool
	 */
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null){
				conn.close();
			}
		} catch (SQLException e) {

			throw new SystemException("Releasing  DB Connection",e,SystemCode.UNABLE_TO_EXECUTE);

		}

	}


	public static BoneCP getConnectionPool() {
		if(connectionPool==null){
			ConnectionManager.configureConnPool();
		}
		return connectionPool;
	}

	public static void setConnectionPool(BoneCP connectionPool) {
		ConnectionManager.connectionPool = connectionPool;
	}

}