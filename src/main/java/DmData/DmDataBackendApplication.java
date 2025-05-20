package DmData;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DmDataBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmDataBackendApplication.class, args);
    }

}
