package dev.lqwd.controller.weather;

import dev.lqwd.dto.weather_api.CurrentWeatherResponseDTO;
import dev.lqwd.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class HomeController {

    private final WeatherService weatherService;

    public HomeController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/home")
    public String home(@CookieValue(value = "sessionId") String sessionId,
                       Model model) {

        List<CurrentWeatherResponseDTO> weatherResponseDTO = weatherService.getWeatherForUser(sessionId);
        model.addAttribute("locationsWeather", weatherResponseDTO);
        return "home";
    }
}