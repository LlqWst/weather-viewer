package crud;

import dev.lqwd.configuration.PersistenceConfig;
import dev.lqwd.entity.Location;
import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.repository.LocationRepository;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes =  {PersistenceConfig.class})
@Transactional
public class LocationServiceTest {

    @Autowired
    LocationRepository locationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test1() {

        User user = userRepository.findById(1).orElseThrow();

        Location location = Location.builder()
                .name("SpB")
                .latitude(BigDecimal.valueOf(61.999))
                .longitude(BigDecimal.valueOf(124.999))
                .user(user)
                .build();

        entityManager.persist(location);
        entityManager.flush();

        Location found = locationRepository.findById(location.getId()).orElseThrow();

        System.out.println(found);
        assertEquals(location, found);

    }
}
