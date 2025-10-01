package dev.lqwd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class FallBackController {

    @GetMapping("/error")
    public String redirectToError() {

        return "error";
    }
}
