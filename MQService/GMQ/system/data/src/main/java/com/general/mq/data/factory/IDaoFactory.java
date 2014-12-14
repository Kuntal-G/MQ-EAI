package com.general.mq.data.factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public interface IDaoFactory {
	
	Connection  createConnection();
	void closeConnection(Connection conn);
	void closeStatement(Statement stmt);
	void closePreparedStatement(PreparedStatement pStmt);
	void closeResultSet(ResultSet rSet);
}