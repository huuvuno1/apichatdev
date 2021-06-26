package nguyenhuuvu.api;

import lombok.AllArgsConstructor;
import nguyenhuuvu.service.UserService;
import nguyenhuuvu.utils.UserUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@AllArgsConstructor
@CrossOrigin
public class TestControler {
    final UserService userService;

    @RequestMapping("/")
    public String fetchAccount()
    {
        return UserUtil.getUsernameFromCurrentRequest();
    }

    @RequestMapping("/api/v1/test")
    public ResponseEntity<?> fetch( HttpServletResponse response)
    {
        Cookie cookie = new Cookie("token", "abc");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new ResponseEntity<>("oke", HttpStatus.OK);
    }
}
