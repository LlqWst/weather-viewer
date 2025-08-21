package dev.lqwd.controllers;

import dev.lqwd.dto.UserCreationRequestDto;
import dev.lqwd.entity.User;
import dev.lqwd.mapper.UserMapper;
import dev.lqwd.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class RegistrationController {

    UserRepository userRepository;
    UserMapper userMapper = UserMapper.INSTANCE;

    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/sign-up")
    public String showRegistrationForm(Model model) {

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
        }

        if(bindingResult.hasErrors()){

            return "sign-up";
        }

        try {

            User user = userRepository.save(userMapper.toUser(creationRequest));
            System.out.println(user);
        } catch (DataIntegrityViolationException e){

            model.addAttribute("error", "User already exist");
            return "sign-up";
        }

        return "redirect:/";
    }
    
}