<%@ page session="true" %>
<%@ page import="beans.*" %>
<%@ page import="java.util.HashMap" %>
<%	//gestisce accessi indesiderati di utenti non loggati
	String username = (String) session.getAttribute("username");
	HashMap<String, User> users = (HashMap<String, User>) application.getAttribute("users"); 
    
	if (username == null || session.getAttribute("priority") == null || username.isBlank() || users == null || !users.containsKey(username) 
			|| users.get(username).isExpired() || !session.getId().equals(users.get(username).getSessionId()))
        response.sendRedirect("login.jsp");
%>

<script>
	sessionStorage.setItem('priority', <%=session.getAttribute("priority")%>);
	sessionStorage.setItem('username', '<%=session.getAttribute("username")%>');
</script>

<html>
   <head>
      <title>Home</title>
		<link type="text/css" href="styles/default.css" rel="stylesheet"></link>
		<script src="./scripts/login.js"></script>
		<script src="./scripts/websocket.js"></script>
   </head>

    <body>
		    <h2 class="left">Welcome ${sessionScope.username}! </h2>
		    <br>
    	<div class="container">
    		
			<div id="app" class="left">
	            <form name="loginForm" id="loginForm" method="post">
	            	<input type="radio" name="service" value="rinnovo" id="rinnovo" onchange="handleRequest()"/>
	            		<label for="rinnovo">Rinnovo carta d'identit�</label>
	            	<br>
	            	<input type="radio" name="service" value="cambio" id="cambio" onchange="handleRequest()"/>
	            		<label for="rinnovo">Cambio residenza</label>
	            </form>
	        </div>
        </div>
        <button id="logout" onclick="logout(event)" class="right" spacing="5%">Logout</button>
    </body>
</html>