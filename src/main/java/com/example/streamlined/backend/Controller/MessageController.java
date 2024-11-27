package com.example.streamlined.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.streamlined.backend.Entity.MessageEntity;
import com.example.streamlined.backend.Service.MessageService;


@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = {
    "http://localhost:5173", "http://localhost:3000", // Development environment
    "https://streamlined-service-portal-4amnsogyi-deployed-projects-4069a065.vercel.app","https://streamlined-service-portal.vercel.app/" // Production environment
}, allowCredentials = "true")
public class MessageController {
    @Autowired
    private MessageService mserv;

    @Autowired
    private SimpMessagingTemplate smtemp;

   @PostMapping
   public ResponseEntity<MessageEntity> createMessage(@RequestBody MessageEntity message){
        MessageEntity createdMessage = mserv.createMessage(message);
        return ResponseEntity.ok(createdMessage);
   }

   @GetMapping
   public ResponseEntity<List<MessageEntity>> getAllMessages(){
        List<MessageEntity> message = mserv.getAllMessages();
        return ResponseEntity.ok(message);
   }

   @GetMapping("/getMessage/{messageId}")
   public ResponseEntity<MessageEntity> getMessageById(@PathVariable Long messageId){
        return mserv.getMessageById(messageId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
   }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<MessageEntity>> getUnreadMessages(@PathVariable Long userId) {
        List<MessageEntity> unreadMessages = mserv.getUnreadMessagesForUser(userId);
        return ResponseEntity.ok(unreadMessages);
   }

   @GetMapping("/unread-recent/{userId}")
    public ResponseEntity<List<MessageEntity>> getUnreadAndRecentMessagesForUser(@PathVariable Long userId) {
        List<MessageEntity> messages = mserv.getUnreadAndRecentMessagesForUser(userId);
        return ResponseEntity.ok(messages);
    }

//    @GetMapping("/recent/{userId}/{senderId}")
//    public ResponseEntity<List<MessageEntity>> getRecentMessages(@PathVariable Long userId, @PathVariable String sender) {
//        List<MessageEntity> recentMessages = mserv.getRecentMessagesForUser(userId, sender);
//        return ResponseEntity.ok(recentMessages);
//    }
   

    @PutMapping("/markAsRead")
    public ResponseEntity<Void> markMessagesAsRead(@RequestBody List<Long> messageIds) {
        mserv.markMessagesAsRead(messageIds);
        return ResponseEntity.ok().build();
     }


   @PutMapping("/updateMessage/{messageId}")
   public ResponseEntity<MessageEntity> updateMessage(@PathVariable Long messageId, @RequestBody MessageEntity messageDetails){
        try{
            MessageEntity updatedMessage = mserv.updateMessage(messageId, messageDetails);
            return ResponseEntity.ok(updatedMessage);
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
   }

   @DeleteMapping("/deleteMessage/{messageId}")
   public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId){
        mserv.deleteMessage(messageId);
        return ResponseEntity.notFound().build();
   }



   public void sendMessage(MessageEntity message) {
     // Save the message to the database
     mserv.createMessage(message);
 
     // Send the message to the receiver via WebSocket
     smtemp.convertAndSendToUser(
         message.getReceiver(), 
         "/queue/messages",     
         message                
     );
 }

}