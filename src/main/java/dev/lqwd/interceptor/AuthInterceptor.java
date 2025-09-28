package dev.lqwd.interceptor;

import dev.lqwd.configuration.app_config.ApplicationUrlConfig;
import dev.lqwd.entity.User;
import dev.lqwd.service.auth.CookieService;
import dev.lqwd.service.repository_service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;


@Component
@AllArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final ApplicationUrlConfig applicationUrlConfig;
    private final SessionService sessionService;
    private final CookieService cookieService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        Optional<User> currentUser = getCurrentUser(request);

        currentUser.ifPresent(user -> request.setAttribute("userName", user.getLogin()));

        if (!hasAccess(requestURI, currentUser)) {
            response.sendRedirect(applicationUrlConfig.getHomeUrl());
            return false;
        }
        return true;
    }

    private boolean hasAccess(String requestURI, Optional<User> currentUser) {
        boolean isHomeUrl = requestURI.equals(applicationUrlConfig.getHomeUrl());
        if (isHomeUrl) {
            return true;
        }

        boolean isAuthUrl = applicationUrlConfig.getAuthUrls().contains(requestURI);
        if (isAuthUrl) {
            return currentUser.isEmpty();
        }
        return currentUser.isPresent();
    }

    private Optional<User> getCurrentUser(HttpServletRequest request) {
        return cookieService.getSessionId(request.getCookies())
                .flatMap(sessionService::getUserById);
    }
}


