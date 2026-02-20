<%@ page session="true" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%	//gestisce accessi di utenti new
	synchronized (application)
	{
		String username = (String) session.getAttribute("username");
		
		if (session.isNew() || username == null || username.isBlank())
	        response.sendRedirect("login.jsp");
	}
%>

<html>
   <head>
      <title>Home</title>
		<link type="text/css" href="styles/default.css" rel="stylesheet"></link>
		<link type="text/css" href="styles/login.css" rel="stylesheet"></link>
		<script src="./scripts/login.js"></script>
		<script src="./scripts/handler.js"></script>
   </head>

    <body>
    <header class="clearfix">
    	<h1>Welcome ${sessionScope.username}!</h1>
    	<%	String username = (String) session.getAttribute("username");
    	
    		if (username == null || username.isEmpty())
    		{
    			%><script> window.location.replace('login.jsp'); </script><%
    			return;
    		}
    		
    		if(username.toLowerCase().equals("guest")) 
    		{//Vista guest/non admin
    	%>
    		<button id="login"  class="right" onclick="login(event)">Login</button><br>
    	<%
    		}
    		else
    		{
    	%>
    		<button id="logout" class="right" onclick="logout(event)">Logout</button><br>
    	<%
    		}%>
    	</header>
    	<br>
    	<div class="container">
    	<%
    		if(username.toLowerCase().equals("guest")) 
    		{//Vista guest/non admin
    	%>
    		<form name="fileForm" id="fileForm" onsubmit="return filesRequest()">
                    <table>
                    	<tr>
	                    	<td>File 1:</td>
	                    </tr>
	                    <tr>
	                    	<td><textarea id="file1" placeholder="Inserisci qui il contenuto del primo file" required></textarea></td>
	                    </tr>
	                    <tr>
	                    	<td>File 2:</td>
	                    </tr>
	                    <tr>
	                    	<td>
		                    	<textarea id="file2" placeholder="Inserisci qui il contenuto del secondo file" required></textarea>
	                    	</td>
	                    </tr>
                    </table>
                    <input type="submit" value="Invia" />
                </form>
				<div id="response"></div>
    	<%
    		}
    		else
    		{//Vista admin
    	%>
    		<h1>Resoconto operazioni del mese</h3>
    		<table>
                    	<tr>
	                    	<td>Totale caratteri processati:</td>
	                    	<td> ${applicationScope.totProcessed}</td>
	                    </tr>
	                    <tr>
	                    	<td>Totale caratteri minuscoli:</td>
	                    	<td> ${applicationScope.totLower}</td>
	                    </tr>
	                    <tr>
	                    	<td>Totale caratteri MAIUSCOLI:</td>
	                    	<td> ${applicationScope.totUpper}</td>
	                    </tr>
                    </table>
    	<%
    		}
    	%>
    	</div>
    </body>
</html>