<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW shop</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center-shop">
    <h1>Your items, ${account.login}:</h1>
    <table>
        <tr>
            <c:forEach items="${account.storage}" var="i">
            <td class="width-150">
                <h3>${i.name}</h3>
                <h3>${i.type} ${i.price}</h3>
                <form action="/sell" method="post">
                    <input type="hidden" name="itemId" value="${i.id}" >
                    <input type="submit" value="Sell" class="btn-quarter60" >
                </form>
            </td>
            </c:forEach>
        </tr>
    </table>

    <h1>Shop:</h1>
    <table>
        <tr>
            <c:forEach items="${stuff}" var="i">
            <td class="width-150">
                <h3>${i.name}</h3>
                <h3>${i.type} ${i.price}</h3>
                <form action="/buy" method="post">
                    <input type="hidden" name="itemId" value="${i.id}" >
                    <input type="submit" value="Buy" class="btn-quarter60" >
                </form>
            </td>
            </c:forEach>
        </tr>
    </table>

    <form action="/success" method="get">
        <input type="submit" value="Back" class="btn150" >
    </form>
</div>
</body>
</html>