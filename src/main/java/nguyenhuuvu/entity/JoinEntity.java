package nguyenhuuvu.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user_zoom")
public class JoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_send", referencedColumnName = "username")
    private UserEntity user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "zoom_id")
    private ZoomEntity zoom;

    @OneToMany(mappedBy = "join",cascade = CascadeType.ALL)
    private List<MessageEntity> messages;
}
