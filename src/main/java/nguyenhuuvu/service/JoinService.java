package nguyenhuuvu.service;

import nguyenhuuvu.entity.JoinEntity;

import java.util.List;

public interface JoinService {
    JoinEntity findJoinEntityByZoom_IdAndUser_Username(String zoomID, String username);
    List<JoinEntity> findJoinPrivateMessage(String usernameCurrent, String usernameRest);
}
