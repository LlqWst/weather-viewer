package dev.lqwd.controller.weahter_api;

import dev.lqwd.dto.AddLocationRequestDTO;
import dev.lqwd.dto.ApiLocationsResponseDTO;
import dev.lqwd.service.weahter_api.LocationService;
import dev.lqwd.service.weahter_api.WeatherApiService;
import dev.lqwd.service.weahter_api.WeatherService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class HomeController {

    private final LocationService locationService;
    private final WeatherService weatherService;

    public HomeController(WeatherService weatherService, LocationService locationService) {
        this.weatherService = weatherService;
        this.locationService = locationService;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/search")
    public String redirectToSignIn(@RequestParam("location") String search,
                                   Model model) {

        if (search == null || search.isBlank()){
            return "search-results";
        }

        List<ApiLocationsResponseDTO> locationsDTO = locationService.getLocations(search);
        model.addAttribute("locations", locationsDTO);
        return "search-results";
    }

    @PostMapping("/add-location")
    public String addLocation(@Valid @ModelAttribute("location") AddLocationRequestDTO locationDTO,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return "redirect:/search-results";
        }

        System.out.println(locationDTO);
        weatherService.getWeather(locationDTO);
        return "redirect:/home";
    }

}