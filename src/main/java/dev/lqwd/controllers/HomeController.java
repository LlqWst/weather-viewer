package dev.lqwd.controllers;

import dev.lqwd.entity.Session;
import dev.lqwd.repository.SessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.UUID;


@Controller
public class HomeController {

    private final SessionRepository sessionRepository;

    public HomeController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/home")
    public String home(@CookieValue(value = "sessionId", required = false, defaultValue = "") UUID sessionId) {

        Optional <Session> session = sessionRepository.findById(sessionId);

        if(session.isEmpty()){

            return "redirect:sign-in";
        }


        return "home";
    }

}