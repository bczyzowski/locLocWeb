package controller;

import data.NewFriendToConfirmRepository;
import data.UserRepository;
import model.NewFriendToConfirm;
import model.User;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@WebServlet("/verify")
public class FriendConfirmationController extends HttpServlet {

    private final UserRepository userRepository;

    private final NewFriendToConfirmRepository friendToConfirmRepository;

    @Inject
    public FriendConfirmationController(UserRepository userRepository, NewFriendToConfirmRepository friendToConfirmRepository) {
        this.userRepository = userRepository;
        this.friendToConfirmRepository = friendToConfirmRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("tok");
        NewFriendToConfirm friendToConfirm = friendToConfirmRepository.get(token);
        if (friendToConfirm != null) {

            LocalDateTime actualTime = LocalDateTime.now();
            long hours = ChronoUnit.HOURS.between(friendToConfirm.getTime(), actualTime);

            if (hours < 24) {
                Long userId = friendToConfirm.getUserId();
                Long friendId = friendToConfirm.getNewFriendId();
                User user = userRepository.get(userId);
                User friend = userRepository.get(friendId);

                user.getFriends().add(friend);
                friend.getFriends().add(user);
                userRepository.update(user);
                userRepository.update(friend);
                friendToConfirmRepository.remove(friendToConfirm);
                req.getSession().setAttribute("flagConfirm", "OK");

            } else {
                friendToConfirmRepository.remove(friendToConfirm);
                req.getSession().setAttribute("flagConfirm", "BAD");
            }

        } else {
            req.getSession().setAttribute("flagConfirm", "BAD");
        }
        resp.sendRedirect("newFriendConfirmation.jsp");
    }
}
