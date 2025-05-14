package DmData.cookie.controller;
import DmData.cookie.dto.CookieConsentDTO;
import DmData.cookie.model.CookieConsent;
import DmData.cookie.service.CookieConsentService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/cookie-consent")
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
public class CookieConsentController {
    private final CookieConsentService service;
    private static final String VISITOR_COOKIE = "visitorId";

    public CookieConsentController(CookieConsentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CookieConsentDTO> getConsent(
            @CookieValue(value = VISITOR_COOKIE, required = false) String visitorId) {
        if (visitorId == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<CookieConsent> maybe = service.findByVisitorId(visitorId);
        return maybe
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CookieConsentDTO> setConsent(
            @CookieValue(value = VISITOR_COOKIE, required = false) String visitorId,
            @RequestBody CookieConsentDTO dto,
            HttpServletResponse response) {

        if (visitorId == null) {
            visitorId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(VISITOR_COOKIE, visitorId);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*365);
            response.addCookie(cookie);
        }

        CookieConsent saved = service.saveOrUpdate(
                visitorId,
                dto.isAnalyticsAccepted(),
                dto.isMarketingAccepted()
        );
        return ResponseEntity.ok(toDto(saved));
    }

    private CookieConsentDTO toDto(CookieConsent c) {
        CookieConsentDTO dto = new CookieConsentDTO();
        dto.setAnalyticsAccepted(c.isAnalyticsAccepted());
        dto.setMarketingAccepted(c.isMarketingAccepted());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        return dto;
    }
}