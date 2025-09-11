package dev.lqwd.interceptor;

import dev.lqwd.service.auth.CookieService;
import dev.lqwd.service.repository_service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;
    private final CookieService cookieService;
    private static final String SIGN_IN_URL = "/weather-viewer/sign-in";

    public LoggingInterceptor(SessionService sessionService, CookieService cookieService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (!hasValidSession(request)) {
            response.sendRedirect(SIGN_IN_URL);
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
