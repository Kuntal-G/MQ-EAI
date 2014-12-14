package com.general.mq.data.factory;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.general.mq.data.factory.ConnectionManager;

public class TestConnectionManager {

@BeforeClass
public static void setUpBeforeClass() throws Exception {
ConnectionManager.configureConnPool();
}

@AfterClass
public static void tearDownAfterClass() throws Exception {
ConnectionManager.shutdownConnPool();
}


@Test
public void testGetConnection() {
Connection conn = ConnectionManager.getConnection();
assertNotNull(conn);
ConnectionManager.closeConnection(conn);
}

}

