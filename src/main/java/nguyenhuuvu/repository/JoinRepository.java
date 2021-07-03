package nguyenhuuvu.repository;

import nguyenhuuvu.entity.JoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JoinRepository extends JpaRepository<JoinEntity, Long> {
    JoinEntity findJoinEntityByZoom_IdAndUser_Username(String zoomID, String username);
    // this list will return 2 items
    @Query("select j from JoinEntity j where (j.user.username = :usernameCurrent and j.zoom.id = :usernameRest) " +
            "or ((j.user.username = :usernameRest and j.zoom.id = :usernameCurrent))")
    List<JoinEntity> findJoinPrivateMessage(@Param("usernameCurrent") String usernameCurrent, @Param("usernameRest") String usernameRest);
}
