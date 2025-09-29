package controller_test;

import config.TestPersistenceConfig;
import dev.lqwd.configuration.app_config.ApplicationUrlConfig;
import dev.lqwd.configuration.app_config.HttpClientConfig;
import dev.lqwd.configuration.web.WebMvcConfig;
import dev.lqwd.controller.auth.RegistrationController;
import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.repository.SessionRepository;
import dev.lqwd.repository.UserRepository;
import dev.lqwd.service.repository_service.SessionService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {TestPersistenceConfig.class, WebMvcConfig.class, ApplicationUrlConfig.class, HttpClientConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RegistrationControllerTest {

    private static final String INIT_USER_LOGIN = "test_1";
    private static final String CORRECT_PASSWORD = "123456";
    private static final String BASE_URL = "/weather-viewer";
    private static final String URL_SIGN_UP = BASE_URL + "/sign-up";
    private static final String URL_HOME = BASE_URL + "/";
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
    public void setUp(){

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

    private void performRegistrationAndAssertErrorsForLogin(String login) throws Exception {
        mockMvc.perform(post(URL_SIGN_UP)
                        .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                        .contextPath(BASE_URL)
                        .param("login", login)
                        .param("password", CORRECT_PASSWORD)
                        .param("passwordConfirm", CORRECT_PASSWORD))
                .andDo(print())
                .andExpect(model().attributeHasErrors("userCreationRequest"))
                .andExpect(model().attributeHasFieldErrors("userCreationRequest", "login"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));

        Optional<User> savedUser = userRepository.findByLogin(login);
        Assertions.assertTrue(savedUser.isEmpty());
    }

    private void performRegistrationAndAssertSaveForLogin(String login) throws Exception {
        mockMvc.perform(post(URL_SIGN_UP)
                        .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                        .contextPath(BASE_URL)
                        .param("login", login)
                        .param("password", CORRECT_PASSWORD)
                        .param("passwordConfirm", CORRECT_PASSWORD))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_HOME));

        Optional<User> savedUser = userRepository.findByLogin(login);
        Assertions.assertTrue(savedUser.isPresent());
    }

    private void performRegistrationAndAssertErrorsForPassword(String loginForTest, String password) throws Exception {
        mockMvc.perform(post(URL_SIGN_UP)
                        .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                        .contextPath(BASE_URL)
                        .param("login", loginForTest)
                        .param("password", password)
                        .param("passwordConfirm", password))
                .andDo(print())
                .andExpect(model().attributeHasErrors("userCreationRequest"))
                .andExpect(model().attributeHasFieldErrors("userCreationRequest", "password"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));

        Optional<User> savedUser = userRepository.findByLogin(loginForTest);
        Assertions.assertTrue(savedUser.isEmpty());
    }

    @Test
    public void should_RedirectToHome_When_HasCookies() throws Exception {

        User user = userRepository.findByLogin(INIT_USER_LOGIN).orElseThrow();
        String uuid = sessionService.create(user);

        Cookie cookie = new Cookie(COOKIE_NAME, uuid);

        mockMvc.perform(get(URL_SIGN_UP)
                        .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                        .contextPath(BASE_URL)
                        .cookie(cookie))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_HOME));

        Optional<Session> sessionSaved = sessionRepository.findById(UUID.fromString(uuid));
        Assertions.assertTrue(sessionSaved.isPresent());

        sessionRepository.deleteAll();
    }

    @Test
    public void should_RenderSignUp_When_NoCookies() throws Exception {

        Cookie cookie = new Cookie("test", "");

        mockMvc.perform(get(URL_SIGN_UP)
                        .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                        .contextPath(BASE_URL)
                        .cookie(cookie))
                .andDo(print())
                .andExpect(view().name("sign-up"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));

    }

    @Test
    public void should_RenderSignUp_When_IncorrectCookies() throws Exception {

        Cookie cookie = new Cookie(COOKIE_NAME, "sdfdsfsd");

        mockMvc.perform(get(URL_SIGN_UP)
                        .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                        .contextPath(BASE_URL)
                        .cookie(cookie))
                .andDo(print())
                .andExpect(view().name("sign-up"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));

    }

    @Test
    public void should_HasErrors_When_CreateWithSameLogin() throws Exception {

        String loginForTest = INIT_USER_LOGIN;

        mockMvc.perform(post(URL_SIGN_UP)
                        .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                        .contextPath(BASE_URL)
                        .param("login", loginForTest)
                        .param("password", CORRECT_PASSWORD)
                        .param("passwordConfirm", CORRECT_PASSWORD))
                .andDo(print())
                .andExpect(flash().attribute("error", "User already exists"))
                .andExpect(redirectedUrl(URL_SIGN_UP));

        Optional<User> savedUser = userRepository.findByLogin(loginForTest);
        Assertions.assertTrue(savedUser.isPresent());

    }

    @Test
    public void should_HasErrors_When_PasswordDoNotMatch() throws Exception {

        String loginForTest = "Test_2";
        String incorrectPassword = "incorrectPassword";

        mockMvc.perform(post(URL_SIGN_UP)
                        .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                        .contextPath(BASE_URL)
                        .param("login", loginForTest)
                        .param("password", CORRECT_PASSWORD)
                        .param("passwordConfirm", incorrectPassword))
                .andDo(print())
                .andExpect(flash().attribute("error", "Passwords don't match"))
                .andExpect(redirectedUrl(URL_SIGN_UP));

        Optional<User> savedUser = userRepository.findByLogin(loginForTest);
        Assertions.assertTrue(savedUser.isEmpty());
    }

    @Test
    public void should_attributeHasErrors_When_passwordLessThan6Chars() throws Exception {

        String loginForTest = "12345";
        performRegistrationAndAssertErrorsForLogin(loginForTest);
    }

    @Test
    public void should_FindUserByLogin_After_Creation() throws Exception {

        String loginForTest = "test_2";
        performRegistrationAndAssertSaveForLogin(loginForTest);

    }

    @Test
    public void should_ApplyAtInEmail() throws Exception {

        String loginForTest = "testCorrrectEmail@gmail.com";
        performRegistrationAndAssertSaveForLogin(loginForTest);

    }

    @Test
    public void should_attributeHasErrors_When_LoginHasAt() throws Exception {

        String loginForTest = "login@hasAt";
        performRegistrationAndAssertErrorsForLogin(loginForTest);

    }

    @Test
    public void should_attributeHasErrors_When_LoginMoreThan20Chars() throws Exception {

        String loginForTest = "loginHasMoreThan20Chars";
        performRegistrationAndAssertErrorsForLogin(loginForTest);

    }

    @Test
    public void should_HasErrors_When_PasswordLengthMoreThan64() throws Exception {

        String loginForTest = "Test_2";
        String longPassword = "1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_17_18_19_20_21_22_23_24_25";

        performRegistrationAndAssertErrorsForPassword(loginForTest, longPassword);
    }

    @Test
    public void should_HasErrors_When_PasswordEmpty() throws Exception {

        String loginForTest = "Test_2";
        String longPassword = "";

        performRegistrationAndAssertErrorsForPassword(loginForTest, longPassword);
    }

    @Test
    public void should_HasErrors_When_PasswordIsNull() throws Exception {

        String loginForTest = "Test_2";
        String longPassword = null;

        performRegistrationAndAssertErrorsForPassword(loginForTest, longPassword);
    }

    @Test
    public void should_HasErrors_When_PasswordIsBlank() throws Exception {

        String loginForTest = "Test_2";
        String longPassword = "    ";

        performRegistrationAndAssertErrorsForPassword(loginForTest, longPassword);
    }

    @Test
    public void should_HasErrors_When_LoginNull() throws Exception {

        String loginForTest = null;
        performRegistrationAndAssertErrorsForLogin(loginForTest);

    }

    @Test
    public void should_HasErrors_When_LoginEmpty() throws Exception {

        String loginForTest = "";
        performRegistrationAndAssertErrorsForLogin(loginForTest);

    }

    @Test
    public void should_HasErrors_When_LoginBlank() throws Exception {

        String loginForTest = "  ";
        performRegistrationAndAssertErrorsForLogin(loginForTest);

    }
}
