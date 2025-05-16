package DmData.cookie.service;

import DmData.cookie.model.Visit;
import DmData.cookie.repository.VisitRepository;
import DmData.util.HashUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VisitService {
    private final VisitRepository repo;
    private final GeoIpService geoIp;

    public VisitService(VisitRepository repo, GeoIpService geoIp) {
        this.repo  = repo;
        this.geoIp = geoIp;
    }

    public void logVisit(String visitorId, String ip) {
        // 1) krypter visitorId siden det kan spores tilbage til bruger
        String hashedVisitor = HashUtil.sha256(visitorId);

        // 2) Geolokation baseret på rå IP (eller drop city hvis du vil være mere anonym)
        Map<String,String> geo = geoIp.lookup(ip);

        // 3) Gem besøget
        Visit v = new Visit();
        v.setVisitorHash(hashedVisitor);
        v.setCountry(geo.get("country"));
        v.setCity(geo.get("city"));
        repo.save(v);
    }


    public long totalVisits() {
        return repo.count();
    }

    public long uniqueVisitors() {
        return repo.countDistinctVisitors();
    }

    public Map<String,Long> visitsByCountry() {
        Map<String,Long> result = new HashMap<>();
        for (Object[] row : repo.countByCity()) {
            result.put((String)row[0], (Long)row[1]);
        }
        return result;
    }
}
