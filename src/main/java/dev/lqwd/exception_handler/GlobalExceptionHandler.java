package dev.lqwd.exception_handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleUniversalException(Exception ex, HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("error");
        mav.addObject("statusCode", 500);

        log.error("Exception occurred: {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        return mav;
    }

}

