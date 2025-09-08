package dev.lqwd.controller.weahter_api;

import dev.lqwd.dto.weather_api.AddLocationRequestDTO;
import dev.lqwd.dto.weather_api.ApiLocationsResponseDTO;
import dev.lqwd.dto.weather_api.ApiWeatherResponseDTO;
import dev.lqwd.service.LocationService;
import dev.lqwd.service.weahter_api.LocationApiService;
import dev.lqwd.service.weahter_api.WeatherApiService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class HomeController {

    private static final String URL_LOCATIONS = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s";
    private static final String URL_WEATHER = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";
    private static final String APP_ID = System.getenv("APP_ID");
    private final LocationApiService locationApiService;
    private final WeatherApiService weatherService;
    private final LocationService locationService;

    public HomeController(WeatherApiService weatherService,
                          LocationApiService locationApiService,
                          LocationService locationService) {
        this.weatherService = weatherService;
        this.locationApiService = locationApiService;
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

        String parameterizedURI = URL_LOCATIONS.formatted(search, APP_ID);
        List<ApiLocationsResponseDTO> locationsDTO = locationApiService.getApiData(parameterizedURI);
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

        System.out.println(locationDTO);
        locationService.save(locationDTO, sessionId);

        String latitude = String.valueOf(locationDTO.getLat());
        String longitude = String.valueOf(locationDTO.getLon());
        String parameterizedURI = URL_WEATHER.formatted(latitude, longitude, APP_ID);

        List<ApiWeatherResponseDTO> locationsDTO = weatherService.getApiData(parameterizedURI);
        System.out.println(locationsDTO);
        return "redirect:/home";
    }

}