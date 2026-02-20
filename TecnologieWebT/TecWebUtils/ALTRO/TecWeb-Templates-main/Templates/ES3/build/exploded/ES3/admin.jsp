<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true"%>
<%@ page import="it.Servlet"%>
<%@ page import="java.util.*"%>
<%@ page import="beans.Utente" %>

<html>
<head>
    <title>Admin Page</title>

</head>
<body>
<h2>Pagina di amministrazione</h2>

<%
    //non si dovrebbe fare perché le password vengono passate in chiaro. Si dovrebbe interrogare un database con una query e fare l'encoding dei dati
    String username = "";
    String password = "";
    username = request.getParameter("username");
    password = request.getParameter("password");
    if (username.equals("admin") && password.equals("admin")) {
        List<Utente> utenti = (List<Utente>) application.getAttribute("utenti");
        for (Utente utente : utenti) {
            out.print("<h3>Utente: "+utente.getUsername()+" sessione: "+utente.getSession()+"</h3>");
            out.print("<br/>");
        }
    }

%>

</body>
</html>
