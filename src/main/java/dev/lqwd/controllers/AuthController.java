package dev.lqwd.controllers;

import dev.lqwd.BCryptUtil;
import dev.lqwd.dto.AuthRequestDto;
import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.mapper.UserMapper;
import dev.lqwd.repository.SessionRepository;
import dev.lqwd.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.HttpCookie;
import java.util.Optional;
import java.util.UUID;


@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public AuthController(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/sign-in")
    public String showRegistrationForm(
            Model model,
            @CookieValue(value = "sessionId", required = false) UUID sessionId) {

        if(sessionId != null
           && sessionRepository.findById(sessionId).isPresent()){

            return "redirect:home";
        }


        model.addAttribute("authRequest", new AuthRequestDto());
        return "sign-in";

    }

    @PostMapping("/sign-in")
    public String authCreation(@Valid @ModelAttribute("authRequest") AuthRequestDto authRequest,
                               HttpServletResponse response,
                               Model model) {

        try {

            User user = Optional.of(userRepository.findByLogin(authRequest.getLogin()))
                    .orElseThrow();

            if(!BCryptUtil.verifyPassword(authRequest.getPassword(), user.getPassword())){

                model.addAttribute("error", "Invalid password");
                return "sign-in";
            }

            Session session = sessionRepository.save(Session.builder()
                    .user(user)
                    .build());

            Cookie cookie = new Cookie("sessionId", session.getId().toString());
            response.addCookie(cookie);

        } catch (Exception e){

            model.addAttribute("error", "Invalid username or password");
            return "sign-in";
        }

        return "redirect:home";
    }
    
}