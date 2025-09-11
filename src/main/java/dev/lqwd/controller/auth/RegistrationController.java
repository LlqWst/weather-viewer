package dev.lqwd.controller.auth;


import dev.lqwd.dto.auth.UserRegistrationRequestDTO;
import dev.lqwd.exception.user_validation.UserValidationException;
import dev.lqwd.service.auth.AuthService;
import dev.lqwd.service.repository_service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class RegistrationController {

    private final UserService userService;
    private final AuthService authService;

    public RegistrationController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/sign-up")
    public String showRegistrationForm(@CookieValue(value = "sessionId", required = false) String sessionId,
                                       Model model) {

        if (authService.hasValidSession(sessionId)) {
            return "redirect:/home";
        }

        model.addAttribute("userCreationRequest", new UserRegistrationRequestDTO());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String registration(@Valid @ModelAttribute("userCreationRequest") UserRegistrationRequestDTO registrationRequest,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            return "sign-up";
        }
        try {
            userService.save(registrationRequest);
            return "redirect:/home";

        } catch (UserValidationException e) {
            model.addAttribute("error", e.getMessage());
            return "sign-up";
        }
    }

}