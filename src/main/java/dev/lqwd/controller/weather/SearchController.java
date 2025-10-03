package dev.lqwd.controller.weather;


import dev.lqwd.dto.weather.LocationWithStatusResponseDTO;
import dev.lqwd.service.weather.WeatherLocationService;
import dev.lqwd.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class SearchController {

    private final WeatherLocationService weatherLocationService;

    @GetMapping("/search")
    public String redirectToSignIn(@CookieValue(value = "sessionId") String sessionId,
                                   @RequestParam("location") String search,
                                   Model model) {

        Validator.validate(search);

        List<LocationWithStatusResponseDTO> locationsWithStatusDTO = weatherLocationService.getLocations(search, sessionId);
        model.addAttribute("locationsWithStatus", locationsWithStatusDTO);
        return "search-results";
    }
}
