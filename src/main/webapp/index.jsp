<%@page import="com.dtc.g24.server.ConvertManager"%>
<%@page import="com.dtc.g24.server.G24Setting"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
Shared Folder: <%=G24Setting.sharedFolder() %>
<br/>
Callback URL: <%=G24Setting.callbackUrl() %>
<form action="convert" method="post">
	<input type="text" name="fname">
	<input type="submit">
</form>
</body>
</html>