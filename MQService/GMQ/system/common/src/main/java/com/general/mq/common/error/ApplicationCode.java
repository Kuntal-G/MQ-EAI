package com.general.mq.common.error;

import com.general.mq.common.util.conf.ErrorConfig;

public enum ApplicationCode implements ErrorCode {

	DATA_INTEGRITY(201,ErrorConfig.CODE_201),
	DATA_PROCESS_FAIL(202,ErrorConfig.CODE_202),
	DATA_ACCESS(203,ErrorConfig.CODE_203),
	DATA_PATTERN(204,ErrorConfig.CODE_204),
	TRANSFORM_FAILURE(205,ErrorConfig.CODE_205),
	TRANSACTION_FAILURE(206,ErrorConfig.CODE_206),
	RMQ_DATA_ACCESS_FAIL(207,ErrorConfig.CODE_207),
	REDIS_DATA_NOT_FOUND(208,ErrorConfig.CODE_208),
	JSON_PARSE_ERROR(209,ErrorConfig.CODE_208),
	DUMP_QUEUE_FULL(210,ErrorConfig.CODE_210);
	

	private final int number;
	private final String message;
	
	private ApplicationCode(int number,String message) {
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