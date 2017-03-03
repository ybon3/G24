package com.dtc.g24.server;

import java.util.ArrayList;
import java.util.List;

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
	private final String FULL_COMMAND;
	private final String WORKSPACE;

	private boolean isRunning;
	private List<String> quene = new ArrayList<>();
	private Thread thread;

	private ConvertManager(String workspace, String convertHome) {
		FULL_COMMAND = convertHome + EXEC;
		WORKSPACE = workspace;
		thread = new Thread(new ConvertProcess());
	}

	public void add(String fname) {
		quene.add(fname);
		synchronized (this) {
			if (!isRunning) {
				//單一時間只會執行一個
				isRunning = true;
				thread.start();
			}
		}
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

			isRunning = false;
		}
	}

	private String wrap(String str) {
		return "\"" + str + "\"";
	}
}
