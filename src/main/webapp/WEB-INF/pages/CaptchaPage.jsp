<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 08.11.14
  Time: 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Captcha</title>
</head>
<body>

${message}
<br>

<img src="/captcha/image" border="1">
<br>
<br>
<form action="/captcha/passCaptcha" method="post">
    <input type="hidden" name="redirectTo" value="${redirectTo}" />
    <input type="text" name="captchaValue" size="12" />
    <input type="submit" name="submit" value="Отправить" />
</form>

</body>
</html>
