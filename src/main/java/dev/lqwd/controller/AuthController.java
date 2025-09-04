package dev.lqwd.controller;

import dev.lqwd.exception.user_validation.UserValidationException;
import dev.lqwd.service.AuthService;
import dev.lqwd.dto.AuthRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String redirectToSignIn(){

        return "redirect:/sign-in";
    }

    @GetMapping({"/sign-in"})
    public String showSignInForm(@CookieValue(value = "sessionId", required = false) String sessionId,
                                 Model model) {

        if (authService.hasValidSession(sessionId)) {
            return "redirect:/home";
        }

        model.addAttribute("authRequest", new AuthRequestDto());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String authValidation(@Valid @ModelAttribute("authRequest") AuthRequestDto authRequest,
                                 BindingResult bindingResult,
                                 HttpServletResponse response,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            return "sign-in";
        }

        try {
            response.addCookie(authService
                    .createNewSession(authRequest));
            return "redirect:/home";

        } catch (UserValidationException e) {
            model.addAttribute("error", e.getMessage());
            return "sign-in";
        }

    }

    @PostMapping("/sign-out")
    public String signOut(@CookieValue(value = "sessionId", required = false) String sessionId,
                          HttpServletResponse response) {

        response.addCookie(authService
                .closeSession(sessionId));
        return "redirect:/sign-in";
    }

}