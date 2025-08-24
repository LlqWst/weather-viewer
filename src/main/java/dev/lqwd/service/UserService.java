package dev.lqwd.service;

import dev.lqwd.dto.UserCreationRequestDto;
import dev.lqwd.entity.User;
import dev.lqwd.exception.UserAlreadyExistException;
import dev.lqwd.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CryptService cryptService;

    public UserService(UserRepository userRepository, CryptService cryptService) {
        this.userRepository = userRepository;
        this.cryptService = cryptService;
    }


    public Optional<User> readByLogin(String login){

        return userRepository.findByLogin(login);
    }

    public User save(UserCreationRequestDto creationRequest){

        String hashedPassword = cryptService.getHashPassword(creationRequest.getPassword());

        try {
            return userRepository.save(User.builder()
                    .login(creationRequest.getLogin())
                    .password(hashedPassword)
                    .build());

        } catch (DataIntegrityViolationException e){

            throw new UserAlreadyExistException("User already exist");
        }

    }

}
