<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 07.10.14
  Time: 21:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<form id="myForm" action="processSite" method="post">
    <span style="color:#ff0000">${errorMassage}</span><br/>
    Адрес сайта: <input type="text" name="url" />
    <input type="submit" value="Проверить сайт.">
</form>
</body>
</html>
