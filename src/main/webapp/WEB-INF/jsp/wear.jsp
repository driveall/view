<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW wear</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center-main">
    <h1>Your items, ${account.login}:</h1>
    <table>
        <tr>
            <c:forEach items="${account.storage}" var="i">
            <td class="width-150">
                <h3>${i.name}</h3>
                <h3>${i.type} ${i.points}</h3>
                <form action="/wear" method="post">
                    <input type="hidden" name="itemId" value="${i.id}" >
                    <input type="submit" value="Wear" class="btn-quarter60" >
                </form>
            </td>
            </c:forEach>
        </tr>
    </table>

    <h1>Your wear:</h1>
    <h2 class="blue">Money ${account.money}</h2>
    <table>
        <tr>
            <td class="width-150">
                <form action="/unwear" method="post">
                    <span><h3 class="blue">Head: ${account.head.name}</h3></span>
                    <input type="hidden" name="itemId" value="${account.head.id}" >
                    <input type="submit" value="Throw" class="btn-quarter60" >
                </form>
            </td>
            <td class="width-150">
                <form action="/unwear" method="post">
                    <span><h3 class="blue">Body: ${account.body.name}</h3></span>
                    <input type="hidden" name="itemId" value="${account.body.id}" >
                    <input type="submit" value="Throw" class="btn-quarter60" >
                </form>
            </td>
            <td class="width-150">
                <form action="/unwear" method="post">
                    <span><h3 class="blue">Legs: ${account.legs.name}</h3></span>
                    <input type="hidden" name="itemId" value="${account.legs.id}" >
                    <input type="submit" value="Throw" class="btn-quarter60" >
                </form>
            </td>
            <td class="width-150">
                <form action="/unwear" method="post">
                    <span><h3 class="blue">Weapon: ${account.weapon.name}</h3></span>
                    <input type="hidden" name="itemId" value="${account.weapon.id}" >
                    <input type="submit" value="Throw" class="btn-quarter60" >
                </form>
            </td>
        </tr>
    </table>

    <form action="/success" method="get">
        <input type="submit" value="Back" class="btn200" >
    </form>
</div>
</body>
</html>