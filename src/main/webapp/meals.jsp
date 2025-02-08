<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="ru.javawebinar.topjava.util.DateUtil" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border="1" cellpadding="5">
<tr>
    <th>Date</th>
    <th>Description</th>
    <th>Calories</th>
</tr>
<c:forEach var = "meal" items="${mealsList}">
    <c:set var="style" value ="color: green;"/>
    <c:if test="${meal.excess}">
        <c:set var="style" value ="color: red;"/>
    </c:if>
    <tr style="${style}">
        <td><c:out value="${DateUtil.formatDateTime(meal.dateTime)}"/></td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
    </tr>
</c:forEach>
</table>
</body>
</html>