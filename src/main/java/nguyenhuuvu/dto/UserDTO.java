package nguyenhuuvu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nguyenhuuvu.enums.Friendship;
import nguyenhuuvu.enums.Gender;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String fullname;
    private Gender gender;
    private Date birthday;
    private String address;
    private Friendship friendship;

    public static AccountDTOBuilder builder()
    {
        return new AccountDTOBuilder();
    }

    public static class AccountDTOBuilder {
        private String username;
        private String email;
        private String fullname;
        private Gender gender;
        private Date birthday;
        private String address;
        private Friendship friendship;

        public AccountDTOBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public AccountDTOBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public AccountDTOBuilder withFullname(String firstname) {
            this.fullname = firstname;
            return this;
        }

        public AccountDTOBuilder withGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public AccountDTOBuilder withBirthday(Date birthday) {
            this.birthday = birthday;
            return this;
        }

        public AccountDTOBuilder withAddress(String address) {
            this.address = address;
            return this;
        }
        public AccountDTOBuilder withFriendship(Friendship friendship) {
            this.friendship = friendship;
            return this;
        }

        public UserDTO build()
        {
            return new UserDTO(username, email, fullname, gender, birthday, address, friendship);
        }
    }
}