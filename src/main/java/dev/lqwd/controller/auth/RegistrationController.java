package dev.lqwd.controller.auth;


import dev.lqwd.dto.auth.UserRegistrationRequestDTO;
import dev.lqwd.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
public class RegistrationController {

    private final AuthService authService;

    @GetMapping("/sign-up")
    public String showRegistrationForm(@ModelAttribute("userCreationRequest") UserRegistrationRequestDTO userCreationRequest) {

        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String registration(@Valid @ModelAttribute("userCreationRequest") UserRegistrationRequestDTO registrationRequest,
                               BindingResult bindingResult,
                               HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "sign-up";
        }

        Cookie sessionId = authService.registration(registrationRequest);
        response.addCookie(sessionId);
        return "redirect:/";
    }

}