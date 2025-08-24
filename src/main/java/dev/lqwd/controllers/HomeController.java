package dev.lqwd.controllers;

import dev.lqwd.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;


@Controller
public class HomeController {

    private final SessionService sessionService;

    public HomeController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/home")
    public String home(@CookieValue(value = "sessionId", required = false) UUID sessionId) {

        if(sessionService.isPresent(sessionId)){

            return "home";
        }

        return "redirect:sign-in";
    }

}