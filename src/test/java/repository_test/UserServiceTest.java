package repository_test;

import config.TestPersistenceConfig;
import dev.lqwd.entity.User;
import dev.lqwd.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserServiceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void should_createUser_and_findUser() {

        User user = User.builder()
                .login("test")
                .password("test")
                .build();

        entityManager.persist(user);
        entityManager.flush();

        User found = userRepository.findById(user.getId()).orElseThrow();

        System.out.println(found);
        assertNotNull(found);

    }
}
