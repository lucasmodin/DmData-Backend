package DmData.message;


import DmData.message.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")

public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping("saveMessage")
    public ResponseEntity<String> saveMessage(@RequestBody Message msg) {
        if (msg != null) {
            try {
                Message obj = new Message(msg.getName(), msg.getMail(), msg.getMessage());
                if (msg.getNumber() != null) {
                    obj.setNumber(msg.getNumber());
                }
                messageService.sendAutoMail(msg);
                messageService.saveMessage(obj);
                return ResponseEntity.ok("obj saved");
            } catch (IllegalArgumentException e) { //Hvis en validering slår fejl
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("Missing required data");
        }
    }


}
