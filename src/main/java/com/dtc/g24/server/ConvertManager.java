package com.dtc.g24.server;

import java.io.File;
import java.nio.file.Paths;
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
		G24Setting.sharedFolder()
	);

	// -loglevel quiet：用來關閉 log，否則 Process 會被 block 無法結束（原因不明）
	// -y 覆寫已存在的檔案
	private static final String EXEC = "ffmpeg -loglevel quiet -y -i ";
	private static final int SCAN_PERIOD = 5;
	private final String FULL_COMMAND;
	private final String WORKSPACE;

	private List<String> quene = new ArrayList<>();

	private ConvertManager(String workspace) {
		//取得 runtime class folder 的方法是參考：
		//http://stackoverflow.com/questions/11747833/getting-filesystem-path-of-class-being-executed
		//另一個可以考慮的方法是使用 ServletContext.getRealPath() 來取得實體路徑
		String execPath = new File(
			getClass().getProtectionDomain().getCodeSource().getLocation().getPath(),
			"ffmpeg"
		).getAbsolutePath();

		FULL_COMMAND = execPath + File.separator + EXEC;
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
				String srcFileName = wrap(Paths.get(WORKSPACE, fname + ".mpg").toString());
				String dstFileName = wrap(Paths.get(WORKSPACE, fname + ".mp4").toString());

				//執行外部程序
				Runtime rt = Runtime.getRuntime();
				try {
					Process p = rt.exec(
						FULL_COMMAND +
						srcFileName + " " +
						dstFileName
					);
					p.waitFor();//會 block 住，直到 Process 執行完畢

					ConvertLogService.complete(fname);
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
