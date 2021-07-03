package nguyenhuuvu.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.entity.VerifyEntity;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.model.JwtResponse;
import nguyenhuuvu.model.Mail;
import nguyenhuuvu.model.SimpleResponse;
import nguyenhuuvu.service.UserService;
import nguyenhuuvu.service.EmailSenderService;
import nguyenhuuvu.service.VerifyService;
import nguyenhuuvu.utils.Constant;
import nguyenhuuvu.utils.DateTimeUtil;
import nguyenhuuvu.utils.JwtTokenUtil;
import nguyenhuuvu.utils.UserUtil;
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
import java.util.Date;
import java.util.Map;

import static nguyenhuuvu.utils.Constant.VERIFY_ACCOUNT_TIME_EXPIRE;

@RestController
@RequestMapping(path = {"/api/v1/accounts/verification"})
@RequiredArgsConstructor
public class VerifyController {

    @Value("${nguyenhuuvu.system.domain}")
    String domain;

    final EmailSenderService emailSenderService;
    final UserService userService;
    final VerifyService verifyService;
    final JwtTokenUtil jwtTokenUtil;

    // fix sau - loi logic
    // chua check token het han chua
    @Operation(description = "Gửi lại email verify", parameters = {
            @Parameter(name = "email", description = "Bắt buộc")
    })
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendCode(@RequestBody Map<String, String> body) throws MessagingException, IOException, UserHandleException {
        UserEntity user = userService.findUserByEmail(body.get("email"));
        if (user != null) {
            user = userService.updateVerify(user);
            Mail mail = emailSenderService.createMailVerify(user, VERIFY_ACCOUNT_TIME_EXPIRE);
            emailSenderService.sendEmail(mail);
            return new ResponseEntity<>(new SimpleResponse(200, "Đã gửi lại email xác thực"), HttpStatus.OK);
        } else
            throw new UserHandleException("This  user does not exist", HttpStatus.NOT_ACCEPTABLE);
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
