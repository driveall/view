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
<div class="center-main">
    <h1>Your login is ${account.login}</h1>
    <h4>Your email: ${account.email}; phone: ${account.phone}</h4>
    <table>
        <tr>
            <td>
                <form action="/update" method="get">
                    <input type="submit" value="Update" class="btn-half100" >
                </form>
            </td>
            <td>
                <form action="/unlogin" method="post">
                    <input type="submit" value="Unlogin" class="btn-half100" >
                </form>
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <td class="width-300">
                <h3 title="${playersOnline}"> Players online: ${onlineCount}</h3>
            </td>
            <td class="width-300">
                <form action="/accounts" method="get">
                    <input type="submit" value="Accounts" class="btn-half60" >
                </form>
            </td>
        </tr>
    </table>


    <h2>Money: ${account.money}, Level: ${account.level}, Points: ${account.points}</h2>
    <table>
        <tr>
            <td class="width-300">
                <h2>You have ${account.storage.size()} items</h2>
            </td>
            <td class="width-300">
                <form action="/shop" method="get">
                    <input type="submit" value="Shop" class="btn-half60" >
                </form>
            </td>
        </tr>
    </table>
    <form action="/wear" method="get">
        <input type="submit" value="Wear" class="btn100" >
    </form>
    <br/>
    <form action="/battle/bot/start" method="post">
        <input type="submit" value="Battle with Bot" class="btn150" >
    </form>
    <form action="/battle/start" method="post">
        <input type="submit" value="Battle 1x1" class="btn150" >
    </form>
</div>
</body>
</html>