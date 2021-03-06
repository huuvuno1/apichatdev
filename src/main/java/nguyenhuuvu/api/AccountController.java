package nguyenhuuvu.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import nguyenhuuvu.dto.UserDTO;
import nguyenhuuvu.entity.RoleEntity;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.model.Mail;
import nguyenhuuvu.service.EmailSenderService;
import nguyenhuuvu.service.UserService;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nguyenhuuvu.utils.Constant.VERIFY_ACCOUNT_TIME_EXPIRE;


@RestController
@RequestMapping(path = {"/api/v1/accounts"})
@AllArgsConstructor
public class AccountController {
    final UserService userService;
    final EmailSenderService emailSenderService;

    @GetMapping("/check/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable("email") String email) {
        UserEntity user = userService.findUserByEmail(email);
        if (user == null)
            return new ResponseEntity<>("Suitable!", HttpStatus.OK);
        else
            return new ResponseEntity<>("Already exist", HttpStatus.NOT_ACCEPTABLE);
    }

    @Operation(description = "Tạo tài khoản mới", parameters = {
            @Parameter(name = "fullname", description = "Bắt buộc"),
            @Parameter(name = "email", description = "Bắt buộc"),
            @Parameter(name = "password", description = "Bắt buộc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tạo tài khoản thành công"),
            @ApiResponse(responseCode = "400", description = "Email này đã liên kết với tài khoản khác")
    })

    @PostMapping
    public ResponseEntity<?> signUpUser(@Valid @NotNull(message = "info not null") @RequestBody UserEntity user) throws MessagingException, IOException {
        // save account
        user = userService.signUpUser(user);

        // send mail verify
        Mail mail = emailSenderService.createMailVerify(user, VERIFY_ACCOUNT_TIME_EXPIRE);
        emailSenderService.sendEmail(mail);

        return new ResponseEntity<>(
                UserDTO
                        .builder()
                        .withUsername(user.getUsername())
                        .withFullname(user.getFullname())
                        .withEmail(user.getEmail())
                        .withGender(user.getGender())
                        .withAddress(user.getAddress())
                        .withBirthday(user.getBirthday())
                        .withRoles(user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.joining(",")))
                        .build(),
                HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        String newPassword = body.get("password");
        String userCurrent = UserUtil.getUsernameFromCurrentRequest();
        userService.changePassword(userCurrent, newPassword);
        return null;
    }
}
