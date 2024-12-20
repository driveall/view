<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW BATTLE</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center700">

    <h1>BATTLE OPPONENT:</h1>
    <h2 class="blue">Name: ${battle.teamTwo.stream().findFirst().get().id}</h2>
    <h2 class="blue">Health: ${battle.teamTwo.stream().findFirst().get().health}</h2>
    <h2 class="blue">Damage: ${battle.teamTwo.stream().findFirst().get().damage}</h2>
    <h2 class="blue">Armor: ${battle.teamTwo.stream().findFirst().get().armor}</h2>

    <br/>

    <h1>YOU:</h1>
    <h2 class="blue">Name: ${battle.teamOne.stream().findFirst().get().id}</h2>
    <h2 class="blue">Health: ${battle.teamOne.stream().findFirst().get().health}</h2>
    <h2 class="blue">Damage: ${battle.teamOne.stream().findFirst().get().damage}</h2>
    <h2 class="blue">Armor: ${battle.teamOne.stream().findFirst().get().armor}</h2>

    <br/>


    <c:if test="${!battle.battleFinished}">
    <form action="/move" method="post">
        <h2 class="blue">Attack:</h2>
        <input type="hidden" name="opponent" value="${battle.teamTwo.stream().findFirst().get().id}" />
        <h2>
            <label class="blue" for="left">Left</label>
            <input type="radio" name="attack" value="Left" id="left" checked />
            <label class="blue" for="center">Center</label>
            <input type="radio" name="attack" value="Center" id="center" />
            <label class="blue" for="right">Right</label>
            <input type="radio" name="attack" value="Right" id="right"/>
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
        <c:if test="${battle.teamOneFinish}">
            <input type="submit" value="UPDATE" class="btn350" />
        </c:if>
        <c:if test="${!battle.teamOneFinish}">
            <input type="submit" value="DO MOVE ${battle.move}" class="btn350" />
        </c:if>
    </form>
    </c:if>
    <c:if test="${battle.battleFinished}">
        <form action="/success" method="get">
            <input type="submit" value="Back" class="btn350" >
        </form>
    </c:if>
</div>
</body>
</html>