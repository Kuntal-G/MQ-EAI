package com.general.mq.common.util.conf;

/**
 * A class for maintaining the component related value from configuration(xml) files for various components.
 *
 */
public  class MQConfig {

	private static final ConfigManager instance = ConfigManager.instance();
	private static final String MQ_CONFIGURATION_XML = "mq-configuration.xml";
	private static final String DB_CONFIGURATION_XML = "db-configuration.xml";
	private static final String CACHE_CONFIGURATION_XML = "cache-configuration.xml";
	private static final String SYSTEM_CONFIGURATION_XML = "system-configuration.xml";

	//Database(MySQL) Configurations
	public static final String DATABASE_DRIVER = instance.getProperty("database.driver",MQConfig.DB_CONFIGURATION_XML);
	public static final String DATABASE_URL = instance.getProperty("database.url",MQConfig.DB_CONFIGURATION_XML)+instance.getProperty("database.name",MQConfig.DB_CONFIGURATION_XML);
	public static final String DATABASE_USER = instance.getProperty("database.user",MQConfig.DB_CONFIGURATION_XML);
	public static final String DATABASE_PASS = instance.getProperty("database.password",MQConfig.DB_CONFIGURATION_XML);
	public static final Integer DATABASE_MIN_CONN = instance.getIntegerProperty("database.min.connection",MQConfig.DB_CONFIGURATION_XML);
	public static final Integer DATABASE_MAX_CONN = instance.getIntegerProperty("database.max.connection",MQConfig.DB_CONFIGURATION_XML);
	public static final Integer DATABASE_MAX_PARTITION = instance.getIntegerProperty("database.partition",MQConfig.DB_CONFIGURATION_XML);
	public static final boolean DATABASE_AUTOCOMMIT = instance.getBooleanProperty("database.autocommit",MQConfig.DB_CONFIGURATION_XML);;
	public static final String DATABASE_TYPE = instance.getProperty("system.database.type",MQConfig.SYSTEM_CONFIGURATION_XML);

	//Cache(Redis) Configurations
	public static final Integer CACHE_PORT = instance.getIntegerProperty("cache.port",MQConfig.CACHE_CONFIGURATION_XML );
	public static final String CACHE_HOST = instance.getProperty("cache.host",MQConfig.CACHE_CONFIGURATION_XML );
	public static final Integer CACHE_TIMEOUT = instance.getIntegerProperty("cache.timeout",MQConfig.CACHE_CONFIGURATION_XML );
	public static final Integer CACHE_MAX_ACTIVE = instance.getIntegerProperty("cache.maxActive",MQConfig.CACHE_CONFIGURATION_XML );
	public static final Integer CACHE_MAX_IDLE = instance.getIntegerProperty("cache.maxIdle",MQConfig.CACHE_CONFIGURATION_XML );
	public static final Integer CACHE_MIN_IDLE = instance.getIntegerProperty("cache.minIdle",MQConfig.CACHE_CONFIGURATION_XML );
	public static final boolean CACHE_TEST_ON_BORROW = instance.getBooleanProperty("cache.testOnBorrow",MQConfig.CACHE_CONFIGURATION_XML );
	public static final boolean CACHE_TEST_ON_RETURN = instance.getBooleanProperty("cache.testOnReturn",MQConfig.CACHE_CONFIGURATION_XML );
	public static final boolean CACHE_TEST_WHILE_IDLE = instance.getBooleanProperty("cache.testWhileIdle",MQConfig.CACHE_CONFIGURATION_XML );
	public static final Integer CACHE_NUM_TEST_PER_EVICTIONRUN = instance.getIntegerProperty("cache.numTestsPerEvictionRun",MQConfig.CACHE_CONFIGURATION_XML );
	public static final Integer CACHE_TIME_BETWEEN_EVICTIONRUNS_MILLIS = instance.getIntegerProperty("cache.timeBetweenEvictionRunsMillis",MQConfig.CACHE_CONFIGURATION_XML );
	public static final Integer CACHE_MAX_WAIT = instance.getIntegerProperty("cache.maxWait",MQConfig.CACHE_CONFIGURATION_XML );
	public static final Integer CACHE_MIN_EVICTIONABLE_IDLETIME_MILLIS = instance.getIntegerProperty("cache.minEvictableIdleTimeMillis",MQConfig.CACHE_CONFIGURATION_XML );
	public static final Integer CACHE_SOFT_MIN_EVICTIONABLE_IDLETIME_MILLIS = instance.getIntegerProperty("cache.softMinEvictableIdleTimeMillis",MQConfig.CACHE_CONFIGURATION_XML );
	public static final boolean CACHE_WHEN_EXHAUSTED_ACTION = instance.getBooleanProperty("cache.whenExhaustedAction",MQConfig.CACHE_CONFIGURATION_XML );
	public static final String CACHE_KEY_SEPARATOR = instance.getProperty("cache.key.separator",MQConfig.CACHE_CONFIGURATION_XML );
	
	
	
