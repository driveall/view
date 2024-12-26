<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW wear</title>
    <link rel="icon" type="image/x-icon" href="https://i.postimg.cc/RhMgY41S/daw.png" />
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center-shop">
    <h1>${msg.get('yourItems')}, ${account.login}:</h1>
    <table>
        <tr>
            <c:forEach items="${account.storage}" var="i">
            <td class="card">
                <h3>${i.type}</h3>
                <h3>${i.name}</h3>
                <h3>${i.price}$ --- ${i.points}</h3>
                <form action="/wear" method="post">
                    <input type="hidden" name="itemId" value="${i.id}" >
                    <input type="submit" value="${msg.get('wear')}" class="btn-quarter60" >
                </form>
            </td>
            </c:forEach>
        </tr>
    </table>

    <h1>${msg.get('yourWear')}:</h1>
    <table>
        <tr>
            <td class="card">
                <form action="/unwear" method="post">
                    <h3 class="blue">${msg.get('head')}:</h3>
                    <h3>${account.head.name}</h3>
                    <h3>${account.head.price}$ --- ${account.head.points}</h3>
                    <input type="hidden" name="itemId" value="${account.head.id}" >
                    <input type="submit" value="${msg.get('throw')}" class="btn-quarter60" >
                </form>
            </td>
            <td class="card">
                <form action="/unwear" method="post">
                    <h3 class="blue">${msg.get('body')}:</h3>
                    <h3>${account.body.name}</h3>
                    <h3>${account.body.price}$ --- ${account.body.points}</h3>
                    <input type="hidden" name="itemId" value="${account.body.id}" >
                    <input type="submit" value="${msg.get('throw')}" class="btn-quarter60" >
                </form>
            </td>
            <td class="width-150">
                <form action="/unwear" method="post">
                    <h3 class="blue">${msg.get('legs')}:</h3>
                    <h3>${account.legs.name}</h3>
                    <h3>${account.legs.price}$ --- ${account.legs.points}</h3>
                    <input type="hidden" name="itemId" value="${account.legs.id}" >
                    <input type="submit" value="${msg.get('throw')}" class="btn-quarter60" >
                </form>
            </td>
            <td class="width-150">
                <form action="/unwear" method="post">
                    <h3 class="blue">${msg.get('weapon')}:</h3>
                    <h3>${account.weapon.name}</h3>
                    <h3>${account.weapon.price}$ --- ${account.weapon.points}</h3>
                    <input type="hidden" name="itemId" value="${account.weapon.id}" >
                    <input type="submit" value="${msg.get('throw')}" class="btn-quarter60" >
                </form>
            </td>
        </tr>
    </table>

    <form action="/success" method="get">
        <input type="submit" value="${msg.get('back')}" class="btn200" >
    </form>
</div>
</body>
</html>