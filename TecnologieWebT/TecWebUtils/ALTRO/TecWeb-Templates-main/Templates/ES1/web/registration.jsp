<%@ page session="true" %>
<%@ page import="it.Login" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Registration</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
</head>
<body>
<div class="login-form">
    <form action="Registration" method="post">
        <h2 class="text-center">Registrazione</h2>
        <div class="form-group">
            <input type="text" class="form-control" placeholder="Username" required="required" name="username">
        </div>
        <div class="form-group">
            <input type="password" class="form-control" placeholder="Password" required="required" name="password">
        </div>
        <!---
        <div class="form-group">
            <input type="group" class="form-control" placeholder="Group" required="required" name="group">
        </div>
        --->
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block">Registrati</button>
            <a href=<%=request.getContextPath()%>/login.jsp>Vai a Login</a>
        </div>
    </form>
</div>

</body>
</html>
