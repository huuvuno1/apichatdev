package nguyenhuuvu.service.impl;

import lombok.AllArgsConstructor;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.entity.VerifyEntity;
import nguyenhuuvu.exception.DuplicateEmailException;
import nguyenhuuvu.exception.GenericUsernameException;
import nguyenhuuvu.repository.UserRepository;
import nguyenhuuvu.service.UserService;
import nguyenhuuvu.utils.Constant;
import nguyenhuuvu.utils.DateTimeUtil;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

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

        String token = UserUtil.generateToken();
        String code = UserUtil.generateCode().toString();
        VerifyEntity verify = new VerifyEntity(token, code, DateTimeUtil.calculateExpiryDate(Constant.TIME_VERIFY_SIGNUP), false, user);
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
}
