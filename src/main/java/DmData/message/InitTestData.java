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

    }
}
