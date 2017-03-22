package controller;

import data.UserRepository;
import model.User;
import service.HashPassword;
import service.TokenGenerator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private final UserRepository userRepository;
    private final HashPassword hashPassword;
    private final TokenGenerator tokenGenerator;

    @Inject
    public RegisterController(UserRepository userRepository, HashPassword hashPassword, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.hashPassword = hashPassword;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("password2");

        if ((firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) ||!password.equals(confirmPassword)||
                !(firstName.length()>=3 && firstName.length()<=20) || !(lastName.length()>=3 && lastName.length()<=20) || !(password.length()>=5)) {
            req.setAttribute("result", 0);
        } else {
            String token = tokenGenerator.generateToken();
            byte[] hashedPass = hashPassword.hashPassword(password, token);

            User user = new User(email, hashedPass, firstName, lastName);

            User userToFind = userRepository.get(user.getEmail());
            if (userToFind == null) {
                user.setToken(token);
                userRepository.add(user);
                req.setAttribute("result", 1);
            } else {
                req.setAttribute("result", 0);
            }
        }

        req.getRequestDispatcher("register.jsp").include(req, resp);
    }
}
