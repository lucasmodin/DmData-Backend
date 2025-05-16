package DmData.message;

import DmData.message.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageControllerTest {

    private MessageService messageService;
    private MessageController messageController;

    @BeforeEach
    void setUp() {
        messageService = mock(MessageService.class);
        messageController = new MessageController(messageService);
    }

    //non-null message
    @Test
    void saveMessage_validMessage_shouldReturnOk() {
        // Arrange
        Message msg = new Message("Kristoffer", "mail@example.com", "Hello from test");

        // Act
        ResponseEntity<String> response = messageController.saveMessage(msg);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("obj saved", response.getBody());
        verify(messageService, times(1)).sendAutoMail(msg);
        verify(messageService, times(1)).saveMessage(any(Message.class));
    }

    //null message (200  betyder at den at den sendes)
    @Test
    void saveMessage_nullMessage_shouldReturnBadRequest() {
        ResponseEntity<String> response = messageController.saveMessage(null);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Missing require data", response.getBody());
        verifyNoInteractions(messageService);
    }

    //test om man kan sende en besked der består af et enkelt anslag
    @Test
    void saveMessage_oneCharacter_shouldReturnOk() {
        Message msg = new Message("Kristoffer", "mail@example.com", "1");
        ResponseEntity<String> response = messageController.saveMessage(msg);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("obj saved", response.getBody());
        verify(messageService, times(1)).sendAutoMail(msg);
        verify(messageService, times(1)).saveMessage(any(Message.class));
    }

    //+ 46 test er i princippet i strid med HTML formattet
    @Test
    void saveMessage_phoneNumberWithPlus46_shouldReturnOk() {
        // Arrange
        Message msg = new Message("Kristoffer", "mail@example.com", "International number test");
        msg.setNumber("+46701234567");

        // Act
        ResponseEntity<String> response = messageController.saveMessage(msg);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("obj saved", response.getBody());

        verify(messageService).sendAutoMail(msg);
        verify(messageService).saveMessage(argThat(savedMsg ->
                "+46701234567".equals(savedMsg.getNumber())
        ));
    }

    @Test
    void saveMessage_phoneNumberTooShort_shouldStillReturnOk() {
        // Arrange
        Message msg = new Message("Kristoffer", "mail@example.com", "Short number test");
        msg.setNumber("1234"); //det er ikke muligt at have et telefon nummer der kun er 4 cifre

        // Act
        ResponseEntity<String> response = messageController.saveMessage(msg);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("obj saved", response.getBody());

        verify(messageService).sendAutoMail(msg);
        verify(messageService).saveMessage(argThat(savedMsg ->
                "1234".equals(savedMsg.getNumber())
        ));
    }

    @Test
    void saveMessage_phoneNumberIsNotNumeric_shouldStillReturnOk() {
        // Arrange
        Message msg = new Message("Kristoffer", "mail@example.com", "Non-numeric number test");
        msg.setNumber("Hello");

        // Act
        ResponseEntity<String> response = messageController.saveMessage(msg);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("obj saved", response.getBody());

        verify(messageService).sendAutoMail(msg);
        verify(messageService).saveMessage(argThat(savedMsg ->
                "Hello".equals(savedMsg.getNumber())
        ));
    }


    @Test
    void saveMessage_phoneNumberWithASingleNumber_shouldReturnOk() {
        // Arrange
        Message msg = new Message("Kristoffer", "mail@example.com", "Non-numeric number test");
        msg.setNumber("Hello1");

        // Act
        ResponseEntity<String> response = messageController.saveMessage(msg);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("obj saved", response.getBody());

        verify(messageService).sendAutoMail(msg);
        verify(messageService).saveMessage(argThat(savedMsg ->
                "Hello".equals(savedMsg.getNumber())
        ));
    }

    @Test
    void saveMessage_invalidPhoneNumberFormat_shouldReturnBadRequest() {
        Message msg = new Message("Kristoffer", "mail@example.com", "Bad number");
        msg.setNumber("Hello");

        ResponseEntity<String> response = messageController.saveMessage(msg);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid phone number format", response.getBody());

        verifyNoInteractions(messageService);
    }

    //Test hvis man kan sende tomme felter ind i backenden
    @Test
    void saveMessage_emptyFields_shouldReturnOk() {
        Message msg = new Message("", "", "");
        ResponseEntity<String> response = messageController.saveMessage(msg);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("obj saved", response.getBody());
    }


    //Test om man man sende en besked til backenden uden et telefon nummber
    //Den virker faktisk.
    @Test
    void saveMessage_withoutPhoneNumber_shouldReturnOk() {
        Message msg = new Message("Kristoffer", "mail@example.com", "No phone number test");

        ResponseEntity<String> response = messageController.saveMessage(msg);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("obj saved", response.getBody());

        verify(messageService).sendAutoMail(msg);
        verify(messageService).saveMessage(argThat(savedMsg -> savedMsg.getNumber() == null));
    }


}