<%@page import="it.ecommerce.servlet.common.language.ManageLanguages"%>
<%@page import="it.ecommerce.servlet.common.brands.ManageBrand"%>
<%@page import="it.ecommerce.util.constants.Common"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@include file="props.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=request.getAttribute(Common.PAGE_TITLE)%></title>
<script type="text/javascript" src="js/common/message.js"></script>
<script type="text/javascript" src="js/common/radio.js"></script>
<script type="text/javascript" src="js/common/button.js"></script>
<script type="text/javascript" src="js/common/form.js"></script>
<script type="text/javascript" src="js/common/validate.js"></script>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<%
String msg = (String)request.getAttribute("msg");
String servletName=(String)request.getAttribute(Common.SERVLET_NAME);
%>
<body onload="msg('<%=msg%>')">
<jsp:include page="form/form.jsp"/>
<jsp:include page="menu/headerMenu.jsp"/>
<br>
<%
if(ManageBrand.class.getName().equals(servletName)){
%>
<jsp:include page="../manage/brands/root.jsp"/>
<%
}else if(ManageLanguages.class.getName().equals(servletName)){
%>
<jsp:include page="../manage/language/language.jsp"/>
<%
}
%>
</body>
</html>