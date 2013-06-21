/*
Copyright (c) 2012, Weikexin eeplat.com
Copyleft 2013 CALM FBT.com
Released under the MIT license.
http://www.opensource.org/licenses/mit-license.php
 */
package com.fbt.openapi.callback;

import java.io.IOException;

import java.util.Properties;

/**
 * Retrieve a config item from d social.properties.
 * 
 * @author Weikexin
 * 
 */

public final class GlobalConfig {

	// private static CallBackConfig config = new CallBackConfig();

	private static Properties configs = new Properties();

	static {
		try {
			configs.load(GlobalConfig.class
					.getResourceAsStream("/social.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private GlobalConfig() {

	}

	/**
	 * Get a CallBack Uri by Key
	 * 
	 * @param key
	 *            defined in social.properties
	 * @return a callbakc uri
	 */

	public static String getCallBack(String key) {
		return (String) configs.get(key);
	}
	
	
	public static String getValue(String key){
		return (String) configs.get(key);
	}

	public static int getQQConnectionTimeout() {

		String qqConTimeOut = (String) configs.get("qq.connection.timeout");
		if (qqConTimeOut != null) {
			try {
				return Integer.parseInt(qqConTimeOut);
			} catch (NumberFormatException e) {
				return 1000 * 30;
			}
		}

		return 1000 * 30;
	}
	
	

	public static int getQQReadTimeout() {

		String qqReadTimeOut = (String) configs.get("qq.read.timeout");
		if (qqReadTimeOut != null) {
			try {
				return Integer.parseInt(qqReadTimeOut);
			} catch (NumberFormatException e) {
				return 1000 * 30;
			}
		}

		return 1000 * 30;
	}

	// public static CallBackConfig getInstance(){
	// return config;
	// }
	//
	// public Map getAllConfigs(){
	// return callbacks;
	// }
	//
	// public static void main(String[] args){
	//
	// System.out.println( CallBackConfig.getInstance().getAllConfigs() );
	// }

}
