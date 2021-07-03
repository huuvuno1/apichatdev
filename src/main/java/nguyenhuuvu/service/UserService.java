package nguyenhuuvu.service;

import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.enums.Friendship;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserEntity signUpUser(UserEntity user);

    UserEntity updateProfile(UserEntity user);

    boolean uploadAvatar(MultipartFile file) throws IOException;

    UserEntity findUserByEmail(String email);

    List<UserEntity> findAll();

    List<UserEntity> findUserByFullnameOrEmailLimit(String q, Pageable pageable);

    List<UserEntity> findListUsersWithPrams(String username, Friendship typeFriendship, Integer page, Integer limit);

    UserEntity updateVerify(UserEntity user);
}
