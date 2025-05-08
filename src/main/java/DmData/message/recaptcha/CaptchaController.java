package DmData.message.recaptcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    @Autowired
    private Verifycaptcha verifycaptcha;  // service filen

    @PostMapping("/contact") //requestParam er fordi der ikke er JSON objekt endnu
    public ResponseEntity<?> handleFormSubmission(@RequestParam("g-recaptcha-response") String captchaResponse) {
        if (!verifycaptcha.verifyCaptcha(captchaResponse)) {  //hvis verificeringen fejler
            return ResponseEntity.badRequest().body("Captcha validation failed");
        }
        return ResponseEntity.ok("Success");
    }
}

