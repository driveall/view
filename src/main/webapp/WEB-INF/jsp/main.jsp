<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW main page</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center700">
    <h1>Your login is ${account.login}</h1>
    <h2 class="blue">Your email is ${account.email}</h2>
    <h2 class="blue">Your phone is ${account.phone}</h2>
    <form action="/update" method="get">
        <input type="submit" value="Update" class="btn350" >
    </form>
    <form action="/delete" method="post">
        <input type="hidden" name="login" value="${account.login}">
        <input type="submit" value="Delete" class="btn350" onclick="change()">
    </form>

    <form action="/unlogin" method="post">
        <input type="submit" value="Unlogin" class="btn350" >
    </form>
    <h2 class="blue">You have ${account.storage.size()} items</h2>
    <h2 class="blue">Money ${account.money}</h2>
    <h2 class="blue">Head: ${account.head.name}</h2>
    <h2 class="blue">Body: ${account.body.name}</h2>
    <h2 class="blue">Legs: ${account.legs.name}</h2>
    <h2 class="blue">Weapon: ${account.weapon.name}</h2>
    <form action="/wear" method="get">
        <input type="submit" value="Wear" class="btn350" >
    </form>
    <form action="/shop" method="get">
        <input type="submit" value="To Shop" class="btn350" >
    </form>
    <script>
        function change(){
            alert();
        }
    </script>
</div>
</body>
</html>