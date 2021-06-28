package nguyenhuuvu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import nguyenhuuvu.enums.Friendship;

import javax.persistence.*;

@Data
@Entity
@Table(name = "friendship", indexes = {@Index(columnList = "user_one,user_two", unique = true)})
@NoArgsConstructor
public class FriendshipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_one", referencedColumnName = "username")
    private UserEntity userOne;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_two", referencedColumnName = "username")
    private UserEntity userTwo;

    @Enumerated(EnumType.STRING)
    private Friendship friendship;
}