	//Message Queue(Rabbit) Configurations
	public static final String MQ_HOST = instance.getProperty("queue.hostname",MQConfig.MQ_CONFIGURATION_XML);
	public static final String MQ_PORT = instance.getProperty("queue.port",MQConfig.MQ_CONFIGURATION_XML);
	public static final String MQ_USER = instance.getProperty("queue.username",MQConfig.MQ_CONFIGURATION_XML);
	public static final String MQ_PASS = instance.getProperty("queue.password",MQConfig.MQ_CONFIGURATION_XML);
	public static final Integer MQ_CHANNEL_PERCONN = instance.getIntegerProperty("queue.channel.perconnection",MQConfig.MQ_CONFIGURATION_XML );
	public static final String QUEUE_PREFIX = instance.getProperty("queue.prefix",MQConfig.MQ_CONFIGURATION_XML);
	public static final String DELAY_PREFIX = instance.getProperty("queue.delay.prefix",MQConfig.MQ_CONFIGURATION_XML);
	public static final String EXCHANGE_PATTERN = instance.getProperty("exchange.naming.pattern",MQConfig.MQ_CONFIGURATION_XML);
	public static final int DEFAULT_MAXATTMPT = instance.getIntegerProperty("queue.default.maxAttmpt",MQConfig.MQ_CONFIGURATION_XML);
	public static final long DEFAULT_NXTATTMPTDLY = instance.getLongProperty("queue.default.nxtAttmptDly",MQConfig.MQ_CONFIGURATION_XML);
	public static final int DEFAULT_MSGPRIORITYATTMPT = instance.getIntegerProperty("queue.default.msgPriorityAttmpt",MQConfig.MQ_CONFIGURATION_XML);
	public static final String DEFAULT_ROUTINGKEY = instance.getProperty("queue.default.routingkey",MQConfig.MQ_CONFIGURATION_XML);
	public static final String DEREGISTERED_CLIENT_QUEUE_NAME="00000";
	public static final int MQ_MAX_PRIORITY = instance.getIntegerProperty("queue.max.priority",MQConfig.MQ_CONFIGURATION_XML);
	public static final long MESSAGE_PROCESSING_TIME=instance.getLongProperty("message.processing.time",MQConfig.MQ_CONFIGURATION_XML);
	public static final int MESSAGE_PROCESSING_TIMEUNIT = instance.getIntegerProperty("message.processing.time.unit",MQConfig.MQ_CONFIGURATION_XML);
	
	
	//System Configurations
	public static final String DEFAULT_SUPPORTED_LANGUAGE = instance.getProperty("system.supported.language.default",MQConfig.SYSTEM_CONFIGURATION_XML);
	public static final long CHNL_CLENR_DELAY = instance.getLongProperty("system.channel.cleaner.delay",MQConfig.SYSTEM_CONFIGURATION_XML);
	public static final long MSG_BATCH_SIZE= instance.getLongProperty("system.message.batch.size",MQConfig.SYSTEM_CONFIGURATION_XML);
	public static final int DUMP_QUEUE_SIZE = instance.getIntegerProperty("dump.queue.size",MQConfig.SYSTEM_CONFIGURATION_XML);
	public static final int DUMP_BATCH_SIZE = instance.getIntegerProperty("batch.msg.dump.size",MQConfig.SYSTEM_CONFIGURATION_XML);
	public static final long DUMP_THREAD_SLEEP= instance.getLongProperty("dump.thread.sleep",MQConfig.SYSTEM_CONFIGURATION_XML);
	
	
	//Mail
	public static final String MAIL_HOST = instance.getProperty("system.smtp.host",MQConfig.SYSTEM_CONFIGURATION_XML);
	public static final String MAIL_PORT = instance.getProperty("system.smtp.port",MQConfig.SYSTEM_CONFIGURATION_XML);
	public static final String MAIL_USERNAME = instance.getProperty("system.smtp.username",MQConfig.SYSTEM_CONFIGURATION_XML);
	public static final String MAIL_PASSWORD= instance.getProperty("system.smtp.password",MQConfig.SYSTEM_CONFIGURATION_XML);
	public static final String MAIL_TO_NOTIFY = instance.getProperty("system.notification.mail",MQConfig.SYSTEM_CONFIGURATION_XML);
	
	public static final String MESSAGE_SEPARATOR = instance.getProperty("	system.message.separator",MQConfig.SYSTEM_CONFIGURATION_XML);


	//Mixed
	public static final String EMPTY_SPACE_SEPERATOR="";

	
}
