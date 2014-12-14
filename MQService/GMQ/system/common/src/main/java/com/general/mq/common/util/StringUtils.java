package com.general.mq.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.general.mq.common.util.conf.MQConfig;



public final class StringUtils {

	private static  String Empty = ""; 
	private static  Set<String> EMPTY_SET = new HashSet<String>();
	private  static long MILLIS_IN_DAY = 1000*60*60*24;
	private static  String ODD = "TSO";
	private static  String EVEN = "TSE";



	/**
	 * Make a string representation of the exception.
	 * @param e The exception to stringify
	 * @return A string with exception name and call stack.
	 */
	public  static String stringifyException(Throwable e) {
		StringWriter stm = new StringWriter();
		PrintWriter wrt = new PrintWriter(stm);
		e.printStackTrace(wrt);
		wrt.close();
		return stm.toString();
	}

	public  static String stringifyStackTrace(StackTraceElement[] stackTrace) {
		final StringBuilder error = new StringBuilder();
		if (stackTrace != null) {
			for (StackTraceElement stackTraceElement : stackTrace) {
				error.append(stackTraceElement);
				error.append("\n");
			}
		}
		return error.toString();
	}

	/**
	 * Time Series Odd and Time Series Even Calculation Logic
	 * based on Current date
	 * @return evenOrOddDay
	 * 
	 * Use it like: String evenOrOddDay = StringUtils.getTimeSeriesTable();
	 */
	@SuppressWarnings("deprecation")
	public static  String getTimeSeriesTable() {
		DateFormat dtFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		final Date date = new Date();
		dtFormat.format(date);
		final int todate = date.getDate();
		long daysReferencingEpoch = (System.currentTimeMillis() / MILLIS_IN_DAY)- todate;
		if (daysReferencingEpoch % 2 == 0)
			return EVEN;
		else
			return ODD;
	}

	/**
	 * Method for generating Unique Id for Message
	 * @return MsgID
	 */

	public static String generateMsgID(){
		UUID clientId = UUID.randomUUID();
		return String.valueOf(clientId);

	}

	/**
	 * Method for generating Unique Id for Producer and Consumer
	 * @return ClientID
	 */

	public static String generateClientID(){
		SecureRandom ng = new SecureRandom();
		String clienID="CID-"+ng.nextInt(1000)+"-"+System.nanoTime();
		return clienID;
	}


	/**
	 * 
	 * @param str 
	 * @param prefix
	 * @return
	 */
	public static boolean startsWith(String str, String prefix) {
		return startsWith(str, prefix, false);
	}
	/**
	 * 
	 * @param str
	 * @param prefix
	 * @param ignoreCase
	 * @return
	 */
	private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
		if (str == null || prefix == null) {
			return (str == null && prefix == null);
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
	}

	/**
	 * Checks if a string is empty
	 * @param text String value to check
	 * @return true/false.
	 */
	public final static boolean isEmpty (final String text) {
		if (null == text ) return true;
		if ( text.length() == 0 ) return true;
		else return false;
	}

	public final static boolean isNonEmpty (final String text) {
		if (null == text ) return false;
		if ( text.length() == 0 ) return false;
		return true;
	}	  

	/**
	 * Given an array of strings, return a comma-separated list of its elements.
	 * @param strs Array of strings
	 * @return Empty string if strs.length is 0, comma separated list of strings
	 * otherwise
	 */

	public  static String arrayToString(final String[] strs) {
		return arrayToString(strs, ',');
	}

	public  static String getLineSeaprator() {
		String seperator = System.getProperty ("line.seperator");
		if ( null == seperator) return "\n";
		else return seperator;
	}

	public  static String arrayToString(final String[] strs,final char delim) {
		if (strs == null || strs.length == 0) { return ""; }
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(strs[0]);
		for (int idx = 1; idx < strs.length; idx++) {
			sbuf.append(delim);
			sbuf.append(strs[idx]);
		}
		return sbuf.toString();
	}

	/**
	 * returns an arraylist of strings  
	 * @param str the comma seperated string values
	 * @return the arraylist of the comma seperated string values
	 */
	public  static String[] getStrings(final String str){
		return getStrings(str, ",");
	}

