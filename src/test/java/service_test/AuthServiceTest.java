package service_test;


import config.TestPersistenceConfig;
import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.repository.SessionRepository;
import dev.lqwd.repository.UserRepository;
import dev.lqwd.service.AuthService;
import dev.lqwd.service.SessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    public void deleteOnlyExpiredSession(){

        Session expiredSession = Session.builder()
                .user(saveUser("test_1", "test_1"))
                .expiresAt(LocalDateTime.now().minusHours(1))
                .build();

        Session savedSession = sessionRepository.save(expiredSession);
        String validSessionId = sessionService.create(saveUser("test_2", "test_2"));

        int deletedCount = authService.cleanupExpiredSessions();
        Assertions.assertEquals(1, deletedCount);

        Optional<Session> validSession = sessionRepository.findById(UUID.fromString(validSessionId));
        Assertions.assertTrue(validSession.isPresent());
        Assertions.assertTrue(validSession.get().getExpiresAt().isAfter(LocalDateTime.now()));

        Optional<Session> foundExpired = sessionRepository.findById(savedSession.getId());
        Assertions.assertFalse(foundExpired.isPresent());
    }

    private User saveUser(String login, String password){
        User savedUser = User.builder()
                .login(login)
                .password(password)
                .build();

        return userRepository.save(savedUser);
    }
}
