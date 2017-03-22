package controller;

import data.NewFriendToConfirmRepository;
import data.UserRepository;
import model.NewFriendToConfirm;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import service.TokenGenerator;
import model.User;
import service.EmailService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/add")
public class AddNewFriendController extends HttpServlet {

    private final UserRepository userRepository;

    private final NewFriendToConfirmRepository friendToConfirmRepository;

    private final EmailService emailService;

    private final TokenGenerator tokenGenerator;

    private final String path;

    @Inject
    public AddNewFriendController(UserRepository userRepository, NewFriendToConfirmRepository friendToConfirmRepository, EmailService emailService, TokenGenerator tokenGenerator, @ConfigProperty(name = "path") String path) {
        this.userRepository = userRepository;
        this.friendToConfirmRepository = friendToConfirmRepository;
        this.emailService = emailService;
        this.tokenGenerator = tokenGenerator;
        this.path = path;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        User user = (User) req.getSession().getAttribute("userObj");
        String token = tokenGenerator.generateToken();

        User newFriend = userRepository.get(email);
        if (newFriend != null) {
            NewFriendToConfirm confirm = new NewFriendToConfirm();
            confirm.setUserId(user.getId());
            confirm.setNewFriendId(newFriend.getId());
            confirm.setToken(token);
            friendToConfirmRepository.add(confirm);

            String page = path + "/locloc/verify?tok=" + token;

            emailService.sendEmail(newFriend, page, user);

            req.getSession().setAttribute("flagAdd", "OK");

        } else {
            req.getSession().setAttribute("flagAdd", "BAD");
        }
        resp.sendRedirect("addNewFriendResponse.jsp");
    }
}
