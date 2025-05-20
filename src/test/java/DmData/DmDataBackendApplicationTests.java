package DmData;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest
class DmDataBackendApplicationTests {

    //need to mock the mail class due to a postconstruct
    @MockitoBean
    private org.springframework.mail.javamail.JavaMailSender mailSender;

    @Test
    void contextLoads() {
    }

}
