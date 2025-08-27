package dev.lqwd.controllers;

import dev.lqwd.exception.user_validation.UserValidationException;
import dev.lqwd.service.CookieService;
import dev.lqwd.service.CryptService;
import dev.lqwd.dto.AuthRequestDto;
import dev.lqwd.entity.User;
import dev.lqwd.service.SessionService;
import dev.lqwd.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
public class AuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final CookieService cookieService;

    public AuthController(UserService userService,
                          SessionService sessionService,
                          CookieService cookieService) {

        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }


    @GetMapping({"/"})
    public String index() {

        return "redirect:sign-in";
    }

    @GetMapping({"/sign-in"})
    public String showSignInForm(
            Model model,
            @CookieValue(value = "sessionId", required = false) UUID sessionId) {

        if (sessionService.isPresent(sessionId)) {
            return "redirect:home";
        }

        model.addAttribute("authRequest", new AuthRequestDto());
        return "sign-in";

    }

    @PostMapping("/sign-in")
    public String authValidation(@Valid @ModelAttribute("authRequest") AuthRequestDto authRequest,
                                 BindingResult bindingResult,
                                 HttpServletResponse response,
                                 Model model) {

         if (bindingResult.hasErrors()){
             return "sign-in";
         }

         try {
             User user = userService.readByLogin(authRequest);
             String sessionId = sessionService.create(user);
             response.addCookie(cookieService.create(sessionId));

             return "redirect:home";
         } catch (UserValidationException e){

             model.addAttribute("error", e.getMessage());
             return "sign-in";
         }

    }

    @PostMapping("/sign-out")
    public String signOut(@CookieValue(value = "sessionId", required = false) UUID sessionId,
                          HttpServletResponse response) {

        sessionService.delete(sessionId);
        response.addCookie(cookieService.delete());

        return "redirect:sign-in";
    }

}