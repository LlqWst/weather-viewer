//package dev.lqwd.filter;
//
//import java.io.*;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@WebFilter("/*")
//public class ExceptionsHandlerFilter extends HttpFilter {
//
//    private static final String ERROR_MESSAGE = "Oops.. something went wrong";
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
//
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        HttpServletResponse res = (HttpServletResponse) servletResponse;
//
//        try {
//
//            chain.doFilter(req, res);
//
//        } catch (Exception e) {
//
//            log.error("error to e.message: {}, e.stack.trace: {}", e.getMessage(), e.getStackTrace());
//            req.setAttribute("error", ERROR_MESSAGE);
//
//            req.getRequestDispatcher("/").forward(req, res);
//
//        }
//    }
//
//}
