package dev.lqwd.controller.weather;


import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import dev.lqwd.service.weather.WeatherLocationService;
import dev.lqwd.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class SearchController {

    private final WeatherLocationService weatherLocationService;

    @GetMapping("/search")
    public String redirectToSignIn(@RequestParam("location") String search,
                                   Model model) {

        Validator.validate(search);

        List<ApiLocationResponseDTO> locationsDTO = weatherLocationService.getLocations(search);
        model.addAttribute("locations", locationsDTO);
        return "search-results";
    }
}
