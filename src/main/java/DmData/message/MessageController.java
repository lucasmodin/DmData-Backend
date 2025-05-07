package DmData.message;


import DmData.message.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@CrossOrigin("*")
public class MessageController {


    private MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping("saveMessage")
    public ResponseEntity<Message> saveMessage(@RequestBody Message msg){
        Message obj = new Message(msg.getName(), msg.getMail(), msg.getMessage());
        if (msg.getNumber() != null){
            obj.setNumber(msg.getNumber());
        }
        messageService.saveMessage(obj);

        return ResponseEntity.ok(obj);
    }


}
