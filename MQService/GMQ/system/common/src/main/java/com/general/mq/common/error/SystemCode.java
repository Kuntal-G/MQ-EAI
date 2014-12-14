package com.general.mq.common.error;

import com.general.mq.common.util.conf.ErrorConfig;

public enum SystemCode implements ErrorCode {

	UNEXPECTED_SYSTEM_ERROR(101,ErrorConfig.CODE_101),
	RESOURCE_NOT_FOUND(102,ErrorConfig.CODE_102),
	CONFIGURATION_LOAD_FAILURE(103,ErrorConfig.CODE_103),
	CONFIGURATION_READ_FAILURE(104,ErrorConfig.CODE_104),
	UNABLE_TO_EXECUTE(105,ErrorConfig.CODE_105),
	INVALID_REQUEST(106,ErrorConfig.CODE_106),
	MAIL_SEND_FAIL(107,ErrorConfig.CODE_107);

	private final int number;
	private final String message;

	private SystemCode(int number,String message) {
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