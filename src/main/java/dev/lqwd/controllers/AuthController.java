package dev.lqwd.controllers;

import dev.lqwd.dto.AuthRequestDto;
import dev.lqwd.entity.User;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.mapper.UserMapper;
import dev.lqwd.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;


@Controller
public class AuthController {

    UserRepository userRepository;
    UserMapper userMapper = UserMapper.INSTANCE;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/sign-in")
    public String showRegistrationForm(Model model) {

        model.addAttribute("authRequest", new AuthRequestDto());
        return "sign-in";

    }

    @PostMapping("/sign-in")
    public String authCreation(@Valid @ModelAttribute("authRequest") AuthRequestDto authRequest,
                               BindingResult bindingResult,
                               Model model) {

        try {

            User user = Optional.of(userRepository.findByLogin(authRequest.getLogin()))
                    .orElseThrow(() -> new BadRequestException("Invalid username or password"));
            System.out.println(user);
        } catch (Exception e){

            model.addAttribute("error", "Invalid username or password");
            return "sign-in";
        }

        return "redirect:/";
    }
    
}