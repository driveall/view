<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW BATTLE</title>
    <link rel="icon" type="image/x-icon" href="https://i.postimg.cc/RhMgY41S/daw.png" />
    <meta charset="UTF-8">
    <style>
        <%@include file="/WEB-INF/style/styles.css"%>
    </style>
</head>
<body>
<div class="center-main">
<c:if test="${!battle.started}">
    <h1>${msg.get('opponentSearching')}...</h1>
    <form action="/battle/move" method="post">
        <input type="submit" value="${msg.get('update')}" class="btn200" >
    </form>
    <form action="/battle/cancel" method="post">
        <input type="submit" value="${msg.get('cancelAndReturn')}" class="btn200" >
    </form>
</c:if>
<c:if test="${battle.started}">
    <c:set var="player" value="${battle.getPlayer(battle.playerId)}" />
    <c:set var="opponent" value="${battle.getOpponentForSinglePlayerBattle(battle.playerId)}" />
    <h1>${msg.get('battleOpponent')}:</h1>
    <h2>${msg.get('name')}: ${opponent.id}</h2>
    <h2>${msg.get('health')}: ${opponent.health}, ${msg.get('damage')}: ${opponent.damage}, ${msg.get('armor')}: ${opponent.armor}</h2>
    <br/>
    <h1>${msg.get('you')}:</h1>
    <h2>${msg.get('name')}: ${player.id}</h2>
    <h2>${msg.get('health')}: ${player.health}, ${msg.get('damage')}: ${player.damage}, ${msg.get('armor')}: ${player.armor}</h2>
    <br/>
    <c:if test="${!player.moveFinished}">
        <form action="/battle/move" method="post">
            <h1>${msg.get('attack')}:</h1>
            <input type="hidden" name="opponent" value="${opponent.id}" />
            <h2>
                <label class="blue">${msg.get('left')}</label>
                <input type="radio" name="attack" value="Left" checked />
                <label class="blue">${msg.get('center')}</label>
                <input type="radio" name="attack" value="Center" />
                <label class="blue">${msg.get('right')}</label>
                <input type="radio" name="attack" value="Right" />
            </h2>
            <h1>${msg.get('defence')}:</h1>
            <h2>
                <label class="blue">${msg.get('left')}</label>
                <input type="radio" name="defence" value="Left" checked />
                <label class="blue">${msg.get('center')}</label>
                <input type="radio" name="defence" value="Center" />
                <label class="blue">${msg.get('right')}</label>
                <input type="radio" name="defence" value="Right" />
            </h2>
            <input type="submit" value="${msg.get('doMove')} ${battle.move}" class="btn150" />
        </form>
    </c:if>
    <c:if test="${player.moveFinished}">
        <c:if test="${battle.battleFinished}">
            <form action="/success" method="get">
                <input type="submit" value="${msg.get('back')}" class="btn150" >
            </form>
        </c:if>
        <c:if test="${!battle.battleFinished}">
            <form action="/battle/move" method="post">
                <input type="submit" value="${msg.get('update')}" class="btn150" >
            </form>
        </c:if>
    </c:if>
</c:if>
</div>
</body>
</html>