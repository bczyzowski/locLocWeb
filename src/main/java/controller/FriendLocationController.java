package controller;

import data.UserRepository;
import model.User;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/loc")
public class FriendLocationController extends HttpServlet {

    private final UserRepository userRepository;

    @Inject
    public FriendLocationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        HttpSession session = req.getSession();
        User user = userRepository.get(id);

        session.setAttribute("email", user.getEmail());
        session.setAttribute("token", user.getToken());
        resp.sendRedirect("coordinates.jsp");

    }
}
