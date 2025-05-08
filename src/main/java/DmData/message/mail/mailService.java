package DmData.message.mail;

import DmData.message.model.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class mailService {

    private final JavaMailSender javaMailSender;
    private String mail = "lucasmodj@gmail.com";

    public mailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @PostConstruct
    public void test(){
        this.sendEmail(new Message("Oskar Bjerborg", "gay@mail.com", "I like big booty"));
    }

    public void sendEmail(Message msg){
        SimpleMailMessage email = new SimpleMailMessage();

        String message ="Hey DM DataScience,\n" +
                "\n" + msg.getName() + " has send you an message" +
                "\n" + msg.getMessage() + "\n" +
                "You can also call him on: " + msg.getNumber() + "or mail: " + msg.getMail()
                + "Best regards \n You homepage !";

        email.setFrom("mySystem");
        email.setTo(mail);
        email.setSubject( msg.getName() + "from: " + "?" + "wants to contact you");
        email.setText(message);
        javaMailSender.send(email);
    }
}
