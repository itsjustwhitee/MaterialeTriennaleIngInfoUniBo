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

    <body onload="loadPage()">
	    <h1>Welcome ${sessionScope.username}!<br> <span style="font-size: 0.8em;">Group: ${sessionScope.group}</span></h1>
	    <button id="logout" onclick="logout(event)">Logout</button>
		<div id="app" class="container">
            <div class="left" id="itemsContainer">
            	<h1>Available items:</h1>
            	<table id="items">
           		</table>
            </div>
           	<div class="right" id="cartContainer">
           		<h1>Cart:</h1>
           		<table id="cart">
           		</table>
           		<button id="checkout" onclick="checkout(event)">Checkout</button>
			</div>
        </div>
    </body>
</html>