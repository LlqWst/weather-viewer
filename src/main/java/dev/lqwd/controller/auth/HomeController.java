package dev.lqwd.controller.auth;

import dev.lqwd.dto.ApiLocationsResponseDTO;
import dev.lqwd.dto.WeatherRequestDTO;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.service.weahter_api.LocationService;
import dev.lqwd.service.weahter_api.WeatherApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class HomeController {

    private final WeatherApiService weatherApiService;
    private final LocationService locationService;

    public HomeController(WeatherApiService weatherApiService, LocationService locationService) {
        this.weatherApiService = weatherApiService;
        this.locationService = locationService;
    }

    @GetMapping("/home")
    public String home(Model model) {

        model.addAttribute("weatherRequest", new WeatherRequestDTO());
        return "home";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<ApiLocationsResponseDTO> redirectToSignIn(@ModelAttribute("weatherRequest") WeatherRequestDTO weatherRequestDto,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
//            return "redirect:/home";
            throw new BadRequestException("Nuul");
        }

        //List<ApiLocationsResponseDTO> locationsDTOs = locationService.getLocations(weatherRequestDto.getLocation());
        return locationService.getLocations(weatherRequestDto.getLocation());
    }

}