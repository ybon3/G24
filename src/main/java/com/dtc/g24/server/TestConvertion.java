package com.dtc.g24.server;

public class TestConvertion {
	// -loglevel quiet 關閉 log，否則 Process 會被 block 無法結束（原因不明）
	// -y 覆寫已存在的檔案
	private static final String EXEC = "D:\\Tools\\ffmpeg\\bin\\ffmpeg -y -i ";
	private final String WORKSPACE = "d:\\test\\oriv\\shared\\";

	public static void main(String[] args) {
		new TestConvertion().convert("01");
	}

	public void convert(String n) {
		String fname = WORKSPACE + n;
		System.out.println("Starting process " + fname);//Delete

		Runtime rt = Runtime.getRuntime();
		try {
			Process p = rt.exec(
				EXEC +
				fname + ".mpg " +
				fname + ".mp4"
			);
			p.waitFor();//會 block 住，直到 Process 執行完畢

			//TODO callback
			System.out.println("Finished process " + fname);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
