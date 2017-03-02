package com.dtc.g24.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/convert")
public class ConvertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ConvertManager convertManager = new ConvertManager(
		G24Setting.sharedFolder(),
		G24Setting.converterPath()
	);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding(StandardCharsets.UTF_8.name());
		convertManager.add(req.getParameter("fname"));
	}
}
