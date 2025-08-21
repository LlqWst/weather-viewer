package dev.lqwd.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = LoginOrEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginOrEmail {

    String message() default "Please provide login 5-20 characters long (char '@' not supported for login), or correct email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
