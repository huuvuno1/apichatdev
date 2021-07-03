package nguyenhuuvu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import nguyenhuuvu.enums.Gender;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(columnList = "username", unique = true),
        @Index(columnList = "email", unique = true)
})
@Data
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @NotBlank(message = "Password is mandatory")
    @Length(min = 5)
    private String password;

    @NotBlank(message = "Email is mandatory")
    //@Email(message = "Invalid format")
    private String email;

    @NotBlank(message = "Fullname is mandatory")
    @Length(min = 5, max = 30)
    private String fullname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date birthday;

    private String address;
    private boolean enabled = false;

    private String avatar;

    public UserEntity(String username, String email, String fullname, Gender gender, Date birthday, String address, String avatar, List<RoleEntity> roles) {
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.avatar = avatar;
        this.roles = roles;
    }

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "verify_id")
    private VerifyEntity verifyEntity;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<JoinEntity> joins;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roles;

}
