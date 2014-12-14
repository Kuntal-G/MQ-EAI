package com.general.mq.common.util.conf;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class is responsible for reading, reloading configuration files from
 * file system
 * 
 * 
 */
public class ConfigManager {
	private static final ConfigManager instance = new ConfigManager();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private ConfigCache cache;

	public static ConfigManager instance() {
		return instance;
	}

	private ConfigManager() {
		cache = new ConfigCache();		
	}
	
	public InputStream getAsStream(String file, boolean absolutePath) {
		return cache.getAsStream(file, absolutePath);
	}
	
	public InputStream getAsStream(String file) {
		return cache.getAsStream(file, false);
	}
	
	public InputStream getAsStream(String file, String domain) {
		return cache.getAsStream(file, domain);
	}

	public Properties getProperties(String config) {
		return cache.get(config);		
	}

	
	/**
	 * @param key
	 *            : Configuration key
	 * @param config
	 *            : configuration file name
	 * @return
	 */
	public String getProperty(String key, String config) {
		Properties list = cache.get(config);
		if (list == null) {
			list = cache.cache(config);
		}
		return list.getProperty(key);
	}

	public boolean getBooleanProperty(String key, String config) {
		final String property = getProperty(key, config);
		return (property != null && property.trim().equalsIgnoreCase("true")) ? true : false;
	}

	public int getIntegerProperty(String key, String config) {
		final String property = getProperty(key, config);
		return property != null ? Integer.parseInt(property) : -1;
	}
	
	public long getLongProperty(String key, String config) {
		final String property = getProperty(key, config);
		return property != null ? Long.parseLong(property) : -1;
	}

	/**
	 * This method will be used for clearing Configuration Cache.
	 */
	public void clearCache() {
		
		try {
			lock.readLock().lock();
			cache = new ConfigCache();
		} finally {
			lock.readLock().unlock();
		}		
	}
	
	public String getAbsolutePath(String path) {
		final URL resource = ConfigManager.class.getResource("/");
		File file = new File(resource.getFile());
		while(path.contains("../")){
			file = new File(file.getAbsolutePath()).getParentFile();
			path = path.replaceFirst("\\.\\./", "");
		}
		return file.getAbsolutePath()+"/"+path;
		

	}
	
}