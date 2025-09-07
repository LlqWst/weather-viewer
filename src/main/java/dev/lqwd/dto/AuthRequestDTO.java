package dev.lqwd.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRequestDTO {

    @NotBlank(message = "Please provide login")
    private String login;

    @NotBlank(message = "Please provide password")
    private String password;

}
