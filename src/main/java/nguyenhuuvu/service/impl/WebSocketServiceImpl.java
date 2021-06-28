package nguyenhuuvu.service.impl;

import lombok.AllArgsConstructor;
import nguyenhuuvu.dto.NoticeDTO;
import nguyenhuuvu.service.WebSocketService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {
    final SimpMessagingTemplate messagingTemplate;
    @Override
    public void sendNotice(String fromUser, String avatar, String toUser, String content, String link) {
        NoticeDTO notice = new NoticeDTO();
        notice.setFromUser(fromUser);
        notice.setAvatar(avatar);
        notice.setContent(content);
        notice.setLink(link);
        notice.setTimeSend(new Date());
        messagingTemplate.convertAndSendToUser(toUser, "/notice/listen", notice);
    }
}
