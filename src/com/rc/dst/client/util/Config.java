package com.rc.dst.client.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Config {

	private static Log log = LogFactory.getLog(Config.class);
	private static ResourceBundle bundle;
	private static ResourceBundle bundleConfig;
	static {
		try {
			bundle = ResourceBundle.getBundle("lucene_config");
		} catch (MissingResourceException e) {
			log.error("Advertiesr-Api: missing confir file \"config.properties\"", e);
			e.printStackTrace();
		}
		initBundleConfig("lucene_config");
	}
	
	public static String getString(String key) {
		try {
			String result = bundle.getString(key);
			return result;
		} catch (java.util.MissingResourceException mre) {
			return "";
		}

	}
	
	public static void initBundleConfig(String configFileName){
		try {
			ResourceBundle.clearCache();
			bundleConfig = ResourceBundle.getBundle(configFileName);
		} catch (MissingResourceException e) {
			log.error("Config: missing confir file "+configFileName+".properties", e);
			e.printStackTrace();
		}
	}
	public static String getConfigString(String key) {
		try {
			String result = bundleConfig.getString(key);
			return result;
		} catch (java.util.MissingResourceException mre) {
			return "";
		}

	}
	public static String getString(ResourceBundle resourceBundle,String key) {
		try {
			String result = resourceBundle.getString(key);
			return result;
		} catch (java.util.MissingResourceException mre) {
			return "";
		}

	}
}
