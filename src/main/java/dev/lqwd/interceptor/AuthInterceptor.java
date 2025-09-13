package dev.lqwd.interceptor;

import dev.lqwd.configuration.AuthConfig;
import dev.lqwd.service.auth.CookieService;
import dev.lqwd.service.repository_service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@AllArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthConfig authConfig;
    private final SessionService sessionService;
    private final CookieService cookieService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        boolean isAuthPath = authConfig.getPublicUrls().contains(requestURI);
        boolean isValidSession = hasValidSession(request);



        if (isAuthPath && isValidSession) {
            response.sendRedirect(authConfig.getHomeUrl());
            return false;
        }

        if (!isAuthPath && !isValidSession) {
            response.sendRedirect(authConfig.getSignInUrl());
            return false;
        }

        return true;
    }

    private boolean hasValidSession(HttpServletRequest request) {
        return cookieService.getSessionId(request.getCookies())
                .filter(sessionService::isPresent)
                .isPresent();
    }
}
