package com.general.mq.rest.rqrsp;

import java.util.Date;

import com.general.mq.rest.rqrsp.common.BaseRequest;

public class HistoryRequest extends BaseRequest{
	
	
	public String clientId;
	public String message;
	public String msgAttrName;
	public String msgAttrValue;
	public int status;
	public String parentId;
	public Date loggingTime;
	public String remark;
	
	public Date toTime;
	public Date fromTime;
}
