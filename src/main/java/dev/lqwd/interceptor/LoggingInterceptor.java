package dev.lqwd.interceptor;

import dev.lqwd.service.auth.CookieService;
import dev.lqwd.service.repository_service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;


@Component
@AllArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String SIGN_IN_URL = "/weather-viewer/sign-in";
    private static final List<String> authPaths = Arrays.asList(
           "/weather-viewer/", "/weather-viewer/sign-in", "/weather-viewer/sign-up");
    private final SessionService sessionService;
    private final CookieService cookieService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        if (!hasValidSession(request) && authPaths.contains(requestURI)) {
            return true;
        } else if (!hasValidSession(request) && !authPaths.contains(requestURI)) {
            response.sendRedirect(SIGN_IN_URL);
            return false;
        } else if (hasValidSession(request) && authPaths.contains(requestURI)) {
            response.sendRedirect("/weather-viewer/home");
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
