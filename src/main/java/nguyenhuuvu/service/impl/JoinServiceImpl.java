package nguyenhuuvu.service.impl;

import lombok.AllArgsConstructor;
import nguyenhuuvu.entity.JoinEntity;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.entity.ZoomEntity;
import nguyenhuuvu.repository.JoinRepository;
import nguyenhuuvu.repository.UserRepository;
import nguyenhuuvu.service.JoinService;
import nguyenhuuvu.utils.ZoomsUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JoinServiceImpl implements JoinService {
    final JoinRepository joinRepository;
    final UserRepository userRepository;

    @Override
    public JoinEntity findJoinEntityByZoom_IdAndUser_Username(String zoomID, String username) {
        return joinRepository.findJoinEntityByZoom_IdAndUser_Username(zoomID, username);
    }

    @Override
    public List<JoinEntity> findJoinPrivateMessage(String usernameCurrent, String usernameRest) {
        List<JoinEntity> joins = joinRepository.findJoinPrivateMessage(usernameCurrent, usernameRest);
        if (joins == null || joins.size() < 2) {
            // create private chat

            UserEntity userCurrent = userRepository.findUserEntityByUsername(usernameCurrent);
            UserEntity userRest = userRepository.findUserEntityByUsername(usernameRest);
            JoinEntity join1 = new JoinEntity();
            join1.setUser(userCurrent);
            join1.setZoom(ZoomsUtil.createPrivateZoom(userRest));
            joinRepository.save(join1);

            JoinEntity join2 = new JoinEntity();
            join2.setUser(userRest);
            join2.setZoom(ZoomsUtil.createPrivateZoom(userCurrent));
            joinRepository.save(join2);

            joins.add(join1);
            joins.add(join2);
        }
        return joins;
    }
}
