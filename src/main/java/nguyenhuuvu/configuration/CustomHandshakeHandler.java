package nguyenhuuvu.configuration;

import nguyenhuuvu.model.StompPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler{
    boolean a = true;
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (a) {
            a = false;
            return new StompPrincipal("nguyenhuuvu");
        }
        else
            return new StompPrincipal("nguyenhuuanh");
    }
}
