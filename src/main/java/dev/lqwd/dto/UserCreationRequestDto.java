package dev.lqwd.dto;

import dev.lqwd.validator.LoginOrEmail;
import dev.lqwd.validator.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreationRequestDto {

    @LoginOrEmail
    private String login;

    @StrongPassword
    private String password;

    @NotBlank(message = "Please confirm the password")
    private String passwordConfirm;
}
