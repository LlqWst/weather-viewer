package dev.lqwd.controller.weahter_api;

import dev.lqwd.dto.weather_api.AddLocationRequestDTO;
import dev.lqwd.dto.weather_api.ApiLocationResponseDTO;
import dev.lqwd.dto.weather_api.WeatherOfLocationResponseDTO;
import dev.lqwd.service.WeatherService;
import dev.lqwd.service.db.LocationService;
import dev.lqwd.service.weahter_api.LocationApiService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Slf4j
public class HomeController {

    private static final String URL_LOCATIONS = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s";
    private static final String APP_ID = System.getenv("APP_ID");
    private final LocationApiService locationApiService;
    private final WeatherService weatherService;
    private final LocationService locationService;

    public HomeController(WeatherService weatherService,
                          LocationApiService locationApiService,
                          LocationService locationService) {
        this.weatherService = weatherService;
        this.locationApiService = locationApiService;
        this.locationService = locationService;
    }

    @GetMapping("/home")
    public String home(@CookieValue(value = "sessionId") String sessionId,
                       Model model) {

        List<WeatherOfLocationResponseDTO> weatherResponseDTO = weatherService.getWeatherForUser(sessionId);
        model.addAttribute("locationsWeather", weatherResponseDTO);
        return "home";
    }

    @GetMapping("/search")
    public String redirectToSignIn(@RequestParam("location") String search,
                                   Model model) {

        if (search == null || search.isBlank()){
            return "search-results";
        }
        List<ApiLocationResponseDTO> locationsDTO = locationApiService.fetchApiData(
                URL_LOCATIONS.formatted(search, APP_ID));
        model.addAttribute("locations", locationsDTO);
        return "search-results";
    }

    @PostMapping("/add-location")
    public String addLocation(@CookieValue(value = "sessionId") String sessionId,
                              @Valid @ModelAttribute("location") AddLocationRequestDTO locationDTO,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return "redirect:/search-results";
        }
        locationService.save(locationDTO, sessionId);
        return "redirect:/home";
    }

    @PostMapping("/delete-location")
    public String deleteLocation(@CookieValue(value = "sessionId") String sessionId,
            @RequestParam("id") String id) {

        if(id == null || id.isBlank()){
            log.warn("try to delete location without id");
            return "redirect:/home";
        }
        locationService.delete(sessionId, id);
        return "redirect:/home";
    }

}