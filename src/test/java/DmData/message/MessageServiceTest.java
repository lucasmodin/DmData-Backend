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

    //man kan godt gemme samme besked flere gange.
    @Test
    void saveMessage_callsRepositorySaveMultipleTimes() {
        Message msg = new Message("Test", "test@mail.com", "1234567890", "Hello");
        messageService.saveMessage(msg);
        messageService.saveMessage(msg);
        verify(messageDB, times(2)).save(msg);
    }

    @Test
    void sendAutoMail_callsMailServiceSendEmailOnce() {
        Message msg = new Message("Test", "test@mail.com", "1234567890", "Hello");
        messageService.sendAutoMail(msg);
        verify(mailService, times(1)).sendEmail(msg);
    }

    // --- Man kan godt gemme beskeder i databasen uden validering fra service---
    @Test
    void saveMessage_emptyFields_saveCalled() {
        Message msg1 = new Message("", "test@mail.com", "1234567890", "Besked");
        Message msg2 = new Message("Firma", "", "1234567890", "Besked");
        Message msg3 = new Message("Firma", "test@mail.com", "", "Besked");
        Message msg4 = new Message("Firma", "test@mail.com", "1234567890", "");

        messageService.saveMessage(msg1);
        messageService.saveMessage(msg2);
        messageService.saveMessage(msg3);
        messageService.saveMessage(msg4);

        // Forvent save kald 1 gang per besked
        verify(messageDB, times(4)).save(any(Message.class));
    }

    @Test
    void saveMessage_phoneNumberTooShort_saveCalled() {
        Message msg = new Message("Firma", "test@mail.com", "1234", "Besked");
        messageService.saveMessage(msg);
        verify(messageDB, times(1)).save(msg);
    }

    //Backenden har ingen validering til telefon numerne når de er nået forbi frontenden
    @Test
    void saveMessage_phoneNumberTooLong_saveCalled() {
        Message msg = new Message("Firma", "test@mail.com", "123456789012", "Besked");
        messageService.saveMessage(msg);
        verify(messageDB, times(1)).save(msg);
    }

    //Beskeder kan godt være et enkelt anslag
    @Test
    void saveMessage_singleCharacterMessage_saveCalled() {
        Message msg = new Message("Firma", "test@mail.com", "1234567890", "1");
        messageService.saveMessage(msg);
        verify(messageDB, times(1)).save(msg);
    }

    //Denne test er mest til hvis man når forbi HTML validering i frontenden
    @Test
    void saveMessage_phoneNumberNotNumeric_saveCalled() {
        Message msg = new Message("Firma", "test@mail.com", "ABCD", "Besked");
        messageService.saveMessage(msg);
        verify(messageDB, times(1)).save(msg);
    }

    //Denne test er mest til hvis man når forbi HTML validering i frontenden
    @Test
    void saveMessage_phoneNumberAlphanumeric_saveCalled() {
        Message msg = new Message("Firma", "test@mail.com", "HEJ1", "Besked");
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