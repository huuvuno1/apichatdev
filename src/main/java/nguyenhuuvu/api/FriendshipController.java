package nguyenhuuvu.api;

import lombok.AllArgsConstructor;
import nguyenhuuvu.entity.FriendshipEntity;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.model.SimpleResponse;
import nguyenhuuvu.service.FriendshipService;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;
import java.util.List;

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

    @GetMapping("/searchs")
    public ResponseEntity<?> search(@RequestParam String q, @RequestParam(required = false, defaultValue = "0") Integer page,
                                    @RequestParam(required = false, defaultValue = "20") Integer size) throws Exception {
        String userCurrent = UserUtil.getUsernameFromCurrentRequest();
        List<FriendshipEntity> friendshipEntities = friendshipService.findFriendsContainFullName(userCurrent, q, page, size);

        return new ResponseEntity<>(friendshipEntities, HttpStatus.OK);
    }
}
