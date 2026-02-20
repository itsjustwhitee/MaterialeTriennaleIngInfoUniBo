<%@ page language="java" contentType="text/html;"
	pageEncoding="ISO-8859-1"%>
<%@page import="beans.*"%>
<% response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); %>
<% response.setHeader("Pragma", "no-cache"); %>
<% response.setDateHeader("Expires", 0); %>
<html>
   <head>
		<link type="text/css" href="styles/general.css" rel="stylesheet"></link>
   		<title>Cruciverba</title>
   		<style>
        table {
            border-collapse: collapse;
        }
        td {
            width: 40px;
            height: 40px;
            text-align: center;
            font-size: 24px;
            border: 2px solid black;
        }
        input {
            width: 100%;
            height: 100%;
            text-align: center;
            font-size: 24px;
            border: none;
        }
    </style>
   </head>
   <body>
	   <div class="container" style="display: flex; align-items: center; flex-direction: column; justify-content: center;">
	    <%if(session.getAttribute("login") != null && (Boolean) session.getAttribute("login") == true)
   		{
   			User u = (User) session.getAttribute("user"); %>
	   	   <form action="login" method="POST">
	   	   		<input type="hidden" value="logout" name="action">
		   	   <button class="bottoni" type="submit">Logout</button>
	   	   </form>
	   	   <h1>Ciao <%= u.getUsername() %>, questo e' il cruciverba condiviso</h1> 
	   	   <table id="cruciverba">
	   	   <pid id="errorP" style="text-align:center; color:red;"></p>
        <!-- Creiamo una griglia 10x10 -->
        <script type="text/javascript" src="./scripts/myFunctions.js"></script>
        <script>
            const dimensione = 10;
            const table = document.getElementById('cruciverba');
            for (let i = 0; i < dimensione; i++) {
                const row = document.createElement('tr');
                for (let j = 0; j < dimensione; j++) {
                    const cella = document.createElement('td');
                    const input = document.createElement('input');
                    input.setAttribute('maxlength', '1');
                    input.setAttribute('data-riga', i);
                    input.setAttribute('data-colonna', j);
                    input.addEventListener('input', function() {
                        inviaCarattere(this.value, i, j);
                    });
                    cella.appendChild(input);
                    row.appendChild(cella);
                }
                table.appendChild(row);
            }
            // Esegui l'aggiornamento della matrice ogni 2 secondi
            setInterval(aggiornaCruciverba, 2000);	
        </script>
	   	    <%} else { %>
	   		<h1>Pagina visibile dagli utenti che hanno effettuato il login</h1>
            <button type="button" class="bottoni" style="display: block; width: 20%;" onclick="window.location.href='loginPage.jsp'">Login</button>
	   <%} %>   
	   </div>
	<body>
</html>