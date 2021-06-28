package nguyenhuuvu.api;

import lombok.RequiredArgsConstructor;
import nguyenhuuvu.dto.MessageDTO;
import nguyenhuuvu.entity.MessageEntity;
import nguyenhuuvu.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
    final SimpMessagingTemplate messagingTemplate;
    final MessageService messageService;

    @MessageMapping("/send-chat")
    public void sendChat(Principal principal, @Payload MessageDTO message) {
        message.setUsernameSend(principal.getName());
        MessageEntity messageEntity = messageService.save(message);
        message.setAvatarUserSend(messageEntity.getJoin().getUser().getAvatar());
        message.setTimeSend(messageEntity.getTimeSend());
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/chat/listen", message);
    }

    @MessageMapping("/create-zoom")
    public void createZoom(Principal principal) {

    }
}
