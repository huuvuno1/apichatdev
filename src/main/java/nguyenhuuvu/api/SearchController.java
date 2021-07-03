package nguyenhuuvu.api;

import lombok.AllArgsConstructor;
import nguyenhuuvu.dto.UserDTO;
import nguyenhuuvu.entity.FriendshipEntity;
import nguyenhuuvu.entity.RoleEntity;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.enums.Friendship;
import nguyenhuuvu.service.FriendshipService;
import nguyenhuuvu.service.UserService;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/searchs")
public class SearchController {
    final UserService userService;
    final FriendshipService friendshipService;

    // search list user for full name or email
    @GetMapping
    public ResponseEntity<?> search(@RequestParam String q, @RequestParam(required = false, defaultValue = "10") Integer limit) {
        Pageable pageable = PageRequest.of(0, limit);
        String usernameCurrent = UserUtil.getUsernameFromCurrentRequest();
        List<FriendshipEntity> friendships = friendshipService.findRelationshipOfUsername(usernameCurrent);
        List<UserDTO> users = new ArrayList<>();
        userService.findUserByFullnameOrEmailLimit(q, pageable).stream().forEach((u) -> {
            long count = friendships.stream().filter(f -> {
                return f.getUserOne().getUsername().equals(u.getUsername()) || f.getUserTwo().getUsername().equals(u.getUsername());
            }).count();
            Friendship friendship = count > 0 ? Friendship.FRIEND : Friendship.STRANGER;
            users.add(UserDTO
                    .builder()
                    .withUsername(u.getUsername())
                    .withFullname(u.getFullname())
                    .withEmail("Not displayed for security reasons!")
                    .withGender(u.getGender())
                    .withAddress(u.getAddress())
                    .withFriendship(friendship)
                    .withRoles(u.getRoles().stream().map(RoleEntity::getName).collect(Collectors.joining(",")))
                    .build());
        });
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // search list friends for name
    @GetMapping("/friends")
    public ResponseEntity<?> searchFriends(@RequestParam String q, @RequestParam(required = false, defaultValue = "0") Integer page,
                                           @RequestParam(required = false, defaultValue = "20") Integer size) throws Exception {
        String userCurrent = UserUtil.getUsernameFromCurrentRequest();
        List<FriendshipEntity> friendshipEntities = friendshipService.findFriendsContainFullName(userCurrent, q, page, size);
        List<UserDTO> users = new ArrayList<>();
        friendshipEntities.stream().forEach(f -> {
            UserEntity u = f.getUserOne().getUsername().equals(userCurrent) ? f.getUserTwo() : f.getUserOne();
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
}
