package com.dtc.g24.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Singleton class
 * <ul>
 * 	<li>單一時間只會進行一個轉檔作業</li>
 * 	<li>fname 不可包含附檔名</li>
 * </ul>
 */
public class ConvertManager {
	public static final ConvertManager instance = new ConvertManager(
		G24Setting.sharedFolder(),
		G24Setting.converterPath()
	);

	// -loglevel quiet：用來關閉 log，否則 Process 會被 block 無法結束（原因不明）
	// -y 覆寫已存在的檔案
	private static final String EXEC = "ffmpeg -loglevel quiet -y -i ";
	private static final int SCAN_PERIOD = 5;
	private final String FULL_COMMAND;
	private final String WORKSPACE;

	private List<String> quene = new ArrayList<>();

	private ConvertManager(String workspace, String convertHome) {
		FULL_COMMAND = convertHome + EXEC;
		WORKSPACE = workspace;

		ScheduledExecutorService scanService = Executors.newSingleThreadScheduledExecutor();
		scanService.scheduleWithFixedDelay(new ConvertProcess(), 0, SCAN_PERIOD, TimeUnit.SECONDS);
	}

	public void add(String fname) {
		quene.add(fname);
	}

	private class ConvertProcess implements Runnable {
		@Override
		public void run() {
			while (!quene.isEmpty()) {
				String fname = quene.remove(0);

				//必須用雙引號包起來，不然遇到空白會發生問題
				String srcFileName = wrap(WORKSPACE + fname + ".mpg");
				String dstFileName = wrap(WORKSPACE + fname + ".mp4");

				//執行外部程序
				Runtime rt = Runtime.getRuntime();
				try {
					Process p = rt.exec(
						FULL_COMMAND +
						srcFileName + " " +
						dstFileName
					);
					p.waitFor();//會 block 住，直到 Process 執行完畢

					Callback.send(fname);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String wrap(String str) {
		return "\"" + str + "\"";
	}
}