	/**
	 * returns an arraylist of strings  
	 * @param str the delimiter seperated string values
	 * @param delimiter
	 * @return the arraylist of the comma seperated string values
	 */
	public  static String[] getStrings( String str, String delimiter){
		if (isEmpty(str)) return null;
		StringTokenizer tokenizer = new StringTokenizer (str,delimiter);
		List<String> values = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			values.add(tokenizer.nextToken());
		}
		return (String[])values.toArray(new String[values.size()]);
	}

	public  static String[] getStrings (final String line,final char delim) {
		String[] result = new String[]{ line, null };
		if (line == null) return result;

		int splitIndex = line.indexOf(delim);
		if ( -1 != splitIndex) {
			result[0] = line.substring(0,splitIndex);
			if ( line.length() > splitIndex )
				result[1] = line.substring(splitIndex+1);
		}
		return result;
	}

	public  static List<String> fastSplit( String text, char separator) {
		return fastSplit (null, text, separator);
	}

	public  static List<String> fastSplit(List<String> result ,  String text, char separator) {
		if (isEmpty(text)) return null;

		if ( null == result) result = new ArrayList<String>();
		int index1 = 0;
		int index2 = text.indexOf(separator);

		if ( index2 >= 0 ) {
			String token = null;
			while (index2 >= 0) {
				token = text.substring(index1, index2);
				result.add(token);
				index1 = index2 + 1;
				index2 = text.indexOf(separator, index1);
				if ( index2 < 0 ) index1--;
			}

			if (index1 < text.length() - 1) {
				result.add(text.substring(index1+1));
			}

		} else {
			result.add(text);
		}

		return result;
	}	  

	public static  void fastSplit( final String[] result,  int[] positions, 
			final String text, final char separator) {

		if (text == null) return;
		if (text.length() == 0) return;

		int index1 = 0;
		int index2 = text.indexOf(separator);

		int pos = -1;
		int resultSeq = 0;
		if ( index2 >= 0 ) {
			String token = null;
			while (index2 >= 0) {
				pos++;
				for ( int aPos: positions ) {
					if ( pos != aPos) continue;
					token = text.substring(index1, index2);
					result[resultSeq++] = token;
					break;
				}
				index1 = index2 + 1;
				index2 = text.indexOf(separator, index1);
				if ( index2 < 0 ) index1--;
			}

			if (index1 < text.length() - 1) {
				pos++;
				for ( int aPos: positions ) {
					if ( pos != aPos) continue;
					result[resultSeq++] = text.substring(index1+1);
					break;
				}
			}

		} else {
			pos++;
			for ( int aPos: positions ) {
				if ( pos != aPos) continue;
				result[resultSeq++] = text;
				break;
			}
		}
	}	  


	public  static String firstTokens( String text, char separator, int tokens) {
		if (isEmpty(text)) return null;

		int index1 = 0;
		int index2 = text.indexOf(separator);
		StringBuilder sb = new StringBuilder();
		String token = null;
		int loop = 0;
		while (index2 >= 0 && loop < tokens) {
			token = text.substring(index1, index2);
			sb.append(token).append(' ');
			loop++;
			index1 = index2 + 1;
			index2 = text.indexOf(separator, index1);
		}

		if ((index1 < text.length() - 1) && loop < tokens) {
			sb.append(token);
		}
		return sb.toString();
	}


	public  static List<String> arrayToList( String[] strA){
		if ( null == strA) return null;
		List<String> strL = new ArrayList<String>(strA.length);
		for (String aStr : strA) {
			strL.add(aStr);
		}
		return strL;
	}


	/**
	 * returns an arraylist of strings  
	 * @param str the comma seperated string values
	 * @return the arraylist of the comma seperated string values
	 */
	public  static Set<String> getUniqueStrings( String str){
		if (str == null)
			return EMPTY_SET;

		StringTokenizer tokenizer = new StringTokenizer (str,",");
		Set<String> values = new HashSet<String>();
		while (tokenizer.hasMoreTokens()) {
			values.add(tokenizer.nextToken());
		}
		return values;
	}

	/**
	 * 
	 * @param input - The original String
	 * @param padding - The padding character
	 * @param finalLength - Final length we want to have 
	 * @return
	 */
	public  static String pad( String input, char padding, int finalLength) {
		int length = 0;
		if ( null != input ) length = input.length();
		else input = "";

		if ( finalLength > length ) {
			StringBuilder sb = new StringBuilder(finalLength);
			sb.append(input);
			for ( int i=length; i< finalLength; i++ ) sb.append(padding);
			return sb.toString();
		} else {
			return input.substring(0,finalLength);
		} 
	}

	public  static String encodeXml(String text) {
		text = text.replaceAll("<", "&lt;");
		text = text.replaceAll(">", "&gt;");
		text = text.replaceAll("&", "&amp;");
		return text;
	}

	public  static String removeNonBreakingSpaces( String text)
	{
		if (StringUtils.isEmpty(text)) return StringUtils.Empty;

		StringBuilder sb = new StringBuilder(text.length());
		for (char ch : text.toCharArray())
		{
			if ((int)ch != 160) sb.append(ch);
		}
		return sb.toString();
	}


	public  static String encodeText( String text,  String encoding)
	{
		try
		{
			return URLEncoder.encode(text, encoding);
		} 
		catch (UnsupportedEncodingException e)
		{
			return text;
		}
	}


	public  static String encodeText( String text)
	{
		if (StringUtils.isEmpty(text)) return text;
		return text.replace(" ", "%20");
	}

	public  static String padWithQuotes( String text)
	{
		if (isEmpty(text)) return text;
		StringBuilder sb = new StringBuilder(text.length() + 2);
		sb.append("\"").append(text).append("\"");
		return sb.toString();
	}

	private  static Pattern pattern = Pattern.compile("\\s+");
	public  static String stripExtraSpace(String input)
	{
		Matcher matcher = pattern.matcher(input);
		return matcher.replaceAll(" ");
	}

	public  static String getId( String text) {
		if (isEmpty(text)) return text;
		return new Integer(text.hashCode()).toString();
	}

	public  static String[] toStringArray( Object[] array) {
		String[] newArray = new String[array.length];

		for (int i = 0; i < array.length; i++) {
			newArray[i] = String.valueOf(array[i]);
		}
		return newArray;
	}	


	public static String getQueueName(String routingKey,String exchangeName){	
		return routingKey+MQConfig.QUEUE_PREFIX+exchangeName;
	}


	public static String getDelayQueueName(String queueName){
		return MQConfig.DELAY_PREFIX + queueName;		 
	}


	public static String prepareCacheKey(String ... id)
	{
		String cacheKey = "" ;
		int cacheIdSizeIndexCount=0;

		for(String cacheId:id){
			cacheIdSizeIndexCount++;
			int cacheIdSizeIndex=id.length;
			if(cacheIdSizeIndex==cacheIdSizeIndexCount){
				cacheKey=cacheKey+cacheId;
			}else{
				cacheKey=cacheKey+cacheId+MQConfig.CACHE_KEY_SEPARATOR;
			}

		}

		return cacheKey;
	}


	public static String prepareMsgId(String msgId, String dTag)
	{
		return msgId+MQConfig.CACHE_KEY_SEPARATOR+dTag;
	}


	public static String extractID(String cacheKey, int position)
	{
		StringTokenizer strTkn = new StringTokenizer(cacheKey, MQConfig.CACHE_KEY_SEPARATOR);
		ArrayList<String> arrLis = new ArrayList<String>(cacheKey.length());
		while(strTkn.hasMoreTokens())
			arrLis.add(strTkn.nextToken());
		return arrLis.get(position);
	}

	/**
	 * Trim out double quotes from start and end of String
	 * @param text
	 * @return
	 */
	public static String trimDoubleQuotes(String text) {
		int textLength = text.length();

		if (textLength >= 2 && text.charAt(0) == '"' && text.charAt(textLength - 1) == '"') {
			return text.substring(1, textLength - 1);
		}

		return text;
	}




}