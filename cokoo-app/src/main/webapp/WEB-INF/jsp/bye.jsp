<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bye</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
Bye!
<c:if test="${googleLogoutUrl !=null}">
    <p>

        You are logged out from the application, but still logged in with your Google ID.

        <a href="${googleLogoutUrl}">Google Logout</a>
    </p>
</c:if>

<p>
    <a href="/hello.do">Return to app</a>
</p>


</body>

</html>