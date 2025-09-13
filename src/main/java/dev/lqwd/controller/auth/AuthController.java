package dev.lqwd.controller.auth;

import dev.lqwd.exception.user_validation.UserValidationException;
import dev.lqwd.service.auth.AuthService;
import dev.lqwd.dto.auth.AuthRequestDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping({"/sign-in"})
    public String showSignInForm(Model model) {

        model.addAttribute("authRequest", new AuthRequestDTO());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String authValidation(@Valid @ModelAttribute("authRequest") AuthRequestDTO authRequest,
                                 BindingResult bindingResult,
                                 HttpServletResponse response,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            return "sign-in";
        }
        try {
            response.addCookie(authService.createNewSession(authRequest));
            return "redirect:/";

        } catch (UserValidationException e) {
            model.addAttribute("error", e.getMessage());
            return "sign-in";
        }

    }

    @PostMapping("/sign-out")
    public String signOut(@CookieValue(value = "sessionId", required = false) String sessionId,
                          HttpServletResponse response) {

        response.addCookie(authService.closeSession(sessionId));
        return "redirect:/";
    }

}