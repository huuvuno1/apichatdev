package nguyenhuuvu.service;

import nguyenhuuvu.entity.FriendshipEntity;
import nguyenhuuvu.exception.UserHandleException;

import java.util.List;

public interface FriendshipService {
    FriendshipEntity saveRelationship(String userOne, String userTwo);
    FriendshipEntity acceptRelationship(String userOne, String userTwo);
    List<FriendshipEntity> findFriendsContainFullName(String usernameCurrent, String fullName, Integer page, Integer size) throws Exception;
    List<FriendshipEntity> findRelationshipOfUsername(String username);
}
