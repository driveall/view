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
    <h1>${msg.get('yourLogin')} ${account.login}</h1>
    <h4>${msg.get('yourEmail')}: ${account.email}; ${msg.get('phone')}: ${account.phone}</h4>
    <table>
        <tr>
            <td>
                <form action="/update" method="get">
                    <input type="submit" value="${msg.get('update')}" class="btn-half100" >
                </form>
            </td>
            <td>
                <form action="/unlogin" method="post">
                    <input type="submit" value="${msg.get('unlogin')}" class="btn-half100" >
                </form>
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <td class="width-300">
                <h3 title="${playersOnline}"> ${msg.get('playersOnline')}: ${onlineCount}</h3>
            </td>
            <td class="width-300">
                <form action="/accounts" method="get">
                    <input type="submit" value="${msg.get('players')}" class="btn-half60" >
                </form>
            </td>
        </tr>
    </table>


    <h2>${msg.get('money')}: ${account.money}, ${msg.get('level')}: ${account.level}, ${msg.get('points')}: ${account.points}</h2>
    <table>
        <tr>
            <td class="width-300">
                <h2>${msg.get('youHave')} ${account.storage.size()} ${msg.get('items')}</h2>
            </td>
            <td class="width-300">
                <form action="/shop" method="get">
                    <input type="submit" value="${msg.get('shop')}" class="btn-half60" >
                </form>
            </td>
        </tr>
    </table>
    <form action="/wear" method="get">
        <input type="submit" value="${msg.get('wear')}" class="btn100" >
    </form>
    <br/>
    <form action="/battle/bot/start" method="post">
        <input type="submit" value="${msg.get('battleWithBot')}" class="btn150" >
    </form>
    <form action="/battle/start" method="post">
        <input type="submit" value="${msg.get('battle')} 1x1" class="btn150" >
    </form>
</div>
</body>
</html>