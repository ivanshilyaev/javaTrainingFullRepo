<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/start.css"/>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <title>Авторизация</title>
</head>

<body>
<div class="wrapper fadeInDown">
    <div id="formContent">
        <!-- Tabs Titles -->

        <!-- Icon -->
        <div class="fadeIn first">
            <img src="http://icons.iconarchive.com/icons/graphicloads/100-flat/256/home-icon.png" id="icon"
                 alt="User Icon"/>
        </div>

        <!-- Login Form -->
        <c:url value="/login.html" var="loginUrl"/>
        <form name="loginForm" method="POST" action="${loginUrl}">
            <input type="text" id="login" class="fadeIn second" name="login" placeholder="Логин">
            <input type="text" id="password" class="fadeIn third" name="password" placeholder="Пароль">
            <input type="submit" class="fadeIn fourth" value="Войти">
        </form>
        ${sessionScope.message}
        <br>

        <!-- Remind Passowrd -->
        <div id="formFooter">
            <a class="underlineHover" href="#">Забыли пароль?</a>
        </div>

    </div>
</div>
</body>
</html>
