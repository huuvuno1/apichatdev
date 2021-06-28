package nguyenhuuvu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private String fromUser;
    private String avatar;
    private String content;
    private String link;
    private Date timeSend;
}
