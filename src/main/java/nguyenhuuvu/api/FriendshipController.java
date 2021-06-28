package nguyenhuuvu.api;

import lombok.AllArgsConstructor;
import nguyenhuuvu.entity.FriendshipEntity;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.model.SimpleResponse;
import nguyenhuuvu.service.FriendshipService;
import nguyenhuuvu.service.WebSocketService;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendshipController {
    final FriendshipService friendshipService;
    final WebSocketService webSocketService;

    @PostMapping("/add/{username_receive}")
    public ResponseEntity<?> addFriend(@PathVariable(name = "username_receive") String usernameReceive) throws UserHandleException {
        String userCurrent = UserUtil.getUsernameFromCurrentRequest();
        FriendshipEntity relationship = friendshipService.saveRelationship(userCurrent, usernameReceive);
        // send notice socket...
        webSocketService.sendNotice(relationship.getUserOne().getFullname(), relationship.getUserOne().getAvatar(),
                relationship.getUserTwo().getUsername(), "has sent you a friend request",
                "/api/v1/friends/accept/" + relationship.getUserOne().getUsername());
        return new ResponseEntity<>(new SimpleResponse(200, "Success!"), HttpStatus.OK);
    }

    @PutMapping("/accept/{username_send}")
    public ResponseEntity<?> acceptFriend(@PathVariable(name = "username_send") String usernameReceive) throws UserHandleException {
        String userCurrent = UserUtil.getUsernameFromCurrentRequest();
        FriendshipEntity relationship = friendshipService.acceptRelationship(userCurrent, usernameReceive);
        // send notice socket...
        webSocketService.sendNotice(relationship.getUserTwo().getFullname(), relationship.getUserTwo().getAvatar(),
                relationship.getUserOne().getUsername(), "has sent you a friend request",
                "/chat/" + relationship.getUserOne().getUsername());
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
