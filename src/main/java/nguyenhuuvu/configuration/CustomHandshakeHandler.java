package nguyenhuuvu.configuration;

import lombok.RequiredArgsConstructor;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.model.StompPrincipal;
import nguyenhuuvu.utils.Constant;
import nguyenhuuvu.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    final JwtTokenUtil jwtTokenUtil;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        Cookie cookie = WebUtils.getCookie(servletRequest, "token");
        if (cookie != null) {
            try {
                if (jwtTokenUtil.isTokenExpired(cookie.getValue()))
                    throw new UserHandleException("");
                String username = jwtTokenUtil.getUsernameFromToken(cookie.getValue());
                return new StompPrincipal(username);
            } catch (Exception e) {
            }
        }
        return new StompPrincipal(Constant.CODE_CHECK);
    }
}
