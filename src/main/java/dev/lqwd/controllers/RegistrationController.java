package dev.lqwd.controllers;

import dev.lqwd.Validator;
import dev.lqwd.dto.UserRegistrationRequestDto;
import dev.lqwd.exception.user_validation.UserValidationException;
import dev.lqwd.service.SessionService;
import dev.lqwd.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class RegistrationController {

    private final UserService userService;
    private final SessionService sessionService;

    public RegistrationController(UserService userService, SessionService sessionService) {

        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/sign-up")
    public String showRegistrationForm(@CookieValue(value = "sessionId", required = false) String sessionId,
                                       Model model) {

        boolean hasValidSession = Validator.parseUUID(sessionId)
                .filter(sessionService::isPresent)
                .isPresent();

        if (hasValidSession) {
            return "redirect:/home";
        }

        model.addAttribute("userCreationRequest", new UserRegistrationRequestDto());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String registration(@Valid @ModelAttribute("userCreationRequest") UserRegistrationRequestDto registrationRequest,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            return "sign-up";
        }

        try {
            Validator.validatePasswordOnEquals(registrationRequest);
            userService.save(registrationRequest);
            return "redirect:/";

        } catch (UserValidationException e) {
            model.addAttribute("error", e.getMessage());
            return "sign-up";
        }
    }

}