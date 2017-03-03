package com.dtc.g24.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dtc.g24.server.jooq.tables.pojos.ConvertLog;

@WebServlet(urlPatterns = "/convert")
public class ConvertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding(StandardCharsets.UTF_8.name());
		String fname = req.getParameter("fname");
		ConvertLog convertLog = ConvertLogService.findByFname(fname);

		if (convertLog == null) {
			//不存在，先建立 ConvertLog
			ConvertLogService.create(fname);
			ConvertManager.instance.add(fname);
		} else if (convertLog.getCompleteTime() != null) {
			//已完成，執行 callback
			Callback.send(fname);
		}
	}
}
