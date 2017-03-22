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
    <link href='https://fonts.googleapis.com/css?family=Montserrat:400,700' rel='stylesheet' type='text/css'>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>

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
                    <a class="nav- link dropdown-toggle" id="supportedContentDropdown" data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">Friends</a>
                    <ul class="dropdown-menu" id="friendList">
                    </ul>
                </li>
            </ul>
            <a class="navbar-brand" href="<c:url value="/history.jsp"/>">History</a>


        </div>
        <div id="navbar" class="navbar-collapse collapse navbar-right">
            <a class="navbar-brand" href="logout">Logout</a>
        </div>
    </div>
</nav>

<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"></h4>
            </div>
            <div class="modal-body">
                <p>No location available for this user</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>


<div id="map" style="width: 100%; height: 100vh;"></div>


<script>
    $(document).ready(function () {
        // getting all friends
        (function () {
            var xhttp = new XMLHttpRequest();
            var obj = new Object();
            var em = '${sessionScope.userObj.email}';
            var tok = '${sessionScope.userObj.token}';
            obj.email = em.toString();
            obj.token = tok.toString();
            var data = JSON.stringify(obj);
            xhttp.open("POST", "${sessionScope.get("path")}" + "/locloc/api/user/friendlist", false);
            xhttp.setRequestHeader("Content-type", "application/json");
            xhttp.send(data);
            var friendList = document.getElementById("friendList");
            var jsonData = JSON.parse(xhttp.responseText);
            //console.log(jsonData);
            sessionStorage.setItem("friends", JSON.stringify(jsonData));
            for (var i = 0; i < jsonData.length; i++) {
                var friend = jsonData[i];
                var name = friend[0];
                var id = "${sessionScope.get("path")}" + "/locloc/loc?id=" + friend[1];
                var liNode = document.createElement("li");
                var aLink = document.createElement("a");
                aLink.setAttribute("href", id);
                aLink.innerText = name;
                liNode.appendChild(aLink);
                friendList.appendChild(liNode);
            }
        })();


        var latitude;
        var longitude;
        var accuracy;
        var time;

        //getting last location
        (function () {
            var xhttp = new XMLHttpRequest();
            var obj = new Object();
            var em = '${sessionScope.get('email')}';
            var tok = '${sessionScope.get('token')}';
            obj.email = em.toString();
            obj.token = tok.toString();
            var data = JSON.stringify(obj);
            xhttp.open("POST", "${sessionScope.get("path")}" + "/locloc/api/location/last", false);
            xhttp.setRequestHeader("Content-type", "application/json");
            xhttp.send(data);
            if (xhttp.responseText.length == 0) {
                $("#myModal").modal("show");
            } else {
                var jsonData = JSON.parse(xhttp.responseText);
                accuracy = jsonData['acc'];
                longitude = jsonData['lon'];
                latitude = jsonData['lat'];
                time = jsonData['time'].replace("T"," ");
                initMap();
            }
        })();

        function initMap() {

            var myLatLng = {lat: latitude, lng: longitude};

            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 10,
                center: myLatLng
            });

            var marker = new google.maps.Marker({
                position: myLatLng,
                map: map,
                title: 'Last saved position'
            });

            var infowindow = new google.maps.InfoWindow({
                content: time

            });


            infowindow.open(map, marker);

            var styles = [{
                "featureType": "landscape",
                "stylers": [{"saturation": -100}, {"lightness": 65}, {"visibility": "on"}]
            }, {
                "featureType": "poi",
                "stylers": [{"saturation": -100}, {"lightness": 51}, {"visibility": "simplified"}]
            }, {
                "featureType": "road.highway",
                "stylers": [{"saturation": -100}, {"visibility": "simplified"}]
            }, {
                "featureType": "road.arterial",
                "stylers": [{"saturation": -100}, {"lightness": 30}, {"visibility": "on"}]
            }, {
                "featureType": "road.local",
                "stylers": [{"saturation": -100}, {"lightness": 40}, {"visibility": "on"}]
            }, {
                "featureType": "transit",
                "stylers": [{"saturation": -100}, {"visibility": "simplified"}]
            }, {"featureType": "administrative.province", "stylers": [{"visibility": "off"}]}, {
                "featureType": "water",
                "elementType": "labels",
                "stylers": [{"visibility": "on"}, {"lightness": -25}, {"saturation": -100}]
            }, {
                "featureType": "water",
                "elementType": "geometry",
                "stylers": [{"hue": "#ffff00"}, {"lightness": -25}, {"saturation": -97}]
            }];

            map.set('styles', styles);
        }
    });

</script>

<script src="js/bootstrap.min.js"></script>
<script src="js/ie10-viewport-bug-workaround.js"></script>
<script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDyg5CdFQuszAU2H6ZNF7RHbb_FBUtWlsg&callback=initMap">
</script>
</body>
</html>


