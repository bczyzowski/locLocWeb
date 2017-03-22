<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>LocLoc</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/ie10-viewport-bug-workaround.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

    <link rel="stylesheet" type="text/css" href="myStyle.css">

</head>

<body>

<jsp:include page="WEB-INF/fragments/headerWithLogin.jspf"/>

<div class="container-fluid text-center" style="width: 50%;">

    <c:if test="${requestScope.get('result')!=null}">
        <c:choose>
            <c:when test="${requestScope.get('result')==1}">
                <div class="alert alert-success" role="alert">Well done! Your registration was successful</div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger" role="alert">Oh! User exist already</div>
            </c:otherwise>
        </c:choose>
    </c:if>

    <form action="register" method="post">
        <div class="form-group row">
            <label for="inputFirstName" class="col-sm-2 col-form-label">First name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="firstName" id="inputFirstName" placeholder="First name"
                       pattern=".{3,20}" required title="3 to 20 characters">
            </div>
        </div>
        <div class="form-group row">
            <label for="inputLastName" class="col-sm-2 col-form-label">Last name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="lastName" id="inputLastName" placeholder="Last name"
                       pattern=".{3,20}" required title="3 to 20 characters">
            </div>
        </div>
        <div class="form-group row">
            <label for="inputEmail" class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-10">
                <input type="email" class="form-control" name="email" id="inputEmail" placeholder="Email" required
                       title="email required">
            </div>
        </div>
        <div class="form-group row">
            <label for="inputPassword" class="col-sm-2 col-form-label">Password</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" name="password" id="inputPassword" placeholder="Password"
                       pattern=".{5,}" required title="5 characters minimum">
            </div>
        </div>
        <div class="form-group row">
            <label for="inputPassword" class="col-sm-2 col-form-label">Confirm password</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" name="password2" id="inputPassword2" placeholder="Password"                      >
            </div>
        </div>
        <p id="passWarning" style="align-content: center"/>
        <div class="form-group row">
            <div class="offset-sm-2 col-sm-10" id="buttonDiv">
                <button type="submit" class="btn btn-primary" id="submitButton">Submit</button>
            </div>
        </div>
    </form>
</div>

<jsp:include page="WEB-INF/fragments/footer.jspf"/>

<script>
    var passWarning = $("#passWarning");
    var pass1 = $("#inputPassword");
    var pass2 = $("#inputPassword2");
    var submit = $("#submitButton");
    submit.attr("disabled","true");
    function validatePassword() {
        console.log("val");
        if (pass1.val() != pass2.val()) {
            passWarning.empty();
            passWarning.append("Check passwords");
            submit.attr("disabled","true");
        } else {
            passWarning.empty();
            submit.removeAttr("disabled");
        }
    }
    pass1.keyup(function () {
        validatePassword();
    });
    pass2.keyup(function () {
        validatePassword();
    });

</script>
<script src="js/bootstrap.min.js"></script>
<script src="js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>

