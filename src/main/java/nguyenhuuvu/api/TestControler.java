package nguyenhuuvu.api;

import lombok.AllArgsConstructor;
import nguyenhuuvu.service.UserService;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestControler {
    final UserService userService;

    @RequestMapping("/")
    public String fetchAccount()
    {
        return UserUtil.getUsernameFromCurrentRequest();
    }
}
