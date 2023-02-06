<%@ page import="java.io.*, java.util.*, za.co.tms.util.*" %>

<html>
<head>
<title>Page Redirection</title>
</head>
<body>
<%
		String command = new String(SuperGlobal.HOST_NAME + "/command/home");
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response.setHeader("Location", command);
%>
</body>
</html>