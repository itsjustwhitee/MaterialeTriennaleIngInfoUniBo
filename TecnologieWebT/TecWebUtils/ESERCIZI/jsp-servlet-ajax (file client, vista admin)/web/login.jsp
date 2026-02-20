
<%@ page session="true" %>
<%@ page import="beans.*" %>
<%@ page import="java.util.HashMap" %>
<%	//gestisce accessi di utenti giá loggati
	String message = "";

	synchronized (application)
	{
		String username = (String) session.getAttribute("username");
		HashMap<String, User> users = (HashMap<String, User>) application.getAttribute("users");
		
		if (username != null && users != null && users.containsKey(username) 
				&& !users.get(username).isExpired() && session.getId().equals(users.get(username).getSessionId()))
	        response.sendRedirect("home.jsp");
	}
%>
<script> fetch('Counter', {method: 'GET'});</script>

<html>
   <head>
      <title>Login</title>
		<link type="text/css" href="styles/login.css" rel="stylesheet"></link>
   </head>

    <body>
    	<div id=loginGUI>
            <div class="left">
            	<h2>Admin login</h2>
                <form name="loginForm" id="loginForm" method="post" action="Login?act=login">
                    <table>
                    	<tr>
	                    	<td>Username:</td>
	                    </tr>
	                    <tr>
	                    	<td><input type="text" name="username" required/></td>
	                    </tr>
	                    <tr>
	                    	<td>Password:</td>
	                    </tr>
	                    <tr>
	                    	<td>
		                    	<input type="password" name="password" required/>
		                    	<br>
		                    	<span class="err">${loginError}</span>
	                    	</td>
	                    </tr>
                    </table>
                    <input type="submit" value="Login" />
                </form>
            </div>
            <div class="separator"></div>
           	<div class="right">
           		<h2 style="color: orange;">Enter as Guest</h2>
                <form name="guestForm" id="guestForm" method="post" action="Login?act=guest">
                    <input type="submit" value="Guest" style="background-color: darkgoldenrod;"/>
                </form>  
			</div>
        </div>
    </body>
</html>
