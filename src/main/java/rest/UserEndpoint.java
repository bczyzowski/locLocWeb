package rest;

import data.NewFriendToConfirmRepository;
import data.UserRepository;
import model.Location;
import model.NewFriendToConfirm;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import service.HashPassword;
import service.TokenGenerator;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import service.EmailService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;


@Path("/user")
@RequestScoped
public class UserEndpoint {

    private final UserRepository userRepository;

    private final NewFriendToConfirmRepository friendToConfirmRepository;

    private final EmailService emailService;

    private final TokenGenerator tokenGenerator;

    private final HashPassword hashPassword;

    private final String path;

    @Inject
    public UserEndpoint(UserRepository userRepository, NewFriendToConfirmRepository friendToConfirmRepository, EmailService emailService, TokenGenerator tokenGenerator, HashPassword hashPassword, @ConfigProperty(name = "path") String path) {
        this.userRepository = userRepository;
        this.friendToConfirmRepository = friendToConfirmRepository;
        this.emailService = emailService;
        this.tokenGenerator = tokenGenerator;
        this.hashPassword = hashPassword;
        this.path = path;
    }

    /*
    for all apps
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(JsonObject json) {

        String email = json.getString("email");
        String password = json.getString("password");

        User user = userRepository.get(email);
        if (user != null) {
            byte[] hashedPass = hashPassword.hashPassword(password, user.getToken());
            JSONObject responseObject = new JSONObject();
            if (Arrays.equals(user.getPassword(), hashedPass)) {
                responseObject.put("token", user.getToken());
                responseObject.put("firstName", user.getFirstname());
                responseObject.put("lastName", user.getLastname());
                System.out.println("Cont");
                return Response.ok(responseObject.toString()).build();
            } else {
                return Response.status(404).build();
            }
        } else {
            return Response.status(404).build();
        }
    }

    /*
     for mobile app & angular2app - getting list of friends with last location
     */
    @POST
    @Path("/friends")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserFriends(JsonObject object) {
        String email = object.getString("email");
        String token = object.getString("token");
        User user = userRepository.get(email);
        if (user.getToken().equals(token)) {
            List<User> friends = user.getFriends();
            JSONArray jsonArray = new JSONArray();
            for (User us : friends) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", us.getEmail());
                Location lastUserLoc = userRepository.getLastLocation(us);
                if (lastUserLoc!=null) {

                    jsonObject.put("latitude", lastUserLoc.getLatitude());
                    jsonObject.put("longitude", lastUserLoc.getLongitude());
                    jsonObject.put("time", lastUserLoc.getTime());
                    jsonObject.put("accuracy", lastUserLoc.getAccuracy());

                } else {
                    // brak loc dla uzytkownika
                    jsonObject.put("latitude", "");
                    jsonObject.put("longitude", "");
                }
                jsonArray.put(jsonObject);
            }
            return Response.ok(jsonArray.toString()).build();
        } else {
            return Response.status(404).build();
        }
    }

    /*
    for all apps
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signup(JsonObject json) {
        String firstName = json.getString("firstName");
        String lastName = json.getString("lastName");
        String email = json.getString("email");
        String password = json.getString("password");

        User user = userRepository.get(email);
        if (user == null) {
            String token = tokenGenerator.generateToken();
            byte[] hashedPass = hashPassword.hashPassword(password, token);
            user = new User(email, hashedPass, firstName, lastName, token);
            userRepository.add(user);
            return Response.accepted().build();
        } else {
            return Response.status(404).build();
        }
    }

    /*
     for all apps
     */
    @POST
    @Path("/newfriend")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newFriend(JsonObject object) {
        String userEmail = object.getString("userEmail");
        String newFriendEmail = object.getString("newFriendEmail");
        User user = userRepository.get(userEmail);
        User newFriend = userRepository.get(newFriendEmail);
        String token = tokenGenerator.generateToken();

        if (newFriend != null) {
            NewFriendToConfirm confirm = new NewFriendToConfirm();
            confirm.setToken(token);
            confirm.setUserId(user.getId());
            confirm.setNewFriendId(newFriend.getId());
            friendToConfirmRepository.add(confirm);
            String page = path + "/locloc/verify?tok=" + token;
            emailService.sendEmail(newFriend, page, user);
            return Response.accepted().build();
        } else {
            return Response.status(404).build();
        }
    }

    /*
    for javascript on server
     */
    @POST
    @Path("/friendlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserFriendsList(JsonObject object) {
        String email = object.getString("email");
        String token = object.getString("token");
        User user = userRepository.get(email);
        if (user.getToken().equals(token)) {
            JSONArray objects = new JSONArray();
            for (User u : user.getFriends()) {
                JSONArray array = new JSONArray();
                array.put(0, u.getFirstname() + " " + u.getLastname());
                array.put(1, u.getId());
                objects.put(array);
            }
            return Response.ok(objects.toString()).build();
        } else {
            return Response.status(404).build();
        }
    }

    /*
    for angular2app
     */
    @POST
    @Path("/userCheck")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkIfUserExist(JsonObject json){
        String email = json.getString("email");
        User user = userRepository.get(email);
        if(user != null){
            return Response.status(404).build();
        }else{
            return Response.status(200).build();
        }
    }
}
