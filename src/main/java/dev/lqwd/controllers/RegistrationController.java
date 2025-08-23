package dev.lqwd.controllers;

import dev.lqwd.BCryptUtil;
import dev.lqwd.dto.UserCreationRequestDto;
import dev.lqwd.entity.User;
import dev.lqwd.mapper.UserMapper;
import dev.lqwd.repository.SessionRepository;
import dev.lqwd.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final SessionRepository sessionRepository;

    public RegistrationController(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/sign-up")
    public String showRegistrationForm(
        Model model,
        @CookieValue(value = "sessionId", required = false) UUID sessionId) {

            if(sessionId != null
               && sessionRepository.findById(sessionId).isPresent()){

                return "redirect:/home";
            }

        model.addAttribute("userCreationRequest", new UserCreationRequestDto());
        return "sign-up";

    }

    @PostMapping("/sign-up")
    public String authCreation(@Valid @ModelAttribute("userCreationRequest") UserCreationRequestDto creationRequest,
                               BindingResult bindingResult,
                               Model model) {


        if (creationRequest.getPassword() != null && creationRequest.getPasswordConfirm() != null
            && !creationRequest.getPassword().equals(creationRequest.getPasswordConfirm())){

            model.addAttribute("error", "Passwords don't match");
            return "sign-up";
        }

        if(bindingResult.hasErrors()){

            return "sign-up";
        }

        try {

            String hashedPassword = BCryptUtil.getHashPassword(creationRequest.getPassword());

            User user = userRepository.save(User.builder()
                    .login(creationRequest.getLogin())
                    .password(hashedPassword)
                    .build());

            System.out.println(user);
        } catch (DataIntegrityViolationException e){

            model.addAttribute("error", "User already exist");
            return "sign-up";
        }

        return "redirect:/";
    }
    
}