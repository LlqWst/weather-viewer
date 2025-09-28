package dev.lqwd.controller;

import dev.lqwd.dto.weather.CurrentWeatherResponseDTO;
import dev.lqwd.service.auth.AuthService;
import dev.lqwd.service.weather.CurrentWeatherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@Slf4j
@AllArgsConstructor
public class MainPageController {

    private final CurrentWeatherService currentWeatherService;

    @GetMapping("/")
    public String redirectToSignIn(@CookieValue(value = "sessionId", required = false) String sessionId,
                                   Model model) {


        List<CurrentWeatherResponseDTO> weatherResponseDTO = currentWeatherService.getWeatherForUser(sessionId);
        model.addAttribute("locationsWeather", weatherResponseDTO);
        return "main-page";
    }
}