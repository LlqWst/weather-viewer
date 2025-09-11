package dev.lqwd.controller.weather;

import dev.lqwd.dto.weather.CurrentWeatherResponseDTO;
import dev.lqwd.service.weather.CurrentWeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class HomeController {

    private final CurrentWeatherService currentWeatherService;

    public HomeController(CurrentWeatherService currentWeatherService) {
        this.currentWeatherService = currentWeatherService;
    }

    @GetMapping("/home")
    public String home(@CookieValue(value = "sessionId") String sessionId,
                       Model model) {

        List<CurrentWeatherResponseDTO> weatherResponseDTO = currentWeatherService.getWeatherForUser(sessionId);
        model.addAttribute("locationsWeather", weatherResponseDTO);
        return "home";
    }
}