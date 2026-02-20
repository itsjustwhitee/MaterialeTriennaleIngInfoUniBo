<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true"%>
<%@ page import="it.Servlet"%>
<%@ page import="java.util.*"%>
<html>
<head>
    <title>Esercizio 1</title>
    <link type="text/css" href="styles/default.css" rel="stylesheet">
    <script type="text/javascript" src="scripts/utils.js"></script>
    <script type="text/javascript" src="scripts/scripts.js"></script>
    <h2>Benvenuto all' acquisto di gruppo!</h2>
</head>
<body>
<%  //Evito di accedere con una get diretta alla pagina
    if(session.getAttribute("logged")==null || session.getAttribute("logged").equals(false))
        response.sendRedirect(request.getContextPath()+"/login.jsp");
%>


<input type="text" id="txt" onchange="calculate()">
<div id="result">
</div>



<!---
<form action="<//%=request.getContextPath()%>/Servlet" method="post">
    <input type="text" id="test" name="test" disabled="yes" value="Clicca il bottone"><br>
    <button type="submit">Termina</button>
</form>
--->
</body>
</html>

