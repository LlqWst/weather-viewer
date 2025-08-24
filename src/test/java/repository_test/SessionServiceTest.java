package repository_test;

import config.TestPersistenceConfig;
import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.repository.SessionRepository;
import dev.lqwd.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@ActiveProfiles("test")
@Transactional
public class SessionServiceTest {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void should_createSession_and_findSession() {

        User user = User.builder()
                .login("test")
                .password("test")
                .build();

        User savedUser = userRepository.findById(1)
                .orElseGet(() -> userRepository.save(user));


        Session session = Session.builder()
                .user(savedUser)
                .build();

        sessionRepository.save(session);

        Session found = sessionRepository.findById(session.getId()).orElseThrow();

        System.out.println(found);
        assertEquals(session, found);

    }

}
