package DmData.message;


import DmData.message.mail.MailService;
import DmData.message.model.Message;
import DmData.message.repositoryMessage.MessageHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class MessageService {

    private MessageHibernateRepository messageDB;
    private MailService mailService;

    // +45 og 8 cifre elller en tom streng
    private static final Pattern PHONE_PATTERN = Pattern.compile("^$|^\\+45\\s\\d{8}$");

    public MessageService(MessageHibernateRepository messageDB, MailService mailService){
        this.messageDB = messageDB;
        this.mailService = mailService;
    }

    public void saveMessage(Message msg){
        if (msg.getNumber() != null && !PHONE_PATTERN.matcher(msg.getNumber()).matches()) {
            throw new IllegalArgumentException("Telefonnummer skal være i formatet: +45 12345678.");
        }
        messageDB.save(msg);
    }

    public void sendAutoMail(Message msg){
        mailService.sendEmail(msg);
    }


}
