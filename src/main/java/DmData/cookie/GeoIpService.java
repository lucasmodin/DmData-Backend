package DmData.cookie;

import org.springframework.stereotype.Service;

import java.util.Map;


public interface GeoIpService {
    Map<String, String> lookup(String ipAdress);
}
