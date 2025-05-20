package DmData.message;

import DmData.message.mail.MailService;
import DmData.message.repositoryMessage.MessageHibernateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DmData.message.model.Message;


import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;

class MessageServiceTest {

    private MessageHibernateRepository messageDB;
    private MailService mailService;
    private MessageService messageService;


    @BeforeEach
    void setUp() {
        messageDB = mock(MessageHibernateRepository.class);
        mailService = mock(MailService.class);
        messageService = new MessageService(messageDB, mailService);
    }

    //Tester om den kan kalder repostiroy engang
    @Test
    void saveMessage_callsRepositorySaveOnce() {
        Message msg = new Message("Test", "test@mail.com", "1234567890", "Hello");
        messageService.saveMessage(msg);
        verify(messageDB, times(1)).save(msg);
    }



    @Test
    void sendAutoMail_callsMailServiceSendEmailOnce() {
        Message msg = new Message("Test", "test@mail.com", "1234567890", "Hello");
        messageService.sendAutoMail(msg);
        verify(mailService, times(1)).sendEmail(msg);
    }



    //Beskeder kan godt være et enkelt anslag
    @Test
    void saveMessage_singleCharacterMessage_saveCalled() {
        Message msg = new Message("Firma", "test@mail.com", "1234567890", "1");
        messageService.saveMessage(msg);
        verify(messageDB, times(1)).save(msg);
    }

    @Test
    void saveMessage_noPhoneNumber_saveCalled() {
        Message msg = new Message("Firma", "test@mail.com", null, "Besked");
        messageService.saveMessage(msg);
        verify(messageDB, times(1)).save(msg);
    }

    @Test
    void saveMessage_noCompanyName_saveCalled() {
        Message msg = new Message(null, "test@mail.com", "1234567890", "Besked");
        messageService.saveMessage(msg);
        verify(messageDB, times(1)).save(msg);
    }

}