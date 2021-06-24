package nguyenhuuvu.repository;

import nguyenhuuvu.entity.FriendshipEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {
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
