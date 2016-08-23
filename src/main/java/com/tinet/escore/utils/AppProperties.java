package com.tinet.escore.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AppProperties {
	private static Log log = LogFactory.getLog(AppProperties.class);

	private static Properties props = null;
	private static final String PROPERTIES = "conf/jdbc.properties";

	public AppProperties() {
	}


	private static int 		sysId;
	private static int 		sysType;
	private static String 	sysTypeName;
	private static String 	appKey;
	private static String 	appSecret;
	private static String 	driverClassName;
	private static String   nmcUrl; //nmc的URL
	private static String   acctUrl; //tccacct的URL
	private static String   tokenNMC;
	
	static {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES);
		if (is != null) {
			props = new Properties();
			try {
				props.load(is);
				sysTypeName = readString("system.typeName", props);
				sysId = readInt("system.id", -1, props);
				sysType = readInt("system.type", -1, props);
				appKey = readString("system.key", props);
				appSecret = readString("system.secret", props);
			} catch (IOException e) {
				log.error("Could not load properties from " + PROPERTIES + ":" + e.getMessage());
				e.printStackTrace();
				System.exit(0);
			} finally {
				//StreamUtil.close(is);
			}
		}
	}
	
	public static int getSysId() {
		return sysId;
	}

	public static int getSysType() {
		return sysType;
	}

	public static String getAppKey() {
		return appKey;
	}

	public static String getAppSecret() {
		return appSecret;
	}

	public static String getSysTypeName() {
		return sysTypeName;
	}

	public static String getDriverClassName() {
		return driverClassName;
	}



	public static Log getLog() {
		return log;
	}

	public static Properties getProps() {
		return props;
	}

	public static String getProperties() {
		return PROPERTIES;
	}

	private static int readInt(String param, int def, Properties props) {
		int ret = def;
		try {
			String value = props.getProperty(param);
			ret = Integer.parseInt(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}
	
	private static String readString(String param, Properties props) {
		try {
			String value = props.getProperty(param).trim();
			return value;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	private static List<String> readStrings(String param, String sep, Properties props) {
		String value = props.getProperty(param);
		String[] ar = value.split(sep);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < ar.length; i++) {
			list.add(ar[i].trim());
		}
		return list;
	}

	public static String getPropValue(String key) {
		return props.getProperty(key);
	}
	
	public static void main(String[] args){		
//		InputStream ins = AppProperties.class.getResourceAsStream("classpath:conf/app.properties");
		InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES);
		Properties p = new Properties();
		
		try {
			p.load(ins);
			System.out.println(p.getProperty("sysId.ccic1"));
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}
	
}