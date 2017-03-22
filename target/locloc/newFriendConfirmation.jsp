<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>LocLoc</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/ie10-viewport-bug-workaround.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="myStyle.css">

</head>

<body>

<jsp:include page="WEB-INF/fragments/headerWithLogin.jspf"/>

<div class="container" style="width: 50%;padding-top: 100px;">


    <c:choose>
        <c:when test="${sessionScope.get('flagConfirm').equals('OK')}">
            <H1>Friend was added</H1>
        </c:when>
        <c:otherwise>
            <H1>Something went wrong</H1>
        </c:otherwise>
    </c:choose>
    <c:remove var="flagConfirm" scope="session"/>
</div>

<jsp:include page="WEB-INF/fragments/footer.jspf"/>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>

