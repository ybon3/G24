package com.dtc.g24.server;

import java.util.ArrayList;
import java.util.List;

public class ConvertManager {
	// -loglevel quiet：用來關閉 log，否則 Process 會被 block 無法結束（原因不明）
	// -y 覆寫已存在的檔案
	private static final String EXEC = "ffmpeg -loglevel quiet -y -i ";
	private final String FULL_COMMAND;
	private final String WORKSPACE;

	private boolean isRunning;
	private List<String> quene = new ArrayList<>();

	public ConvertManager(String workspace, String convertHome) {
		FULL_COMMAND = convertHome + EXEC;
		WORKSPACE = workspace;
	}

	public void add(String fname) {
		quene.add(fname);
		synchronized (this) {
			if (!isRunning) {
				//單一時間只會執行一個
				isRunning = true;
				new Thread(new ConvertProcess()).start();
			}
		}
	}

	private class ConvertProcess implements Runnable {
		@Override
		public void run() {
			while (!quene.isEmpty()) {
				String fname = quene.remove(0);
				String fullName = WORKSPACE + fname;

				Runtime rt = Runtime.getRuntime();
				try {
					Process p = rt.exec(
						FULL_COMMAND +
						fullName + ".mpg " +
						fullName + ".mp4"
					);
					p.waitFor();//會 block 住，直到 Process 執行完畢

					CallBack.send(fname);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			isRunning = false;
		}
	}
}
