<%@ page session="true" %>
<%@ page import="beans.*" %>
<%@ page import="java.util.HashMap" %>
<%	//gestisce accessi indesiderati di utenti non loggati
	String username = (String) session.getAttribute("username");
	HashMap<String, User> users = (HashMap<String, User>) application.getAttribute("users"); 
    
	if (username == null || username.isBlank() || users == null || !users.containsKey(username) 
			|| users.get(username).isExpired() || !session.getId().equals(users.get(username).getSessionId()))
        response.sendRedirect("login.jsp");
%>
<html>
   <head>
      <title>Home</title>
		<link type="text/css" href="styles/default.css" rel="stylesheet"></link>
		<script src="./scripts/login.js"></script>
		<script src="./scripts/handler.js"></script>
   </head>

    <body>
	    <h1>Welcome ${sessionScope.username}!<br> <span style="font-size: 0.8em;">Group: ${sessionScope.group}</span></h1>
	    <button id="logout" onclick="logout(event)">Logout</button>
		<div id="app">
            <div class="left">
            	
            </div>
            <div class="separator"></div>
           	<div class="right">
           		
			</div>
        </div>
    </body>
</html>