package DmData.cookie;

import java.util.Map;

public interface GeoIpService {
    Map<String, String> lookup(String ipAdress);
}
