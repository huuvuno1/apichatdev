package nguyenhuuvu.service;

import nguyenhuuvu.exception.UserHandleException;

public interface VerifyService {
    boolean verifyToken(String token);

    boolean verifyCode(String email, String code) throws UserHandleException;
}
