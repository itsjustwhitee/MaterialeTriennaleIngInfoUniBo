<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<% int righe = 5;
	int colonne = 5;%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Matrici</title>
<link type="text/css" href="./styles/default.css" rel="stylesheet"> </link>
<script type="text/javascript" src="./scripts/function.js"></script>
</head>
<body>
	<div id="matrice">
	<% for(int i = 0 ; i < righe ; i++){
		String nomeId="riga "+i;
		%>
		<div id="<%=nomeId%>">
		<span>
		<%
			for(int j = 0 ; j < colonne ; j++){
				String idCella = i + " " + j;
				%>
				<input type="text" 
				name="<%= idCella %>"
				id="<%= idCella %>"
				onKeyUp="validate(this);" />
			<%}
		%>
		</span>
		</div>
	<%} %>
	</div>
</body>
</html>