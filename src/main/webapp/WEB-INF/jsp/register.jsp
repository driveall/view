<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW register</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center-register">
    <h1>${msg.get('registration')}:</h1>
    <form method="post" action="/register">
        <h2 class="red">${msg.get('createLogin')}: <input type="text" name="login" class="field200"> </h2>
        <h2 class="red">${msg.get('createPassword')}: <input type="password" name="pass" class="field200"> </h2>
        <h2 class="red">${msg.get('passwordAgain')}: <input type="password" name="pass2" class="field200"> </h2>
        <h2 class="blue">${msg.get('enterEmail')}: <input type="text" name="email" class="field200"> </h2>
        <h2 class="blue">${msg.get('enterPhone')}: <input type="text" name="phone" class="field200"> </h2>
        <input type="submit" value="${msg.get('register')}" class="btn150">
    </form>

    <form action="/index">
        <input type="submit" value="${msg.get('back')}" class="btn150">
    </form>

</div>
</body>
</html>