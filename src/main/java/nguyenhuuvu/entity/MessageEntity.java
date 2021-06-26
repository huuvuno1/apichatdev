package nguyenhuuvu.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String message;
    private Date timeSend;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "join_id")
    private JoinEntity join;
}
