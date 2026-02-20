<%@ page language="java" contentType="text/html;"
	pageEncoding="UTF-8"%>
<%@page import="beans.*"%>

<html>
   <head>
   		<meta charset="UTF-8">
		<link type="text/css" href="styles/general.css" rel="stylesheet"></link>
		<link rel="preconnect" href="https://fonts.googleapis.com">
    	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    	<link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">
   		<script type="text/javascript" src="./scripts/myFunctions.js"></script>
   </head>
   <body>
	   <div class="container" style="display: flex; align-items: center; flex-direction: column; justify-content: center;"> 
	   <h1>Pagina di registrazione</h1>  
			<form action="SignUp" method="POST" name="registra">
				<input type="text" placeholder="Nome utente" name="Username" class="textfield" style="display: block; text-align: center;" onBlur="checkFields(this)" required>
				<input type="password" class="textfield" name="Pwd" placeholder="Password" style="display: block; text-align: center;" onBlur="checkFields(this)" required>
				<input type="password" class="textfield" name="ConfirmPwd" id="check" placeholder="ConfermaPassword" style="display: block; text-align: center;"
				  onBlur="checkPassword(this.form)" required>
				<button type="submit" class="bottoni" style="display: block; width: 95%;" id="signUpBtn">Registrati</button>
				<button type="button" class="bottoni" style="display: block; width: 95%;" onclick="window.location.href='loginPage.jsp'">Login</button>
			</form>
			<%if(session.getAttribute("error") !=null){ 
					int error = (int) session.getAttribute("error");
						switch (error){ //errore nella registrazione, username già registrato
						case 1:
						%>
							<p id="errorP" style="text-align:center; color:red;">Errore, l'utente con questo Username è già registrato</p>
							
						<%break;
						case -1://registrazione andata a buon fine%>
						<p id="errorP" style="text-align:center; color:green;">Registrazione andata a buon fine</p>
						<%break;
						}
					session.removeAttribute("error");		
				}else{ %>
				<p id="errorP" style="text-align:center; color:red;"></p>
			<%} 
			 %>
	   </div>
	<body>
</html>