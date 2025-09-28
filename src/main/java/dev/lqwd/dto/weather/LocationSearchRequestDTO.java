package dev.lqwd.dto.weather;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationSearchRequestDTO {

    @NotBlank(message = "Please provide Location")
    @Pattern(
            regexp = "^[a-zA-Zа-яА-Я0-9\\s\\-',.()]{1,100}+$",
            message = "Location can contain only letters of the Latin and Russian alphabets and numbers. Max length: 100")
    private String location;

}