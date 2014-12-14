package com.general.mq.rest.rqrsp;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.general.mq.rest.rqrsp.common.BaseResponse;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DefaultConfigResponse extends BaseResponse{

	public long processTime;
	public int processTimeUnit;
	public int maxAtmpt;
}
