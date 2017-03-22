package rest;

import data.UserRepository;
import model.Location;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

@Path("/location")
@RequestScoped
public class LocationEndpoint {

    @Inject
    private UserRepository userRepository;

    /*
    for mobile app - new location (last fetched)
     */
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveLocation(JsonObject json) {
        User user = userRepository.get(json.getString("email"));
        if (user != null && user.getToken().equals(json.getString("authToken"))) {
            Location location = new Location(Double.valueOf(json.get("latitude").toString()), Double.valueOf(json.get("longitude").toString()), Float.valueOf(json.get("accuracy").toString()), LocalDateTime.parse(json.getString("time")));
            userRepository.updateLocation(user, location);
            return Response.accepted().build();
        } else {
            return Response.status(404).build();
        }
    }

    /*
    for mobile app - new locations (list of locations which haven't been uploaded yet)
     */
    @POST
    @Path("/sendAll")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveAllLocations(JsonArray jsonArray) {
        int updatedLocations = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = (JsonObject) jsonArray.get(i);
            User user = userRepository.get(object.getString("email"));
            if (user != null && user.getToken().equals(object.getString("authToken"))) {
                Location location = locationParser(object);
                userRepository.updateLocation(user, location);
                updatedLocations++;
            }
        }
        if (updatedLocations == jsonArray.size()) {
            return Response.accepted().build();
        } else {
            return Response.status(404).build();
        }

    }

    /*
    for mobile app & angula2app - getting last location
     */
    @POST
    @Path("/last")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLastLocation(JsonObject json) {
        User user = userRepository.get(json.getString("email"));
        if (user != null && user.getToken().equals(json.getString("token"))) {
            Location lastLoc = userRepository.getLastLocation(user);
            if (lastLoc != null) {
                JSONObject resObject = new JSONObject();
                resObject.put("lat", lastLoc.getLatitude());
                resObject.put("lon", lastLoc.getLongitude());
                resObject.put("acc", lastLoc.getAccuracy());
                resObject.put("time", lastLoc.getTime());
                return Response.ok(resObject.toString()).build();
            } else {
                return Response.status(404).build();
            }
        } else {
            return Response.status(404).build();
        }
    }

    /*
        for javascript on server app -> query with id parameter
        for mobile app & angular2app -> query with friendEmail parameter
     */

    @POST
    @Path("/history")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getHistoryLocation(JsonObject json) {
        User user = userRepository.get(json.getString("email"));
        if (user != null && user.getToken().equals(json.getString("token"))) {
            String id = json.getString("id", "");
            User friend;
            if (id.equals("")) {
                String friendEmail = json.getString("friendEmail");
                friend = userRepository.get(friendEmail);
            } else {
                long friendId = Long.valueOf(id);
                friend = userRepository.get(friendId);
                System.out.println("ID");
            }
            if (user.getFriends().contains(friend) || user.getId().equals(friend.getId())) {
                Integer day = json.getInt("day");
                Integer month = json.getInt("month");
                Integer year = json.getInt("year");
                List<Location> locations = userRepository.getLocationsByDayAndMonthAndYear(day, month, year, friend.getId());
                if (locations.size() > 0) {
                    JSONArray allData = new JSONArray();
                    int jsonIndex = 0;
                    for (int i = locations.size() - 1; i >= 0; i--) {
                        JSONObject loc = new JSONObject();
                        Location tmp = locations.get(i);
                        loc.put("lat", tmp.getLatitude());
                        loc.put("lon", tmp.getLongitude());
                        loc.put("acc", tmp.getAccuracy());
                        loc.put("time", tmp.getTime());
                        allData.put(jsonIndex++, loc);
                    }

                    return Response.ok(allData.toString()).build();
                } else {
                    return Response.status(404).build();
                }
            } else {
                return Response.status(404).build();
            }
        } else {
            return Response.status(404).build();
        }
    }

    private static Location locationParser(JsonObject json) {
        return new Location(Double.valueOf(json.get("latitude").toString()), Double.valueOf(json.get("longitude").toString()), Float.valueOf(json.get("accuracy").toString()), LocalDateTime.parse(json.getString("time")));

    }
}
