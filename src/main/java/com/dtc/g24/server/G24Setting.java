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

	public static String converterPath() {
		return instance.getProperty("converter.path");
	}

	public static String callbackUrl() {
		return instance.getProperty("callback.url");
	}
}
