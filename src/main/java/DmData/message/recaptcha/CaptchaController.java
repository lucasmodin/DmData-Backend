package DmData.message.recaptcha;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class CaptchaController {


    private Verifycaptcha verifycaptcha;  // service filen

    public CaptchaController(Verifycaptcha verifycaptcha) {
        this.verifycaptcha = verifycaptcha;
    }

    @PostMapping("/verify-captcha")
    public ResponseEntity<?> handleFormSubmission(@RequestParam("g-recaptcha-response") String captchaResponse) {
        if (!verifycaptcha.verifyCaptcha(captchaResponse)) {  //hvis verificeringen fejler
            return ResponseEntity.badRequest().body("Captcha validation failed");

        }
        return ResponseEntity.ok("Success");
    }
}

