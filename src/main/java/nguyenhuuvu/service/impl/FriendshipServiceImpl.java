package nguyenhuuvu.service.impl;

import lombok.AllArgsConstructor;
import nguyenhuuvu.entity.FriendshipEntity;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.enums.Friendship;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.repository.FriendshipRepository;
import nguyenhuuvu.repository.UserRepository;
import nguyenhuuvu.service.FriendshipService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    final UserRepository userRepository;
    final FriendshipRepository friendshipRepository;

    @Override
    public FriendshipEntity saveRelationship(String usernameOne, String usernameTwo) {
        if (usernameOne.equals(usernameTwo))
            throw new UserHandleException("Make friends with yourself!", HttpStatus.NOT_ACCEPTABLE);
        FriendshipEntity temp = friendshipRepository.findFriendshipBetweenTwoUser(usernameOne, usernameTwo);
        if (temp != null)
            throw new UserHandleException("The two are already friends!", HttpStatus.NOT_ACCEPTABLE);

        UserEntity userOne = userRepository.findUserEntityByUsername(usernameOne);
        UserEntity userTwo = userRepository.findUserEntityByUsername(usernameTwo);
        if (userTwo == null || userOne == null)
            throw new UserHandleException("User is not exist!", HttpStatus.NOT_ACCEPTABLE);
        FriendshipEntity friendshipEntity = new FriendshipEntity();
        friendshipEntity.setUserOne(userOne);
        friendshipEntity.setUserTwo(userTwo);
        friendshipEntity.setFriendship(Friendship.WAIT_ACCEPT);
        return friendshipRepository.save(friendshipEntity);
    }

    @Override
    public FriendshipEntity acceptRelationship(String usernameOne, String usernameTwo) throws UserHandleException {
        FriendshipEntity friendship = friendshipRepository.findFriendshipBetweenTwoUser(usernameOne, usernameTwo);
        if (friendship == null)
            throw new UserHandleException("Friend request does not exist", HttpStatus.NOT_ACCEPTABLE);
        if (friendship.getFriendship().equals(Friendship.WAIT_ACCEPT)) {
            friendship.setFriendship(Friendship.FRIEND);
            friendshipRepository.save(friendship);
            return friendship;
        } else
            throw new UserHandleException("Already friends!");
    }

    @Override
    public List<FriendshipEntity> findFriendsContainFullName(String usernameCurrent, String fullName, Integer page, Integer size) throws Exception {
        if (size <= 0 || page < 0)
            throw new Exception("Invalid input parameter");
        Pageable pageable = PageRequest.of(page, size);
        return friendshipRepository.findFriendsContainFullName(usernameCurrent, fullName, pageable);
    }

}
