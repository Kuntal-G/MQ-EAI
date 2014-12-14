package com.general.mq.common.util.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

import com.general.mq.common.error.SystemCode;
import com.general.mq.common.exception.SystemException;





class ConfigCache {
	static final String CONFIG_TYPE = "CONFIG_TYPE";
	static final String CONFIG_ROOT = "CONFIG_ROOT";
	static final String APP_DEV = "APP_DEV";
	static final String APP_PROD = "APP_PROD";
	static final String APP_QA = "APP_QA";

	private final Map<String, Properties> propertiesCache = new ConcurrentHashMap<String, Properties>();
	private final String separator;
	private final String type;
	private final String root;
	private final String environment;
	private final String domain;
	private final String machine;
	/*
	 * Non final for second time initialization
	 */
	private static Logger logger = LoggerFactory.getLogger(ConfigCache.class);;

	public ConfigCache() {
		this.type = System.getProperty(CONFIG_TYPE);
		final boolean isInline = isInMemory();
		this.separator = isInline ? "/" : System.getProperty("file.separator");
		this.root = isInline ? "/config" + separator + "general-mq" : System.getProperty(CONFIG_ROOT);
		this.environment = System.getProperty(APP_DEV);
		this.domain = System.getProperty(APP_PROD);
		this.machine = System.getProperty(APP_QA);
	}

	Properties get(String config) {
		if (!propertiesCache.keySet().contains(config)) {
			cache(config);
		}
		return propertiesCache.get(config);
	}

	void cacheAll() {
		final File file = new File(root);
		if (file.exists()) {
			String[] list = file.list();
			for (String config : list) {
				if (isValid(config)) {
					cache(config);
				}
			}
		}
	}

	Properties cache(String config) {
		reinitializedLogger();
		final Properties properties = new Properties();
		cache(config, "", properties, true);
		if (environment != null && !"".equals(environment)) {
			boolean found = cache(config, environment.toLowerCase(), properties, false);
			if (found && domain != null && !"".equals(domain)) {
				String real = appendPath(environment.toLowerCase(), domain.toLowerCase());
				found = cache(config, real, properties, false);
				if (found && machine != null && !"".equals(machine)) {
					real = appendPath(environment.toLowerCase(), domain.toLowerCase(), machine.toLowerCase());
					cache(config, real, properties, false);
				}
			}
		}
		propertiesCache.put(config, properties);
		return properties;
	}

	boolean cache(String config, String path, Properties properties, boolean breakOnFailure) throws SystemException {
		final String appendPath = appendPath(root, path, config);
		logger.trace("Trying to load configuration: " + path + this.separator + config);
		if (isInMemory()) {
			try {
				final InputStream in = ConfigCache.class.getResourceAsStream(appendPath);
				properties.loadFromXML(in);
			} catch (Exception exception) {
				if (breakOnFailure) {
					throw new SystemException(exception, SystemCode.CONFIGURATION_LOAD_FAILURE);
				}
			}
			return true;
		} else {
			final File file = new File(appendPath);
			if (file.exists()) {
				try {
					final FileInputStream in = new FileInputStream(file);
					properties.loadFromXML(in);
				} catch (Exception exception) {
					throw new SystemException(exception, SystemCode.CONFIGURATION_LOAD_FAILURE);
				}
				return true;
			} else {
				if (breakOnFailure) {
					throw new SystemException(appendPath,SystemCode.RESOURCE_NOT_FOUND);
				}
				return false;
			}
		}

	}

	void clear() {
		propertiesCache.clear();
	}

	void clear(String config) {
		propertiesCache.remove(config);

	}

	InputStream getAsStream(String file, boolean absolutePath) {
		if (absolutePath) {
			return ConfigCache.class.getResourceAsStream(file);
		} else {
			final String appendPath = appendPath(root, "", file);
			return ConfigCache.class.getResourceAsStream(appendPath);
		}
	}

	InputStream getAsStream(String file, String domain) {
		final String appendPath = appendPath(root, domain, file);
		return ConfigCache.class.getResourceAsStream(appendPath);
	}

	private void reinitializedLogger() {
		if (logger == null || logger instanceof NOPLogger) {
			logger.info(ConfigCache.class.getName() + " logger was initialzed incorrectly. trying to reinitilize...");
			logger = LoggerFactory.getLogger(ConfigCache.class);
		}
		if (!(logger instanceof NOPLogger)) {
			logger.info(ConfigCache.class.getName() + " logger is reinitialzed with proper appender");
		}
	}

	private boolean isValid(String config) {
		return config.endsWith(".xml");
	}

	private boolean isInMemory() {
		return type == null || "INLINE".equalsIgnoreCase(type);
	}

	private String appendPath(String... paths) {
		if (paths.length > 0) {
			String path = paths[0];
			for (int index = 1; index < paths.length; index++) {
				final String string = paths[index];
				if (!"".equals(string.trim())) {
					path = path + separator + string;
				}
			}
			return path;
		}
		return null;
	}

}
