package DmData.cookie;

import DmData.cookie.model.Visit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
        // 1) Pseudonymiser IP
        String pseudoIp = pseudonymizeIp(ip);

        // 2) Geolokation baseret på rå IP (eller drop city hvis du vil være mere anonym)
        Map<String,String> geo = geoIp.lookup(ip);

        // 3) Gem besøget
        Visit v = new Visit();
        v.setVisitorId(visitorId);
        v.setIpAddress(pseudoIp);
        v.setCountry(geo.get("country"));
        v.setCity(geo.get("city"));
        repo.save(v);
    }

    private String pseudonymizeIp(String ip) {
        if (ip == null) return "unknown";

        // IPv4 → fjern sidste octet
        if (ip.contains(".")) {
            String[] parts = ip.split("\\.");
            if (parts.length == 4) {
                return parts[0] + "." + parts[1] + "." + parts[2] + ".0";
            }
        }
        // IPv6 → fjern sidste blok
        if (ip.contains(":")) {
            int idx = ip.lastIndexOf(':');
            if (idx != -1) {
                return ip.substring(0, idx) + ":0";
            }
        }
        // fallback
        return ip;
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
