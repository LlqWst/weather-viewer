package controller_test;

import config.TestPersistenceConfig;
import dev.lqwd.configuration.WebMvcConfig;
import dev.lqwd.controllers.RegistrationController;
import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.repository.SessionRepository;
import dev.lqwd.repository.UserRepository;
import dev.lqwd.service.SessionService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {TestPersistenceConfig.class, WebMvcConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuthControllerTest {

    private static final String INIT_USER_LOGIN = "test_1";
    private static final String CORRECT_PASSWORD = "123456";
    private static final String URL_CONTROLLER = "/sign-in";
    private static final String URL_HOME = "/home";
    private static final String COOKIE_NAME = "sessionId";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @InjectMocks
    private RegistrationController registrationController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionService sessionService;

    private MockMvc mockMvc;
    private AutoCloseable closeable;
    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    @Transactional
    public void setUp() {

        closeable = MockitoAnnotations.openMocks(this);

        AutowireCapableBeanFactory beanFactory = webApplicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(registrationController);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        userRepository.save(User.builder()
                .login(INIT_USER_LOGIN)
                .password(CORRECT_PASSWORD)
                .build());
    }

    @AfterEach
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        closeable.close();
    }


    @Test
    public void should_RedirectToHome_When_HasCookies() throws Exception {

        Optional<User> user = userRepository.findByLogin(INIT_USER_LOGIN);
        String uuid = sessionService.create(Optional.of(user).get().orElseThrow());

        Cookie cookie = new Cookie(COOKIE_NAME, uuid);

        mockMvc.perform(get(URL_CONTROLLER)
                        .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                        .cookie(cookie))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_HOME));

        Optional<Session> sessionSaved = sessionRepository.findById(UUID.fromString(uuid));
        Assertions.assertTrue(sessionSaved.isPresent());

        sessionRepository.deleteAll();
    }
}