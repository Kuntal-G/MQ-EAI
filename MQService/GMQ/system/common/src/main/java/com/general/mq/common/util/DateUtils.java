package com.general.mq.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.general.mq.common.error.ValidationCode;
import com.general.mq.common.exception.ApplicationException;


public abstract class DateUtils {
	public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT");
	private static final String DEFAULT_FORMATTER_STR = "MM/dd/yyyy";

	public static Calendar toCalendar(Date date) {
		return toCalendar(date, DEFAULT_TIME_ZONE);
	}

	public static Calendar toCalendar(Date date, TimeZone zone) {
		Calendar calendar = null;
		if (date != null) {
			calendar = new GregorianCalendar(DEFAULT_TIME_ZONE);
			calendar.setTimeInMillis(date.getTime());
			calendar.setTimeZone(zone);
		}
		return calendar;
	}

	public static int getAge(Date dateOfBirth) {
		int age = 0;
		Calendar born = Calendar.getInstance(DEFAULT_TIME_ZONE);
		Calendar now = Calendar.getInstance(DEFAULT_TIME_ZONE);
		if (dateOfBirth != null) {
			now.setTime(new Date());
			born.setTime(dateOfBirth);
			if (born.after(now)) {
				throw new IllegalArgumentException("Can't be born in the future");
			}
			age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
			if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
				age -= 1;
			}
		}
		return age;
	}

	/**
	 * Convert String to Date
	 * 
	 * @param dateOfBirth
	 * @return Date
	 * @throws ApplicationException
	 */
	public static Date toDate(String dateOfBirth) throws ApplicationException {
		DateFormatter formatter = new DateFormatter(DEFAULT_FORMATTER_STR);
		formatter.setLenient(false);
		Date formattedDate = null;
		if (dateOfBirth != null) {
			try {
				formattedDate = formatter.parse(dateOfBirth);
			} catch (ParseException e) {
				throw new ApplicationException(e,ValidationCode.INVALID_DATE_FORMAT);
			}
		}
		return formattedDate;
	}

	/**
	 * Convert Date to String
	 * 
	 * @param dateOfBirth
	 * @return String
	 */
	public static String dateToString(Date dateOfBirth) {
		DateFormatter formatter = new DateFormatter(DEFAULT_FORMATTER_STR);
		String formattedDate = null;
		if (dateOfBirth != null) {
			formattedDate = formatter.format(dateOfBirth);
		}
		return formattedDate;
	}

	public static long nowLong() {
		final Calendar instance = Calendar.getInstance(DEFAULT_TIME_ZONE);
		return instance.getTimeInMillis();
	}

	public static Timestamp nowTimestamp() {
		return new Timestamp(nowLong());
	}

	public static Date nowDate() {
		return new Date(nowLong());
	}


	public static Date sqlToUtil(java.sql.Timestamp sqlDate ) {
		return  new Date(sqlDate.getTime());

	}


	public static java.sql.Timestamp utilToSql(Date utilDate) {
		return new java.sql.Timestamp(utilDate.getTime());
	}



	public static String ExtCalToJavaDate(String strDate)	{
		int i1 = strDate.indexOf(" ");
		int i2= strDate.lastIndexOf(" ");
		strDate = strDate.substring(i1, i2);
		i2 = strDate.lastIndexOf(" ");
		String substr = strDate.substring(i2+1);
		strDate = strDate.substring(0,i2)+"+"+substr;
		return strDate;
	}



}
