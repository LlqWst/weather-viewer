package controller_test;

import config.TestPersistenceConfig;
import dev.lqwd.configuration.WebMvcConfig;
import dev.lqwd.controllers.RegistrationController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {TestPersistenceConfig.class, WebMvcConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
public class RegistrationControllerTest {

    private AutoCloseable closeable;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @InjectMocks
    private RegistrationController registrationController;


    @BeforeEach
    public void setUp(){
        closeable = MockitoAnnotations.openMocks(this);

        AutowireCapableBeanFactory beanFactory = webApplicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(registrationController);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void should_attributeHasErrors_When_passwordLessThan6Chars() throws Exception {

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                        .param("login", "66")
                        .param("password", "666666")
                        .param("passwordConfirm", "666666"))
                .andDo(print())
                .andExpect(model().attributeHasErrors("userCreationRequest"))
                .andExpect(model().attributeHasFieldErrors("userCreationRequest", "login"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

}
