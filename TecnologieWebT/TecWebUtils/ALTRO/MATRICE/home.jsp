<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link type="text/css" href="./styles/default.css" rel="stylesheet"></link>
<script type="text/javascript" src="./scripts/function.js"></script>
</head>
<body onLoad="init();">
<p> Inserisci un numero per ogni elemento della matrice </p><br/>
<div>
<%
	for(int i=0;i<6;i++){
		%>
		<span>
		<%for(int j=0;j<6;j++){
			String temp=i+" "+j;
			%>
			<input type="text" value="" name="<%=temp%>" id="<%=temp%>" onKeyDown='validate(this);' onKeyUp='validate(this);' onKeyPress='validate(this);'/>
			<%
		} %> </span><br/><%	
	}
%>
<input type="button" value="Invia" onClick="send();"/>
</div>
</body>
</html>