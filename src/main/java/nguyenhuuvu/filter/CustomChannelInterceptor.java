package nguyenhuuvu.filter;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.lang.Strings;
import lombok.AllArgsConstructor;
import nguyenhuuvu.exception.UserHandleException;
import nguyenhuuvu.utils.Constant;
import nguyenhuuvu.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor
public class CustomChannelInterceptor implements ChannelInterceptor {
    final JwtTokenUtil jwtTokenUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String username = accessor.getUser().getName();
            // if client not use cookie
            if (username.equals(Constant.CODE_CHECK)) {
                String jwtToken = accessor.getFirstNativeHeader("token");
                if (Strings.hasText(jwtToken) && !jwtToken.equals("null")) {
                    try {
                        if (jwtTokenUtil.isTokenExpired(jwtToken))
                            throw new UserHandleException("Token expire!", HttpStatus.FORBIDDEN);
                        Authentication authentication = jwtTokenUtil.getAuthentication(jwtToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        accessor.setUser(authentication);
                    } catch (MalformedJwtException e) {
                        throw new UserHandleException("Bro định hack hay gì =))", HttpStatus.FORBIDDEN);
                    }
                } else {
                    throw new UserHandleException("Forbidden", HttpStatus.FORBIDDEN);
                }
            }
        }

        return message;
    }



}
