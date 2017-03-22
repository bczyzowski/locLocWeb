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
<%--header--%>
<jsp:include page="WEB-INF/fragments/headerWithLogin.jspf"/>

<div class="container-fluid bg-1 text-center">

    <div class="container" style="margin-top: 20vh">
        <img src="whiteLogo.png" class="img-responsive center-block">
       <%-- <p style="margin-top: 20vh">This is a template for a simple marketing or informational website. It includes a large callout called a
            jumbotron and three supporting pieces of content. Use it as a starting point to create something more
            unique.</p>--%>
        <p style="margin-top: 20vh"><a class="btn btn-primary btn-lg" href="https://drive.google.com/file/d/0B_EQompdNncrdjdxcjdHZEJqQ3c/view?usp=sharing" role="button">Download our app&raquo;</a></p>
    </div>
</div>

<jsp:include page="WEB-INF/fragments/footer.jspf"/>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
