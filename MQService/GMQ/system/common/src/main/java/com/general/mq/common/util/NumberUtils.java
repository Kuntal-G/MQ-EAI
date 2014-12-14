package com.general.mq.common.util;

import java.math.BigDecimal;

public class NumberUtils {

	public static String toString(BigDecimal bigDecimal) {
		return bigDecimal.toString();
	}
	
	public static long toMiliSeconds(long ttl, int timeUnit)	{
		long longttl = ttl;
		switch (timeUnit) {
		case 2:
			longttl = ttl*1000;
			break;
		case 3:
			longttl = ttl*60*1000;
			break;
		}
		return longttl;
	}

}
