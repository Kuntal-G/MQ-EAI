package com.general.mq.common.util;

import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class DateFormatter extends SimpleDateFormat {
	public DateFormatter(String format) {
		super(format);
		super.setLenient(false);
	}
}
