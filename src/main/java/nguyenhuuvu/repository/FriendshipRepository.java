package nguyenhuuvu.repository;

import nguyenhuuvu.entity.FriendshipEntity;
import nguyenhuuvu.enums.Friendship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    List<FriendshipEntity>
    findFriendshipEntitiesByUserOne_UsernameOrUserTwo_Username(String param1, String param2);

    // query list friends has status like as FRIEND, WAIT_ACCEPT
    @Query("select f from FriendshipEntity f " +
            "where (f.userOne.username = :username or f.userTwo.username = :username) " +
            "and f.friendship = :friendship")
    List<FriendshipEntity> findFriendshipEntities(@Param("username") String username,
                                                  @Param("friendship") Friendship relationship,
                                                  Pageable pageable);

    List<FriendshipEntity> findFriendshipEntitiesByUserOne_UsernameAndFriendship(String username, Friendship friendship);
    List<FriendshipEntity> findFriendshipEntitiesByUserTwo_UsernameAndFriendship(String username, Friendship friendship);

    @Query("delete from FriendshipEntity f " +
            "where (f.userOne.username = :userOne and f.userTwo.username = :userTwo) " +
            "or (f.userOne.username = :userTwo and f.userTwo.username = :userOne)")
    void removeFriendshipOfTwoUsers(@Param("userOne") String userOne,
                                    @Param("userTwo") String userTwo);

    @Query("SELECT f from FriendshipEntity f " +
            "where (f.userOne.username=:userTwo and f.userTwo.username=:userOne)")
    FriendshipEntity findFriendshipBetweenTwoUser(@Param("userOne") String usernameOne,
                                                  @Param("userTwo") String usernameTwo);

    @Query("select f from FriendshipEntity f " +
            "where (f.userOne.username=:username and f.userTwo.fullname like %:name%) " +
            "or (f.userTwo.username=:username and f.userOne.fullname like %:name%)")
    List<FriendshipEntity> findFriendsContainFullName(@Param("username") String usernameCurrent,
                                                      @Param("name") String fullName,
                                                      Pageable pageable);
}
