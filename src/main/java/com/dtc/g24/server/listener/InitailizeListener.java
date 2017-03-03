package com.dtc.g24.server.listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.h2.tools.Server;

import com.dtc.g24.server.Callback;
import com.dtc.g24.server.ConvertLogService;
import com.dtc.g24.server.ConvertManager;
import com.dtc.g24.server.dao.ConnectionFactory;
import com.dtc.g24.server.jooq.tables.pojos.ConvertLog;
import com.google.common.io.CharStreams;


// 在啟動 web application 時進行資料庫檢查，若 Table 不存在就建立
@WebListener
public class InitailizeListener implements ServletContextListener {

	private Server h2TcpServer;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			dbInitialize();
			convertLogCheck();
		} catch (Exception e) {
			//XXX 是否該在這邊阻止 bootup？
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (h2TcpServer != null) {
			h2TcpServer.stop();
		}
	}

	private void dbInitialize() throws Exception {
		Connection conn = null;
		InputStream is = null;
		try {
			// start the TCP Server
			h2TcpServer = Server.createTcpServer().start();

			// 取得資料庫連線
			conn = ConnectionFactory.get();

			is = getClass().getClassLoader().getResourceAsStream("database/tableSchema.sql");
			String sql = CharStreams.toString(
				new InputStreamReader(is, StandardCharsets.UTF_8)
			);

			conn.createStatement().execute(sql);
			conn.commit();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (is != null) { is.close(); }
			} catch (Exception e) {}
			try {
				if (conn != null) { conn.close(); }
			} catch (Exception e) {}
		}
	}

	/**
	 * 處理未正確完成的轉檔流程
	 */
	private void convertLogCheck() {
		List<ConvertLog> list = ConvertLogService.findAll();
		for(ConvertLog convertLog : list) {
			if (convertLog.getCompleteTime() == null) {
				ConvertManager.instance.add(convertLog.getFname());
			} else {
				Callback.send(convertLog.getFname());
			}
		}
	}
}
