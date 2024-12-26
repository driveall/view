<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW shop</title>
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
                <img src="${i.image}" height="110px" width="140px" />
                <h3>${i.price}$ --- ${i.points}</h3>
                <form action="/sell" method="post">
                    <input type="hidden" name="itemId" value="${i.id}" >
                    <input type="submit" value="${msg.get('sell')}" class="btn-quarter60" >
                </form>
            </td>
            </c:forEach>
        </tr>
    </table>
    <h2>${msg.get('money')}: ${account.money}</h2>
    <h1>${msg.get('shop')}:</h1>
    <table>
        <tr>
            <c:forEach items="${stuff}" var="i">
            <td class="card">
                <h3>${i.type}</h3>
                <h3>${i.name}</h3>
                <img src="${i.image}" height="110px" width="140px" />
                <h3>${i.price}$ --- ${i.points}</h3>
                <form action="/buy" method="post">
                    <input type="hidden" name="itemId" value="${i.id}" >
                    <input type="submit" value="${msg.get('buy')}" class="btn-quarter60" >
                </form>
            </td>
            </c:forEach>
        </tr>
    </table>

    <form action="/success" method="get">
        <input type="submit" value="${msg.get('back')}" class="btn150" >
    </form>
</div>
</body>
</html>