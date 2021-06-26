package nguyenhuuvu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MessageDTO {
    private String usernameSend;
    private String message;
    private Date timeSend;
    private String avatarUserSend;

    public static MessageDTOBuilder builder() {
        return new MessageDTOBuilder();
    }

    public static class MessageDTOBuilder {
        private String usernameSend;
        private String message;
        private Date timeSend;
        private String avatarUserSend;

        public MessageDTOBuilder withUsernameSend(String usernameSend) {
            this.usernameSend = usernameSend;
            return this;
        }

        public MessageDTOBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public MessageDTOBuilder withTimeSend(Date timeSend) {
            this.timeSend = timeSend;
            return this;
        }

        public MessageDTOBuilder withAvatarUserSend(String avatarUserSend) {
            this.avatarUserSend = avatarUserSend;
            return this;
        }

        public MessageDTO build() {
            return new MessageDTO(usernameSend, message, timeSend, avatarUserSend);
        }
    }
}
