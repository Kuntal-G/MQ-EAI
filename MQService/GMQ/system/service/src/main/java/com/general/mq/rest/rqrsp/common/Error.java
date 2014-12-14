package com.general.mq.rest.rqrsp.common;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Error {

	public Integer code;
	public String message;
	public String errorDetail;
	//public String errorStack;
}