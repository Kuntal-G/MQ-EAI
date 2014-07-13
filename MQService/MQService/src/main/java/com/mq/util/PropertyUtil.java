package com.mq.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
	private static final char DELIMITTER =',';
	
	private static Properties loadConfig() throws IOException {
		Properties prop = new Properties();
		prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		return prop;
	}
	
	public static String loadValue(String key) throws IOException {
		String value = "";
		Properties prop = loadConfig();
		if (prop != null) {
			value = prop.getProperty(key);
		}
		return value;
	}
	
	
	public static void writeToFile(String clientId,String msgId, String msg){
		try {
			FileWriter writer = new FileWriter("/home/kuntal/practice/config-data/mq.csv");
			
			writer.append("CLIENT_ID");
		    writer.append(DELIMITTER);
		    writer.append("MESSAGE_ID");
		    writer.append(DELIMITTER);
		    writer.append("MESSAGE");
		    writer.append('\n');
	 
			writer.append(clientId);
		    writer.append(DELIMITTER);
		    writer.append(msgId);
		    writer.append(DELIMITTER);
		    writer.append(msg);
		    writer.append('\n');
	 
		    writer.flush();
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
