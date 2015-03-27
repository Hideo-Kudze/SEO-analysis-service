<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 01.11.14
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
</head>
<body>

<c:choose>
    <c:when test="${report.status == 0}">
        Сайт не находится на обработке и его нету в базе даннхы.
        Сейчас вы будете перенаправленны на гланую страницу где сможете его добавить.

        <script type="text/javascript">
            function redirect() {
                setTimeout(function(){window.location = "/";}, 2700);
            }
            window.onload = redirect;
        </script>

    </c:when>
    <c:when test="${report.status == 1}">
        ${report}
    </c:when>
    <c:when test="${report.status == 2}">
        Сайт обрабатывается. Подождите пожлуйста.
        <script type="text/javascript">
            function reload() {
                setTimeout(function(){location.reload();}, 1500);
            }
            window.onload = reload;
        </script>
    </c:when>
</c:choose>



</body>
</html>
