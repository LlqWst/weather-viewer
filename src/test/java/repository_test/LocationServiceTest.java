package repository_test;

import config.TestPersistenceConfig;
import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;
import dev.lqwd.repository.LocationRepository;
import dev.lqwd.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LocationServiceTest {


    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void should_createLocation_and_findLocation() {

        User user = User.builder()
                .login("test")
                .password("test")
                .build();

        User savedUser = userRepository.findById(1L)
                .orElseGet(() -> userRepository.save(user));

        Location location = Location.builder()
                .name("SpB")
                .latitude(BigDecimal.valueOf(61.999))
                .longitude(BigDecimal.valueOf(124.999))
                .user(savedUser)
                .build();

        locationRepository.save(location);

        Location found = locationRepository.findById(location.getId()).orElseThrow();

        System.out.println(found);
        assertEquals(location, found);

    }
}
