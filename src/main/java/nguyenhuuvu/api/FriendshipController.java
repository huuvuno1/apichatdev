package nguyenhuuvu.api;

import lombok.AllArgsConstructor;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.model.SimpleResponse;
import nguyenhuuvu.service.FriendshipService;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendshipController {
    final FriendshipService friendshipService;

    @PostMapping("/add")
    public ResponseEntity<?> addFriend(@RequestParam(name = "username_receive") String usernameReceive) throws UserHandleException {
        String userCurrent = UserUtil.getUsernameFromCurrentRequest();
        friendshipService.saveRelationship(userCurrent, usernameReceive);
        // send notice socket..
        return new ResponseEntity<>(new SimpleResponse(200, "Success!"), HttpStatus.OK);
    }

    @PutMapping("/accept")
    public ResponseEntity<?> acceptFriend(@RequestParam(name = "username_send") String usernameReceive) throws UserHandleException {
        String userCurrent = UserUtil.getUsernameFromCurrentRequest();
        friendshipService.acceptRelationship(userCurrent, usernameReceive);
        return new ResponseEntity<>(new SimpleResponse(200, "Success!"), HttpStatus.OK);
    }
}
