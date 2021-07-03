package nguyenhuuvu.service.impl;

import lombok.AllArgsConstructor;
import nguyenhuuvu.entity.FriendshipEntity;
import nguyenhuuvu.entity.RoleEntity;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.entity.VerifyEntity;
import nguyenhuuvu.enums.Friendship;
import nguyenhuuvu.exception.DuplicateEmailException;
import nguyenhuuvu.exception.GenericUsernameException;
import nguyenhuuvu.repository.FriendshipRepository;
import nguyenhuuvu.repository.RoleRepository;
import nguyenhuuvu.repository.UserRepository;
import nguyenhuuvu.repository.VerifyRepository;
import nguyenhuuvu.service.UserService;
import nguyenhuuvu.utils.Constant;
import nguyenhuuvu.utils.DateTimeUtil;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final RoleRepository roleRepository;
    final FriendshipRepository friendshipRepository;
    final VerifyRepository verifyRepository;

    @Transactional
    public UserEntity signUpUser(UserEntity user) {
        UserEntity userCheck = userRepository.findUserEntityByEmail(user.getEmail());
        if (userCheck != null) {
            throw new DuplicateEmailException("Email is exist!");
        }

        // create username random and check
        String username = null;
        boolean isError = true;
        for (int i = 1; i <= 10; i++) {
            username = UserUtil.createUsername(user.getFullname());
            UserEntity temp = userRepository.findUserEntityByUsername(username);
            if (temp == null) {
                isError = false;
                break;
            }
        }

        if (isError)
            throw new GenericUsernameException();

        user.setEnabled(false);
        user.setUsername(username);

        // role`s user is ROLE_USER
        RoleEntity roleUser = roleRepository.findRoleEntityByName(Constant.ROLE_USER);
        if (roleUser == null) {
            roleUser = new RoleEntity();
            roleUser.setName(Constant.ROLE_USER);
        }
        user.setRoles(Stream.of(roleUser).collect(Collectors.toList()));
        roleUser.setUsers(Stream.of(user).collect(Collectors.toList()));
        String token = UserUtil.generateToken();
        String code = UserUtil.generateCode().toString();
        VerifyEntity verify = new VerifyEntity(token, code, new Date(System.currentTimeMillis() + Constant.TIME_VERIFY_SIGNUP), false, user);
        user.setVerifyEntity(verify);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserEntity updateProfile(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public boolean uploadAvatar(MultipartFile file) throws IOException {
        if (file != null) {
            File convertFile = new File("src/main/resources/static/file/" + file.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
            fileOutputStream.write(file.getBytes());
            fileOutputStream.close();
            System.out.println(convertFile.getAbsolutePath());
            return true;
        }
        return false;
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findUserEntityByEmail(email);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserEntity> findUserByFullnameOrEmailLimit(String q, Pageable pageable) {
        return userRepository.searchUsersLikeFullNameOrEqualEmail(q, q, pageable);
    }

    @Override
    public List<UserEntity> findListUsersWithPrams(String username, Friendship typeFriendship, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit);
        if (typeFriendship == null)
            typeFriendship = Friendship.FRIEND;

        List<FriendshipEntity> friendshipEntities = null;

        switch (typeFriendship) {
            case STRANGER:
                return userRepository.findUserEntitiesNotFriendWithUser(username, pageable);
            case FRIEND:
                friendshipEntities = friendshipRepository.findFriendshipEntities(username, typeFriendship, pageable);
                break;
            case INVITE:
                friendshipEntities = friendshipRepository.findFriendshipEntitiesByUserTwo_UsernameAndFriendship(username, Friendship.WAIT_ACCEPT);
                break;
            case WAIT_ACCEPT:
                friendshipEntities = friendshipRepository.findFriendshipEntitiesByUserOne_UsernameAndFriendship(username, Friendship.WAIT_ACCEPT);
                break;
            default:
                return null;
        }

        List<UserEntity> userEntities = new ArrayList<>();
        friendshipEntities.forEach((f) -> {
            UserEntity u = f.getUserOne().getUsername().equals(username) ? f.getUserTwo() : f.getUserOne();
            userEntities.add(new UserEntity(
                    u.getUsername(),
                    u.getEmail(),
                    u.getFullname(),
                    u.getGender(),
                    u.getBirthday(),
                    u.getAddress(),
                    u.getAvatar(),
                    u.getRoles()
            ));
        });
        return userEntities;
    }

    @Override
    public UserEntity updateVerify(UserEntity user) {
        if (user.getVerifyEntity().getTimeExpire().before(new Date())) {
            String token = UserUtil.generateToken();
            String code = UserUtil.generateCode().toString();
            VerifyEntity verify = new VerifyEntity(user.getVerifyEntity().getId(), token,
                    code, new Date(System.currentTimeMillis() + Constant.TIME_VERIFY_SIGNUP), false, user);
            user.setVerifyEntity(verify);
            userRepository.save(user);
        }
        return user;
    }
}
