package DmData.message.mail;

import DmData.message.model.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private String mail = "osbj0001@stud.kea.dk";

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @PostConstruct
    public void test(){
        this.sendEmail(new Message("Oskar Bjerborg", "gay@mail.com","25672072" ,"I like big booty"));
    }

    public void sendEmail(Message msg){
        SimpleMailMessage email = new SimpleMailMessage();

        String message ="Hey DM DataScience,\n" +
                "\n" + msg.getName() + " from big ass company ?\n" +
                "\n" + msg.getMessage() + "\n" +
                "\nYou can reach him on: " + msg.getNumber() + " or mail: " + msg.getMail() + "\n"
                + "\nBest regards" +
                "\nYou homepage !";

        email.setFrom("mySystem");
        email.setTo(mail);
        email.setSubject( msg.getName() + " from: " + "?" + " have send an message");
        email.setText(message);
        javaMailSender.send(email);
    }
}
