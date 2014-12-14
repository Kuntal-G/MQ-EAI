package com.general.mq.common.error;

import com.general.mq.common.util.conf.ErrorConfig;

public enum ValidationCode implements ErrorCode {

	VALUE_REQUIRED(301,ErrorConfig.CODE_301),
	INVALID_FORMAT(302,ErrorConfig.CODE_302),
	INVALID_JSON_MSG_FORMAT(303,ErrorConfig.CODE_303),
	INVALID_JSON_MAPPING(304,ErrorConfig.CODE_304),
	INVALID_DATE_FORMAT(305,ErrorConfig.CODE_305),
	INVALID_NUMBER_FORMAT(306,ErrorConfig.CODE_306),
	MANDATORY_REQUIRED(307,ErrorConfig.CODE_307),
	INVALID_VALUE(308,ErrorConfig.CODE_308),
	INVALID_PARAM(309,ErrorConfig.CODE_309);


	private final int number;
	private final String message;

	private ValidationCode(int number,String message) {
		this.number = number;
		this.message=message;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public String getMessage() {
		return message;
	}
}