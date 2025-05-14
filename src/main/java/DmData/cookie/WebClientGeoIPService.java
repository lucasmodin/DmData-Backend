package DmData.cookie;

import DmData.cookie.dto.IpApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
@Profile("prod")
public class WebClientGeoIPService implements GeoIpService{

    private final WebClient webClient;
    private final Duration timeout;

    public WebClientGeoIPService(WebClient.Builder builder,
                                 @Value("${geoip.api.base-url:https://ipapi.co}") String baseUrl,
                                 @Value("${geoip.api.timeout-seconds:2}") long timeoutSeconds) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
        this.timeout = Duration.ofSeconds(timeoutSeconds);

    }

    @Override
    //for ikke at lave kald hele tiden kan vi cache
    @Cacheable(value = "geoip", key = "#ip", unless = "#result['country']=='unknown'")
    public Map<String, String> lookup(String ip) {
        // ip-api endpoint: /json/{ip}?fields=status,country,city
        Mono<IpApiResponse> respMono = webClient.get()
                .uri("/{ip}/json", ip)
                .retrieve()
                .bodyToMono(IpApiResponse.class)
                .timeout(timeout)
                .onErrorResume(e -> Mono.empty());

        IpApiResponse resp = respMono.block();
        if(resp != null) {
            return Map.of(
                    "country", resp.country != null ? resp.country : "unknown",
                    "city", resp.city != null ? resp.city : "unknown"
            );
        } else {
            return Map.of(
                    "country", "unknown",
                    "city", "unknown"
            );
        }
    }


}




