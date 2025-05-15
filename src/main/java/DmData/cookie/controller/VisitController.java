package DmData.cookie.controller;
import DmData.cookie.service.VisitService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/visits")
@CrossOrigin(
        origins = "http://localhost:63342",
        allowCredentials = "true"
)
public class VisitController {
    private static final String VISITOR_COOKIE = "visitorId";
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    /**
     * POST for at logge et visit.
     * Sætter visitorId-cookie hvis den ikke findes.
     */
    @PostMapping
    public void logVisit(
            @CookieValue(value = VISITOR_COOKIE, required = false) String visitorId,
            HttpServletRequest req,
            HttpServletResponse resp) {

        if (visitorId == null) {
            visitorId = UUID.randomUUID().toString();
            Cookie c = new Cookie(VISITOR_COOKIE, visitorId);
            c.setPath("/");
            c.setMaxAge(60 * 60 * 24 * 365);
            c.setHttpOnly(true);
            //i prod skal setSecure være (true) - vigtigt for sikkerhed, men virker ikke locally da den skal være over https:
            //c.setSecure(true);
            resp.addCookie(c);
        }

        // Hent brugerens IP (bag proxy)
        String ip = req.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = req.getRemoteAddr();
        }

        // Servicen gemmer landet/by
        visitService.logVisit(visitorId, ip);
    }

    /** GET statistik for admin */
    @GetMapping("/stats")
    public Map<String,Object> stats() {
        return Map.of(
                "totalVisits",    visitService.totalVisits(),
                "uniqueVisitors", visitService.uniqueVisitors(),
                "byCountry",      visitService.visitsByCountry()
        );
    }
}

