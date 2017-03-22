package filters;


import data.UserRepository;
import model.User;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    private final UserRepository userRepository;

    @Inject
    public AuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession != null && httpSession.getAttribute("Auth-token") != null) {
            String userEmail = String.valueOf(httpSession.getAttribute("emailOwner"));
            String userToken = userRepository.getToken(userEmail);
            String tokenFromSession = String.valueOf(httpSession.getAttribute("Auth-token"));
            if (userToken.equals(tokenFromSession))
                filterChain.doFilter(httpServletRequest, servletResponse);
            else{
                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                httpServletResponse.sendRedirect("login.jsp");
            }
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.sendRedirect("login.jsp");
        }
    }

    @Override
    public void destroy() {

    }
}
