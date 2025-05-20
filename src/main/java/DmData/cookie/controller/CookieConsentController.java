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
        origins = "http://localhost:63342",
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
        //hvis brugerId (cookie) ikke findes
        if (visitorId == null) {
            return ResponseEntity.notFound().build();
        }
        //hvis brugerid (cookie) findes i databasen
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

        //hvis brugeren ikke har en cookie (visitorId), opret en med 1 års varighed:
        if (visitorId == null) {
            visitorId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(VISITOR_COOKIE, visitorId);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*365);
            cookie.setHttpOnly(true);
            //i prod skal setSecure være (true) - vigtigt for sikkerhed, men virker ikke locally da den skal være over https:
            //cookie.setSecure(true);
            response.addCookie(cookie);
        }
        //gem eller opdater i databasen
        CookieConsent saved = service.saveOrUpdate(
                visitorId,
                dto.isAnalyticsAccepted(),
                dto.isMarketingAccepted()
        );
        return ResponseEntity.ok(toDto(saved));
    }

    //map til dto
    private CookieConsentDTO toDto(CookieConsent c) {
        CookieConsentDTO dto = new CookieConsentDTO();
        dto.setAnalyticsAccepted(c.isAnalyticsAccepted());
        dto.setMarketingAccepted(c.isMarketingAccepted());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        return dto;
    }
}