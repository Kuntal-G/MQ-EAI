package com.general.mq.rest.rqrsp.common;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BaseResponse {
	public Boolean success;
	public String status;
	

}
