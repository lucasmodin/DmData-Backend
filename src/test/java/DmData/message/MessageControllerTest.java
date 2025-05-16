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


}