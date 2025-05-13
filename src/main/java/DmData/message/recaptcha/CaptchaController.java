package DmData.message.recaptcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class CaptchaController {

    @Autowired
    private Verifycaptcha verifycaptcha;  // service filen

    @PostMapping("/verify-captcha")
    public ResponseEntity<?> handleFormSubmission(@RequestParam("g-recaptcha-response") String captchaResponse) {
        if (!verifycaptcha.verifyCaptcha(captchaResponse)) {  //hvis verificeringen fejler
            return ResponseEntity.badRequest().body("Captcha validation failed");
        }
        return ResponseEntity.ok("Success");
    }
}

