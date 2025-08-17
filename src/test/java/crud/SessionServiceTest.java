package crud;

import dev.lqwd.configuration.PersistenceConfig;
import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.repository.SessionRepository;
import dev.lqwd.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes =  {PersistenceConfig.class})
@Transactional
public class SessionServiceTest {

    @Autowired
    SessionRepository sessionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test1() {

        User user = userRepository.findById(1).orElseThrow();

        Session session = Session.builder()
                .user(user)
                .build();

        entityManager.persist(session);
        entityManager.flush();

        Session found = sessionRepository.findById(session.getId()).orElseThrow();

        System.out.println(found);
        assertEquals(session, found);

    }
}
