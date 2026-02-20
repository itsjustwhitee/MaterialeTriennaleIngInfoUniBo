<%@ page language="java" contentType="text/html;"
	pageEncoding="ISO-8859-1"%>
<%@page import="beans.*"%>
<% response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); %>
<% response.setHeader("Pragma", "no-cache"); %>
<% response.setDateHeader("Expires", 0); %>
<html>
   <head>
		<link type="text/css" href="styles/general.css" rel="stylesheet"></link>
		<link rel="preconnect" href="https://fonts.googleapis.com">
    	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    	
   </head>
   <body>
	   <div class="container" style="display: flex; align-items: center; flex-direction: column; justify-content: center;"> 
	   	   <h1>Pagina di Login</h1>    
			<form action="login" method="POST">
				<input type="hidden" value="login" name="action">
				<input name ="username" type="text" placeholder="Nome utente" class="textfield" style="display: block; text-align: center;" required>
				<input name="password" type="password" class="textfield" placeholder="Password" style="display: block; text-align: center;" required>
				<button type="submit" class="bottoni" style="display: block; width: 95%;">Accedi</button>
				<button type="button" class="bottoni" style="display: block; width: 95%;" onclick="window.location.href='registrazione.jsp'">Registrati</button>
			</form>
			<%if(session.getAttribute("error") !=null){ 
					int error = (int) session.getAttribute("error");
					switch (error){ 
					case 2://Utente non registrato%>
						<p id="errorP" style="text-align:center; color:red;">Non � presente nessun utente con questo Username</p>
						
					<%break;
					case 3: //Password errata%>
					<p id="errorP" style="text-align:center; color:red;">Password errata</p>
					<%break;
					case 4: //Logout avvenuto con successo %>
					<p id="errorP" style="text-align:center; color:green;">Logout avvenuto con successo</p>
					<%break;
					}
						session.removeAttribute("error");
				}
					 %>
	   </div>
	<body>
</html>