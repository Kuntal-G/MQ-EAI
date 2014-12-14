package com.general.mq.common.util.conf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;

import org.junit.Test;

import com.general.mq.common.util.conf.ConfigManager;

public class ConfigManagerTest {

	@Test
	public void testgetProperty() {
		final String property = ConfigManager.instance().getProperty("system.supported.language.default", "system-configuration.xml");
		assertEquals("US", property);
	}
	
	
	@Test
	public void testgetIntegerProperty() {
		final Integer property = ConfigManager.instance().getIntegerProperty("database.min.connection", "db-configuration.xml");
		assertEquals("5", property.toString());
	}
	
	@Test
	public void testgetAsStream() {
		final InputStream stream = ConfigManager.instance().getAsStream("mq-configuration.xml");
		assertNotNull(stream);
	}
	
	
	@Test
	public void testGetAbsolutePath() throws Exception {
		final String absolutePath = ConfigManager.instance().getAbsolutePath("../../../service/src/main/java");
		assertTrue(new File(absolutePath).exists());
	}

}
