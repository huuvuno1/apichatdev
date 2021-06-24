package nguyenhuuvu.service;

import nguyenhuuvu.entity.FriendshipEntity;
import nguyenhuuvu.exception.UserHandleException;

public interface FriendshipService {
    FriendshipEntity saveRelationship(String userOne, String userTwo) throws UserHandleException;
    FriendshipEntity acceptRelationship(String userOne, String userTwo) throws UserHandleException;
}
