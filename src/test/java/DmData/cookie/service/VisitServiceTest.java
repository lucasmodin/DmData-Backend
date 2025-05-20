package DmData.cookie.service;

import DmData.cookie.model.Visit;
import DmData.cookie.repository.VisitRepository;
import DmData.util.HashUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VisitServiceTest {

    @Mock
    VisitRepository repo;
    @Mock
    GeoIpService geoIp;
    @InjectMocks
    VisitService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void logVisit_savesPseudonymizedAndGeo() {
        String visitorId = "v123";
        String hashedId = HashUtil.sha256(visitorId);
        String rawIp = "8.8.8.8";

        //stub
        when(geoIp.lookup(rawIp)).thenReturn(Map.of("country", "US", "city", "NYC"));

        //execute
        service.logVisit(visitorId, rawIp);

        //cap and save
        ArgumentCaptor<Visit> cap = ArgumentCaptor.forClass(Visit.class);
        verify(repo).save(cap.capture());

        Visit v = cap.getValue();
        assert v.getVisitorHash().equals(hashedId);
        assert v.getCountry().equals("US");
        assert v.getCity().equals("NYC");
    }
}