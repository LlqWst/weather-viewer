package dev.lqwd.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class LoginOrEmailValidator implements ConstraintValidator<LoginOrEmail, String> {
    private static final Pattern LOGIN_PATTERN = Pattern.compile("^[a-zA-Z0-9 ~!#$%^&*()_=+/'\".-]{5,20}$");

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()){
            return false;
        }

        return LOGIN_PATTERN.matcher(value).matches() || EMAIL_PATTERN.matcher(value).matches();
    }
}
