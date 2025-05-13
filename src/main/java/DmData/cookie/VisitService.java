package DmData.cookie;

import DmData.cookie.model.Visit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisitService {
    private final VisitRepository visitRepository;
    private final GeoIpService geoIpService;

    public VisitService(VisitRepository visitRepository, GeoIpService geoIpService) {
        this.visitRepository = visitRepository;
        this.geoIpService = geoIpService;
    }

    public void logVisit(String visitorId, String ipAdress) {
        Visit v = new Visit();
        v.setVisitorId(visitorId);
        v.setIpAddress(ipAdress);

        //geo-lokation
        Map<String, String> geo = geoIpService.lookup(ipAdress);
        v.setCountry(geo.get("country"));
        v.setCity(geo.get("city"));

        visitRepository.save(v);
    }

    public long getTotalVisits() {
        return visitRepository.count();
    }

    public long getUniqueVisitors() {
        return visitRepository.countDistinctVisitors();
    }

    public Map<String,Long> getVisitsByCountry() {
        List<Object[]> rows = visitRepository.countByCity();
        Map<String,Long> m = new HashMap<>();
        for (Object[] row : rows) {
            m.put((String)row[0], (Long)row[1]);
        }
        return m;
    }
}
