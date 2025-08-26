package dev.lqwd.controllers;

import dev.lqwd.exception.UserAlreadyExistException;
import dev.lqwd.dto.UserCreationRequestDto;
import dev.lqwd.service.SessionService;
import dev.lqwd.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
public class RegistrationController {

    private final UserService userService;
    private final SessionService sessionService;

    public RegistrationController(UserService userService, SessionService sessionService) {

        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/sign-up")
    public String showRegistrationForm(
            Model model,
            @CookieValue(value = "sessionId", required = false) UUID sessionId) {

        if (sessionService.isPresent(sessionId)) {
            return "redirect:/home";
        }

        model.addAttribute("userCreationRequest", new UserCreationRequestDto());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String registration(@Valid @ModelAttribute("userCreationRequest") UserCreationRequestDto creationRequest,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            return "sign-up";
        }

        if (!doPasswordsMatch(creationRequest)) {
            model.addAttribute("error", "Passwords don't match");
            return "sign-up";
        }

        try {
            userService.save(creationRequest);
        } catch (UserAlreadyExistException e) {

            model.addAttribute("error", e.getMessage());
            return "sign-up";
        }

        return "redirect:/";
    }

    private static boolean doPasswordsMatch(UserCreationRequestDto creationRequest) {
        return creationRequest.getPassword().equals(creationRequest.getPasswordConfirm());
    }

}