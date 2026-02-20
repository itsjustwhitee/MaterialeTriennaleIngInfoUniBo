
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


<html>
   <head>
      <title>Login</title>
		<link type="text/css" href="styles/login.css" rel="stylesheet"></link>
   </head>

    <body>
    	<div id=loginGUI>
            <div class="left">
            	<h2>Login</h2>
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
           		<h2 style="color: orange;">Register</h2>
                <form name="registerForm" id="registerForm" method="post" action="Login?act=registration">
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
	                    	<td><input type="password" name="password" required/></td>
	                    </tr>
                    </table>
                    <input type="submit" value="Registration" style="background-color: darkgoldenrod;"/>
                </form>  
			</div>
        </div>
    </body>
</html>
