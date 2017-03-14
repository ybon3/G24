package com.dtc.g24.server;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;

import com.dtc.g24.server.dao.ConnectionFactory;
import com.dtc.g24.server.jooq.tables.daos.ConvertLogDao;
import com.dtc.g24.server.jooq.tables.pojos.ConvertLog;

public class ConvertLogService {
	/**
	 * 取得所有 ConvertLog
	 */
	public static List<ConvertLog> findAll() {
		ConvertLogDao dao = null;
		try {
			dao = getConvertLogDao();
			return dao.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(dao);
		}
		return null;
	}

	/**
	 * 以 fname 取得 ConvertLog
	 */
	public static ConvertLog findByFname(String fname) {
		ConvertLogDao dao = null;
		try {
			dao = getConvertLogDao();
			List<ConvertLog> list = dao.fetchByFname(fname);
			if (!list.isEmpty()) {
				return list.get(0);//理論上只會有一筆
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(dao);
		}
		return null;
	}

	/**
	 * 以 fname 建立 ConvertLog
	 */
	public static void create(String fname) {
		ConvertLog data = new ConvertLog();
		data.setFname(fname);
		data.setCreateTime(new Timestamp(new Date().getTime()));

		ConvertLogDao dao = null;
		try {
			dao = getConvertLogDao();
			dao.insert(data);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(dao);
		}
	}

	/**
	 * 更新 ConvertLog 的 CompleteTime
	 */
	public static void complete(String fname) {
		ConvertLogDao dao = null;
		try {
			dao = getConvertLogDao();
			List<ConvertLog> list = dao.fetchByFname(fname);
			if (!list.isEmpty()) {
				ConvertLog data = list.get(0);//理論上只會有一筆
				data.setCompleteTime(new Timestamp(new Date().getTime()));
				dao.update(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(dao);
		}
	}

	/**
	 * 刪除 ConvertLog
	 */
	public static void delete(String fname) {
		ConvertLogDao dao = null;
		try {
			dao = getConvertLogDao();
			dao.delete(dao.fetchByFname(fname));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(dao);
		}
	}

	private static ConvertLogDao getConvertLogDao() throws SQLException  {
		return new ConvertLogDao(new DefaultConfiguration()
			.set(ConnectionFactory.get())
			.derive(SQLDialect.H2));
	}

	private static void close(ConvertLogDao dao) {
		if (dao != null) {
			try {
				dao.configuration().connectionProvider().acquire().close();
			} catch (SQLException e) {}
		}
	}
}
