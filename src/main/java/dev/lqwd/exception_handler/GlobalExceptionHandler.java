package dev.lqwd.exception_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleUniversalException(Exception ex, Model model) {

        model.addAttribute("errorCode", "DB_ERROR");
        log.error("Exception occurred:  {}",  ex.getMessage(), ex);
        return "error";
    }


}

