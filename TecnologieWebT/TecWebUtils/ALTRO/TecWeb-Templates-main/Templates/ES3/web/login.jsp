<%@ page session="true" %>
<%@ page import="it.Login" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
    <meta charset="utf-8">
</head>
<body>
    <form action="Login" method="post">
        <br/>
            <input type="text" placeholder="Username" required="required" name="username">
        <br/>
        <input type="password" placeholder="Password" required="required" name="password">
        <br/>
        <button type="submit">Log in</button>
    </form>
</div>
</body>
</html>
