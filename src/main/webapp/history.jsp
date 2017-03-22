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
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

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


        </div>
        <div id="navbar" class="navbar-collapse collapse navbar-right">
            <a class="navbar-brand" href="logout">Logout</a>
        </div>
    </div>
</nav>

<div class="container-fluid">

    <div class="row">
        <div class="col-sm-3">
            <%--users--%>
            <div class="list-group" id="listOfUser">

            </div>
        </div>
        <div class="col-sm-3" style="position: relative">
            <%--datepicker--%>
            <p>Date: <input type="text" id="datepicker" style="position: absolute;z-index: 3000"></p>
            <%--locations--%>
            <div class="list-group" id="listOfLocations">
            </div>

        </div>
        <%--map--%>
        <div class="col-sm-6">
            <div id="map" style="width: 100%; height: 100vh;"></div>
        </div>
    </div>


</div>
<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog" style="z-index: 3001">
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
<div id="myModal2" class="modal fade" role="dialog" style="z-index: 3001">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"></h4>
            </div>
            <div class="modal-body">
                <p>Select user before</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>

<script>
    $(document).ready(function () {

        var selectedDay, selectedMonth, selectedYear;
        var lastUserActive;
        var lastLocActive;
        var latitude, longitude;
        var time;
        var locationsArrayToPaint;

        (function initFriendsList() {
            //friends saved from coordinates.jsp
            var jsonFriends = sessionStorage.getItem("friends");
            jsonFriends = JSON.parse(jsonFriends);

            var list = $("#listOfUser");

            // my loc
            var aLink = document.createElement("a");
            var myId = "${sessionScope.get('userObj').id}";

            //console.log(myId);


            //ustawienie historii dla zalogowanego uzytkownika
            //wybor nowego uzytkownika z listy skutkuje wyczyszczeniem historii lokaliazacji i ukryciem mapy
            $(aLink).data("id", myId.toString()).click(function () {
                if (lastLocActive != null) {
                    $(lastLocActive).removeClass("list-group-item active").addClass('list-group-item');
                    $("#map").hide();
                    $("#listOfLocations").empty();
                }

                setUserActive($(this));
            });
            aLink.innerText = "${sessionScope.get('userObj').firstname}" + ' ' + "${sessionScope.get('userObj').lastname}";
            aLink.className = "list-group-item";
            list.append(aLink);

            //ustawienie dla przyjaciol zalogowanego uzytkownika
            //wybor nowego uzytkownika z listy skutkuje wyczyszczeniem historii lokaliazacji i ukryciem mapy
            for (var i = 0; i < jsonFriends.length; i++) {
                var friend = jsonFriends[i];
                var name = friend[0];
                var id = friend[1];
                var aLink = document.createElement("a");
                $(aLink).data("id", id.toString()).click(function () {
                    if (lastLocActive != null) {
                        $(lastLocActive).removeClass("list-group-item active").addClass('list-group-item');
                        $("#map").hide();
                        $("#listOfLocations").empty();
                    }
                    setUserActive($(this));
                });
                aLink.innerText = name;
                aLink.className = "list-group-item";
                list.append(aLink);
            }
        })();

        //datepicker setup
        $(function () {
            $("#datepicker").datepicker({
                dateFormat: 'yy-m-d',
                onSelect: function (dateText) {
                    if (lastUserActive != null) {
                        $("#map").hide();
                        $("#listOfLocations").empty();
                        var time = new Date(dateText);
                        selectedDay = time.getDate();
                        selectedMonth = time.getMonth() + 1;
                        selectedYear = time.getFullYear();
                        retrieveLocHistory(lastUserActive);
                    } else {
                        $("#myModal2").modal("show");
                    }
                }
            });

        });

        // metody pomocnicze
        function createLocNode(text, className, latitude, longitude, time) {
            var node = document.createElement("a");
            node.innerText = text.replace("T", " ");
            node.className = className;
            $(node).data("lat", latitude);
            $(node).data("lon", longitude);
            $(node).data("time", time);
            return node;
        }

        function createNode(text, className) {
            var node = document.createElement("a");
            node.innerText = text;
            node.className = className;
            return node;
        }

        function setUserActive(node) {
            if (lastUserActive != null) {
                $(lastUserActive).removeClass("list-group-item active").addClass('list-group-item');
            }
            $(node).removeClass('list-group-item').addClass("list-group-item active");
            lastUserActive = node;

        }

        function setShowOnMapButtonActive(node) {
            if (lastLocActive != null) {
                $(lastLocActive).removeClass("list-group-item active").addClass('list-group-item');
            }
            $(node).removeClass('list-group-item').addClass("list-group-item active");
            lastLocActive = node;
            $("#map").show();
            initMap();
        }

        function setLocActive(node) {
            if (lastLocActive != null) {
                $(lastLocActive).removeClass("list-group-item active").addClass('list-group-item');
            }
            $(node).removeClass('list-group-item').addClass("list-group-item active");
            lastLocActive = node;
            latitude = node.data("lat");
            longitude = node.data("lon");
            time = node.data("time");
            locationsArrayToPaint = null;
            $("#map").show();
            initMap();
        }

        // koniec metod pomocniczych

        function retrieveLocHistory(node) {
            if (selectedYear != null) {

                var id = $(node).data("id");
                var xhttp = new XMLHttpRequest();
                var obj = new Object();
                var em = '${sessionScope.get('userObj').email}';
                var tok = '${sessionScope.get('userObj').token}';
                obj.email = em.toString();
                obj.token = tok.toString();
                obj.id = id;
                obj.day = selectedDay;
                obj.month = selectedMonth;
                obj.year = selectedYear;
                var data = JSON.stringify(obj);
                xhttp.open("POST", "${sessionScope.get("path")}" + "/locloc/api/location/history", false);
                xhttp.setRequestHeader("Content-type", "application/json");
                xhttp.send(data);
                if (xhttp.responseText.length == 0) {
                    $("#myModal").modal("show");
                    $("#listOfLocations").empty();
                } else {
                    var locList = $("#listOfLocations");
                    var jsonData = JSON.parse(xhttp.responseText);
                    var pointsArray = [];
                    for (i = 0; i < jsonData.length; i++) {
                        //console.log(jsonData[i]);
                        var json = jsonData[i];
                        var loc = {lat: json.lat, lng: json.lon};
                        pointsArray[i] = loc;
                        var node = createLocNode(json.time, "list-group-item", json.lat, json.lon, json.time);
                        $(node).click(function () {
                            setLocActive($(this));
                        });
                        locList.append(node);
                    }
                    var showHistoryOnMap = createNode("Show on map", "list-group-item");

                    $(showHistoryOnMap).click(function () {
                        locationsArrayToPaint = pointsArray;
                        setShowOnMapButtonActive(showHistoryOnMap);
                    });

                    if (pointsArray.length == 1) {
                        $(showHistoryOnMap).addClass("disabled").unbind("click");
                    }

                    locList.prepend(showHistoryOnMap);
                }
                selectedYear = null;
                selectedMonth = null;
                selectedDay = null;
            }
        }

        function initMap() {

            if (locationsArrayToPaint != null && locationsArrayToPaint.length > 1) {
                var map = new google.maps.Map(document.getElementById('map'), {
                    zoom: 10,
                    center: locationsArrayToPaint[parseInt(locationsArrayToPaint.length / 2, 10)]
                });

                var locPath = new google.maps.Polyline({
                    path: locationsArrayToPaint,
                    geodesic: true,
                    strokeColor: '#FF0000',
                    strokeOpacity: 1.0,
                    strokeWeight: 2
                });

                locPath.setMap(map);

                for (i = 0; i < locationsArrayToPaint.length; i++) {
                    var marker = new google.maps.Marker({
                        position: locationsArrayToPaint[i],
                        map: map
                    });
                }


            } else {

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
            }


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

        //pobranie przyjaciol
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
            // console.log(jsonData);
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
    });

</script>

<script src="js/bootstrap.min.js"></script>
<script src="js/ie10-viewport-bug-workaround.js"></script>
<script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDyg5CdFQuszAU2H6ZNF7RHbb_FBUtWlsg&callback=initMap">
</script>
</body>
</html>


