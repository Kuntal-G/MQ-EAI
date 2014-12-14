package com.general.mq.common.util.conf;

/**
 * For any DB field/column name update(addition/rename),please update it here. Also change the query in SQL-Config File
 *
 */
public class TableColumnConfig {
	
	
	//Common
	public static final String ID = "id";
	public static final String CLIENT_ID = "clientId";
	public static final String QUEUE_ID = "queueId";
	public static final String QUEUE_NAME = "queueName";
	public static final String ROUTING_KEY = "routingKey";
    public static final String STATUS = "status";
	
	//QueDetail
    public static final String EXCHANGE_NAME = "exchangeName";
    public static final String MAX_ATTEMPT = "maxAtmpt";
    public static final String NEXT_ATTEMPT_DELAY = "nxtAtmptDly";
    public static final String MSG_PRIORITY_ATTEMPT = "msgPriorityAtmpt";
    
	//MsgChannel
    public static final String CHANNEL_ID = "channelId";
    public static final String CHANNEL_STATUS = "channelStatus";
    public static final String CREATE_TIME = "createTime";
    public static final String MAX_UPDATED_TTL = "maxUpdatedTTL";
    public static final String LAST_MSG_DLVRD_TIME = "lastMsgDlvrdTime";
    public static final String LAST_UPDATED_TIME = "lastUpdatedTime";
	
	//Client
	public static final String CLIENT_TYPE = "clientType";
	public static final String IS_ACTIVE = "isActive";
	public static final String TIME_TO_LIVE = "timeToLive";
	
	//History
	public static final String MESSAGE = "message";
    public static final String MSG_ATTR_NAME = "msgAttrName";
    public static final String MSG_ATTR_VALUE = "msgAttrValue";
    public static final String PARENT_ID = "parentId";
    public static final String LOGGING_TIME = "loggingTime";
    public static final String REMARK = "remark";
	
	
    

	
    


    
 
	

}
