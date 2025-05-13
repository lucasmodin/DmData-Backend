package DmData.cookie;

import DmData.cookie.model.CookieConsent;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CookieConsentService {

    private final CookieConsentRepository repo;

    public CookieConsentService(CookieConsentRepository repo) {
        this.repo = repo;
    }

    public CookieConsent saveOrUpdate(String visitorId, boolean analytics, boolean marketing) {
        CookieConsent consent = repo.findByVisitorId(visitorId)
                .orElseGet(() -> {
                    CookieConsent c = new CookieConsent();
                    c.setVisitorId(visitorId);
                    return c;
                });
        consent.setAnalyticsAccepted(analytics);
        consent.setMarketingAccepted(marketing);
        return repo.save(consent);
    }

    public Optional<CookieConsent> findByVisitorId(String visitorId) {
        return repo.findByVisitorId(visitorId);
    }
}
