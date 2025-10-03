package dev.lqwd.exception_handler;

import dev.lqwd.dto.auth.AuthRequestDTO;
import dev.lqwd.dto.auth.UserRegistrationRequestDTO;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.exception.UnauthorizedException;
import dev.lqwd.exception.api_weather_exception.ApiServiceUnavailableException;
import dev.lqwd.exception.api_weather_exception.SubscriptionApiException;
import dev.lqwd.exception.api_weather_exception.UnexpectedExternalApiException;
import dev.lqwd.exception.user_validation.UserAuthException;
import dev.lqwd.exception.user_validation.UserRegistrationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(UserRegistrationException.class)
    public String handleUserAlreadyExistsException(Exception e,
                                                   HttpServletRequest request,
                                                   Model model) {


        log.error("Exception occurred:  {}", e.getMessage(), e);

        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO();
        requestDTO.setLogin(request.getParameter("login"));

        model.addAttribute("error", e.getMessage());
        model.addAttribute("userCreationRequest", requestDTO);
        return "sign-up";
    }

    @ExceptionHandler(UserAuthException.class)
    public String handleUserAuthExceptionException(Exception e,
                                                   HttpServletRequest request,
                                                   Model model) {


        log.warn("Exception occurred:  {}", e.getMessage(), e);

        AuthRequestDTO requestDTO = new AuthRequestDTO();
        requestDTO.setLogin(request.getParameter("login"));

        model.addAttribute("error", e.getMessage());
        model.addAttribute("authRequest", requestDTO);
        return "sign-in";
    }

    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequestException(Exception e,
                                            Model model,
                                            HttpServletResponse response) {

        log.warn("Exception occurred:  {}", e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        model.addAttribute("error", e.getMessage());
        return "search-results";
    }

    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorizedException(Exception e,
                                              HttpServletResponse response) {

        log.error("Exception occurred:  {}", e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "error";
    }

    @ExceptionHandler(SubscriptionApiException.class)
    public String handleSubscriptionApiException(Exception e,
                                                 Model model,
                                                 HttpServletResponse response) {

        log.error("Exception occurred:  {}", e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    @ExceptionHandler(UnexpectedExternalApiException.class)
    public String handleUnexpectedExternalApiException(Exception e,
                                                       Model model,
                                                       HttpServletResponse response) {

        log.error("Exception occurred:  {}", e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    @ExceptionHandler(ApiServiceUnavailableException.class)
    public String handleApiServiceUnavailableException(Exception e,
                                                       Model model,
                                                       HttpServletResponse response) {

        log.error("Exception occurred:  {}", e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleUniversalException(Exception e,
                                           HttpServletResponse response) {

        log.error("Exception occurred:  {}", e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "error";
    }

}

