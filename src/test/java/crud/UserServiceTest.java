package crud;

import config.TestPersistenceConfig;
import dev.lqwd.entity.User;
import dev.lqwd.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test1() {

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
