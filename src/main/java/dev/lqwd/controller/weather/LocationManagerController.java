package dev.lqwd.controller.weather;

import dev.lqwd.dto.weather.AddLocationRequestDTO;
import dev.lqwd.service.repository_service.LocationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class LocationManagerController {

    private final LocationService locationService;

    @PostMapping("/add-location")
    public String addLocation(@CookieValue(value = "sessionId") String sessionId,
                              @Valid @ModelAttribute("location") AddLocationRequestDTO locationDTO,
                              BindingResult bindingResult) {

        if (!bindingResult.hasErrors()) {
            locationService.save(locationDTO, sessionId);
        }
        return "redirect:/";
    }

    @PostMapping("/delete-location")
    public String deleteLocation(@CookieValue(value = "sessionId") String sessionId,
                                 @RequestParam("id") String id) {

        if (id != null && !id.isBlank()) {
            locationService.delete(sessionId, id);
        }
        return "redirect:/";
    }

}
