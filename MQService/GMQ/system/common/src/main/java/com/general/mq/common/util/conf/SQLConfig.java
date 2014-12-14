package com.general.mq.common.util.conf;


public class SQLConfig {

	public static final int defaultQueueID = 1;
	public static final int CHANNEL_STALE=0;
	public static final int CHANNEL_ACTIVE=0;
	public static final long BUFFER_TIME_CLEAN=900000;
	
	//Queue Table related SQL
	public static final String CREATE_QUEUE= "INSERT INTO Queue_Details"
			+ "(queueName, exchangeName, maxAtmpt,nxtAtmptDly,msgPriorityAtmpt,routingKey) "
			+ "VALUES(?,?,?,?,?,?)";

	public static final String MODIFY_QUEUE= "";
	
	

	public static final String LOAD_ALL_QUEUE = "SELECT * FROM Queue_Details where queueId<>1"; 

	public static final String FIND_QUEUE = "SELECT * FROM Queue_Details WHERE queueName=? AND queueId<>1"; 
	public static final String FIND_EXCHANGE = "SELECT * FROM Queue_Details WHERE exchangeName=? AND queueId<>1"; 



	//History Table related SQL
	public static final String SAVE_MSG_HISTORY="INSERT INTO History"
			+ "(queueName, clientId, message,msgAttrName,msgAttrValue,status,parentId,loggingTime,remark) "
			+ "VALUES(?,?,?,?,?,?,?,?,?)";

	public static final String LOAD_ALL_MSG_HISTORY = "SELECT * FROM History ORDER BY loggingTime desc LIMIT ?,?"; 

	

	
	//ExchangeRK table related SQL
	public static final String LOAD_ALL_EXCHANGE = "SELECT * FROM ExchangeRK";
	public static final String LOAD_KEY_BY_EXCHANGE = "SELECT * FROM ExchangeRK where exchangeName=?";
	public static final String ADD_EXCHANGE = "INSERT INTO ExchangeRK"
				+ "(exchangeName, routingKey) "
				+ "VALUES(?,?)";
	
	
	
	//Client Table related SQL
		public static final String CREATE_CLIENT= "INSERT INTO Client"
				+ "(clientId, clientType, queueId,isActive,routingKey,timeToLive) "
				+ "VALUES(?,?,?,?,?,?)";

		public static final String UPDATE_CLIENT= "UPDATE Client set queueId=?,routingKey=? WHERE clientId=?";

		public static final String LOAD_ALL_CLIENT = "SELECT * FROM Client WHERE isActive=1"; 

		public static final String FIND_CLIENT= "SELECT * FROM Client WHERE clientId=?"; 
		
		
		
		//Channel Related SQL
		public static final String CREATE_CHANNEL= "INSERT INTO MsgChannel"
				+ "(clientId, channelId, createTime,channelStatus,maxUpdatedTTL,lastUpdatedTime,lastMsgDlvrdTime) "
				+ "VALUES(?,?,?,?,?,?,?)";

		public static final String UPDATE_CHANNEL_STALE= "UPDATE MsgChannel set channelStatus="+SQLConfig.CHANNEL_STALE+" WHERE channelId=?";
		
		public static final String UPDATE_CHANNEL_MAXTTL= "UPDATE MsgChannel set maxUpdatedTTL=? WHERE channelId=?";
		
		public static final String UPDATE_CHANNEL_LASTUPDATETIME= "UPDATE MsgChannel set lastUpdatedTime=? WHERE channelId=?";
		
		public static final String UPDATE_CHANNEL_LASTDELVERDTIME= "UPDATE MsgChannel set lastMsgDlvrdTime=? WHERE channelId=?";
		
		public static final String UPDATE_CHANNEL_MAXTTLANDLASTUPDATEDTIME= "UPDATE MsgChannel set maxUpdatedTTL=?,lastUpdatedTime=? WHERE channelId=?";
		

		public static final String DELETE_CHANNEL= "";

		public static final String LOAD_ALL_CHANNEL = "SELECT * FROM MsgChannel"; 
		
		public static final String FIND_CHANNEL_BY_FILTER= "SELECT * FROM MsgChannel WHERE clientId=?";
		
		public static final String CHNL_CLN_ALL_CHANNEL = "SELECT * FROM MsgChannel LOCK IN SHARE MODE";
		
				

}