package com.dtc.g24.server;

import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class CallBack {
	public static void send(String fname) {
		HttpGet request = new HttpGet(G24Setting.callbackUrl() + "?fname=" + fname);

		HttpClient client = HttpClientBuilder.create().build();

		try {
			client.execute(request);
		} catch (IOException e) {
			//Ignore
		}
	}
}
