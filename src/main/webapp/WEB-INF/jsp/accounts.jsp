<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW accounts</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center-main">
    <h1>All accounts:</h1>
    <form action="/success" method="get">
        <input type="submit" value="Back" class="btn150" >
    </form>
    <table>
        <c:forEach items="${accounts}" var="i">
        <tr>
            <td class="width-300">
                <h3>Name: ${i.login}</h3>
            </td>
            <td class="width-300">
                <h3>Level: ${i.level}</h3>
            </td>
        </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>