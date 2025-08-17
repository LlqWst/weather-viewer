package dev.lqwd.controllers;


import dev.lqwd.service.LocationService;
import dev.lqwd.service.SessionService;
import dev.lqwd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @Autowired
    UserService userService;

    @Autowired
    LocationService locationService;

    @Autowired
    SessionService sessionService;

    @GetMapping("/")
    public String home() {

        return "index";

    }

    @GetMapping("/hello")
    public String hello() {

        return "Приф";

    }

}