package dev.lqwd.interceptor;

import dev.lqwd.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    public LoggingInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String session = getSessionId(request.getCookies());

        if (session.isBlank()){
            response.sendRedirect("/weather-viewer/sign-in");
            return false;
        }

        UUID sessionId = UUID.fromString(session);

        if (!sessionService.isPresent(sessionId)) {

            response.sendRedirect("/weather-viewer/sign-in");
            return false;
        }

        return true;
    }


    private static String getSessionId(Cookie[] cookies){
        if (cookies == null){
            return "";
        }

        for(Cookie cookie : cookies){
            if(cookie.getName().equals("sessionId")) {
                return cookie.getValue();
            }
        }

        return "";

    }

}
