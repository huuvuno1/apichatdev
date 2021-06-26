package nguyenhuuvu.api;

import lombok.AllArgsConstructor;
import nguyenhuuvu.dto.MessageDTO;
import nguyenhuuvu.service.JoinService;
import nguyenhuuvu.service.MessageService;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessagesController {
    final MessageService messageService;

    @GetMapping("/specific/{username}")
    public ResponseEntity<?> getPrivateMessage(@PathVariable("username") String usernameClick,
                                               @RequestParam(value = "before_time", required = false) Date date,
                                               @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                               @RequestParam(value = "limit", required = false, defaultValue = "30") Integer limit) {
        String usernameCurrent = UserUtil.getUsernameFromCurrentRequest();
        List<MessageDTO> messages = new ArrayList<>();
        messageService.findPrivateMessages(usernameCurrent, usernameClick, date, page, limit).stream()
                .forEach(m -> {
                    messages.add(MessageDTO
                            .builder()
                            .withUsernameSend(m.getJoin().getUser().getUsername())
                            .withAvatarUserSend(m.getJoin().getUser().getAvatar())
                            .withMessage(m.getMessage())
                            .withTimeSend(m.getTimeSend())
                            .build()
                    );
                });
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    @GetMapping("/group/{group_id}")
    public ResponseEntity<?> getMessagesInGroup(@PathVariable("group_id") String groupID,
                                               @RequestParam(value = "before_time", required = false) Date date,
                                               @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                               @RequestParam(value = "limit", required = false, defaultValue = "30") Integer limit) {
        String usernameCurrent = UserUtil.getUsernameFromCurrentRequest();
        List<MessageDTO> messages = new ArrayList<>();
        messageService.findMessageInGroup(usernameCurrent, groupID, date, page, limit).stream()
                .forEach(m -> {
                    messages.add(MessageDTO
                            .builder()
                            .withUsernameSend(m.getJoin().getUser().getUsername())
                            .withAvatarUserSend(m.getJoin().getUser().getAvatar())
                            .withMessage(m.getMessage())
                            .withTimeSend(m.getTimeSend())
                            .build()
                    );
                });
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

}
