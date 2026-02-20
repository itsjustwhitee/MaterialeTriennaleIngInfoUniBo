<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true"%>
<%@ page import="it.Servlet"%>
<%@ page import="java.util.*"%>

<html>
<head>
    <title>Esercizio 3</title>
    <link type="text/css" href="styles/default.css" rel="stylesheet">
    <script type="text/javascript" src="scripts/utils.js"></script>
    <script type="text/javascript" src="scripts/scripts.js"></script>
    <script type="text/javascript" src="scripts/websocket.js"></script>
    <h2>Benvenuto!</h2>
</head>
<body>
<%
    if(session.getAttribute("logged")==null || session.getAttribute("logged").equals(false))
        response.sendRedirect(request.getContextPath()+"/login.jsp");
%>

<input type="text" id="numbers" name="numbers">
<div>
    <button id="register" name="register" onclick="handleMatch(this.id)">register</button>
    <button id="quit" name="quit" onclick="handleMatch(this.id)">quit</button>
    <button id="start" name="start" onclick="handleMatch(this.id)">start</button>
    <button id="extract" name="extract" onclick="myFunction()">Estrai!</button>
</div>

</body>
</html>

