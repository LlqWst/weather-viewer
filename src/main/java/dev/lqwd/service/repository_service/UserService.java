package dev.lqwd.service.repository_service;

import dev.lqwd.utils.Validator;
import dev.lqwd.dto.auth.AuthRequestDTO;
import dev.lqwd.dto.auth.UserRegistrationRequestDTO;
import dev.lqwd.entity.User;
import dev.lqwd.exception.user_validation.UserAuthException;
import dev.lqwd.exception.user_validation.UserRegistrationException;
import dev.lqwd.repository.UserRepository;
import dev.lqwd.service.auth.CryptService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private static final String ERROR_MESSAGE_USER_EXISTS = "User already exists";
    private static final String ERROR_MESSAGE_INCORRECT_LOGIN = "Incorrect login";

    private final UserRepository userRepository;
    private final CryptService cryptService;


    public User readByLogin(AuthRequestDTO authRequest) throws UserAuthException {
        User user = userRepository.findByLogin(authRequest.getLogin())
                .orElseGet(() -> {
                    log.warn(ERROR_MESSAGE_INCORRECT_LOGIN);
                    throw new UserAuthException(ERROR_MESSAGE_INCORRECT_LOGIN);
                });

        cryptService.verifyPassword(authRequest.getPassword(), user.getPassword());
        return user;
    }

    public User save(UserRegistrationRequestDTO creationRequest) {
        Validator.validatePasswordOnEquals(creationRequest);
        String hashedPassword = cryptService.getHashPassword(creationRequest.getPassword());

        try {
            return userRepository.save(User.builder()
                    .login(creationRequest.getLogin())
                    .password(hashedPassword)
                    .build());
        } catch (ConstraintViolationException e) {
            if (e.getKind() == ConstraintViolationException.ConstraintKind.UNIQUE) {
                log.warn(ERROR_MESSAGE_USER_EXISTS);
                throw new UserRegistrationException(ERROR_MESSAGE_USER_EXISTS, e);
            }
            throw e;
        }
    }

}
