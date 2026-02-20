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
		<script src="./scripts/websocket.js"></script>
   </head>

    <body>
    	
		<div id="app" class="container">
			<img alt="wait" title="wait" src="images/wait.gif" class="left" width="10px" style="padding-right: 5px"/>
	    	<h2 class="right">Sii paziente ${sessionScope.username}...</h2><br>
            <span id="serviceStatus"></span>
        </div>
    </body>
</html>