package DmData.cookie.controller;


import DmData.cookie.dto.CookieConsentDTO;
import DmData.cookie.model.CookieConsent;
import DmData.cookie.service.CookieConsentService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CookieConsentController.class)
class CookieConsentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    CookieConsentService cookieConsentService;

    @Test
    void getConsent_withoutCookie_returns404() throws Exception {
        mockMvc.perform(get("/api/cookie-consent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getConsent_withCookie_returnsDto() throws Exception {
        CookieConsent c = new CookieConsent();
        c.setVisitorId("v1");
        c.setAnalyticsAccepted(true);
        c.setMarketingAccepted(false);
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());
        when(cookieConsentService.findByVisitorId("v1"))
                .thenReturn(Optional.of(c));

        mockMvc.perform(get("/api/cookie-consent")
                .cookie(new Cookie("visitorId", "v1"))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.analyticsAccepted").value(true))
                .andExpect(jsonPath("$.marketingAccepted").value(false));
    }

    @Test
    void postConsent_withoutCookie_setsCookieAndReturnsDTO() throws Exception {
        //arrange dto payload
        CookieConsentDTO c = new CookieConsentDTO();
        c.setAnalyticsAccepted(true);
        c.setMarketingAccepted(false);

        // Stub: når service.saveOrUpdate(...) kaldes, returnér et model-objekt
        CookieConsent saved = new CookieConsent();
        saved.setVisitorId("new-uuid");
        saved.setAnalyticsAccepted(true);
        saved.setMarketingAccepted(false);
        saved.setCreatedAt(LocalDateTime.now());
        saved.setUpdatedAt(LocalDateTime.now());
        when(cookieConsentService.saveOrUpdate(
                anyString(), eq(true), eq(false)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/cookie-consent")
                .header("Origin", "http://localhost:3000")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                  { "analyticsAccepted": true, "marketingAccepted": false}
                """)
            )
                .andExpect(status().isOk())
                // Der skal være en ny cookie med navnet visitorId
                .andExpect(cookie().exists("visitorId"))
                // JSON-svar matcher de værdier, vi satte på din gemte model
                .andExpect(jsonPath("$.analyticsAccepted").value(true))
                .andExpect(jsonPath("$.marketingAccepted").value(false));
        // Verificér at servicen blev kaldt med det genererede visitorId og booleans
        verify(cookieConsentService).saveOrUpdate(anyString(), eq(true), eq(false));
    }

    @Test
    void postConsent_withCookie_reusesExistingIdAndReturnsDto() throws Exception {
        // Arrange: samme som før, men med en kendt visitorId
        CookieConsentDTO dto = new CookieConsentDTO();
        dto.setAnalyticsAccepted(false);
        dto.setMarketingAccepted(true);

        CookieConsent saved = new CookieConsent();
        saved.setVisitorId("v1");
        saved.setAnalyticsAccepted(false);
        saved.setMarketingAccepted(true);
        saved.setCreatedAt(LocalDateTime.now());
        saved.setUpdatedAt(LocalDateTime.now());
        when(cookieConsentService.saveOrUpdate(
                eq("v1"), eq(false), eq(true)))
                .thenReturn(saved);

        // Act & Assert
        mockMvc.perform(post("/api/cookie-consent")
                        .header("Origin", "http://localhost:3000")
                        .cookie(new Cookie("visitorId", "v1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                { "analyticsAccepted": false, "marketingAccepted": true }
            """)
                )
                .andExpect(status().isOk())
                // Fordi vi allerede havde en cookie, skal der IKKE sættes en ny
                .andExpect(cookie().doesNotExist("visitorId"))
                .andExpect(jsonPath("$.analyticsAccepted").value(false))
                .andExpect(jsonPath("$.marketingAccepted").value(true));

        verify(cookieConsentService)
                .saveOrUpdate(eq("v1"), eq(false), eq(true));
    }
}