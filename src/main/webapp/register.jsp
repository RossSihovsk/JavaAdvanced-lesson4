<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Форма регистрации</title>
</head>

<body>


    <h1>Форма регистрации</h1>
    <form action="registering" method="post">
        <input name="firstName" type="text" placeholder="Імя"><br><br>
        <input name="lastName" type="text" placeholder="Фамілія"><br><br>
        <input name="email" type="text" placeholder="Логін"><br><br>
        <input name="password" type="password" placeholder="Пароль"><br><br>
        <input name="accessLevel" type="radio" id="user" value="user" checked>
        <label for="user">Користувач</label><br>
        <input name="accessLevel" type="radio" id="admin" value="admin">
        <label for="admin">Адміністратор</label><br><br>
        <input type="submit" value="Відправити">
    </form>


</body>

</html>
