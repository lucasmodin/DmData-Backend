package DmData.message;


import DmData.message.model.Message;
import DmData.message.repositoryMessage.MessageHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {


    private MessageHibernateRepository messageDB;

    public MessageService(MessageHibernateRepository messageDB){
        this.messageDB = messageDB;
    }

    public void saveMessage(Message msg){
        messageDB.save(msg);
    }


}
