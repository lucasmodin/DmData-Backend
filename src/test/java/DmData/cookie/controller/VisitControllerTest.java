package DmData.cookie.controller;

import DmData.cookie.model.CookieConsent;
import DmData.cookie.service.VisitService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitController.class)
class VisitControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    VisitService visitService;

    @Test
    void postLogVisit_setsVisitorIdCookie_andCallsService() throws Exception {
        mockMvc.perform( post("/api/visits")
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("visitorId"));
        verify(visitService).logVisit(anyString(), anyString());
    }

    @Test
    void postLogVisit_reusesExistingCookie() throws Exception {
        // Vi sender en visitorId=abc-123 i request
        mockMvc.perform(post("/api/visits")
                        .header("Origin", "http://localhost:3000")
                        .cookie(new Cookie("visitorId", "abc-123"))
                )
                .andExpect(status().isOk())
                // Forvent ingen ny Set-Cookie header, da vi genbruger den eksisterende
                .andExpect(cookie().doesNotExist("visitorId"));

        // Servicen skal være kaldt med netop "abc-123"
        verify(visitService).logVisit(eq("abc-123"), anyString());
    }

    @Test
    void getStats_returnsJsonMetrics() throws Exception {
        when(visitService.totalVisits()).thenReturn(42L);
        when(visitService.uniqueVisitors()).thenReturn(10L);
        when(visitService.visitsByCountry())
                .thenReturn(Map.of("DK", 7L, "US", 35L));

        mockMvc.perform(get("/api/visits/stats")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalVisits").value(42))
                .andExpect(jsonPath("$.uniqueVisitors").value(10))
                .andExpect(jsonPath("$.byCountry.DK").value(7))
                .andExpect(jsonPath("$.byCountry.US").value(35));
    }

}