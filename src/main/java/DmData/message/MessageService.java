package DmData.message;


import DmData.message.mail.MailService;
import DmData.message.model.Message;
import DmData.message.repositoryMessage.MessageHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {


    private MessageHibernateRepository messageDB;
    private MailService mailService;

    public MessageService(MessageHibernateRepository messageDB, MailService mailService){
        this.messageDB = messageDB;
        this.mailService = mailService;
    }

    public void saveMessage(Message msg){
        messageDB.save(msg);
    }

    public void sendAutoMail(Message msg){
        mailService.sendEmail(msg);
    }


}
