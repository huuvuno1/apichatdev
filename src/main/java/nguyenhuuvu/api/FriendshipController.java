package nguyenhuuvu.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import nguyenhuuvu.dto.UserDTO;
import nguyenhuuvu.entity.FriendshipEntity;
import nguyenhuuvu.entity.RoleEntity;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.enums.Friendship;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.model.SimpleResponse;
import nguyenhuuvu.service.FriendshipService;
import nguyenhuuvu.service.UserService;
import nguyenhuuvu.service.WebSocketService;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendshipController {
    final FriendshipService friendshipService;
    final UserService userService;
    final WebSocketService webSocketService;


    @Operation(description = "lấy danh sách user theo điều kiện", parameters = {
            @Parameter(name = "friendship", description = "Cần truyền 1 trong các tham số như: FRIEND, WAIT_ACCEPT, STRANGER, INVITE")
    })
    @GetMapping
    public ResponseEntity<?> fetchUsers(@RequestParam(required = false) Friendship friendship,
                                       @RequestParam(required = false, defaultValue = "0") Integer page,
                                       @RequestParam(required = false, defaultValue = "20") Integer size) throws Exception {
        String userCurrent = UserUtil.getUsernameFromCurrentRequest();
        List<UserEntity> userEntities = userService.findListUsersWithPrams(userCurrent, friendship, page, size);
        if (userEntities == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        List<UserDTO> users = new ArrayList<>();
        userEntities.forEach(u -> {
            users.add(UserDTO
                    .builder()
                    .withUsername(u.getUsername())
                    .withFullname(u.getFullname())
                    .withEmail(u.getEmail())
                    .withGender(u.getGender())
                    .withAddress(u.getAddress())
                    .withBirthday(u.getBirthday())
                    .withRoles(u.getRoles().stream().map(RoleEntity::getName).collect(Collectors.joining(",")))
                    .build());
        });

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

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

    @DeleteMapping("/unfriend/{username}")
    public ResponseEntity<?> unfriend(@PathVariable(name = "username") String usernameFriend) throws UserHandleException {
        String userCurrent = UserUtil.getUsernameFromCurrentRequest();
        friendshipService.removeRelationship(userCurrent, usernameFriend);
        return new ResponseEntity<>(new SimpleResponse(200, "Success!"), HttpStatus.OK);
    }

    @DeleteMapping("/delete_invite/{username}")
    public ResponseEntity<?> deleteInvitation(@PathVariable(name = "username") String username) throws UserHandleException {
        String userCurrent = UserUtil.getUsernameFromCurrentRequest();
        friendshipService.removeRelationship(userCurrent, username);
        return new ResponseEntity<>(new SimpleResponse(200, "Success!"), HttpStatus.OK);
    }


}
