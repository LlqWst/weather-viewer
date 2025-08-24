package dev.lqwd.controllers;

import dev.lqwd.service.CryptService;
import dev.lqwd.dto.AuthRequestDto;
import dev.lqwd.entity.User;
import dev.lqwd.service.SessionService;
import dev.lqwd.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;
import java.util.UUID;


@Controller
public class AuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final CryptService cryptService;

    public AuthController(UserService userService, SessionService sessionService, CryptService cryptService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cryptService = cryptService;
    }

    @GetMapping("/sign-in")
    public String showRegistrationForm(
            Model model,
            @CookieValue(value = "sessionId", required = false) UUID sessionId) {

        if(sessionService.isPresent(sessionId)){

            return "redirect:home";
        }

        model.addAttribute("authRequest", new AuthRequestDto());
        return "sign-in";

    }

    @PostMapping("/sign-in")
    public String authCreation(@Valid @ModelAttribute("authRequest") AuthRequestDto authRequest,
                               BindingResult bindingResult,
                               HttpServletResponse response,
                               Model model) {


        if(bindingResult.hasErrors()){

            model.addAttribute("error", "Invalid username or password");
            return "sign-in";
        }

        Optional<User> user = userService.readByLogin(authRequest.getLogin());

        if(isIncorrectCredentials(authRequest, user)){

            model.addAttribute("error", "Invalid username or password");
            return "sign-in";
        }

        String sessionId = sessionService.create(user.get());

        Cookie cookie = new Cookie("sessionId", sessionId);
        response.addCookie(cookie);

        return "redirect:home";
    }

    private boolean isIncorrectCredentials(AuthRequestDto authRequest, Optional<User> user) {
        return user.isEmpty() || !cryptService.verifyPassword(authRequest.getPassword(), user.get().getPassword());
    }

}