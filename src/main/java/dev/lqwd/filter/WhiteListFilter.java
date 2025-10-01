package dev.lqwd.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@WebFilter("/*")
@Component
@Slf4j
public class WhiteListFilter extends HttpFilter {

    private static final List<String> WHITE_LIST = List.of(
            "/", "/sign-up", "/sign-in", "/search",
            "/sign-out", "/add-location", "/delete-location",
            "/search-results", "/error");

    private static final String ERROR_PAGE = "/weather-viewer/error";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpRes = (HttpServletResponse) servletResponse;

        String path = httpReq.getServletPath();

        if (WHITE_LIST.contains(path)) {
            chain.doFilter(httpReq, httpRes);
        } else {
            log.warn("Attempt to request to a non-existent path. PATH: {}", path);
            httpRes.sendRedirect(ERROR_PAGE);
        }
    }
}