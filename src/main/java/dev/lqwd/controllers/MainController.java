package dev.lqwd.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home()
    {
        return "index";

    }

    @GetMapping("/hello")
    public String hello() {

        return "Приф";

    }

}