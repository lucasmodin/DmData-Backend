package DmData.message;


import DmData.message.model.Message;
import DmData.message.repositoryMessage.MessageHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitTestData implements CommandLineRunner {

    @Autowired
    MessageHibernateRepository messageDB;

    @Override
    public void run(String... args) throws Exception {

        Message obj1 = new Message("Oskar","osbj0001@stud.kea.dk","GAy as hell boyyyyeee");
        Message obj2 = new Message("Oskar Pedersen","osbj0001@stud.kea.dk","Yihahahaha");

        messageDB.save(obj1);
        messageDB.save(obj2);
    }
}
