package nguyenhuuvu.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class WebSocketController {
    final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/hello")
    public void testGroup(String text, SimpMessageHeaderAccessor accessor) {
        messagingTemplate.convertAndSendToUser(accessor.getUser().getName(), "/topic/greetings", "vu day");
    }
}
