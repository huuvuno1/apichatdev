package nguyenhuuvu.service;

import nguyenhuuvu.dto.MessageDTO;
import nguyenhuuvu.entity.MessageEntity;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface MessageService {
    List<MessageEntity> findPrivateMessages(String usernameCurrent, String zoomID, Date beforeTime, Integer page, Integer limit);
    List<MessageEntity> findMessageInGroup(String usernameCurrent, String zoomID, Date beforeTime, Integer page, Integer limit);
    MessageEntity save(MessageDTO messageDTO);
}
