package nguyenhuuvu.api;

import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.model.JwtResponse;
import nguyenhuuvu.model.Mail;
import nguyenhuuvu.model.SimpleResponse;
import nguyenhuuvu.service.UserService;
import nguyenhuuvu.service.EmailSenderService;
import nguyenhuuvu.service.VerifyService;
import nguyenhuuvu.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static nguyenhuuvu.utils.Constant.VERIFY_ACCOUNT_TIME_EXPIRE;

@RestController
@RequestMapping(path = {"/api/v1/accounts/verification"})
public class VerifyController {

    @Value("${nguyenhuuvu.system.domain}")
    String domain;

    final EmailSenderService emailSenderService;
    final UserService userService;
    final VerifyService verifyService;
    final JwtTokenUtil jwtTokenUtil;

    public VerifyController(EmailSenderService emailSenderService, UserService userService, VerifyService verifyService,
                            JwtTokenUtil jwtTokenUtil) {
        this.emailSenderService = emailSenderService;
        this.userService = userService;
        this.verifyService = verifyService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // fix sau - loi logic
    // chua check token het han chua
    @PostMapping("?action=resend-code")
    public ResponseEntity<?> resendCode(@RequestBody Map<String, String> body) throws MessagingException, IOException, UserHandleException {
        UserEntity user = userService.findUserByEmail(body.get("email"));
        if (user != null) {
            Mail mail = emailSenderService.createMailVerify(user, VERIFY_ACCOUNT_TIME_EXPIRE);
            emailSenderService.sendEmail(mail);
            return new ResponseEntity<>(new SimpleResponse(200, "Đã gửi lại email xác thực"), HttpStatus.OK);
        } else
            throw new UserHandleException("This token does not exist");
    }

    @GetMapping
    public ResponseEntity<?> verifyToLink(@RequestParam("token") String token) {
        boolean isOke = verifyService.verifyToken(token);
        if (isOke)
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/signin?msg=verify-success")).build();
        else
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/url-invalid?msg=expire")).build();
    }

    @PostMapping
    public ResponseEntity<?> verifyToCode(@RequestBody Map<String, String> info) {
        String code = info.get("code");
        String email = info.get("email");
        boolean isOke = verifyService.verifyCode(email, code);
        if (isOke) {
            UserEntity userEntity = userService.findUserByEmail(email);
            String token = jwtTokenUtil.generateToken(userEntity.getUsername());
            return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new SimpleResponse(400, "The verification code is invalid or has expired!"), HttpStatus.NOT_ACCEPTABLE);
    }

}
