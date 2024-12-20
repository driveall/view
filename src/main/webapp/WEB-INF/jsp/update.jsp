<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW update profile</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center560">
    <h1>Update account ${account.login}:</h1>
    <form method="post" action="/update">
        <h2 class="blue">Update pasword: <input type="password" name="pass" class="field200"> </h2>
        <h2 class="blue">Enter again: <input type="password" name="pass2" class="field200"> </h2>
        <h2 class="blue">Update email(${account.email}): <input type="text" name="email" class="field200"> </h2>
        <h2 class="blue">Update phone(${account.phone}): <input type="text" name="phone" class="field200"> </h2>
        <input type="hidden" name="login" value="${account.login}">
        <input type="submit" value="ok" class="btn200">
    </form>
    <form action="/success">
        <input type="submit" value="Back" class="btn200">
    </form>
    <br/>
    <form action="/delete" method="post">
        <input type="hidden" name="login" value="${account.login}">
        <input type="submit" value="Delete" class="btn350" onclick="change()">
    </form>
    <script>
        function change(){
            alert();
        }
    </script>
</div>
</body>
</html>