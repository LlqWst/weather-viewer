package dev.lqwd.controller.weather;

import dev.lqwd.dto.weather_api.api_response.ApiLocationResponseDTO;
import dev.lqwd.service.weahter_api.LocationApiService;
import dev.lqwd.uri_builder.UriApiLocationBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final LocationApiService locationApiService;

    public SearchController(LocationApiService locationApiService) {
        this.locationApiService = locationApiService;
    }

    @GetMapping("/search")
    public String redirectToSignIn(@RequestParam("location") String search,
                                   Model model) {

        if (search != null && !search.isBlank()) {
            String url = new UriApiLocationBuilder(search).build();
            List<ApiLocationResponseDTO> locationsDTO = locationApiService.fetchApiData(url);
            model.addAttribute("locations", locationsDTO);
        }
        return "search-results";
    }
}
