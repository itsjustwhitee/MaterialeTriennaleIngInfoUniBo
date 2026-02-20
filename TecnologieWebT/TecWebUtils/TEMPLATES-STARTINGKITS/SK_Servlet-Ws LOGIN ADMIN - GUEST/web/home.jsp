<%@ page session="true" %>
<%@ page import="beans.*" %>
<%@ page import="java.util.HashMap" %>
<%	//se accede un utente non autenticato viene settato come guest
	String username = (String) session.getAttribute("username");
	if (username == null || username.isBlank())
	{
		session.setAttribute("username", "guest");
		username = "guest";
	}
%>
<html>
   <head>
      <title>Home</title>
		<link type="text/css" href="styles/default.css" rel="stylesheet"></link>
		<script src="./scripts/login.js"></script>
		<script src="./scripts/handler.js"></script>
   </head>

    <body>
	    <h1>Welcome ${sessionScope.username}!</h1>
	    <br>
		<div id="app" class="container">
		
			<%
				if(username.equals("guest"))
				{%>
					<button id="login"  class="left" onclick="login(event)">Login</button>
					<br>
					<div id="guestView" class="onDark">
					</div>
			<%	} 
				else
				{%>
					<button id="logout" onclick="logout(event)">Logout</button>
					<br>
					<div id="adminView" class="onDark">
					</div>
			<%	} %>
        </div>
    </body>
</html>