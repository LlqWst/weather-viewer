package dev.lqwd.controller.weather;

import dev.lqwd.dto.weather.LocationSearchRequestDTO;
import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import dev.lqwd.service.weather.WeatherLocationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@AllArgsConstructor
public class SearchController {

    private final WeatherLocationService weatherLocationService;

    @GetMapping("/search")
    public String redirectToSignIn(@Valid @ModelAttribute("location") LocationSearchRequestDTO location,
                                   BindingResult bindingResult,
                                   Model model) {

        if (bindingResult.hasErrors()) {
            return "search-results";
        }
        List<ApiLocationResponseDTO> locationsDTO = weatherLocationService.getLocations(location.getLocation());
        model.addAttribute("locations", locationsDTO);
        return "search-results";
    }
}
