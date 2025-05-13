package DmData.cookie;

import DmData.cookie.model.CookieConsent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CookieConsentRepository extends JpaRepository<CookieConsent, Long> {

    Optional<CookieConsent> findByVisitorId(String visitorId);
}
