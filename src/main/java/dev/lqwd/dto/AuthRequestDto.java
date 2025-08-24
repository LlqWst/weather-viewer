package dev.lqwd.dto;

import dev.lqwd.annotation.LoginOrEmail;
import dev.lqwd.annotation.StrongPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRequestDto {

    @LoginOrEmail
    private String login;

    @StrongPassword
    private String password;

}
