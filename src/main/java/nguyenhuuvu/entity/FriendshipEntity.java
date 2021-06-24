package nguyenhuuvu.entity;

import lombok.Data;
import nguyenhuuvu.enums.Friendship;

import javax.persistence.*;

@Data
@Entity
@Table(name = "friendship")
public class FriendshipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_one")
    private UserEntity userOne;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_two")
    private UserEntity userTwo;

    private Friendship friendship;
}
