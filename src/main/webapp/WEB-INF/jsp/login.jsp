<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW login</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center-login">
    <h1>Login:</h1>
    <form method="post" action="/login">
        <h2 class="red">Enter login: <input type="text" name="login" class="field200"></h2>
        <h2 class="red">Password: <input type="password" name="pass" class="field200"></h2>
        <input type="submit" value="Login" class="btn150">
    </form>
    <form action="/index">
        <input type="submit" value="Back" class="btn150">
    </form>
</div>
</body>
</html>