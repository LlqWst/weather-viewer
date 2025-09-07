package dev.lqwd.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class FallBackController {

    @RequestMapping("/**")
    public String handleIncorrectUrls() {

        return "error";
    }
}