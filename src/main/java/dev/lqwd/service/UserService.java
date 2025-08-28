package dev.lqwd.service;

import dev.lqwd.dto.AuthRequestDto;
import dev.lqwd.dto.UserRegistrationRequestDto;
import dev.lqwd.entity.User;
import dev.lqwd.exception.user_validation.IncorrectCredentialsException;
import dev.lqwd.exception.user_validation.UserAlreadyExistsException;
import dev.lqwd.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private static final String ERROR_MESSAGE_USER_EXISTS = "User already exists";
    private static final String ERROR_MESSAGE_INCORRECT_LOGIN = "Incorrect login";

    private final UserRepository userRepository;
    private final CryptService cryptService;

    public UserService(UserRepository userRepository, CryptService cryptService) {
        this.userRepository = userRepository;
        this.cryptService = cryptService;
    }


    public User readByLogin(AuthRequestDto authRequest) throws IncorrectCredentialsException {

        User user = userRepository.findByLogin(authRequest.getLogin())
                .orElseGet(() -> {
                    log.warn(ERROR_MESSAGE_INCORRECT_LOGIN);
                    throw new IncorrectCredentialsException(ERROR_MESSAGE_INCORRECT_LOGIN);
                });

        cryptService.verifyPassword(authRequest.getPassword(), user.getPassword());
        return user;
    }

    public User save(UserRegistrationRequestDto creationRequest) {

        String hashedPassword = cryptService.getHashPassword(creationRequest.getPassword());

        try {
            return userRepository.save(User.builder()
                    .login(creationRequest.getLogin())
                    .password(hashedPassword)
                    .build());

        } catch (DataIntegrityViolationException e) {

            log.warn(ERROR_MESSAGE_USER_EXISTS);
            throw new UserAlreadyExistsException(ERROR_MESSAGE_USER_EXISTS);
        }
    }

}
