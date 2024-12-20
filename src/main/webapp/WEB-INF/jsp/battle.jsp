<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW BATTLE</title>
    <meta charset="UTF-8">
    <style>
        <%@include file="/WEB-INF/style/styles.css"%>
    </style>
</head>
<body>
<div class="center700">
    <c:set var="player" value="${battle.getPlayer(battle.playerId)}" />
    <c:set var="opponent" value="${battle.getOpponentForSinglePlayerBattle(battle.playerId)}" />
    <h1>BATTLE OPPONENT:</h1>
    <h2 class="blue">Name: ${opponent.id}</h2>
    <h2 class="blue">Health: ${opponent.health}</h2>
    <h2 class="blue">Damage: ${opponent.damage}</h2>
    <h2 class="blue">Armor: ${opponent.armor}</h2>

    <br/>

    <h1>YOU:</h1>
    <h2 class="blue">Name: ${player.id}</h2>
    <h2 class="blue">Health: ${player.health}</h2>
    <h2 class="blue">Damage: ${player.damage}</h2>
    <h2 class="blue">Armor: ${player.armor}</h2>
    <br/>

    <c:if test="${!player.moveFinished}">
        <form action="/battle/move" method="post">
            <h2 class="blue">Attack:</h2>
            <input type="hidden" name="opponent" value="${battle.teamTwo.stream().findFirst().get().id}" />
            <h2>
                <label class="blue">Left</label>
                <input type="radio" name="attack" value="Left" checked />
                <label class="blue">Center</label>
                <input type="radio" name="attack" value="Center" />
                <label class="blue">Right</label>
                <input type="radio" name="attack" value="Right" />
            </h2>
            <h2 class="blue">Defence:</h2>
            <h2>
                <label class="blue">Left</label>
                <input type="radio" name="defence" value="Left" checked />
                <label class="blue">Center</label>
                <input type="radio" name="defence" value="Center" />
                <label class="blue">Right</label>
                <input type="radio" name="defence" value="Right" />
            </h2>
            <input type="submit" value="DO MOVE ${battle.move}" class="btn350" />
        </form>
    </c:if>
    <c:if test="${player.moveFinished}">
        <c:if test="${battle.battleFinished}">
            <form action="/success" method="get">
                <input type="submit" value="Back" class="btn350" >
            </form>
        </c:if>
        <c:if test="${!battle.battleFinished}">
            <form action="/battle/move" method="post">
                <input type="submit" value="Update" class="btn350" >
            </form>
        </c:if>
    </c:if>
</div>
</body>
</html>