package nguyenhuuvu.controller.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/test")
    public String testGroup(String text) {
        return "hello: " + text;
    }
}
