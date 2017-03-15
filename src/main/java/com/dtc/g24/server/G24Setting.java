package com.dtc.g24.server;

import com.dtc.common.DoubleProperties;

public class G24Setting extends DoubleProperties {
	private static G24Setting instance = new G24Setting();

	private G24Setting() {
		super("dtc-config.xml", "dtc-g24.properties");
	}

	public static String sharedFolder() {
		return instance.getProperty("shared.folder");
	}

	public static String callbackUrl() {
		return instance.getProperty("callback.url");
	}

	public static String dbPath() {
		return instance.getProperty("db.path");
	}

	public static String dbUsername() {
		return instance.getProperty("db.username");
	}

	public static String dbPassword() {
		return instance.getProperty("db.password");
	}

	public static int dbPoolMaxConnections() {
		try {
			return Integer.parseInt(instance.getProperty("db.pool.max.connections", "30")); // default : 30
		} catch (Exception e) {
			return 30;
		}
	}

	// 單位：秒
	public static int dbPoolLoginTimeout() {
		try {
			return Integer.parseInt(instance.getProperty("db.pool.login.timeout", "30")); // default : 30
		} catch (Exception e) {
			return 30;
		}
	}
}
