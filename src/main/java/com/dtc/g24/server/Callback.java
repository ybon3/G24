package com.dtc.g24.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class Callback {
	/**
	 * 使用 Executors 執行，避免程序被 Http 行為 Block 住
	 */
	public static void send(final String fname) {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.execute(new Runnable() {
			@Override
			public void run() {
				HttpPost post = new HttpPost(G24Setting.callbackUrl());
				List<NameValuePair> params = new ArrayList<>();
				params.add(new BasicNameValuePair("fname", fname));
				post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

				HttpClient client = HttpClientBuilder.create().build();

				try {
					HttpResponse response = client.execute(post);

					//XXX 可能可以補強判斷方式
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						ConvertLogService.delete(fname);
					}
				} catch (IOException e) {
					//Ignore
				}
			}
		});
	}
}
