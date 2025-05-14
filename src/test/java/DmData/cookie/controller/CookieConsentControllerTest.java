package DmData.cookie.controller;


import DmData.cookie.service.CookieConsentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CookieConsentController.class)
class CookieConsentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    CookieConsentService cookieConsentService;

    @Test
    void getConsent_withoutCookie_returns404() throws Exception {
        mockMvc.perform(get("/api/cookie-consent"))
    }
}