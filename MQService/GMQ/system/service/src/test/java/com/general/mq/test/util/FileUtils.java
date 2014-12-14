package com.general.mq.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FileUtils {

	private static FileInputStream input;
	private static FileOutputStream out;
	private static Properties prop;
	private static String PATH=System.getProperty("user.dir")+"/src/test/java/";

	public static String getProperty(String fileName,String keyName){
		try {
			input=new FileInputStream(PATH+fileName);
			prop= new Properties();
			prop.load(input);

			return prop.getProperty(keyName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public static Properties getAllProperty(String fileName){
		try {
			input=new FileInputStream(PATH+fileName);
			prop= new Properties();
			prop.load(input);

			return prop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void  storeProperty(String fileName,Properties prop){

		try {
			out = new FileOutputStream(PATH+fileName);
			prop.store(out, "Updating Value");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
}
