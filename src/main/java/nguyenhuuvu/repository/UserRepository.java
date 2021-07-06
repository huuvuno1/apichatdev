package nguyenhuuvu.repository;

import nguyenhuuvu.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findUserEntityByUsernameOrEmail(String username, String password);

    @Query("select u from UserEntity u where u.username = :username")
    UserEntity findUserEntityByUsername(@Param("username") String username);

    UserEntity findUserEntityByEmail(String email);

    @Query("SELECT DISTINCT u FROM UserEntity u LEFT JOIN FriendshipEntity f " +
            "ON (u.username = f.userOne.username or u.username = f.userTwo.username) " +
            "WHERE (f.id is null and u.username <> :username) or (f.userOne.username <> :username and f.userTwo.username <> :username)")
    List<UserEntity> findUserEntitiesNotFriendWithUser(@Param("username") String username,
                                                       Pageable pageable);

    @Query("SELECT e FROM UserEntity e where e.fullname like %:name% or e.email=:email")
    List<UserEntity> searchUsersLikeFullNameOrEqualEmail(@Param("name") String fullname, @Param("email") String email,
                                                         Pageable pageable);
}
