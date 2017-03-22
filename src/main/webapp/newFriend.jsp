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

<nav class="navbar navbar-default">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<c:url value="/loc?id=${sessionScope.userObj.id}"/>">My Location</a>
            <a class="navbar-brand" href="<c:url value="/newFriend.jsp"/>">Add new friend</a>
            <ul class="nav navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="supportedContentDropdown" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">Friends</a>
                    <div class="dropdown-menu" aria-labelledby="supportedContentDropdown">
                        <c:forEach items="${sessionScope.userObj.friends}" var="fr">
                            <a class="dropdown-item"
                               href="<c:url value="/loc?id=${fr.id}"/>"> ${fr.firstname} ${fr.lastname}<br></a>
                        </c:forEach>
                    </div>
                </li>
            </ul>

        </div>
        <div id="navbar" class="navbar-collapse collapse navbar-right">
            <a class="navbar-brand" href="logout">Logout</a>
        </div>
    </div>
</nav>
<div class="container-fluid text-center" style="width: 50%;padding-top: 100px;">
    <form action="add" method="post">
        <div class="form-group row">
            <label for="email" class="col-sm-2 col-form-label">Enter friend's e-mail</label>
            <div class="col-sm-10">
                <input type="email" class="form-control" name="email" id="email" placeholder="E-mail">
            </div>
        </div>
        <div class="form-group row">
            <div class="offset-sm-2 col-sm-10">
                <button type="submit" class="btn btn-primary">Send request</button>
            </div>
        </div>
    </form>
</div>

<jsp:include page="WEB-INF/fragments/footer.jspf"/>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>

