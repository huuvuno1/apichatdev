package nguyenhuuvu.service.impl;

import lombok.AllArgsConstructor;
import nguyenhuuvu.dto.MessageDTO;
import nguyenhuuvu.entity.JoinEntity;
import nguyenhuuvu.entity.MessageEntity;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.entity.ZoomEntity;
import nguyenhuuvu.enums.ZoomType;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.repository.JoinRepository;
import nguyenhuuvu.repository.MessageRepository;
import nguyenhuuvu.repository.UserRepository;
import nguyenhuuvu.repository.ZoomRepository;
import nguyenhuuvu.service.MessageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    final MessageRepository messageRepository;
    final JoinRepository joinRepository;
    final ZoomRepository zoomRepository;
    final UserRepository userRepository;

    @Override
    public List<MessageEntity> findPrivateMessages(String usernameCurrent, String zoomID, Date beforeTime, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("timeSend"));
        if (beforeTime == null)
            beforeTime = new Date(System.currentTimeMillis() + 1000 * 10);
        return messageRepository.findPrivateMessages(usernameCurrent, zoomID, ZoomType.PRIVATE, beforeTime, pageable);
    }

    @Override
    public List<MessageEntity> findMessageInGroup(String usernameCurrent, String zoomID, Date beforeTime, Integer page, Integer limit) {
        JoinEntity join = joinRepository.findJoinEntityByZoom_IdAndUser_Username(zoomID, usernameCurrent);
        if (join == null)
            throw new UserHandleException("Users who are not part of the chat group!", HttpStatus.FORBIDDEN);
        Pageable pageable = PageRequest.of(page, limit, Sort.by("timeSend"));
        if (beforeTime == null)
            beforeTime = new Date(System.currentTimeMillis() + 1000 * 10);
        return messageRepository.findMessagesInGroup(zoomID, beforeTime, pageable);
    }

    /*
           Only handle private message because create zoom to group had been handled
     */
    @Override
    @Transactional
    public MessageEntity save(MessageDTO messageDTO) {
        JoinEntity join = joinRepository
                .findJoinEntityByZoom_IdAndUser_Username(messageDTO.getReceiver(), messageDTO.getUsernameSend());
        if (join == null) {
            UserEntity userReceive = userRepository.findUserEntityByUsername(messageDTO.getReceiver());
            if (userReceive != null) {
                UserEntity userSend = userRepository.findUserEntityByUsername(messageDTO.getUsernameSend());

                ZoomEntity zoom1 = zoomRepository.findZoomEntityById(userReceive.getUsername());
                if (zoom1 == null)
                    zoom1 = new ZoomEntity(userReceive.getUsername(), userReceive.getFullname(), ZoomType.PRIVATE, false, null);

                ZoomEntity zoom2 = zoomRepository.findZoomEntityById(userSend.getUsername());
                if (zoom2 == null)
                    zoom2 = new ZoomEntity(userSend.getUsername(), userSend.getFullname(), ZoomType.PRIVATE, false, null);

                JoinEntity join1 = new JoinEntity(userReceive, false, zoom2, null);
                JoinEntity join2 = new JoinEntity(userSend, false, zoom1, null);
                if (!userReceive.getUsername().equals(userSend.getUsername()))
                    joinRepository.save(join1);
                join2 = joinRepository.save(join2);
                join = join2;
            } else {
                throw new UserHandleException("User receive not found!", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        MessageEntity messageEntity = new MessageEntity(null, messageDTO.getMessage(), new Date(), join);
        return messageRepository.save(messageEntity);
    }


}
