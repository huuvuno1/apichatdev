package nguyenhuuvu.service.impl;

import lombok.AllArgsConstructor;
import nguyenhuuvu.entity.JoinEntity;
import nguyenhuuvu.entity.MessageEntity;
import nguyenhuuvu.enums.ZoomType;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.repository.JoinRepository;
import nguyenhuuvu.repository.MessageRepository;
import nguyenhuuvu.service.MessageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    final MessageRepository messageRepository;
    final JoinRepository joinRepository;

    @Override
    public List<MessageEntity> findPrivateMessages(String usernameCurrent, String zoomID, Date beforeTime, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("timeSend"));
        if (beforeTime == null)
            beforeTime = new Date(System.currentTimeMillis() + 1000*10);
        return messageRepository.findPrivateMessages(usernameCurrent, zoomID, ZoomType.PRIVATE, beforeTime, pageable);
    }

    @Override
    public List<MessageEntity> findMessageInGroup(String usernameCurrent, String zoomID, Date beforeTime, Integer page, Integer limit) {
        JoinEntity join = joinRepository.findJoinEntityByZoom_IdAndUser_Username(zoomID, usernameCurrent);
        if (join == null)
            throw new UserHandleException("Users who are not part of the chat group!", HttpStatus.FORBIDDEN);
        Pageable pageable = PageRequest.of(page, limit, Sort.by("timeSend"));
        if (beforeTime == null)
            beforeTime = new Date(System.currentTimeMillis() + 1000*10);
        return messageRepository.findMessagesInGroup(zoomID, beforeTime, pageable);
    }
}
