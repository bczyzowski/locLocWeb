package controller;

import data.UserRepository;
import model.Location;
import model.User;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import service.HashPassword;


import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;


@WebServlet("/login")
public class LoginController extends HttpServlet {


    private final UserRepository userRepository;

    private final HashPassword hashPassword;

    private final String path;

    @Inject
    public LoginController(UserRepository userRepository, HashPassword hashPassword, @ConfigProperty(name = "path") String path) {
        this.userRepository = userRepository;
        this.hashPassword = hashPassword;
        this.path = path;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = checkUser(email, password);
        if (user != null) {
            HttpSession session = req.getSession(true);

          //for AuthenticationFilter
            session.setAttribute("emailOwner", email);
            session.setAttribute("Auth-token", user.getToken());

            session.setAttribute("userObj", user);
            session.setAttribute("path",path);
            session.setMaxInactiveInterval(900);
            resp.sendRedirect("coordinates.jsp");
        } else {
            req.setAttribute("flag", "loginFailed");
            req.getRequestDispatcher("login.jsp").include(req, resp);
        }

    }

    private User checkUser(String email, String password) {
        if(!email.isEmpty()&&!password.isEmpty()){
            User user = userRepository.get(email);
            if (user != null) {
                byte[] hashedPass = hashPassword.hashPassword(password, user.getToken());
                if (Arrays.equals(user.getPassword(), hashedPass)) {
                    return user;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }else {
            return null;
        }
    }
}
