package nguyenhuuvu.repository;

import nguyenhuuvu.entity.MessageEntity;
import nguyenhuuvu.enums.ZoomType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    @Query("select m from MessageEntity m where m.join.zoom.zoomType = :zoomType and ((m.join.user.username = :userCurrent and m.join.zoom.id = :zoomID) " +
            "or (m.join.user.username = :zoomID and m.join.zoom.id = :userCurrent)) and m.timeSend < :date")
    List<MessageEntity> findPrivateMessages(@Param("userCurrent") String usernameCurrent,
                                            @Param("zoomID") String zoomID,
                                            @Param("zoomType") ZoomType zoomType,
                                            @Param("date") Date beforeTime,
                                            Pageable pageable);

    @Query("select m from MessageEntity m where (m.join.zoom.id = :zoomID) and m.timeSend < :date")
    List<MessageEntity> findMessagesInGroup(@Param("zoomID") String zoomID,
                                            @Param("date") Date beforeTime,
                                            Pageable pageable);
}
