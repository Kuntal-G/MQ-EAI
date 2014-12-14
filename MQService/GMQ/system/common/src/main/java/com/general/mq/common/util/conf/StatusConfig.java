package com.general.mq.common.util.conf;

public class StatusConfig {
	
	//Common
	public static final String CLIENT_ID="Client-ID : ";
	public static final String UI_NULLIFY_VALUE="null";
	public static final String QUEUE_ERROR="Queue-Error : ";
	public static final String TTL_ERROR="TTL-Error : ";
	public static final String CLIENT_ERROR="Client-Error : ";
	public static final String MSG_ATTMPT_NOT_FOUND="Msg Attmpt is not found in Redis.";
	public static final String RK_NOT_EXIST="Routing Key should be from one of the available Routing keys.";
	
	public static final String REASON="Reason : ";
	public static final String CLIENTID_NOT_VALID="Client Id invalid.";
	public static final String RK_NOT_VALID="Routing Key invalid. ";
	public static final String QNAME_NOT_VALID="Queue Name invalid.";
	public static final String TIMEUNIT_NOT_VALID="Time Unit invalid. Permitted Values are 1, 2 & 3 only.";
	public static final String MSGID_NOT_VALID="Message Id invalid.";
	public static final String PARENTID_NOT_VALID="Parent Id invalid.";
	public static final String MSG_PRIORITY_ATMPT_NOT_VALID="msgPriorityAttmpt is invalid. Permitted Values 1, 2 & 3 only.";
	
	
		
	// Queue Service related status
	public static final String QUEUE_CREATE="Queue Created Successfully";
	public static final String QUEUE_MODIFY="Queue Modified Successfully";
	public static final String QUEUE_TERMINATE="Queue Terminated SuccessFully";
	public static final String QUEUE_NOT_EXIST="Queue doesnot Exist";
	public static final String QUEUE_NAME_REQUIRED="Queue Name is mandatory";
	public static final String QUEUE_ALREADY_EXIST="Queue Already Exist";
	
	
	// Client Service related status
	public static final String PUBLISH_MSG="Message Published Successfully";
	public static final String CUSTOM_PUBLISH_MSG="Custom Message Published Successfully";
	public static final String PROCESS_MSG_ACK="Acknowledgement Processed Successfully";
	public static final String CLIENT_NOT_CONSUMER="Client is not Consumer.";
	public static final String CLIENT_NOT_PRODUCER="Client is not Producer.";
	
		
	//Mailing related
	public static final String UPDATE_LOG_LEVEL_SUB="Logging Level Changed";
	
	//Configuration related
	public static final String UPDATE_CONFIG="Configuration Updated Successfully";
	public static final String UPDATE_LOCAL_CACHE="Local Cache Adaptor Updated Successfully";
	
	//Producer related
	public static final String PRODUCER_REGISTER = "Producer Registered Successfully";
	public static final String PRODUCER_DELETE = "Producer Deleted Successfully";
	public static final String PRODUCER_UPDATE ="Producer Updated Successfully";
	public static final String PRODUCER_PUBLISH = "Message Published Successfully";
	
	//Consumer related
	public static final String CONSUMER_REGISTER = "Consumer Registered Successfully";
	public static final String CONSUMER_DEREGISTER = "Consumer DeRegistered Successfully";
	public static final String CONSUMER_UPDATE ="Consumer Updated Successfully";
	public static final String TTL_UPDATE_SUCCESS ="Processing Time Updated Successfully";
	public static final String TTL_UPDATE_REJECT ="Acknowledgement after Processing Time Expiry.";
	public static final String TTL_UPDATE_FAIL ="Processing Time cannot be Updated";
	
	//Common for Produce and Consumer
	public static final String INVALID_CLIENT_ID = "Client ID is Invalid.";
	public static final String INVALID_RK = "Routing Key is Invalid";
	
	
	//Acknowledgement Status
	public static final int SUCCESS_ACK_STATUS=1;
	public static final int FAILURE_ACK_STATUS=0;
	public static final int FAILURE_ACK_RTY_STATUS=3;
	public static final String ACK_ACCEPTED="Acknowledgement processed succesfully";
	public static final String ACK_REJECTED="Acknowledgement rejected";
	public static final String NACK_ACCEPTED="Negetive Acknowledgement processed succesfully";
	public static final String NACK_REJECTED="Negetive Acknowledgement Rejected";
	public static final String INVALID_ACK_STATUS="Acknowledgement status is not valid.";
	public static final String INVALID_ACK_FORMAT="Invalid Acknowledgement Format."; 
	
	//Message Channel Status
	public static final int ACTIVE_CHANNEL_STATUS=1;
	
	
	//General
	public static final String DATA_NOT_EXIST="Data is not Available";

	
}