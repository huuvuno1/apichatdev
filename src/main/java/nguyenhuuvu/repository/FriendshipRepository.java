package nguyenhuuvu.repository;

import nguyenhuuvu.entity.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {
    @Query("SELECT f from FriendshipEntity f " +
            "where (f.userOne.username=:userTwo and f.userTwo.username=:userOne)")
    FriendshipEntity findFriendshipBetweenTwoUser(@Param("userOne") String usernameOne, @Param("userTwo") String usernameTwo);
}
