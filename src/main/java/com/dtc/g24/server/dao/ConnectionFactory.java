package com.dtc.g24.server.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.Driver;
import org.h2.jdbcx.JdbcConnectionPool;

import com.dtc.g24.server.G24Setting;

public class ConnectionFactory {
	private static JdbcConnectionPool pool;
	static {
		String path = G24Setting.dbPath().replace('\\', '/');
		Driver.load();
		pool = JdbcConnectionPool.create(
			"jdbc:h2:tcp://localhost/" + path + "/database/g24DB",
			G24Setting.dbUsername(),
			G24Setting.dbPassword()
		);
		pool.setMaxConnections(G24Setting.dbPoolMaxConnections());
		pool.setLoginTimeout(G24Setting.dbPoolLoginTimeout());
	}

	public static Connection get() throws SQLException {
		return pool.getConnection();
	}
}
