package com.dtc.g24.server;

import java.io.IOException;

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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		convertManager.add(req.getParameter("fname"));
	}
}
