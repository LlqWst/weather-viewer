package dev.lqwd.dto;

import dev.lqwd.annotation.LoginOrEmail;
import dev.lqwd.annotation.StrongPassword;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRequestDto {

    private String login;

    private String password;

}
