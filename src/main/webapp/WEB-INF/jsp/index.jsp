<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div>
    <div class="center-index">
        <h1>DAW</h1>
        <h2>Online: ${onlineCount}</h2>
        <form action="/login">
            <input style="font-" type="submit" value="Login" class="btn200" />
        </form>

        <form action="/register">
            <input type="submit" value="Register" class="btn200" />
        </form>
    </div>
</div>
</body>
</html>