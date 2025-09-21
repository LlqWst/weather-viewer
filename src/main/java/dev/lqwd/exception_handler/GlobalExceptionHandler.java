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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserRegistrationException.class)
    public String handleUserAlreadyExistsException(Exception e,
                                                   RedirectAttributes redirectAttributes,
                                                   HttpServletRequest request) {


        log.error("Exception occurred:  {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("error", e.getMessage());

        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO();
        requestDTO.setLogin(request.getParameter("login"));
        redirectAttributes.addFlashAttribute("userCreationRequest", requestDTO);

        return "redirect:/sign-up";
    }

    @ExceptionHandler(UserAuthException.class)
    public String handleUserAuthExceptionException(Exception e,
                                                   RedirectAttributes redirectAttributes,
                                                   HttpServletRequest request) {


        log.warn("Exception occurred:  {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("error", e.getMessage());

        AuthRequestDTO requestDTO = new AuthRequestDTO();
        requestDTO.setLogin(request.getParameter("login"));
        redirectAttributes.addFlashAttribute("authRequest", requestDTO);

        return "redirect:/sign-in";
    }

    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequestException(Exception e,
                                            Model model,
                                            HttpServletResponse response) {

        log.warn("Exception occurred:  {}", e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
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

