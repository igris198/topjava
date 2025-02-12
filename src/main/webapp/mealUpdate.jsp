<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meal</title>
    <style>
        label {
            display: inline-block;
            width: 150px;
            margin-bottom: 10px;
        }
        input {
            width: 200px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${meal.id == null ? 'Add meal' : 'Edit meal'}</h2>
<form name="mealUpdate" method="post" action="meals">
    <input type="hidden" name="id" value=${meal.id}>
    <label for="datetime">DateTime: </label>
    <input type="datetime-local" name = "datetime" id = "datetime" value=${meal.dateTime}>
    <br>
    <label for="description">Description: </label>
    <input type="text" name = "description" id = "description" value=${meal.description}>
    <br>
    <label for="calories">Calories: </label>
    <input type="number" name = "calories" id = "calories" value=${meal.calories}>
    <br>
    <button type="submit">Save</button>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>