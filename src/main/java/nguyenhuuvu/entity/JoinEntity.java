package nguyenhuuvu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user_zoom")
@NoArgsConstructor
public class JoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_send", referencedColumnName = "username")
    private UserEntity user;

    private boolean seenMsgLatest = false;

    public JoinEntity(UserEntity user, boolean seenMsgLatest, ZoomEntity zoom, List<MessageEntity> messages) {
        this.user = user;
        this.seenMsgLatest = seenMsgLatest;
        this.zoom = zoom;
        this.messages = messages;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "zoom_id")
    private ZoomEntity zoom;

    @OneToMany(mappedBy = "join",cascade = CascadeType.ALL)
    private List<MessageEntity> messages;
}
