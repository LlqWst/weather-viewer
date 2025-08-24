package dev.lqwd.interceptor;

import dev.lqwd.service.CookieService;
import dev.lqwd.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;
import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;
    private final CookieService cookieService;

    public LoggingInterceptor(SessionService sessionService, CookieService cookieService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        Optional <UUID> sessionId = cookieService.getSessionId(request);

        if (sessionId.isEmpty()){

            response.sendRedirect("/weather-viewer/sign-in");
            return false;
        }

        if (!sessionService.isPresent(sessionId.get())) {

            response.sendRedirect("/weather-viewer/sign-in");
            return false;
        }

        return true;
    }

}
