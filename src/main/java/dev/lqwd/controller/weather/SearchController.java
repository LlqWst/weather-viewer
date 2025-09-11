package dev.lqwd.controller.weather;

import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import dev.lqwd.service.weather.WeatherLocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final WeatherLocationService weatherLocationService;

    public SearchController(WeatherLocationService weatherLocationService) {
        this.weatherLocationService = weatherLocationService;
    }

    @GetMapping("/search")
    public String redirectToSignIn(@RequestParam("location") String location,
                                   Model model) {

        if (location != null && !location.isBlank()) {
            List<ApiLocationResponseDTO> locationsDTO = weatherLocationService.getLocations(location);
            model.addAttribute("locations", locationsDTO);
        }
        return "search-results";
    }
}
