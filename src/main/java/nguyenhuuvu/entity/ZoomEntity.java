package nguyenhuuvu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nguyenhuuvu.enums.ZoomType;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "zoom")
@NoArgsConstructor
@AllArgsConstructor
public class ZoomEntity {
    // in private message, id is username of account
    // else id is random value
    @Id
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ZoomType zoomType;
    // Accept to send
    private boolean active;

    @OneToMany(mappedBy = "zoom", cascade = CascadeType.ALL)
    private List<JoinEntity> joins;
}
