package dev.lqwd.controller.auth;

import dev.lqwd.service.auth.AuthService;
import dev.lqwd.dto.auth.AuthRequestDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping({"/sign-in"})
    public String showSignInForm(@ModelAttribute("authRequest") AuthRequestDTO authRequest) {

        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String authValidation(@Valid @ModelAttribute("authRequest") AuthRequestDTO authRequest,
                                 BindingResult bindingResult,
                                 HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "sign-in";
        }
        response.addCookie(authService.openSession(authRequest));
        return "redirect:/";
    }

    @PostMapping("/sign-out")
    public String signOut(@CookieValue(value = "sessionId", required = false) String sessionId,
                          HttpServletResponse response) {

        response.addCookie(authService.closeSession(sessionId));
        return "redirect:/";
    }

}