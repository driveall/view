<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW</title>
    <link rel="icon" type="image/x-icon" href="https://i.postimg.cc/RhMgY41S/daw.png" />
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div>
    <div class="center-index">
        <h1>DAW</h1>
        <h2>${msg.get('online')}: ${onlineCount}</h2>
        <form action="/login">
            <input type="submit" value="${msg.get('login')}" class="btn200" />
        </form>

        <form action="/register">
            <input type="submit" value="${msg.get('register')}" class="btn200" />
        </form>
        <table>
            <tr>
                <td class="width-300">
                    <form action="/lang" method="post">
                        <input type="hidden" name="lng" value="ua">
                        <input type="submit" value="Мова" class="btn-half60" />
                    </form>
                </td>
                <td class="width-300">
                    <form action="/lang" method="post">
                        <input type="hidden" name="lng" value="us">
                        <input type="submit" value="English" class="btn-half60" />
                    </form>
                </td>
            </tr>
        </table>


    </div>
</div>
</body>
</html>