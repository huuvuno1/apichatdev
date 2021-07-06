package nguyenhuuvu.api;

import lombok.RequiredArgsConstructor;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.service.UserService;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(path = {"/api/v1/accounts/profile"})
@RequiredArgsConstructor
public class ProfileController {
    final UserService userService;
    
    @GetMapping
    public ResponseEntity<?> getInfo() {
        String username = UserUtil.getUsernameFromCurrentRequest();
        UserEntity user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UserEntity user) {
        userService.updateProfile(user);
        return new ResponseEntity<>("Update success!", HttpStatus.OK);
    }

    @PostMapping(path = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        if (userService.uploadAvatar(file))
            return new ResponseEntity<>("Successfully!", HttpStatus.OK);
        else
            return new ResponseEntity<>("File upload is null", HttpStatus.NOT_ACCEPTABLE);
    }
}
