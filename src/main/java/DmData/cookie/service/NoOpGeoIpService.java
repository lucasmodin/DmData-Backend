package DmData.cookie.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;

//this is a stub for unit-testing
@Service
@Profile("!prod")
public class NoOpGeoIpService implements GeoIpService {
    @Override
    public Map<String, String> lookup(String ip) {
        return Map.of(
                "country", "unknown",
                "city", "unknown"
        );
        }
    }


